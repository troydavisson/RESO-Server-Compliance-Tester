/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**  
 * @author pobrien 
 */
public class RequestedPayloadUnavailable extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20215";
    
    /**
     * 
     */
    public RequestedPayloadUnavailable() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
