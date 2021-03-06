/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtend.ide.formatting.preferences;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import static org.eclipse.xtend.core.formatting2.XtendFormatterPreferenceKeys.*;


/**
 * @author Dennis Huebner - Initial contribution and API
 */
@SuppressWarnings("restriction")
public class WhiteSpaceTab extends AbstractModifyDialogTab {

	public WhiteSpaceTab(IModificationListener modifyListener, Map<String, String> workingValues) {
		super(modifyListener, workingValues);
	}

	@Override
	protected String previewText() {
		return PreviewCode.bracesPreviewText();
	}

	@Override
	protected void doCreatePreferences(Composite composite, int col) {
		Group wsGr = createGroup(col, composite, "Insert space:");
		createCheckboxPref(wsGr, col, "Between keyword and parenthesis single line", whitespaceBetweenKeywordAndParenthesisSL);
		createCheckboxPref(wsGr, col, "Between keyword and parenthesis multiline", whitespaceBetweenKeywordAndParenthesisML);
	}
}
