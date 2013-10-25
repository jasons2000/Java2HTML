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
package com.java2html.java_parser.ast.expression;

import com.java2html.java_parser.common.ValueObject;

/**
 * Represents binary expression such as "a + b".
 * 
 * @author Ben Yu
 */
public final class BinaryExpression extends ValueObject implements Expression {
  public final Expression left;
  public final Operator op;
  public final Expression right;
  
  public BinaryExpression(Expression left, Operator op, Expression right) {
    this.left = left;
    this.op = op;
    this.right = right;
  }
  
  @Override public String toString() {
    return "(" + left + " " + op + " " + right + ")";
  }
}
