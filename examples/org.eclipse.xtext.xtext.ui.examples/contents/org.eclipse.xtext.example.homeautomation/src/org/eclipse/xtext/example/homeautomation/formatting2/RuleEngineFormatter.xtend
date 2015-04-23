/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.example.homeautomation.formatting2;

import org.eclipse.xtext.example.homeautomation.ruleEngine.Declaration
import org.eclipse.xtext.example.homeautomation.ruleEngine.Device
import org.eclipse.xtext.example.homeautomation.ruleEngine.Model
import org.eclipse.xtext.example.homeautomation.ruleEngine.Rule
import org.eclipse.xtext.example.homeautomation.ruleEngine.State
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XSwitchExpression
import org.eclipse.xtext.xbase.formatting2.XbaseFormatter

import static org.eclipse.xtext.example.homeautomation.ruleEngine.RuleEnginePackage.Literals.*
import static org.eclipse.xtext.xbase.XbasePackage.Literals.*

/**
 * The formatter is particularly important for languages with semantic whitespace, since it is responsible
 * for producing correct whitespace that reflects the semantic structure. This formatter actually modifies
 * the structure by converting single expressions in control statements to block expressions. For instance,
 * <pre>
 *     if (condition) println
 * </pre>
 * becomes
 * <pre>
 *     if (condition)
 *         println
 * </pre>
 */
class RuleEngineFormatter extends XbaseFormatter {

	def dispatch void format(Model model, extension IFormattableDocument document) {
		model.prepend[setNewLines(0, 0, 1); noSpace]
		for (Declaration declaration : model.getDeclarations()) {
			format(declaration, document);
			declaration.append[setNewLines(1, 1, 2)]
		}
	}

	def dispatch void format(Device device, extension IFormattableDocument document) {
		device.regionFor.feature(DEVICE__NAME).surround[oneSpace]
		device.regionFor.keyword("be").surround[oneSpace]
		for (State state : device.getStates()) {
			state.immediatelyPreceding.keyword(",").prepend[noSpace].append[oneSpace]
			format(state, document);
		}
	}

	def dispatch void format(Rule rule, extension IFormattableDocument document) {
		rule.regionFor.feature(RULE__DESCRIPTION).surround[oneSpace]
		rule.regionFor.feature(RULE__DEVICE_STATE).surround[oneSpace]
		rule.thenPart.prepend[newLine]
		format(rule.thenPart, document);
	}

	override dispatch void format(XBlockExpression expr, extension IFormattableDocument document) {
		expr.surround[indent]
		for (child : expr.expressions) {
			val sem = child.immediatelyFollowing.keyword(";")
			if (sem != null) {
				sem.prepend[noSpace]
				if (child != expr.expressions.last)
					sem.append[newLine]
			} else if (child != expr.expressions.last)
				child.append[newLine]
			child.format(document)
		}
	}

	override dispatch void format(XSwitchExpression expr, extension IFormattableDocument document) {
		set(expr.^switch.previousHiddenRegion, expr.nextHiddenRegion)[indent]
		expr.regionFor.keyword("switch").append[oneSpace]
		expr.^switch.append[newLine].format(document)
		for (c : expr.cases) {
			if (c.typeGuard != null && c.^case != null) {
				c.typeGuard.append[oneSpace]
				c.^case.append[noSpace]
			} else if (c.typeGuard != null) {
				c.typeGuard.append[noSpace]
			} else if (c.^case != null) {
				c.^case.prepend[oneSpace].append[noSpace]
			}
			c.regionFor.feature(XCASE_PART__FALL_THROUGH).prepend[noSpace].append[newLine]
			c.^case.format(document)
			if (c == expr.cases.last && expr.^default == null)
				c.then.formatBody(true, document)
			else
				c.then.formatBodyParagraph(document)
		}
		if (expr.^default != null) {
			expr.regionFor.keyword("default").append[noSpace]
			expr.^default.formatBody(true, document)
		}
	}

	override protected void formatBody(XExpression expr, boolean forceMultiline, extension IFormattableDocument doc) {
		if (expr == null)
			return;
		if (expr instanceof XBlockExpression) {
			expr.prepend[newLine]
		} else if (forceMultiline || expr.previousHiddenRegion.isMultiline) {
			expr.prepend[newLine].surround[indent]
		} else {
			expr.prepend[oneSpace]
		}
		expr.format(doc)
	}

	override protected void formatBodyInline(XExpression expr, boolean forceMultiline,
		extension IFormattableDocument doc) {
		if (expr == null)
			return;
		if (expr instanceof XBlockExpression) {
			expr.surround[newLine]
		} else if (forceMultiline || expr.previousHiddenRegion.isMultiline) {
			expr.prepend[newLine].surround[indent].append[newLine]
		} else {
			expr.surround[oneSpace]
		}
		expr.format(doc)
	}

	override protected void formatBodyParagraph(XExpression expr, extension IFormattableDocument doc) {
		if (expr == null)
			return;
		if (expr instanceof XBlockExpression) {
			expr.surround[newLine]
		} else {
			expr.surround[oneSpace]
		}
		expr.format(doc)
	}

}
