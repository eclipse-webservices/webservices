/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060222   125574 zina@ca.ibm.com - Zina Mostafia
 * 20060222   225574 gilberta@ca.ibm.com - Gilbert Andrews
 * 20090310   242440 yenlu@ca.ibm.com - Yen Lu, Pluggable IFile to URI Converter
 *******************************************************************************/
/*
 * Created on May 8, 2004
 */
package org.eclipse.jst.ws.internal.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.wst.ws.internal.converter.IIFile2UriConverter;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;


/**
 * @author gilberta
 */
public class J2EEActionAdapterFactory {
	 	  
  private static String getConvertedURIFromIFile(IFile file,String defaultURI)
  {
	String convertedLocation = null;
	boolean allowBaseConversionOnFailure = true;
	if (file != null && file.exists())
	{	
	  IIFile2UriConverter converter = WSPlugin.getInstance().getIFile2UriConverter();
	  if (converter != null)
	  {
		convertedLocation = converter.convert(file);
		allowBaseConversionOnFailure = converter.allowBaseConversionOnFailure();
	  }
	}
	if (convertedLocation == null && allowBaseConversionOnFailure)
	  return defaultURI;
	return convertedLocation;
  }
  
  private static String getConvertedURIFromURI(String originalURI)
  {
	IFile file = null;
    if (originalURI != null)
	{
	  String platformResource = "platform:/resource";
	  if (originalURI.startsWith(platformResource))
	    file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(originalURI.substring(platformResource.length())));
	  else if (originalURI.startsWith("file:"))
	  {
		String filePath = convertToRelative(originalURI);
		file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath));
	  }
	}
    return getConvertedURIFromIFile(file,originalURI);
  }
  
  public static String getWSDLURI(ServiceImpl serviceImpl)
  {
  	Definition definition = serviceImpl.getEnclosingDefinition();
  	String location = definition.getLocation();
  	return getConvertedURIFromURI(location);
  }
   
  //has to be under the webcontent
  public static final String EJB_MODULE = "ejbModule";
  public static final String APPCLIENT_MODULE = "appClientModule";
  public static final String WEB_MODULE = "WebContent";

  public static String getWSDLURI(ServiceRef serviceImpl)
  {
  	 String moduleRoot = null;
	 IProject project = ProjectUtilities.getProject(serviceImpl);
	 if(J2EEUtils.isWebComponent(project))
	 {
	   moduleRoot = WEB_MODULE;
	 }
     else if (J2EEUtils.isEJBComponent(project))
	 {
	   moduleRoot = EJB_MODULE;
	 }
	 else if (J2EEUtils.isAppClientComponent(project))
	 {
	   moduleRoot = APPCLIENT_MODULE;
	 }

	 if (moduleRoot != null)
	 {
	   IPath path = project.getLocation().addTrailingSeparator();
	   path = path.append(moduleRoot).addTrailingSeparator();
	   path = path.append(serviceImpl.getWsdlFile());
	   File file = new File(path.toString());
	   try {
	     URL url = file.toURL();
	     return getConvertedURIFromURI(url.toString());
	   }
	   catch(MalformedURLException e) {		   
	   }
	 }
	 return null;
 }
  
  public static String getWSDLURI(WSDLResourceImpl wsdlRI)
  {
  	Definition definition = wsdlRI.getDefinition();
  	String location = definition.getLocation();
  	return getConvertedURIFromURI(location);
  }
  
  public static String getWSILPath(WSDLResourceImpl wsdlRI)
  {
  	return convertToRelative(getWSDLURI(wsdlRI));
  }
  
  public static String getWSILPath(ServiceRef serviceImpl)
  {
  	return convertToRelative(getWSDLURI(serviceImpl));
  }
  
  public static String getWSILPath(ServiceImpl serviceImpl)
  {
  	return convertToRelative(getWSDLURI(serviceImpl));
  }
  
  public static String FILE="file:/";
  public static String FILEL="file:"; 
  public static String convertToRelative(String uri)
  {
  	//remove file:
  	String root = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    root = FILE + root;
  	String rootL = FILEL + root;
    if(uri.startsWith(root) || uri.startsWith(rootL)){
      return uri.substring(root.length());  	
    }
    return uri;
  }
}
