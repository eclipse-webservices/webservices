/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner     
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060516   126965 kathy@ca.ibm.com - Kathy Chan
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20070123   167487 makandre@ca.ibm.com - Andrew Mak
 * 20070403   173654 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.extension;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.common.HTTPUtility;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.SimpleContext;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;
import org.eclipse.wst.ws.internal.wsrt.WebServiceState;

public class PreClientDevelopCommand extends AbstractDataModelOperation 
{
  
  private TypeRuntimeServer typeRuntimeServer_;
  private String            clientRuntimeId_;
  private IContext          context_;
  private ISelection        selection_;
  private String            project_;
  private String            module_;
  private String            moduleType_;
  private String            earProject_;
  private String            ear_;
  private IWebServiceClient webServiceClient_;
  private String            j2eeLevel_;
  private ResourceContext   resourceContext_;
  private boolean           develop_;
  private boolean           assemble_;
  private boolean           deploy_;
  private boolean			test_;
  private boolean			install_;
  private boolean			run_;
  private String            wsdlURI_;
  private Object            dataObject_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  IStatus status = Status.OK_STATUS;
	  
	  if (develop_) {
		  IEnvironment environment = getEnvironment();

		  // Split up the project and module
		  int index = module_.indexOf("/");
		  if (index!=-1){
			  project_ = module_.substring(0,index);
			  module_ = module_.substring(index+1);
		  }

		  if (ear_!=null && ear_.length()>0)
		  {
			  int earIndex = ear_.indexOf("/");
			  if (earIndex!=-1) {
				  earProject_ = ear_.substring(0,earIndex);
				  ear_ = ear_.substring(earIndex+1);
			  }
		  }    

		  IWebServiceRuntime wsrt = WebServiceRuntimeExtensionUtils2
		  .getClientRuntime(clientRuntimeId_);
		  WebServiceClientInfo wsInfo = new WebServiceClientInfo();

		  wsInfo.setServerFactoryId(typeRuntimeServer_.getServerId());
		  wsInfo.setServerInstanceId(typeRuntimeServer_.getServerInstanceId());
		  wsInfo.setState(WebServiceState.UNKNOWN_LITERAL);
		  wsInfo.setWebServiceRuntimeId(typeRuntimeServer_.getRuntimeId());
		  
		  // check for redirection in the wsdl
		  HTTPUtility httpUtil = new HTTPUtility();		  
		  wsInfo.setWsdlURL(httpUtil.handleRedirect(wsdlURI_));

		  webServiceClient_ = wsrt.getWebServiceClient(wsInfo);
		  WebServiceScenario scenario = WebServiceScenario.CLIENT_LITERAL;
		  context_ = new SimpleContext(develop_, assemble_, deploy_, install_, run_, true, test_,
				  false, scenario, resourceContext_.isOverwriteFilesEnabled(),
				  resourceContext_.isCreateFoldersEnabled(), resourceContext_
				  .isCheckoutFilesEnabled());

		  // Create the client module if needed.
		  IProject project = ProjectUtilities.getProject(project_);
		  RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId_).getRequiredFacetVersions();
		  if (!project.exists())
		  {
			  boolean matches = WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportTemplate(clientRuntimeId_, moduleType_);
			  if (matches)
			  {  
				  CreateFacetedProjectCommand command = new CreateFacetedProjectCommand();
				  command.setProjectName(project_);
				  command.setTemplateId(moduleType_);
				  command.setRequiredFacetVersions(rfv);
				  command.setServerFactoryId(typeRuntimeServer_.getServerId());
				  command.setServerInstanceId(typeRuntimeServer_.getServerInstanceId());
				  status = command.execute( monitor, adaptable );
				  if (status.getSeverity() == Status.ERROR)
				  {
					  environment.getStatusHandler().reportError( status );
					  return status;
				  }        
			  }            
		  } else {
				// add facets required by Web service runtime
		        if (rfv.length != 0) {
		        	status = FacetUtils.addRequiredFacetsToProject(project, rfv, monitor);
		        	if (status.getSeverity() == Status.ERROR)
		        	{
		        		environment.getStatusHandler().reportError( status );
		        		return status;
		        	}      
		        }
		  }        
	  }
    return status;
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
  {
	typeRuntimeServer_ = typeRuntimeServer;  
  }
  
  public void setClientRuntimeId( String id)
  {
    clientRuntimeId_ = id;
  }
  
  public void setClientJ2EEVersion( String j2eeLevel )
  {
	j2eeLevel_ = j2eeLevel;  
  }
  
  public String getJ2eeLevel()
  {
    return j2eeLevel_;  
  }
  
  public IWebServiceClient getWebService()
  {
	return webServiceClient_;  
  }
  
  public IContext getContext()
  {
    return context_;
  }
  
  public void setResourceContext( ResourceContext resourceContext )
  {
    resourceContext_ = resourceContext;	  
  }
  
  public ISelection getSelection()
  {
    return selection_;	  
  }
  
  public void setSelection( ISelection selection )
  {
	selection_ = selection;  
  }

  public String getProject()
  {
    return project_;    
  }
  
  public String getModule()
  {
    return module_;	  
  }
  
  public void setModule( String module )
  {
	module_ = module;
  }
  
  public void setModuleType( String moduleType)
  {
    moduleType_ = moduleType;
  }

  public String getEarProject()
  {
    return earProject_;   
  }
  
  public String getEar()
  {
	return ear_;  
  }
  
  public void setEar( String ear )
  {
	ear_ = ear;  
  }
  
  public void setDevelopClient(boolean developClient) {
	  develop_ = developClient;
  }	

  public void setAssembleClient(boolean assembleClient) {
	  assemble_ = assembleClient;
  }

  public void setDeployClient(boolean deployClient)
  {
      deploy_ = deployClient;
  }
	
  public void setInstallClient(boolean installClient)
  {
	install_ = installClient;
  }	
  
  public void setTestService(boolean testService)
  {
	test_ = testService;
  }		
  
  public void setStartService(boolean startService)
  {
	  run_ = startService;
  }
  
  public void setWsdlURI(String uri)
  {
    wsdlURI_ = uri;
  }
  
  public void setDataObject( Object object )
  {
    dataObject_ = object;	  
  }
  
  public Object getDataObject()
  {
	Object result = null;
	
    if( dataObject_ != null && dataObject_ instanceof IWebService )
	{
	  // The data object has already been set with an IWebService
	  // so we will keep this value.
	  result = dataObject_;
	}
    else
	{
	  result = webServiceClient_;	
	}
	
	return result;
  }
}
