package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD to validate against the 1.7 DTD
 * @author pobrien
 */
public class ValidateMetadata17DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateMetadata17DTD() {
     super();
    setDocTypeLocation("RETS-METADATA-20041001.dtd");
  }

}