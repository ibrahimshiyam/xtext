/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.web.server.validation

import com.google.inject.Inject
import com.google.inject.Singleton
import org.eclipse.xtext.diagnostics.Severity
import org.eclipse.xtext.validation.CheckMode
import org.eclipse.xtext.validation.IResourceValidator
import org.eclipse.xtext.web.server.InvalidRequestException
import org.eclipse.xtext.web.server.model.XtextWebDocumentAccess

@Singleton
class ValidationService {
	
	@Inject IResourceValidator resourceValidator
	
	def validate(XtextWebDocumentAccess document) throws InvalidRequestException {
		val issues = document.readOnly[ it, cancelIndicator |
			resourceValidator.validate(resource, CheckMode.ALL, cancelIndicator)
		]
		val result = new ValidationResult
		issues.filter[severity != Severity.IGNORE].forEach[ issue |
			result.entries += new ValidationResult.Entry(issue.message, issue.severity.translate,
				issue.lineNumber + 1, issue.offset, issue.offset + issue.length)
		]
		return result
	}
	
	protected def translate(Severity severity){
		switch severity {
			case WARNING : "warning"
			case ERROR : "error"
			case INFO : "info"
			default: "ignore"
		}
	}
	
}