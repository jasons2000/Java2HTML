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
package com.src2html.java_parser.parser;

import static org.codehaus.jparsec.Parsers.between;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import com.src2html.java_parser.ast.declaration.*;
import com.src2html.java_parser.ast.expression.Expression;
import com.src2html.java_parser.ast.statement.Statement;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Terminals;
import com.src2html.java_parser.ast.statement.Modifier;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.misc.Mapper;

/**
 * Parses class, interface, enum, annotation declarations.
 * 
 * @author Ben Yu
 */
public final class DeclarationParser {
  static Parser<DefBody> body(Parser<Member> member) {
    Parser<Member> empty = TerminalParser.term(";").retn(null);
    return Mapper.curry(DefBody.class).sequence(
        TerminalParser.term("{"), empty.or(member).many().map(new Map<List<Member>, List<Member>>() {
          public List<Member> map(List<Member> from) {
            removeNulls(from);
            return from;
          }
        }), TerminalParser.term("}"));
  }

  static void removeNulls(List<?> list) {
    for (Iterator<?> it = list.iterator(); it.hasNext();) {
      if (it.next() == null) {
        it.remove();
      }
    }
  }
  
  static Parser<Member> fieldDef(Parser<Expression> initializer) {
    return Mapper.<Member>curry(FieldDef.class).sequence(
        StatementParser.modifier(initializer).many(), TypeLiteralParser.TYPE_LITERAL, Terminals.Identifier.PARSER,
        TerminalParser.term("=").next(ExpressionParser.arrayInitializerOrRegularExpression(initializer))
            .optional(),
        TerminalParser.term(";"));
  }
  
  static final Parser<TypeParameterDef> TYPE_PARAMETER =
      Mapper.curry(TypeParameterDef.class).sequence(
          Terminals.Identifier.PARSER, TerminalParser.term("extends").next(TypeLiteralParser.TYPE_LITERAL)
              .optional());
  
  static final Parser<List<TypeParameterDef>> TYPE_PARAMETERS =
      between(TerminalParser.term("<"), TYPE_PARAMETER.sepBy1(TerminalParser.term(",")), TerminalParser.term(">"));
  
  static Parser<Member> constructorDef(Parser<Modifier> mod, Parser<Statement> stmt) {
    return Mapper.<Member>curry(ConstructorDef.class).sequence(
        mod.many(), Terminals.Identifier.PARSER,
        TerminalParser.term("("), StatementParser.parameter(mod).sepBy(TerminalParser.term(",")), TerminalParser.term(")"),
        TerminalParser.term("throws").next(TypeLiteralParser.ELEMENT_TYPE_LITERAL.sepBy1(TerminalParser.term(","))).optional(),
        StatementParser.blockStatement(stmt));
  }
  
  static Parser<Member> methodDef(
      Parser<Modifier> mod, Parser<Expression> defaultValue, Parser<Statement> stmt) {
    return Mapper.<Member>curry(MethodDef.class).sequence(
        mod.many(), TYPE_PARAMETERS.optional(),
        TypeLiteralParser.TYPE_LITERAL, Terminals.Identifier.PARSER,
        TerminalParser.term("("), StatementParser.parameter(mod).sepBy(TerminalParser.term(",")), TerminalParser.term(")"),
        TerminalParser.term("throws").next(TypeLiteralParser.ELEMENT_TYPE_LITERAL.sepBy1(TerminalParser.term(","))).optional(),
        TerminalParser.term("default").next(ExpressionParser.arrayInitializerOrRegularExpression(defaultValue))
            .optional(),
        Parsers.or(
            StatementParser.blockStatement(stmt),
            TerminalParser.term(";").retn(null)));
  }
  
  static Parser<Member> initializerDef(Parser<Statement> stmt) {
    return Mapper.<Member>curry(ClassInitializerDef.class).sequence(
        TerminalParser.term("static").succeeds(), StatementParser.blockStatement(stmt));
  }
  
  static Parser<Member> nestedDef(Parser<Declaration> dec) {
    return Mapper.<Member>curry(NestedDef.class).sequence(dec);
  }
  
