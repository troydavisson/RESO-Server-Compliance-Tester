/* $Header$ 
 */
package com.realtor.rets.compliance.junit;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.realtor.rets.compliance.TestReport;
import com.realtor.rets.compliance.TestResult;

/**
 *  JUnit testcase class for RETS server Negative tests - expecting failure responses 
 * 
 * @author pobrien
 */
public class NegativeTestEvaluatorsTest extends EvaluatorTest {
    
    private static Log log = LogFactory.getLog(NegativeTestEvaluatorsTest.class);

    public NegativeTestEvaluatorsTest(String arg0) {
        super(arg0);
    }

    public void testNegativeGetObjectTest() {
            log.debug("JUNIT Testing NegativeTesting GetObject.");
            String xmlFileName = appPath + "/config/TestScripts/NegativeTestingGetObject.xml";
            
            TestReport negativeTestReport = executer.execute(RetsConn, xmlFileName);
            assertNotNull("The Test Report from the GetObject Negative test is null", negativeTestReport);
            log.debug(negativeTestReport.getDescription());
            
            ArrayList negativeTestResults = (ArrayList) negativeTestReport.getTestResults();
    
            //  negativeTestResults.get(0) =  Invalid Resource Get Object test        
            TestResult aNegativeTestResult = (TestResult) negativeTestResults.get(0);
            assertNotNull("The NegativeSearch testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The NegativeSearch Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("NegativeSearch results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
    
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());
            
            String testResponseNotes = aNegativeTestResult.getNotes();        
    //        log.debug("NegativeSearch test Notes: " + testResponseNotes);        
            assertTrue("This NegativeSearch test result Notes DO Not contain the correct language for a success result.", 
                       testResponseNotes.startsWith("The Negative Test Invalid Resource Get Object expected a Transaction StatusResponse of 20400 but The server returned a StatusResponse of  null"));        
    //////////////////////////////////////////////////////////////////////
            
            //  negativeTestResults.get(1) =  Invalid Type Get Object test 
            aNegativeTestResult = (TestResult) negativeTestResults.get(1);
            assertNotNull("The NegativeSearch testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The NegativeSearch Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("NegativeSearch results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
    
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());        
    
            testResponseNotes = aNegativeTestResult.getNotes();        
          //log.debug("NegativeSearch test Notes: " + testResponseNotes);        
          assertTrue("This NegativeSearch test result Notes DO Not contain the correct language for a success result.", 
                     testResponseNotes.startsWith("The Negative Test Invalid Type Get Object expected a Transaction StatusResponse of 20401 but The server returned a StatusResponse of  null"));        
    
        }

    public void testNegativeMetaDataTest() {
            log.debug("JUNIT Testing NegativeTestingGetMetadata.");
            String xmlFileName = appPath + "/config/TestScripts/NegativeTestingGetMetadata.xml";
            
            TestReport negativeTestReport = executer.execute(RetsConn, xmlFileName);
            assertNotNull("The Test Report from the NegativeMetaDataTest is null", negativeTestReport);
            log.debug(negativeTestReport.getDescription());
            
            ArrayList negativeTestResults = (ArrayList) negativeTestReport.getTestResults();
    
            TestResult aNegativeTestResult = (TestResult) negativeTestResults.get(1);
            assertNotNull("The GetMetaData/Negative testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The GetMetaData/Negative Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("GetMetaData/Negative results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
    //        log.debug("GetMetaData/Negative test Notes: " + aNegativeTestResult.getNotes());
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());
    
            String testResponseNotes = aNegativeTestResult.getNotes();
            
           assertTrue("GetMetaData/Negative test result Notes DO Not contain the correct language for a success result.", 
                        testResponseNotes.startsWith("The Negative Test Invalid Resource expected a Transaction StatusResponse of 20500 but The server returned a StatusResponse of  20503."));
    
    ///////////////////////////////////////////////////////////        
            aNegativeTestResult = (TestResult) negativeTestResults.get(3);
            assertNotNull("The GetMetaData/Negative testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The GetMetaData/Negative Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("GetMetaData/Negative results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
           //log.debug("GetMetaData/Negative test Notes: " + aNegativeTestResult.getNotes());
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());
    
            testResponseNotes = aNegativeTestResult.getNotes();        
            assertTrue("GetMetaData/Negative test result Notes DO Not contain the correct language for a success result.", 
                        testResponseNotes.startsWith("The Negative Test Invalid Type expected a Transaction StatusResponse of 20501"));        
    ///////////////////////////////////////////////////////////
            
            
            aNegativeTestResult = (TestResult) negativeTestResults.get(4);
            assertNotNull("The GetMetaData/Negative testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The GetMetaData/Negative Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("GetMetaData/Negative results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
           //log.debug("GetMetaData/Negative test Notes: " + aNegativeTestResult.getNotes());
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());
    
            testResponseNotes = aNegativeTestResult.getNotes();        
            assertTrue("GetMetaData/Negative test result Notes DO Not contain the correct language for a success result.", 
                        testResponseNotes.startsWith("The Negative Test Requested DTD Unavailable expected a Transaction StatusResponse of 20514"));        
         
        }

