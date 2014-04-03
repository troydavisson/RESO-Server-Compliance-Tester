package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateUpdateResponse and validates against the 1.8 RETS Update Response
 * @author pobrien
 */
public class ValidateUpdateResponse172 extends ValidateUpdateResponse {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateUpdateResponse172() {
     super();
    setDocTypeLocation("rets-update-1_7_2");
  }

}
