package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateUpdateResponse and validates against the 1.8 RETS GetPayloadList Response
 * @author pobrien
 */
public class ValidatePostObjectResponse18 extends ValidateUpdateResponse {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidatePostObjectResponse18() {
     super();
    setDocTypeLocation("rets-postobject-1_8.dtd");
  }

}