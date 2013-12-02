package com.realtor.rets.compliance;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * @author pobrien
 * <p/>
 * Trivial wrapper around hashmap and properties file loaded into hashmap
 * Just doing such that if implementation of where properties is persisted changes,
 * don't have to worry about 'clients' of PropertyManager
 */
public class PropertyManager {
    static private PropertyManager instance = null;
    static private Map instanceMap = new TreeMap();

    private static Log log = LogFactory.getLog(PropertyManager.class);

    private Map map = new TreeMap();

    private String filename = null;
    private static String configDirectory = File.separator + "config";  // default
    private static String testFile = null;
    private static String testDirectory = configDirectory;

    private PropertyManager(String filename) {
        this.filename = filename;
    }

    static public PropertyManager getInstance(String instanceName) {
        PropertyManager pm = (PropertyManager) instanceMap.get(instanceName);
        if (pm == null) {
        	pm = new PropertyManager(instanceName);
            instanceMap.put(instanceName, pm);
        }
        return pm;
    }

    public Iterator iterator() {
        return map.keySet().iterator();
    }

    public Set getKeys() {
        if ( map != null ) return map.keySet();
        else return null;
    }

    public String getValue(String key) {
        return (map.get(key) != null) ? (String) map.get(key) : null;
    }

    public String getProperty(String key) {
        return this.getValue(key);
    }

    public void putValue(String key, String value) {
        map.put(key, value);
    }

    public static String getConfigDirectory() {
        return configDirectory;
    }

    public static void setConfigDirectory(String newConfigDirectory) {
        PropertyManager.configDirectory = newConfigDirectory;
    }

    public Properties getProperties() {
        Object o = null;
        Properties props = new Properties();
        if ( map == null || map.isEmpty() ) load();
        Iterator iterator = map.keySet().iterator();
        while ( iterator.hasNext() ) {
            o = iterator.next();
            props.put(o, map.get(o));
        }
        return props;
    }

    /**
     * Write the properties to a file in their current working directory.  When user comes back to the
     * application, it will look for this file first in their working directory then default to the properties
     * file included in the jar (classpath loaded)
     *
     * @TODO : Do we need to consider more explicit properties file locations?
     */
    public void persist() throws IOException {
        if (filename != null) {
            Properties properties = new Properties();
            File userDirectory = new File(getConfigDirectory());
            File file = new File(filename);
            PrintWriter writer = new PrintWriter(new FileOutputStream(file));
            String string = null;
            Object key = null, value = null;
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                key = iterator.next();
                value = map.get(key);
                if (value == null)
                    string = "";
                else
                    string = value.toString();
                if (log.isDebugEnabled()) {
                	log.debug(key.toString() + " = " + string);
                }
                writer.println(key.toString() + "=" + string);
                properties.setProperty(key.toString(), string);
            }
            writer.close();
        }

    }


    public void load() {
        if (filename != null) {
            map.clear();
            Properties properties = new Properties();
            try {
                InputStream inputStream = null;
                File file = new File(filename);
                if (file.exists()) {
                    // load from working directory
                    inputStream = new FileInputStream(file);
                    if (log.isDebugEnabled()) {
                    	log.debug("Using file --> " + file.getAbsolutePath());
                    }
                } else {
                    // load the default properties file from classpath
                    inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
                    if (log.isDebugEnabled()) {
                    	log.debug("Using classloaded file");
                    }
                }
                if (inputStream != null) {
                	properties.load(inputStream);
                }
//                properties.list(System.out);
            } catch (IOException ioe) {
                ioe.printStackTrace();  // @TODO : do we have logging in this app somewhere?
                return;
            }
            Object key = null;
            Enumeration enumeration = properties.keys();
            while (enumeration.hasMoreElements()) {
                key = enumeration.nextElement();
                map.put(key.toString(), properties.get(key));
            }
            fixEncodedValues();
        }
    }

    public void fixEncodedValues() {
        String key = null, value = null;
        Iterator iterator = map.keySet().iterator();
        while ( iterator.hasNext() ) {
            key = (String) iterator.next();
            value = (String) map.get(key);
            if ( value != null ) {
                while ( value.indexOf('\\') > -1 ) {
                    int index = value.indexOf('\\');
                    value = value.substring(0,index) + value.substring(index+1);
                }
            }
            map.put(key, value);
        }
        System.out.println("Fixed encoded characters");
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    /**
        Get the parameters properties PropertyManager.
        @return the parameters properties PropertyManager.
     */
    public static PropertyManager getParametersPropertyManager() {
    	String parameterFile = getConfigDirectory() + File.separator + "TestParameters.properties";
        if (log.isDebugEnabled()) {
            log.debug("Params File: " + parameterFile);
        }

        try {
        	return getInstance(parameterFile);
        }
        catch (Exception e) {
            log.error("ERROR: could not get parameter File: " + parameterFile);
            log.error(e + parameterFile);
            return null;
        }
    }

    /**
     	Get the client properties PropertyManager.
     	@return the client properties PropertyManager.
     */
    public static PropertyManager getClientPropertyManager() {
    	String configFile = getConfigDirectory() + File.separator + "TestClient.properties";
        if (log.isDebugEnabled()) {
            log.debug("Config File: " + configFile);
        }

        try {
        	return getInstance(configFile);
        }
        catch (Exception e) {
            log.error("ERROR: could not get parameter File: " + configFile);
            log.error(e + configFile);
            return null;
        }
    }

    /**
        Set the test file.
     */
    public static void setTestFile(String newTestFile) {
    	testFile = newTestFile;
    }

    /**
     	Get the test file.
     */
    public static String getTestFile() {
    	return testFile;
    }

    /**
     	Set the test directory.
    */
    public static void setTestDirectory(String newTestDirectory) {
    	testDirectory = newTestDirectory;
    }

    /**
     	Get the test directory.
    */
    public static String getTestDirectory() {
    	return testDirectory;
    }

    static public void main(String[] args) {
        PropertyManager pm1 = PropertyManager.getInstance("TestParameters.properties");
        PropertyManager pm2 = PropertyManager.getInstance("TestClient.properties");
        pm1.load();
        System.out.println("------------------");
        pm2.load();
    }
}


