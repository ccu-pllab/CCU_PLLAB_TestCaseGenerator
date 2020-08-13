// Generated from Ocl.g4 by ANTLR 4.4
package ccu.pllab.tcgen.oclRunner;
//�o�����O�N�Ojava���Yimport�������A�]��.g4�ɻPOclParser.java�O�s�����Y�A�ҥH���ઽ���bOclParser.java�W�Kimport�A�@���s�ɴN�|�Q�~���A�]���p�G�nimport���ɭԡA�b@header���Y�[
	import ccu.pllab.tcgen.AbstractSyntaxTree.*;
	import java.util.ArrayList;
	import java.util.Set;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link OclParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface OclVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link OclParser#unaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(@NotNull OclParser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#propertyCallParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyCallParameters(@NotNull OclParser.PropertyCallParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#formalParameterLists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterLists(@NotNull OclParser.FormalParameterListsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#unaryExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpressions(@NotNull OclParser.UnaryExpressionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#primaryExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpressions(@NotNull OclParser.PrimaryExpressionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#oclExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOclExpressions(@NotNull OclParser.OclExpressionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#relationalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalOperator(@NotNull OclParser.RelationalOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#constraints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraints(@NotNull OclParser.ConstraintsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#simpleTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeSpecifier(@NotNull OclParser.SimpleTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#actualParameterLists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActualParameterLists(@NotNull OclParser.ActualParameterListsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#oclExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOclExpression(@NotNull OclParser.OclExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(@NotNull OclParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#calculator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculator(@NotNull OclParser.CalculatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#propertyCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyCall(@NotNull OclParser.PropertyCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#booleanExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExp(@NotNull OclParser.BooleanExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#qualifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiers(@NotNull OclParser.QualifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#timeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeExpression(@NotNull OclParser.TimeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#formalParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterList(@NotNull OclParser.FormalParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#logicalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOperator(@NotNull OclParser.LogicalOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#actualParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActualParameterList(@NotNull OclParser.ActualParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#literalCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralCollection(@NotNull OclParser.LiteralCollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#stereotype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStereotype(@NotNull OclParser.StereotypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#returnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnType(@NotNull OclParser.ReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#pathName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathName(@NotNull OclParser.PathNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(@NotNull OclParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#relationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(@NotNull OclParser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#packageDeclarationCS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclarationCS(@NotNull OclParser.PackageDeclarationCSContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#operationContext}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationContext(@NotNull OclParser.OperationContextContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContent(@NotNull OclParser.ContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull OclParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionItem(@NotNull OclParser.CollectionItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionKind(@NotNull OclParser.CollectionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#letExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExpression(@NotNull OclParser.LetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#ifExps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExps(@NotNull OclParser.IfExpsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#packageName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageName(@NotNull OclParser.PackageNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#ifExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpression(@NotNull OclParser.IfExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#contextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextDeclaration(@NotNull OclParser.ContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpression(@NotNull OclParser.PostfixExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(@NotNull OclParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionMethod(@NotNull OclParser.CollectionMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#classifierContext}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierContext(@NotNull OclParser.ClassifierContextContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull OclParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#relationalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpressions(@NotNull OclParser.RelationalExpressionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#multiplicativeExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpressions(@NotNull OclParser.MultiplicativeExpressionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#enumLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumLiteral(@NotNull OclParser.EnumLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#operationName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationName(@NotNull OclParser.OperationNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#typeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpecifier(@NotNull OclParser.TypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(@NotNull OclParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#logicalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExpression(@NotNull OclParser.LogicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarator(@NotNull OclParser.DeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionType(@NotNull OclParser.CollectionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#addOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOperator(@NotNull OclParser.AddOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint(@NotNull OclParser.ConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#multiplyOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplyOperator(@NotNull OclParser.MultiplyOperatorContext ctx);
}