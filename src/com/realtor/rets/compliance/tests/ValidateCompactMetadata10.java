package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateCompact to validate metadata responses
 * @author pobrien
 */
public class ValidateCompactMetadata10 extends ValidateCompact10 {
  /**
   * Creates a new instance of ValidateCompact
   */
  public ValidateCompactMetadata10() {
     super();
    setDocTypeLocation("RETSCOMPACTMETA-20001001.dtd");
  }

}