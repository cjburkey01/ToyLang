package com.cjburkey.toylang;

import com.cjburkey.toylang.antlr.ToyLangBaseVisitor;
import com.cjburkey.toylang.antlr.ToyLangParser;
import com.cjburkey.toylang.lang.*;
import com.cjburkey.toylang.lang.expression.*;
import com.cjburkey.toylang.lang.statement.Return;
import com.cjburkey.toylang.lang.statement.VariableDec;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

@SuppressWarnings("WeakerAccess")
public class Program implements IScope {

    private final boolean debug;

    private final StatementVisitor _statementVisitor = new StatementVisitor();
    private final VariableDecVisitor _variableDecVisitor = new VariableDecVisitor();
    private final ParametersVisitor _parametersVisitor = new ParametersVisitor();
    private final ArgumentsVisitor _argumentsVisitor = new ArgumentsVisitor();
    private final IfStatementVisitor _ifStatementVisitor = new IfStatementVisitor();
    private final ExpressionVisitor _expressionVisitor = new ExpressionVisitor();

    private final ScopeContainer mainScope = new ScopeContainer();
    private final Stack<IScope> scopeStack = new Stack<>();

    public Program(boolean debug) {
        this.debug = debug;
    }

    public Program() {
        this(false);
    }

    public void pushStack(IScope scope) {
        scopeStack.push(scope);
    }

    public void popStack() {
        scopeStack.pop();
    }

    public IScope getScope() {
        return scopeStack.isEmpty() ? null : scopeStack.peek();
    }

    public Program parse(ToyLangParser.ProgramContext programContext) {
        programContext.statement().forEach(_statementVisitor::visit);
        return this;
    }

    public void execute() {
        // Check for all errors and stop after checking if any are found
        if (tryCall(false, IStatement::errorCheck)) return;

        // Execute; stop if any unexpected errors occur
        tryCall(true, IStatement::execute);
    }

    private boolean tryCall(boolean stopOnError, Function<IStatement, ToyLangError> function) {
        boolean errored = false;
        List<IStatement> statements = getScope().scope().statements;
        for (IStatement statement : statements) {
            ToyLangError error = function.apply(statement);
            if (error != null) {
                System.err.println(error);
                errored = true;
                if (stopOnError) return true;
            }
        }
        return errored;
    }

    public ScopeContainer scope() {
        return mainScope;
    }

