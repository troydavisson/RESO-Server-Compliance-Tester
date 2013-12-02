/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**  
 * 
 * @author pobrien
 */
public class InvalidQuerySyntax extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20206";
    
    /**
     * 
     */
    public InvalidQuerySyntax() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
