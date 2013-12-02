package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD to validate against the 1.5 DTD
 * @author pobrien
 */
public class ValidateMetadata15DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateMetadata15DTD() {
     super();
    setDocTypeLocation("RETS-METADATA-20021015.dtd");
  }

}