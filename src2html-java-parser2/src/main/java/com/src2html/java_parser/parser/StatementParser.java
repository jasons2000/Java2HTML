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

import static com.src2html.java_parser.parser.ExpressionParser.paren;
import static org.codehaus.jparsec.Parsers.between;

import java.util.ArrayList;
import java.util.List;

import com.src2html.java_parser.ast.expression.Expression;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Terminals;
import com.src2html.java_parser.ast.statement.Annotation;
import com.src2html.java_parser.ast.statement.AssertStatement;
import com.src2html.java_parser.ast.statement.BlockStatement;
import com.src2html.java_parser.ast.statement.BreakStatement;
import com.src2html.java_parser.ast.statement.ContinueStatement;
import com.src2html.java_parser.ast.statement.DoWhileStatement;
import com.src2html.java_parser.ast.statement.ExpressionListStatement;
import com.src2html.java_parser.ast.statement.ExpressionStatement;
import com.src2html.java_parser.ast.statement.ForStatement;
import com.src2html.java_parser.ast.statement.ForeachStatement;
import com.src2html.java_parser.ast.statement.IfStatement;
import com.src2html.java_parser.ast.statement.LabelStatement;
import com.src2html.java_parser.ast.statement.Modifier;
import com.src2html.java_parser.ast.statement.NopStatement;
import com.src2html.java_parser.ast.statement.ParameterDef;
import com.src2html.java_parser.ast.statement.ReturnStatement;
import com.src2html.java_parser.ast.statement.Statement;
import com.src2html.java_parser.ast.statement.SuperCallStatement;
import com.src2html.java_parser.ast.statement.SwitchStatement;
import com.src2html.java_parser.ast.statement.SynchronizedBlockStatement;
import com.src2html.java_parser.ast.statement.SystemModifier;
import com.src2html.java_parser.ast.statement.ThisCallStatement;
import com.src2html.java_parser.ast.statement.ThrowStatement;
import com.src2html.java_parser.ast.statement.TryStatement;
import com.src2html.java_parser.ast.statement.VarStatement;
import com.src2html.java_parser.ast.statement.WhileStatement;
import static com.src2html.java_parser.parser.TypeLiteralParser.TYPE_LITERAL;
import org.codehaus.jparsec.functors.Unary;
import org.codehaus.jparsec.misc.Mapper;

/**
 * Parses a statement.
 * 
 * @author Ben Yu
 */
public final class StatementParser {
  
  static Parser<Modifier> systemModifier(SystemModifier... modifiers) {
    List<Parser<Modifier>> list = new ArrayList<Parser<Modifier>>(modifiers.length);
    for (Modifier modifier : modifiers) {
      list.add(TerminalParser.term(modifier.toString()).retn(modifier));
    }
    return Parsers.or(list);
  }
  
  static final Parser<Modifier> SYSTEM_MODIFIER = systemModifier(SystemModifier.values());
  
  static Parser<Annotation> annotation(Parser<Expression> expr) {
    Parser<Annotation.Element> element = Mapper.curry(Annotation.Element.class).sequence(
        Terminals.Identifier.PARSER.followedBy(TerminalParser.term("=")).atomic().optional(),
        ExpressionParser.arrayInitializerOrRegularExpression(expr));
    return Mapper.curry(Annotation.class).sequence(
        TerminalParser.term("@"), TypeLiteralParser.ELEMENT_TYPE_LITERAL,
        paren(element.sepBy(TerminalParser.term(","))).optional());
  }
  
  static Parser<Modifier> modifier(Parser<Expression> expr) {
    return Parsers.or(annotation(expr), SYSTEM_MODIFIER);
  }
  
  static final Parser<Statement> NOP = TerminalParser.term(";").retn(NopStatement.instance);
  
  static final Parser<Unary<Statement>> LABEL =
      curry(LabelStatement.class).prefix(Terminals.Identifier.PARSER, TerminalParser.term(":")).atomic();
  
  static final Parser<Statement> BREAK = curry(BreakStatement.class)
      .sequence(TerminalParser.term("break"), Terminals.Identifier.PARSER.optional(), TerminalParser.term(";"));
  
  static final Parser<Statement> CONTINUE = curry(ContinueStatement.class)
      .sequence(TerminalParser.term("continue"), Terminals.Identifier.PARSER.optional(), TerminalParser.term(";"));
  
  static Parser<Statement> returnStatement(Parser<Expression> expr) {
    return curry(ReturnStatement.class)
        .sequence(TerminalParser.term("return"), expr.optional(), TerminalParser.term(";"));
  }
  
  static Parser<Statement> blockStatement(Parser<Statement> stmt) {
    return curry(BlockStatement.class).sequence(TerminalParser.term("{"), stmt.many(), TerminalParser.term("}"));
  }
  
  static Parser<Statement> whileStatement(Parser<Expression> expr, Parser<Statement> stmt) {
    return curry(WhileStatement.class).sequence(
        TerminalParser.phrase("while ("), expr, TerminalParser.term(")"), stmt);
  }
  
  static Parser<Statement> doWhileStatement(Parser<Statement> stmt, Parser<Expression> expr) {
    return curry(DoWhileStatement.class)
        .sequence(TerminalParser.term("do"), stmt, TerminalParser.phrase("while ("), expr, TerminalParser.phrase(") ;"));
  }
  
  static Parser<Statement> ifStatement(Parser<Expression> expr, Parser<Statement> stmt) {
    return curry(IfStatement.class)
        .sequence(TerminalParser.phrase("if ("), expr, TerminalParser.term(")"), stmt,
            Parsers.pair(between(TerminalParser.phrase("else if ("), expr, TerminalParser.term(")")), stmt).many(),
            TerminalParser.term("else").next(stmt).optional());
  }
  
