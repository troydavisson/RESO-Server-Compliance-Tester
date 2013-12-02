/* $Header$ 
 */
package com.realtor.rets.compliance.tests;


/**
 *  Negative Tests Result Evaluator class for GetMetadata Negative test that sends an
 *  Invalid Format, requesting the DTD version that is unavailable from the server.
 *  It will evaluate each of the name in the <Format\> tag in the script 
 *  In processNegativeTestResults() the param transRespStatus value
 *  MUST match the value of msf_EXPECTED_STATUS_RESPONSE_CODE, for the test to succeed.
 * 
 * @author pobrien
 */
public class RequestedDTDUnavailable extends NegativeBaseEvaluator {
    
    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20514";


    /**
     * 
     */
    public RequestedDTDUnavailable() {
        super();
        
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);

    }
}
