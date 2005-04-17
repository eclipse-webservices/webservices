/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.ServletType;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.componentcore.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;

public class UpdateWEBXMLCommand extends SimpleCommand {

	private String LABEL = "TASK_LABEL_UPDATE_WEB_XML";
	private String DESCRIPTION = "TASK_DESC_UPDATE_WEB_XML";

	private MessageUtils msgUtils_;
	private IProject serverProject;

  public UpdateWEBXMLCommand()
  {
    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
    setName (msgUtils_.getMessage(LABEL));
    setDescription( msgUtils_.getMessage(DESCRIPTION));
  }

	public Status execute(Environment environment) {
		if (serverProject != null) {
			Status status = null;
			status = addServlet(serverProject, getAxisServletDescriptor());
			if (status.getSeverity() == Status.ERROR) {
				environment.getStatusHandler().reportError(status);
				return status;
			}
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
		return new SimpleStatus( "" );
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

	private ServletDescriptor getSoapMonitorServletDescriptor() {
		ServletDescriptor sd = new ServletDescriptor();
		sd._name = "SOAPMonitorService"; //$NON-NLS-1$
		sd._displayName = "SOAPMonitorService"; //$NON-NLS-1$
		sd._className = "org.apache.axis.monitor.SOAPMonitorService"; //$NON-NLS-1$
		sd._mappings = new String[] { "/SOAPMonitor" }; //$NON-NLS-1$
		sd._loadOnStartup = new Integer(100);
		Map params = new HashMap();
		params.put("SOAPMonitorPort", //$NON-NLS-1$
					 "5001");   //$NON-NLS-1$
		sd._params = params;
		return sd;
	}

	public Status addServlet(
		IProject webProject,
		ServletDescriptor servletDescriptor) {

		Object accessorKey = new Object();
		WebArtifactEdit webEdit = null;		
		StructureEdit structEdit = null;
		try {
			// 
			WebApp webapp = null;
			structEdit = StructureEdit.getStructureEditForWrite(webProject);;
			WorkbenchComponent wbcs[] = structEdit.getWorkbenchModules();
			
			webEdit = WebArtifactEdit.getWebArtifactEditForWrite(wbcs[0]);
			if (webEdit != null) {
				webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();
			//
			
			boolean foundServlet = false;

			List theServlets = webapp.getServlets();
			for (int i = 0; i < theServlets.size(); i++) {
				Servlet aServlet = (Servlet) theServlets.get(i);
				if (aServlet.getServletName().equals(servletDescriptor._name)) {
					foundServlet = true;
				}
			}

			if (foundServlet) {
				return new SimpleStatus( "" );
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
			webEdit.save(new NullProgressMonitor());
			}

			return new SimpleStatus( "" );
		} catch (Exception e) {

			return new SimpleStatus(
					"UpdateWEBXMLCommand.addServlet", //$NON-NLS-1$
					msgUtils_.getMessage("MSG_ERROR_UPDATE_WEB_XML"),
					Status.ERROR, e);
		}
		finally{
			if (webEdit != null)
				webEdit.dispose();	
			if (structEdit!=null)
				structEdit.dispose();
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
