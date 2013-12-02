/* $Header$ 
 */
package com.realtor.rets.compliance.junit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.realtor.rets.retsapi.RETSSearchTransaction;

import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.TestEvaluator;
import com.realtor.rets.compliance.TestReport;
import com.realtor.rets.compliance.tests.dmql.*;
import com.realtor.rets.compliance.tests.dmql.DMQLSystemCharResultsStarts;
import com.realtor.rets.compliance.tests.dmql.DMQLSystemNumericResultsMax;

/**
 *  JUnit testCase class for testing System-type (Compact Format) DMQL Evaluators ONLY
 * 
 * @author pobrien
 */
public class DMQLSystemEvaluatorsTest extends TestCase {
    
    private static Log log = LogFactory.getLog(DMQLSystemEvaluatorsTest.class);
    
    private static final String TODAY_DATE_STRING = 
            new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    
    private static final String COMPACT_FORMAT_RESPONSE_DATE_TODAY = 
    	"<RETS ReplyCode=\"0\" ReplyText=\"Operation successful\" >\n" +
    	"<DELIMITER value=\"09\"/>\n" +
    	"<COLUMNS>\tListDate\tState\tListPrice\tCity\t</COLUMNS>\n" +
    	"<DATA>\t" + TODAY_DATE_STRING + "\tOH\t2000.00\tCLEVELAND\t</DATA>\n" +
    	"<DATA>\t" + TODAY_DATE_STRING + "\tIL\t54000.00\tCHICAGO\t</DATA>\n" +
    	"<DATA>\t" + TODAY_DATE_STRING + "\tOH\t32110.00\tCINCINNATI\t</DATA>\n" +
    	"<MAXROWS/>\n" +
    	"</RETS>";
    
    private static final String COMPACT_FORMAT_RESPONSE_CHAR_STARTS = 
    	"<RETS ReplyCode=\"0\" ReplyText=\"Operation successful\" >\n" +
    	"<DELIMITER value=\"09\"/>\n" +
    	"<COLUMNS>\tListDate\tState\tListPrice\tCity\t</COLUMNS>\n" +
    	"<DATA>\t2003-11-25\tOH\t2000.00\tCLEVELAND\t</DATA>\n" +
    	"<DATA>\t2004-08-15\tIL\t54000.00\tCHICAGO\t</DATA>\n" +
    	"<DATA>\t2002-04-08\tOH\t32110.00\tCINCINNATI\t</DATA>\n" +
    	"<MAXROWS/>\n" +
    	"</RETS>";
    
   private static final String COMPACT_FORMAT_RESPONSE_CHAR = 
    	"<RETS ReplyCode=\"0\" ReplyText=\"Operation successful\" >\n" +
    	"<DELIMITER value=\"09\"/>\n" +
    	"<COLUMNS>\tListDate\tState\tListPrice\tCity\t</COLUMNS>\n" +
    	"<DATA>\t2003-11-25\tOH\t2000.00\tCLEVELAND\t</DATA>\n" +
    	"<DATA>\t2004-08-15\tIL\t54000.00\tCHICAGO\t</DATA>\n" +
    	"<DATA>\t2003-01-08\tIL\t101000.00\tPEORIA\t</DATA>\n" +
    	"<DATA>\t2005-01-01\tOH\t72400.00\tWAPAKONETA\t</DATA>\n" +
    	"<DATA>\t2002-04-08\tOH\t32110.00\tCINCINNATI\t</DATA>\n" +
    	"<DATA>\t2001-06-12\tWA\t33160.00\tSEATTLE\t</DATA>\n" +
    	"<MAXROWS/>\n" +
    	"</RETS>";
    
    private static final String COMPACT_FORMAT_RESPONSE_NUM = 
        "<RETS ReplyCode=\"0\" ReplyText=\"Operation successful\" >\n" +
        "<DELIMITER value=\"09\"/>\n" +
        "<COLUMNS>\tListDate\tState\tListPrice\tCity\t</COLUMNS>\n" +
        "<DATA>\t2003-11-25\tOH\t2000.00\tCLEVELAND\t</DATA>\n" +
        "<DATA>\t2004-08-15\tIL\t54000.00\tCHIGAGO\t</DATA>\n" +
        "<DATA>\t2003-01-08\tIL\t101000.00\tPEORIA\t</DATA>\n" +
        "<MAXROWS/>\n" +
        "</RETS>";
    
    private HashMap transMap = new HashMap();
    
    protected void setUp() throws Exception {
        BasicConfigurator.configure();      
    }
    
    private TestReport runEvaluation(TestEvaluator evaluator, String response) {
        log.debug("JUnit Testing DMQLDynamicTestSystem Evaluator " + evaluator.getClass().getName());
        RETSSearchTransaction searchTrans = new RETSSearchTransaction();
        searchTrans.setResponse(response);
        
        transMap.put(evaluator.getClass().getName(), searchTrans);
        TestReport testReport = new TestReport();
        evaluator.evaluate(transMap, testReport);
        
        return testReport;
    }
    
