package com.realtor.rets.compliance;

import java.io.*;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * @author pobrien
 */
public class ResourceManager
{
    private static Log log = LogFactory.getLog(ResourceManager.class);    
    
    private String configDirectoryPath= File.separator + "config";

    private static final String[] DTD_RESOURCE_NAMES = {
            "RETS-20021015.dtd",
            "METADATA-20001001.dtd",
            "METADATA-20021015.dtd",
            "REData-20001001.dtd",
            "REData-20021015.dtd",
            "RETS-METADATA-20001001.dtd",
            "RETS-METADATA-20021015.dtd",
            "RETSCOMPACTMETA-20001001.dtd",
            "RETSCOMPACTMETA-20030710.dtd",
            "RETSCOMPACTSEARCH-20030710.dtd",
            "RETSMETADATA-20021015.dtd"
    };

    public ResourceManager() {}

    public ResourceManager(String path) {
        this();
        this.setConfigDirectoryPath(path);
    }

    public String getConfigDirectoryPath() { 
        return configDirectoryPath; 
    }
    
    public void setConfigDirectoryPath(String path) { 
        this.configDirectoryPath = path; 
    }

    public void assureConfigDirectory() {
        if ( configDirectoryPath != null ) {
            File configDirectory = new File(this.getConfigDirectoryPath());
            if ( !configDirectory.exists() ) {
                boolean result = configDirectory.mkdir();
                log.debug("Make directory " + getConfigDirectoryPath() + " => " + result);
            }
        }
    }


    public void downloadDTDs() {
        saveResourceStreamsLocally(DTD_RESOURCE_NAMES, new File(getConfigDirectoryPath()));
    }

    private void saveResourceStreamsLocally(String[] resourceNames) {
        saveResourceStreamsLocally(resourceNames, null);
    }

    public void saveResourceStreamsLocally(String resourceName, File directory) {
        saveResourceStreamsLocally(new String [] {resourceName}, directory);
    }

    private void saveResourceStreamsLocally(String[] resourceNames, File directory) {
        byte[] buffer = null;
        File newFile = null;
        InputStream inputStream = null;
        OutputStreamWriter writer = null;
        for ( int i=0; i < resourceNames.length; i++ ) {
            try {
                inputStream = getClass().getClassLoader().getResourceAsStream(resourceNames[i]);
                if ( inputStream != null ) {
                    if ( directory != null)
                        newFile = new File(directory, resourceNames[i]);
                    else
                        newFile = new File(resourceNames[i]);
                    
                    // ensure that the file's destination directory exists first
                    File parentDirectory = new File(newFile.getParent());
                    if (! parentDirectory.exists()) {
                        parentDirectory.mkdirs();
                    }

                    writer = new OutputStreamWriter(new FileOutputStream(newFile));

                    String line;
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    while ( (line = reader.readLine())!=null ) {
                        writer.write(line + "\n");
                    }
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public void main(String[] args) throws Exception {
        ResourceManager instance = new ResourceManager();
//        instance.downloadDTDs();

        Enumeration enumeration = ResourceManager.class.getClassLoader().getResources("");
        while ( enumeration.hasMoreElements() ) {
            System.out.println(enumeration.nextElement());
        }

    }
}
