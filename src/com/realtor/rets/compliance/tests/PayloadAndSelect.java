/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**  
 * @author pobrien 
 */
public class PayloadAndSelect extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20217";
    
    /**
     * 
     */
    public PayloadAndSelect() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
