package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD to validate against the 1.8 DTD
 * @author pobrien
 */
public class ValidateMetadata172DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateMetadata172DTD() {
     super();
    setDocTypeLocation("RETS-METADATA-20080829.dtd");
  }

}
