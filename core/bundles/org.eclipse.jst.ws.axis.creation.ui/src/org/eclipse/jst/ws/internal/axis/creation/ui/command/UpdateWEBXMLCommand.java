/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20070528   186056 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.ServletType;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.jst.javaee.core.DisplayName;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class UpdateWEBXMLCommand extends AbstractDataModelOperation {

	private IProject serverProject;

  public UpdateWEBXMLCommand( )
  {
  }

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();
		if (serverProject != null) {
			IStatus status = null;
			status = addServlet(serverProject, getAxisServletDescriptor());
			if (status.getSeverity() == Status.ERROR) {
				environment.getStatusHandler().reportError(status);
				return status;
			}
			addServlet(serverProject, getAdmintServletDescriptor());
			if (status.getSeverity() == Status.ERROR) {
				environment.getStatusHandler().reportError(status);
				return status;
			}
		}
		return Status.OK_STATUS;
	}

	private ServletDescriptor getAxisServletDescriptor() {

		ServletDescriptor sd = new ServletDescriptor();
		sd._name = "AxisServlet"; //$NON-NLS-1$
		sd._displayName = "Apache-Axis Servlet"; //$NON-NLS-1$
		sd._className = "org.apache.axis.transport.http.AxisServlet"; //$NON-NLS-1$
		sd._mappings = new String[] { "/servlet/AxisServlet", //$NON-NLS-1$
										"*.jws", //$NON-NLS-1$
										"/services/*" }; //$NON-NLS-1$
		return sd;
	}

	private ServletDescriptor getAdmintServletDescriptor() {
		ServletDescriptor sd = new ServletDescriptor();
		sd._name = "AdminServlet"; //$NON-NLS-1$
		sd._displayName = "Axis Admin Servlet"; //$NON-NLS-1$
		sd._className = "org.apache.axis.transport.http.AdminServlet"; //$NON-NLS-1$
		sd._mappings = new String[] { "/servlet/AdminServlet" }; //$NON-NLS-1$
		sd._loadOnStartup = new Integer(100);
		return sd;
	}
	
	public IStatus addServlet(final IProject webProject, final ServletDescriptor servletDescriptor) {
	
		try {
			
			final IModelProvider provider = ModelProviderManager.getModelProvider(webProject);
			
			provider.modify(new Runnable() {
				public void run() {
					Object object = provider.getModelObject();
					if (object instanceof org.eclipse.jst.javaee.web.WebApp) {
						org.eclipse.jst.javaee.web.WebApp javaeeWebApp = (org.eclipse.jst.javaee.web.WebApp) object;
						addJavaeeServlet(webProject, servletDescriptor, javaeeWebApp);
					} else if (object instanceof org.eclipse.jst.j2ee.webapplication.WebApp) {
						WebApp webApp = (WebApp) object;
						addServlet(webProject, servletDescriptor, webApp);
					}
					
				}
			}
				, null);

			return Status.OK_STATUS;
	} catch (Exception e) {

		return StatusUtils.errorStatus(
				AxisCreationUIMessages.MSG_ERROR_UPDATE_WEB_XML,
				e);
	}
	}

	private void addServlet(IProject webProject, ServletDescriptor servletDescriptor, WebApp webapp) {

		List theServlets = webapp.getServlets();
		for (int i = 0; i < theServlets.size(); i++) {
			Servlet aServlet = (Servlet) theServlets.get(i);
			if (aServlet.getServletName().equals(servletDescriptor._name)) {
				return;
			}
		}

		WebapplicationFactory factory = WebapplicationFactory.eINSTANCE;

		Servlet servlet = factory.createServlet();
		ServletType servletType = factory.createServletType();
		servlet.setWebType(servletType);
		servlet.setServletName(servletDescriptor._name);
		servletType.setClassName(servletDescriptor._className);
		if(servletDescriptor._displayName != null){
			servlet.setDisplayName(servletDescriptor._displayName);
		}
		if(servletDescriptor._loadOnStartup != null){
			servlet.setLoadOnStartup(servletDescriptor._loadOnStartup);
		}
		if(servletDescriptor._params != null){
			Properties properties = servlet.getParamsAsProperties();
			properties.putAll(servletDescriptor._params);
		}
		webapp.getServlets().add(servlet);

		if(servletDescriptor._mappings != null){
			for(int i=0; i<servletDescriptor._mappings.length; i++){
				ServletMapping servletMapping = factory.createServletMapping();
				servletMapping.setServlet(servlet);
				servletMapping.setUrlPattern(servletDescriptor._mappings[i]);
				webapp.getServletMappings().add(servletMapping);					
			}
		}
	}
	
	private void addJavaeeServlet(IProject webProject, ServletDescriptor servletDescriptor, org.eclipse.jst.javaee.web.WebApp webapp) {

		List theServlets = webapp.getServlets();
	   for (int i = 0; i < theServlets.size(); i++) {
		org.eclipse.jst.javaee.web.Servlet aServlet = (org.eclipse.jst.javaee.web.Servlet) theServlets.get(i);
		if (aServlet.getServletName().equals(servletDescriptor._name)) {
			return;
		 }
	   }
	   
	   WebFactory factory = WebFactory.eINSTANCE;

	   org.eclipse.jst.javaee.web.Servlet servlet = factory.createServlet();

	   servlet.setServletName(servletDescriptor._name);
	   servlet.setServletClass(servletDescriptor._className);
	   
	   if(servletDescriptor._displayName != null){
		   DisplayName displayNameObj = JavaeeFactory.eINSTANCE.createDisplayName();
			displayNameObj.setValue(servletDescriptor._displayName);
			servlet.getDisplayNames().add(displayNameObj);  
	   }
	   if(servletDescriptor._loadOnStartup != null){
		  servlet.setLoadOnStartup(servletDescriptor._loadOnStartup);
	   }
//	   if(servletDescriptor._params != null){
//		  Properties properties = servlet.getParamsAsProperties();
//		  properties.putAll(servletDescriptor._params);
//	   }
	   webapp.getServlets().add(servlet);
	
	   if(servletDescriptor._mappings != null){
		  for(int i=0; i<servletDescriptor._mappings.length; i++){
			org.eclipse.jst.javaee.web.ServletMapping servletMapping = factory.createServletMapping();			
			servletMapping.setServletName(servlet.getServletName());
			UrlPatternType url = JavaeeFactory.eINSTANCE.createUrlPatternType();
			url.setValue(servletDescriptor._mappings[i]);
			servletMapping.getUrlPatterns().add(url);
			webapp.getServletMappings().add(servletMapping);					
		  }
	   }
}
  
  public void setServerProject(IProject serverProject)
  {
    this.serverProject = serverProject;
  }

	public class ServletDescriptor {
		String _name;
		String _className;
		String _displayName;
		Map _params;
		String[] _mappings;
		Integer _loadOnStartup;
	}

}
