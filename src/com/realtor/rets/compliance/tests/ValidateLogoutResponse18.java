package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateUpdateResponse and validates against the 1.8 RETS Logout Response
 * @author pobrien
 */
public class ValidateLogoutResponse18 extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateLogoutResponse18() {
     super();
    setDocTypeLocation("RETS-LOGOUT-1_7.dtd");


  }

}