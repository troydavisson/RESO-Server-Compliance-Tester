package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateCompact to validate metadata responses
 * @author pobrien
 */
public class ValidateCompactMetadata18 extends ValidateCompact {
  /**
   * Creates a new instance of ValidateCompact
   */
  public ValidateCompactMetadata18() {
     super();
    setDocTypeLocation("rets-compact-metadata-1_8.dtd");
  }

}