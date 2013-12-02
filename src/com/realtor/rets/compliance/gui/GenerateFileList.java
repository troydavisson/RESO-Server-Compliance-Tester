package com.realtor.rets.compliance.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * @author pobrien
 */
public class GenerateFileList
{
    public GenerateFileList() {}

    public Collection getFileList(File file, String extension) {
        File[] files = file.listFiles(new FileExtensionFilter(extension));
        if ( files != null && files.length > 0 ) {
            Collection collection = new ArrayList();
            for ( int i=0; i < files.length; i++ ) {
                collection.add(files[i].getName());
            }
            return collection;
        }
        return null;
    }

    public void generateFile(File destinationFile, Collection lineItems) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(destinationFile));
            Iterator iterator = lineItems.iterator();
            while ( iterator.hasNext() ) {
                writer.println(iterator.next().toString());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if ( writer != null ) writer.close();
        }
    }

    static public void main(String[] args) {
        GenerateFileList instance = new GenerateFileList();
        File directory =
                new File("D:\\Projects\\Avantia\\retsCompliance\\config\\TestScripts\\");
//        System.err.println("Directory exists --> " + directory.exists());
        Collection collection = instance.getFileList(directory, "xml");
        instance.generateFile(new File(directory, "testscripts.txt"), collection);
        System.err.println(collection.toString());
    }

    class FileExtensionFilter implements FilenameFilter {
      String pattern;
      FileExtensionFilter(String pattern) { this.pattern = pattern; }
      public boolean accept(File dir, String name) {
        String filename = new File(name).getName();
//          System.out.println("--> " + filename + " accept => " + (filename.indexOf(afn) != -1));
        return filename.indexOf(pattern) != -1;
      }
    }
}
