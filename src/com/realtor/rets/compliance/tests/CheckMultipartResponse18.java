/*
 * CheckMultipartResponse
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.CollectionUtils;
import org.realtor.rets.retsapi.RETSGetObjectTransaction;
import org.realtor.rets.retsapi.RETSTransaction;

import javax.mail.internet.MimeBodyPart;
import java.util.Iterator;
import java.util.Map;

/**
 * Extends the TestEvaluator Interface (extends BaseEvaluator) Check for
 * compliance to MultipartMime format
 *
 * @author pobrien
 */
public class CheckMultipartResponse18 extends BaseEvaluator {


  public CheckMultipartResponse18() {
  }


protected TestResult processResults(String transName,RETSTransaction t) {
      if (t == null) {
        return reportResult("CheckMultipartResponse",
                                              "Transaction \"" + transName
                                                + "\" is null, check the transaction definition",
                                              "Failure",
                                              "Transaction could not be constructed");
      } else {
		  Map responseHeaders = CollectionUtils.copyLowerCaseMap(t.getResponseHeaderMap());

		  Object contentType=responseHeaders.get("content-type");
		  String contentTypeString=contentType.toString();

		  	if (contentTypeString==null
		  			|| (contentTypeString.indexOf("multipart/parallel")<0)){
			  return reportResult("CheckMultipartResponse",
			                                                "Content-type \"" + contentTypeString
			                                                  + "\" is not multipart/parallel",
			                                                "Failure",
                                              "Invalid content-type");
		  	}
		  String boundary=getBoundary(contentTypeString.replace("[","").replace("]",""));

		  	if (boundary==null){
						  return reportResult("CheckMultipartResponse",
						                                                "boundary is null",
						                                                "Failure",
			                                              "Missing boundary");
		  	}
		  String notes="Content-type:"+contentTypeString+" boundary "+boundary;
        	MimeBodyPart part = null;
		    Iterator iterator = ((RETSGetObjectTransaction) t).allReturnedObjects().iterator();
				int i = 0;

			    try{
			    	while ( iterator.hasNext() ) {
        			part = (MimeBodyPart) iterator.next();
        			String contentId=part.getHeader("Content-ID",";");
        			String objectId=part.getHeader("Object-ID",";");

        		 	if (contentId==null||objectId==null){
						  return reportResult("CheckMultipartResponse",
						                                                "missing required header field(s):"
						                                                + "Content-ID andObject-ID  must all be present"
						                                                + "values here are: Content-ID"+contentId+",Object-ID:"+objectId,
						                                                "Failure",
			                                              "Missing Required Multipart Header Fields");
        		 	}
        			
        			}
				} catch (Exception e) {
				return reportResult("CheckMultipartResponse",
				                     "Transaction \"" + transName + "failed, could not create mime body part",
				                       "Failure",
                                      e.getMessage());
				}



        return reportResult("CheckMultipartResponse",
		                                    "Transaction \"" + transName
		                                      + "\" Multipart formatted correctly","Success", notes);

      }

  }

  private String getBoundary(String contentString){
  		  contentString=contentString.trim();
          int boundIdx = contentString.indexOf("boundary=");
          int endIdx = contentString.length();
  		  String boundary="--"+contentString.substring(boundIdx+9,endIdx).replace("\"","")+"--";
          System.out.println("getBoundary boundary: "+boundary);
		  return boundary;
  }

  }