    private <I extends ParserRuleContext, O> List<O> visitList(List<I> ctxs, TB<O> visitor) {
        return ctxs.stream()
                .map(ctx -> visitor.visit(ctx).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public class StatementVisitor extends TB<IStatement> {

        @Override
        public Optional<IStatement> visit(ParseTree tree) {
            Optional<IStatement> value = super.visit(tree);
            if (debug) value.ifPresent(System.out::println);
            return value;
        }

        @Override
        public Optional<IStatement> visitVarDec(ToyLangParser.VarDecContext ctx) {
            return Optional.ofNullable(_variableDecVisitor.visit(ctx.variableDec()).orElse(null));
        }

        @Override
        public Optional<IStatement> visitExpr(ToyLangParser.ExprContext ctx) {
            return Optional.ofNullable(_expressionVisitor.visit(ctx.expression()).orElse(null));
        }

        @Override
        public Optional<IStatement> visitReturn(ToyLangParser.ReturnContext ctx) {
            return Optional.of(new Return(_expressionVisitor.visit(ctx.expression()).orElse(null)));
        }

        @Override
        public Optional<IStatement> visitIfState(ToyLangParser.IfStateContext ctx) {
            return Optional.ofNullable(_ifStatementVisitor.visit(ctx.ifStatement()).orElse(null));
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

    public class VariableDecVisitor extends TB<VariableDec> {

        @Override
        public Optional<VariableDec> visitVariableDec(ToyLangParser.VariableDecContext ctx) {
            Optional<IExpression<?>> expression = _expressionVisitor.visit(ctx.expression());
            return Optional.of(new VariableDec(ctx.variableName().getText(),
                    (ctx.typeName() == null) ? null : ctx.typeName().getText(),
                    expression.orElse(null)));
        }

    }

    public class ArgumentsVisitor extends TB<List<IExpression<?>>> {

        @Override
        public Optional<List<IExpression<?>>> visitArguments(ToyLangParser.ArgumentsContext ctx) {
            List<IExpression<?>> arguments = visit(ctx.arguments()).orElse(new ArrayList<>());
            arguments.add(_expressionVisitor.visit(ctx.expression()).orElse(null));
            return Optional.of(arguments);
        }

    }

    public class IfStatementVisitor extends TB<If> {

        @Override
        public Optional<If> visitElseBranch(ToyLangParser.ElseBranchContext ctx) {
            return Optional.of(new If(true,
                    _expressionVisitor.visit(ctx.expression()).orElse(null),
                    visitList(ctx.statement(), _statementVisitor),
                    null));
        }

        @Override
        public Optional<If> visitIfStatement(ToyLangParser.IfStatementContext ctx) {
            return Optional.of(new If(false,
                    _expressionVisitor.visit(ctx.expression()).orElse(null),
                    visitList(ctx.statement(), _statementVisitor),
                    visitList(ctx.elseBranch(), _ifStatementVisitor)));
        }

    }

    public class ExpressionVisitor extends TB<IExpression<?>> {

        @Override
        public Optional<IExpression<?>> visitInt(ToyLangParser.IntContext ctx) {
            return Optional.of(new FloatVal(Integer.parseInt(ctx.getText())));
        }

        @Override
        public Optional<IExpression<?>> visitFloat(ToyLangParser.FloatContext ctx) {
            return Optional.of(new FloatVal(Float.parseFloat(ctx.getText())));
        }

        @Override
        public Optional<IExpression<?>> visitString(ToyLangParser.StringContext ctx) {
            String val = ctx.getText();
            return Optional.of(new StringVal(val.substring(1, val.length() - 1)));
        }

        @Override
        public Optional<IExpression<?>> visitPar(ToyLangParser.ParContext ctx) {
            return visit(ctx.expression());
        }

        @Override
        public Optional<IExpression<?>> visitNeg(ToyLangParser.NegContext ctx) {
            return Optional.of(new UnaryOperator(Operator.get(ctx.op.getText()), visit(ctx.expression()).orElse(null)));
        }

        @Override
        public Optional<IExpression<?>> visitMulDiv(ToyLangParser.MulDivContext ctx) {
            return Optional.of(new BinaryOperator(Operator.get(ctx.op.getText()),
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }

        @Override
        public Optional<IExpression<?>> visitAddSub(ToyLangParser.AddSubContext ctx) {
            return Optional.of(new BinaryOperator(Operator.get(ctx.op.getText()),
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }

        @Override
        public Optional<IExpression<?>> visitCompare(ToyLangParser.CompareContext ctx) {
            return Optional.of(new BinaryOperator(Operator.get(ctx.op.getText()),
                    visit(ctx.expression(0)).orElse(null),
                    visit(ctx.expression(1)).orElse(null)));
        }

        @Override
        public Optional<IExpression<?>> visitFunc(ToyLangParser.FuncContext ctx) {
            return Optional.of(new FuncVal((ctx.typeName() == null) ? null : ctx.typeName().getText(),
                    _parametersVisitor.visit(ctx.parameters()).orElse(null),
                    visitList(ctx.statement(), _statementVisitor)));
        }

        @Override
        public Optional<IExpression<?>> visitVarRef(ToyLangParser.VarRefContext ctx) {
            return Optional.of(new VarRef(ctx.variableName().getText()));
        }

        @Override
        public Optional<IExpression<?>> visitFuncRef(ToyLangParser.FuncRefContext ctx) {
            return Optional.of(new FuncRef(((ctx.SELF() == null) ? ctx.variableName().getText() : null),
                    _argumentsVisitor.visit(ctx.arguments()).orElse(null)));
        }

        @Override
        public Optional<IExpression<?>> visitIf(ToyLangParser.IfContext ctx) {
            If ifStatement = _ifStatementVisitor.visit(ctx.ifStatement()).orElse(null);
            if (ifStatement != null) ifStatement.isExpression = true;
            return Optional.ofNullable(ifStatement);
        }

    }

    private class TB<T> extends ToyLangBaseVisitor<Optional<T>> {

        @Override
        public Optional<T> visit(ParseTree tree) {
            // If an error occurs, stop as soon as possible
            if (ParserErrorHandler.hasErrored()) return Optional.empty();

            // If the input is null (for some reason; likely laziness on my part), ignore it.
            if (tree == null) return Optional.empty();

            // Visit
            return super.visit(tree);
        }

    }

}
