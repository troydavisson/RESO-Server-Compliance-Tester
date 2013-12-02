/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**
 * 
 * @author pobrien
 */
public class UnknownQueryField extends NegativeBaseEvaluator {
    
    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20200";

    /**
     * 
     */
    public UnknownQueryField() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
