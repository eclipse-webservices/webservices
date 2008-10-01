/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
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
 * 20070509   182274 kathy@ca.ibm.com - Kathy Chan
 * 20071212	  200193 gilberta@ca.ibm.com - Gilbert Andrews
 * 20071220   213640 kathy@ca.ibm.com - Kathy Chan
 * 20080326   221364 kathy@ca.ibm.com - Kathy Chan
 * 20080724   241275 pmoogk@ca.ibm.com - Peter Moogk, Validate WSDL before doing major Web service processing.
 * 20081001   243869 ericdp@ca.ibm.com - Eric D. Peters, Web Service tools allowing mixed J2EE levels
 *******************************************************************************/

package org.eclipse.jst.ws.internal.creation.ui.extension;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.common.WSDLParserFactory;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.TimedWSDLSelectionConditionCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.common.HTTPUtility;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IMerger;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.SimpleContext;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;
import org.eclipse.wst.ws.internal.wsrt.WebServiceState;

public class PreServiceDevelopCommand extends AbstractDataModelOperation 
{
  private TypeRuntimeServer typeRuntimeServer_;
  private String            serviceRuntimeId_;
  private IContext          context_;
  private ISelection        selection_;
  private String			project_;
  private String            module_;
  private String			moduleType_;
  private String			earProject_;
  private String            ear_;
  private IProject			initialProject_;
	
  private IWebService       webService_;
  private String            j2eeLevel_;
  private ResourceContext   resourceContext_;
  
  private boolean develop_;
  private boolean assemble_;
  private boolean deploy_;
  private boolean install_;
  private boolean run_;
  private boolean client_;
  private boolean test_;
  private boolean publish_;
  

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


		  IWebServiceRuntime wsrt   = WebServiceRuntimeExtensionUtils2.getServiceRuntime( serviceRuntimeId_ );
		  WebServiceInfo     wsInfo = new WebServiceInfo();

		  wsInfo.setServerFactoryId( typeRuntimeServer_.getServerId() );
		  wsInfo.setServerInstanceId( typeRuntimeServer_.getServerInstanceId());
		  wsInfo.setState( WebServiceState.UNKNOWN_LITERAL );
		  wsInfo.setWebServiceRuntimeId( typeRuntimeServer_.getRuntimeId() );
		  
