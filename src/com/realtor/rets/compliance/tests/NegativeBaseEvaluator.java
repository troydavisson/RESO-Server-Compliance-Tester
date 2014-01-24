/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.TestResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSTransaction;

import java.util.Map;

/**
 *  Base class for Negative Tests Result Evaluator classes will evaluate each of the  
 *  GetMetadata, GetObject, search Negative test transacions
 * 
 * @author pobrien
 */
public abstract class NegativeBaseEvaluator extends BaseEvaluator {
    
    private static Log log = LogFactory.getLog(NegativeBaseEvaluator.class);
    
    protected String testResultStatus = null;
    protected String testResultDesc = null;       //testResult Description
    protected String testResultNotes = null;

    
    protected String correctResponseStatus = null;

    /**
     * 
     */
    public NegativeBaseEvaluator() {
        super();
        specReference = "";
    }

    protected TestResult processResults(String transName, RETSTransaction transaction) {
        TestResult testResult   = null;
        String transRespStatus  = null;
        
        if (transaction != null) {
            transRespStatus = transaction.getResponseStatus();
            
            if (log.isDebugEnabled()) {
                log.debug("**Neg Test Resp Status: " + transRespStatus);
            }
            
            Map responseMap = transaction.getResponseMap();
            String responseBody = (String) responseMap.get("body");
            if (transRespStatus == null) {
                setFailureResponse(responseBody, transName, "null");
                return reportResult(transName, testResultDesc, 
                                    testResultStatus, testResultNotes);
            }
            
            if (responseBody != null && responseBody.length() > 0) {
                testResult = processNegativeTestResults(transName, responseBody,
                                                        transRespStatus);    
            }
        }
        testResult.setRetsReplyCode(transRespStatus);
        return testResult;
    }

/*    
    protected abstract TestResult processNegativeTestResults(String transName,
                                String responseBody,
                                String transRespStatus);
*/
    
    protected TestResult processNegativeTestResults(String transName,
            String responseBody, String transRespStatus) {

        TestResult negativeTestResult   = null;

        if (log.isDebugEnabled()) {
            log.debug("Test Transaction: " + transName + " : " 
                      + transRespStatus);
        }

        if (!transRespStatus.equals(correctResponseStatus)) {
            setFailureResponse(responseBody, transName, transRespStatus);
        } else {
            setSuccessResponse(responseBody, transName, transRespStatus);
        }

        negativeTestResult = reportResult(transName, testResultDesc, 
                                      testResultStatus, testResultNotes);
        
        return negativeTestResult;
    }
    

    protected void setFailureResponse(String responseBody, String transName, 
                                    String actualResponseStatus) {
        testResultStatus = "FAILURE";
        testResultNotes = "The Negative Test " + transName 
        + " expected a Transaction StatusResponse of " + correctResponseStatus
        + " but The server returned a StatusResponse of  " + actualResponseStatus
        + ".   SEE Response Body: "+ '\n' + responseBody;

        testResultDesc = "Compare returned status value of transaction " + transName +  
        ", " + actualResponseStatus + " to the correct Response status value for this transacion "
        + correctResponseStatus;
    } 

    protected void setSuccessResponse(String responseBody, String transName, 
            String actualResponseStatus) {
        testResultStatus = "SUCCESS";
        testResultNotes = "The Negative Test " + transName 
        + " Succeeded!  It expected a Transaction StatusResponse of " + correctResponseStatus
        + " and The server returned the same StatusResponse of  " + actualResponseStatus
        + ".   SEE Response Body: "+ '\n' + responseBody; 
        
        testResultDesc = "Compare returned status value of transaction " + transName +  
        ", " + actualResponseStatus + " to the correct Response status value for this transacion "
        + correctResponseStatus;
} 

    
    public String getCorrectResponseStatus() {
        return correctResponseStatus;
    }
    public void setCorrectResponseStatus(String correctResponseStatus) {
        this.correctResponseStatus = correctResponseStatus;
    }
}
