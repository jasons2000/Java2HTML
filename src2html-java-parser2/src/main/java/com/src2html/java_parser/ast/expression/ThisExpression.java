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

/**
 * "this" or "A.B.this".
 * 
 * @author Ben Yu
 */
public final class ThisExpression extends ValueObject implements Expression {
  public final List<String> qualifiers;

  public ThisExpression(List<String> qualifiers) {
    this.qualifiers = qualifiers;
  }
  
  @Override public String toString() {
    return qualifiers.isEmpty() ? "this"
        : Strings.join(".", qualifiers) + ".this";
  }
}
