/*******************************************************************************
 * Copyright (c) 2003, 2010 WSO2 Inc, IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * WSO2 Inc. - modified and fix web.xml wiped out when Axis2 facet
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20070606   177421 sandakith@wso2.com - fix web.xml wiped out when Axis2 facet
 * 20091207   192005 samindaw@wso2.com - merge the web.xml to have axis2 welcome file defined
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.commands;


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
import org.eclipse.jst.j2ee.webapplication.ErrorCodeErrorPage;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.ServletType;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.jst.javaee.core.DisplayName;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.jst.javaee.web.WelcomeFileList;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class MergeWEBXMLCommand extends AbstractDataModelOperation {

	private IProject serverProject;
	private boolean extraAxis2TagsAdded = false;

  public MergeWEBXMLCommand( ) {
  }

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ){
		return Status.OK_STATUS;
	}
	
	public IStatus exexuteOverride( IProgressMonitor monitor){
		IEnvironment environment = getEnvironment();
		if (serverProject != null) {
			IStatus status = null;
			status = mergeWebXML(getAxisServletDescriptor());
			if (status.getSeverity() == Status.ERROR) {
				environment.getStatusHandler().reportError(status);
				return status;
			}
			status = mergeWebXML(getAdmintServletDescriptor());
			if (status.getSeverity() == Status.ERROR) {
				environment.getStatusHandler().reportError(status);
				return status;
			}
		}
		return Status.OK_STATUS;
	}
	
	private IStatus mergeWebXML(final ServletDescriptor servletDescriptor){
		IStatus status = Status.OK_STATUS;
		final IModelProvider provider = ModelProviderManager.getModelProvider(serverProject);
		provider.modify(new Runnable(){
			public void run() {
				Object object = provider.getModelObject();
				if (object instanceof org.eclipse.jst.javaee.web.WebApp){
					org.eclipse.jst.javaee.web.WebApp javaeeWebApp = 
									(org.eclipse.jst.javaee.web.WebApp) object;
					addJavaeeServlet(serverProject, servletDescriptor, javaeeWebApp);
				}
				if (object instanceof org.eclipse.jst.j2ee.webapplication.WebApp){
					WebApp webApp = (WebApp) object;
					addServlet(serverProject, servletDescriptor, webApp);
				}
			}
			
		}, null);
		return status;
	}
	
	private ServletDescriptor getAxisServletDescriptor() {

		ServletDescriptor sd = new ServletDescriptor();
		sd._name = "AxisServlet"; //$NON-NLS-1$
		sd._displayName = "Apache-Axis Servlet"; //$NON-NLS-1$
		sd._className = "org.apache.axis2.transport.http.AxisServlet"; //$NON-NLS-1$
		sd._mappings = new String[] { "/servlet/AxisServlet", //$NON-NLS-1$
										"*.jws", //$NON-NLS-1$
										"/services/*" }; //$NON-NLS-1$
		//sd._loadOnStartup = new Integer(1);
		return sd;
	}

	private ServletDescriptor getAdmintServletDescriptor() {
		ServletDescriptor sd = new ServletDescriptor();
		sd._name = "AxisAdminServlet"; //$NON-NLS-1$
		sd._displayName = "Apache-Axis Admin Servlet Web Admin"; //$NON-NLS-1$
		sd._className = "org.apache.axis2.transport.http.AxisAdminServlet"; //$NON-NLS-1$
		sd._mappings = new String[] { "/axis2-admin/*"	}; //$NON-NLS-1$
		sd._loadOnStartup = new Integer(100);
		return sd;
	}

	public IStatus addServlet(IProject webProject, ServletDescriptor servletDescriptor,
																			WebApp webapp) {
		try {
			   List theServlets = webapp.getServlets();
			   for (int i = 0; i < theServlets.size(); i++) {
				Servlet aServlet = (Servlet) theServlets.get(i);
				if (aServlet.getServletName().equals(servletDescriptor._name)) {
					return Status.OK_STATUS;
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
			   
			   if (!extraAxis2TagsAdded) {
				    //add the welcome file list
				    webapp.getFileList().addFileNamed("/axis2-web/index.jsp");
				    
				    //add the error pages
				    ErrorCodeErrorPage errorPage404 = factory.createErrorCodeErrorPage();
				    errorPage404.setErrorCode("404");
				    errorPage404.setLocation("/axis2-web/Error/error404.jsp");
				    webapp.getErrorPages().add(errorPage404);
				    
				    ErrorCodeErrorPage errorPage500 = factory.createErrorCodeErrorPage();
				    errorPage500.setErrorCode("500");
				    errorPage500.setLocation("/axis2-web/Error/error500.jsp");
				    webapp.getErrorPages().add(errorPage500);
				    extraAxis2TagsAdded = true;
			   }
			   
			return Status.OK_STATUS;
		} catch (Exception e) {

			return StatusUtils.errorStatus(
					"MSG_ERROR_UPDATE_WEB_XML",
					e);
		}
	}
	
	private void addJavaeeServlet(IProject webProject, ServletDescriptor servletDescriptor,
			org.eclipse.jst.javaee.web.WebApp webapp) {
		List theServlets = webapp.getServlets();
		for (int i = 0; i < theServlets.size(); i++) {
			org.eclipse.jst.javaee.web.Servlet aServlet = 
								(org.eclipse.jst.javaee.web.Servlet) theServlets.get(i);
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
		webapp.getServlets().add(servlet);
		if(servletDescriptor._mappings != null){
			for(int i=0; i<servletDescriptor._mappings.length; i++){
				org.eclipse.jst.javaee.web.ServletMapping servletMapping = 
													factory.createServletMapping();			
				servletMapping.setServletName(servlet.getServletName());
				UrlPatternType url = JavaeeFactory.eINSTANCE.createUrlPatternType();
				url.setValue(servletDescriptor._mappings[i]);
				servletMapping.getUrlPatterns().add(url);
				webapp.getServletMappings().add(servletMapping);					
			}
		}
		List welcomeFileLists = webapp.getWelcomeFileLists();
		if (welcomeFileLists!=null){
			for (Object list : welcomeFileLists) {
				if (list instanceof WelcomeFileList){
					WelcomeFileList welcomeList=(WelcomeFileList) list;
					if (!welcomeList.getWelcomeFiles().contains(Axis2Constants.AXIS2_WELCOME_FILE))
						welcomeList.getWelcomeFiles().add(Axis2Constants.AXIS2_WELCOME_FILE);
				}
			}
		}
	}
  

  public void setServerProject(IProject serverProject) {
    this.serverProject = serverProject;
  }
  
  public void setExtraAxis2TagsAdded(boolean flag) {
	    this.extraAxis2TagsAdded = flag;
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
