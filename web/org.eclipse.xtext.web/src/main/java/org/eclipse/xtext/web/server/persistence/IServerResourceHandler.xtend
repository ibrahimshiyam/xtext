/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.web.server.persistence

import com.google.inject.ImplementedBy
import java.io.IOException
import org.eclipse.xtext.web.server.model.IXtextWebDocument

@ImplementedBy(NullImpl)
interface IServerResourceHandler {
	
	def IXtextWebDocument get(String resourceId) throws IOException
	
	def void put(IXtextWebDocument document) throws IOException
	
	static class NullImpl implements IServerResourceHandler {
		override get(String resourceId) throws IOException {
			throw new IOException("This server does not support resource handling.")
		}
		override put(IXtextWebDocument document) throws IOException {
			throw new IOException("This server does not support resource handling.")
		}
	}
	
}