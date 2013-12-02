/* $Header$ 
 */
package com.realtor.rets.compliance.tests.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.realtor.rets.compliance.gui.ReportForm;

/**
 *  A Utility class used by other testing/evaluator classes to provide
 *      dateTime "testing" functionality.
 * 
 * @author pobrien
 */
public class DateTimeUtils {
    
    private static Log log = LogFactory.getLog(ReportForm.class);
    private final static long HALF_HOUR_IN_MILLIS = 1800000;
    
    public static Calendar getGMTDateTimeNow() {
        TimeZone gmtZone = TimeZone.getTimeZone("GMT");        
        Calendar gmtCalendar = new GregorianCalendar();
        gmtCalendar = Calendar.getInstance(gmtZone);
  
        return gmtCalendar;
    }

    public static Calendar getGMTTimeFromString(String DateHeader) 
                throws ParseException{
        
        TimeZone gmtZone = TimeZone.getTimeZone("GMT");
        Calendar gmtCalendar = null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");        
        
        try {
            Date retsHeaderDate = dateFormatter.parse(DateHeader);
            gmtCalendar = Calendar.getInstance();
            gmtCalendar.setTime(retsHeaderDate); 
            
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.debug("Date Parsing EXCEPTION: " + e.getMessage());
            }
            e.printStackTrace();
            throw e;
        }        
        
        return gmtCalendar;
    }
    
    public static boolean CompareRETSTimeToNow(Calendar calDateHeader, 
                            Calendar gmtCalendarNow) {
        
        boolean isValidRETSTime = false;
        long headerTimeInMillis;
        long nowInMillis;
        long variance;
        
        headerTimeInMillis = calDateHeader.getTimeInMillis();
        nowInMillis = gmtCalendarNow.getTimeInMillis();
        variance = nowInMillis - headerTimeInMillis;
        log.debug("Variance - millis: " + variance);
        if (variance < HALF_HOUR_IN_MILLIS ) {
            isValidRETSTime = true;            
        }
        
        return isValidRETSTime;
    }

}
