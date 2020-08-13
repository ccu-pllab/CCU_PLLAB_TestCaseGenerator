// Generated from Ocl.g4 by ANTLR 4.4
package ccu.pllab.tcgen.oclRunner;
//�o�����O�N�Ojava���Yimport�������A�]��.g4�ɻPOclParser.java�O�s�����Y�A�ҥH���ઽ���bOclParser.java�W�Kimport�A�@���s�ɴN�|�Q�~���A�]���p�G�nimport���ɭԡA�b@header���Y�[
	import ccu.pllab.tcgen.AbstractSyntaxTree.*;
	import java.util.ArrayList;
	import java.util.Set;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OclParser}.
 */
public interface OclListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OclParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(@NotNull OclParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(@NotNull OclParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#propertyCallParameters}.
	 * @param ctx the parse tree
	 */
	void enterPropertyCallParameters(@NotNull OclParser.PropertyCallParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#propertyCallParameters}.
	 * @param ctx the parse tree
	 */
	void exitPropertyCallParameters(@NotNull OclParser.PropertyCallParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#formalParameterLists}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterLists(@NotNull OclParser.FormalParameterListsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#formalParameterLists}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterLists(@NotNull OclParser.FormalParameterListsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#unaryExpressions}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressions(@NotNull OclParser.UnaryExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#unaryExpressions}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressions(@NotNull OclParser.UnaryExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#primaryExpressions}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpressions(@NotNull OclParser.PrimaryExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#primaryExpressions}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpressions(@NotNull OclParser.PrimaryExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#oclExpressions}.
	 * @param ctx the parse tree
	 */
	void enterOclExpressions(@NotNull OclParser.OclExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#oclExpressions}.
	 * @param ctx the parse tree
	 */
	void exitOclExpressions(@NotNull OclParser.OclExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#relationalOperator}.
	 * @param ctx the parse tree
	 */
	void enterRelationalOperator(@NotNull OclParser.RelationalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#relationalOperator}.
	 * @param ctx the parse tree
	 */
	void exitRelationalOperator(@NotNull OclParser.RelationalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#constraints}.
	 * @param ctx the parse tree
	 */
	void enterConstraints(@NotNull OclParser.ConstraintsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#constraints}.
	 * @param ctx the parse tree
	 */
	void exitConstraints(@NotNull OclParser.ConstraintsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#simpleTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTypeSpecifier(@NotNull OclParser.SimpleTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#simpleTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTypeSpecifier(@NotNull OclParser.SimpleTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#actualParameterLists}.
	 * @param ctx the parse tree
	 */
	void enterActualParameterLists(@NotNull OclParser.ActualParameterListsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#actualParameterLists}.
	 * @param ctx the parse tree
	 */
	void exitActualParameterLists(@NotNull OclParser.ActualParameterListsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#oclExpression}.
	 * @param ctx the parse tree
	 */
	void enterOclExpression(@NotNull OclParser.OclExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#oclExpression}.
	 * @param ctx the parse tree
	 */
	void exitOclExpression(@NotNull OclParser.OclExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(@NotNull OclParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(@NotNull OclParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#calculator}.
	 * @param ctx the parse tree
	 */
	void enterCalculator(@NotNull OclParser.CalculatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#calculator}.
	 * @param ctx the parse tree
	 */
	void exitCalculator(@NotNull OclParser.CalculatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#propertyCall}.
	 * @param ctx the parse tree
	 */
	void enterPropertyCall(@NotNull OclParser.PropertyCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#propertyCall}.
	 * @param ctx the parse tree
	 */
	void exitPropertyCall(@NotNull OclParser.PropertyCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#booleanExp}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExp(@NotNull OclParser.BooleanExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#booleanExp}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExp(@NotNull OclParser.BooleanExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#qualifiers}.
	 * @param ctx the parse tree
	 */
	void enterQualifiers(@NotNull OclParser.QualifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#qualifiers}.
	 * @param ctx the parse tree
	 */
	void exitQualifiers(@NotNull OclParser.QualifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#timeExpression}.
	 * @param ctx the parse tree
	 */
	void enterTimeExpression(@NotNull OclParser.TimeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#timeExpression}.
	 * @param ctx the parse tree
	 */
	void exitTimeExpression(@NotNull OclParser.TimeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(@NotNull OclParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(@NotNull OclParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(@NotNull OclParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(@NotNull OclParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#actualParameterList}.
	 * @param ctx the parse tree
	 */
	void enterActualParameterList(@NotNull OclParser.ActualParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#actualParameterList}.
	 * @param ctx the parse tree
	 */
	void exitActualParameterList(@NotNull OclParser.ActualParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#literalCollection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralCollection(@NotNull OclParser.LiteralCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#literalCollection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralCollection(@NotNull OclParser.LiteralCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#stereotype}.
	 * @param ctx the parse tree
	 */
	void enterStereotype(@NotNull OclParser.StereotypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#stereotype}.
	 * @param ctx the parse tree
	 */
	void exitStereotype(@NotNull OclParser.StereotypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(@NotNull OclParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(@NotNull OclParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#pathName}.
	 * @param ctx the parse tree
	 */
	void enterPathName(@NotNull OclParser.PathNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#pathName}.
	 * @param ctx the parse tree
	 */
	void exitPathName(@NotNull OclParser.PathNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(@NotNull OclParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(@NotNull OclParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(@NotNull OclParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(@NotNull OclParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#packageDeclarationCS}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclarationCS(@NotNull OclParser.PackageDeclarationCSContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#packageDeclarationCS}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclarationCS(@NotNull OclParser.PackageDeclarationCSContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#operationContext}.
	 * @param ctx the parse tree
	 */
	void enterOperationContext(@NotNull OclParser.OperationContextContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#operationContext}.
	 * @param ctx the parse tree
	 */
	void exitOperationContext(@NotNull OclParser.OperationContextContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#content}.
	 * @param ctx the parse tree
	 */
	void enterContent(@NotNull OclParser.ContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#content}.
	 * @param ctx the parse tree
	 */
	void exitContent(@NotNull OclParser.ContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull OclParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull OclParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#collectionItem}.
	 * @param ctx the parse tree
	 */
	void enterCollectionItem(@NotNull OclParser.CollectionItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#collectionItem}.
	 * @param ctx the parse tree
	 */
	void exitCollectionItem(@NotNull OclParser.CollectionItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#collectionKind}.
	 * @param ctx the parse tree
	 */
	void enterCollectionKind(@NotNull OclParser.CollectionKindContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#collectionKind}.
	 * @param ctx the parse tree
	 */
	void exitCollectionKind(@NotNull OclParser.CollectionKindContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#letExpression}.
	 * @param ctx the parse tree
	 */
	void enterLetExpression(@NotNull OclParser.LetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#letExpression}.
	 * @param ctx the parse tree
	 */
	void exitLetExpression(@NotNull OclParser.LetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#ifExps}.
	 * @param ctx the parse tree
	 */
	void enterIfExps(@NotNull OclParser.IfExpsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#ifExps}.
	 * @param ctx the parse tree
	 */
	void exitIfExps(@NotNull OclParser.IfExpsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#packageName}.
	 * @param ctx the parse tree
	 */
	void enterPackageName(@NotNull OclParser.PackageNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#packageName}.
	 * @param ctx the parse tree
	 */
	void exitPackageName(@NotNull OclParser.PackageNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#ifExpression}.
	 * @param ctx the parse tree
	 */
	void enterIfExpression(@NotNull OclParser.IfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#ifExpression}.
	 * @param ctx the parse tree
	 */
	void exitIfExpression(@NotNull OclParser.IfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#contextDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterContextDeclaration(@NotNull OclParser.ContextDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#contextDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitContextDeclaration(@NotNull OclParser.ContextDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(@NotNull OclParser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(@NotNull OclParser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(@NotNull OclParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(@NotNull OclParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#collectionMethod}.
	 * @param ctx the parse tree
	 */
	void enterCollectionMethod(@NotNull OclParser.CollectionMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#collectionMethod}.
	 * @param ctx the parse tree
	 */
	void exitCollectionMethod(@NotNull OclParser.CollectionMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#classifierContext}.
	 * @param ctx the parse tree
	 */
	void enterClassifierContext(@NotNull OclParser.ClassifierContextContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#classifierContext}.
	 * @param ctx the parse tree
	 */
	void exitClassifierContext(@NotNull OclParser.ClassifierContextContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull OclParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull OclParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#relationalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpressions(@NotNull OclParser.RelationalExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#relationalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpressions(@NotNull OclParser.RelationalExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#multiplicativeExpressions}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpressions(@NotNull OclParser.MultiplicativeExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#multiplicativeExpressions}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpressions(@NotNull OclParser.MultiplicativeExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#enumLiteral}.
	 * @param ctx the parse tree
	 */
	void enterEnumLiteral(@NotNull OclParser.EnumLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#enumLiteral}.
	 * @param ctx the parse tree
	 */
	void exitEnumLiteral(@NotNull OclParser.EnumLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#operationName}.
	 * @param ctx the parse tree
	 */
	void enterOperationName(@NotNull OclParser.OperationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#operationName}.
	 * @param ctx the parse tree
	 */
	void exitOperationName(@NotNull OclParser.OperationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpecifier(@NotNull OclParser.TypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpecifier(@NotNull OclParser.TypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(@NotNull OclParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(@NotNull OclParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(@NotNull OclParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(@NotNull OclParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#declarator}.
	 * @param ctx the parse tree
	 */
	void enterDeclarator(@NotNull OclParser.DeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#declarator}.
	 * @param ctx the parse tree
	 */
	void exitDeclarator(@NotNull OclParser.DeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#collectionType}.
	 * @param ctx the parse tree
	 */
	void enterCollectionType(@NotNull OclParser.CollectionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#collectionType}.
	 * @param ctx the parse tree
	 */
	void exitCollectionType(@NotNull OclParser.CollectionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#addOperator}.
	 * @param ctx the parse tree
	 */
	void enterAddOperator(@NotNull OclParser.AddOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#addOperator}.
	 * @param ctx the parse tree
	 */
	void exitAddOperator(@NotNull OclParser.AddOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(@NotNull OclParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(@NotNull OclParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link OclParser#multiplyOperator}.
	 * @param ctx the parse tree
	 */
	void enterMultiplyOperator(@NotNull OclParser.MultiplyOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OclParser#multiplyOperator}.
	 * @param ctx the parse tree
	 */
	void exitMultiplyOperator(@NotNull OclParser.MultiplyOperatorContext ctx);
}