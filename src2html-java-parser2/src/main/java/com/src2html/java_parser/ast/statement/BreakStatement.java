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
package com.src2html.java_parser.ast.statement;

import com.src2html.java_parser.common.ValueObject;

/**
 * Represents "break" statement.
 * 
 * @author Ben Yu
 */
public final class BreakStatement extends ValueObject implements Statement {
  public final String label;

  public BreakStatement(String label) {
    this.label = label;
  }
  
  @Override public String toString() {
    return label == null ? "break;" : "break " + label + ";";
  }
}
