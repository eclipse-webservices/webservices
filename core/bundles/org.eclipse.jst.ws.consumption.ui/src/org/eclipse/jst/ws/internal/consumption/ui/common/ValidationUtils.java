/*******************************************************************************
 * Copyright (c) 2004, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060222   115834 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   136158 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   136705 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060421   136761 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060425   137831 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427   126780 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20060905   156230 kathy@ca.ibm.com - Kathy Chan, Handling projects with no target runtime
 * 20070119   159458 mahutch@ca.ibm.com - Mark Hutchinson
 * 20070723   194434 kathy@ca.ibm.com - Kathy Chan, Check for non-existing EAR with content not deleted
 * 20071107   203826 kathy@ca.ibm.com - Kathy Chan
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 * 20080205   170141 kathy@ca.ibm.com - Kathy Chan
 * 20080416   215084 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080417   227599 kathy@ca.ibm.com - Kathy Chan
 * 20080717   241283 ericdp@ca.ibm.com - Eric D. Peters, Class version error when invoking sample JSP on Tomcat 5.5 when Java project is Java 6
 * 20081001   243869 ericdp@ca.ibm.com - Eric D. Peters, Web Service tools allowing mixed J2EE levels
 * 20090121   261730 zhang@ca.ibm.com - Allan Zhang, WebService client runtime id return null
 * 20090302   249602 ericdp@ca.ibm.com - Eric D. Peters, PII- association warning message needs update
 * 20090401   269994 ericdp@ca.ibm.com - Eric D. Peters, NLS- Validation messages unclear when servers don't support runtime/project combo
 * 20100511   309395 mahutch@ca.ibm.com - Mark Hutchinson, WS Wizard Converting Java Project into Utility Project without any warning
 * 20101207   332067 mahutch@ca.ibm.com - Mark Hutchinson, Consider server in web service wizard validation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jem.util.emf.workbench.WorkbenchResourceHelperBase;
import org.eclipse.jst.common.project.facet.JavaFacetUtils;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.project.JavaEEProjectUtilities;
import org.eclipse.jst.j2ee.webservice.internal.WebServiceConstants;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WSDLPort;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServices;
import org.eclipse.jst.j2ee.webservice.wsdd.WsddResource;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.RuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.VersionFormatException;
import org.eclipse.wst.common.project.facet.core.internal.ProjectFacetVersion;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntimeChecker;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

/**
 *
 */
public class ValidationUtils
{
	//Constants to help decide how much validation to do.
	public static final int VALIDATE_NONE = 0;
	public static final int VALIDATE_ALL = 1;
	public static final int VALIDATE_SERVER_RUNTIME_CHANGES = 2;
	public static final int VALIDATE_PROJECT_CHANGES = 3;
	public static final int VALIDATE_SCALE_CHANGES = 4;

  /**
   * 
   */
  public ValidationUtils()
  {
  }
  
  /**
   * Returns a new validation state based on the current validation state
   * and the requested validation state. If the requested validation state
   * does not cover all the validation covered by the current validation
   * state, VALIDATE_ALL is returned. Otherwise, the requested validation
   * state is returned.
   * 
   * @param currentValidationState the validaton state at the time the caller's requests a change
   * @param requestedValidationState the validation state being requested by the caller.
   * @return Returns VALIDATE_ALL if the requested validation state
   * does not cover all the validation covered by the current validation
   * state. Returned requestedValidationState otherwise.
   */
  public int getNewValidationState(int currentValidationState, int requestedValidationState) {
		int newValidationState;
		if (currentValidationState == VALIDATE_NONE
				|| currentValidationState == requestedValidationState) {
			newValidationState = requestedValidationState;
		} else {
			newValidationState = VALIDATE_ALL;
		}

		return newValidationState;

	}
  
