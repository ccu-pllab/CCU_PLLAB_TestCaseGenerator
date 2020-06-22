// Generated from Result.g4 by ANTLR 4.4
package ccu.pllab.tcgen.libs.clpresultparse;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ResultParser}.
 */
public interface ResultListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ResultParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(@NotNull ResultParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(@NotNull ResultParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#elms}.
	 * @param ctx the parse tree
	 */
	void enterElms(@NotNull ResultParser.ElmsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#elms}.
	 * @param ctx the parse tree
	 */
	void exitElms(@NotNull ResultParser.ElmsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#pairedLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPairedLiteral(@NotNull ResultParser.PairedLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#pairedLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPairedLiteral(@NotNull ResultParser.PairedLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#argStr}.
	 * @param ctx the parse tree
	 */
	void enterArgStr(@NotNull ResultParser.ArgStrContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#argStr}.
	 * @param ctx the parse tree
	 */
	void exitArgStr(@NotNull ResultParser.ArgStrContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#stateList}.
	 * @param ctx the parse tree
	 */
	void enterStateList(@NotNull ResultParser.StateListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#stateList}.
	 * @param ctx the parse tree
	 */
	void exitStateList(@NotNull ResultParser.StateListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#pairedObj}.
	 * @param ctx the parse tree
	 */
	void enterPairedObj(@NotNull ResultParser.PairedObjContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#pairedObj}.
	 * @param ctx the parse tree
	 */
	void exitPairedObj(@NotNull ResultParser.PairedObjContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#objElmList}.
	 * @param ctx the parse tree
	 */
	void enterObjElmList(@NotNull ResultParser.ObjElmListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#objElmList}.
	 * @param ctx the parse tree
	 */
	void exitObjElmList(@NotNull ResultParser.ObjElmListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(@NotNull ResultParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(@NotNull ResultParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#preStateStr}.
	 * @param ctx the parse tree
	 */
	void enterPreStateStr(@NotNull ResultParser.PreStateStrContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#preStateStr}.
	 * @param ctx the parse tree
	 */
	void exitPreStateStr(@NotNull ResultParser.PreStateStrContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#postStateStr}.
	 * @param ctx the parse tree
	 */
	void enterPostStateStr(@NotNull ResultParser.PostStateStrContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#postStateStr}.
	 * @param ctx the parse tree
	 */
	void exitPostStateStr(@NotNull ResultParser.PostStateStrContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#objElm}.
	 * @param ctx the parse tree
	 */
	void enterObjElm(@NotNull ResultParser.ObjElmContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#objElm}.
	 * @param ctx the parse tree
	 */
	void exitObjElm(@NotNull ResultParser.ObjElmContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull ResultParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull ResultParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#result}.
	 * @param ctx the parse tree
	 */
	void enterResult(@NotNull ResultParser.ResultContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#result}.
	 * @param ctx the parse tree
	 */
	void exitResult(@NotNull ResultParser.ResultContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#ascElmList}.
	 * @param ctx the parse tree
	 */
	void enterAscElmList(@NotNull ResultParser.AscElmListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#ascElmList}.
	 * @param ctx the parse tree
	 */
	void exitAscElmList(@NotNull ResultParser.AscElmListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#argSelf}.
	 * @param ctx the parse tree
	 */
	void enterArgSelf(@NotNull ResultParser.ArgSelfContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#argSelf}.
	 * @param ctx the parse tree
	 */
	void exitArgSelf(@NotNull ResultParser.ArgSelfContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#argRet}.
	 * @param ctx the parse tree
	 */
	void enterArgRet(@NotNull ResultParser.ArgRetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#argRet}.
	 * @param ctx the parse tree
	 */
	void exitArgRet(@NotNull ResultParser.ArgRetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#argArg}.
	 * @param ctx the parse tree
	 */
	void enterArgArg(@NotNull ResultParser.ArgArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#argArg}.
	 * @param ctx the parse tree
	 */
	void exitArgArg(@NotNull ResultParser.ArgArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link ResultParser#ascElm}.
	 * @param ctx the parse tree
	 */
	void enterAscElm(@NotNull ResultParser.AscElmContext ctx);
	/**
	 * Exit a parse tree produced by {@link ResultParser#ascElm}.
	 * @param ctx the parse tree
	 */
	void exitAscElm(@NotNull ResultParser.AscElmContext ctx);
}