package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD and validates against the 1.8 RETS DTD
 * @author pobrien
 */
public class ValidateSearch172DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateSearch172DTD() {
     super();
    setDocTypeLocation("RETS-20080829.dtd");
  }

}
