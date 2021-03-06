package org.eclipse.xtext.testlanguages.backtracking.idea;

import org.eclipse.xtext.util.Modules2;
import org.eclipse.xtext.testlanguages.backtracking.SimpleBeeLangTestLanguageStandaloneSetupGenerated;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class SimpleBeeLangTestLanguageStandaloneSetupIdea extends SimpleBeeLangTestLanguageStandaloneSetupGenerated {

    @Override
    public Injector createInjector() {
        Module runtimeModule = new org.eclipse.xtext.testlanguages.backtracking.SimpleBeeLangTestLanguageRuntimeModule();
        Module ideaModule = new org.eclipse.xtext.testlanguages.backtracking.idea.SimpleBeeLangTestLanguageIdeaModule();
        Module mergedModule = Modules2.mixin(runtimeModule, ideaModule);
        return Guice.createInjector(mergedModule);
    }

}
