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

import org.eclipse.wst.wsi.internal.core.util.WSIProperties;

/**
 * WS-I test tools property.
 */
public class WSITestToolsProperties
{
  protected static String installURL = "";
  protected static String tadfile = "";

  public final static String schemaDir = "common/schemas/";
  public final static String SSBP_ASSERTION_FILE = "http://www.ws-i.org/Testing/Tools/2005/01/SSBP10_BP11_TAD_1-0.xml";
  public final static String AP_ASSERTION_FILE = "http://www.ws-i.org/Testing/Tools/2004/12/AP10_BP11_SSBP10_TAD.xml";
  public final static String DEFAULT_ASSERTION_FILE = AP_ASSERTION_FILE;
  
  public static final String STOP_NON_WSI = "0";
  public static final String WARN_NON_WSI = "1";
  public static final String IGNORE_NON_WSI = "2";
  
  protected static boolean eclipseContext = false;

  /**
   *  Constructor.
   */
  protected WSITestToolsProperties()
  {
    super();
  }
  
  public static void setEclipseContext(boolean eclipseActive)
  {
    eclipseContext = eclipseActive;
  }
  
  public static boolean getEclipseContext()
  {
    return eclipseContext;
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

  /**
   * Checks the WS-I preferences for the given file and return them in a
   * WSIPreferences object.
   * 
   * @param fileuri The file URI to check the WS-I preferences for.
   * @return A WSIPreferences object containing the preference for this file URI.
   */
  public static WSIPreferences checkWSIPreferences(String fileuri)
  {
  	return new WSIPreferences();
  }
  
  /**
   * Remove file: and any slashes from the fileuri. 
   * Eclipse's resolution mechanism needs to start with the drive.
   */
  protected static String trimURI(String fileuri)
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
