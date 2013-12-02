/*
 * TestEvaluator.java
 *
 */

package com.realtor.rets.compliance;

import java.util.HashMap;


/**
 *
 * @author pobrien
 */
public interface TestEvaluator {
    
    String evaluate(HashMap trans, TestReport testReport);
    
}