    public void testDMQLSystemCharStarts() {
    	TestReport testReport = runEvaluation(new DMQLSystemCharResultsStarts(), COMPACT_FORMAT_RESPONSE_CHAR);
    	
        assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", testReport);
        
        TestResult testResult = (TestResult) ((ArrayList) testReport.getTestResults()).get(0);
        assertEquals("Unexpected test result success", testResult.getStatus(), "FAILURE");
        assertEquals("Unexpected test result notes", testResult.getNotes(), 
        		"One or more Search fields had an incorrect 'Character value' in the response: " + 
				"PEORIA" +
				"\n\n" + 
				COMPACT_FORMAT_RESPONSE_CHAR);
        assertEquals("Unexpected test result description", testResult.getDescription(), "Compare value of requested field City; Value must Start With the String C");
        
    	testReport = runEvaluation(new DMQLSystemCharResultsStarts(), COMPACT_FORMAT_RESPONSE_CHAR_STARTS);
    	
        assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", testReport);
        
        testResult = (TestResult) ((ArrayList) testReport.getTestResults()).get(0);
        assertEquals("Unexpected test result success", testResult.getStatus(), "SUCCESS");
        assertEquals("Unexpected test result notes", testResult.getNotes(), 
        		"All requested search fields had the correct Character values in the response " + 
				"\n\n" + 
				COMPACT_FORMAT_RESPONSE_CHAR_STARTS);
        assertEquals("Unexpected test result description", testResult.getDescription(), "Compare value of requested field City; Value must Start With the String C");
    }
    
    public void testDMQLSystemCharContains() {
    	TestReport testReport = runEvaluation(new DMQLSystemCharResultsContains(), COMPACT_FORMAT_RESPONSE_CHAR);
    	
        assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", testReport);
        
        TestResult testResult = (TestResult) ((ArrayList) testReport.getTestResults()).get(0);
        
        assertEquals("Unexpected test result success", testResult.getStatus(), "SUCCESS");
        assertEquals("Unexpected test result notes", testResult.getNotes(), 
        		"All requested search fields had the correct Character values in the response " + 
				"\n\n" + 
				COMPACT_FORMAT_RESPONSE_CHAR);
        assertEquals("Unexpected test result description", testResult.getDescription(), "Compare value of requested field City; Value contains the String: a");
    }
    
    public void testDMQLSystemCharAND() {
    	TestReport testReport = runEvaluation(new DMQLSystemCharResultsAND(), COMPACT_FORMAT_RESPONSE_CHAR);

    	assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", testReport);
        
        TestResult testResult = (TestResult) ((ArrayList) testReport.getTestResults()).get(0);
       
        assertEquals("Unexpected test result success", testResult.getStatus(), "SUCCESS");
        assertEquals("Unexpected test result notes", testResult.getNotes(), 
        		"All requested search fields had the correct Character & Numeric values in the response " + 
				"\n\n" + 
				COMPACT_FORMAT_RESPONSE_CHAR);
        assertEquals("Unexpected test result description", testResult.getDescription(), "Compare value of requested field City to the field Value a* AND make sure the numeric field ListPrice is <= to the value 300000");
    }
    
    public void testDMQLSystemCharOR() {
    	TestReport testReport = runEvaluation(new DMQLSystemCharResultsOR(), COMPACT_FORMAT_RESPONSE_CHAR);
    	
        assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", testReport);
        
        TestResult testResult = (TestResult) ((ArrayList) testReport.getTestResults()).get(0);
        
        assertEquals("Unexpected test result success", testResult.getStatus(), "SUCCESS");
        assertEquals("Unexpected test result notes", testResult.getNotes(), 
        		"All requested search fields had the correct Numeric values in the response " +
				"\n\n" + 
				COMPACT_FORMAT_RESPONSE_CHAR);
        assertEquals("Unexpected test result description", testResult.getDescription(), "Compare value of requested field City to the field Value a* *OR* ensure the numeric field ListPrice is <= to the value 300000");
    }
    
    
    public void testDMQLSystemDateMin() {
        TestReport testReport = runEvaluation(new DMQLSystemDateResultsMin(), COMPACT_FORMAT_RESPONSE_CHAR);
        assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", testReport);
        
        TestResult testResult = (TestResult) ((ArrayList) testReport.getTestResults()).get(0);
        assertEquals("Unexpected test result success", testResult.getStatus(), "SUCCESS");
        assertEquals("Unexpected test result notes", testResult.getNotes(), 
        		"All requested search fields had the correct Date values in the response: " +
				"\n\n" + 
				COMPACT_FORMAT_RESPONSE_CHAR);
        assertTrue("Unexpected test result description", 
        		testResult.getDescription().startsWith( 
        				"Compare value of requested field ListDate to the specified range; date field Value must be AFTER "));
    }
    
