/* $Header$ 
 */
package com.realtor.rets.compliance.junit;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.realtor.rets.compliance.TestReport;
import com.realtor.rets.compliance.TestResult;

/**
 *  JUnit testCase class for testing Standard-XML-type DMQL Evaluators ONLY 
 * 
 * @author pobrien
 */
public class DMQLStandardEvaluatorsTest extends EvaluatorTest {
    
    private static Log log = LogFactory.getLog(DMQLStandardEvaluatorsTest.class);

    public DMQLStandardEvaluatorsTest(String arg0) {
        super(arg0);
    }
    
    public void testDMQLStandardEvaluators() {
        log.debug("Testing DMQL dynamic Evaluator");
        log.debug(appPath);
        String xmlFileName = appPath + "/config/TestScripts/DMQLDynamicTest.xml";
        TestReport dmqlTestReport = executer.execute(RetsConn, xmlFileName);
        assertNotNull("The DMQL Test Report from the DMQL dynamic test is null", dmqlTestReport);
        log.debug(dmqlTestReport.getDescription());
        
        ArrayList dmqlTestResults = (ArrayList) dmqlTestReport.getTestResults();        
        
        numericResultsLessThanTest(dmqlTestResults);
        
        numericResultsGTTest(dmqlTestResults);
    
        numericResultsRangeTest(dmqlTestResults);
        
        doCharacterANDTest(dmqlTestResults);
    
        doCharacterORTest(dmqlTestResults);
        
        doCharacterStartsWithTest(dmqlTestResults);
    
        doCharacterContainsTest(dmqlTestResults);
        
        doDateGreaterThanTest(dmqlTestResults);
        
        doDateLessThanTest(dmqlTestResults);
        
        doDateTodayTest(dmqlTestResults);
        
    }

    private void doDateTodayTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(9);
        assertNotNull("The DMQL Test RESULT *10* (Date Test Matching Today) - DMQL dynamic test is null", dmqlNumericTestRes);
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " + testRespDescription); 
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        
    }

    private void doDateLessThanTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(8);
        assertNotNull("The DMQL Test RESULT *9* (Date Test LEss THAN) - DMQL dynamic test is null", dmqlNumericTestRes);
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
        
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        String testResponseNotes = dmqlNumericTestRes.getNotes();   
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " + testRespDescription); 
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Date values"));
        
    }

    private void doDateGreaterThanTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(7);
        assertNotNull("The DMQL Test RESULT *8* (Date Test GREATER THAN) - DMQL dynamic test is null", dmqlNumericTestRes);
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
    
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " + testRespDescription); 
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Date values"));
    
    }

    /**
     * @param dmqlTestResults
     */
    private void doCharacterContainsTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(6);
        assertNotNull("The DMQL Test RESULT *7* (Char Test AND) - DMQL dynamic test is null", dmqlNumericTestRes);
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " +testRespDescription);  
        
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Character"));
    
    }

    /**
     * @param dmqlTestResults
     */
    private void doCharacterStartsWithTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(5);
        assertNotNull("The DMQL Test RESULT *6* (Char Test AND) - DMQL dynamic test is null", dmqlNumericTestRes);
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
        
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " +testRespDescription);        
        
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Character"));
    
        
    }

    /**
     * @param dmqlTestResults
     */
    private void doCharacterORTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(4);
        assertNotNull("The DMQL Test RESULT *5* (Char Test AND) - DMQL dynamic test is null", dmqlNumericTestRes);
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " +testRespDescription);
        
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Numeric values"));
        
        
    }

    /**
     * @param dmqlTestResults
     */
    private void doCharacterANDTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(3);
        assertNotNull("The DMQL Test RESULT *4* (Char Test AND) - DMQL dynamic test is null", dmqlNumericTestRes);
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " +testRespDescription);
        
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Character"));
    }

    /**
     * @param dmqlTestResults
     */
    private void numericResultsRangeTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(2);
        assertNotNull("The DMQL Test RESULT *3* (Numeric Test Range) - DMQL dynamic test is null", dmqlNumericTestRes);        
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + ": "
                  + dmqlNumericTestRes.getStatus());
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " +testRespDescription); 
        
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Numeric values"));
    }

    /**
     * @param dmqlTestResults
     */
    private void numericResultsGTTest(ArrayList dmqlTestResults) {
        TestResult dmqlNumericTestRes = (TestResult) dmqlTestResults.get(1);
        assertNotNull("The DMQL Test RESULT 2 (Numeric Test LT) - DMQL dynamic test is null", dmqlNumericTestRes);
        
        assertNotNull("The DMQL Test Result Status == null", dmqlNumericTestRes.getStatus());
        log.debug("DMQL test results for test " + dmqlNumericTestRes.getName() + " "
                  + dmqlNumericTestRes.getStatus());
        assertEquals("No success for DMQL test status!", "SUCCESS", dmqlNumericTestRes.getStatus());
    
        String testResponseNotes = dmqlNumericTestRes.getNotes();
        String testRespDescription = dmqlNumericTestRes.getDescription();
        log.debug("DMQL test result DESC: " +testRespDescription); 
        
         assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                    testResponseNotes.startsWith("All requested search fields had the correct Numeric values"));
    }

    /**
     * @param dmqlTestResults
     */
    private void numericResultsLessThanTest(ArrayList dmqlTestResults) {
        TestResult thisDmqlTestResult = (TestResult) dmqlTestResults.get(0);
        assertNotNull("The DMQL Test RESULT 1 - DMQL dynamic test is null", thisDmqlTestResult);
        
        assertNotNull("The DMQL Test Result Status == null", thisDmqlTestResult.getStatus());
        assertEquals("No success for DMQL test status!", "SUCCESS", thisDmqlTestResult.getStatus());
        log.debug("DMQL test results for test " + thisDmqlTestResult.getName() 
                  + "testResult " + thisDmqlTestResult.getStatus());
       // log.debug("DMQL test results Notes: " + thisDmqlTestResult.getNotes());
        String testResponseNotes = thisDmqlTestResult.getNotes();
        String testRespDescription = thisDmqlTestResult.getDescription();
        log.debug("DMQL test result DESC: " + testRespDescription); 
    
        assertTrue("DMQL result Notes DO Not contain the correct language for a success result.", 
                   testResponseNotes.startsWith("All requested search fields had the correct Numeric values"));
    }
    
}
