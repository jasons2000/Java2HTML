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
import com.java2html.java_parser.ast.type.TypeLiteral;

/**
 * Represents the use of an annotation.
 * 
 * @author Ben Yu
 */
public final class Annotation extends ValueObject implements Modifier {
  
  public static final class Element extends ValueObject {
    public final String name;
    public final Expression value;
    
    public Element(String name, Expression value) {
      this.name = name;
      this.value = value;
    }
    
    @Override public String toString() {
      return (name == null ? value.toString() : name + "=" + value);
    }
  }
  public final TypeLiteral type;
  public final List<Element> elements;
  
  public Annotation(TypeLiteral type, List<Element> elements) {
    this.type = type;
    this.elements = elements;
  }
  
  @Override public String toString() {
    return "@" + type + (elements == null ? "" : "(" + Strings.join(", ", elements) + ")");
  }
}