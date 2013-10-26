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

import com.src2html.java_parser.common.Strings;
import com.src2html.java_parser.common.ValueObject;
import com.src2html.java_parser.ast.type.TypeLiteral;

/**
 * Represents a "new Foo[] {...}" or "new Foo[size] {...}" expression.
 * 
 * @author Ben Yu
 */
public final class NewArrayExpression extends ValueObject implements Expression {
  public final TypeLiteral elementType;
  public final Expression length;
  public final List<Expression> initializer;
  
  public NewArrayExpression(TypeLiteral type, Expression length, List<Expression> initializer) {
    this.elementType = type;
    this.length = length;
    this.initializer = initializer;
  }
  
  @Override public String toString() {
    return "new " + elementType + "[" + (length == null ? "" : length) + "]"
        + (initializer == null ? "" : " {" + Strings.join(", ", initializer) + "}");
  }
}
