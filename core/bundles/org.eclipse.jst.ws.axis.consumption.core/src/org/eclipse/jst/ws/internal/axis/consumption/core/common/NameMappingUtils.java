/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


package org.eclipse.jst.ws.internal.axis.consumption.core.common;

import java.util.Map;

import org.apache.axis.utils.JavaUtils;
import org.apache.axis.wsdl.toJava.Utils;

public class NameMappingUtils {

  private NameMappingUtils() {
  }

  public static String getPackageName(String namespace, Map ns2pkgMap)
  {
    if (namespace != null)
    {
      if (ns2pkgMap != null)
      {
        Object pkg = ns2pkgMap.get(namespace);
        if (pkg != null)
          return (String)pkg;
      }
      return namespaceURI2PackageName(namespace);
    }
    else
      return "";
  }

  /**
   * namespaceURI2PackageName
   * @param namespaceURI
   * @return package name based on namespace
   */
  public static String namespaceURI2PackageName(String namespaceURI)
  {
  	return Utils.makePackageName(namespaceURI);
  }
  
  /**
   * xmlNameToJavaClass
   * @param xmlname
   * @return Java class name
   */
  public static String xmlNameToJavaClass(String xmlname)
  {
  	return Utils.xmlNameToJavaClass(xmlname);
  }
  
  /**
   * getPortName
   * @param partName
   * @return port name used by the Axis emitter
   */
  public static String getPortName(String portName) 
  {
  	if (!JavaUtils.isJavaId(portName)) 
        portName = xmlNameToJavaClass(portName);
  	return portName;
  }
  
}
