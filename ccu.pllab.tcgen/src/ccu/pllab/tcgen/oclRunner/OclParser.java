// Generated from Ocl.g4 by ANTLR 4.4
package ccu.pllab.tcgen.oclRunner;
//�o�����O�N�Ojava���Yimport�������A�]��.g4�ɻPOclParser.java�O�s�����Y�A�ҥH���ઽ���bOclParser.java�W�Kimport�A�@���s�ɴN�|�Q�~���A�]���p�G�nimport���ɭԡA�b@header���Y�[
	import ccu.pllab.tcgen.AbstractSyntaxTree.*;
	import java.util.ArrayList;
	import java.util.Set;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OclParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__63=1, T__62=2, T__61=3, T__60=4, T__59=5, T__58=6, T__57=7, T__56=8, 
		T__55=9, T__54=10, T__53=11, T__52=12, T__51=13, T__50=14, T__49=15, T__48=16, 
		T__47=17, T__46=18, T__45=19, T__44=20, T__43=21, T__42=22, T__41=23, 
		T__40=24, T__39=25, T__38=26, T__37=27, T__36=28, T__35=29, T__34=30, 
		T__33=31, T__32=32, T__31=33, T__30=34, T__29=35, T__28=36, T__27=37, 
		T__26=38, T__25=39, T__24=40, T__23=41, T__22=42, T__21=43, T__20=44, 
		T__19=45, T__18=46, T__17=47, T__16=48, T__15=49, T__14=50, T__13=51, 
		T__12=52, T__11=53, T__10=54, T__9=55, T__8=56, T__7=57, T__6=58, T__5=59, 
		T__4=60, T__3=61, T__2=62, T__1=63, T__0=64, NAME=65, INTEGER=66, STRING=67, 
		WHITESPACE=68;
	public static final String[] tokenNames = {
		"<INVALID>", "'endif'", "'Integer = '", "'.mul('", "'{'", "'..'", "'::'", 
		"'='", "'subsequence('", "'Boolean = '", "'implies'", "'('", "'package'", 
		"'context'", "'Integer[][]'", "','", "'false'", "'Sequence'", "'prepend('", 
		"'RandomIndexArray{1..'", "'>='", "'iterate(it: Integer;'", "'<'", "']'", 
		"'pre'", "'@'", "'.add('", "'<>'", "'let'", "'iterate(it2: Integer;'", 
		"'OrderSet'", "'then'", "'+'", "'.mod('", "'Array(Integer)'", "'/'", "'true'", 
		"';'", "'}'", "'if'", "'<='", "'inv'", "'post'", "'.sub('", "'*'", "'Bag'", 
		"'endpackage'", "'.'", "'->'", "'Integer[]'", "':'", "'['", "'|'", "'>'", 
		"'xor'", "'or'", "'.div('", "'else'", "'in'", "'RandomIndexArray{1..size@pre}'", 
		"')'", "'and'", "'not'", "'-'", "'Set'", "NAME", "INTEGER", "STRING", 
		"WHITESPACE"
	};
	public static final int
		RULE_packageDeclarationCS = 0, RULE_packageName = 1, RULE_pathName = 2, 
		RULE_oclExpressions = 3, RULE_constraints = 4, RULE_constraint = 5, RULE_content = 6, 
		RULE_contextDeclaration = 7, RULE_stereotype = 8, RULE_operationContext = 9, 
		RULE_classifierContext = 10, RULE_operationName = 11, RULE_returnType = 12, 
		RULE_formalParameterList = 13, RULE_formalParameterLists = 14, RULE_typeSpecifier = 15, 
		RULE_collectionType = 16, RULE_oclExpression = 17, RULE_letExpression = 18, 
		RULE_expression = 19, RULE_logicalExpression = 20, RULE_relationalExpressions = 21, 
		RULE_logicalOperator = 22, RULE_relationalExpression = 23, RULE_relationalOperator = 24, 
		RULE_additiveExpression = 25, RULE_multiplicativeExpressions = 26, RULE_addOperator = 27, 
		RULE_multiplicativeExpression = 28, RULE_unaryExpressions = 29, RULE_multiplyOperator = 30, 
		RULE_unaryExpression = 31, RULE_unaryOperator = 32, RULE_postfixExpression = 33, 
		RULE_primaryExpressions = 34, RULE_calculator = 35, RULE_primaryExpression = 36, 
		RULE_literal = 37, RULE_booleanExp = 38, RULE_propertyCall = 39, RULE_timeExpression = 40, 
		RULE_propertyCallParameters = 41, RULE_ifExpression = 42, RULE_ifExps = 43, 
		RULE_enumLiteral = 44, RULE_simpleTypeSpecifier = 45, RULE_literalCollection = 46, 
		RULE_collectionItem = 47, RULE_collectionKind = 48, RULE_collectionMethod = 49, 
		RULE_qualifiers = 50, RULE_declarator = 51, RULE_actualParameterList = 52, 
		RULE_actualParameterLists = 53;
	public static final String[] ruleNames = {
		"packageDeclarationCS", "packageName", "pathName", "oclExpressions", "constraints", 
		"constraint", "content", "contextDeclaration", "stereotype", "operationContext", 
		"classifierContext", "operationName", "returnType", "formalParameterList", 
		"formalParameterLists", "typeSpecifier", "collectionType", "oclExpression", 
		"letExpression", "expression", "logicalExpression", "relationalExpressions", 
		"logicalOperator", "relationalExpression", "relationalOperator", "additiveExpression", 
		"multiplicativeExpressions", "addOperator", "multiplicativeExpression", 
		"unaryExpressions", "multiplyOperator", "unaryExpression", "unaryOperator", 
		"postfixExpression", "primaryExpressions", "calculator", "primaryExpression", 
		"literal", "booleanExp", "propertyCall", "timeExpression", "propertyCallParameters", 
		"ifExpression", "ifExps", "enumLiteral", "simpleTypeSpecifier", "literalCollection", 
		"collectionItem", "collectionKind", "collectionMethod", "qualifiers", 
		"declarator", "actualParameterList", "actualParameterLists"
	};

	@Override
	public String getGrammarFileName() { return "Ocl.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public OclParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class PackageDeclarationCSContext extends ParserRuleContext {
		public PackageExp astRoot;
		public PackageNameContext paNa;
		public OclExpressionsContext oclExp;
		public OclExpressionsContext oclExpressions() {
			return getRuleContext(OclExpressionsContext.class,0);
		}
		public PackageNameContext packageName() {
			return getRuleContext(PackageNameContext.class,0);
		}
		public PackageDeclarationCSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDeclarationCS; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPackageDeclarationCS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPackageDeclarationCS(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPackageDeclarationCS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageDeclarationCSContext packageDeclarationCS() throws RecognitionException {
		PackageDeclarationCSContext _localctx = new PackageDeclarationCSContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_packageDeclarationCS);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108); match(T__52);
			setState(109); ((PackageDeclarationCSContext)_localctx).paNa = packageName();
			((PackageDeclarationCSContext)_localctx).astRoot = new PackageExp(((PackageDeclarationCSContext)_localctx).paNa.name);
			setState(111); ((PackageDeclarationCSContext)_localctx).oclExp = oclExpressions();
			//�s���Ҧ�context
					for(AbstractSyntaxTreeNode node :((PackageDeclarationCSContext)_localctx).oclExp.contextNode)
					(_localctx.astRoot).addTreeChildNode(node);
				
			setState(113); match(T__18);
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

	public static class PackageNameContext extends ParserRuleContext {
		public String name;
		public PathNameContext phNa;
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public PackageNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPackageName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPackageName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPackageName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageNameContext packageName() throws RecognitionException {
		PackageNameContext _localctx = new PackageNameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_packageName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115); ((PackageNameContext)_localctx).phNa = pathName();
			((PackageNameContext)_localctx).name = ((PackageNameContext)_localctx).phNa.name;
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

	public static class PathNameContext extends ParserRuleContext {
		public String name;
		public Token NAME;
		public TerminalNode NAME(int i) {
			return getToken(OclParser.NAME, i);
		}
		public List<TerminalNode> NAME() { return getTokens(OclParser.NAME); }
		public PathNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPathName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPathName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPathName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathNameContext pathName() throws RecognitionException {
		PathNameContext _localctx = new PathNameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_pathName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118); ((PathNameContext)_localctx).NAME = match(NAME);
			((PathNameContext)_localctx).name = (((PathNameContext)_localctx).NAME!=null?((PathNameContext)_localctx).NAME.getText():null);
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__58) {
				{
				{
				setState(120); match(T__58);
				setState(121); ((PathNameContext)_localctx).NAME = match(NAME);
				_localctx.name+="::"+(((PathNameContext)_localctx).NAME!=null?((PathNameContext)_localctx).NAME.getText():null);
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

	public static class OclExpressionsContext extends ParserRuleContext {
		public ArrayList<AbstractSyntaxTreeNode> contextNode;
		public ConstraintsContext cons;
		public ConstraintsContext constraints() {
			return getRuleContext(ConstraintsContext.class,0);
		}
		public OclExpressionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oclExpressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterOclExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitOclExpressions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitOclExpressions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OclExpressionsContext oclExpressions() throws RecognitionException {
		OclExpressionsContext _localctx = new OclExpressionsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_oclExpressions);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128); ((OclExpressionsContext)_localctx).cons = constraints();
			(_localctx.contextNode)=(((OclExpressionsContext)_localctx).cons.contextNode);
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

	public static class ConstraintsContext extends ParserRuleContext {
		public ArrayList<AbstractSyntaxTreeNode> contextNode;
		public ConstraintContext con;
		public ConstraintsContext cons;
		public ConstraintContext constraint() {
			return getRuleContext(ConstraintContext.class,0);
		}
		public ConstraintsContext constraints() {
			return getRuleContext(ConstraintsContext.class,0);
		}
		public ConstraintsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraints; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterConstraints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitConstraints(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitConstraints(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintsContext constraints() throws RecognitionException {
		ConstraintsContext _localctx = new ConstraintsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_constraints);
		try {
			setState(137);
			switch (_input.LA(1)) {
			case T__51:
				enterOuterAlt(_localctx, 1);
				{
				setState(131); ((ConstraintsContext)_localctx).con = constraint();

							((ConstraintsContext)_localctx).contextNode = new ArrayList<AbstractSyntaxTreeNode>();
							(_localctx.contextNode).add(((ConstraintsContext)_localctx).con.contextNode);
						
				setState(133); ((ConstraintsContext)_localctx).cons = constraints();
				(_localctx.contextNode).addAll(((ConstraintsContext)_localctx).cons.contextNode);
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 2);
				{
				((ConstraintsContext)_localctx).contextNode = new ArrayList<AbstractSyntaxTreeNode>();
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

	public static class ConstraintContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode contextNode;
		public ContextDeclarationContext coDe;
		public StereotypeContext sType;
		public Token NAME;
		public OclExpressionContext oclExp;
		public ContentContext co;
		public List<OclExpressionContext> oclExpression() {
			return getRuleContexts(OclExpressionContext.class);
		}
		public TerminalNode NAME(int i) {
			return getToken(OclParser.NAME, i);
		}
		public List<TerminalNode> NAME() { return getTokens(OclParser.NAME); }
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public StereotypeContext stereotype(int i) {
			return getRuleContext(StereotypeContext.class,i);
		}
		public ContextDeclarationContext contextDeclaration() {
			return getRuleContext(ContextDeclarationContext.class,0);
		}
		public OclExpressionContext oclExpression(int i) {
			return getRuleContext(OclExpressionContext.class,i);
		}
		public List<StereotypeContext> stereotype() {
			return getRuleContexts(StereotypeContext.class);
		}
		public ConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintContext constraint() throws RecognitionException {
		ConstraintContext _localctx = new ConstraintContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_constraint);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(139); ((ConstraintContext)_localctx).coDe = contextDeclaration();
			setState(149); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(140); ((ConstraintContext)_localctx).sType = stereotype();
					setState(143);
					_la = _input.LA(1);
					if (_la==NAME) {
						{
						setState(141); ((ConstraintContext)_localctx).NAME = match(NAME);
						(((ConstraintContext)_localctx).sType.stereo).setException((((ConstraintContext)_localctx).NAME!=null?((ConstraintContext)_localctx).NAME.getText():null));
						}
					}

					setState(145); match(T__14);
					setState(146); ((ConstraintContext)_localctx).oclExp = oclExpression();

									(((ConstraintContext)_localctx).sType.stereo).addTreeChildNode(((ConstraintContext)_localctx).oclExp.node);
									if(((ConstraintContext)_localctx).coDe.node instanceof OperationContext)
									{
										((OperationContext)(((ConstraintContext)_localctx).coDe.node)).setStereoType(((ConstraintContext)_localctx).sType.stereo);
										//if(((ConstraintContext)_localctx).co.stereo!=null)
										//((OperationContext)(((ConstraintContext)_localctx).coDe.node)).setStereoType(((ConstraintContext)_localctx).co.stereo);
									}
									else{
										((ClassifierContext)(((ConstraintContext)_localctx).coDe.node)).setStereoType(((ConstraintContext)_localctx).sType.stereo);
									}
								
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(151); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(153); ((ConstraintContext)_localctx).co = content();

							//(((ConstraintContext)_localctx).sType.stereo).addTreeChildNode(((ConstraintContext)_localctx).oclExp.node);
							if(((ConstraintContext)_localctx).coDe.node instanceof OperationContext)
							{
							//	((OperationContext)(((ConstraintContext)_localctx).coDe.node)).setStereoType(((ConstraintContext)_localctx).sType.stereo);
								if(((ConstraintContext)_localctx).co.stereo!=null)
								((OperationContext)(((ConstraintContext)_localctx).coDe.node)).setStereoType(((ConstraintContext)_localctx).co.stereo);
							}
							//else{
							//	((ClassifierContext)(((ConstraintContext)_localctx).coDe.node)).setStereoType(((ConstraintContext)_localctx).sType.stereo);
							//}
							((ConstraintContext)_localctx).contextNode = ((ConstraintContext)_localctx).coDe.node;
						
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

	public static class ContentContext extends ParserRuleContext {
		public StereoType stereo;
		public StereotypeContext sType;
		public OclExpressionContext oclExp;
		public OclExpressionContext oclExpression() {
			return getRuleContext(OclExpressionContext.class,0);
		}
		public TerminalNode NAME() { return getToken(OclParser.NAME, 0); }
		public StereotypeContext stereotype() {
			return getRuleContext(StereotypeContext.class,0);
		}
		public ContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_content; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentContext content() throws RecognitionException {
		ContentContext _localctx = new ContentContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_content);
		int _la;
		try {
			setState(165);
			switch (_input.LA(1)) {
			case T__40:
			case T__23:
			case T__22:
				enterOuterAlt(_localctx, 1);
				{
				setState(156); ((ContentContext)_localctx).sType = stereotype();
				setState(158);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(157); match(NAME);
					}
				}

				setState(160); match(T__14);
				setState(161); ((ContentContext)_localctx).oclExp = oclExpression();

						 	((ContentContext)_localctx).stereo = ((ContentContext)_localctx).sType.stereo;
							(_localctx.stereo).addTreeChildNode(((ContentContext)_localctx).oclExp.node);
						
				}
				break;
			case T__51:
			case T__18:
				enterOuterAlt(_localctx, 2);
				{
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

	public static class ContextDeclarationContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public OperationContextContext opCo;
		public ClassifierContextContext clCo;
		public OperationContextContext operationContext() {
			return getRuleContext(OperationContextContext.class,0);
		}
		public ClassifierContextContext classifierContext() {
			return getRuleContext(ClassifierContextContext.class,0);
		}
		public ContextDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterContextDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitContextDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextDeclarationContext contextDeclaration() throws RecognitionException {
		ContextDeclarationContext _localctx = new ContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_contextDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167); match(T__51);
			setState(174);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(168); ((ContextDeclarationContext)_localctx).opCo = operationContext();
				((ContextDeclarationContext)_localctx).node = ((ContextDeclarationContext)_localctx).opCo.operation;
				}
				break;
			case 2:
				{
				setState(171); ((ContextDeclarationContext)_localctx).clCo = classifierContext();
				((ContextDeclarationContext)_localctx).node = ((ContextDeclarationContext)_localctx).clCo.classifier;
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

	public static class StereotypeContext extends ParserRuleContext {
		public StereoType stereo;
		public StereotypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stereotype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterStereotype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitStereotype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitStereotype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StereotypeContext stereotype() throws RecognitionException {
		StereotypeContext _localctx = new StereotypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_stereotype);
		try {
			setState(182);
			switch (_input.LA(1)) {
			case T__40:
				enterOuterAlt(_localctx, 1);
				{
				setState(176); match(T__40);
				((StereotypeContext)_localctx).stereo = new StereoType("precondition");
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(178); match(T__22);
				((StereotypeContext)_localctx).stereo = new StereoType("postcondition");
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(180); match(T__23);
				((StereotypeContext)_localctx).stereo = new StereoType("invariant");
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

	public static class OperationContextContext extends ParserRuleContext {
		public OperationContext operation;
		public Token NAME;
		public OperationNameContext opNa;
		public FormalParameterListContext fpl;
		public ReturnTypeContext rt;
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public TerminalNode NAME() { return getToken(OclParser.NAME, 0); }
		public OperationNameContext operationName() {
			return getRuleContext(OperationNameContext.class,0);
		}
		public OperationContextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationContext; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterOperationContext(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitOperationContext(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitOperationContext(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationContextContext operationContext() throws RecognitionException {
		OperationContextContext _localctx = new OperationContextContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_operationContext);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184); ((OperationContextContext)_localctx).NAME = match(NAME);
			setState(185); match(T__58);
			setState(186); ((OperationContextContext)_localctx).opNa = operationName();

						((OperationContextContext)_localctx).operation = new OperationContext((((OperationContextContext)_localctx).NAME!=null?((OperationContextContext)_localctx).NAME.getText():null),((OperationContextContext)_localctx).opNa.name);
					
			setState(188); match(T__53);
			setState(189); ((OperationContextContext)_localctx).fpl = formalParameterList();
			(_localctx.operation).setParameters(((OperationContextContext)_localctx).fpl.list);
			setState(191); match(T__4);
			setState(196);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(192); match(T__14);
				setState(193); ((OperationContextContext)_localctx).rt = returnType();
				(_localctx.operation).setReturnType(((OperationContextContext)_localctx).rt.reType);
				}
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

	public static class ClassifierContextContext extends ParserRuleContext {
		public ClassifierContext classifier;
		public Token NAME;
		public TerminalNode NAME(int i) {
			return getToken(OclParser.NAME, i);
		}
		public List<TerminalNode> NAME() { return getTokens(OclParser.NAME); }
		public ClassifierContextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifierContext; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterClassifierContext(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitClassifierContext(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitClassifierContext(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContextContext classifierContext() throws RecognitionException {
		ClassifierContextContext _localctx = new ClassifierContextContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_classifierContext);
		try {
			setState(203);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(198); match(NAME);
				setState(199); match(T__14);
				setState(200); match(NAME);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(201); ((ClassifierContextContext)_localctx).NAME = match(NAME);
				((ClassifierContextContext)_localctx).classifier = new ClassifierContext((((ClassifierContextContext)_localctx).NAME!=null?((ClassifierContextContext)_localctx).NAME.getText():null));
				}
				break;
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

	public static class OperationNameContext extends ParserRuleContext {
		public String name;
		public Token NAME;
		public TerminalNode NAME() { return getToken(OclParser.NAME, 0); }
		public OperationNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterOperationName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitOperationName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitOperationName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationNameContext operationName() throws RecognitionException {
		OperationNameContext _localctx = new OperationNameContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_operationName);
		try {
			setState(222);
			switch (_input.LA(1)) {
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(205); ((OperationNameContext)_localctx).NAME = match(NAME);
				((OperationNameContext)_localctx).name = (((OperationNameContext)_localctx).NAME!=null?((OperationNameContext)_localctx).NAME.getText():null);
				}
				break;
			case T__57:
				enterOuterAlt(_localctx, 2);
				{
				setState(207); match(T__57);
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 3);
				{
				setState(208); match(T__32);
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 4);
				{
				setState(209); match(T__1);
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 5);
				{
				setState(210); match(T__42);
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 6);
				{
				setState(211); match(T__24);
				}
				break;
			case T__44:
				enterOuterAlt(_localctx, 7);
				{
				setState(212); match(T__44);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 8);
				{
				setState(213); match(T__11);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 9);
				{
				setState(214); match(T__29);
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 10);
				{
				setState(215); match(T__20);
				}
				break;
			case T__37:
				enterOuterAlt(_localctx, 11);
				{
				setState(216); match(T__37);
				}
				break;
			case T__54:
				enterOuterAlt(_localctx, 12);
				{
				setState(217); match(T__54);
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 13);
				{
				setState(218); match(T__2);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 14);
				{
				setState(219); match(T__9);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 15);
				{
				setState(220); match(T__10);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 16);
				{
				setState(221); match(T__3);
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

	public static class ReturnTypeContext extends ParserRuleContext {
		public String reType;
		public TypeSpecifierContext ts;
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_returnType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224); ((ReturnTypeContext)_localctx).ts = typeSpecifier();
			((ReturnTypeContext)_localctx).reType = ((ReturnTypeContext)_localctx).ts.value;
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

	public static class FormalParameterListContext extends ParserRuleContext {
		public ArrayList<PropertyCallExp> list;
		public Token NAME;
		public TypeSpecifierContext tySpec;
		public FormalParameterListsContext fpls;
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public TerminalNode NAME() { return getToken(OclParser.NAME, 0); }
		public FormalParameterListsContext formalParameterLists() {
			return getRuleContext(FormalParameterListsContext.class,0);
		}
		public FormalParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterFormalParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitFormalParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitFormalParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalParameterListContext formalParameterList() throws RecognitionException {
		FormalParameterListContext _localctx = new FormalParameterListContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_formalParameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			((FormalParameterListContext)_localctx).list = new ArrayList<PropertyCallExp> ();
			setState(234);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(228); ((FormalParameterListContext)_localctx).NAME = match(NAME);
				setState(229); match(T__14);
				setState(230); ((FormalParameterListContext)_localctx).tySpec = typeSpecifier();
				setState(231); ((FormalParameterListContext)_localctx).fpls = formalParameterLists();

								PropertyCallExp var=new PropertyCallExp((((FormalParameterListContext)_localctx).NAME!=null?((FormalParameterListContext)_localctx).NAME.getText():null));
								var.setType(((FormalParameterListContext)_localctx).tySpec.value);
								(_localctx.list).add(var);
								(_localctx.list).addAll(((FormalParameterListContext)_localctx).fpls.list);
							
				}
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

	public static class FormalParameterListsContext extends ParserRuleContext {
		public ArrayList<PropertyCallExp> list;
		public Token NAME;
		public TypeSpecifierContext tySpec;
		public FormalParameterListsContext fpls;
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public TerminalNode NAME() { return getToken(OclParser.NAME, 0); }
		public FormalParameterListsContext formalParameterLists() {
			return getRuleContext(FormalParameterListsContext.class,0);
		}
		public FormalParameterListsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameterLists; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterFormalParameterLists(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitFormalParameterLists(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitFormalParameterLists(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalParameterListsContext formalParameterLists() throws RecognitionException {
		FormalParameterListsContext _localctx = new FormalParameterListsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_formalParameterLists);
		try {
			setState(244);
			switch (_input.LA(1)) {
			case T__49:
				enterOuterAlt(_localctx, 1);
				{
				setState(236); match(T__49);
				setState(237); ((FormalParameterListsContext)_localctx).NAME = match(NAME);
				setState(238); match(T__14);
				setState(239); ((FormalParameterListsContext)_localctx).tySpec = typeSpecifier();
				setState(240); ((FormalParameterListsContext)_localctx).fpls = formalParameterLists();

						((FormalParameterListsContext)_localctx).list = new ArrayList<PropertyCallExp> ();
						PropertyCallExp var=new PropertyCallExp((((FormalParameterListsContext)_localctx).NAME!=null?((FormalParameterListsContext)_localctx).NAME.getText():null));
						var.setType(((FormalParameterListsContext)_localctx).tySpec.value);
						(_localctx.list).add(var);
						(_localctx.list).addAll(((FormalParameterListsContext)_localctx).fpls.list);
					
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				((FormalParameterListsContext)_localctx).list = new ArrayList<PropertyCallExp> ();
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

	public static class TypeSpecifierContext extends ParserRuleContext {
		public String value;
		public SimpleTypeSpecifierContext sts;
		public CollectionKindContext col;
		public SimpleTypeSpecifierContext simpleTypeSpecifier() {
			return getRuleContext(SimpleTypeSpecifierContext.class,0);
		}
		public CollectionKindContext collectionKind() {
			return getRuleContext(CollectionKindContext.class,0);
		}
		public TypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSpecifierContext typeSpecifier() throws RecognitionException {
		TypeSpecifierContext _localctx = new TypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_typeSpecifier);
		try {
			setState(252);
			switch (_input.LA(1)) {
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(246); ((TypeSpecifierContext)_localctx).sts = simpleTypeSpecifier();
				((TypeSpecifierContext)_localctx).value = ((TypeSpecifierContext)_localctx).sts.name;
				}
				break;
			case T__50:
			case T__47:
			case T__34:
			case T__19:
			case T__15:
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(249); ((TypeSpecifierContext)_localctx).col = collectionKind();
				((TypeSpecifierContext)_localctx).value = ((TypeSpecifierContext)_localctx).col.type;
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

	public static class CollectionTypeContext extends ParserRuleContext {
		public SimpleTypeSpecifierContext simpleTypeSpecifier() {
			return getRuleContext(SimpleTypeSpecifierContext.class,0);
		}
		public CollectionKindContext collectionKind() {
			return getRuleContext(CollectionKindContext.class,0);
		}
		public CollectionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterCollectionType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitCollectionType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionTypeContext collectionType() throws RecognitionException {
		CollectionTypeContext _localctx = new CollectionTypeContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254); collectionKind();
			setState(255); match(T__53);
			setState(256); simpleTypeSpecifier();
			setState(257); match(T__4);
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

	public static class OclExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public ExpressionContext ex;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public OclExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oclExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterOclExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitOclExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitOclExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OclExpressionContext oclExpression() throws RecognitionException {
		OclExpressionContext _localctx = new OclExpressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_oclExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259); ((OclExpressionContext)_localctx).ex = expression();
			((OclExpressionContext)_localctx).node = ((OclExpressionContext)_localctx).ex.node;
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

	public static class LetExpressionContext extends ParserRuleContext {
		public OperatorExp operator;
		public Token NAME;
		public ExpressionContext ex;
		public Token INTEGER;
		public TerminalNode INTEGER() { return getToken(OclParser.INTEGER, 0); }
		public TerminalNode NAME() { return getToken(OclParser.NAME, 0); }
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public LetExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterLetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitLetExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLetExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetExpressionContext letExpression() throws RecognitionException {
		LetExpressionContext _localctx = new LetExpressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_letExpression);
		try {
			setState(296);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(262); match(T__36);
				setState(263); ((LetExpressionContext)_localctx).NAME = match(NAME);
				PropertyCallExp var=new PropertyCallExp((((LetExpressionContext)_localctx).NAME!=null?((LetExpressionContext)_localctx).NAME.getText():null));
				setState(265); match(T__57);
				setState(266); ((LetExpressionContext)_localctx).ex = expression();
				setState(267); match(T__6);

							OperatorExp eq=new OperatorExp("=");
							eq.setOperand(var,((LetExpressionContext)_localctx).ex.node);
							((LetExpressionContext)_localctx).operator = new OperatorExp("and");
						
				setState(269); ((LetExpressionContext)_localctx).ex = expression();
				(_localctx.operator).setOperand(eq,((LetExpressionContext)_localctx).ex.node);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(272); match(T__36);
				setState(273); match(NAME);
				setState(274); match(T__14);
				setState(275); match(T__30);
				setState(276); match(T__57);
				setState(277); match(T__5);
				PropertyCallExp var=new PropertyCallExp("randomIndexArray");
						 	PropertyCallExp size=new PropertyCallExp("size");
						 	size.setTimeExpression();
						 	size.setType("int");
						 	PropertyCallExp index= new PropertyCallExp("arrayIndex");
						 	index.setType("int");
						 	var.addParameters(size,index);
						 
				setState(279); match(T__6);
				setState(280); ((LetExpressionContext)_localctx).ex = expression();

						 	OperatorExp and=new OperatorExp("and");
						 	and.setOperand(var,((LetExpressionContext)_localctx).ex.node);
						 	((LetExpressionContext)_localctx).operator = and;
						 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(283); match(T__36);
				setState(284); match(NAME);
				setState(285); match(T__14);
				setState(286); match(T__30);
				setState(287); match(T__57);
				setState(288); match(T__45);
				setState(289); ((LetExpressionContext)_localctx).INTEGER = match(INTEGER);
				setState(290); match(T__26);
				PropertyCallExp var=new PropertyCallExp("randomIndexArray");
						 	LiteralExp liter=new LiteralExp("Integer",(((LetExpressionContext)_localctx).INTEGER!=null?((LetExpressionContext)_localctx).INTEGER.getText():null));
						 	PropertyCallExp index= new PropertyCallExp("arrayIndex");
						 	index.setType("int");
						 	var.addParameters(liter,index);
						 
				setState(292); match(T__6);
				setState(293); ((LetExpressionContext)_localctx).ex = expression();

						 	OperatorExp and=new OperatorExp("and");
						 	and.setOperand(var,((LetExpressionContext)_localctx).ex.node);
						 	((LetExpressionContext)_localctx).operator = and;
						 
				}
				break;
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

	public static class ExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public LogicalExpressionContext logicExp;
		public LogicalExpressionContext logicalExpression() {
			return getRuleContext(LogicalExpressionContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(298); ((ExpressionContext)_localctx).logicExp = logicalExpression();

						((ExpressionContext)_localctx).node = ((ExpressionContext)_localctx).logicExp.node;
					
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

	public static class LogicalExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public RelationalExpressionContext reExp;
		public RelationalExpressionsContext relExp;
		public RelationalExpressionContext relationalExpression() {
			return getRuleContext(RelationalExpressionContext.class,0);
		}
		public RelationalExpressionsContext relationalExpressions() {
			return getRuleContext(RelationalExpressionsContext.class,0);
		}
		public LogicalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterLogicalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitLogicalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLogicalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalExpressionContext logicalExpression() throws RecognitionException {
		LogicalExpressionContext _localctx = new LogicalExpressionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_logicalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(301); ((LogicalExpressionContext)_localctx).reExp = relationalExpression();
			setState(302); ((LogicalExpressionContext)_localctx).relExp = relationalExpressions(((LogicalExpressionContext)_localctx).reExp.node);

					 	((LogicalExpressionContext)_localctx).node = ((LogicalExpressionContext)_localctx).relExp.node;
					
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

	public static class RelationalExpressionsContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode reExp1;
		public AbstractSyntaxTreeNode node;
		public LogicalOperatorContext logicOper;
		public RelationalExpressionContext reExp2;
		public RelationalExpressionsContext reExps;
		public RelationalExpressionContext relationalExpression() {
			return getRuleContext(RelationalExpressionContext.class,0);
		}
		public RelationalExpressionsContext relationalExpressions() {
			return getRuleContext(RelationalExpressionsContext.class,0);
		}
		public LogicalOperatorContext logicalOperator() {
			return getRuleContext(LogicalOperatorContext.class,0);
		}
		public RelationalExpressionsContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public RelationalExpressionsContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode reExp1) {
			super(parent, invokingState);
			this.reExp1 = reExp1;
		}
		@Override public int getRuleIndex() { return RULE_relationalExpressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterRelationalExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitRelationalExpressions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitRelationalExpressions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalExpressionsContext relationalExpressions(AbstractSyntaxTreeNode reExp1) throws RecognitionException {
		RelationalExpressionsContext _localctx = new RelationalExpressionsContext(_ctx, getState(), reExp1);
		enterRule(_localctx, 42, RULE_relationalExpressions);
		try {
			setState(311);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(305); ((RelationalExpressionsContext)_localctx).logicOper = logicalOperator();
				setState(306); ((RelationalExpressionsContext)_localctx).reExp2 = relationalExpression();
				setState(307); ((RelationalExpressionsContext)_localctx).reExps = relationalExpressions(((RelationalExpressionsContext)_localctx).reExp2.node);

							(((RelationalExpressionsContext)_localctx).logicOper.operator).setOperand(_localctx.reExp1,((RelationalExpressionsContext)_localctx).reExps.node);
							((RelationalExpressionsContext)_localctx).node = ((RelationalExpressionsContext)_localctx).logicOper.operator;
							
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				((RelationalExpressionsContext)_localctx).node = _localctx.reExp1;
				}
				break;
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

	public static class LogicalOperatorContext extends ParserRuleContext {
		public String logic;
		public OperatorExp operator;
		public LogicalOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterLogicalOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitLogicalOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLogicalOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalOperatorContext logicalOperator() throws RecognitionException {
		LogicalOperatorContext _localctx = new LogicalOperatorContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_logicalOperator);
		try {
			setState(321);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(313); match(T__3);
				((LogicalOperatorContext)_localctx).logic = "and";((LogicalOperatorContext)_localctx).operator = new OperatorExp("and");
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(315); match(T__9);
				((LogicalOperatorContext)_localctx).logic = "or";((LogicalOperatorContext)_localctx).operator = new OperatorExp("or");
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 3);
				{
				setState(317); match(T__10);
				((LogicalOperatorContext)_localctx).logic = "xor";((LogicalOperatorContext)_localctx).operator = new OperatorExp("xor");
				}
				break;
			case T__54:
				enterOuterAlt(_localctx, 4);
				{
				setState(319); match(T__54);
				((LogicalOperatorContext)_localctx).logic = "implies";((LogicalOperatorContext)_localctx).operator = new OperatorExp("implies");
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

	public static class RelationalExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public AdditiveExpressionContext addEx1;
		public RelationalOperatorContext relation;
		public AdditiveExpressionContext addEx2;
		public AdditiveExpressionContext additiveExpression(int i) {
			return getRuleContext(AdditiveExpressionContext.class,i);
		}
		public List<AdditiveExpressionContext> additiveExpression() {
			return getRuleContexts(AdditiveExpressionContext.class);
		}
		public RelationalOperatorContext relationalOperator() {
			return getRuleContext(RelationalOperatorContext.class,0);
		}
		public RelationalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterRelationalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitRelationalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitRelationalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalExpressionContext relationalExpression() throws RecognitionException {
		RelationalExpressionContext _localctx = new RelationalExpressionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_relationalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323); ((RelationalExpressionContext)_localctx).addEx1 = additiveExpression();
			((RelationalExpressionContext)_localctx).node = ((RelationalExpressionContext)_localctx).addEx1.node;
			setState(329);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(325); ((RelationalExpressionContext)_localctx).relation = relationalOperator();
				setState(326); ((RelationalExpressionContext)_localctx).addEx2 = additiveExpression();

								(((RelationalExpressionContext)_localctx).relation.operator).setOperand (((RelationalExpressionContext)_localctx).addEx1.node,((RelationalExpressionContext)_localctx).addEx2.node);
								((RelationalExpressionContext)_localctx).node = ((RelationalExpressionContext)_localctx).relation.operator;
							
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

	public static class RelationalOperatorContext extends ParserRuleContext {
		public String relation;
		public OperatorExp operator;
		public RelationalOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterRelationalOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitRelationalOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitRelationalOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalOperatorContext relationalOperator() throws RecognitionException {
		RelationalOperatorContext _localctx = new RelationalOperatorContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_relationalOperator);
		try {
			setState(343);
			switch (_input.LA(1)) {
			case T__57:
				enterOuterAlt(_localctx, 1);
				{
				setState(331); match(T__57);
				((RelationalOperatorContext)_localctx).relation = "=";((RelationalOperatorContext)_localctx).operator = new OperatorExp ("=");
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 2);
				{
				setState(333); match(T__11);
				((RelationalOperatorContext)_localctx).relation = ">";((RelationalOperatorContext)_localctx).operator = new OperatorExp (">");
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 3);
				{
				setState(335); match(T__42);
				((RelationalOperatorContext)_localctx).relation = "<";((RelationalOperatorContext)_localctx).operator = new OperatorExp ("<");
				}
				break;
			case T__44:
				enterOuterAlt(_localctx, 4);
				{
				setState(337); match(T__44);
				((RelationalOperatorContext)_localctx).relation = ">=";((RelationalOperatorContext)_localctx).operator = new OperatorExp (">=");
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 5);
				{
				setState(339); match(T__24);
				((RelationalOperatorContext)_localctx).relation = "<=";((RelationalOperatorContext)_localctx).operator = new OperatorExp ("<=");
				}
				break;
			case T__37:
				enterOuterAlt(_localctx, 6);
				{
				setState(341); match(T__37);
				((RelationalOperatorContext)_localctx).relation = "<>";((RelationalOperatorContext)_localctx).operator = new OperatorExp ("<>");
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

	public static class AdditiveExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public MultiplicativeExpressionContext mulEx1;
		public MultiplicativeExpressionsContext mulExs;
		public MultiplicativeExpressionContext multiplicativeExpression() {
			return getRuleContext(MultiplicativeExpressionContext.class,0);
		}
		public MultiplicativeExpressionsContext multiplicativeExpressions() {
			return getRuleContext(MultiplicativeExpressionsContext.class,0);
		}
		public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterAdditiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitAdditiveExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitAdditiveExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
		AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_additiveExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345); ((AdditiveExpressionContext)_localctx).mulEx1 = multiplicativeExpression();
			setState(346); ((AdditiveExpressionContext)_localctx).mulExs = multiplicativeExpressions(((AdditiveExpressionContext)_localctx).mulEx1.node);

					((AdditiveExpressionContext)_localctx).node = ((AdditiveExpressionContext)_localctx).mulExs.node;
				
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

	public static class MultiplicativeExpressionsContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode mulEx1;
		public AbstractSyntaxTreeNode node;
		public AddOperatorContext addOp;
		public MultiplicativeExpressionContext mulEx2;
		public MultiplicativeExpressionsContext mulExs;
		public MultiplicativeExpressionContext multiplicativeExpression() {
			return getRuleContext(MultiplicativeExpressionContext.class,0);
		}
		public MultiplicativeExpressionsContext multiplicativeExpressions() {
			return getRuleContext(MultiplicativeExpressionsContext.class,0);
		}
		public AddOperatorContext addOperator() {
			return getRuleContext(AddOperatorContext.class,0);
		}
		public MultiplicativeExpressionsContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public MultiplicativeExpressionsContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode mulEx1) {
			super(parent, invokingState);
			this.mulEx1 = mulEx1;
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterMultiplicativeExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitMultiplicativeExpressions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMultiplicativeExpressions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExpressionsContext multiplicativeExpressions(AbstractSyntaxTreeNode mulEx1) throws RecognitionException {
		MultiplicativeExpressionsContext _localctx = new MultiplicativeExpressionsContext(_ctx, getState(), mulEx1);
		enterRule(_localctx, 52, RULE_multiplicativeExpressions);
		try {
			setState(355);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(349); ((MultiplicativeExpressionsContext)_localctx).addOp = addOperator();
				setState(350); ((MultiplicativeExpressionsContext)_localctx).mulEx2 = multiplicativeExpression();
				setState(351); ((MultiplicativeExpressionsContext)_localctx).mulExs = multiplicativeExpressions(((MultiplicativeExpressionsContext)_localctx).mulEx2.node);

						(((MultiplicativeExpressionsContext)_localctx).addOp.operator).setOperand(_localctx.mulEx1,((MultiplicativeExpressionsContext)_localctx).mulExs.node);
						((MultiplicativeExpressionsContext)_localctx).node = ((MultiplicativeExpressionsContext)_localctx).addOp.operator;
					
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				((MultiplicativeExpressionsContext)_localctx).node = _localctx.mulEx1;
				}
				break;
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

	public static class AddOperatorContext extends ParserRuleContext {
		public String type;
		public OperatorExp operator;
		public AddOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterAddOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitAddOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitAddOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AddOperatorContext addOperator() throws RecognitionException {
		AddOperatorContext _localctx = new AddOperatorContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_addOperator);
		try {
			setState(361);
			switch (_input.LA(1)) {
			case T__32:
				enterOuterAlt(_localctx, 1);
				{
				setState(357); match(T__32);
				((AddOperatorContext)_localctx).type = "+";((AddOperatorContext)_localctx).operator = new OperatorExp ("+");
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(359); match(T__1);
				((AddOperatorContext)_localctx).type = "-";((AddOperatorContext)_localctx).operator = new OperatorExp ("-");
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

	public static class MultiplicativeExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public UnaryExpressionContext unEx1;
		public UnaryExpressionsContext unExs;
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public UnaryExpressionsContext unaryExpressions() {
			return getRuleContext(UnaryExpressionsContext.class,0);
		}
		public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterMultiplicativeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitMultiplicativeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMultiplicativeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExpressionContext multiplicativeExpression() throws RecognitionException {
		MultiplicativeExpressionContext _localctx = new MultiplicativeExpressionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_multiplicativeExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363); ((MultiplicativeExpressionContext)_localctx).unEx1 = unaryExpression();
			setState(364); ((MultiplicativeExpressionContext)_localctx).unExs = unaryExpressions(((MultiplicativeExpressionContext)_localctx).unEx1.node);

					((MultiplicativeExpressionContext)_localctx).node = ((MultiplicativeExpressionContext)_localctx).unExs.node;
					
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

	public static class UnaryExpressionsContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode unEx1;
		public AbstractSyntaxTreeNode node;
		public MultiplyOperatorContext mulOp;
		public UnaryExpressionContext unEx2;
		public UnaryExpressionsContext unExs;
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public UnaryExpressionsContext unaryExpressions() {
			return getRuleContext(UnaryExpressionsContext.class,0);
		}
		public MultiplyOperatorContext multiplyOperator() {
			return getRuleContext(MultiplyOperatorContext.class,0);
		}
		public UnaryExpressionsContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public UnaryExpressionsContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode unEx1) {
			super(parent, invokingState);
			this.unEx1 = unEx1;
		}
		@Override public int getRuleIndex() { return RULE_unaryExpressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterUnaryExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitUnaryExpressions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitUnaryExpressions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpressionsContext unaryExpressions(AbstractSyntaxTreeNode unEx1) throws RecognitionException {
		UnaryExpressionsContext _localctx = new UnaryExpressionsContext(_ctx, getState(), unEx1);
		enterRule(_localctx, 58, RULE_unaryExpressions);
		try {
			setState(373);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(367); ((UnaryExpressionsContext)_localctx).mulOp = multiplyOperator();
				setState(368); ((UnaryExpressionsContext)_localctx).unEx2 = unaryExpression();
				setState(369); ((UnaryExpressionsContext)_localctx).unExs = unaryExpressions(((UnaryExpressionsContext)_localctx).unEx2.node);

						(((UnaryExpressionsContext)_localctx).mulOp.operator).setOperand(_localctx.unEx1,((UnaryExpressionsContext)_localctx).unExs.node);
						((UnaryExpressionsContext)_localctx).node = ((UnaryExpressionsContext)_localctx).mulOp.operator;
					
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				((UnaryExpressionsContext)_localctx).node = _localctx.unEx1;
				}
				break;
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

	public static class MultiplyOperatorContext extends ParserRuleContext {
		public String type;
		public OperatorExp operator;
		public MultiplyOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplyOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterMultiplyOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitMultiplyOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMultiplyOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplyOperatorContext multiplyOperator() throws RecognitionException {
		MultiplyOperatorContext _localctx = new MultiplyOperatorContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_multiplyOperator);
		try {
			setState(379);
			switch (_input.LA(1)) {
			case T__20:
				enterOuterAlt(_localctx, 1);
				{
				setState(375); match(T__20);
				((MultiplyOperatorContext)_localctx).type = "*";((MultiplyOperatorContext)_localctx).operator = new OperatorExp ("*");
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 2);
				{
				setState(377); match(T__29);
				((MultiplyOperatorContext)_localctx).type = "/";((MultiplyOperatorContext)_localctx).operator = new OperatorExp ("/");
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

	public static class UnaryExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public UnaryOperatorContext unOp;
		public PostfixExpressionContext poEx;
		public PostfixExpressionContext postfixExpression() {
			return getRuleContext(PostfixExpressionContext.class,0);
		}
		public UnaryOperatorContext unaryOperator() {
			return getRuleContext(UnaryOperatorContext.class,0);
		}
		public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpressionContext unaryExpression() throws RecognitionException {
		UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_unaryExpression);
		try {
			setState(388);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(381); ((UnaryExpressionContext)_localctx).unOp = unaryOperator();
				setState(382); ((UnaryExpressionContext)_localctx).poEx = postfixExpression();

						(((UnaryExpressionContext)_localctx).unOp.operator).setUnaryOperand(((UnaryExpressionContext)_localctx).poEx.node);
						((UnaryExpressionContext)_localctx).node = ((UnaryExpressionContext)_localctx).unOp.operator;
					
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(385); ((UnaryExpressionContext)_localctx).poEx = postfixExpression();
				((UnaryExpressionContext)_localctx).node = ((UnaryExpressionContext)_localctx).poEx.node;
				}
				break;
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

	public static class UnaryOperatorContext extends ParserRuleContext {
		public String type;
		public OperatorExp operator;
		public UnaryOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterUnaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitUnaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitUnaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryOperatorContext unaryOperator() throws RecognitionException {
		UnaryOperatorContext _localctx = new UnaryOperatorContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_unaryOperator);
		try {
			setState(394);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(390); match(T__1);
				((UnaryOperatorContext)_localctx).type = "-";((UnaryOperatorContext)_localctx).operator = new OperatorExp("-");
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(392); match(T__2);
				((UnaryOperatorContext)_localctx).type = "not";((UnaryOperatorContext)_localctx).operator = new OperatorExp("not");
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

	public static class PostfixExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public PrimaryExpressionContext priEx;
		public PrimaryExpressionsContext pes;
		public CalculatorContext cal;
		public CalculatorContext calculator() {
			return getRuleContext(CalculatorContext.class,0);
		}
		public PrimaryExpressionsContext primaryExpressions() {
			return getRuleContext(PrimaryExpressionsContext.class,0);
		}
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public PostfixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPostfixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPostfixExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPostfixExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostfixExpressionContext postfixExpression() throws RecognitionException {
		PostfixExpressionContext _localctx = new PostfixExpressionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_postfixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396); ((PostfixExpressionContext)_localctx).priEx = primaryExpression();
			setState(397); ((PostfixExpressionContext)_localctx).pes = primaryExpressions(((PostfixExpressionContext)_localctx).priEx.node);
			setState(402);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(398); ((PostfixExpressionContext)_localctx).cal = calculator(((PostfixExpressionContext)_localctx).pes.node);
				((PostfixExpressionContext)_localctx).node = ((PostfixExpressionContext)_localctx).cal.node;
				}
				break;
			case 2:
				{
				((PostfixExpressionContext)_localctx).node = ((PostfixExpressionContext)_localctx).pes.node;
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

	public static class PrimaryExpressionsContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode priEx;
		public AbstractSyntaxTreeNode node;
		public PropertyCallContext prop;
		public PrimaryExpressionsContext pes;
		public PropertyCallContext propertyCall() {
			return getRuleContext(PropertyCallContext.class,0);
		}
		public PrimaryExpressionsContext primaryExpressions() {
			return getRuleContext(PrimaryExpressionsContext.class,0);
		}
		public PrimaryExpressionsContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public PrimaryExpressionsContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode priEx) {
			super(parent, invokingState);
			this.priEx = priEx;
		}
		@Override public int getRuleIndex() { return RULE_primaryExpressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPrimaryExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPrimaryExpressions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPrimaryExpressions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionsContext primaryExpressions(AbstractSyntaxTreeNode priEx) throws RecognitionException {
		PrimaryExpressionsContext _localctx = new PrimaryExpressionsContext(_ctx, getState(), priEx);
		enterRule(_localctx, 68, RULE_primaryExpressions);
		try {
			setState(415);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(408);
				switch (_input.LA(1)) {
				case T__17:
					{
					setState(404); match(T__17);
					((PrimaryExpressionsContext)_localctx).node = new OperatorExp(".");
					}
					break;
				case T__16:
					{
					setState(406); match(T__16);
					((PrimaryExpressionsContext)_localctx).node = new OperatorExp("->");
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(410); ((PrimaryExpressionsContext)_localctx).prop = propertyCall();
				setState(411); ((PrimaryExpressionsContext)_localctx).pes = primaryExpressions(((PrimaryExpressionsContext)_localctx).prop.property);
				((OperatorExp)_localctx.node).setOperand(_localctx.priEx,((PrimaryExpressionsContext)_localctx).pes.node);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				((PrimaryExpressionsContext)_localctx).node = _localctx.priEx;
				}
				break;
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

	public static class CalculatorContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode priEx;
		public AbstractSyntaxTreeNode node;
		public ExpressionContext ex;
		public CalculatorContext cal;
		public CalculatorContext calculator() {
			return getRuleContext(CalculatorContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public CalculatorContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public CalculatorContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode priEx) {
			super(parent, invokingState);
			this.priEx = priEx;
		}
		@Override public int getRuleIndex() { return RULE_calculator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterCalculator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitCalculator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCalculator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CalculatorContext calculator(AbstractSyntaxTreeNode priEx) throws RecognitionException {
		CalculatorContext _localctx = new CalculatorContext(_ctx, getState(), priEx);
		enterRule(_localctx, 70, RULE_calculator);
		try {
			setState(458);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(417); match(T__38);
				((CalculatorContext)_localctx).node = new OperatorExp("+");
				setState(419); ((CalculatorContext)_localctx).ex = expression();
				((OperatorExp)_localctx.node).setOperand(_localctx.priEx,((CalculatorContext)_localctx).ex.node);
				setState(421); match(T__4);
				setState(422); ((CalculatorContext)_localctx).cal = calculator(_localctx.node);
				((CalculatorContext)_localctx).node = ((CalculatorContext)_localctx).cal.node;
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(425); match(T__21);
				((CalculatorContext)_localctx).node = new OperatorExp("-");
				setState(427); ((CalculatorContext)_localctx).ex = expression();
				((OperatorExp)_localctx.node).setOperand(_localctx.priEx,((CalculatorContext)_localctx).ex.node);
				setState(429); match(T__4);
				setState(430); ((CalculatorContext)_localctx).cal = calculator(_localctx.node);
				((CalculatorContext)_localctx).node = ((CalculatorContext)_localctx).cal.node;
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(433); match(T__61);
				((CalculatorContext)_localctx).node = new OperatorExp("*");
				setState(435); ((CalculatorContext)_localctx).ex = expression();
				((OperatorExp)_localctx.node).setOperand(_localctx.priEx,((CalculatorContext)_localctx).ex.node);
				setState(437); match(T__4);
				setState(438); ((CalculatorContext)_localctx).cal = calculator(_localctx.node);
				((CalculatorContext)_localctx).node = ((CalculatorContext)_localctx).cal.node;
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(441); match(T__8);
				((CalculatorContext)_localctx).node = new OperatorExp("/");
				setState(443); ((CalculatorContext)_localctx).ex = expression();
				((OperatorExp)_localctx.node).setOperand(_localctx.priEx,((CalculatorContext)_localctx).ex.node);
				setState(445); match(T__4);
				setState(446); ((CalculatorContext)_localctx).cal = calculator(_localctx.node);
				((CalculatorContext)_localctx).node = ((CalculatorContext)_localctx).cal.node;
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(449); match(T__31);
				((CalculatorContext)_localctx).node = new OperatorExp("%");
				setState(451); ((CalculatorContext)_localctx).ex = expression();
				((OperatorExp)_localctx.node).setOperand(_localctx.priEx,((CalculatorContext)_localctx).ex.node);
				setState(453); match(T__4);
				setState(454); ((CalculatorContext)_localctx).cal = calculator(_localctx.node);
				((CalculatorContext)_localctx).node = ((CalculatorContext)_localctx).cal.node;
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				((CalculatorContext)_localctx).node = _localctx.priEx;
				}
				break;
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

	public static class PrimaryExpressionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public LiteralCollectionContext literCol;
		public LiteralContext Literal;
		public PropertyCallContext prop;
		public ExpressionContext ex;
		public IfExpressionContext ifExp;
		public LetExpressionContext letExp;
		public PropertyCallContext propertyCall() {
			return getRuleContext(PropertyCallContext.class,0);
		}
		public LiteralCollectionContext literalCollection() {
			return getRuleContext(LiteralCollectionContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public IfExpressionContext ifExpression() {
			return getRuleContext(IfExpressionContext.class,0);
		}
		public LetExpressionContext letExpression() {
			return getRuleContext(LetExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_primaryExpression);
		try {
			setState(480);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(460); ((PrimaryExpressionContext)_localctx).literCol = literalCollection();
				((PrimaryExpressionContext)_localctx).node = ((PrimaryExpressionContext)_localctx).literCol.node;
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(463); ((PrimaryExpressionContext)_localctx).Literal = literal();

				  		((PrimaryExpressionContext)_localctx).node = ((PrimaryExpressionContext)_localctx).Literal.liter;
				  		
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(466); ((PrimaryExpressionContext)_localctx).prop = propertyCall();
				((PrimaryExpressionContext)_localctx).node = ((PrimaryExpressionContext)_localctx).prop.property;
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(469); match(T__53);
				setState(470); ((PrimaryExpressionContext)_localctx).ex = expression();

							((PrimaryExpressionContext)_localctx).node = ((PrimaryExpressionContext)_localctx).ex.node;
						
				setState(472); match(T__4);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(474); ((PrimaryExpressionContext)_localctx).ifExp = ifExpression();
				((PrimaryExpressionContext)_localctx).node = ((PrimaryExpressionContext)_localctx).ifExp.node;
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(477); ((PrimaryExpressionContext)_localctx).letExp = letExpression();
				((PrimaryExpressionContext)_localctx).node = ((PrimaryExpressionContext)_localctx).letExp.operator;
				}
				break;
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
		public LiteralExp liter;
		public Token STRING;
		public Token INTEGER;
		public BooleanExpContext boolExp;
		public BooleanExpContext booleanExp() {
			return getRuleContext(BooleanExpContext.class,0);
		}
		public List<TerminalNode> INTEGER() { return getTokens(OclParser.INTEGER); }
		public EnumLiteralContext enumLiteral() {
			return getRuleContext(EnumLiteralContext.class,0);
		}
		public TerminalNode STRING() { return getToken(OclParser.STRING, 0); }
		public TerminalNode INTEGER(int i) {
			return getToken(OclParser.INTEGER, i);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_literal);
		int _la;
		try {
			setState(500);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(482); ((LiteralContext)_localctx).STRING = match(STRING);
				((LiteralContext)_localctx).liter = new LiteralExp("String",(((LiteralContext)_localctx).STRING!=null?((LiteralContext)_localctx).STRING.getText():null));
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				boolean minus=false;
				setState(487);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(485); match(T__1);
					minus=true;
					}
				}

				setState(489); ((LiteralContext)_localctx).INTEGER = match(INTEGER);
				if(minus) ((LiteralContext)_localctx).liter = new LiteralExp("Integer","-"+(((LiteralContext)_localctx).INTEGER!=null?((LiteralContext)_localctx).INTEGER.getText():null));else ((LiteralContext)_localctx).liter = new LiteralExp("Integer",(((LiteralContext)_localctx).INTEGER!=null?((LiteralContext)_localctx).INTEGER.getText():null));
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(491); ((LiteralContext)_localctx).INTEGER = match(INTEGER);
				String real=(((LiteralContext)_localctx).INTEGER!=null?((LiteralContext)_localctx).INTEGER.getText():null);
				setState(493); match(T__17);
				setState(494); ((LiteralContext)_localctx).INTEGER = match(INTEGER);
				real+="."+(((LiteralContext)_localctx).INTEGER!=null?((LiteralContext)_localctx).INTEGER.getText():null);((LiteralContext)_localctx).liter = new LiteralExp("Real",real);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(496); enumLiteral();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(497); ((LiteralContext)_localctx).boolExp = booleanExp();
				((LiteralContext)_localctx).liter = new LiteralExp("Boolean",((LiteralContext)_localctx).boolExp.value);
				}
				break;
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

	public static class BooleanExpContext extends ParserRuleContext {
		public String value;
		public BooleanExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterBooleanExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitBooleanExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitBooleanExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanExpContext booleanExp() throws RecognitionException {
		BooleanExpContext _localctx = new BooleanExpContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_booleanExp);
		try {
			setState(506);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(502); match(T__28);
				((BooleanExpContext)_localctx).value = "true";
				}
				break;
			case T__48:
				enterOuterAlt(_localctx, 2);
				{
				setState(504); match(T__48);
				((BooleanExpContext)_localctx).value = "false";
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

	public static class PropertyCallContext extends ParserRuleContext {
		public PropertyCallExp property;
		public PathNameContext paNa;
		public QualifiersContext qua;
		public PropertyCallParametersContext pcps;
		public QualifiersContext qualifiers() {
			return getRuleContext(QualifiersContext.class,0);
		}
		public PropertyCallParametersContext propertyCallParameters() {
			return getRuleContext(PropertyCallParametersContext.class,0);
		}
		public TimeExpressionContext timeExpression() {
			return getRuleContext(TimeExpressionContext.class,0);
		}
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public PropertyCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPropertyCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPropertyCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPropertyCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyCallContext propertyCall() throws RecognitionException {
		PropertyCallContext _localctx = new PropertyCallContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_propertyCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(508); ((PropertyCallContext)_localctx).paNa = pathName();
			((PropertyCallContext)_localctx).property = new PropertyCallExp(((PropertyCallContext)_localctx).paNa.name);
			setState(513);
			_la = _input.LA(1);
			if (_la==T__39) {
				{
				setState(510); timeExpression();
				(_localctx.property).setTimeExpression();
				}
			}

			setState(518);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(515); ((PropertyCallContext)_localctx).qua = qualifiers();
				(_localctx.property).setQualifier(((PropertyCallContext)_localctx).qua.list);   if(((PropertyCallContext)_localctx).qua.list2!=null){(_localctx.property).setQualifier2(((PropertyCallContext)_localctx).qua.list2);}  
				}
			}

			setState(523);
			_la = _input.LA(1);
			if (_la==T__53) {
				{
				setState(520); ((PropertyCallContext)_localctx).pcps = propertyCallParameters();
				(_localctx.property).setParameters(((PropertyCallContext)_localctx).pcps.list);
				}
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

	public static class TimeExpressionContext extends ParserRuleContext {
		public TimeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterTimeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitTimeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTimeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimeExpressionContext timeExpression() throws RecognitionException {
		TimeExpressionContext _localctx = new TimeExpressionContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_timeExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(525); match(T__39);
			setState(526); match(T__40);
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

	public static class PropertyCallParametersContext extends ParserRuleContext {
		public ArrayList<AbstractSyntaxTreeNode> list;
		public DeclaratorContext de;
		public ActualParameterListContext apl;
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public ActualParameterListContext actualParameterList() {
			return getRuleContext(ActualParameterListContext.class,0);
		}
		public PropertyCallParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyCallParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterPropertyCallParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitPropertyCallParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPropertyCallParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyCallParametersContext propertyCallParameters() throws RecognitionException {
		PropertyCallParametersContext _localctx = new PropertyCallParametersContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_propertyCallParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528); match(T__53);
			setState(530);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(529); ((PropertyCallParametersContext)_localctx).de = declarator();
				}
				break;
			}
			setState(535);
			_la = _input.LA(1);
			if (((((_la - 11)) & ~0x3f) == 0 && ((1L << (_la - 11)) & ((1L << (T__53 - 11)) | (1L << (T__50 - 11)) | (1L << (T__48 - 11)) | (1L << (T__47 - 11)) | (1L << (T__36 - 11)) | (1L << (T__34 - 11)) | (1L << (T__28 - 11)) | (1L << (T__25 - 11)) | (1L << (T__19 - 11)) | (1L << (T__15 - 11)) | (1L << (T__2 - 11)) | (1L << (T__1 - 11)) | (1L << (T__0 - 11)) | (1L << (NAME - 11)) | (1L << (INTEGER - 11)) | (1L << (STRING - 11)))) != 0)) {
				{
				setState(532); ((PropertyCallParametersContext)_localctx).apl = actualParameterList();

							((PropertyCallParametersContext)_localctx).list = ((PropertyCallParametersContext)_localctx).apl.list;
						
				}
			}

			setState(537); match(T__4);
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

	public static class IfExpressionContext extends ParserRuleContext {
		public IfExp node;
		public ExpressionContext condition;
		public ExpressionContext then;
		public IfExpsContext ifs;
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public IfExpsContext ifExps() {
			return getRuleContext(IfExpsContext.class,0);
		}
		public IfExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterIfExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitIfExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIfExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExpressionContext ifExpression() throws RecognitionException {
		IfExpressionContext _localctx = new IfExpressionContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_ifExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(539); match(T__25);
			setState(540); ((IfExpressionContext)_localctx).condition = expression();
			setState(541); match(T__33);
			setState(542); ((IfExpressionContext)_localctx).then = expression();
			setState(543); ((IfExpressionContext)_localctx).ifs = ifExps(((IfExpressionContext)_localctx).condition.node,((IfExpressionContext)_localctx).then.node);
			((IfExpressionContext)_localctx).node = ((IfExpressionContext)_localctx).ifs.node;
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

	public static class IfExpsContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode condition;
		public AbstractSyntaxTreeNode then;
		public IfExp node;
		public ExpressionContext elseExp;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IfExpsContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public IfExpsContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode condition, AbstractSyntaxTreeNode then) {
			super(parent, invokingState);
			this.condition = condition;
			this.then = then;
		}
		@Override public int getRuleIndex() { return RULE_ifExps; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterIfExps(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitIfExps(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIfExps(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExpsContext ifExps(AbstractSyntaxTreeNode condition,AbstractSyntaxTreeNode then) throws RecognitionException {
		IfExpsContext _localctx = new IfExpsContext(_ctx, getState(), condition, then);
		enterRule(_localctx, 86, RULE_ifExps);
		try {
			setState(553);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(546); match(T__7);
				setState(547); ((IfExpsContext)_localctx).elseExp = expression();
				setState(548); match(T__63);
				((IfExpsContext)_localctx).node = new IfExp("If",_localctx.condition,_localctx.then,((IfExpsContext)_localctx).elseExp.node);
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 2);
				{
				setState(551); match(T__63);
				((IfExpsContext)_localctx).node = new IfExp("If",_localctx.condition,_localctx.then);
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

	public static class EnumLiteralContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(OclParser.NAME, i);
		}
		public List<TerminalNode> NAME() { return getTokens(OclParser.NAME); }
		public EnumLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterEnumLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitEnumLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitEnumLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumLiteralContext enumLiteral() throws RecognitionException {
		EnumLiteralContext _localctx = new EnumLiteralContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_enumLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(555); match(NAME);
			setState(556); match(T__58);
			setState(557); match(NAME);
			setState(562);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__58) {
				{
				{
				setState(558); match(T__58);
				setState(559); match(NAME);
				}
				}
				setState(564);
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

	public static class SimpleTypeSpecifierContext extends ParserRuleContext {
		public String name;
		public PathNameContext pn;
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public SimpleTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterSimpleTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitSimpleTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitSimpleTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeSpecifierContext simpleTypeSpecifier() throws RecognitionException {
		SimpleTypeSpecifierContext _localctx = new SimpleTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_simpleTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565); ((SimpleTypeSpecifierContext)_localctx).pn = pathName();
			((SimpleTypeSpecifierContext)_localctx).name = ((SimpleTypeSpecifierContext)_localctx).pn.name;
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

	public static class LiteralCollectionContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public CollectionKindContext colKind;
		public CollectionItemContext colItem;
		public CollectionMethodContext coMe;
		public List<CollectionItemContext> collectionItem() {
			return getRuleContexts(CollectionItemContext.class);
		}
		public CollectionMethodContext collectionMethod() {
			return getRuleContext(CollectionMethodContext.class,0);
		}
		public CollectionItemContext collectionItem(int i) {
			return getRuleContext(CollectionItemContext.class,i);
		}
		public CollectionKindContext collectionKind() {
			return getRuleContext(CollectionKindContext.class,0);
		}
		public LiteralCollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalCollection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterLiteralCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitLiteralCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLiteralCollection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralCollectionContext literalCollection() throws RecognitionException {
		LiteralCollectionContext _localctx = new LiteralCollectionContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_literalCollection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568); ((LiteralCollectionContext)_localctx).colKind = collectionKind();
			CollectionExp node1=new CollectionExp(((LiteralCollectionContext)_localctx).colKind.type);
			setState(570); match(T__60);
			setState(579);
			_la = _input.LA(1);
			if (((((_la - 11)) & ~0x3f) == 0 && ((1L << (_la - 11)) & ((1L << (T__53 - 11)) | (1L << (T__50 - 11)) | (1L << (T__48 - 11)) | (1L << (T__47 - 11)) | (1L << (T__36 - 11)) | (1L << (T__34 - 11)) | (1L << (T__28 - 11)) | (1L << (T__25 - 11)) | (1L << (T__19 - 11)) | (1L << (T__15 - 11)) | (1L << (T__2 - 11)) | (1L << (T__1 - 11)) | (1L << (T__0 - 11)) | (1L << (NAME - 11)) | (1L << (INTEGER - 11)) | (1L << (STRING - 11)))) != 0)) {
				{
				setState(571); ((LiteralCollectionContext)_localctx).colItem = collectionItem(node1);
				setState(576);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__49) {
					{
					{
					setState(572); match(T__49);
					setState(573); collectionItem(node1);
					}
					}
					setState(578);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(581); match(T__26);
			setState(582); match(T__16);
			setState(583); ((LiteralCollectionContext)_localctx).coMe = collectionMethod(node1);
			if(((LiteralCollectionContext)_localctx).coMe.node2 instanceof IterateExp) {((LiteralCollectionContext)_localctx).node = ((LiteralCollectionContext)_localctx).coMe.node2;}else {((CollectionExp)_localctx.node).setMethodName(((PropertyCallExp)((LiteralCollectionContext)_localctx).coMe.node2).getVariable()); ((LiteralCollectionContext)_localctx).node = node1;}
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

	public static class CollectionItemContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public ExpressionContext ex;
		public ExpressionContext ex2;
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public CollectionItemContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public CollectionItemContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode node) {
			super(parent, invokingState);
			this.node = node;
		}
		@Override public int getRuleIndex() { return RULE_collectionItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterCollectionItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitCollectionItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionItemContext collectionItem(AbstractSyntaxTreeNode node) throws RecognitionException {
		CollectionItemContext _localctx = new CollectionItemContext(_ctx, getState(), node);
		enterRule(_localctx, 94, RULE_collectionItem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(586); ((CollectionItemContext)_localctx).ex = expression();
			((CollectionExp)_localctx.node).setStart(((CollectionItemContext)_localctx).ex.node);
			setState(592);
			_la = _input.LA(1);
			if (_la==T__59) {
				{
				setState(588); match(T__59);
				setState(589); ((CollectionItemContext)_localctx).ex2 = expression();
				((CollectionExp)_localctx.node).setEnd(((CollectionItemContext)_localctx).ex2.node);
				}
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

	public static class CollectionKindContext extends ParserRuleContext {
		public String type;
		public CollectionKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionKind; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterCollectionKind(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitCollectionKind(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionKindContext collectionKind() throws RecognitionException {
		CollectionKindContext _localctx = new CollectionKindContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_collectionKind);
		try {
			setState(606);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(594); match(T__0);
				((CollectionKindContext)_localctx).type = "Set";
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 2);
				{
				setState(596); match(T__19);
				((CollectionKindContext)_localctx).type = "Bag";
				}
				break;
			case T__47:
				enterOuterAlt(_localctx, 3);
				{
				setState(598); match(T__47);
				((CollectionKindContext)_localctx).type = "Sequence";
				}
				break;
			case T__34:
				enterOuterAlt(_localctx, 4);
				{
				setState(600); match(T__34);
				((CollectionKindContext)_localctx).type = "OrderSet";
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 5);
				{
				setState(602); match(T__15);
				((CollectionKindContext)_localctx).type = "Intger[]";
				}
				break;
			case T__50:
				enterOuterAlt(_localctx, 6);
				{
				setState(604); match(T__50);
				((CollectionKindContext)_localctx).type = "Intger[][]";
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

	public static class CollectionMethodContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode node;
		public AbstractSyntaxTreeNode node2;
		public PathNameContext pN;
		public LiteralContext lit;
		public ExpressionContext ex;
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralCollectionContext literalCollection() {
			return getRuleContext(LiteralCollectionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public CollectionMethodContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public CollectionMethodContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode node) {
			super(parent, invokingState);
			this.node = node;
		}
		@Override public int getRuleIndex() { return RULE_collectionMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterCollectionMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitCollectionMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionMethodContext collectionMethod(AbstractSyntaxTreeNode node) throws RecognitionException {
		CollectionMethodContext _localctx = new CollectionMethodContext(_ctx, getState(), node);
		enterRule(_localctx, 98, RULE_collectionMethod);
		try {
			setState(645);
			switch (_input.LA(1)) {
			case T__43:
			case T__35:
				enterOuterAlt(_localctx, 1);
				{
					((CollectionMethodContext)_localctx).node2 = new IterateExp(((CollectionExp)_localctx.node).getType());
				setState(613);
				switch (_input.LA(1)) {
				case T__43:
					{
					setState(609); match(T__43);
					((IterateExp)_localctx.node2).setIt("it");
					}
					break;
				case T__35:
					{
					setState(611); match(T__35);
					((IterateExp)_localctx.node2).setIt("it2");
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(615); ((CollectionMethodContext)_localctx).pN = pathName();
				((IterateExp)_localctx.node2).setAccVariable(((CollectionMethodContext)_localctx).pN.name);
				setState(617); match(T__14);

				 	((IterateExp)_localctx.node2).setStart(((CollectionExp)_localctx.node).getStart());
				 	((IterateExp)_localctx.node2).setEnd(((CollectionExp)_localctx.node).getEnd());
				 
				setState(627);
				switch (_input.LA(1)) {
				case T__62:
					{
					setState(619); match(T__62);
					setState(620); ((CollectionMethodContext)_localctx).lit = literal();
					((IterateExp)_localctx.node2).setAccType("Integer");
					 	 ((CollectionExp)_localctx.node).setAccType("Integer");
					 	 ((IterateExp)_localctx.node2).setAcc((((CollectionMethodContext)_localctx).lit.liter).getValue());
					 	 ((CollectionExp)_localctx.node).setAcc((((CollectionMethodContext)_localctx).lit.liter).getValue());
					 	
					}
					break;
				case T__55:
					{
					setState(623); match(T__55);
					((IterateExp)_localctx.node2).setAccType("Boolean");((CollectionExp)_localctx.node).setAccType("Boolean");
					setState(625); match(T__28);
					((IterateExp)_localctx.node2).setAcc("true");((CollectionExp)_localctx.node).setAcc("true");
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(629); match(T__12);
				setState(630); ((CollectionMethodContext)_localctx).ex = expression();
				((IterateExp)_localctx.node2).setBody(((CollectionMethodContext)_localctx).ex.node);((CollectionExp)_localctx.node).setBody(((CollectionMethodContext)_localctx).ex.node);
				setState(632); match(T__4);
				}
				break;
			case T__46:
				enterOuterAlt(_localctx, 2);
				{
				setState(634); match(T__46);
				setState(635); expression();
				setState(636); match(T__4);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 3);
				{
				setState(638); match(T__56);
				setState(639); expression();
				setState(640); match(T__49);
				setState(641); literalCollection();
				setState(642); match(T__4);
				}
				break;
			case T__63:
			case T__61:
			case T__59:
			case T__57:
			case T__54:
			case T__51:
			case T__49:
			case T__44:
			case T__42:
			case T__41:
			case T__40:
			case T__38:
			case T__37:
			case T__33:
			case T__32:
			case T__31:
			case T__29:
			case T__26:
			case T__24:
			case T__23:
			case T__22:
			case T__21:
			case T__20:
			case T__18:
			case T__17:
			case T__16:
			case T__12:
			case T__11:
			case T__10:
			case T__9:
			case T__8:
			case T__7:
			case T__6:
			case T__4:
			case T__3:
			case T__1:
				enterOuterAlt(_localctx, 4);
				{
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

	public static class QualifiersContext extends ParserRuleContext {
		public ArrayList<AbstractSyntaxTreeNode> list;
		public ArrayList<AbstractSyntaxTreeNode> list2;
		public ActualParameterListContext apl;
		public ActualParameterListContext apl2;
		public ActualParameterListContext actualParameterList(int i) {
			return getRuleContext(ActualParameterListContext.class,i);
		}
		public List<ActualParameterListContext> actualParameterList() {
			return getRuleContexts(ActualParameterListContext.class);
		}
		public QualifiersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterQualifiers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitQualifiers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitQualifiers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiersContext qualifiers() throws RecognitionException {
		QualifiersContext _localctx = new QualifiersContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_qualifiers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647); match(T__13);
			setState(648); ((QualifiersContext)_localctx).apl = actualParameterList();
			((QualifiersContext)_localctx).list = ((QualifiersContext)_localctx).apl.list;((QualifiersContext)_localctx).list2 = null;
			setState(650); match(T__41);
			setState(656);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(651); match(T__13);
				setState(652); ((QualifiersContext)_localctx).apl2 = actualParameterList();
				((QualifiersContext)_localctx).list2 = ((QualifiersContext)_localctx).apl2.list;
				setState(654); match(T__41);
				}
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

	public static class DeclaratorContext extends ParserRuleContext {
		public SimpleTypeSpecifierContext sts;
		public TerminalNode NAME(int i) {
			return getToken(OclParser.NAME, i);
		}
		public SimpleTypeSpecifierContext simpleTypeSpecifier() {
			return getRuleContext(SimpleTypeSpecifierContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public List<TerminalNode> NAME() { return getTokens(OclParser.NAME); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaratorContext declarator() throws RecognitionException {
		DeclaratorContext _localctx = new DeclaratorContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_declarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(658); match(NAME);
			setState(663);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__49) {
				{
				{
				setState(659); match(T__49);
				setState(660); match(NAME);
				}
				}
				setState(665);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(668);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(666); match(T__14);
				setState(667); ((DeclaratorContext)_localctx).sts = simpleTypeSpecifier();
				}
			}

			setState(677);
			_la = _input.LA(1);
			if (_la==T__27) {
				{
				setState(670); match(T__27);
				setState(671); match(NAME);
				setState(672); match(T__14);
				setState(673); typeSpecifier();
				setState(674); match(T__57);
				setState(675); expression();
				}
			}

			setState(679); match(T__12);
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

	public static class ActualParameterListContext extends ParserRuleContext {
		public ArrayList<AbstractSyntaxTreeNode> list;
		public ExpressionContext ex1;
		public ActualParameterListsContext apls;
		public ActualParameterListsContext actualParameterLists() {
			return getRuleContext(ActualParameterListsContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ActualParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actualParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterActualParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitActualParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitActualParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActualParameterListContext actualParameterList() throws RecognitionException {
		ActualParameterListContext _localctx = new ActualParameterListContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_actualParameterList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(681); ((ActualParameterListContext)_localctx).ex1 = expression();

					((ActualParameterListContext)_localctx).list = new ArrayList<AbstractSyntaxTreeNode>();
					(_localctx.list).add(((ActualParameterListContext)_localctx).ex1.node);
					
				
			setState(683); ((ActualParameterListContext)_localctx).apls = actualParameterLists(((ActualParameterListContext)_localctx).ex1.node);

					(_localctx.list).addAll(((ActualParameterListContext)_localctx).apls.list);
					
				
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

	public static class ActualParameterListsContext extends ParserRuleContext {
		public AbstractSyntaxTreeNode ex1;
		public ArrayList<AbstractSyntaxTreeNode> list;
		public ExpressionContext ex2;
		public ActualParameterListsContext apls;
		public ActualParameterListsContext actualParameterLists() {
			return getRuleContext(ActualParameterListsContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ActualParameterListsContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public ActualParameterListsContext(ParserRuleContext parent, int invokingState, AbstractSyntaxTreeNode ex1) {
			super(parent, invokingState);
			this.ex1 = ex1;
		}
		@Override public int getRuleIndex() { return RULE_actualParameterLists; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).enterActualParameterLists(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OclListener ) ((OclListener)listener).exitActualParameterLists(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitActualParameterLists(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActualParameterListsContext actualParameterLists(AbstractSyntaxTreeNode ex1) throws RecognitionException {
		ActualParameterListsContext _localctx = new ActualParameterListsContext(_ctx, getState(), ex1);
		enterRule(_localctx, 106, RULE_actualParameterLists);
		try {
			setState(693);
			switch (_input.LA(1)) {
			case T__49:
				enterOuterAlt(_localctx, 1);
				{
				setState(686); match(T__49);
				setState(687); ((ActualParameterListsContext)_localctx).ex2 = expression();

						((ActualParameterListsContext)_localctx).list = new ArrayList<AbstractSyntaxTreeNode>();
						(_localctx.list).add(((ActualParameterListsContext)_localctx).ex2.node);
					
				setState(689); ((ActualParameterListsContext)_localctx).apls = actualParameterLists(((ActualParameterListsContext)_localctx).ex2.node);

						(_localctx.list).addAll(((ActualParameterListsContext)_localctx).apls.list);
					
				}
				break;
			case T__41:
			case T__4:
				enterOuterAlt(_localctx, 2);
				{

							((ActualParameterListsContext)_localctx).list = new ArrayList<AbstractSyntaxTreeNode>();
						
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3F\u02ba\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\4\3\4\3\4\3\4\3\4\7\4~\n\4\f\4\16\4\u0081\13\4\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\5\6\u008c\n\6\3\7\3\7\3\7\3\7\5\7\u0092\n\7\3\7\3"+
		"\7\3\7\3\7\6\7\u0098\n\7\r\7\16\7\u0099\3\7\3\7\3\7\3\b\3\b\5\b\u00a1"+
		"\n\b\3\b\3\b\3\b\3\b\3\b\5\b\u00a8\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t"+
		"\u00b1\n\t\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00b9\n\n\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00c7\n\13\3\f\3\f\3\f\3\f"+
		"\3\f\5\f\u00ce\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\5\r\u00e1\n\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\5\17\u00ed\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20"+
		"\u00f7\n\20\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00ff\n\21\3\22\3\22\3"+
		"\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u012b"+
		"\n\24\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\5\27\u013a\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u0144\n"+
		"\30\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u014c\n\31\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\5\32\u015a\n\32\3\33\3\33\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u0166\n\34\3\35\3\35\3\35\3\35"+
		"\5\35\u016c\n\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\5\37"+
		"\u0178\n\37\3 \3 \3 \3 \5 \u017e\n \3!\3!\3!\3!\3!\3!\3!\5!\u0187\n!\3"+
		"\"\3\"\3\"\3\"\5\"\u018d\n\"\3#\3#\3#\3#\3#\3#\5#\u0195\n#\3$\3$\3$\3"+
		"$\5$\u019b\n$\3$\3$\3$\3$\3$\5$\u01a2\n$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3"+
		"%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3"+
		"%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u01cd\n%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3"+
		"&\3&\3&\3&\3&\3&\3&\3&\3&\3&\5&\u01e3\n&\3\'\3\'\3\'\3\'\3\'\5\'\u01ea"+
		"\n\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u01f7\n\'\3(\3(\3"+
		"(\3(\5(\u01fd\n(\3)\3)\3)\3)\3)\5)\u0204\n)\3)\3)\3)\5)\u0209\n)\3)\3"+
		")\3)\5)\u020e\n)\3*\3*\3*\3+\3+\5+\u0215\n+\3+\3+\3+\5+\u021a\n+\3+\3"+
		"+\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\5-\u022c\n-\3.\3.\3.\3.\3"+
		".\7.\u0233\n.\f.\16.\u0236\13.\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\7\60\u0241\n\60\f\60\16\60\u0244\13\60\5\60\u0246\n\60\3\60\3\60\3\60"+
		"\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\5\61\u0253\n\61\3\62\3\62\3\62"+
		"\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u0261\n\62\3\63\3\63"+
		"\3\63\3\63\3\63\5\63\u0268\n\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\5\63\u0276\n\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\5\63\u0288\n\63\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\3\64\3\64\3\64\5\64\u0293\n\64\3\65\3\65\3\65\7\65"+
		"\u0298\n\65\f\65\16\65\u029b\13\65\3\65\3\65\5\65\u029f\n\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\5\65\u02a8\n\65\3\65\3\65\3\66\3\66\3\66\3\66"+
		"\3\66\3\67\3\67\3\67\3\67\3\67\3\67\3\67\5\67\u02b8\n\67\3\67\2\28\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP"+
		"RTVXZ\\^`bdfhjl\2\2\u02de\2n\3\2\2\2\4u\3\2\2\2\6x\3\2\2\2\b\u0082\3\2"+
		"\2\2\n\u008b\3\2\2\2\f\u008d\3\2\2\2\16\u00a7\3\2\2\2\20\u00a9\3\2\2\2"+
		"\22\u00b8\3\2\2\2\24\u00ba\3\2\2\2\26\u00cd\3\2\2\2\30\u00e0\3\2\2\2\32"+
		"\u00e2\3\2\2\2\34\u00e5\3\2\2\2\36\u00f6\3\2\2\2 \u00fe\3\2\2\2\"\u0100"+
		"\3\2\2\2$\u0105\3\2\2\2&\u012a\3\2\2\2(\u012c\3\2\2\2*\u012f\3\2\2\2,"+
		"\u0139\3\2\2\2.\u0143\3\2\2\2\60\u0145\3\2\2\2\62\u0159\3\2\2\2\64\u015b"+
		"\3\2\2\2\66\u0165\3\2\2\28\u016b\3\2\2\2:\u016d\3\2\2\2<\u0177\3\2\2\2"+
		">\u017d\3\2\2\2@\u0186\3\2\2\2B\u018c\3\2\2\2D\u018e\3\2\2\2F\u01a1\3"+
		"\2\2\2H\u01cc\3\2\2\2J\u01e2\3\2\2\2L\u01f6\3\2\2\2N\u01fc\3\2\2\2P\u01fe"+
		"\3\2\2\2R\u020f\3\2\2\2T\u0212\3\2\2\2V\u021d\3\2\2\2X\u022b\3\2\2\2Z"+
		"\u022d\3\2\2\2\\\u0237\3\2\2\2^\u023a\3\2\2\2`\u024c\3\2\2\2b\u0260\3"+
		"\2\2\2d\u0287\3\2\2\2f\u0289\3\2\2\2h\u0294\3\2\2\2j\u02ab\3\2\2\2l\u02b7"+
		"\3\2\2\2no\7\16\2\2op\5\4\3\2pq\b\2\1\2qr\5\b\5\2rs\b\2\1\2st\7\60\2\2"+
		"t\3\3\2\2\2uv\5\6\4\2vw\b\3\1\2w\5\3\2\2\2xy\7C\2\2y\177\b\4\1\2z{\7\b"+
		"\2\2{|\7C\2\2|~\b\4\1\2}z\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080"+
		"\3\2\2\2\u0080\7\3\2\2\2\u0081\177\3\2\2\2\u0082\u0083\5\n\6\2\u0083\u0084"+
		"\b\5\1\2\u0084\t\3\2\2\2\u0085\u0086\5\f\7\2\u0086\u0087\b\6\1\2\u0087"+
		"\u0088\5\n\6\2\u0088\u0089\b\6\1\2\u0089\u008c\3\2\2\2\u008a\u008c\b\6"+
		"\1\2\u008b\u0085\3\2\2\2\u008b\u008a\3\2\2\2\u008c\13\3\2\2\2\u008d\u0097"+
		"\5\20\t\2\u008e\u0091\5\22\n\2\u008f\u0090\7C\2\2\u0090\u0092\b\7\1\2"+
		"\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094"+
		"\7\64\2\2\u0094\u0095\5$\23\2\u0095\u0096\b\7\1\2\u0096\u0098\3\2\2\2"+
		"\u0097\u008e\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a"+
		"\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c\5\16\b\2\u009c\u009d\b\7\1\2"+
		"\u009d\r\3\2\2\2\u009e\u00a0\5\22\n\2\u009f\u00a1\7C\2\2\u00a0\u009f\3"+
		"\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a3\7\64\2\2\u00a3"+
		"\u00a4\5$\23\2\u00a4\u00a5\b\b\1\2\u00a5\u00a8\3\2\2\2\u00a6\u00a8\3\2"+
		"\2\2\u00a7\u009e\3\2\2\2\u00a7\u00a6\3\2\2\2\u00a8\17\3\2\2\2\u00a9\u00b0"+
		"\7\17\2\2\u00aa\u00ab\5\24\13\2\u00ab\u00ac\b\t\1\2\u00ac\u00b1\3\2\2"+
		"\2\u00ad\u00ae\5\26\f\2\u00ae\u00af\b\t\1\2\u00af\u00b1\3\2\2\2\u00b0"+
		"\u00aa\3\2\2\2\u00b0\u00ad\3\2\2\2\u00b1\21\3\2\2\2\u00b2\u00b3\7\32\2"+
		"\2\u00b3\u00b9\b\n\1\2\u00b4\u00b5\7,\2\2\u00b5\u00b9\b\n\1\2\u00b6\u00b7"+
		"\7+\2\2\u00b7\u00b9\b\n\1\2\u00b8\u00b2\3\2\2\2\u00b8\u00b4\3\2\2\2\u00b8"+
		"\u00b6\3\2\2\2\u00b9\23\3\2\2\2\u00ba\u00bb\7C\2\2\u00bb\u00bc\7\b\2\2"+
		"\u00bc\u00bd\5\30\r\2\u00bd\u00be\b\13\1\2\u00be\u00bf\7\r\2\2\u00bf\u00c0"+
		"\5\34\17\2\u00c0\u00c1\b\13\1\2\u00c1\u00c6\7>\2\2\u00c2\u00c3\7\64\2"+
		"\2\u00c3\u00c4\5\32\16\2\u00c4\u00c5\b\13\1\2\u00c5\u00c7\3\2\2\2\u00c6"+
		"\u00c2\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\25\3\2\2\2\u00c8\u00c9\7C\2\2"+
		"\u00c9\u00ca\7\64\2\2\u00ca\u00ce\7C\2\2\u00cb\u00cc\7C\2\2\u00cc\u00ce"+
		"\b\f\1\2\u00cd\u00c8\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce\27\3\2\2\2\u00cf"+
		"\u00d0\7C\2\2\u00d0\u00e1\b\r\1\2\u00d1\u00e1\7\t\2\2\u00d2\u00e1\7\""+
		"\2\2\u00d3\u00e1\7A\2\2\u00d4\u00e1\7\30\2\2\u00d5\u00e1\7*\2\2\u00d6"+
		"\u00e1\7\26\2\2\u00d7\u00e1\7\67\2\2\u00d8\u00e1\7%\2\2\u00d9\u00e1\7"+
		".\2\2\u00da\u00e1\7\35\2\2\u00db\u00e1\7\f\2\2\u00dc\u00e1\7@\2\2\u00dd"+
		"\u00e1\79\2\2\u00de\u00e1\78\2\2\u00df\u00e1\7?\2\2\u00e0\u00cf\3\2\2"+
		"\2\u00e0\u00d1\3\2\2\2\u00e0\u00d2\3\2\2\2\u00e0\u00d3\3\2\2\2\u00e0\u00d4"+
		"\3\2\2\2\u00e0\u00d5\3\2\2\2\u00e0\u00d6\3\2\2\2\u00e0\u00d7\3\2\2\2\u00e0"+
		"\u00d8\3\2\2\2\u00e0\u00d9\3\2\2\2\u00e0\u00da\3\2\2\2\u00e0\u00db\3\2"+
		"\2\2\u00e0\u00dc\3\2\2\2\u00e0\u00dd\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0"+
		"\u00df\3\2\2\2\u00e1\31\3\2\2\2\u00e2\u00e3\5 \21\2\u00e3\u00e4\b\16\1"+
		"\2\u00e4\33\3\2\2\2\u00e5\u00ec\b\17\1\2\u00e6\u00e7\7C\2\2\u00e7\u00e8"+
		"\7\64\2\2\u00e8\u00e9\5 \21\2\u00e9\u00ea\5\36\20\2\u00ea\u00eb\b\17\1"+
		"\2\u00eb\u00ed\3\2\2\2\u00ec\u00e6\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\35"+
		"\3\2\2\2\u00ee\u00ef\7\21\2\2\u00ef\u00f0\7C\2\2\u00f0\u00f1\7\64\2\2"+
		"\u00f1\u00f2\5 \21\2\u00f2\u00f3\5\36\20\2\u00f3\u00f4\b\20\1\2\u00f4"+
		"\u00f7\3\2\2\2\u00f5\u00f7\b\20\1\2\u00f6\u00ee\3\2\2\2\u00f6\u00f5\3"+
		"\2\2\2\u00f7\37\3\2\2\2\u00f8\u00f9\5\\/\2\u00f9\u00fa\b\21\1\2\u00fa"+
		"\u00ff\3\2\2\2\u00fb\u00fc\5b\62\2\u00fc\u00fd\b\21\1\2\u00fd\u00ff\3"+
		"\2\2\2\u00fe\u00f8\3\2\2\2\u00fe\u00fb\3\2\2\2\u00ff!\3\2\2\2\u0100\u0101"+
		"\5b\62\2\u0101\u0102\7\r\2\2\u0102\u0103\5\\/\2\u0103\u0104\7>\2\2\u0104"+
		"#\3\2\2\2\u0105\u0106\5(\25\2\u0106\u0107\b\23\1\2\u0107%\3\2\2\2\u0108"+
		"\u0109\7\36\2\2\u0109\u010a\7C\2\2\u010a\u010b\b\24\1\2\u010b\u010c\7"+
		"\t\2\2\u010c\u010d\5(\25\2\u010d\u010e\7<\2\2\u010e\u010f\b\24\1\2\u010f"+
		"\u0110\5(\25\2\u0110\u0111\b\24\1\2\u0111\u012b\3\2\2\2\u0112\u0113\7"+
		"\36\2\2\u0113\u0114\7C\2\2\u0114\u0115\7\64\2\2\u0115\u0116\7$\2\2\u0116"+
		"\u0117\7\t\2\2\u0117\u0118\7=\2\2\u0118\u0119\b\24\1\2\u0119\u011a\7<"+
		"\2\2\u011a\u011b\5(\25\2\u011b\u011c\b\24\1\2\u011c\u012b\3\2\2\2\u011d"+
		"\u011e\7\36\2\2\u011e\u011f\7C\2\2\u011f\u0120\7\64\2\2\u0120\u0121\7"+
		"$\2\2\u0121\u0122\7\t\2\2\u0122\u0123\7\25\2\2\u0123\u0124\7D\2\2\u0124"+
		"\u0125\7(\2\2\u0125\u0126\b\24\1\2\u0126\u0127\7<\2\2\u0127\u0128\5(\25"+
		"\2\u0128\u0129\b\24\1\2\u0129\u012b\3\2\2\2\u012a\u0108\3\2\2\2\u012a"+
		"\u0112\3\2\2\2\u012a\u011d\3\2\2\2\u012b\'\3\2\2\2\u012c\u012d\5*\26\2"+
		"\u012d\u012e\b\25\1\2\u012e)\3\2\2\2\u012f\u0130\5\60\31\2\u0130\u0131"+
		"\5,\27\2\u0131\u0132\b\26\1\2\u0132+\3\2\2\2\u0133\u0134\5.\30\2\u0134"+
		"\u0135\5\60\31\2\u0135\u0136\5,\27\2\u0136\u0137\b\27\1\2\u0137\u013a"+
		"\3\2\2\2\u0138\u013a\b\27\1\2\u0139\u0133\3\2\2\2\u0139\u0138\3\2\2\2"+
		"\u013a-\3\2\2\2\u013b\u013c\7?\2\2\u013c\u0144\b\30\1\2\u013d\u013e\7"+
		"9\2\2\u013e\u0144\b\30\1\2\u013f\u0140\78\2\2\u0140\u0144\b\30\1\2\u0141"+
		"\u0142\7\f\2\2\u0142\u0144\b\30\1\2\u0143\u013b\3\2\2\2\u0143\u013d\3"+
		"\2\2\2\u0143\u013f\3\2\2\2\u0143\u0141\3\2\2\2\u0144/\3\2\2\2\u0145\u0146"+
		"\5\64\33\2\u0146\u014b\b\31\1\2\u0147\u0148\5\62\32\2\u0148\u0149\5\64"+
		"\33\2\u0149\u014a\b\31\1\2\u014a\u014c\3\2\2\2\u014b\u0147\3\2\2\2\u014b"+
		"\u014c\3\2\2\2\u014c\61\3\2\2\2\u014d\u014e\7\t\2\2\u014e\u015a\b\32\1"+
		"\2\u014f\u0150\7\67\2\2\u0150\u015a\b\32\1\2\u0151\u0152\7\30\2\2\u0152"+
		"\u015a\b\32\1\2\u0153\u0154\7\26\2\2\u0154\u015a\b\32\1\2\u0155\u0156"+
		"\7*\2\2\u0156\u015a\b\32\1\2\u0157\u0158\7\35\2\2\u0158\u015a\b\32\1\2"+
		"\u0159\u014d\3\2\2\2\u0159\u014f\3\2\2\2\u0159\u0151\3\2\2\2\u0159\u0153"+
		"\3\2\2\2\u0159\u0155\3\2\2\2\u0159\u0157\3\2\2\2\u015a\63\3\2\2\2\u015b"+
		"\u015c\5:\36\2\u015c\u015d\5\66\34\2\u015d\u015e\b\33\1\2\u015e\65\3\2"+
		"\2\2\u015f\u0160\58\35\2\u0160\u0161\5:\36\2\u0161\u0162\5\66\34\2\u0162"+
		"\u0163\b\34\1\2\u0163\u0166\3\2\2\2\u0164\u0166\b\34\1\2\u0165\u015f\3"+
		"\2\2\2\u0165\u0164\3\2\2\2\u0166\67\3\2\2\2\u0167\u0168\7\"\2\2\u0168"+
		"\u016c\b\35\1\2\u0169\u016a\7A\2\2\u016a\u016c\b\35\1\2\u016b\u0167\3"+
		"\2\2\2\u016b\u0169\3\2\2\2\u016c9\3\2\2\2\u016d\u016e\5@!\2\u016e\u016f"+
		"\5<\37\2\u016f\u0170\b\36\1\2\u0170;\3\2\2\2\u0171\u0172\5> \2\u0172\u0173"+
		"\5@!\2\u0173\u0174\5<\37\2\u0174\u0175\b\37\1\2\u0175\u0178\3\2\2\2\u0176"+
		"\u0178\b\37\1\2\u0177\u0171\3\2\2\2\u0177\u0176\3\2\2\2\u0178=\3\2\2\2"+
		"\u0179\u017a\7.\2\2\u017a\u017e\b \1\2\u017b\u017c\7%\2\2\u017c\u017e"+
		"\b \1\2\u017d\u0179\3\2\2\2\u017d\u017b\3\2\2\2\u017e?\3\2\2\2\u017f\u0180"+
		"\5B\"\2\u0180\u0181\5D#\2\u0181\u0182\b!\1\2\u0182\u0187\3\2\2\2\u0183"+
		"\u0184\5D#\2\u0184\u0185\b!\1\2\u0185\u0187\3\2\2\2\u0186\u017f\3\2\2"+
		"\2\u0186\u0183\3\2\2\2\u0187A\3\2\2\2\u0188\u0189\7A\2\2\u0189\u018d\b"+
		"\"\1\2\u018a\u018b\7@\2\2\u018b\u018d\b\"\1\2\u018c\u0188\3\2\2\2\u018c"+
		"\u018a\3\2\2\2\u018dC\3\2\2\2\u018e\u018f\5J&\2\u018f\u0194\5F$\2\u0190"+
		"\u0191\5H%\2\u0191\u0192\b#\1\2\u0192\u0195\3\2\2\2\u0193\u0195\b#\1\2"+
		"\u0194\u0190\3\2\2\2\u0194\u0193\3\2\2\2\u0195E\3\2\2\2\u0196\u0197\7"+
		"\61\2\2\u0197\u019b\b$\1\2\u0198\u0199\7\62\2\2\u0199\u019b\b$\1\2\u019a"+
		"\u0196\3\2\2\2\u019a\u0198\3\2\2\2\u019b\u019c\3\2\2\2\u019c\u019d\5P"+
		")\2\u019d\u019e\5F$\2\u019e\u019f\b$\1\2\u019f\u01a2\3\2\2\2\u01a0\u01a2"+
		"\b$\1\2\u01a1\u019a\3\2\2\2\u01a1\u01a0\3\2\2\2\u01a2G\3\2\2\2\u01a3\u01a4"+
		"\7\34\2\2\u01a4\u01a5\b%\1\2\u01a5\u01a6\5(\25\2\u01a6\u01a7\b%\1\2\u01a7"+
		"\u01a8\7>\2\2\u01a8\u01a9\5H%\2\u01a9\u01aa\b%\1\2\u01aa\u01cd\3\2\2\2"+
		"\u01ab\u01ac\7-\2\2\u01ac\u01ad\b%\1\2\u01ad\u01ae\5(\25\2\u01ae\u01af"+
		"\b%\1\2\u01af\u01b0\7>\2\2\u01b0\u01b1\5H%\2\u01b1\u01b2\b%\1\2\u01b2"+
		"\u01cd\3\2\2\2\u01b3\u01b4\7\5\2\2\u01b4\u01b5\b%\1\2\u01b5\u01b6\5(\25"+
		"\2\u01b6\u01b7\b%\1\2\u01b7\u01b8\7>\2\2\u01b8\u01b9\5H%\2\u01b9\u01ba"+
		"\b%\1\2\u01ba\u01cd\3\2\2\2\u01bb\u01bc\7:\2\2\u01bc\u01bd\b%\1\2\u01bd"+
		"\u01be\5(\25\2\u01be\u01bf\b%\1\2\u01bf\u01c0\7>\2\2\u01c0\u01c1\5H%\2"+
		"\u01c1\u01c2\b%\1\2\u01c2\u01cd\3\2\2\2\u01c3\u01c4\7#\2\2\u01c4\u01c5"+
		"\b%\1\2\u01c5\u01c6\5(\25\2\u01c6\u01c7\b%\1\2\u01c7\u01c8\7>\2\2\u01c8"+
		"\u01c9\5H%\2\u01c9\u01ca\b%\1\2\u01ca\u01cd\3\2\2\2\u01cb\u01cd\b%\1\2"+
		"\u01cc\u01a3\3\2\2\2\u01cc\u01ab\3\2\2\2\u01cc\u01b3\3\2\2\2\u01cc\u01bb"+
		"\3\2\2\2\u01cc\u01c3\3\2\2\2\u01cc\u01cb\3\2\2\2\u01cdI\3\2\2\2\u01ce"+
		"\u01cf\5^\60\2\u01cf\u01d0\b&\1\2\u01d0\u01e3\3\2\2\2\u01d1\u01d2\5L\'"+
		"\2\u01d2\u01d3\b&\1\2\u01d3\u01e3\3\2\2\2\u01d4\u01d5\5P)\2\u01d5\u01d6"+
		"\b&\1\2\u01d6\u01e3\3\2\2\2\u01d7\u01d8\7\r\2\2\u01d8\u01d9\5(\25\2\u01d9"+
		"\u01da\b&\1\2\u01da\u01db\7>\2\2\u01db\u01e3\3\2\2\2\u01dc\u01dd\5V,\2"+
		"\u01dd\u01de\b&\1\2\u01de\u01e3\3\2\2\2\u01df\u01e0\5&\24\2\u01e0\u01e1"+
		"\b&\1\2\u01e1\u01e3\3\2\2\2\u01e2\u01ce\3\2\2\2\u01e2\u01d1\3\2\2\2\u01e2"+
		"\u01d4\3\2\2\2\u01e2\u01d7\3\2\2\2\u01e2\u01dc\3\2\2\2\u01e2\u01df\3\2"+
		"\2\2\u01e3K\3\2\2\2\u01e4\u01e5\7E\2\2\u01e5\u01f7\b\'\1\2\u01e6\u01e9"+
		"\b\'\1\2\u01e7\u01e8\7A\2\2\u01e8\u01ea\b\'\1\2\u01e9\u01e7\3\2\2\2\u01e9"+
		"\u01ea\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01ec\7D\2\2\u01ec\u01f7\b\'"+
		"\1\2\u01ed\u01ee\7D\2\2\u01ee\u01ef\b\'\1\2\u01ef\u01f0\7\61\2\2\u01f0"+
		"\u01f1\7D\2\2\u01f1\u01f7\b\'\1\2\u01f2\u01f7\5Z.\2\u01f3\u01f4\5N(\2"+
		"\u01f4\u01f5\b\'\1\2\u01f5\u01f7\3\2\2\2\u01f6\u01e4\3\2\2\2\u01f6\u01e6"+
		"\3\2\2\2\u01f6\u01ed\3\2\2\2\u01f6\u01f2\3\2\2\2\u01f6\u01f3\3\2\2\2\u01f7"+
		"M\3\2\2\2\u01f8\u01f9\7&\2\2\u01f9\u01fd\b(\1\2\u01fa\u01fb\7\22\2\2\u01fb"+
		"\u01fd\b(\1\2\u01fc\u01f8\3\2\2\2\u01fc\u01fa\3\2\2\2\u01fdO\3\2\2\2\u01fe"+
		"\u01ff\5\6\4\2\u01ff\u0203\b)\1\2\u0200\u0201\5R*\2\u0201\u0202\b)\1\2"+
		"\u0202\u0204\3\2\2\2\u0203\u0200\3\2\2\2\u0203\u0204\3\2\2\2\u0204\u0208"+
		"\3\2\2\2\u0205\u0206\5f\64\2\u0206\u0207\b)\1\2\u0207\u0209\3\2\2\2\u0208"+
		"\u0205\3\2\2\2\u0208\u0209\3\2\2\2\u0209\u020d\3\2\2\2\u020a\u020b\5T"+
		"+\2\u020b\u020c\b)\1\2\u020c\u020e\3\2\2\2\u020d\u020a\3\2\2\2\u020d\u020e"+
		"\3\2\2\2\u020eQ\3\2\2\2\u020f\u0210\7\33\2\2\u0210\u0211\7\32\2\2\u0211"+
		"S\3\2\2\2\u0212\u0214\7\r\2\2\u0213\u0215\5h\65\2\u0214\u0213\3\2\2\2"+
		"\u0214\u0215\3\2\2\2\u0215\u0219\3\2\2\2\u0216\u0217\5j\66\2\u0217\u0218"+
		"\b+\1\2\u0218\u021a\3\2\2\2\u0219\u0216\3\2\2\2\u0219\u021a\3\2\2\2\u021a"+
		"\u021b\3\2\2\2\u021b\u021c\7>\2\2\u021cU\3\2\2\2\u021d\u021e\7)\2\2\u021e"+
		"\u021f\5(\25\2\u021f\u0220\7!\2\2\u0220\u0221\5(\25\2\u0221\u0222\5X-"+
		"\2\u0222\u0223\b,\1\2\u0223W\3\2\2\2\u0224\u0225\7;\2\2\u0225\u0226\5"+
		"(\25\2\u0226\u0227\7\3\2\2\u0227\u0228\b-\1\2\u0228\u022c\3\2\2\2\u0229"+
		"\u022a\7\3\2\2\u022a\u022c\b-\1\2\u022b\u0224\3\2\2\2\u022b\u0229\3\2"+
		"\2\2\u022cY\3\2\2\2\u022d\u022e\7C\2\2\u022e\u022f\7\b\2\2\u022f\u0234"+
		"\7C\2\2\u0230\u0231\7\b\2\2\u0231\u0233\7C\2\2\u0232\u0230\3\2\2\2\u0233"+
		"\u0236\3\2\2\2\u0234\u0232\3\2\2\2\u0234\u0235\3\2\2\2\u0235[\3\2\2\2"+
		"\u0236\u0234\3\2\2\2\u0237\u0238\5\6\4\2\u0238\u0239\b/\1\2\u0239]\3\2"+
		"\2\2\u023a\u023b\5b\62\2\u023b\u023c\b\60\1\2\u023c\u0245\7\6\2\2\u023d"+
		"\u0242\5`\61\2\u023e\u023f\7\21\2\2\u023f\u0241\5`\61\2\u0240\u023e\3"+
		"\2\2\2\u0241\u0244\3\2\2\2\u0242\u0240\3\2\2\2\u0242\u0243\3\2\2\2\u0243"+
		"\u0246\3\2\2\2\u0244\u0242\3\2\2\2\u0245\u023d\3\2\2\2\u0245\u0246\3\2"+
		"\2\2\u0246\u0247\3\2\2\2\u0247\u0248\7(\2\2\u0248\u0249\7\62\2\2\u0249"+
		"\u024a\5d\63\2\u024a\u024b\b\60\1\2\u024b_\3\2\2\2\u024c\u024d\5(\25\2"+
		"\u024d\u0252\b\61\1\2\u024e\u024f\7\7\2\2\u024f\u0250\5(\25\2\u0250\u0251"+
		"\b\61\1\2\u0251\u0253\3\2\2\2\u0252\u024e\3\2\2\2\u0252\u0253\3\2\2\2"+
		"\u0253a\3\2\2\2\u0254\u0255\7B\2\2\u0255\u0261\b\62\1\2\u0256\u0257\7"+
		"/\2\2\u0257\u0261\b\62\1\2\u0258\u0259\7\23\2\2\u0259\u0261\b\62\1\2\u025a"+
		"\u025b\7 \2\2\u025b\u0261\b\62\1\2\u025c\u025d\7\63\2\2\u025d\u0261\b"+
		"\62\1\2\u025e\u025f\7\20\2\2\u025f\u0261\b\62\1\2\u0260\u0254\3\2\2\2"+
		"\u0260\u0256\3\2\2\2\u0260\u0258\3\2\2\2\u0260\u025a\3\2\2\2\u0260\u025c"+
		"\3\2\2\2\u0260\u025e\3\2\2\2\u0261c\3\2\2\2\u0262\u0267\b\63\1\2\u0263"+
		"\u0264\7\27\2\2\u0264\u0268\b\63\1\2\u0265\u0266\7\37\2\2\u0266\u0268"+
		"\b\63\1\2\u0267\u0263\3\2\2\2\u0267\u0265\3\2\2\2\u0268\u0269\3\2\2\2"+
		"\u0269\u026a\5\6\4\2\u026a\u026b\b\63\1\2\u026b\u026c\7\64\2\2\u026c\u0275"+
		"\b\63\1\2\u026d\u026e\7\4\2\2\u026e\u026f\5L\'\2\u026f\u0270\b\63\1\2"+
		"\u0270\u0276\3\2\2\2\u0271\u0272\7\13\2\2\u0272\u0273\b\63\1\2\u0273\u0274"+
		"\7&\2\2\u0274\u0276\b\63\1\2\u0275\u026d\3\2\2\2\u0275\u0271\3\2\2\2\u0276"+
		"\u0277\3\2\2\2\u0277\u0278\7\66\2\2\u0278\u0279\5(\25\2\u0279\u027a\b"+
		"\63\1\2\u027a\u027b\7>\2\2\u027b\u0288\3\2\2\2\u027c\u027d\7\24\2\2\u027d"+
		"\u027e\5(\25\2\u027e\u027f\7>\2\2\u027f\u0288\3\2\2\2\u0280\u0281\7\n"+
		"\2\2\u0281\u0282\5(\25\2\u0282\u0283\7\21\2\2\u0283\u0284\5^\60\2\u0284"+
		"\u0285\7>\2\2\u0285\u0288\3\2\2\2\u0286\u0288\3\2\2\2\u0287\u0262\3\2"+
		"\2\2\u0287\u027c\3\2\2\2\u0287\u0280\3\2\2\2\u0287\u0286\3\2\2\2\u0288"+
		"e\3\2\2\2\u0289\u028a\7\65\2\2\u028a\u028b\5j\66\2\u028b\u028c\b\64\1"+
		"\2\u028c\u0292\7\31\2\2\u028d\u028e\7\65\2\2\u028e\u028f\5j\66\2\u028f"+
		"\u0290\b\64\1\2\u0290\u0291\7\31\2\2\u0291\u0293\3\2\2\2\u0292\u028d\3"+
		"\2\2\2\u0292\u0293\3\2\2\2\u0293g\3\2\2\2\u0294\u0299\7C\2\2\u0295\u0296"+
		"\7\21\2\2\u0296\u0298\7C\2\2\u0297\u0295\3\2\2\2\u0298\u029b\3\2\2\2\u0299"+
		"\u0297\3\2\2\2\u0299\u029a\3\2\2\2\u029a\u029e\3\2\2\2\u029b\u0299\3\2"+
		"\2\2\u029c\u029d\7\64\2\2\u029d\u029f\5\\/\2\u029e\u029c\3\2\2\2\u029e"+
		"\u029f\3\2\2\2\u029f\u02a7\3\2\2\2\u02a0\u02a1\7\'\2\2\u02a1\u02a2\7C"+
		"\2\2\u02a2\u02a3\7\64\2\2\u02a3\u02a4\5 \21\2\u02a4\u02a5\7\t\2\2\u02a5"+
		"\u02a6\5(\25\2\u02a6\u02a8\3\2\2\2\u02a7\u02a0\3\2\2\2\u02a7\u02a8\3\2"+
		"\2\2\u02a8\u02a9\3\2\2\2\u02a9\u02aa\7\66\2\2\u02aai\3\2\2\2\u02ab\u02ac"+
		"\5(\25\2\u02ac\u02ad\b\66\1\2\u02ad\u02ae\5l\67\2\u02ae\u02af\b\66\1\2"+
		"\u02afk\3\2\2\2\u02b0\u02b1\7\21\2\2\u02b1\u02b2\5(\25\2\u02b2\u02b3\b"+
		"\67\1\2\u02b3\u02b4\5l\67\2\u02b4\u02b5\b\67\1\2\u02b5\u02b8\3\2\2\2\u02b6"+
		"\u02b8\b\67\1\2\u02b7\u02b0\3\2\2\2\u02b7\u02b6\3\2\2\2\u02b8m\3\2\2\2"+
		"\66\177\u008b\u0091\u0099\u00a0\u00a7\u00b0\u00b8\u00c6\u00cd\u00e0\u00ec"+
		"\u00f6\u00fe\u012a\u0139\u0143\u014b\u0159\u0165\u016b\u0177\u017d\u0186"+
		"\u018c\u0194\u019a\u01a1\u01cc\u01e2\u01e9\u01f6\u01fc\u0203\u0208\u020d"+
		"\u0214\u0219\u022b\u0234\u0242\u0245\u0252\u0260\u0267\u0275\u0287\u0292"+
		"\u0299\u029e\u02a7\u02b7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}