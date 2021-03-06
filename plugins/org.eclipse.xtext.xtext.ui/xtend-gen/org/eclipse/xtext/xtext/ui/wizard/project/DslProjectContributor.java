package org.eclipse.xtext.xtext.ui.wizard.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.ui.util.IProjectFactoryContributor;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xtext.ui.wizard.project.DefaultProjectFactoryContributor;
import org.eclipse.xtext.xtext.ui.wizard.project.XtextProjectInfo;

/**
 * Contributes a workflow file and the grammar to the new DSL project
 * @author Dennis Huebner - Initial contribution and API
 * @since 2.3
 */
@SuppressWarnings("all")
public class DslProjectContributor extends DefaultProjectFactoryContributor {
  private XtextProjectInfo projectInfo;
  
  private String sourceRoot;
  
  public DslProjectContributor(final XtextProjectInfo projectInfo) {
    this.projectInfo = projectInfo;
  }
  
  public void setSourceRoot(final String sourceRoot) {
    this.sourceRoot = sourceRoot;
  }
  
  @Override
  public void contributeFiles(final IProject project, final IProjectFactoryContributor.IFileCreator creator) {
    try {
      String _defaultCharset = project.getDefaultCharset();
      CharSequence _workflow = this.workflow(_defaultCharset);
      String _basePackagePath = this.projectInfo.getBasePackagePath();
      String _plus = ((this.sourceRoot + "/") + _basePackagePath);
      String _plus_1 = (_plus + "/Generate");
      String _languageNameAbbreviation = this.projectInfo.getLanguageNameAbbreviation();
      String _plus_2 = (_plus_1 + _languageNameAbbreviation);
      String _plus_3 = (_plus_2 + ".mwe2");
      creator.writeToFile(_workflow, _plus_3);
      CharSequence _grammar = this.grammar();
      String _grammarFilePath = this.projectInfo.getGrammarFilePath();
      String _plus_4 = ((this.sourceRoot + "/") + _grammarFilePath);
      creator.writeToFile(_grammar, _plus_4);
      CharSequence _wfLaunchConfig = this.wfLaunchConfig();
      String _projectName = this.projectInfo.getProjectName();
      String _plus_5 = (".launch/Generate Language Infrastructure (" + _projectName);
      String _plus_6 = (_plus_5 + ").launch");
      creator.writeToFile(_wfLaunchConfig, _plus_6);
      boolean _and = false;
      boolean _isCreateEclipseRuntimeLaunchConfig = this.projectInfo.isCreateEclipseRuntimeLaunchConfig();
      if (!_isCreateEclipseRuntimeLaunchConfig) {
        _and = false;
      } else {
        boolean _isCreateUiProject = this.projectInfo.isCreateUiProject();
        _and = _isCreateUiProject;
      }
      if (_and) {
        CharSequence _launchConfig = this.launchConfig();
        creator.writeToFile(_launchConfig, ".launch/Launch Runtime Eclipse.launch");
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private CharSequence workflow(final String encoding) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("module ");
    String _basePackagePath = this.projectInfo.getBasePackagePath();
    String _plus = (_basePackagePath + "/Generate");
    String _languageNameAbbreviation = this.projectInfo.getLanguageNameAbbreviation();
    String _plus_1 = (_plus + _languageNameAbbreviation);
    String _replaceAll = _plus_1.replaceAll("/", ".");
    _builder.append(_replaceAll, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import org.eclipse.emf.mwe.utils.*");
    _builder.newLine();
    _builder.append("import org.eclipse.xtext.generator.*");
    _builder.newLine();
    _builder.append("import org.eclipse.xtext.ui.generator.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("var grammarURI = \"classpath:/");
    String _basePackagePath_1 = this.projectInfo.getBasePackagePath();
    _builder.append(_basePackagePath_1, "");
    _builder.append("/");
    String _languageNameAbbreviation_1 = this.projectInfo.getLanguageNameAbbreviation();
    _builder.append(_languageNameAbbreviation_1, "");
    _builder.append(".xtext\"");
    _builder.newLineIfNotEmpty();
    _builder.append("var fileExtensions = \"");
    String _fileExtension = this.projectInfo.getFileExtension();
    _builder.append(_fileExtension, "");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("var projectName = \"");
    String _projectName = this.projectInfo.getProjectName();
    _builder.append(_projectName, "");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("var runtimeProject = \"../${projectName}\"");
    _builder.newLine();
    _builder.append("var generateXtendStub = true");
    _builder.newLine();
    _builder.append("var encoding = \"");
    _builder.append(encoding, "");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("var fileHeader = \"/*\\n * generated by Xtext \\${version}\\n */\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("Workflow {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("bean = StandaloneSetup {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("scanClassPath = true");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("platformUri = \"${runtimeProject}/..\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// The following two lines can be removed, if Xbase is not used.");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("registerGeneratedEPackage = \"org.eclipse.xtext.xbase.XbasePackage\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("registerGenModelFile = \"platform:/resource/org.eclipse.xtext.xbase/model/Xbase.genmodel\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("component = DirectoryCleaner {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("directory = \"${runtimeProject}/src-gen\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("component = DirectoryCleaner {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("directory = \"${runtimeProject}/model/generated\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      boolean _isCreateUiProject = this.projectInfo.isCreateUiProject();
      if (_isCreateUiProject) {
        _builder.append("\t");
        _builder.append("component = DirectoryCleaner {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("directory = \"${runtimeProject}.ui/src-gen\"");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    {
      boolean _isCreateTestProject = this.projectInfo.isCreateTestProject();
      if (_isCreateTestProject) {
        _builder.append("\t");
        _builder.append("component = DirectoryCleaner {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("directory = \"${runtimeProject}.tests/src-gen\"");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("component = Generator auto-inject {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("pathRtProject = runtimeProject");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("projectNameRt = projectName");
    _builder.newLine();
    {
      boolean _isCreateUiProject_1 = this.projectInfo.isCreateUiProject();
      if (_isCreateUiProject_1) {
        _builder.append("\t\t");
        _builder.append("pathUiProject = \"${runtimeProject}.ui\"");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("projectNameUi = \"${projectName}.ui\"");
        _builder.newLine();
      }
    }
    {
      boolean _isCreateTestProject_1 = this.projectInfo.isCreateTestProject();
      if (_isCreateTestProject_1) {
        _builder.append("\t\t");
        _builder.append("pathTestProject = \"${runtimeProject}.tests\"");
        _builder.newLine();
      }
    }
    _builder.append("\t\t");
    _builder.append("language = auto-inject {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("uri = grammarURI");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// Java API to access grammar elements (required by several other fragments)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = grammarAccess.GrammarAccessFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// generates Java API for the generated EPackages");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = ecore.EMFGeneratorFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = serializer.SerializerFragment auto-inject {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("generateStub = false");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// a custom ResourceFactory for use with EMF");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = resourceFactory.ResourceFactoryFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// The antlr parser generator fragment.");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = parser.antlr.XtextAntlrGeneratorFragment auto-inject {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//  options = {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//      backtrack = true");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//  }");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// Xtend-based API for validation");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = validation.ValidatorFragment auto-inject {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//    composedCheck = \"org.eclipse.xtext.validation.ImportUriValidator\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//    composedCheck = \"org.eclipse.xtext.validation.NamesAreUniqueValidator\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// scoping and exporting API");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = scoping.ImportNamespacesScopingFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = exporting.QualifiedNamesFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// generator API");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = generator.GeneratorFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("// formatter API");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fragment = formatting.FormatterFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.newLine();
    {
      boolean _isCreateTestProject_2 = this.projectInfo.isCreateTestProject();
      if (_isCreateTestProject_2) {
        _builder.append("\t\t\t");
        _builder.append("// generates junit test support classes into Generator#pathTestProject");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = junit.Junit4Fragment auto-inject {}");
        _builder.newLine();
      }
    }
    _builder.append("\t\t\t");
    _builder.newLine();
    {
      boolean _isCreateUiProject_2 = this.projectInfo.isCreateUiProject();
      if (_isCreateUiProject_2) {
        _builder.append("\t\t\t");
        _builder.append("fragment = builder.BuilderIntegrationFragment auto-inject {}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// labeling API");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = labeling.LabelProviderFragment auto-inject {}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// outline API");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = outline.OutlineTreeProviderFragment auto-inject {}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = outline.QuickOutlineFragment auto-inject {}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// quickfix API");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = quickfix.QuickfixProviderFragment auto-inject {}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// content assist API");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = contentAssist.ContentAssistFragment auto-inject {}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// generates a more lightweight Antlr parser and lexer tailored for content assist");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = parser.antlr.XtextAntlrUiGeneratorFragment auto-inject {}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// provides a preference page for template proposals");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = templates.CodetemplatesGeneratorFragment auto-inject {}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// rename refactoring");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = refactoring.RefactorElementNameFragment auto-inject {}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("// provides a compare view");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("fragment = compare.CompareFragment auto-inject {}");
        _builder.newLine();
      }
    }
    _builder.append("\t\t\t\t");
    _builder.append("// provides the necessary bindings for java types integration");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("fragment = types.TypesGeneratorFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("// generates the required bindings only if the grammar inherits from Xbase");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("fragment = xbase.XbaseGeneratorFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("// generates the required bindings only if the grammar inherits from Xtype");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("fragment = xbase.XtypeGeneratorFragment auto-inject {}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence grammar() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("grammar ");
    String _languageName = this.projectInfo.getLanguageName();
    _builder.append(_languageName, "");
    _builder.append(" with org.eclipse.xtext.common.Terminals");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("generate ");
    String _languageNameAbbreviation = this.projectInfo.getLanguageNameAbbreviation();
    String _firstLower = StringExtensions.toFirstLower(_languageNameAbbreviation);
    _builder.append(_firstLower, "");
    _builder.append(" \"");
    String _nsURI = this.projectInfo.getNsURI();
    _builder.append(_nsURI, "");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("Model:");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("greetings+=Greeting*;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("Greeting:");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\'Hello\' name=ID \'!\';");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence wfLaunchConfig() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
    _builder.newLine();
    _builder.append("<launchConfiguration type=\"org.eclipse.emf.mwe2.launch.Mwe2LaunchConfigurationType\">");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"org.eclipse.debug.core.ATTR_REFRESH_SCOPE\" value=\"${working_set:&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&#10;&lt;launchConfigurationWorkingSet factoryID=&quot;org.eclipse.ui.internal.WorkingSetFactory&quot; id=&quot;1299248699643_13&quot; label=&quot;working set&quot; name=&quot;working set&quot;&gt;&#10;&lt;item factoryID=&quot;org.eclipse.ui.internal.model.ResourceFactory&quot; path=&quot;/");
    String _projectName = this.projectInfo.getProjectName();
    _builder.append(_projectName, "");
    _builder.append("&quot; type=&quot;4&quot;/&gt;&#10;&lt;item factoryID=&quot;org.eclipse.ui.internal.model.ResourceFactory&quot; path=&quot;/");
    String _generatorProjectName = this.projectInfo.getGeneratorProjectName();
    _builder.append(_generatorProjectName, "");
    _builder.append("&quot; type=&quot;4&quot;/&gt;&#10;&lt;item factoryID=&quot;org.eclipse.ui.internal.model.ResourceFactory&quot; path=&quot;/");
    String _testProjectName = this.projectInfo.getTestProjectName();
    _builder.append(_testProjectName, "");
    _builder.append("&quot; type=&quot;4&quot;/&gt;&#10;&lt;item factoryID=&quot;org.eclipse.ui.internal.model.ResourceFactory&quot; path=&quot;/");
    String _uiProjectName = this.projectInfo.getUiProjectName();
    _builder.append(_uiProjectName, "");
    _builder.append("&quot; type=&quot;4&quot;/&gt;&#10;&lt;/launchConfigurationWorkingSet&gt;}\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<listAttribute key=\"org.eclipse.debug.core.MAPPED_RESOURCE_PATHS\">");
    _builder.newLine();
    _builder.append("<listEntry value=\"/");
    String _projectName_1 = this.projectInfo.getProjectName();
    _builder.append(_projectName_1, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("</listAttribute>");
    _builder.newLine();
    _builder.append("<listAttribute key=\"org.eclipse.debug.core.MAPPED_RESOURCE_TYPES\">");
    _builder.newLine();
    _builder.append("<listEntry value=\"4\"/>");
    _builder.newLine();
    _builder.append("</listAttribute>");
    _builder.newLine();
    _builder.append("<listAttribute key=\"org.eclipse.debug.ui.favoriteGroups\">");
    _builder.newLine();
    _builder.append("<listEntry value=\"org.eclipse.debug.ui.launchGroup.debug\"/>");
    _builder.newLine();
    _builder.append("<listEntry value=\"org.eclipse.debug.ui.launchGroup.run\"/>");
    _builder.newLine();
    _builder.append("</listAttribute>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.MAIN_TYPE\" value=\"org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.PROGRAM_ARGUMENTS\" value=\"src/");
    String _basePackagePath = this.projectInfo.getBasePackagePath();
    _builder.append(_basePackagePath, "");
    _builder.append("/Generate");
    String _languageNameAbbreviation = this.projectInfo.getLanguageNameAbbreviation();
    _builder.append(_languageNameAbbreviation, "");
    _builder.append(".mwe2\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.PROJECT_ATTR\" value=\"");
    String _projectName_2 = this.projectInfo.getProjectName();
    _builder.append(_projectName_2, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.VM_ARGUMENTS\" value=\"-Xmx512m\"/>");
    _builder.newLine();
    _builder.append("</launchConfiguration>");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence launchConfig() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
    _builder.newLine();
    _builder.append("<launchConfiguration type=\"org.eclipse.pde.ui.RuntimeWorkbench\">");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"append.args\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"askclear\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"automaticAdd\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"automaticValidate\" value=\"false\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"bad_container_name\" value=\"/");
    String _projectName = this.projectInfo.getProjectName();
    _builder.append(_projectName, "");
    _builder.append("/.launch/\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<stringAttribute key=\"bootstrap\" value=\"\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"checked\" value=\"[NONE]\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"clearConfig\" value=\"false\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"clearws\" value=\"false\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"clearwslog\" value=\"false\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"configLocation\" value=\"${workspace_loc}/.metadata/.plugins/org.eclipse.pde.core/Launch Runtime Eclipse\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"default\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"includeOptional\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"location\" value=\"${workspace_loc}/../runtime-EclipseXtext\"/>");
    _builder.newLine();
    _builder.append("<listAttribute key=\"org.eclipse.debug.ui.favoriteGroups\">");
    _builder.newLine();
    _builder.append("<listEntry value=\"org.eclipse.debug.ui.launchGroup.debug\"/>");
    _builder.newLine();
    _builder.append("<listEntry value=\"org.eclipse.debug.ui.launchGroup.run\"/>");
    _builder.newLine();
    _builder.append("</listAttribute>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.JRE_CONTAINER\" value=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.PROGRAM_ARGUMENTS\" value=\"-os ${target.os} -ws ${target.ws} -arch ${target.arch} -nl ${target.nl}\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.SOURCE_PATH_PROVIDER\" value=\"org.eclipse.pde.ui.workbenchClasspathProvider\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"org.eclipse.jdt.launching.VM_ARGUMENTS\" value=\"-Xms40m -Xmx512m -XX:MaxPermSize=256m\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"pde.version\" value=\"3.3\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"product\" value=\"org.eclipse.platform.ide\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"show_selected_only\" value=\"false\"/>");
    _builder.newLine();
    _builder.append("<stringAttribute key=\"templateConfig\" value=\"${target_home}/configuration/config.ini\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"tracing\" value=\"false\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"useDefaultConfig\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"useDefaultConfigArea\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"useProduct\" value=\"true\"/>");
    _builder.newLine();
    _builder.append("<booleanAttribute key=\"usefeatures\" value=\"false\"/>");
    _builder.newLine();
    _builder.append("</launchConfiguration>");
    _builder.newLine();
    return _builder;
  }
}
