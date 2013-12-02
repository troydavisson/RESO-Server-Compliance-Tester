package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD and validates against the 1.5 RETS DTD
 * @author pobrien
 */
public class ValidateSearch15DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateSearch15DTD() {
     super();
    setDocTypeLocation("RETS-20021015.dtd");
  }

}