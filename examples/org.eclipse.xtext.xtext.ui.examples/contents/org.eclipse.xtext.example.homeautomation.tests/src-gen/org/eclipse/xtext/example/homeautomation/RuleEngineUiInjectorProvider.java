/*
 * generated by Xtext
 */
package org.eclipse.xtext.example.homeautomation;

import org.eclipse.xtext.junit4.IInjectorProvider;

import com.google.inject.Injector;

public class RuleEngineUiInjectorProvider implements IInjectorProvider {
	
	@Override
	public Injector getInjector() {
		return org.eclipse.xtext.example.homeautomation.ui.internal.RuleEngineActivator.getInstance().getInjector("org.eclipse.xtext.example.homeautomation.RuleEngine");
	}
	
}
