package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD to validate against the 1.8 DTD
 * @author pobrien
 */
public class ValidateMetadata18DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateMetadata18DTD() {
     super();
    setDocTypeLocation("RETS-METADATA-20131001.dtd");
  }

}