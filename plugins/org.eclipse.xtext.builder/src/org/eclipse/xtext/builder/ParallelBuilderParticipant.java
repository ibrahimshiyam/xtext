package org.eclipse.xtext.builder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2.IFileCallback;
import org.eclipse.xtext.generator.FileSystemAccessQueue;
import org.eclipse.xtext.generator.FileSystemAccessRequest;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author Anton Kosyakov - Initial contribution and API
 * @since 2.7
 */
public class ParallelBuilderParticipant extends BuilderParticipant {
	
	private static final int QUEUE_CAPACITY = 50;
	
	private static final int QUEUE_POLL_TIMEOUT = 50;

	@Override
	protected void handleChangedContents(Delta delta, IBuildContext context, IFileSystemAccess access)
			throws CoreException {
		Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
		saveResourceStorage(resource, access);
		if (shouldGenerate(resource, context)) {
			getGenerator().doGenerate(resource, access);
		}
	}
	
	@Override
	protected void doBuild(
			List<Delta> deltas, 
			Map<String, OutputConfiguration> outputConfigurations,
			Map<OutputConfiguration, Iterable<IMarker>> generatorMarkers, 
			IBuildContext context,
			EclipseResourceFileSystemAccess2 access, 
			IProgressMonitor progressMonitor) throws CoreException {
		BlockingQueue<FileSystemAccessRequest> requestQueue = newBlockingQueue(QUEUE_CAPACITY);
		FileSystemAccessQueue fileSystemAccessQueue = new FileSystemAccessQueue(requestQueue, progressMonitor);
		context.getResourceSet().eAdapters().add(fileSystemAccessQueue);
		try {
			SubMonitor subMonitor = SubMonitor.convert(progressMonitor, 1);
			subMonitor.subTask("Compiling...");
			access.setMonitor(subMonitor.newChild(1));
			
			int nProcessors = Runtime.getRuntime().availableProcessors();
			int nThreads = Math.max(2, Math.min(4, nProcessors));
			ExecutorService executorService = Executors.newFixedThreadPool(nThreads, new ThreadFactoryBuilder().setNameFormat("ParallelGenerator-%d").build());
	
			for (IResourceDescription.Delta delta : deltas) {
				try {
					Runnable runnable = createRunnable(delta, context, outputConfigurations, generatorMarkers, fileSystemAccessQueue, access, subMonitor);
					executorService.execute(runnable);
				} catch (Exception e) {
					addMarkerAndLogError(delta.getUri(), e);
				}
			}
			executorService.shutdown();
			
			List<org.eclipse.xtext.xbase.lib.Pair<URI, Throwable>> exceptions = Lists.newArrayList();
			boolean interrupted = false;
			try {
				while (!requestQueue.isEmpty() || !executorService.isTerminated()) {
					if (subMonitor.isCanceled()) {
						cancelProcessing(requestQueue, executorService);
						throw new OperationCanceledException();
					}
	
					FileSystemAccessRequest request = null;
					try {
						request = requestQueue.poll(QUEUE_POLL_TIMEOUT, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						interrupted = true;
					}
					if (request != null) {
						try {
							request.getProcedure().apply();
						} catch (OperationCanceledException e) {
							cancelProcessing(requestQueue, executorService);
							throw e;
						} catch (Exception e) {
							Throwable cause = e;
							if (cause instanceof CoreException) {
								cause = cause.getCause();
							}
							exceptions.add(org.eclipse.xtext.xbase.lib.Pair.of(request.getUri(), cause));
						}
					}
				}
			} finally {
				if (interrupted) {
					Thread.currentThread().interrupt();
				}
				for (org.eclipse.xtext.xbase.lib.Pair<URI, Throwable> exception : exceptions) {
					addMarkerAndLogError(exception.getKey(), exception.getValue());
				}
			}
		} finally {
			context.getResourceSet().eAdapters().remove(fileSystemAccessQueue);
		}
	}

	private void cancelProcessing(BlockingQueue<FileSystemAccessRequest> requestQueue, ExecutorService executorService) {
		// make sure waiting put on the queue are processed by freeing space in the queue
		requestQueue.clear();
		// stop processing of resources immediately
		executorService.shutdownNow();
	}

	protected Runnable createRunnable(
			final IResourceDescription.Delta delta, 
			final IBuildContext context,
			final Map<String, OutputConfiguration> outputConfigurations, 
			final Map<OutputConfiguration, Iterable<IMarker>> generatorMarkers,
			final FileSystemAccessQueue fileSystemAccessQueue, 
			final IFileSystemAccess delegate, 
			final IProgressMonitor progressMonitor) {
		return new Runnable() {

			@Override
			public void run() {
				try {
					Set<IFile> derivedResources = getDerivedResources(delta, outputConfigurations, generatorMarkers);

					IFileSystemAccess fsa = getParalleFileSystemAccess(delta, context, derivedResources, fileSystemAccessQueue, delegate);
					boolean generated = doGenerate(delta, context, fsa);
					
					fileSystemAccessQueue.sendAsync(delta.getUri(), getFlushAndCleanDerivedResourcesCallback(delta, derivedResources, context, generated, delegate, progressMonitor));
				} catch (OperationCanceledException e)  {
					// do nothing 
				} catch (Throwable e) {
					addMarkerAndLogError(delta.getUri(), e);
				}
			}

		};
	}

	protected <E> BlockingQueue<E> newBlockingQueue(int capacity) {
		return new LinkedBlockingQueue<E>(capacity);
	}

	protected Procedure0 getFlushAndCleanDerivedResourcesCallback(
			final Delta delta,
			final Set<IFile> derivedResources, 
			final IBuildContext context, 
			final boolean generated, 
			final IFileSystemAccess delegate, 
			final IProgressMonitor progressMonitor) {
		return new Procedure0() {

			@Override
			public void apply() {
				try {
					if (delegate instanceof EclipseResourceFileSystemAccess2) {
						EclipseResourceFileSystemAccess2 eclipseAccess = (EclipseResourceFileSystemAccess2) delegate;
						if (generated) {
							eclipseAccess.flushSourceTraces();
						}
						cleanDerivedResources(delta, derivedResources, context, eclipseAccess, progressMonitor);
					}
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	protected IFileSystemAccess getParalleFileSystemAccess(final IResourceDescription.Delta delta,
			final IBuildContext context, Set<IFile> derivedResources, FileSystemAccessQueue fileSystemAccessQueue, IFileSystemAccess delegate) {
		String currentSourceFolder = getCurrentSourceFolder(context, delta);
		IFileCallback postProcessor = getPostProcessor(delta, context, derivedResources);
		return new ParallelFileSystemAccess(delegate, delta, fileSystemAccessQueue, currentSourceFolder, postProcessor);
	}

}
