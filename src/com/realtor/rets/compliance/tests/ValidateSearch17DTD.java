package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateDTD and validates against the 1.5 RETS DTD
 * @author pobrien
 */
public class ValidateSearch17DTD extends ValidateDTD {
  /**
   * Creates a new instance of ValidateDTD
   */
  public ValidateSearch17DTD() {
     super();
    setDocTypeLocation("RETS-20041001.dtd");
  }

}