/*
* generated by Xtext
*/
grammar DebugInternalIndentationAwareTestLanguage ;

// Rule NodeList
ruleNodeList :
	ruleNode (
		RULE_NL ruleNode
	)*
;

// Rule Node
ruleNode :
	ruleString (
		RULE_NL RULE_BEGIN ruleNodeList RULE_END
	)?
;

// Rule String
ruleString :
	RULE_OTHER+
;

RULE_NL :
	(
		'\r' |
		'\n'
	)+ '\t'*
;

RULE_BEGIN :
	'{'
;

RULE_END :
	'}'
;

RULE_OTHER :
	.
;