  /**
   * Returns IStatus resulting from checking for empty fields. Used for validation of page 1 of the
   * Web service/client wizards.
   * @param validationState one of VALIDATE_NONE, VALIDATE_ALL, VALIDATE_SERVER_RUNTIME_CHANGES, VALIDATE_PROJECT_CHANGES, VALIDATE_SCALE_CHANGES
   * @param typeId Web service type id (isClient=false) or Web service client implementation type id (isClient=true) 
   * @param serviceImpl String representation of the object selection from page 1
   * @param runtimeId Web service runtime id
   * @param serverId server type id
   * @param projectName name of project
   * @param needEar boolean <code>true</code> if EAR is required, <code>false</code> if not.
   * @param earProjectName name of EAR project
   * @param projectTypeId template id
   * @param isClient boolean <code>true</code> if the method is being called for client side validation, 
   * <code>false</code> for service side validation.
   * @return IStatus with severity IStatus.OK if all mandatory fields are non-null and non-empty. 
   * IStatus with severity IStatus.ERROR otherwise.
   */
  public IStatus checkMissingFieldStatus(int validationState, String typeId, String serviceImpl, String runtimeId, String serverId,
			String projectName, boolean needEar, String earProjectName, String projectTypeId,
			boolean isClient)
  {
		// Object selection	  
	  if (validationState==VALIDATE_ALL && !isClient)
	  {
		if (serviceImpl.length() == 0) {
			int scenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(typeId);
			if (scenario == WebServiceScenario.BOTTOMUP)
			{
			  return StatusUtils
					.errorStatus(ConsumptionUIMessages.MSG_NO_OBJECT_SELECTION);
			}
			else
			{
			  return StatusUtils
					.errorStatus(ConsumptionUIMessages.MSG_NO_SERVICE_SELECTION);				
			}
		}
	  }

		// Web service runtime
	    if (validationState == VALIDATE_ALL || validationState == VALIDATE_SERVER_RUNTIME_CHANGES) {
			if (runtimeId == null || runtimeId.length() == 0) {
				if (isClient) {
					return StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_RUNTIME,
							new String[] { ConsumptionUIMessages.MSG_CLIENT_SUB }));
				} else {
					return StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_RUNTIME,
							new String[] { ConsumptionUIMessages.MSG_SERVICE_SUB }));
				}
			}
		}

	    if (validationState == VALIDATE_ALL || validationState == VALIDATE_PROJECT_CHANGES) {
			// Project
			if (projectName == null || projectName.length() == 0) {
				if (isClient) {
					return StatusUtils.errorStatus(NLS.bind(
							ConsumptionUIMessages.MSG_CLIENT_PROJECT_EMPTY, new String[] { "" }));
				} else {
					return StatusUtils.errorStatus(NLS.bind(
							ConsumptionUIMessages.MSG_SERVICE_PROJECT_EMPTY, new String[] { "" }));
				}
			}

			// Project type (if project does not exist)
			IProject project = ProjectUtilities.getProject(projectName);
			if (!project.exists()) {
				if (projectTypeId == null || projectTypeId.length() == 0) {

					if (isClient) {
						return StatusUtils
								.errorStatus(ConsumptionUIMessages.MSG_CLIENT_PROJECT_TYPE_EMPTY);
					} else {
						return StatusUtils
								.errorStatus(ConsumptionUIMessages.MSG_SERVICE_PROJECT_TYPE_EMPTY);
					}
				}
			}

			// Ear (if need ear)
			if (needEar) {
				if (earProjectName == null || earProjectName.length() == 0) {
					if (isClient) {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_CLIENT_EAR_EMPTY, new String[] { "" }));
					} else {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_SERVICE_EAR_EMPTY, new String[] { "" }));
					}
				}
			}
		}
	    
		// Server (if Web service runtime requires a server or project does not
		// exist)
	    if (validationState == VALIDATE_ALL || validationState == VALIDATE_SERVER_RUNTIME_CHANGES
				|| validationState == VALIDATE_PROJECT_CHANGES) {
			if (serverId == null || serverId.length() == 0) {
				RuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getRuntimeById(runtimeId);
				if (desc.getServerRequired()) {
					if (isClient) {
						StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_SERVER,
								new String[] { ConsumptionUIMessages.MSG_CLIENT_SUB }));
					} else {
						StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_SERVER,
								new String[] { ConsumptionUIMessages.MSG_SERVICE_SUB }));
					}
				} else {
					IProject project = ProjectUtilities.getProject(projectName);
					if (!project.exists()) {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_PROJECT_MUST_EXIST,
								new String[] { projectName }));

					}
				}
			}
		}
		
		return Status.OK_STATUS;
  }
  
  /**
   * Returns IStatus resulting from checking for errors. Used for validation of page 1 of the
   * Web service/client wizards.
   * @param validationState one of VALIDATE_NONE, VALIDATE_ALL, VALIDATE_SERVER_RUNTIME_CHANGES, VALIDATE_PROJECT_CHANGES, VALIDATE_SCALE_CHANGES
   * @param typeRuntimeServer Web service runtime, type and server 
   * @param projectName name of project
   * @param needEar boolean <code>true</code> if EAR is required, <code>false</code> if not.
   * @param earProjectName name of EAR project
   * @param projectTypeId template id
   * @param isClient boolean <code>true</code> if the method is being called for client side validation, 
   * <code>false</code> for service side validation.
   * @return IStatus with severity IStatus.OK if no errors are present,
   * IStatus with severity IStatus.ERROR otherwise.
   */
  public IStatus checkErrorStatus(int validationState, TypeRuntimeServer typeRuntimeServer, String projectName, String initialProjectName, boolean needEar, String earProjectName, String projectTypeId,
			boolean isClient) {
	
	  	// Ensure server, Web service runtime, and Web service type are
		// compatible

		String typeId = typeRuntimeServer.getTypeId();
		String runtimeId = typeRuntimeServer.getRuntimeId();
		String serverId = typeRuntimeServer.getServerId();
		String serverInstanceId = typeRuntimeServer.getServerInstanceId();

		// Labels
	    String serverLabel = "";
	    if (serverId != null && serverId.length()>0)
	    {
	    	serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverId);
	    }
		String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
		
	    if (validationState == VALIDATE_ALL || validationState == VALIDATE_SERVER_RUNTIME_CHANGES) {
	    	if (serverId != null && serverId.length() > 0) {
				if (isClient) {
					if (!WebServiceRuntimeExtensionUtils2
							.isServerClientRuntimeTypeSupported(serverId,
									runtimeId, typeId)) {
						return StatusUtils
								.errorStatus(NLS
										.bind(
												ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS,
												new String[] { serverLabel,
														runtimeLabel }));
					}
				} else {
					if (!WebServiceRuntimeExtensionUtils2
							.isServerRuntimeTypeSupported(serverId, runtimeId,
									typeId)) {
						return StatusUtils
								.errorStatus(NLS
										.bind(
												ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS,
												new String[] { serverLabel,
														runtimeLabel }));
					}
				
					if (initialProjectName != "" && serverId != null && !projectName.equals(initialProjectName) &&
				    		(WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(typeId) == WebServiceScenario.BOTTOMUP) &&
				    		J2EEUtils.isJavaComponent(ProjectUtilities.getProject(initialProjectName))){
				    		
				    		if(!doesServerSupportProject(serverId,initialProjectName)){
				    	    	return StatusUtils.errorStatus(NLS.bind(
										ConsumptionUIMessages.MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_JAVAPROJECT,
										new String[] { serverLabel, initialProjectName }));
				  		    }
				    	}
				}
			}
	    	
		}
	    
		// If the project exists, ensure it supports the Web service type, Web
		// service runtime, and server. If the Ear also exists and the project
		// and Ear are not already associated, ensure they can be.
		// If the project does not exist, ensure the project type supports the
		// Web service type, Web service runtime, and server
		if (validationState == VALIDATE_ALL || validationState == VALIDATE_SERVER_RUNTIME_CHANGES
				|| validationState == VALIDATE_PROJECT_CHANGES) {
			ValidationUtils valUtils = new ValidationUtils();
			IProject project = ProjectUtilities.getProject(projectName);
			if (project.exists()) {

				if (isClient) {
					// Check if the runtime supports it.
					if (!WebServiceRuntimeExtensionUtils2.doesClientTypeAndRuntimeSupportProject(
							typeId, runtimeId, projectName)) {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_CLIENT_RUNTIME_DOES_NOT_SUPPORT_PROJECT,
								new String[] { runtimeLabel, serverLabel, projectName }));
					}

					// Check if the server supports it.
					if (serverId != null && serverId.length() > 0) {
			        	  
						if (!valUtils.doesServerSupportProject(serverId, projectName)) {
							// Server does not support the project...
							
							boolean isJavaUtilProject = project != null ? FacetUtils.isJavaProject(project) : false ;
			    		  
							// If this error is due to differing Java facet versions on a Java utility project, display a specific error message
							if(isJavaUtilProject && doesServerSupportClientProject(serverId, projectName, true)) {
								return StatusUtils.errorStatus(NLS.bind(
										ConsumptionUIMessages.MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_PROJECT_JAVA_UTIL_VERSION,
										new String[] { serverLabel, projectName }));
			    			  
							} else {
								// Otherwise display a general error
								return StatusUtils.errorStatus(NLS.bind(
										ConsumptionUIMessages.MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_PROJECT,
										new String[] { serverLabel, projectName }));
							}
						}
					}
				} else {
					// Check if the runtime supports it.
					if (!WebServiceRuntimeExtensionUtils2.doesServiceTypeAndRuntimeSupportProject(
							typeId, runtimeId, projectName)) {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_SERVICE_RUNTIME_DOES_NOT_SUPPORT_PROJECT,
								new String[] { runtimeLabel, serverLabel, projectName }));
					}

					// Check if the server supports it.
					if (serverId != null && serverId.length() > 0)
					{
					  if (!valUtils.doesServerSupportProject(serverId, projectName)) {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_PROJECT,
								new String[] { serverLabel, projectName }));
					  }
					}
				}
			} else {
				// Look at the project type to ensure that it is suitable for
				// the
				// selected runtime and server.
				String templateId = projectTypeId;
				String templateLabel = FacetUtils.getTemplateLabelById(templateId);

				if (isClient) {
					// Check if the runtime supports it.
					if (!WebServiceRuntimeExtensionUtils2.doesClientTypeAndRuntimeSupportTemplate(
							typeId, runtimeId, templateId, serverId)) {

						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_CLIENT_RUNTIME_DOES_NOT_SUPPORT_TEMPLATE,
								new String[] { runtimeLabel, serverLabel, templateLabel }));
					}

					// Check if the server supports it.
					if (serverId != null && serverId.length()>0)
					{
					  if (!valUtils.doesServerSupportTemplate(serverId, templateId)) {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_TEMPLATE,
								new String[] { serverLabel, templateLabel }));
					  }
					}

				    if (WebServiceRuntimeExtensionUtils2.getClientRuntimeId(typeRuntimeServer, projectName, projectTypeId) == "")
				    {
				    	return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_CLIENT_RUNTIME_DOES_NOT_SUPPORT_TEMPLATE,
								new String[] { runtimeLabel, serverLabel, templateLabel }));
				    }
					
				} else {
					// Check if the runtime supports it.
					if (!WebServiceRuntimeExtensionUtils2.doesServiceTypeAndRuntimeSupportTemplate(
							typeId, runtimeId, templateId, serverId)) {
						return StatusUtils
								.errorStatus(NLS
										.bind(
												ConsumptionUIMessages.MSG_SERVICE_RUNTIME_DOES_NOT_SUPPORT_TEMPLATE,
												new String[] { runtimeLabel, serverLabel, templateLabel }));
					}

					// Check if the server supports it.
					if (serverId != null && serverId.length()>0)
					{
					  if (!valUtils.doesServerSupportTemplate(serverId, templateId)) {
						return StatusUtils.errorStatus(NLS.bind(
								ConsumptionUIMessages.MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_TEMPLATE,
								new String[] { serverLabel, templateLabel }));
					  }
					}
				}

			}						
			
		}
		
	    if (validationState == VALIDATE_ALL || validationState == VALIDATE_PROJECT_CHANGES) {
			// Check if project/EAR association is good.
			if (needEar) {
				IProject project = ProjectUtilities.getProject(projectName);
				IProject ep = ProjectUtilities.getProject(earProjectName);
				
				// 194434 - Check for non-existing EAR with contents that's not deleted previously
				IStatus canCreateEARStatus = J2EEUtils.canCreateEAR(ep);
				if (!canCreateEARStatus.isOK()) {
					return canCreateEARStatus;
				}
			
				if (project.exists() && ep.exists()) {
					if (!J2EEUtils.isComponentAssociated(ep, project)) {
						boolean associateStatus = J2EEUtils.canAssociateProjectToEARWithoutWarning(project, ep);
						if (!associateStatus) {
							if (isClient) {
								return StatusUtils.warningStatus(NLS.bind(
										ConsumptionUIMessages.MSG_CLIENT_CANNOT_ASSOCIATE,
										new String[] { projectName, ep.getName()}));
							} else {
								return StatusUtils.warningStatus(NLS.bind(
										ConsumptionUIMessages.MSG_SERVICE_CANNOT_ASSOCIATE,
										new String[] { projectName, ep.getName()}));
							}
						}
					}
				}
			}
		}		
	        	
	    // Defect 203826 - Give extender a way to veto server, project, project type and EAR choices
	    if (validationState == VALIDATE_ALL || validationState == VALIDATE_SERVER_RUNTIME_CHANGES
				|| validationState == VALIDATE_PROJECT_CHANGES) {	
	    	TypeRuntimeServer trs = new TypeRuntimeServer();
	    	trs.setTypeId(typeId);
	    	trs.setRuntimeId(runtimeId);
	    	trs.setServerId(serverId);
	    	IWebServiceRuntimeChecker runtimeChecker = null;
	    	if (isClient) {
	    		String clientRuntimeId = WebServiceRuntimeExtensionUtils2.getClientRuntimeId(trs, projectName, projectTypeId);
	    		runtimeChecker = WebServiceRuntimeExtensionUtils2.getClientRuntimeChecker(clientRuntimeId);
	    		
	    	} else {
	    		String serviceRuntimeId = WebServiceRuntimeExtensionUtils2.getServiceRuntimeId(trs, projectName, projectTypeId);
	    		runtimeChecker = WebServiceRuntimeExtensionUtils2.getServiceRuntimeChecker(serviceRuntimeId);
	    	}
	    	if (runtimeChecker != null) {
	    		return runtimeChecker.checkRuntimeCompatibility(serverId, serverInstanceId, projectName, projectTypeId, earProjectName);
		    }
	    }
	    	    
		return Status.OK_STATUS;

	}
  
  /**
   * Returns IStatus resulting from checking for warnings. Used for validation of page 1 of the
   * Web service/client wizards. 
   * @param validationState one of VALIDATE_NONE, VALIDATE_ALL, VALIDATE_SERVER_RUNTIME_CHANGES, VALIDATE_PROJECT_CHANGES, VALIDATE_SCALE_CHANGES
   * @param scaleSetting one of <BR/>
   * ScenarioContext.WS_TEST<BR/>
   * ScenarioContext.WS_START<BR/>
   * ScenarioContext.WS_INSTALL<BR/>
   * ScenarioContext.WS_DEPLOY<BR/>
   * ScenarioContext.WS_ASSEMBLE<BR/>
   * ScenarioContext.WS_DEVELOP<BR/>
   * ScenarioContext.WS_NONE
   * @param serverId server type id
   * @param serverId server instance id
   * @param isClient boolean <code>true</code> if the method is being called for client side validation, 
   * <code>false</code> for service side validation.
   * @return IStatus with severity IStatus.OK if no errors are present,
   * IStatus with severity IStatus.WARNING otherwise.
   */
  public IStatus checkWarningStatus(int validationState, int scaleSetting, String serverId,
			String serverInstanceId, boolean isClient) {
	  return checkWarningStatus(validationState, scaleSetting, serverId, serverInstanceId, isClient, null, null, null);
  }
  
  /**
   * Returns IStatus resulting from checking for warnings. Used for validation of page 1 of the
   * Web service/client wizards. 
   * @param validationState one of VALIDATE_NONE, VALIDATE_ALL, VALIDATE_SERVER_RUNTIME_CHANGES, VALIDATE_PROJECT_CHANGES, VALIDATE_SCALE_CHANGES
   * @param scaleSetting one of <BR/>
   * ScenarioContext.WS_TEST<BR/>
   * ScenarioContext.WS_START<BR/>
   * ScenarioContext.WS_INSTALL<BR/>
   * ScenarioContext.WS_DEPLOY<BR/>
   * ScenarioContext.WS_ASSEMBLE<BR/>
   * ScenarioContext.WS_DEVELOP<BR/>
   * ScenarioContext.WS_NONE
   * @param serverId server type id
   * @param serverId server instance id
   * @param isClient boolean <code>true</code> if the method is being called for client side validation, 
   * <code>false</code> for service side validation.
   * @return IStatus with severity IStatus.OK if no errors are present,
   * IStatus with severity IStatus.WARNING otherwise.
   */
  public IStatus checkWarningStatus(int validationState, int scaleSetting, String serverId,
			String serverInstanceId, boolean isClient, String projectName, IProject initialProject, String typeId) {
		// Return a warning if there is no server selection and scale setting is
		// anything beyond assemble.
		if (validationState == VALIDATE_ALL || validationState == VALIDATE_SCALE_CHANGES
				|| validationState == VALIDATE_SERVER_RUNTIME_CHANGES) {
			if (serverId == null || serverId.length() == 0) {
				if (scaleSetting < ScenarioContext.WS_ASSEMBLE) {
					if (isClient) {
						return StatusUtils.warningStatus(NLS.bind(
								ConsumptionUIMessages.MSG_WARN_NO_CLIENT_SERVER, new String[0]));
					} else {
						return StatusUtils.warningStatus(NLS.bind(
								ConsumptionUIMessages.MSG_WARN_NO_SERVICE_SERVER, new String[0]));
					}
				}
			} else {
				// Return a warning if the selected server has only stub
				// runtimes
				// and the scale setting is anything beyond deploy.
				IServerType serverType = ServerCore.findServerType(serverId);
				if (serverType != null) {
					// Find a Runtime which is not a stub
					IRuntime nonStubRuntime = ServerUtils.getNonStubRuntime(serverId);
					if ((scaleSetting < ScenarioContext.WS_DEPLOY) && nonStubRuntime == null && serverInstanceId == null) {
						String servertypeLabel = WebServiceRuntimeExtensionUtils2
								.getServerLabelById(serverId);
						return StatusUtils.warningStatus(NLS.bind(
								ConsumptionUIMessages.MSG_WARN_STUB_ONLY,
								new String[] { servertypeLabel }));
					}
				}

			}
		}
		//return a warning in bottom up scenarios where the intial project will be converted into a java utility project
		if (initialProject != null && projectName != null && !projectName.equals(initialProject.getName()) && !JavaEEProjectUtilities.isUtilityProject(initialProject) && FacetUtils.isJavaProject(initialProject) && 
				(WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(typeId) == WebServiceScenario.BOTTOMUP)){ 			
			return StatusUtils.warningStatus(NLS.bind(ConsumptionUIMessages.MSG_WARN_CONVERTED_TO_UTIL, initialProject.getName()));	
		}

		return Status.OK_STATUS;

	}


  /**
   * Returns whether or not the provided server type supports the facet versions on the provided client project
   * @param serverFactoryId server type id
   * @param projectName name of a project that may or may not exist.
   * @param ignoreJavaVersionFacet Whether or not to ignore differences in java facet version
   * @return boolean <code>true</code> if the server type supports the facet versions on the project (facets versions are inferred from a Java project), 
   * <code>false</code> if the server type does not support the facet versions or facet versions on the project cannot be determined.   */
  public boolean doesServerSupportClientProject(String serverFactoryId, String projectName, boolean ignoreJavaVersionFacet) {
    Set facets = FacetUtils.getFacetsForProject(projectName);

    if (ignoreJavaVersionFacet && facets != null) {

      Set facetsCopy = new HashSet();
      facetsCopy.addAll(facets);
      for (Iterator it = facetsCopy.iterator(); it.hasNext();) {
        ProjectFacetVersion pfv = (ProjectFacetVersion) it.next();
        String facetId = pfv.getProjectFacet().getId();
        if (facetId != null && facetId.equalsIgnoreCase(JavaFacetUtils.JAVA_FACET.getId())) {
          it.remove();
        }
      }

      facets = facetsCopy;

    }

    if (facets == null) {
      return true;
    } else {
      return doesServerSupportFacets(serverFactoryId, facets);
    }
  }
  
  /**
   * Returns whether or not the provided server type supports the facet versions on the provided project
   * @param serverFactoryId server type id
   * @param projectName name of a project that may or may not exist.
   * @return boolean <code>true</code> if the server type supports the facet versions on the project (facets versions are inferred from a Java project), 
   * <code>false</code> if the server type does not support the facet versions or facet versions on the project cannot be determined.   */
  public boolean doesServerSupportProject(String serverFactoryId, String projectName)
  {
	Set facets = FacetUtils.getFacetsForProject(projectName);
	if (facets == null)
		return true;
	else 
		return doesServerSupportFacets(serverFactoryId, facets); 
  }

  /**
   * Returns whether or not the provided server type supports the facet versions in the provided set.
   * @param serverFactoryId server type id
   * @param facets set containing elements of type {@link IProjectFacetVersion}
   * @return boolean <code>true</code> if the server type supports the facet versions in the provided set,
   * <code>false</code> otherwise.
   */
  public boolean doesServerSupportFacets(String serverFactoryId, Set facets)
  {
    Set runtimes = FacetUtils.getRuntimes(new Set[]{facets});
    
    Iterator itr = runtimes.iterator();
    IServerType st = ServerCore.findServerType(serverFactoryId);        
    String runtimeTypeId = st.getRuntimeType().getId();
    while (itr.hasNext())
    {
      org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime)itr.next();
      IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
      if (sRuntime != null)
      {
	      IRuntimeType runtimeType = sRuntime.getRuntimeType();
	      if (runtimeType != null && runtimeTypeId.equals(runtimeType.getId()))
	      {
	        //found a match
	        return true;
	      }
      }
    }
    
    return false;    
  }
  
  /**
   * Returns whether or not the provided server type supports at least one version of
   * each of the fixed facets on the provided template
   * @param serverFactoryId server type id
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return boolean <code>true</code> if the server type supports at least one version of
   * each of the fixed facets on the provided template, <code>false</code> otherwise.
   */
  public boolean doesServerSupportTemplate(String serverFactoryId, String templateId)
  {
    IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
    Set templateFacets = template.getFixedProjectFacets();
    Iterator templateFacetsItr = templateFacets.iterator();
    while (templateFacetsItr.hasNext())
    {
      boolean serverSupportsThisOne = false;
      IProjectFacet fixedFacet = (IProjectFacet)templateFacetsItr.next();      
      List versions = null;
      try
      {
        versions = fixedFacet.getSortedVersions(true);
      } catch (VersionFormatException e)
      {
        Set versionSet = fixedFacet.getVersions();
        Iterator itr = versionSet.iterator();
        versions = new ArrayList();
        while (itr.hasNext())
        {
            versions.add(itr.next());
        }            
      } catch (CoreException e)
      {
        Set versionSet = fixedFacet.getVersions();
        Iterator itr = versionSet.iterator();
        versions = new ArrayList();
        while (itr.hasNext())
        {
            versions.add(itr.next());
        }            
      } 
      Iterator versionsItr = versions.iterator();
      while(versionsItr.hasNext())
      {
        IProjectFacetVersion pfv = (IProjectFacetVersion)versionsItr.next();
        Set pfvs = new HashSet();
        pfvs.add(pfv);
        if (doesServerSupportFacets(serverFactoryId, pfvs))
        {
          serverSupportsThisOne = true;
          break;
        }        
      }
      
      if (!serverSupportsThisOne)
      {
        return false;
      }
    }
    
    return true;
  }
  
  /**
   * Returns whether or not the provided project or project type (if the project is null or does not exist)
   * rules out the need for an EAR.
   * @param projectName name of a project
   * @param projectTypeId id of a {@link IFacetedProjectTemplate}. Must be non-empty if the project is null or
   * does not exist. 
   * @return boolean <code>true</code> if the need for an EAR is not ruled out, <code>false</code> otherwise.
   */
	public boolean projectOrProjectTypeNeedsEar(String projectName, String projectTypeId)
	{
		// If the project is a simple Java project or the project type is
		// Java utility return false.
		if (projectName != null && projectName.length() > 0) {
			IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
			if (project.exists()) {
				if (FacetUtils.isJavaProject(project)) {
					return false;
				}
			}
		}

		// Project didn't rule out the need for an EAR
		// so check the proect type
		String templateId = projectTypeId;
		if (templateId != null && templateId.length() > 0) {
			if (FacetUtils.isUtilityTemplate(templateId)) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * Returns whether or not an J2EE 1.3 EAR project can be installed on a server of
	 * the provided server type.
	 * @param serverTypeId server type id
	 * @return boolean <code>true</code> if a J2EE 1.3 EAR project can be installed on a server of
	 * the provided server type, <code>false</code> otherwise. 
	 */
	public boolean serverNeedsEAR(String serverTypeId)
	{
		if (serverTypeId == null) {
			return false;
		}
	    if (serverTypeId.length() > 0) {
			String targetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverTypeId);
			if (targetId != null && targetId.length() > 0) {
				if (!ServerUtils.isTargetValidForEAR(targetId, "13")) // rm j2ee
				{
					return false;
				}
			}
		}

		return true;
	}
	
  public IStatus validateProjectTargetAndJ2EE(String projectName, String compName, String earName, String earCompName, String serverFactoryId, String j2eeLevel)
  {
    IProject p = ProjectUtilities.getProject(projectName);
    IProject earP = null;
    if (earName!=null && !earName.equalsIgnoreCase("")) {
    	earP = ProjectUtilities.getProject(earName);
    }
    IStatus targetStatus = doesProjectTargetMatchServerType(p, serverFactoryId);
    if (earP!=null && targetStatus.getSeverity()==Status.OK)
    {
      //check the EAR      
      IStatus earTargetStatus = doesProjectTargetMatchServerType(earP, serverFactoryId);
      if(earTargetStatus.getSeverity()==Status.ERROR)
      {
        return earTargetStatus;
      }            
    }
    else
    {
      return targetStatus;
    }
    

    //Validate service side J2EE level    
    IStatus j2eeStatus = doesProjectMatchJ2EELevel(p, compName, j2eeLevel);
    if(earP!=null && j2eeStatus.getSeverity()==Status.OK)
    {
      IStatus earJ2EEStatus = doesProjectMatchJ2EELevel(earP, earCompName, j2eeLevel);
      if(earJ2EEStatus.getSeverity()==Status.ERROR)
      {
        return earJ2EEStatus;
      }
    }
    else
    {
      return j2eeStatus;
    }
    
    return Status.OK_STATUS;
  }
  
  //TODO: This method is obselete - should be removed.
  private IStatus doesProjectTargetMatchServerType(IProject p, String serverFactoryId)
  {
    if (p!=null && p.exists())
    {
      IRuntime projectTarget = ServerSelectionUtils.getRuntimeTarget(p.getName());
      if (projectTarget!=null)
      {
        String projectTargetId = projectTarget.getRuntimeType().getId();
        String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverFactoryId);
        if (serverTargetId!=null && serverTargetId.length()>0)
        {
          if(!projectTargetId.equals(serverTargetId))
          { 
            return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_SERVER_TARGET_MISMATCH,new String[]{p.getName()}) );
          }
        }
      }
    }
    return Status.OK_STATUS;        
  }

  //TODO: This method is obselete - should be removed.
  private IStatus doesProjectMatchJ2EELevel(IProject p, String compName, String j2eeLevel)
  {

    try {
		if (p!=null && p.exists())
		{
  	  int projectJ2EELevel = J2EEUtils.getJ2EEVersion(p);
		  if (projectJ2EELevel!=-1)
		  {
		    String projectJ2EELevelString = String.valueOf(projectJ2EELevel);
		    if (j2eeLevel!=null && j2eeLevel.length()>0)
		    {
		      if (!projectJ2EELevelString.equals(j2eeLevel))
		      {
		        return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_J2EE_MISMATCH,new String[]{p.getName()}) );
		      }
		    }
		  }
		}
	} catch(Exception e){
    
  }
    
    return Status.OK_STATUS;        
  }
  
  //TODO: This method is obselete - should be removed.
  public IStatus validateProjectType(String projectName, SelectionListChoices runtime2ClientTypes)
  {
    IStatus status = Status.OK_STATUS;
    IProject p = ProjectUtilities.getProject(projectName);
    if (p==null || !p.exists())
    {
      //Project does not exist which means a new project of the correct type will be created
      //We're done. All is good.
      return status;
    }
      
    //If the project exists, we should see it in the project list for the selected client
    //project type.
    String[] projectNames = runtime2ClientTypes.getChoice().getChoice().getList().getList();
    for (int i=0; i<projectNames.length; i++)
    {
      if (projectName.equals(projectNames[i]))
      {
        //Found the project. All is good.
        return status;
      }
    }
    
    //Didn't find the project. Return an error.
    //Get the label for the client type id
    String clientTypeLabel = getClientTypeLabel(runtime2ClientTypes.getChoice().getList().getSelection());
    String message = NLS.bind(ConsumptionUIMessages.MSG_WRONG_CLIENT_PROJECT_TYPE,new String[]{projectName, clientTypeLabel});
    IStatus eStatus = StatusUtils.errorStatus( message );
    return eStatus;
    
  }
  
  //TODO: This method is obselete - should be removed.
  private String getClientTypeLabel( String type )
  {	  
	  if (type.equals(IModuleConstants.JST_WEB_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_WEB;
	  }
	  else if (type.equals(IModuleConstants.JST_EJB_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_EJB;
	  }
	  else if (type.equals(IModuleConstants.JST_APPCLIENT_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_APP_CLIENT;
	  }
	  else if (type.equals(IModuleConstants.JST_UTILITY_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_CONTAINERLESS;
	  }
	  else
	  {
		  //No known label, return the typeId itself. 
		  return type;
	  }	  	  
  }  
  
  /**
   * Returns whether or not this project is likely hosting any of the services
   * in the provided WSDL as a J2EE Web service.
   * @param p IProject in the workspace
   * @param wsdlURL URL of a WSDL document
   * @param parser
   * @return boolean <code>true</code> if the project contains a webservices.xml 
   * deployment descriptor that points to at least one of the ports in the provided WSDL. 
   * Returns <code>false</code> otherwise.
   */
  public boolean isProjectServiceProject(IProject p, String wsdlURL, WebServicesParser parser)
  {
    if (p==null || wsdlURL==null || wsdlURL.length()==0 || parser==null)
      return false;
    
    IResource wsXML = getWebServcesXML(p);
    if (wsXML==null)
      return false;
    
    
    //Make a list of all the wsdl-port's in webservices.xml
    if (!(wsXML instanceof IFile))
    {
      return false;
    }
     
    Resource res = WorkbenchResourceHelperBase.getResource((IFile)wsXML, true);
    WsddResource wsddRes = (WsddResource)res;    
    WebServices webServices = wsddRes.getWebServices();
    Iterator wsDescs = webServices.getWebServiceDescriptions().iterator();
    ArrayList wsdlPortList = new ArrayList();
    while(wsDescs.hasNext())
    {
      WebServiceDescription wsDesc = (WebServiceDescription)wsDescs.next();
      Iterator pcs = wsDesc.getPortComponents().iterator();
      while(pcs.hasNext())
      {
        PortComponent pc = (PortComponent)pcs.next();
        WSDLPort wsdlPort = pc.getWsdlPort();
        wsdlPortList.add(new QualifiedName(wsdlPort.getNamespaceURI(), wsdlPort.getLocalPart()));
      }
    }
    
    ArrayList portList = getPortNamesFromWsdl(wsdlURL, parser);

    //If any of the QualifiedNames in portList equals any of the QualifiedNames
    //in wsdlPortList, this is the service project. Return true.
    Object[] ports = portList.toArray();
    Object[] wsdlPorts = wsdlPortList.toArray();
    for (int i = 0; i < ports.length; i++)
    {
      QualifiedName portName = (QualifiedName) ports[i];
      for (int j = 0; j < wsdlPorts.length; j++)
      {
        QualifiedName wsdlPortName = (QualifiedName) wsdlPorts[j];
        if (portName.equals(wsdlPortName))
        {
          return true;
        }
      }
    }

    return false;
  }
  
  /**
   * Returns the IResource corresponding to the webservices.xml in the provided project.
   * @param p an IProject in the workspace.
   * @return IResource corresponding to the webservices.xml in the provided project,
   * null if there is no webservices.xml in the project.
   */
  private IResource getWebServcesXML(IProject p)
  {
    //Get the module root.    
    IResource moduleRoot = getModuleRoot(p);
    if (!(moduleRoot instanceof IContainer))
      return null;

    IResource webServicesXML = null;
    if (J2EEProjectUtilities.isDynamicWebProject(p))
    {
      StringBuffer wsPath = new StringBuffer();
      wsPath.append("WEB-INF/");
      wsPath.append(WebServiceConstants.WEBSERVICE_DD_SHORT_NAME);
      webServicesXML = ((IContainer) moduleRoot).findMember(wsPath.toString());
    }
    else
    {
      StringBuffer wsPath = new StringBuffer();
      wsPath.append("META-INF/");
      wsPath.append(WebServiceConstants.WEBSERVICE_DD_SHORT_NAME);
      webServicesXML = ((IContainer) moduleRoot).findMember(wsPath.toString());      
    }
    return webServicesXML;
  }
  
  private IResource getModuleRoot(IProject p)
  {
    IPath modulePath = null;
    try 
    {
      IVirtualComponent vc = ComponentCore.createComponent(p);
      if (vc != null) 
      {
        modulePath = vc.getRootFolder().getWorkspaceRelativePath();
      }
    }
    catch(Exception ex)
    {
      
    } 
    
    IResource res = ResourceUtils.findResource(modulePath);
    return res;
  }
  
  /**
   * Returns a list of WSDL ports in the provided WSDL
   * @param wsdlURL URL of a WSDL document
   * @param parser 
   * @return ArrayList containing elements of type {@link QualifiedName}
   */
  private ArrayList getPortNamesFromWsdl(String wsdlURL, WebServicesParser parser)
  {
  	ArrayList portNameList = new ArrayList();
  	Definition def = parser.getWSDLDefinition(wsdlURL);
    Map services = def.getServices();
    Service service = null;
    for (Iterator it = services.values().iterator(); it.hasNext();)
    {
      service = (Service)it.next();
      String namespace = service.getQName().getNamespaceURI();
      Map ports = service.getPorts();
      for (Iterator it2 = ports.values().iterator(); it2.hasNext();)
      {
      	Port port = (Port)it2.next();
      	portNameList.add(new QualifiedName(namespace, port.getName()));
      }
    }        
  	
  	return portNameList;
  	
  }
  
  private class QualifiedName
  {
    String namespaceURI;

    String localPart;

    /**
     * @param namespaceURI
     * @param localPart
     */
    public QualifiedName(String namespaceURI, String localPart)
    {
      super();
      this.namespaceURI = namespaceURI;
      this.localPart = localPart;
    }

    /**
     * @return Returns the localPart.
     */
    public String getLocalPart()
    {
      return localPart;
    }

    /**
     * @param localPart
     *          The localPart to set.
     */
    public void setLocalPart(String localPart)
    {
      this.localPart = localPart;
    }

    /**
     * @return Returns the namespaceURI.
     */
    public String getNamespaceURI()
    {
      return namespaceURI;
    }

    /**
     * @param namespaceURI
     *          The namespaceURI to set.
     */
    public void setNamespaceURI(String namespaceURI)
    {
      this.namespaceURI = namespaceURI;
    }

    public boolean equals(QualifiedName qn)
    {
      return (qn.getNamespaceURI().equals(namespaceURI) && qn.getLocalPart()
          .equals(localPart));
    }
  }
}