    public void testDMQLSystemDateToday() {
        TestReport testReport = runEvaluation(new DMQLSystemDateResultsToday(), COMPACT_FORMAT_RESPONSE_DATE_TODAY);
        assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", testReport);
        
        TestResult testResult = (TestResult) ((ArrayList) testReport.getTestResults()).get(0);
        assertEquals("Unexpected test result success", testResult.getStatus(), "SUCCESS");
        assertEquals("Unexpected test result notes", testResult.getNotes(), 
        		"All requested search fields had the correct Date values in the response: " +
				"\n\n" + 
				COMPACT_FORMAT_RESPONSE_DATE_TODAY);
        assertTrue("Unexpected test result description", 
        		testResult.getDescription().startsWith( 
        			"Compare value of requested field ListDate to the specified today's Date; date field Value must be THE SAME AS the value "));
    }
    
    public void testDMQLSystemNumMax(){
        
        TestReport testReport = runEvaluation(new DMQLSystemNumericResultsMax(), COMPACT_FORMAT_RESPONSE_NUM);
        assertNotNull("The DMQL Test Report from the DMQL System Numeric Max Evaluator is null", testReport);
        ArrayList dmqlTestResults = (ArrayList) testReport.getTestResults();
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(0);
        assertNotNull("The DMQL Test RESULT for DMQLSystemNumericResultsMax test is null", dmqlNumericTestRes);
        
        System.err.println(dmqlNumericTestRes.getName() + " - " + dmqlNumericTestRes.getStatus());        
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());        
        assertEquals("No success for DMQLSystemNumericResultsMax status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        //System.err.println("DMQL test result DESC: " + testRespDescription); 
        
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Numeric values"));

        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                   testRespDescription.startsWith("Compare value of requested field ListPrice to 300000"));

    }
    
    public void testDMQLSystemNumMin(){
        TestReport testReport = runEvaluation(new DMQLSystemNumericResultsMin(), COMPACT_FORMAT_RESPONSE_NUM);
        assertNotNull("The DMQL Test Report from the DMQL System Numeric MIN Evaluator is null", testReport);
        ArrayList dmqlTestResults = (ArrayList) testReport.getTestResults();
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(0);
        assertNotNull("The DMQL Test RESULT for DMQLSystemNumericResultsMin test is null", dmqlNumericTestRes);

        System.err.println(dmqlNumericTestRes.getName() + " - " + dmqlNumericTestRes.getStatus());        
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());        
        assertEquals("No success for DMQLSystemNumericResultsMax status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        //System.err.println("DMQL test result DESC: " + testRespDescription);
        
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                   testResponseNotes.startsWith("All requested search fields had the correct Numeric values"));

        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                   testRespDescription.startsWith("Compare value of requested field ListPrice to 200"));

    }
    
    public void testDMQLSystemNumRange(){
        TestReport testReport = runEvaluation(new DMQLSystemNumericResultsRange(), COMPACT_FORMAT_RESPONSE_NUM);
        assertNotNull("The DMQL Test Report from the DMQL System Numeric RANGE Evaluator is null", testReport);
        ArrayList dmqlTestResults = (ArrayList) testReport.getTestResults();
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(0);
        assertNotNull("The DMQL Test RESULT for DMQLSystemNumericResultsRange test is null", dmqlNumericTestRes);

        System.err.println(dmqlNumericTestRes.getName() + " - " + dmqlNumericTestRes.getStatus());        
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());
        
//      System.err.println("DMQL test result DESC: " + testRespDescription);
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        assertTrue("DMQL result DESC does Not contain the correct language for a success result.", 
                   testRespDescription.startsWith("Compare value of requested field ListPrice which must be in the range of"));
        
        assertEquals("No success for DMQLSystemNumericResultsMax status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                   testResponseNotes.startsWith("All requested search fields had a Numeric value within the correct range"));

    }
    
    public void testDMQLSystemDateMax(){
        TestReport testReport = runEvaluation(new DMQLSystemDateResultsMax(), COMPACT_FORMAT_RESPONSE_NUM);
        assertNotNull("The DMQL Test Report from the DMQL System Date Max Evaluator is null", testReport);
        ArrayList dmqlTestResults = (ArrayList) testReport.getTestResults();
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(0);
        assertNotNull("The DMQL Test RESULT for DMQLSystemDateResultsMax test is null", dmqlNumericTestRes);
        
        System.err.println(dmqlNumericTestRes.getName() + " - " + dmqlNumericTestRes.getStatus());        
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());
      
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        //System.err.println("DMQL test result DESC: " + testRespDescription);
        assertTrue("DMQL result DESC does Not contain the correct language for a success result.", 
                   testRespDescription.startsWith("Compare value of requested field ListDate's date value to"));

        assertEquals("No success for DMQLSystemNumericResultsMax status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                   testResponseNotes.startsWith("All requested search fields had the correct Date values in the response"));

        
    }

}