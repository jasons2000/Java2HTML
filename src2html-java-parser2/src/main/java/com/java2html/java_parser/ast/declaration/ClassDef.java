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

import java.util.List;

import com.java2html.java_parser.ast.statement.Modifier;
import com.java2html.java_parser.ast.type.TypeLiteral;
import com.java2html.java_parser.common.Strings;
import com.java2html.java_parser.common.ValueObject;
/**
 * Represents a class definition;
 * 
 * @author Ben Yu
 */
public final class ClassDef extends ValueObject implements Declaration {
  public final List<Modifier> modifiers;
  public final String name;
  public final List<TypeParameterDef> typeParameters;
  public final TypeLiteral superclass;
  public final List<TypeLiteral> interfaces;
  public final DefBody body;
  
  public ClassDef(List<Modifier> modifiers, String name, List<TypeParameterDef> typeParameters,
      TypeLiteral superclass, List<TypeLiteral> interfaces, DefBody body) {
    this.modifiers = modifiers;
    this.name = name;
    this.typeParameters = typeParameters;
    this.superclass = superclass;
    this.interfaces = interfaces;
    this.body = body;
  }
  
  @Override public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Modifier modifier : modifiers) {
      builder.append(modifier).append(' ');
    }
    builder.append("class ").append(name);
    if (typeParameters != null) {
      builder.append('<');
      Strings.join(builder, ", ", typeParameters);
      builder.append('>');
    }
    if (superclass != null) {
      builder.append(" extends ").append(superclass);
    }
    if (interfaces != null) {
      builder.append(" implements ");
      Strings.join(builder, ", ", interfaces);
    }
    builder.append(' ').append(body);
    return builder.toString();
  }
}
