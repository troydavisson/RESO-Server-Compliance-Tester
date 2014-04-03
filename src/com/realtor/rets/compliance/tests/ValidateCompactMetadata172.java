package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateCompact to validate metadata responses
 * @author pobrien
 */
public class ValidateCompactMetadata172 extends ValidateCompact {
  /**
   * Creates a new instance of ValidateCompact
   */
  public ValidateCompactMetadata172() {
     super();
    setDocTypeLocation("rets-compact-metadata-1_7_2.dtd");
  }

}
