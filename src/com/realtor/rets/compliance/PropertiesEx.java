package com.realtor.rets.compliance;
import java.util.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: paula
 * Date: 1/24/14
 * Time: 5:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesEx extends Properties {
    public void load(InputStream fis) throws IOException {
        System.out.println("in properties ex load");

        Scanner in = new Scanner(fis);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while(in.hasNext()) {
            out.write(in.nextLine().replace("\\","\\\\").getBytes());
            out.write("\n".getBytes());
        }

        InputStream is = new ByteArrayInputStream(out.toByteArray());
        super.load(is);
    }
}