    public void testNegativeSearchTests() {
            log.debug("JUNIT Testing NegativeTestingSearch.");
            String xmlFileName = appPath + "/config/TestScripts/NegativeTestingSearch.xml";
            
            TestReport negativeTestReport = executer.execute(RetsConn, xmlFileName);
            assertNotNull("The Test Report from the NegativeSearchTests is null", negativeTestReport);
            log.debug(negativeTestReport.getDescription());
            
            ArrayList negativeTestResults = (ArrayList) negativeTestReport.getTestResults();        
       
            //negativeTestResults.get(0) = UnknownQueryField test
            TestResult aNegativeTestResult = (TestResult) negativeTestResults.get(0);
            assertNotNull("The NegativeSearch testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The NegativeSearch Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("NegativeSearch results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
    
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());
            
            String testResponseNotes = aNegativeTestResult.getNotes();        
    
            assertTrue("GetMetaData/Negative test result Notes DO Not contain the correct language for a success result.", 
                       testResponseNotes.startsWith("The Negative Test Unknown Query Field expected a Transaction StatusResponse of 20200"));        
    
    /////////////////////////////////////////////////////////
            //negativeTestResults.get(1) = UnknownSelect test
            aNegativeTestResult = (TestResult) negativeTestResults.get(1);
            assertNotNull("The NegativeSearch testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The NegativeSearch Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("NegativeSearch results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
    
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());
            
            testResponseNotes = aNegativeTestResult.getNotes();        
            
            assertTrue("GetMetaData/Negative test result Notes DO Not contain the correct language for a success result.", 
                       testResponseNotes.startsWith("The Negative Test Unknown Field In Select expected a Transaction StatusResponse of 20202"));        
    
    /////////////////////////////////////////////////////////       
    //      negativeTestResults.get(2) = InvalidQuerySyntax test
            aNegativeTestResult = (TestResult) negativeTestResults.get(2);
            assertNotNull("The NegativeSearch testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The NegativeSearch Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("NegativeSearch results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
    
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());        
            testResponseNotes = aNegativeTestResult.getNotes();        
    
            assertTrue("GetMetaData/Negative test result Notes DO Not contain the correct language for a success result.", 
                       testResponseNotes.startsWith("The Negative Test Invalid Query Syntax expected a Transaction StatusResponse of 20206 but"));        
    
    /////////////////////////////////////////////////////////       
    //      negativeTestResults.get(3) = RequestedDTDUnavailable test
            
            aNegativeTestResult = (TestResult) negativeTestResults.get(3);
            assertNotNull("The NegativeSearch testResult Obj is null", aNegativeTestResult);
            
            assertNotNull("The NegativeSearch Test Result Status == null", aNegativeTestResult.getStatus());
            log.debug("NegativeSearch results for test " + aNegativeTestResult.getName() + ": "
                      + aNegativeTestResult.getStatus());
    
            assertEquals("No success for GetMetaData/Negative status!", "FAILURE", aNegativeTestResult.getStatus());
            
            testResponseNotes = aNegativeTestResult.getNotes();        
            //log.debug("NegativeSearch test Notes: " + testResponseNotes);        
            assertTrue("This NegativeSearch test result Notes DO Not contain the correct language for a success result.", 
                       testResponseNotes.startsWith("The Negative Test Requested DTD Unavailable expected a Transaction StatusResponse of 20514 but The server"));        
    
        }

}
