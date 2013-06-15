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

package com.java2html.java_parser.ast.declaration;

import com.java2html.java_parser.ast.statement.Modifier;
import com.java2html.java_parser.common.ValueObject;

import java.util.List;

/**
 * Represents an annotation definition.
 * 
 * @author Ben Yu
 */
public final class AnnotationDef extends ValueObject implements Declaration {
  public final List<Modifier> modifiers;
  public final String name;
  public final DefBody body;
  
  public AnnotationDef(
      List<Modifier> modifiers, String name, DefBody body) {
    this.modifiers = modifiers;
    this.name = name;
    this.body = body;
  }
  
  @Override public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Modifier modifier : modifiers) {
      builder.append(modifier).append(' ');
    }
    builder.append("@interface ").append(name).append(' ').append(body);
    return builder.toString();
  }
}
