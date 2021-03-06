package org.eclipse.xtext.enumrules.idea;

import org.eclipse.xtext.enumrules.idea.lang.EnumRulesTestLanguageLanguage;

import com.intellij.openapi.extensions.ExtensionFactory;

public class EnumRulesTestLanguageExtensionFactory implements ExtensionFactory {

	public Object createInstance(final String factoryArgument, final String implementationClass) {
		Class<?> clazz;
		try {
			clazz = Class.forName(implementationClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Couldn't load "+implementationClass, e);
		}
		return EnumRulesTestLanguageLanguage.INSTANCE.<Object> getInstance(clazz);
	}

}
