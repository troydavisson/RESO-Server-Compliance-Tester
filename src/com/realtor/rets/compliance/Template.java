package com.realtor.rets.compliance;

import java.util.*;
import java.util.regex.*;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Template
{
    private static Log log = LogFactory.getLog(Template.class);
    Properties values = new Properties();
    static Pattern templateComment =
        Pattern.compile("(?si)\\$\\{\\s*([\\w\\.]+)\\s*\\}");

    public void setProperties( Properties props  ) {
        values = props;
    }

    public void set( String name, String value  ) {
        values.setProperty( name, value );
    }

    public String fillIn( String text )
            throws TemplateException {
        return fillIn( text, values);
    }

    static public String fillIn( String text, Properties values)
            throws TemplateException {
        
        if ( values == null) {
            // nothing to do
            return text;
        }

        StringBuffer errorList = new StringBuffer();

        Matcher matcher = templateComment.matcher( text );

        StringBuffer buffer = new StringBuffer();
        while ( matcher.find() ) {
            String name = matcher.group(1);
            String value = values.getProperty( name );
            if (value==null) {
                errorList.append("${"+name+"} ");
                //matcher.appendReplacement( buffer, "\\${$1}" );
            } else {
                matcher.appendReplacement( buffer, value );
            }
        }

        if (errorList.length() >0) {
            throw new TemplateException("Error: One or more missing properties-"+errorList.toString());
        }

        matcher.appendTail( buffer );
        return buffer.toString();
    }

    static public String fillIn( File f, Properties values)
            throws IOException, TemplateException {
        
        StringBuffer sb = new StringBuffer();
        char [] b = new char[1024];
        int n;

        Reader is = new FileReader(f);

        while ((n=is.read(b)) > 0) {
            sb.append(b, 0, n);
        }
       
        return fillIn( sb.toString(), values);
    }

    public static void main( String [] args )
    {
        String templateText =
            "&lt;html&gt;&lt;head&gt;\n"+
            "&lt;body&gt;\n"+
            "This is some text.\n"+
            "${foo.1}\n"+
            "Some more text.\n"+
            "\n"+
            "${ bar.1 }\n"+
            "More text.\n"+
            "${ bar.1\n"+
            "}\n"+
            "&lt;/body&gt;&lt;/html&gt;\n";

        Template template = new Template();
        template.set( "foo.1", "FooTemplate");
        template.set( "bar.1", "BarTemplate");
        try {
            System.out.println( templateText );
            System.out.println( "++++++++++++++++++++++++++++++++++++++++++" );
            System.out.println( template.fillIn(templateText) );
            System.out.println( "++++++++++++++++++++++++++++++++++++++++++" );
            System.out.println( Template.fillIn( templateText, template.values) );
        } catch (TemplateException e) {
            System.out.println("Error: "+e);
        }
    }

}
