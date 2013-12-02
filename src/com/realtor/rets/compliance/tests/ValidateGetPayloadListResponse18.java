package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateUpdateResponse and validates against the 1.8 RETS GetPayloadList Response
 * @author pobrien
 */
public class ValidateGetPayloadListResponse18 extends ValidateDTD{
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateGetPayloadListResponse18() {
     super();
    setDocTypeLocation("rets-getpayloadlist-1_8.dtd");
  }

}