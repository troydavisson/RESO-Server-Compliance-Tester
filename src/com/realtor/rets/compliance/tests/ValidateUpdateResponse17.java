package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateUpdateResponse and validates against the 1.7 RETS Update Response
 * @author pobrien
 */
public class ValidateUpdateResponse17 extends ValidateUpdateResponse {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateUpdateResponse17() {
     super();
    setDocTypeLocation("RETS-UPDATE-1_7.dtd");
  }

}