package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateUpdateResponse and validates against the 1.7 RETS Update Response
 * @author pobrien
 */
public class ValidateLogoutResponse172 extends ValidateLogoutResponse {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateLogoutResponse172() {
     super();
    setDocTypeLocation("RETS-LOGOUT-1_7.dtd");


  }

}
