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

package com.java2html.java_parser.ast.statement;

import java.util.List;

import com.java2html.java_parser.common.Strings;
import com.java2html.java_parser.common.ValueObject;
import com.java2html.java_parser.ast.expression.Expression;

/**
 * Represents the comma delimited expression list used in the initializer of a "for" loop.
 * 
 * @author Ben Yu
 */
public final class ExpressionListStatement extends ValueObject implements Statement {
  public final List<Expression> expressions;

  public ExpressionListStatement(List<Expression> expressions) {
    this.expressions = expressions;
  }
  
  @Override public String toString() {
    return Strings.join(", ", expressions) + ";";
  }
}
