/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on May 8, 2004
 */
package org.eclipse.jst.ws.internal.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceImpl;

import com.ibm.wtp.emf.workbench.ProjectUtilities;


/**
 * @author gilberta
 */
public class J2EEActionAdapterFactory {
	
 	
  public static String getWSDLURI(ServiceImpl serviceImpl)
  {
  	Definition definition = serviceImpl.getEnclosingDefinition();
  	String location = definition.getLocation();
  	
  	return location;
  }
   
  //has to be under the webcontent
  public static String WEB_CONTENT = "WebContent";
  public static String getWSDLURI(ServiceRef serviceImpl)
  {
  	IProject project = ProjectUtilities.getProject(serviceImpl);
  	IPath path = project.getLocation().addTrailingSeparator();
  	path = path.append(WEB_CONTENT).addTrailingSeparator();
  	path = path.append(serviceImpl.getWsdlFile());
  	File file = new File(path.toString());
  	try{
  	  URL url = file.toURL();
  	  return url.toString();
  	}catch(MalformedURLException e){return null;}
  	
 }
  
  public static String getWSDLURI(WSDLResourceImpl wsdlRI)
  {
  	Definition definition = wsdlRI.getDefinition();
  	String location = definition.getLocation();
  	
  	return location;
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
