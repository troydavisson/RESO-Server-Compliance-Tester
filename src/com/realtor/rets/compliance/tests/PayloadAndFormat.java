/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**  
 * @author pobrien
 */
public class PayloadAndFormat extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20216";
    
    /**
     * 
     */
    public PayloadAndFormat() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
