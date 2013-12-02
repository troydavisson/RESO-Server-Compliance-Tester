package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateCompact to validate search responses
 * @author pobrien
 */
public class ValidateCompactSearch extends ValidateCompact {
  /**
   * Creates a new instance of ValidateCompact
   */
  public ValidateCompactSearch() {
     super();
    setDocTypeLocation("RETSCOMPACTSEARCH-20030710.dtd");
  }

}