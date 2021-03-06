/*
 * generated by Xtext
 */
grammar PsiInternalFormatterTestLanguage;

options {
	superClass=AbstractPsiAntlrParser;
}

@lexer::header {
package org.eclipse.xtext.formatting2.internal.idea.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package org.eclipse.xtext.formatting2.internal.idea.parser.antlr.internal;

import org.eclipse.xtext.idea.parser.AbstractPsiAntlrParser;
import org.eclipse.xtext.formatting2.internal.idea.lang.FormatterTestLanguageElementTypeProvider;
import org.eclipse.xtext.idea.parser.TokenTypeProvider;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.formatting2.internal.services.FormatterTestLanguageGrammarAccess;

import com.intellij.lang.PsiBuilder;
}

@parser::members {

	protected FormatterTestLanguageGrammarAccess grammarAccess;

	protected FormatterTestLanguageElementTypeProvider elementTypeProvider;

	public PsiInternalFormatterTestLanguageParser(PsiBuilder builder, TokenStream input, FormatterTestLanguageElementTypeProvider elementTypeProvider, FormatterTestLanguageGrammarAccess grammarAccess) {
		this(input);
		setPsiBuilder(builder);
    	this.grammarAccess = grammarAccess;
		this.elementTypeProvider = elementTypeProvider;
	}

	@Override
	protected String getFirstRuleName() {
		return "Root";
	}

}

//Entry rule entryRuleRoot
entryRuleRoot:
	{ markComposite(elementTypeProvider.getRootElementType()); }
	ruleRoot
	EOF;

// Rule Root
ruleRoot:
	(
		{
			markComposite(elementTypeProvider.getRoot_IDListParserRuleCall_0ElementType());
		}
		ruleIDList
		{
			doneComposite();
		}
		    |
		{
			markComposite(elementTypeProvider.getRoot_KWListParserRuleCall_1ElementType());
		}
		ruleKWList
		{
			doneComposite();
		}
	)
;

//Entry rule entryRuleIDList
entryRuleIDList:
	{ markComposite(elementTypeProvider.getIDListElementType()); }
	ruleIDList
	EOF;

// Rule IDList
ruleIDList:
	(
		(
			{
				precedeComposite(elementTypeProvider.getIDList_IDListAction_0ElementType());
				doneComposite();
			}
		)
		{
			markLeaf(elementTypeProvider.getIDList_IdlistKeyword_1ElementType());
		}
		otherlv_1='idlist'
		{
			doneLeaf(otherlv_1);
		}
		(
			(
				{
					markLeaf(elementTypeProvider.getIDList_IdsIDTerminalRuleCall_2_0ElementType());
				}
				lv_ids_2_0=RULE_ID
				{
					doneLeaf(lv_ids_2_0);
				}
			)
		)*
	)
;

//Entry rule entryRuleKWList
entryRuleKWList:
	{ markComposite(elementTypeProvider.getKWListElementType()); }
	ruleKWList
	EOF;

// Rule KWList
ruleKWList:
	(
		(
			{
				precedeComposite(elementTypeProvider.getKWList_KWListAction_0ElementType());
				doneComposite();
			}
		)
		{
			markLeaf(elementTypeProvider.getKWList_KwlistKeyword_1ElementType());
		}
		otherlv_1='kwlist'
		{
			doneLeaf(otherlv_1);
		}
		(
			(
				{
					markLeaf(elementTypeProvider.getKWList_Kw1Kw1Keyword_2_0ElementType());
				}
				lv_kw1_2_0='kw1'
				{
					doneLeaf(lv_kw1_2_0);
				}
			)
		)?
		(
			(
				{
					markLeaf(elementTypeProvider.getKWList_Kw2Kw2Keyword_3_0ElementType());
				}
				lv_kw2_3_0='kw2'
				{
					doneLeaf(lv_kw2_3_0);
				}
			)
		)?
		(
			(
				{
					markLeaf(elementTypeProvider.getKWList_Kw3Kw3Keyword_4_0ElementType());
				}
				lv_kw3_4_0='kw3'
				{
					doneLeaf(lv_kw3_4_0);
				}
			)
		)?
		(
			(
				{
					markLeaf(elementTypeProvider.getKWList_Kw4Kw4Keyword_5_0ElementType());
				}
				lv_kw4_5_0='kw4'
				{
					doneLeaf(lv_kw4_5_0);
				}
			)
		)?
		(
			(
				{
					markLeaf(elementTypeProvider.getKWList_Kw5Kw5Keyword_6_0ElementType());
				}
				lv_kw5_6_0='kw5'
				{
					doneLeaf(lv_kw5_6_0);
				}
			)
		)?
	)
;

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' .|~(('\\'|'"')))* '"'|'\'' ('\\' .|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;
