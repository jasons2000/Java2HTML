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

import com.java2html.java_parser.common.ValueObject;
import com.java2html.java_parser.ast.expression.Expression;
import com.java2html.java_parser.ast.type.TypeLiteral;

/**
 * Represents the enhanced for loop statement.
 * 
 * @author Ben Yu
 */
public final class ForeachStatement extends ValueObject implements Statement {
  public final TypeLiteral type;
  public final String var;
  public final Expression of;
  public final Statement statement;
  
  public ForeachStatement(TypeLiteral type, String var, Expression of, Statement statement) {
    this.type = type;
    this.var = var;
    this.of = of;
    this.statement = statement;
  }
 
  @Override public String toString() {
    return "for (" + type + " " + var + " : " + of + ") " + statement;
  }
}