  static Parser<Statement> switchStatement(Parser<Expression> expr, Parser<Statement> stmt) {
    return curry(SwitchStatement.class)
        .sequence(TerminalParser.phrase("switch ("), expr, TerminalParser.phrase(") {"),
            Parsers.pair(between(TerminalParser.term("case"), expr, TerminalParser.term(":")), stmt.optional()).many(),
            TerminalParser.phrase("default :").next(stmt.optional()).optional(), TerminalParser.term("}"));
  }
  
  static Parser<Statement> foreachStatement(Parser<Expression> expr, Parser<Statement> stmt) {
    return curry(ForeachStatement.class).sequence(
        TerminalParser.phrase("for ("), TypeLiteralParser.TYPE_LITERAL, Terminals.Identifier.PARSER, TerminalParser.term(":"),
        expr, TerminalParser.term(")"), stmt);
  }
  
  static Parser<Statement> forStatement(Parser<Expression> expr, Parser<Statement> stmt) {
    return curry(ForStatement.class).sequence(
        TerminalParser.phrase("for ("), Parsers.or(varStatement(expr), expressionList(expr), NOP),
        expr.optional(), TerminalParser.term(";"), expr.sepBy(TerminalParser.term(",")), TerminalParser.term(")"),
        stmt);
  }
  
  static Parser<Statement> thisCall(Parser<Expression> expr) {
    return curry(ThisCallStatement.class)
        .sequence(TerminalParser.term("this"), TerminalParser.term("("), expr.sepBy(TerminalParser.term(",")), TerminalParser.term(")"), TerminalParser.term(";"));
  }
  
  static Parser<Statement> superCall(Parser<Expression> expr) {
    return curry(SuperCallStatement.class)
    .sequence(TerminalParser.term("super"), TerminalParser.term("("), expr.sepBy(TerminalParser.term(",")), TerminalParser.term(")"), TerminalParser.term(";"));
  }
  
  static Parser<Statement> varStatement(Parser<Expression> expr) {
    Parser<Expression> initializer =
        TerminalParser.term("=").next(ExpressionParser.arrayInitializerOrRegularExpression(expr));
    Parser<VarStatement.Var> var = Mapper.curry(VarStatement.Var.class).sequence(
        Terminals.Identifier.PARSER, initializer.optional());
    return curry(VarStatement.class).sequence(
        modifier(expr).many(), TypeLiteralParser.TYPE_LITERAL,
        var.sepBy1(TerminalParser.term(",")), TerminalParser.term(";"));
  }

  static Parser<Statement> expressionList(Parser<Expression> expr) {
    return curry(ExpressionListStatement.class).sequence(
        expr.sepBy1(TerminalParser.term(",")), TerminalParser.term(";"));
  }
  
  static Parser<Statement> synchronizedBlock(Parser<Statement> stmt) {
    return curry(SynchronizedBlockStatement.class)
        .sequence(TerminalParser.term("synchronized"), blockStatement(stmt));
  }
  
  static Parser<Statement> assertStatement(Parser<Expression> expr) {
    return curry(AssertStatement.class)
        .sequence(TerminalParser.term("assert"), expr, TerminalParser.term(":").next(expr).optional(), TerminalParser.term(";"));
  }
  
  static Parser<Statement> expression(Parser<Expression> expr) {
    return curry(ExpressionStatement.class).sequence(expr, TerminalParser.term(";"));
  }
  
  static Parser<ParameterDef> parameter(Parser<Modifier> mod) {
    return Mapper.curry(ParameterDef.class).sequence(
      mod.many(), TYPE_LITERAL, TerminalParser.term("...").succeeds(), Terminals.Identifier.PARSER);
  }
  
  static Parser<Statement> tryStatement(Parser<Modifier> mod, Parser<Statement> stmt) {
    Parser<Statement> block = blockStatement(stmt);
    return curry(TryStatement.class).sequence(
        TerminalParser.term("try"), block,
        Mapper.curry(TryStatement.CatchBlock.class).sequence(
            TerminalParser.term("catch"), TerminalParser.term("("), parameter(mod), TerminalParser.term(")"), block).many(),
        TerminalParser.term("finally").next(block).optional());
  }
  
  static Parser<Statement> throwStatement(Parser<Expression> thrown) {
    return curry(ThrowStatement.class).sequence(TerminalParser.term("throw"), thrown, TerminalParser.term(";"));
  }
  
  static Parser<Statement> statement(Parser<Expression> expr) {
    Parser.Reference<Statement> ref = Parser.newReference();
    Parser<Statement> lazy = ref.lazy();
    @SuppressWarnings("unchecked")
    Parser<Statement> parser = Parsers.or(
        returnStatement(expr), BREAK, CONTINUE, blockStatement(lazy),
        foreachStatement(expr, lazy), forStatement(expr, lazy),
        whileStatement(expr, lazy), doWhileStatement(lazy, expr),
        ifStatement(expr, lazy), switchStatement(expr, lazy),
        tryStatement(modifier(expr), lazy), throwStatement(expr),
        synchronizedBlock(lazy), assertStatement(expr), varStatement(expr),
        thisCall(expr), superCall(expr),
        expression(expr), NOP).prefix(LABEL).label("statement");
    ref.set(parser);
    return parser;
  }
  
  private static Mapper<Statement> curry(Class<? extends Statement> clazz, Object... curryArgs) {
    return Mapper.curry(clazz, curryArgs);
  }
}
