/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.wsi.internal.util.WSIProperties;

import com.ibm.etools.webservice.ui.plugin.WebServiceUIPlugin;
import com.ibm.etools.webservice.ui.wsi.preferences.PersistentWSIContext;

/**
 * WS-I test tools property.
 * 
 * @author Lawrence Mandel, IBM
 *
 */

public class WSITestToolsProperties
{
  protected static String installURL = "";
  protected static String tadfile = "";

  public final static String schemaDir = "common/schemas/";
  public final static String SSBP_ASSERTION_FILE = "common/profiles/SSBP10_BP11_TAD.xml";
  public final static String AP_ASSERTION_FILE = "common/profiles/AP10_BP11_SSBP10_TAD.xml";
  public final static String DEFAULT_ASSERTION_FILE = AP_ASSERTION_FILE;
  
  public static final String STOP_NON_WSI = PersistentWSIContext.STOP_NON_WSI; // "0";
  public static final String WARN_NON_WSI = PersistentWSIContext.WARN_NON_WSI; // "1";
  public static final String IGNORE_NON_WSI = PersistentWSIContext.IGNORE_NON_WSI; //"2";

  /**
   *  Constructor.
   */
  protected WSITestToolsProperties()
  {
    super();
    // TODO Auto-generated constructor stub
  }
  
  /**
   * Set the schema location.
   * @param dir the schema location.
   */
  public static void setInstallDir(String dir)
  {
    installURL = dir;
  }
  
  /**
   * Set local properties.
   */
  public static void setLocal()
  {
    Properties schemaLocationProperties = new Properties();
    String schemaURL = installURL + schemaDir;
    try
    {
      schemaLocationProperties.setProperty(
        "wsi.analyzer.xmlschema.schema",
        URIEncoder.encode(schemaURL + "XMLSchema.xsd", "UTF8"));
      schemaLocationProperties.setProperty(
        "wsi.analyzer.wsdl.schema",
        URIEncoder.encode(schemaURL + "wsdl11.xsd", "UTF8"));
      schemaLocationProperties.setProperty(
        "wsi.analyzer.soap.schema",
        URIEncoder.encode(schemaURL + "soapEnvelope.xsd", "UTF8"));
      schemaLocationProperties.setProperty(
        "wsi.analyzer.wsdlsoap.schema",
        URIEncoder.encode(schemaURL + "wsdlSoap.xsd", "UTF8"));
    }
    catch (UnsupportedEncodingException e)
    {
      schemaLocationProperties.setProperty(
        "wsi.analyzer.xmlschema.schema",
        URIEncoder.encode(schemaURL + "XMLSchema.xsd"));
      schemaLocationProperties.setProperty(
        "wsi.analyzer.wsdl.schema",
        URIEncoder.encode(schemaURL + "wsdl11.xsd"));
      schemaLocationProperties.setProperty(
        "wsi.analyzer.soap.schema",
        URIEncoder.encode(schemaURL + "soapEnvelope.xsd"));
      schemaLocationProperties.setProperty(
        "wsi.analyzer.wsdlsoap.schema",
        URIEncoder.encode(schemaURL + "wsdlSoap.xsd"));
    }
    WSIProperties.setThreadLocalProperties(schemaLocationProperties);
  }

 
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wsdl.validate.wsi.CheckWSI#checkWSI(java.lang.String)
   */
  public static WSIPreferences checkWSIPreferences(String fileuri)
  {
  	WSIPreferences preferences = new WSIPreferences();
  	
    // Remove file: and any slashes from the fileuri. 
  	// Eclipse's resolution mechanism needs to start with the drive.
    String uriStr = trimURI(fileuri);

    WebServiceUIPlugin wsui = WebServiceUIPlugin.getInstance();
    PersistentWSIContext APcontext = wsui.getWSIAPContext();
    PersistentWSIContext SSBPcontext = wsui.getWSISSBPContext();
    
    IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(uriStr));
    if (files != null && files.length == 1)
    {
      // check project level conpliance
      IProject project = files[0].getProject();
      
      if (APcontext.projectStopNonWSICompliances(project))
      {
      	preferences.setTADFile(installURL + AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      } 
      else if (APcontext.projectWarnNonWSICompliances(project))
      {
      	preferences.setTADFile(installURL + AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      else if (SSBPcontext.projectStopNonWSICompliances(project))
      {
      	preferences.setTADFile(installURL + SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      }
      else if (SSBPcontext.projectWarnNonWSICompliances(project))
      {
      	preferences.setTADFile(installURL + SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      else
      {
      	preferences.setTADFile(installURL + DEFAULT_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.IGNORE_NON_WSI);
      }
    }
    else
    {
      // If we can't obtain the project preference use the global preference.
      String APlevel = APcontext.getPersistentWSICompliance();
      String SSBPlevel = SSBPcontext.getPersistentWSICompliance();
      if(APlevel.equals(PersistentWSIContext.STOP_NON_WSI))
      {
      	preferences.setTADFile(installURL + AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      }
      else if(APlevel.equals(PersistentWSIContext.WARN_NON_WSI))
      {
      	preferences.setTADFile(installURL + AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      if(SSBPlevel.equals(PersistentWSIContext.STOP_NON_WSI))
      {
      	preferences.setTADFile(installURL + SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      }
      else if(SSBPlevel.equals(PersistentWSIContext.WARN_NON_WSI))
      {
      	preferences.setTADFile(installURL + SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      else
      {
      	preferences.setTADFile(installURL + DEFAULT_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.IGNORE_NON_WSI);
      }
    }
    return preferences;
  }
  
  /**
   * Remove file: and any slashes from the fileuri. 
   * Eclipse's resolution mechanism needs to start with the drive.
   */
  private static String trimURI(String fileuri)
  {
  	String uriStr = fileuri;
  	
    if(fileuri.startsWith("file:"))
    {
      uriStr = fileuri.substring(5);
    }
    while(uriStr.startsWith("/") || uriStr.startsWith("\\"))
    {
      uriStr = uriStr.substring(1);
    }
    return uriStr;
  }

}
