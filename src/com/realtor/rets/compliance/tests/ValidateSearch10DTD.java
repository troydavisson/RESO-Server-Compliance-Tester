package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD and validates against the 1.0 RETS DTD
 * @author pobrien
 */
public class ValidateSearch10DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateSearch10DTD() {
     super();
    setDocTypeLocation("RETS-20001001.dtd");
  }

}