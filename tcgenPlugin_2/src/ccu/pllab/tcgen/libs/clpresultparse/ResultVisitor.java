// Generated from Result.g4 by ANTLR 4.4
package ccu.pllab.tcgen.libs.clpresultparse;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ResultParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ResultVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ResultParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(@NotNull ResultParser.ArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#elms}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElms(@NotNull ResultParser.ElmsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#pairedLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairedLiteral(@NotNull ResultParser.PairedLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#argStr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgStr(@NotNull ResultParser.ArgStrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#stateList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateList(@NotNull ResultParser.StateListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#pairedObj}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairedObj(@NotNull ResultParser.PairedObjContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#objElmList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjElmList(@NotNull ResultParser.ObjElmListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(@NotNull ResultParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#preStateStr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreStateStr(@NotNull ResultParser.PreStateStrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#postStateStr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostStateStr(@NotNull ResultParser.PostStateStrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#objElm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjElm(@NotNull ResultParser.ObjElmContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull ResultParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult(@NotNull ResultParser.ResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#ascElmList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAscElmList(@NotNull ResultParser.AscElmListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#argSelf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgSelf(@NotNull ResultParser.ArgSelfContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#argRet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgRet(@NotNull ResultParser.ArgRetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#argArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgArg(@NotNull ResultParser.ArgArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link ResultParser#ascElm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAscElm(@NotNull ResultParser.AscElmContext ctx);
}