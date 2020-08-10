package ccu.pllab.tcgen.tc;

  
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.lang3.tuple.ImmutablePair;

import ccu.pllab.tcgen.libs.TestData;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;

public class TestSuiteGen {
	TCGenConfig config = null;
	Model mClsDiagInfo = null;
	List<TestData> mTestDataList = null;

	public TestSuiteGen(List<TestData> testDataLst, Model clsDiagInfo, TCGenConfig conf) {
		mTestDataList = testDataLst;
		mClsDiagInfo = clsDiagInfo;
		config = conf;
	}

	// we should have some kinds of assertion mapping
	private String genAssertion(String type, String value, String resultWord, boolean isConstructor) {
		String assertStr = "";

		// check if known types
		if (type.equals("Boolean")) {
			if (Integer.valueOf(value) == 1) {
				assertStr = String.format("assertTrue(%s)", resultWord);
			} else {
				assertStr = String.format("assertFalse(%s)", resultWord);
			}
		} else if (type.equals("Integer")) {
			assertStr = String.format("assertTrue(%s.equals(%s))", resultWord, value);
		} else if (type.equals("String")) {
			assertStr = String.format("assertTrue(%s.equals(%s))", value, resultWord);
		} else if (type.equals("UnlimitedNatural")) {
			throw new IllegalStateException();
		} else {
			// non-primitive data types
			assertStr = String.format("assertTrue(%s.equals(%s))", value, resultWord);
		}

		return assertStr;
	}

	public void generateTestSuite() {
		ArrayList<String> testCaseFileList = new ArrayList<String>();

		// C. testcase and builders are resident in configurable package
		// 1. generate builder to testcase folder
		String builderString = BuilderGen.genBuilder(mClsDiagInfo, config.getTestCasePackage(), config.getABSTestCaseFolder().getAbsolutePath());

		// generate all test cases
		for (TestData testData : mTestDataList) {
			String caseName = genTestCase(testData, config);
			testCaseFileList.add(caseName);
		}
		genTestSuite(config, testCaseFileList);
	}

	// will return the name of the generated test case class
	private String genTestCase(TestData data, TCGenConfig config) {
		String testCaseName = String.format("Test%s_%s_c%s", config.getTargetClass(), config.getTargetMethod(), data.getPathID());

		// 3. generate association instantiations
		String ascString = BuilderGen.genAscSetter(data);

		// 4. build arg_list
		String argString = BuilderGen.genArgList(mClsDiagInfo, data);

		// 5. interpret ret_type, if to be an object, translate expected_result
		// into object instances
		ImmutablePair<String, String> ret = data.getRet();
		String retString = BuilderGen.genExpectedResult(mClsDiagInfo, ret.getLeft(), ret.getRight());

		// 6. compile sys_decl
		String sysDecl = BuilderGen.genPreStateDecl(data);
		sysDecl += BuilderGen.genPostStateDecl(data);

		// 7. invoke builder for sys_init
		String sysInit = BuilderGen.genPreStateInit(mClsDiagInfo, data);
		sysInit += BuilderGen.genPostStateInit(mClsDiagInfo, data);

		// 8. sys_cleanup?
		String sysCleanup = BuilderGen.genPreStateClean(data);
		sysCleanup += BuilderGen.genPostStateClean(data);

		// 9. generate testcase
		InputStreamReader fr = null;

		if (!data.isInvalidated()) {
			fr = new InputStreamReader(TestSuiteGen.class.getResourceAsStream("testcase.stg"));
		} else {
			fr = new InputStreamReader(TestSuiteGen.class.getResourceAsStream("invalidated_testcase.stg"));
		}

		StringTemplateGroup group = new StringTemplateGroup(fr, DefaultTemplateLexer.class);
		StringTemplate template = group.getInstanceOf("testcase");

		// E. from model package
		template.setAttribute("testCasePackage", config.getTestCasePackage());

		template.setAttribute("classPackage", mClsDiagInfo.getPackageName());

		ArrayList<String> clsList = new ArrayList<String>();
		for (UML2Class clsInfo : mClsDiagInfo.getClasses()) {
			clsList.add(clsInfo.getName());
		}
		if (data.isInvalidated() && data.getPreconditionName() != null && !data.getPreconditionName().equals("")) {
			template.setAttribute("exception_name", data.getPreconditionName());
		}
		template.setAttribute("classList", clsList);
		// F. same to A
		template.setAttribute("class_name", config.getTargetClass());
		template.setAttribute("method_name", config.getTargetMethod());

		// G. from model
		template.setAttribute("ret_type", data.getRet().getLeft());
	
		// H. auto inc
		template.setAttribute("case_no", String.valueOf(data.getPathID()));

		if (data.getRet().getLeft().equals("void")) {
			template.setAttribute("return_void", true);
		}
		template.setAttribute("is_constructor", data.isConstructor());
		String assertStr = this.genAssertion(data.getRet().getLeft(), retString, "result", data.isConstructor());
		template.setAttribute("assert", assertStr);

		// I. from test data
		template.setAttribute("target_obj", BuilderGen.genObjName(config.getTargetClass(), data.getSelfID()));
		template.setAttribute("arg_list", argString);
		template.setAttribute("sys_decl", sysDecl);
		template.setAttribute("sys_init", sysInit);
		template.setAttribute("asc_init", ascString);
		template.setAttribute("sys_cleanup", sysCleanup);

		outputFile(config.getABSTestCaseFolder() + "/" + testCaseName + ".java", template.toString());

		return testCaseName;
	}

	private String genTestSuite(TCGenConfig config, ArrayList<String> testCaseFileList) {
		InputStreamReader fr = null;

		fr = new InputStreamReader(TestSuiteGen.class.getResourceAsStream("testsuite.stg"));

		StringTemplateGroup group = new StringTemplateGroup(fr, DefaultTemplateLexer.class);
		StringTemplate template = group.getInstanceOf("testsuite");

		template.setAttribute("package_name", config.getTestCasePackage());
		template.setAttribute("testsuite_name", config.getTestSuiteName());
		template.setAttribute("testcases", testCaseFileList);

		String content = template.toString();

		outputFile(config.getABSTestCaseFolder() + "/" + config.getTestSuiteName() + ".java", content);

		return content;
	}

	private void outputFile(String path, String content) {
		FileWriter output = null;

		try {
			final File file = new File(path);
			final File parent_directory = file.getParentFile();

			if (null != parent_directory) {
				parent_directory.mkdirs();
			}

			output = new FileWriter(path);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