		  environment.getLog().log(ILog.INFO, "ws_framework", 5107, this, "PreServiceDevelopCommand", "serviceRuntimeId_ = "+ serviceRuntimeId_);
		  ServiceRuntimeDescriptor serviceRuntimeDescriptor =  WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId_);
		  IMerger merger = serviceRuntimeDescriptor.getServiceImplementationType().getMergerClass();
		  wsInfo.setMerger(merger);		  

		  webService_  = wsrt.getWebService( wsInfo );

		  //Set up the IContext
		  WebServiceScenario scenario = null;
		  int scenarioInt = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(typeRuntimeServer_.getTypeId());
		  if (scenarioInt == WebServiceScenario.BOTTOMUP)
		  {
			  scenario = WebServiceScenario.BOTTOMUP_LITERAL;
			  String impl = (String)(selection_.getSelection())[0];
			  wsInfo.setImplURL(impl);
		  }
		  else if (scenarioInt == WebServiceScenario.TOPDOWN)
		  {
			  scenario = WebServiceScenario.TOPDOWN_LITERAL;
			  String wsdlURL = (String)(selection_.getSelection())[0];
			  
			  // check for redirection in the wsdl
			  HTTPUtility httpUtil = new HTTPUtility();
			  String      newWsdlURL  = httpUtil.handleRedirect(wsdlURL);
			  
			  wsInfo.setWsdlURL( newWsdlURL );   
			  
			  // Validate the url before proceeding.
			  TimedWSDLSelectionConditionCommand timedCmd = new TimedWSDLSelectionConditionCommand();
			  WebServicesParser                  parser   = WSDLParserFactory.getWSDLParser();
			  
			  timedCmd.setWebServicesParser( parser );
			  timedCmd.setWebServiceURI( newWsdlURL );
			  timedCmd.execute( null, null );
			  
	      WebServiceEntity entity = parser.getWebServiceEntityByURI( newWsdlURL );
	      
	      if( entity == null || entity.getType() != WebServiceEntity.TYPE_WSDL )
	      {
	        // This isn't a wsdl file
	        status = StatusUtils.errorStatus( ConsumptionUIMessages.PAGE_MSG_SELECTION_MUST_BE_WSDL );
          environment.getStatusHandler().reportError( status );
          return status;
	      }
		  }

		  context_     = new SimpleContext(develop_, assemble_, deploy_, install_, run_, client_, test_, publish_, 
				  scenario, 
				  resourceContext_.isOverwriteFilesEnabled(),
				  resourceContext_.isCreateFoldersEnabled(),
				  resourceContext_.isCheckoutFilesEnabled());

		  // Create the service module if needed.
		  IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(project_);
		  RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId_).getRequiredFacetVersions();
		  if (!project.exists())
		  {
			  boolean matches = WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportTemplate(serviceRuntimeId_, moduleType_);
			  if (matches)
			  {				  
				  CreateFacetedProjectCommand command = new CreateFacetedProjectCommand();
				  command.setProjectName(project_);
				  command.setTemplateId(moduleType_);
				  command.setRequiredFacetVersions(rfv);
				  command.setServerFactoryId(typeRuntimeServer_.getServerId());
				  command.setServerInstanceId(typeRuntimeServer_.getServerInstanceId());
				  command.setEarProjectName(earProject_);
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
	  
	  
		  if(initialProject_ != null && FacetUtils.isJavaProject(initialProject_)){
			  J2EEUtils.addJavaProjectAsUtilityJar(initialProject_, project, monitor);
			  try{
		  		String uri = initialProject_.getName() + ".jar";
		  		J2EEUtils.addJAROrModuleDependency(project, uri);
			  } catch (CoreException ce){
				  String errorMessage = NLS.bind(ConsumptionUIMessages.MSG_ERROR_MODULE_DEPENDENCY, new String[]{project.getName(), initialProject_.getName()});
				  IStatus errorStatus = StatusUtils.errorStatus(errorMessage);
				  environment.getStatusHandler().reportError(errorStatus);
			  } catch (IOException ioe){
				  String errorMessage = NLS.bind(ConsumptionUIMessages.MSG_ERROR_MODULE_DEPENDENCY, new String[]{project.getName(), initialProject_.getName()});
				  IStatus errorStatus = StatusUtils.errorStatus(errorMessage);
				  environment.getStatusHandler().reportError(errorStatus);					
			  }		
		  }
	}
	  return status;

  }
  
  public void setServiceTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
  {
	  typeRuntimeServer_ = typeRuntimeServer;  
  }
  
  public void setServiceRuntimeId(String id)
  {
    serviceRuntimeId_ = id;
  }
	  
  public void setServiceJ2EEVersion( String j2eeLevel )
  {
	j2eeLevel_ = j2eeLevel;  
  }
  
  public String getJ2eeLevel()
  {
	  return j2eeLevel_;  
  }
	
  public IWebService getWebService()
  {
	return webService_;  
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
  
  public void setModuleType(String type)
  {
	  moduleType_ = type;
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
  
  public void setInstallService(boolean installService)
	{
		install_ = installService;
	}

	public void setDevelopService(boolean developService) {
		develop_ = developService;
	}	
	
	public void setAssembleService(boolean assembleService) {
		assemble_ = assembleService;
	}

	public void setDeployService(boolean deployService) {
		deploy_ = deployService;
	}
  
	public void setStartService(boolean startService)
	{
		run_ = startService;
	}
	
	public void setTestService(boolean testService)
	{
		test_ = testService;
	}	
	
  public void setPublishService(boolean publishService)
  {
    publish_ = publishService;
  }
	
  public void setGenerateProxy(boolean genProxy)
  {
    client_ = genProxy;  
  }	
	
  public void setInitialProject(IProject initialProject)
  {
	  initialProject_ = initialProject;  
  }	
    
  public IProject getInitialProject()
  {
	  return initialProject_;  
  }	

}
