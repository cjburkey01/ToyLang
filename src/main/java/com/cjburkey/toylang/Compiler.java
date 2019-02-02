package com.cjburkey.toylang;

import com.cjburkey.toylang.antlr.ToyLangBaseVisitor;
import com.cjburkey.toylang.antlr.ToyLangParser;
import com.cjburkey.toylang.lang.IScope;
import com.cjburkey.toylang.lang.Operator;
import com.cjburkey.toylang.lang.Parameter;
import com.cjburkey.toylang.lang.ScopeContainer;
import com.cjburkey.toylang.lang.Statement;
import com.cjburkey.toylang.lang.expression.BinaryOperator;
import com.cjburkey.toylang.lang.expression.Expression;
import com.cjburkey.toylang.lang.expression.FloatVal;
import com.cjburkey.toylang.lang.expression.FuncRef;
import com.cjburkey.toylang.lang.expression.FuncVal;
import com.cjburkey.toylang.lang.expression.If;
import com.cjburkey.toylang.lang.expression.StringVal;
import com.cjburkey.toylang.lang.expression.UnaryOperator;
import com.cjburkey.toylang.lang.expression.VarRef;
import com.cjburkey.toylang.lang.statement.Return;
import com.cjburkey.toylang.lang.statement.VariableDec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;

@SuppressWarnings("WeakerAccess")
public class Compiler implements IScope {
    
    private final StatementVisitor _statementVisitor = new StatementVisitor();
    private final VariableDecVisitor _variableDecVisitor = new VariableDecVisitor();
    private final ParametersVisitor _parametersVisitor = new ParametersVisitor();
    private final ArgumentsVisitor _argumentsVisitor = new ArgumentsVisitor();
    private final IfStatementVisitor _ifStatementVisitor = new IfStatementVisitor();
    private final ExpressionVisitor _expressionVisitor = new ExpressionVisitor();
    
    private final ScopeContainer mainScope = new ScopeContainer();
    
    public Compiler start(ToyLangParser.ProgramContext programContext) {
        programContext.statement().forEach(_statementVisitor::visit);
        return this;
    }
    
    public ScopeContainer scope() {
        return mainScope;
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
        @Override
        public Optional<Statement> visitReturn(ToyLangParser.ReturnContext ctx) {
            return Optional.of(new Return(_expressionVisitor.visit(ctx.expression()).orElse(null)));
        }
        @Override
        public Optional<Statement> visitIfState(ToyLangParser.IfStateContext ctx) {
            return Optional.ofNullable(_ifStatementVisitor.visit(ctx.ifStatement()).orElse(null));
        }
    }
    
    public class VariableDecVisitor extends TB<VariableDec> {
        @Override
        public Optional<VariableDec> visitVariableDec(ToyLangParser.VariableDecContext ctx) {
            Optional<Expression> expression = _expressionVisitor.visit(ctx.expression());
            return Optional.of(new VariableDec(ctx.variableName().getText(),
                    (ctx.typeName() == null) ? null : ctx.typeName().getText(),
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
    
    public class IfStatementVisitor extends TB<If> {
        @Override
        public Optional<If> visitIfStatement(ToyLangParser.IfStatementContext ctx) {
            // TODO: FINISH THIS
            return Optional.empty();
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
        public Optional<Expression> visitString(ToyLangParser.StringContext ctx) {
            String val = ctx.getText();
            return Optional.of(new StringVal(val.substring(1, val.length() - 1)));
        }
        @Override
        public Optional<Expression> visitPar(ToyLangParser.ParContext ctx) {
            return visit(ctx.expression());
        }
        @Override
        public Optional<Expression> visitNeg(ToyLangParser.NegContext ctx) {
            return Optional.of(new UnaryOperator(Operator.get(ctx.op.getText()), visit(ctx.expression()).orElse(null)));
        }
        @Override
        public Optional<Expression> visitMulDiv(ToyLangParser.MulDivContext ctx) {
            return Optional.of(new BinaryOperator(Operator.get(ctx.op.getText()),
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }
        @Override
        public Optional<Expression> visitAddSub(ToyLangParser.AddSubContext ctx) {
            return Optional.of(new BinaryOperator(Operator.get(ctx.op.getText()),
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }
        @Override
        public Optional<Expression> visitCompare(ToyLangParser.CompareContext ctx) {
            return Optional.of(new BinaryOperator(Operator.get(ctx.op.getText()),
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }
        @Override
        public Optional<Expression> visitFunc(ToyLangParser.FuncContext ctx) {
            return Optional.of(new FuncVal((ctx.typeName() == null) ? null : ctx.typeName().getText(),
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
            return Optional.of(new FuncRef(ctx.SELF() == null ? ctx.variableName().getText() : null, _argumentsVisitor.visit(ctx.arguments()).orElse(null)));
        }
        @Override
        public Optional<Expression> visitIf(ToyLangParser.IfContext ctx) {
            return Optional.ofNullable(_ifStatementVisitor.visit(ctx.ifStatement()).orElse(null));
        }
    }
    
    private class TB<T> extends ToyLangBaseVisitor<Optional<T>> {
        @Override
        public Optional<T> visit(ParseTree tree) {
            // If an error occurs, stop as soon as possible
            if (CompilerErrorHandler.hasErrored()) return Optional.empty();
            
            // If the input is null (for some reason, likely laziness on my part), ignore it.
            if (tree == null) return Optional.empty();
            
            // Visit
            return super.visit(tree);
        }
    }
    
}
