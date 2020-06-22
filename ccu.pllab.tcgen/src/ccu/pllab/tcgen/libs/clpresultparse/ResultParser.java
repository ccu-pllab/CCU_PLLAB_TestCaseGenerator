// Generated from Result.g4 by ANTLR 4.4
package ccu.pllab.tcgen.libs.clpresultparse;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ResultParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__12=1, T__11=2, T__10=3, T__9=4, T__8=5, T__7=6, T__6=7, T__5=8, T__4=9, 
		T__3=10, T__2=11, T__1=12, T__0=13, STRUCTNAME=14, ID=15, INTEGER=16, 
		FLOAT=17, NUMBER=18, LETTER=19, WS=20;
	public static final String[] tokenNames = {
		"<INVALID>", "'ARG'", "'uml_asc'", "'POST'", "'uml_obj'", "'void'", "'['", 
		"']'", "'='", "'[]'", "'('", "')'", "','", "'PRE'", "STRUCTNAME", "ID", 
		"INTEGER", "FLOAT", "NUMBER", "LETTER", "WS"
	};
	public static final int
		RULE_result = 0, RULE_preStateStr = 1, RULE_postStateStr = 2, RULE_argStr = 3, 
		RULE_stateList = 4, RULE_argList = 5, RULE_argRet = 6, RULE_argSelf = 7, 
		RULE_argArg = 8, RULE_pairedObj = 9, RULE_pairedLiteral = 10, RULE_elms = 11, 
		RULE_objElmList = 12, RULE_ascElmList = 13, RULE_objElm = 14, RULE_ascElm = 15, 
		RULE_literal = 16, RULE_list = 17;
	public static final String[] ruleNames = {
		"result", "preStateStr", "postStateStr", "argStr", "stateList", "argList", 
		"argRet", "argSelf", "argArg", "pairedObj", "pairedLiteral", "elms", "objElmList", 
		"ascElmList", "objElm", "ascElm", "literal", "list"
	};

	@Override
	public String getGrammarFileName() { return "Result.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ResultParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ResultContext extends ParserRuleContext {
		public PreStateStrContext preStateStr() {
			return getRuleContext(PreStateStrContext.class,0);
		}
		public PostStateStrContext postStateStr() {
			return getRuleContext(PostStateStrContext.class,0);
		}
		public ArgStrContext argStr() {
			return getRuleContext(ArgStrContext.class,0);
		}
		public ResultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterResult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitResult(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitResult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultContext result() throws RecognitionException {
		ResultContext _localctx = new ResultContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_result);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(36); preStateStr();
			setState(37); argStr();
			setState(38); postStateStr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PreStateStrContext extends ParserRuleContext {
		public StateListContext stateList() {
			return getRuleContext(StateListContext.class,0);
		}
		public PreStateStrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_preStateStr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterPreStateStr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitPreStateStr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitPreStateStr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PreStateStrContext preStateStr() throws RecognitionException {
		PreStateStrContext _localctx = new PreStateStrContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_preStateStr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40); match(T__0);
			setState(41); match(T__5);
			setState(42); stateList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PostStateStrContext extends ParserRuleContext {
		public StateListContext stateList() {
			return getRuleContext(StateListContext.class,0);
		}
		public PostStateStrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postStateStr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterPostStateStr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitPostStateStr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitPostStateStr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostStateStrContext postStateStr() throws RecognitionException {
		PostStateStrContext _localctx = new PostStateStrContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_postStateStr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44); match(T__10);
			setState(45); match(T__5);
			setState(46); stateList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgStrContext extends ParserRuleContext {
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public ArgStrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argStr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterArgStr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitArgStr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitArgStr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgStrContext argStr() throws RecognitionException {
		ArgStrContext _localctx = new ArgStrContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_argStr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48); match(T__12);
			setState(49); match(T__5);
			setState(50); argList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StateListContext extends ParserRuleContext {
		public ElmsContext elms(int i) {
			return getRuleContext(ElmsContext.class,i);
		}
		public List<ElmsContext> elms() {
			return getRuleContexts(ElmsContext.class);
		}
		public StateListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stateList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterStateList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitStateList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitStateList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StateListContext stateList() throws RecognitionException {
		StateListContext _localctx = new StateListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_stateList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52); match(T__7);
			setState(53); elms();
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(54); match(T__1);
				setState(55); elms();
				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(61); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgListContext extends ParserRuleContext {
		public List<ArgArgContext> argArg() {
			return getRuleContexts(ArgArgContext.class);
		}
		public ArgRetContext argRet() {
			return getRuleContext(ArgRetContext.class,0);
		}
		public ArgSelfContext argSelf() {
			return getRuleContext(ArgSelfContext.class,0);
		}
		public ArgArgContext argArg(int i) {
			return getRuleContext(ArgArgContext.class,i);
		}
		public ArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgListContext argList() throws RecognitionException {
		ArgListContext _localctx = new ArgListContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_argList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63); match(T__7);
			setState(64); argRet();
			setState(65); match(T__1);
			setState(66); argSelf();
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(67); match(T__1);
				setState(68); argArg();
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(74); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgRetContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ObjElmContext objElm() {
			return getRuleContext(ObjElmContext.class,0);
		}
		public ArgRetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argRet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterArgRet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitArgRet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitArgRet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgRetContext argRet() throws RecognitionException {
		ArgRetContext _localctx = new ArgRetContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_argRet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			switch (_input.LA(1)) {
			case T__8:
				{
				setState(76); match(T__8);
				}
				break;
			case T__7:
			case INTEGER:
			case FLOAT:
				{
				setState(77); literal();
				}
				break;
			case T__3:
				{
				setState(78); objElm();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgSelfContext extends ParserRuleContext {
		public PairedObjContext pairedObj() {
			return getRuleContext(PairedObjContext.class,0);
		}
		public ArgSelfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argSelf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterArgSelf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitArgSelf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitArgSelf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgSelfContext argSelf() throws RecognitionException {
		ArgSelfContext _localctx = new ArgSelfContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_argSelf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81); pairedObj();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgArgContext extends ParserRuleContext {
		public PairedLiteralContext pairedLiteral() {
			return getRuleContext(PairedLiteralContext.class,0);
		}
		public PairedObjContext pairedObj() {
			return getRuleContext(PairedObjContext.class,0);
		}
		public ArgArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterArgArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitArgArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitArgArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgArgContext argArg() throws RecognitionException {
		ArgArgContext _localctx = new ArgArgContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_argArg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(83); pairedLiteral();
				}
				break;
			case 2:
				{
				setState(84); pairedObj();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairedObjContext extends ParserRuleContext {
		public List<ObjElmContext> objElm() {
			return getRuleContexts(ObjElmContext.class);
		}
		public ObjElmContext objElm(int i) {
			return getRuleContext(ObjElmContext.class,i);
		}
		public PairedObjContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairedObj; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterPairedObj(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitPairedObj(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitPairedObj(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairedObjContext pairedObj() throws RecognitionException {
		PairedObjContext _localctx = new PairedObjContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_pairedObj);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87); match(T__7);
			setState(90);
			switch (_input.LA(1)) {
			case T__3:
				{
				setState(88); objElm();
				}
				break;
			case T__4:
				{
				setState(89); match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(92); match(T__1);
			setState(93); objElm();
			setState(94); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairedLiteralContext extends ParserRuleContext {
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public PairedLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairedLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterPairedLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitPairedLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitPairedLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairedLiteralContext pairedLiteral() throws RecognitionException {
		PairedLiteralContext _localctx = new PairedLiteralContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_pairedLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96); match(T__7);
			setState(97); literal();
			setState(98); match(T__1);
			setState(99); literal();
			setState(100); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElmsContext extends ParserRuleContext {
		public AscElmListContext ascElmList() {
			return getRuleContext(AscElmListContext.class,0);
		}
		public ObjElmListContext objElmList() {
			return getRuleContext(ObjElmListContext.class,0);
		}
		public ElmsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elms; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterElms(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitElms(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitElms(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElmsContext elms() throws RecognitionException {
		ElmsContext _localctx = new ElmsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_elms);
		try {
			setState(110);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(102); match(T__4);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(103); match(T__7);
				setState(106);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(104); objElmList();
					}
					break;
				case 2:
					{
					setState(105); ascElmList();
					}
					break;
				}
				setState(108); match(T__6);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjElmListContext extends ParserRuleContext {
		public List<ObjElmContext> objElm() {
			return getRuleContexts(ObjElmContext.class);
		}
		public ObjElmContext objElm(int i) {
			return getRuleContext(ObjElmContext.class,i);
		}
		public ObjElmListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objElmList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterObjElmList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitObjElmList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitObjElmList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjElmListContext objElmList() throws RecognitionException {
		ObjElmListContext _localctx = new ObjElmListContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_objElmList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112); objElm();
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(113); match(T__1);
				setState(114); objElm();
				}
				}
				setState(119);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AscElmListContext extends ParserRuleContext {
		public AscElmContext ascElm(int i) {
			return getRuleContext(AscElmContext.class,i);
		}
		public List<AscElmContext> ascElm() {
			return getRuleContexts(AscElmContext.class);
		}
		public AscElmListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ascElmList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterAscElmList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitAscElmList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitAscElmList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AscElmListContext ascElmList() throws RecognitionException {
		AscElmListContext _localctx = new AscElmListContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_ascElmList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120); ascElm();
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(121); match(T__1);
				setState(122); ascElm();
				}
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjElmContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(ResultParser.INTEGER, 0); }
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public TerminalNode STRUCTNAME() { return getToken(ResultParser.STRUCTNAME, 0); }
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public List<ObjElmContext> objElm() {
			return getRuleContexts(ObjElmContext.class);
		}
		public ObjElmContext objElm(int i) {
			return getRuleContext(ObjElmContext.class,i);
		}
		public ObjElmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objElm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterObjElm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitObjElm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitObjElm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjElmContext objElm() throws RecognitionException {
		ObjElmContext _localctx = new ObjElmContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_objElm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128); match(T__3);
			setState(129); match(T__9);
			setState(130); match(T__1);
			setState(131); match(STRUCTNAME);
			setState(132); match(T__1);
			setState(133); match(INTEGER);
			setState(141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(134); match(T__1);
				setState(137);
				switch (_input.LA(1)) {
				case T__7:
				case INTEGER:
				case FLOAT:
					{
					setState(135); literal();
					}
					break;
				case T__3:
					{
					setState(136); objElm();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(144); match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AscElmContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(ResultParser.INTEGER); }
		public TerminalNode STRUCTNAME() { return getToken(ResultParser.STRUCTNAME, 0); }
		public TerminalNode INTEGER(int i) {
			return getToken(ResultParser.INTEGER, i);
		}
		public AscElmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ascElm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterAscElm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitAscElm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitAscElm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AscElmContext ascElm() throws RecognitionException {
		AscElmContext _localctx = new AscElmContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_ascElm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146); match(T__3);
			setState(147); match(T__11);
			setState(148); match(T__1);
			setState(149); match(STRUCTNAME);
			setState(150); match(T__1);
			setState(151); match(INTEGER);
			setState(152); match(T__1);
			setState(153); match(INTEGER);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(154); match(T__1);
				setState(155); match(INTEGER);
				}
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(161); match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(ResultParser.INTEGER, 0); }
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public TerminalNode FLOAT() { return getToken(ResultParser.FLOAT, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_literal);
		try {
			setState(166);
			switch (_input.LA(1)) {
			case INTEGER:
				enterOuterAlt(_localctx, 1);
				{
				setState(163); match(INTEGER);
				}
				break;
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(164); match(FLOAT);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(165); list();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(ResultParser.INTEGER); }
		public ListContext list(int i) {
			return getRuleContext(ListContext.class,i);
		}
		public TerminalNode INTEGER(int i) {
			return getToken(ResultParser.INTEGER, i);
		}
		public List<ListContext> list() {
			return getRuleContexts(ListContext.class);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ResultListener ) ((ResultListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ResultVisitor ) return ((ResultVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168); match(T__7);
			setState(169); match(INTEGER);
			setState(177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(170); match(T__1);
				setState(173);
				switch (_input.LA(1)) {
				case INTEGER:
					{
					setState(171); match(INTEGER);
					}
					break;
				case T__7:
					{
					setState(172); list();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(179);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(180); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\26\u00b9\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\7\6;\n\6\f\6\16\6>\13\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\7\7H\n\7\f\7\16\7K\13\7\3\7\3\7\3\b\3\b\3\b\5\bR\n\b\3\t\3\t\3"+
		"\n\3\n\5\nX\n\n\3\13\3\13\3\13\5\13]\n\13\3\13\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\5\rm\n\r\3\r\3\r\5\rq\n\r\3\16\3\16\3"+
		"\16\7\16v\n\16\f\16\16\16y\13\16\3\17\3\17\3\17\7\17~\n\17\f\17\16\17"+
		"\u0081\13\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u008c\n"+
		"\20\7\20\u008e\n\20\f\20\16\20\u0091\13\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u009f\n\21\f\21\16\21\u00a2\13"+
		"\21\3\21\3\21\3\22\3\22\3\22\5\22\u00a9\n\22\3\23\3\23\3\23\3\23\3\23"+
		"\5\23\u00b0\n\23\7\23\u00b2\n\23\f\23\16\23\u00b5\13\23\3\23\3\23\3\23"+
		"\2\2\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2\2\u00b7\2&\3\2\2"+
		"\2\4*\3\2\2\2\6.\3\2\2\2\b\62\3\2\2\2\n\66\3\2\2\2\fA\3\2\2\2\16Q\3\2"+
		"\2\2\20S\3\2\2\2\22W\3\2\2\2\24Y\3\2\2\2\26b\3\2\2\2\30p\3\2\2\2\32r\3"+
		"\2\2\2\34z\3\2\2\2\36\u0082\3\2\2\2 \u0094\3\2\2\2\"\u00a8\3\2\2\2$\u00aa"+
		"\3\2\2\2&\'\5\4\3\2\'(\5\b\5\2()\5\6\4\2)\3\3\2\2\2*+\7\17\2\2+,\7\n\2"+
		"\2,-\5\n\6\2-\5\3\2\2\2./\7\5\2\2/\60\7\n\2\2\60\61\5\n\6\2\61\7\3\2\2"+
		"\2\62\63\7\3\2\2\63\64\7\n\2\2\64\65\5\f\7\2\65\t\3\2\2\2\66\67\7\b\2"+
		"\2\67<\5\30\r\289\7\16\2\29;\5\30\r\2:8\3\2\2\2;>\3\2\2\2<:\3\2\2\2<="+
		"\3\2\2\2=?\3\2\2\2><\3\2\2\2?@\7\t\2\2@\13\3\2\2\2AB\7\b\2\2BC\5\16\b"+
		"\2CD\7\16\2\2DI\5\20\t\2EF\7\16\2\2FH\5\22\n\2GE\3\2\2\2HK\3\2\2\2IG\3"+
		"\2\2\2IJ\3\2\2\2JL\3\2\2\2KI\3\2\2\2LM\7\t\2\2M\r\3\2\2\2NR\7\7\2\2OR"+
		"\5\"\22\2PR\5\36\20\2QN\3\2\2\2QO\3\2\2\2QP\3\2\2\2R\17\3\2\2\2ST\5\24"+
		"\13\2T\21\3\2\2\2UX\5\26\f\2VX\5\24\13\2WU\3\2\2\2WV\3\2\2\2X\23\3\2\2"+
		"\2Y\\\7\b\2\2Z]\5\36\20\2[]\7\13\2\2\\Z\3\2\2\2\\[\3\2\2\2]^\3\2\2\2^"+
		"_\7\16\2\2_`\5\36\20\2`a\7\t\2\2a\25\3\2\2\2bc\7\b\2\2cd\5\"\22\2de\7"+
		"\16\2\2ef\5\"\22\2fg\7\t\2\2g\27\3\2\2\2hq\7\13\2\2il\7\b\2\2jm\5\32\16"+
		"\2km\5\34\17\2lj\3\2\2\2lk\3\2\2\2mn\3\2\2\2no\7\t\2\2oq\3\2\2\2ph\3\2"+
		"\2\2pi\3\2\2\2q\31\3\2\2\2rw\5\36\20\2st\7\16\2\2tv\5\36\20\2us\3\2\2"+
		"\2vy\3\2\2\2wu\3\2\2\2wx\3\2\2\2x\33\3\2\2\2yw\3\2\2\2z\177\5 \21\2{|"+
		"\7\16\2\2|~\5 \21\2}{\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3"+
		"\2\2\2\u0080\35\3\2\2\2\u0081\177\3\2\2\2\u0082\u0083\7\f\2\2\u0083\u0084"+
		"\7\6\2\2\u0084\u0085\7\16\2\2\u0085\u0086\7\20\2\2\u0086\u0087\7\16\2"+
		"\2\u0087\u008f\7\22\2\2\u0088\u008b\7\16\2\2\u0089\u008c\5\"\22\2\u008a"+
		"\u008c\5\36\20\2\u008b\u0089\3\2\2\2\u008b\u008a\3\2\2\2\u008c\u008e\3"+
		"\2\2\2\u008d\u0088\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f"+
		"\u0090\3\2\2\2\u0090\u0092\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7\r"+
		"\2\2\u0093\37\3\2\2\2\u0094\u0095\7\f\2\2\u0095\u0096\7\4\2\2\u0096\u0097"+
		"\7\16\2\2\u0097\u0098\7\20\2\2\u0098\u0099\7\16\2\2\u0099\u009a\7\22\2"+
		"\2\u009a\u009b\7\16\2\2\u009b\u00a0\7\22\2\2\u009c\u009d\7\16\2\2\u009d"+
		"\u009f\7\22\2\2\u009e\u009c\3\2\2\2\u009f\u00a2\3\2\2\2\u00a0\u009e\3"+
		"\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a3\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3"+
		"\u00a4\7\r\2\2\u00a4!\3\2\2\2\u00a5\u00a9\7\22\2\2\u00a6\u00a9\7\23\2"+
		"\2\u00a7\u00a9\5$\23\2\u00a8\u00a5\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a7"+
		"\3\2\2\2\u00a9#\3\2\2\2\u00aa\u00ab\7\b\2\2\u00ab\u00b3\7\22\2\2\u00ac"+
		"\u00af\7\16\2\2\u00ad\u00b0\7\22\2\2\u00ae\u00b0\5$\23\2\u00af\u00ad\3"+
		"\2\2\2\u00af\u00ae\3\2\2\2\u00b0\u00b2\3\2\2\2\u00b1\u00ac\3\2\2\2\u00b2"+
		"\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b6\3\2"+
		"\2\2\u00b5\u00b3\3\2\2\2\u00b6\u00b7\7\t\2\2\u00b7%\3\2\2\2\21<IQW\\l"+
		"pw\177\u008b\u008f\u00a0\u00a8\u00af\u00b3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}