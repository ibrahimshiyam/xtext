/**
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.example.homeautomation.formatting2;

import com.google.common.base.Objects;
import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericArrayTypeReference;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeConstraint;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmWildcardTypeReference;
import org.eclipse.xtext.example.homeautomation.ruleEngine.Declaration;
import org.eclipse.xtext.example.homeautomation.ruleEngine.Device;
import org.eclipse.xtext.example.homeautomation.ruleEngine.Model;
import org.eclipse.xtext.example.homeautomation.ruleEngine.Rule;
import org.eclipse.xtext.example.homeautomation.ruleEngine.RuleEnginePackage;
import org.eclipse.xtext.example.homeautomation.ruleEngine.State;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.regionaccess.IHiddenRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionFinder;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionsFinder;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.XAssignment;
import org.eclipse.xtext.xbase.XBasicForLoopExpression;
import org.eclipse.xtext.xbase.XBinaryOperation;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XCasePart;
import org.eclipse.xtext.xbase.XClosure;
import org.eclipse.xtext.xbase.XCollectionLiteral;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.XDoWhileExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.XForLoopExpression;
import org.eclipse.xtext.xbase.XIfExpression;
import org.eclipse.xtext.xbase.XInstanceOfExpression;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.XReturnExpression;
import org.eclipse.xtext.xbase.XSwitchExpression;
import org.eclipse.xtext.xbase.XSynchronizedExpression;
import org.eclipse.xtext.xbase.XThrowExpression;
import org.eclipse.xtext.xbase.XTryCatchFinallyExpression;
import org.eclipse.xtext.xbase.XTypeLiteral;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.XWhileExpression;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.formatting2.XbaseFormatter;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xtype.XFunctionTypeRef;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;

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
@SuppressWarnings("all")
public class RuleEngineFormatter extends XbaseFormatter {
  protected void _format(final Model model, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.setNewLines(0, 0, 1);
        it.noSpace();
      }
    };
    document.<Model>prepend(model, _function);
    EList<Declaration> _declarations = model.getDeclarations();
    for (final Declaration declaration : _declarations) {
      {
        this.format(declaration, document);
        final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.setNewLines(1, 1, 2);
          }
        };
        document.<Declaration>append(declaration, _function_1);
      }
    }
  }
  
  protected void _format(final Device device, @Extension final IFormattableDocument document) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(device);
    ISemanticRegion _feature = _regionFor.feature(RuleEnginePackage.Literals.DEVICE__NAME);
    final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.oneSpace();
      }
    };
    document.surround(_feature, _function);
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(device);
    ISemanticRegion _keyword = _regionFor_1.keyword("be");
    final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.oneSpace();
      }
    };
    document.surround(_keyword, _function_1);
    EList<State> _states = device.getStates();
    for (final State state : _states) {
      {
        ISemanticRegionFinder _immediatelyPreceding = this.textRegionExtensions.immediatelyPreceding(state);
        ISemanticRegion _keyword_1 = _immediatelyPreceding.keyword(",");
        final Procedure1<IHiddenRegionFormatter> _function_2 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.noSpace();
          }
        };
        ISemanticRegion _prepend = document.prepend(_keyword_1, _function_2);
        final Procedure1<IHiddenRegionFormatter> _function_3 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.oneSpace();
          }
        };
        document.append(_prepend, _function_3);
        this.format(state, document);
      }
    }
  }
  
  protected void _format(final Rule rule, @Extension final IFormattableDocument document) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(rule);
    ISemanticRegion _feature = _regionFor.feature(RuleEnginePackage.Literals.RULE__DESCRIPTION);
    final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.oneSpace();
      }
    };
    document.surround(_feature, _function);
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(rule);
    ISemanticRegion _feature_1 = _regionFor_1.feature(RuleEnginePackage.Literals.RULE__DEVICE_STATE);
    final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.oneSpace();
      }
    };
    document.surround(_feature_1, _function_1);
    XExpression _thenPart = rule.getThenPart();
    final Procedure1<IHiddenRegionFormatter> _function_2 = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.newLine();
      }
    };
    document.<XExpression>prepend(_thenPart, _function_2);
    XExpression _thenPart_1 = rule.getThenPart();
    this.format(_thenPart_1, document);
  }
  
  @Override
  protected void _format(final XBlockExpression expr, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.indent();
      }
    };
    document.<XBlockExpression>surround(expr, _function);
    EList<XExpression> _expressions = expr.getExpressions();
    for (final XExpression child : _expressions) {
      {
        ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(child);
        final ISemanticRegion sem = _immediatelyFollowing.keyword(";");
        boolean _notEquals = (!Objects.equal(sem, null));
        if (_notEquals) {
          final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
            @Override
            public void apply(final IHiddenRegionFormatter it) {
              it.noSpace();
            }
          };
          document.prepend(sem, _function_1);
          EList<XExpression> _expressions_1 = expr.getExpressions();
          XExpression _last = IterableExtensions.<XExpression>last(_expressions_1);
          boolean _notEquals_1 = (!Objects.equal(child, _last));
          if (_notEquals_1) {
            final Procedure1<IHiddenRegionFormatter> _function_2 = new Procedure1<IHiddenRegionFormatter>() {
              @Override
              public void apply(final IHiddenRegionFormatter it) {
                it.newLine();
              }
            };
            document.append(sem, _function_2);
          }
        } else {
          EList<XExpression> _expressions_2 = expr.getExpressions();
          XExpression _last_1 = IterableExtensions.<XExpression>last(_expressions_2);
          boolean _notEquals_2 = (!Objects.equal(child, _last_1));
          if (_notEquals_2) {
            final Procedure1<IHiddenRegionFormatter> _function_3 = new Procedure1<IHiddenRegionFormatter>() {
              @Override
              public void apply(final IHiddenRegionFormatter it) {
                it.newLine();
              }
            };
            document.<XExpression>append(child, _function_3);
          }
        }
        this.format(child, document);
      }
    }
  }
  
  @Override
  protected void _format(final XSwitchExpression expr, @Extension final IFormattableDocument document) {
    XExpression _switch = expr.getSwitch();
    IHiddenRegion _previousHiddenRegion = this.textRegionExtensions.previousHiddenRegion(_switch);
    IHiddenRegion _nextHiddenRegion = this.textRegionExtensions.nextHiddenRegion(expr);
    final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.indent();
      }
    };
    document.set(_previousHiddenRegion, _nextHiddenRegion, _function);
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("switch");
    final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.oneSpace();
      }
    };
    document.append(_keyword, _function_1);
    XExpression _switch_1 = expr.getSwitch();
    final Procedure1<IHiddenRegionFormatter> _function_2 = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.newLine();
      }
    };
    XExpression _append = document.<XExpression>append(_switch_1, _function_2);
    this.format(_append, document);
    EList<XCasePart> _cases = expr.getCases();
    for (final XCasePart c : _cases) {
      {
        boolean _and = false;
        JvmTypeReference _typeGuard = c.getTypeGuard();
        boolean _notEquals = (!Objects.equal(_typeGuard, null));
        if (!_notEquals) {
          _and = false;
        } else {
          XExpression _case = c.getCase();
          boolean _notEquals_1 = (!Objects.equal(_case, null));
          _and = _notEquals_1;
        }
        if (_and) {
          JvmTypeReference _typeGuard_1 = c.getTypeGuard();
          final Procedure1<IHiddenRegionFormatter> _function_3 = new Procedure1<IHiddenRegionFormatter>() {
            @Override
            public void apply(final IHiddenRegionFormatter it) {
              it.oneSpace();
            }
          };
          document.<JvmTypeReference>append(_typeGuard_1, _function_3);
          XExpression _case_1 = c.getCase();
          final Procedure1<IHiddenRegionFormatter> _function_4 = new Procedure1<IHiddenRegionFormatter>() {
            @Override
            public void apply(final IHiddenRegionFormatter it) {
              it.noSpace();
            }
          };
          document.<XExpression>append(_case_1, _function_4);
        } else {
          JvmTypeReference _typeGuard_2 = c.getTypeGuard();
          boolean _notEquals_2 = (!Objects.equal(_typeGuard_2, null));
          if (_notEquals_2) {
            JvmTypeReference _typeGuard_3 = c.getTypeGuard();
            final Procedure1<IHiddenRegionFormatter> _function_5 = new Procedure1<IHiddenRegionFormatter>() {
              @Override
              public void apply(final IHiddenRegionFormatter it) {
                it.noSpace();
              }
            };
            document.<JvmTypeReference>append(_typeGuard_3, _function_5);
          } else {
            XExpression _case_2 = c.getCase();
            boolean _notEquals_3 = (!Objects.equal(_case_2, null));
            if (_notEquals_3) {
              XExpression _case_3 = c.getCase();
              final Procedure1<IHiddenRegionFormatter> _function_6 = new Procedure1<IHiddenRegionFormatter>() {
                @Override
                public void apply(final IHiddenRegionFormatter it) {
                  it.oneSpace();
                }
              };
              XExpression _prepend = document.<XExpression>prepend(_case_3, _function_6);
              final Procedure1<IHiddenRegionFormatter> _function_7 = new Procedure1<IHiddenRegionFormatter>() {
                @Override
                public void apply(final IHiddenRegionFormatter it) {
                  it.noSpace();
                }
              };
              document.<XExpression>append(_prepend, _function_7);
            }
          }
        }
        ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(c);
        ISemanticRegion _feature = _regionFor_1.feature(XbasePackage.Literals.XCASE_PART__FALL_THROUGH);
        final Procedure1<IHiddenRegionFormatter> _function_8 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.noSpace();
          }
        };
        ISemanticRegion _prepend_1 = document.prepend(_feature, _function_8);
        final Procedure1<IHiddenRegionFormatter> _function_9 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.newLine();
          }
        };
        document.append(_prepend_1, _function_9);
        XExpression _case_4 = c.getCase();
        this.format(_case_4, document);
        boolean _and_1 = false;
        EList<XCasePart> _cases_1 = expr.getCases();
        XCasePart _last = IterableExtensions.<XCasePart>last(_cases_1);
        boolean _equals = Objects.equal(c, _last);
        if (!_equals) {
          _and_1 = false;
        } else {
          XExpression _default = expr.getDefault();
          boolean _equals_1 = Objects.equal(_default, null);
          _and_1 = _equals_1;
        }
        if (_and_1) {
          XExpression _then = c.getThen();
          this.formatBody(_then, true, document);
        } else {
          XExpression _then_1 = c.getThen();
          this.formatBodyParagraph(_then_1, document);
        }
      }
    }
    XExpression _default = expr.getDefault();
    boolean _notEquals = (!Objects.equal(_default, null));
    if (_notEquals) {
      ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword_1 = _regionFor_1.keyword("default");
      final Procedure1<IHiddenRegionFormatter> _function_3 = new Procedure1<IHiddenRegionFormatter>() {
        @Override
        public void apply(final IHiddenRegionFormatter it) {
          it.noSpace();
        }
      };
      document.append(_keyword_1, _function_3);
      XExpression _default_1 = expr.getDefault();
      this.formatBody(_default_1, true, document);
    }
  }
  
  @Override
  protected void formatBody(final XExpression expr, final boolean forceMultiline, @Extension final IFormattableDocument doc) {
    boolean _equals = Objects.equal(expr, null);
    if (_equals) {
      return;
    }
    if ((expr instanceof XBlockExpression)) {
      final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
        @Override
        public void apply(final IHiddenRegionFormatter it) {
          it.newLine();
        }
      };
      doc.<XBlockExpression>prepend(((XBlockExpression)expr), _function);
    } else {
      boolean _or = false;
      if (forceMultiline) {
        _or = true;
      } else {
        IHiddenRegion _previousHiddenRegion = this.textRegionExtensions.previousHiddenRegion(expr);
        boolean _isMultiline = _previousHiddenRegion.isMultiline();
        _or = _isMultiline;
      }
      if (_or) {
        final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.newLine();
          }
        };
        XExpression _prepend = doc.<XExpression>prepend(expr, _function_1);
        final Procedure1<IHiddenRegionFormatter> _function_2 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.indent();
          }
        };
        doc.<XExpression>surround(_prepend, _function_2);
      } else {
        final Procedure1<IHiddenRegionFormatter> _function_3 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.oneSpace();
          }
        };
        doc.<XExpression>prepend(expr, _function_3);
      }
    }
    this.format(expr, doc);
  }
  
  @Override
  protected void formatBodyInline(final XExpression expr, final boolean forceMultiline, @Extension final IFormattableDocument doc) {
    boolean _equals = Objects.equal(expr, null);
    if (_equals) {
      return;
    }
    if ((expr instanceof XBlockExpression)) {
      final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
        @Override
        public void apply(final IHiddenRegionFormatter it) {
          it.newLine();
        }
      };
      doc.<XBlockExpression>surround(((XBlockExpression)expr), _function);
    } else {
      boolean _or = false;
      if (forceMultiline) {
        _or = true;
      } else {
        IHiddenRegion _previousHiddenRegion = this.textRegionExtensions.previousHiddenRegion(expr);
        boolean _isMultiline = _previousHiddenRegion.isMultiline();
        _or = _isMultiline;
      }
      if (_or) {
        final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.newLine();
          }
        };
        XExpression _prepend = doc.<XExpression>prepend(expr, _function_1);
        final Procedure1<IHiddenRegionFormatter> _function_2 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.indent();
          }
        };
        XExpression _surround = doc.<XExpression>surround(_prepend, _function_2);
        final Procedure1<IHiddenRegionFormatter> _function_3 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.newLine();
          }
        };
        doc.<XExpression>append(_surround, _function_3);
      } else {
        final Procedure1<IHiddenRegionFormatter> _function_4 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.oneSpace();
          }
        };
        doc.<XExpression>surround(expr, _function_4);
      }
    }
    this.format(expr, doc);
  }
  
  @Override
  protected void formatBodyParagraph(final XExpression expr, @Extension final IFormattableDocument doc) {
    boolean _equals = Objects.equal(expr, null);
    if (_equals) {
      return;
    }
    if ((expr instanceof XBlockExpression)) {
      final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
        @Override
        public void apply(final IHiddenRegionFormatter it) {
          it.newLine();
        }
      };
      doc.<XBlockExpression>surround(((XBlockExpression)expr), _function);
    } else {
      final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
        @Override
        public void apply(final IHiddenRegionFormatter it) {
          it.oneSpace();
        }
      };
      doc.<XExpression>surround(expr, _function_1);
    }
    this.format(expr, doc);
  }
  
  public void format(final Object device, final IFormattableDocument document) {
    if (device instanceof JvmTypeParameter) {
      _format((JvmTypeParameter)device, document);
      return;
    } else if (device instanceof JvmFormalParameter) {
      _format((JvmFormalParameter)device, document);
      return;
    } else if (device instanceof XtextResource) {
      _format((XtextResource)device, document);
      return;
    } else if (device instanceof XAssignment) {
      _format((XAssignment)device, document);
      return;
    } else if (device instanceof XBinaryOperation) {
      _format((XBinaryOperation)device, document);
      return;
    } else if (device instanceof XDoWhileExpression) {
      _format((XDoWhileExpression)device, document);
      return;
    } else if (device instanceof XFeatureCall) {
      _format((XFeatureCall)device, document);
      return;
    } else if (device instanceof XMemberFeatureCall) {
      _format((XMemberFeatureCall)device, document);
      return;
    } else if (device instanceof XWhileExpression) {
      _format((XWhileExpression)device, document);
      return;
    } else if (device instanceof XFunctionTypeRef) {
      _format((XFunctionTypeRef)device, document);
      return;
    } else if (device instanceof JvmGenericArrayTypeReference) {
      _format((JvmGenericArrayTypeReference)device, document);
      return;
    } else if (device instanceof JvmParameterizedTypeReference) {
      _format((JvmParameterizedTypeReference)device, document);
      return;
    } else if (device instanceof JvmWildcardTypeReference) {
      _format((JvmWildcardTypeReference)device, document);
      return;
    } else if (device instanceof Device) {
      _format((Device)device, document);
      return;
    } else if (device instanceof Rule) {
      _format((Rule)device, document);
      return;
    } else if (device instanceof XBasicForLoopExpression) {
      _format((XBasicForLoopExpression)device, document);
      return;
    } else if (device instanceof XBlockExpression) {
      _format((XBlockExpression)device, document);
      return;
    } else if (device instanceof XClosure) {
      _format((XClosure)device, document);
      return;
    } else if (device instanceof XCollectionLiteral) {
      _format((XCollectionLiteral)device, document);
      return;
    } else if (device instanceof XConstructorCall) {
      _format((XConstructorCall)device, document);
      return;
    } else if (device instanceof XForLoopExpression) {
      _format((XForLoopExpression)device, document);
      return;
    } else if (device instanceof XIfExpression) {
      _format((XIfExpression)device, document);
      return;
    } else if (device instanceof XInstanceOfExpression) {
      _format((XInstanceOfExpression)device, document);
      return;
    } else if (device instanceof XReturnExpression) {
      _format((XReturnExpression)device, document);
      return;
    } else if (device instanceof XSwitchExpression) {
      _format((XSwitchExpression)device, document);
      return;
    } else if (device instanceof XSynchronizedExpression) {
      _format((XSynchronizedExpression)device, document);
      return;
    } else if (device instanceof XThrowExpression) {
      _format((XThrowExpression)device, document);
      return;
    } else if (device instanceof XTryCatchFinallyExpression) {
      _format((XTryCatchFinallyExpression)device, document);
      return;
    } else if (device instanceof XTypeLiteral) {
      _format((XTypeLiteral)device, document);
      return;
    } else if (device instanceof XVariableDeclaration) {
      _format((XVariableDeclaration)device, document);
      return;
    } else if (device instanceof JvmTypeConstraint) {
      _format((JvmTypeConstraint)device, document);
      return;
    } else if (device instanceof Model) {
      _format((Model)device, document);
      return;
    } else if (device instanceof XExpression) {
      _format((XExpression)device, document);
      return;
    } else if (device instanceof XImportDeclaration) {
      _format((XImportDeclaration)device, document);
      return;
    } else if (device instanceof XImportSection) {
      _format((XImportSection)device, document);
      return;
    } else if (device == null) {
      _format((Void)null, document);
      return;
    } else if (device != null) {
      _format(device, document);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(device, document).toString());
    }
  }
}
