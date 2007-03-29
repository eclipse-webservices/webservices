/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.tests.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.webservice.wsdd.ServletLink;
import org.eclipse.jst.j2ee.webservice.wsdd.ServiceImplBean;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;

public class WSCustomAdapterFactory implements IAdapterFactory {

	private static final Class[] types = {
		IFile.class, String.class
	};
	
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof ServletLink) {
			System.out.println("getAdapter on "+adaptableObject.toString());
			ServletLink beanLink = (ServletLink) adaptableObject;
			
			IFile wsdlFile = getWSDLFile(beanLink);
			System.out.println("wsdlFile = "+wsdlFile);
			if (adapterType == IFile.class) {
				return wsdlFile;
			} else 
				if (adapterType == String.class) {
				String wsdlFileString = wsdlFile.getLocationURI().toString();
				System.out.println("wsdlFileString = "+wsdlFileString);
				return wsdlFileString;
				} else {
					return null;
				}
			}
		else {
			return null;
		}
	}

	public Class[] getAdapterList() {
		return types;
	}
	
	private IFile getWSDLFile(ServiceImplBean bean) {
		  EObject eObject = bean.eContainer();
		  if (eObject == null)
			  return null;
	          
		  eObject = eObject.eContainer();
	      
		  if (eObject instanceof WebServiceDescription) {
			  WebServiceDescription wsd = (WebServiceDescription) eObject;
			  
			  IProject project = ProjectUtilities.getProject(wsd);    	  
	    	  
			  IPath path = J2EEUtils.getWebContentPath(project);    	 
			  path = path.append(wsd.getWsdlFile()); 
			  return ResourceUtils.getWorkspaceRoot().getFile(path); 	  
			      	    
		  }
		  return null;
	  }
	  
	  private IFile getWSDLFile(ServletLink link) {
		  EObject eObject = link.eContainer();
		  if (eObject instanceof ServiceImplBean)
			  return getWSDLFile((ServiceImplBean) eObject);
		  return null;
	  }

}
