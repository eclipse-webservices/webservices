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

import java.net.URI;

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

/**
 * The WSCustomAdapterFactory is an AdapterFactory similar to one an adopter would provide to adapt a custom object 
 * to a WSDL uri that the Web services wizards and the Web Services Explorer would take as input.  The Servlet Link, 
 * representing the service implementation under the JSR-109 Web services branch of the J2EE Project Explorer, is 
 * used as the "custom object" in this test.
 *
 */
public class WSCustomAdapterFactory implements IAdapterFactory {

	private static final Class[] types = {
		IFile.class, String.class
	};
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof ServletLink && ((adapterType == IFile.class ) || (adapterType == String.class))) {
			System.out.println("getAdapter on "+adaptableObject.toString());
			ServletLink beanLink = (ServletLink) adaptableObject;

			IFile wsdlFile = getWSDLFile(beanLink);
			System.out.println("wsdlFile = "+wsdlFile);
			if (adapterType == IFile.class) {
				return wsdlFile;
			} else 
				if (adapterType == String.class) {
					URI wsdlUri = wsdlFile.getLocationURI();
					String wsdlFileString = null;
					if (wsdlUri != null) {
						wsdlFileString = wsdlUri.toString();
						System.out.println("wsdlFileString = "+wsdlFileString);
					}
					return wsdlFileString;
				} else {
					return null;
				}
		}
		else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		return types;
	}
	
	/**
	 * @param bean The ServiceImplBean
	 * @return The IFile representing the WSDL file for this ServiceImplBean.
	 */
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
	  
	  /**
	 * @param link The ServletLink
	 * @return The IFile representing the WSDL file for this ServletLink
	 */
	private IFile getWSDLFile(ServletLink link) {
		  EObject eObject = link.eContainer();
		  if (eObject instanceof ServiceImplBean)
			  return getWSDLFile((ServiceImplBean) eObject);
		  return null;
	  }

}
