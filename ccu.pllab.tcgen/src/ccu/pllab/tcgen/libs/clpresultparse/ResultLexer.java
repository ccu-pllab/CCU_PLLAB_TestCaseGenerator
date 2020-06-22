// Generated from Result.g4 by ANTLR 4.4
package ccu.pllab.tcgen.libs.clpresultparse;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ResultLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__12=1, T__11=2, T__10=3, T__9=4, T__8=5, T__7=6, T__6=7, T__5=8, T__4=9, 
		T__3=10, T__2=11, T__1=12, T__0=13, STRUCTNAME=14, ID=15, INTEGER=16, 
		FLOAT=17, NUMBER=18, LETTER=19, WS=20;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'"
	};
	public static final String[] ruleNames = {
		"T__12", "T__11", "T__10", "T__9", "T__8", "T__7", "T__6", "T__5", "T__4", 
		"T__3", "T__2", "T__1", "T__0", "STRUCTNAME", "ID", "INTEGER", "FLOAT", 
		"NUMBER", "LETTER", "WS"
	};


	public ResultLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Result.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\26\u0099\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\f\3\f"+
		"\3\r\3\r\3\16\3\16\3\16\3\16\3\17\6\17^\n\17\r\17\16\17_\3\17\3\17\7\17"+
		"d\n\17\f\17\16\17g\13\17\3\20\3\20\3\20\7\20l\n\20\f\20\16\20o\13\20\3"+
		"\21\5\21r\n\21\3\21\6\21u\n\21\r\21\16\21v\3\22\6\22z\n\22\r\22\16\22"+
		"{\3\22\3\22\6\22\u0080\n\22\r\22\16\22\u0081\3\22\3\22\5\22\u0086\n\22"+
		"\3\22\6\22\u0089\n\22\r\22\16\22\u008a\5\22\u008d\n\22\3\23\3\23\3\24"+
		"\3\24\3\25\6\25\u0094\n\25\r\25\16\25\u0095\3\25\3\25\2\2\26\3\3\5\4\7"+
		"\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26\3\2\6\3\2\62;\6\2\"\"))--~~\4\2C\\c|\5\2\13\f\17\17"+
		"\"\"\u00a5\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"+
		"\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\3+\3\2\2\2\5/\3\2\2\2"+
		"\7\67\3\2\2\2\t<\3\2\2\2\13D\3\2\2\2\rI\3\2\2\2\17K\3\2\2\2\21M\3\2\2"+
		"\2\23O\3\2\2\2\25R\3\2\2\2\27T\3\2\2\2\31V\3\2\2\2\33X\3\2\2\2\35]\3\2"+
		"\2\2\37h\3\2\2\2!q\3\2\2\2#y\3\2\2\2%\u008e\3\2\2\2\'\u0090\3\2\2\2)\u0093"+
		"\3\2\2\2+,\7C\2\2,-\7T\2\2-.\7I\2\2.\4\3\2\2\2/\60\7w\2\2\60\61\7o\2\2"+
		"\61\62\7n\2\2\62\63\7a\2\2\63\64\7c\2\2\64\65\7u\2\2\65\66\7e\2\2\66\6"+
		"\3\2\2\2\678\7R\2\289\7Q\2\29:\7U\2\2:;\7V\2\2;\b\3\2\2\2<=\7w\2\2=>\7"+
		"o\2\2>?\7n\2\2?@\7a\2\2@A\7q\2\2AB\7d\2\2BC\7l\2\2C\n\3\2\2\2DE\7x\2\2"+
		"EF\7q\2\2FG\7k\2\2GH\7f\2\2H\f\3\2\2\2IJ\7]\2\2J\16\3\2\2\2KL\7_\2\2L"+
		"\20\3\2\2\2MN\7?\2\2N\22\3\2\2\2OP\7]\2\2PQ\7_\2\2Q\24\3\2\2\2RS\7*\2"+
		"\2S\26\3\2\2\2TU\7+\2\2U\30\3\2\2\2VW\7.\2\2W\32\3\2\2\2XY\7R\2\2YZ\7"+
		"T\2\2Z[\7G\2\2[\34\3\2\2\2\\^\5\'\24\2]\\\3\2\2\2^_\3\2\2\2_]\3\2\2\2"+
		"_`\3\2\2\2`e\3\2\2\2ad\5%\23\2bd\5\'\24\2ca\3\2\2\2cb\3\2\2\2dg\3\2\2"+
		"\2ec\3\2\2\2ef\3\2\2\2f\36\3\2\2\2ge\3\2\2\2hm\5\'\24\2il\5%\23\2jl\5"+
		"\'\24\2ki\3\2\2\2kj\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2n \3\2\2\2om"+
		"\3\2\2\2pr\7/\2\2qp\3\2\2\2qr\3\2\2\2rt\3\2\2\2su\t\2\2\2ts\3\2\2\2uv"+
		"\3\2\2\2vt\3\2\2\2vw\3\2\2\2w\"\3\2\2\2xz\t\2\2\2yx\3\2\2\2z{\3\2\2\2"+
		"{y\3\2\2\2{|\3\2\2\2|}\3\2\2\2}\177\7\60\2\2~\u0080\t\2\2\2\177~\3\2\2"+
		"\2\u0080\u0081\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u008c"+
		"\3\2\2\2\u0083\u0085\7G\2\2\u0084\u0086\t\3\2\2\u0085\u0084\3\2\2\2\u0085"+
		"\u0086\3\2\2\2\u0086\u0088\3\2\2\2\u0087\u0089\t\2\2\2\u0088\u0087\3\2"+
		"\2\2\u0089\u008a\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b"+
		"\u008d\3\2\2\2\u008c\u0083\3\2\2\2\u008c\u008d\3\2\2\2\u008d$\3\2\2\2"+
		"\u008e\u008f\t\2\2\2\u008f&\3\2\2\2\u0090\u0091\t\4\2\2\u0091(\3\2\2\2"+
		"\u0092\u0094\t\5\2\2\u0093\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0093"+
		"\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\b\25\2\2"+
		"\u0098*\3\2\2\2\20\2_cekmqv{\u0081\u0085\u008a\u008c\u0095\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}