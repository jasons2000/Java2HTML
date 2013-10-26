/*****************************************************************************
 * Copyright (C) Codehaus.org                                                *
 * ------------------------------------------------------------------------- *
 * Licensed under the Apache License, Version 2.0 (the "License");           *
 * you may not use this file except in compliance with the License.          *
 * You may obtain a copy of the License at                                   *
 *                                                                           *
 * http://www.apache.org/licenses/LICENSE-2.0                                *
 *                                                                           *
 * Unless required by applicable law or agreed to in writing, software       *
 * distributed under the License is distributed on an "AS IS" BASIS,         *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 * See the License for the specific language governing permissions and       *
 * limitations under the License.                                            *
 *****************************************************************************/
package com.src2html.java_parser.ast.expression;

import java.util.List;

import com.src2html.java_parser.ast.declaration.DefBody;
import com.src2html.java_parser.common.Strings;
import com.src2html.java_parser.common.ValueObject;
import com.src2html.java_parser.ast.type.TypeLiteral;

/**
 * Represents a non-qualified "new" statement with possibly anonymous class syntax.
 * 
 * @author Ben Yu
 */
public class NewExpression extends ValueObject implements Expression {
  public final Expression qualifier;
  public final TypeLiteral type;
  public final List<Expression> arguments;
  public final DefBody classBody;
  
  public NewExpression(
      Expression qualifier, TypeLiteral type, List<Expression> arguments, DefBody classBody) {
    this.qualifier = qualifier;
    this.type = type;
    this.arguments = arguments;
    this.classBody = classBody;
  }
  
  @Override public String toString() {
    return (qualifier == null ? "" : qualifier + ".")
        + "new " + type + "(" + Strings.join(", ", arguments) + ")"
        + (classBody == null ? "" : " " + classBody);
  }
}
