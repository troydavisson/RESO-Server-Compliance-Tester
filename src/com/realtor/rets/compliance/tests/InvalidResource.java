/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.realtor.rets.compliance.TestResult;

/**
 *  Negative Tests Result Evaluator class for GetMetadata Negative test that sends an
 *  Invalid Resource will evaluate each of the name in the <id\> tag in the script 
 *  The test (in processNegativeTestResults) the passed in transRespStatus value
 *  MUST match the value of msf_EXPECTED_STATUS_RESPONSE_CODE, for the test to succeed.
 * 
 * @author pobrien
 */
public class InvalidResource extends NegativeBaseEvaluator {

    private static Log log = LogFactory.getLog(InvalidResource.class);
    
    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20500";
    
    /**
     * 
     */
    public InvalidResource() {
        super();
        
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);

    }
    protected TestResult processNegativeTestResults(String transName,
                                            String responseBody,                                            
                                            String transRespStatus) {
        
        TestResult negativeTestResult   = null;

        if (log.isDebugEnabled()) {
            log.debug("Test Transaction: " + transName + " : " 
                      + transRespStatus);
        }
        
        if (!transRespStatus.equals(msf_EXPECTED_STATUS_RESPONSE_CODE)) {
            setFailureResponse(responseBody, transName, transRespStatus);
        } else {
            setSuccessResponse(responseBody, transName, transRespStatus);
        }
        
        negativeTestResult = reportResult(transName, testResultDesc, 
                                          testResultStatus, testResultNotes);        
        
        return negativeTestResult;
        
    }

}