  static Parser<Declaration> classDef(Parser<Modifier> mod, Parser<Member> member) {
    return curry(ClassDef.class).sequence(
        mod.many(), TerminalParser.term("class"), Terminals.Identifier.PARSER, TYPE_PARAMETERS.optional(),
        TerminalParser.term("extends").next(TypeLiteralParser.ELEMENT_TYPE_LITERAL).optional(),
        TerminalParser.term("implements").next(TypeLiteralParser.ELEMENT_TYPE_LITERAL.sepBy1(TerminalParser.term(","))).optional(),
        body(member));
  }
  
  static Parser<Declaration> interfaceDef(Parser<Modifier> mod, Parser<Member> member) {
    return curry(InterfaceDef.class).sequence(
        mod.many(), TerminalParser.term("interface"), Terminals.Identifier.PARSER, TYPE_PARAMETERS.optional(),
        TerminalParser.term("extends").next(TypeLiteralParser.ELEMENT_TYPE_LITERAL.sepBy1(TerminalParser.term(","))).optional(),
        body(member));
  }
  
  static Parser<Declaration> annotationDef(Parser<Modifier> mod, Parser<Member> member) {
    return curry(AnnotationDef.class).sequence(
        mod.many(), TerminalParser.phrase("@ interface"), Terminals.Identifier.PARSER, body(member));
  }
  
  static Parser<Declaration> enumDef(Parser<Expression> expr, Parser<Member> member) {
    Parser<EnumDef.Value> enumValue = Mapper.curry(EnumDef.Value.class).sequence(
        Terminals.Identifier.PARSER, between(TerminalParser.term("("), expr.sepBy(TerminalParser.term(",")), TerminalParser.term(")"))
            .optional(),
        between(TerminalParser.term("{"), member.many(), TerminalParser.term("}")).optional());
    return curry(EnumDef.class).sequence(
        StatementParser.modifier(expr).many(), TerminalParser.term("enum"), Terminals.Identifier.PARSER,
        TerminalParser.term("implements").next(TypeLiteralParser.ELEMENT_TYPE_LITERAL.sepBy1(TerminalParser.term(","))).optional(),
        TerminalParser.term("{"), enumValue.sepBy(TerminalParser.term(",")), TerminalParser.term(";").next(member.many()).optional(), TerminalParser.term("}"));
  }
  
  static final Parser<QualifiedName> QUALIFIED_NAME =
      Mapper.curry(QualifiedName.class).sequence(Terminals.Identifier.PARSER.sepBy1(TerminalParser.term(".")));
  
  static final Parser<Import> IMPORT = Mapper.curry(Import.class).sequence(
      TerminalParser.term("import"), TerminalParser.term("static").succeeds(),
      QUALIFIED_NAME, TerminalParser.phrase(". *").succeeds(), TerminalParser.term(";"));
  
  static final Parser<QualifiedName> PACKAGE = between(TerminalParser.term("package"), QUALIFIED_NAME, TerminalParser.term(";"));
  
  public static Parser<Program> program() {
    Parser.Reference<Member> memberRef = Parser.newReference();
    Parser<Expression> expr = ExpressionParser.expression(body(memberRef.lazy()));
    Parser<Statement> stmt = StatementParser.statement(expr);
    Parser<Modifier> mod = StatementParser.modifier(expr);
    Parser.Reference<Declaration> decRef = Parser.newReference();
    Parser<Member> member = Parsers.or(
        fieldDef(expr), methodDef(mod, expr, stmt), constructorDef(mod, stmt),
        initializerDef(stmt), nestedDef(decRef.lazy()));
    memberRef.set(member);
    Parser<Declaration> declaration = Parsers.or(
        classDef(mod, member), interfaceDef(mod, member),
        enumDef(expr, member), annotationDef(mod, member));
    decRef.set(declaration);
    return Mapper.curry(Program.class).sequence(
        PACKAGE.optional(), IMPORT.many(), declaration.many());
  }
  
  /** Parses any Java source.  */
  public static Program parse(String source) {
    return TerminalParser.parse(program(), source);
  }
  
  /** Parses source code read from {@code url}. */
  public static Program parse(URL url) throws IOException {
    InputStream in = url.openStream();
    try {
      return TerminalParser.parse(
          program(), new InputStreamReader(in, Charset.forName("UTF-8")), url.toString());
    } finally {
      in.close();
    }
  }
  
  private static Mapper<Declaration> curry(
      Class<? extends Declaration> clazz, Object... curryArgs) {
    return Mapper.curry(clazz, curryArgs);
  }
}
