RETS Server Compliance README

					The RETS 1.8 Server Compliance Checking Tool 
					============================================

This RETS Compliance project test suite contains a Java/Swing based application that allows those who implement a RETS server to verify that their server is compliant with the RETS 1.5 Specification.  


****** Manifest*******
This Project includes the following contents:
	README.txt			This document
	rc.bat				The "batch" file that a user would use to launch this application
  							at a Windows command prompt.
	config/         	Properties files, DTD's
	  TestScripts/		RETS 1.8 Spec Compliance TestScripts
					Java Application Files:
	lib/            	Common Java classes in JAR files
	
		ADDITIONAL Files & Directories included with the Source distribtion ONLY:
	BUILD.xml			The "build" file that will be used by Jakarta ANT to build from Source code
	BUILD.properties	The "build" properties file - Needs to be customized

	src/				The base directory for all of the source java files.  When you run the build everything within this directory will be used to create the RETSServerCompliance.jar.
	
					Software Requirements
					====================
	Java 1.7 JRE 
	
	
					Building From Source
					====================

You may also build your own version of this distribution from the source code, 

Please use the included Apache Ant build.xml file.  


Please note: Both the build.xml and the build.properties files (along with this file) are in the root directory of this src distribution, which we will call $COMPLIANCE_ROOT.

You should choose the following ant Main target:

 -jar

YOU WILL WANT to customize the properties settings in the build.properties.   The important properties to change are:

 -deploy -> defaults to $COMPLIANCE_ROOT/deploy
 -root.config -> defaults to /config (e.g. C:\config)
 
The other properties settings, you may (optionally) wish to change are:

 -build
 -classes
 
Please note that classes should be in the format ${build}/classes. 

If you change the value of root.config to something other than /config, you should also modify the $COMPLIANCE_ROOT/config/log4j.properites values 
 -log4j.appender.R1.File=/config/logs/retsClientAPI.log
 -log4j.appender.ServerComplianceLog.File=/config/logs/retsServerCompliance.log
 
You will want to replace /config/logs with YOUR path to YOUR logs directory, such as /documents/serverCompliance/deploy/config/logs.  

NOTE: The default logging location is $COMPLIANCE_ROOT\config\logs.



					Running from the Batch File 
					===========================
Program Use:

The rc.bat is used to run the application.  The rc.bat can be found in $COMPLIANCE_ROOT.  It contains the following command-line switches and be edited to override these defaults:

-c config
   specifies the configuration file directory of "config"
   default if unspecified is the "config" directory on the drive root

-t config\TestScripts
   specifies the test script directory of "config\TestScripts"
   default if unspecified is the configuration directory