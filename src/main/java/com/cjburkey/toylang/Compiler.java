package com.cjburkey.toylang;

import com.cjburkey.toylang.antlr.ToyLangBaseVisitor;
import com.cjburkey.toylang.antlr.ToyLangParser;
import com.cjburkey.toylang.lang.Parameter;
import com.cjburkey.toylang.lang.expression.*;
import com.cjburkey.toylang.lang.Statement;
import com.cjburkey.toylang.lang.VariableDec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;

@SuppressWarnings("WeakerAccess")
public class Compiler {
    
    private final StatementVisitor _statementVisitor = new StatementVisitor();
    private final VariableDecVisitor _variableDecVisitor = new VariableDecVisitor();
    private final ParametersVisitor _parametersVisitor = new ParametersVisitor();
    private final ArgumentsVisitor _argumentsVisitor = new ArgumentsVisitor();
    private final ExpressionVisitor _expressionVisitor = new ExpressionVisitor();
    
    public Compiler start(ToyLangParser.ProgramContext programContext) {
        programContext.statement().forEach(_statementVisitor::visit);
        return this;
    }
    
    public class StatementVisitor extends TB<Statement> {
        @Override
        public Optional<Statement> visit(ParseTree tree) {
            Optional<Statement> value = super.visit(tree);
            value.ifPresent(System.out::println);
            return value;
        }
        @Override
        public Optional<Statement> visitVarDec(ToyLangParser.VarDecContext ctx) {
            return Optional.ofNullable(_variableDecVisitor.visit(ctx.variableDec()).orElse(null));
        }
        @Override
        public Optional<Statement> visitExpr(ToyLangParser.ExprContext ctx) {
            return Optional.ofNullable(_expressionVisitor.visit(ctx.expression()).orElse(null));
        }
    }
    
    public class VariableDecVisitor extends TB<VariableDec> {
        @Override
        public Optional<VariableDec> visitVariableDec(ToyLangParser.VariableDecContext ctx) {
            Optional<Expression> expression = _expressionVisitor.visit(ctx.expression());
            return Optional.of(new VariableDec(ctx.variableName().getText(),
                    ctx.typeName().getText(),
                    expression.orElse(null)));
        }
    }
    
    public class ParametersVisitor extends TB<List<Parameter>> {
        @Override
        public Optional<List<Parameter>> visitParameters(ToyLangParser.ParametersContext ctx) {
            List<Parameter> parameters = visit(ctx.parameters()).orElse(new ArrayList<>());
            parameters.add(new Parameter(ctx.parameter().variableName().getText(), ctx.parameter().typeName().getText()));
            return Optional.of(parameters);
        }
    }
    
    public class ArgumentsVisitor extends TB<List<Expression>> {
        @Override
        public Optional<List<Expression>> visitArguments(ToyLangParser.ArgumentsContext ctx) {
            List<Expression> arguments = visit(ctx.arguments()).orElse(new ArrayList<>());
            arguments.add(_expressionVisitor.visit(ctx.expression()).orElse(null));
            return Optional.of(arguments);
        }
    }
    
    public class ExpressionVisitor extends TB<Expression> {
        @Override
        public Optional<Expression> visitInt(ToyLangParser.IntContext ctx) {
            return Optional.of(new FloatVal(Integer.parseInt(ctx.getText())));
        }
        @Override
        public Optional<Expression> visitFloat(ToyLangParser.FloatContext ctx) {
            return Optional.of(new FloatVal(Float.parseFloat(ctx.getText())));
        }
        @Override
        public Optional<Expression> visitPar(ToyLangParser.ParContext ctx) {
            return visit(ctx.expression());
        }
        @Override
        public Optional<Expression> visitNeg(ToyLangParser.NegContext ctx) {
            return Optional.of(new UnaryOperator(Expression.Operator.NEG, visit(ctx.expression()).orElse(null)));
        }
        @Override
        public Optional<Expression> visitMulDiv(ToyLangParser.MulDivContext ctx) {
            return Optional.of(new BinaryOperator(ctx.TIMES() == null ? Expression.Operator.DIV : Expression.Operator.MUL,
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }
        @Override
        public Optional<Expression> visitAddSub(ToyLangParser.AddSubContext ctx) {
            return Optional.of(new BinaryOperator(ctx.PLUS() == null ? Expression.Operator.SUB : Expression.Operator.ADD,
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }
        @Override
        public Optional<Expression> visitFunc(ToyLangParser.FuncContext ctx) {
            return Optional.of(new FuncVal(ctx.typeName().getText(),
                    _parametersVisitor.visit(ctx.parameters()).orElse(null),
                    ctx.statement().stream()
                            .map(_statementVisitor::visit)
                            .map(e -> e.orElse(null))
                            .collect(Collectors.toList())));
        }
        @Override
        public Optional<Expression> visitVarRef(ToyLangParser.VarRefContext ctx) {
            return Optional.of(new VarRef(ctx.variableName().getText()));
        }
        @Override
        public Optional<Expression> visitFuncRef(ToyLangParser.FuncRefContext ctx) {
            return Optional.of(new FuncRef(ctx.variableName().getText(), _argumentsVisitor.visit(ctx.arguments()).orElse(null)));
        }
    }
    
    private class TB<T> extends ToyLangBaseVisitor<Optional<T>> {
        @Override
        public Optional<T> visit(ParseTree tree) {
            if (tree == null) return Optional.empty();
            return super.visit(tree);
        }
    }
    
}
