/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtend.ide.quickfix;

import static org.eclipse.xtext.ui.util.DisplayRunHelper.*;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtend.core.jvmmodel.IXtendJvmAssociations;
import org.eclipse.xtend.core.xtend.XtendClass;
import org.eclipse.xtend.ide.codebuilder.AbstractAnnotationBuilder;
import org.eclipse.xtend.ide.codebuilder.AbstractClassBuilder;
import org.eclipse.xtend.ide.codebuilder.CodeBuilderFactory;
import org.eclipse.xtend.ide.wizards.NewXtendClassWizard;
import org.eclipse.xtend.ide.wizards.NewXtendClassWizardPage;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.ui.refactoring.impl.ProjectUtil;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.annotations.xAnnotations.XAnnotationsPackage;
import org.eclipse.xtext.xbase.ui.quickfix.ILinkingIssueQuickfixProvider;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Quickfixes to create new Xtend types.
 * 
 * @author Jan Koehnlein - Initial contribution and API
 */
@NonNullByDefault
public class CreateXtendTypeQuickfixes implements ILinkingIssueQuickfixProvider {

	private static final Logger LOG = Logger.getLogger(CreateXtendTypeQuickfixes.class);
	
	@Inject 
	private ProjectUtil projectUtil;

	@Inject
	private Provider<NewXtendClassWizard> newXtendClassWizardProvider;
	
	@Inject
	private IXtendJvmAssociations associations;
	
	@Inject
	private CodeBuilderFactory codeBuilderFactory;
	
	@Inject
	private CodeBuilderQuickfix codeBuilderQuickfix;

	public void addQuickfixes(Issue issue, IssueResolutionAcceptor issueResolutionAcceptor,
			IXtextDocument xtextDocument, XtextResource resource, 
			EObject referenceOwner, EReference unresolvedReference)
			throws Exception {
		String typeName = xtextDocument.get(issue.getOffset(), issue.getLength());
		if (unresolvedReference == XbasePackage.Literals.XCONSTRUCTOR_CALL__CONSTRUCTOR) {
			if(((XConstructorCall)referenceOwner).getConstructor().eIsProxy()) {
				newXtendClassQuickfix(typeName, resource, issue, issueResolutionAcceptor);
				newLocalXtendClassQuickfix(typeName, resource, issue, issueResolutionAcceptor);
			}
		} else if(unresolvedReference == XbasePackage.Literals.XTYPE_LITERAL__TYPE
				|| unresolvedReference == TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE) {
			newXtendClassQuickfix(typeName, resource, issue, issueResolutionAcceptor);
			newLocalXtendClassQuickfix(typeName, resource, issue, issueResolutionAcceptor);
		} else if(unresolvedReference == XAnnotationsPackage.Literals.XANNOTATION__ANNOTATION_TYPE) {
			newLocalXtendAnnotationQuickfix(typeName, resource, issue, issueResolutionAcceptor);
		}
	}

	protected void newLocalXtendClassQuickfix(String typeName, XtextResource resource, Issue issue,
			IssueResolutionAcceptor issueResolutionAcceptor) {
		EObject eObject = resource.getEObject(issue.getUriToProblem().fragment());
		XtendClass xtendClass = EcoreUtil2.getContainerOfType(eObject, XtendClass.class);
		if(xtendClass != null) {
			JvmGenericType inferredType = associations.getInferredType(xtendClass);
			if(inferredType != null) {
				AbstractClassBuilder classBuilder = codeBuilderFactory.createClassBuilder(inferredType);
				classBuilder.setClassName(typeName);
				classBuilder.setVisibility(JvmVisibility.PUBLIC);
				classBuilder.setContext(xtendClass);
				codeBuilderQuickfix.addQuickfix(classBuilder, "Create local Xtend class '" + typeName + "'", issue, issueResolutionAcceptor);
			}
		}
	}

	protected void newLocalXtendAnnotationQuickfix(String typeName, XtextResource resource, Issue issue,
			IssueResolutionAcceptor issueResolutionAcceptor) {
		EObject eObject = resource.getEObject(issue.getUriToProblem().fragment());
		XtendClass xtendClass = EcoreUtil2.getContainerOfType(eObject, XtendClass.class);
		if(xtendClass != null) {
			JvmGenericType inferredType = associations.getInferredType(xtendClass);
			if(inferredType != null) {
				AbstractAnnotationBuilder annotationBuilder = codeBuilderFactory.createAnnotationBuilder(inferredType);
				annotationBuilder.setAnnotationName(typeName);
				annotationBuilder.setVisibility(JvmVisibility.PUBLIC);
				annotationBuilder.setContext(xtendClass);
				codeBuilderQuickfix.addQuickfix(annotationBuilder, "Create local Xtend annotation '@" + typeName + "'", issue, issueResolutionAcceptor);
			}
		}
	}

	protected void newXtendClassQuickfix(final String typeName, final XtextResource resource, Issue issue,
			IssueResolutionAcceptor issueResolutionAcceptor) {
		issueResolutionAcceptor.accept(issue, "Create Xtend class '" + typeName + "'",
				"Opens the new Xtend class wizard to create the type '" + typeName + "'", "xtend_file.png",
				new IModification() {
					public void apply(@Nullable IModificationContext context) throws Exception {
						runAsyncInDisplayThread(new Runnable() {
							public void run() {
								NewElementWizard newXtendClassWizard = newXtendClassWizardProvider.get();
								WizardDialog dialog = createWizardDialog(newXtendClassWizard);
								NewXtendClassWizardPage page = (NewXtendClassWizardPage) newXtendClassWizard.getStartingPage();
								configureWizardPage(page, resource.getURI(), typeName);
								dialog.open();
							}
						});
					}
				});
	}

	protected WizardDialog createWizardDialog(NewElementWizard newXtendClassWizard) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		newXtendClassWizard.init(workbench, new StructuredSelection());
		WizardDialog dialog = new WizardDialog(shell, newXtendClassWizard);
		dialog.create();
		return dialog;
	}

	
	protected void configureWizardPage(NewTypeWizardPage page, URI contextUri, String typeName) {
		setPackageName(page, contextUri);
		page.setTypeName(typeName, true);
	}

	protected void setPackageName(NewTypeWizardPage page, URI contextUri) {
		IJavaProject javaProject = getJavaProject(contextUri);
		String path = contextUri.trimSegments(1).toPlatformString(true);
		try {
			if(javaProject != null) {
				IPackageFragment packageFragment = javaProject.findPackageFragment(new Path(path));
				IPackageFragmentRoot root = (IPackageFragmentRoot) packageFragment
						.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
				page.setPackageFragment(packageFragment, true);
				page.setPackageFragmentRoot(root, true);
			}
		} catch (JavaModelException e) {
			LOG.error("Could not find package for " + path, e);
		}
	}

	@Nullable
	protected IJavaProject getJavaProject(URI uri){
		IProject project = projectUtil.getProject(uri);
		if(project == null){
			return null;
		}
		return JavaCore.create(project);
	}

}