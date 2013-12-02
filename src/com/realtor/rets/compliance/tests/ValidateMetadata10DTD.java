package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD to validate against the 1.0 DTD
 * @author pobrien
 */
public class ValidateMetadata10DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateMetadata10DTD() {
     super();
    setDocTypeLocation("RETS-METADATA-20001001.dtd");
  }

}