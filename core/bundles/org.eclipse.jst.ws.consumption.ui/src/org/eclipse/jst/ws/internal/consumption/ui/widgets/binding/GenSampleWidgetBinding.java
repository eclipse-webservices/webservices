/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20060608   145529 kathy@ca.ibm.com - Kathy Chan
 * 20060717   146332 makandre@ca.ibm.com - Andrew Mak
 * 20070516   186233 gilberta@ca.ibm.com - Gilbert Andrews
 * 20070815   199626 kathy@ca.ibm.com - Kathy Chan
 * 20080325   184761 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080331   224953 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080415   227237 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080425   221232 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080616   237298 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080619   237797 gilberta@ca.ibm.com - Gilbert Andrews
 * 20090324   247535 mahutch@ca.ibm.com - Mark Hutchinson, Wrong server instance(s) is chosen during JAX-RPC sample generation
 * 20100528   314934 mahutch@ca.ibm.com - Mark Hutchinson, Pressing "Launch" on service test page causes test client not to launch on "Client test" page
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.binding;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.EclipseIPath2URLStringTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestDelegateCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishDefaultCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.TestDefaultingFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.TestWebServiceClient;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WebServiceClientTestArrivalCommand;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.SelectionCommand;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceState;


public class GenSampleWidgetBinding implements CommandWidgetBinding
{  

	
	
/* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#create()
   */
  public CommandFragmentFactory create()
  {
    return new CommandFragmentFactory()
           {
             public CommandFragment create()
             {
               return new GenSampleRootCommandFragment();  
             }
           };
  }

  public SelectionList getLegitTestFacility()
  {
   	ScenarioContext scenarioContext = WebServicePlugin.getInstance().getScenarioContext().copy();
  	String[] testTypes = scenarioContext.getWebServiceTestTypes();	  
  	List newTestTypes = new ArrayList();
		
  	for(int i = 0;i<testTypes.length;i++){
  		WebServiceTestExtension extension =
  			(WebServiceTestExtension) WebServiceTestRegistry.getInstance()
  			.getWebServiceExtensionsByName(testTypes[i]);
		  
  		if(extension.testJavaProxy()){
  			boolean defaultJaxrpc = extension.isDefaultJAXRPC();
  			if(defaultJaxrpc){
  				newTestTypes.add(testTypes[i]); 
    			}
  		}
  	}	
  	String[] tempArray = new String[newTestTypes.size()];
  	return new SelectionList((String[]) newTestTypes.toArray(tempArray), 0);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry)
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {                    
    // Before Client Test widget.
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService",FinishTestFragment.class);
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestWidget.class );
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "RunTestClient", ClientTestWidget.class );
    dataRegistry.addMapping(InitializeProxyCommand.class, "CanRunTestClient", ClientTestWidget.class );
    dataRegistry.addMapping(InitializeProxyCommand.class, "IsWebProject", ClientTestWidget.class );
    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientTestWidget.class );
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject", ClientTestWidget.class );
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR", ClientTestWidget.class );
    dataRegistry.addMapping(InitializeProxyCommand.class, "TestFacility",ClientTestWidget.class);
    dataRegistry.addMapping(InitializeProxyCommand.class, "Popup",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Methods",ClientTestWidget.class);

    // After the client test widget   
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProjectEAR",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProject",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestFacility",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "Folder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "JspFolder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "RunTestClient",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "Methods",ClientTestDelegateCommand.class);    
    dataRegistry.addMapping(ClientTestWidget.class, "TestService",FinishTestFragment.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestID",FinishTestFragment.class);
    dataRegistry.addMapping(ClientTestWidget.class, "IsTestWidget",FinishTestFragment.class);
    dataRegistry.addMapping(ClientTestWidget.class, "ServerInstanceId", FinishDefaultCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "ExistingServerId", FinishDefaultCommand.class);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
           
    widgetRegistry.add( "ClientTestWidget", 
                        ConsumptionUIMessages.PAGE_TITLE_WS_SAMPLE,
                        ConsumptionUIMessages.PAGE_DESC_WS_SAMPLE,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new ClientTestWidget(); 
                          }
                        });
  }
  
  private class InitializeProxyCommand extends AbstractDataModelOperation
  {
    private IStructuredSelection selection_;
	private TypeRuntimeServer typeRuntimeServer_;
	private String            project_;
	private String            module_;
//	private String            earProject_;
	private String            ear_;
	private IWebServiceClient webServiceClient_;
	private String            wsdlURI_;
	private boolean canRunTestClient_;
	private boolean isWebProject = false;
    
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
	{    
		IEnvironment env = getEnvironment();
	    IStatus status = Status.OK_STATUS;	

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
//	    		earProject_ = ear_.substring(0,earIndex);
	    		ear_ = ear_.substring(earIndex+1);
	    	}
	    }    
	  
	    IProject project = ProjectUtilities.getProject(project_);
	    WebServiceClientInfo clientInfo = new WebServiceClientInfo();
	    clientInfo.setImplURL(getProxyBean());
	    //clientInfo.setJ2eeLevel(j2eeLevel_);
	    clientInfo.setServerFactoryId(typeRuntimeServer_.getServerId());
	  	clientInfo.setServerInstanceId(typeRuntimeServer_.getServerInstanceId());
	  	clientInfo.setState(WebServiceState.UNKNOWN_LITERAL);
	  	clientInfo.setWebServiceRuntimeId(typeRuntimeServer_.getRuntimeId());
	  	clientInfo.setWsdlURL(wsdlURI_);
	  	/*
	  	if (clientInfo.getServerInstanceId()==null)
	  	{
	  		CreateServerCommand createServerCommand = new CreateServerCommand();
	  		createServerCommand.setServerFactoryid(clientInfo.getServerFactoryId());
	  		createServerCommand.setEnvironment( env );
	  		IStatus createServerStatus = createServerCommand.execute( null, null );
	  		if (createServerStatus.getSeverity()==Status.OK){
	  			clientInfo.setServerInstanceId(createServerCommand.getServerInstanceId());
	  			clientInfo.setServerCreated(true);
	  			canRunTestClient_ = true;
	  		}
	  		else if (createServerStatus.getSeverity()==Status.ERROR){
	  			if(J2EEUtils.isWebComponent(project))
	  				canRunTestClient_ = false;
	  			else 
	  				canRunTestClient_ = true;
	  		}               
	  	}
	  	else {
	  		canRunTestClient_ = true;
	  	}*/
	  	
	  	canRunTestClient_ = true;
	  	IProject[] earproject = J2EEProjectUtilities.getReferencingEARProjects(project);
	  	boolean earNull = false;
	  	if (earproject.length<1) earNull = true;
	  		
	  	boolean j2eeProject = J2EEProjectUtilities.isJEEProject(project);
	  	if(J2EEUtils.isWebComponent(project))
	  		isWebProject = true;
	  	
	  	if (j2eeProject && earNull)
	  		canRunTestClient_ = false;
	  /*		  	
		if (!earNull && clientInfo.getServerInstanceId() != null){
	  		
	  		AddModuleToServerCommand command = new AddModuleToServerCommand();
	  		command.setServerInstanceId(clientInfo.getServerInstanceId());
	  		command.setProject(project_);
	  		command.setModule(module_);
	  		command.setEnvironment( env );
	  		status = command.execute( monitor, null );
	  		if (status.getSeverity()==Status.ERROR)
	  		{
	  			env.getStatusHandler().reportError(status);
	  		}     
	  	}*/
	  	webServiceClient_ = new TestWebServiceClient(clientInfo);
	  	return status;
  	}
	
	public boolean getPopup(){
		return true;
	}
	
	public String getProxyBean()
    {
		String proxyBean = "";
        try
        {
        	IResource resource    = ResourceUtils.getResourceFromSelection( selection_.getFirstElement() );
        	String    beanPackage = ResourceUtils.getJavaResourcePackageName( resource.getFullPath() );
        
        	if( beanPackage==null )
        		beanPackage = "";
        	else
        		beanPackage = beanPackage + ".";

        	proxyBean = beanPackage + resource.getName();

        	if( proxyBean.toLowerCase().endsWith(".java") || proxyBean.toLowerCase().endsWith(".class")) 
        	{
        		proxyBean = proxyBean.substring(0,proxyBean.lastIndexOf('.'));
        	}
        }
        catch( CoreException exc )
        {        
        }
      
        return proxyBean;
    }
    
	public IWebServiceClient getWebServiceClient()
	{
	  return webServiceClient_;
	}
	
	public boolean getCanRunTestClient(){
		return canRunTestClient_;
	}
	
    public boolean getGenerateProxy()
    {
      return true;
    }
    
    public boolean getCanGenerateProxy()
    {
      return true;
    }
        
    public SelectionList getTestFacility()
    {
    	return getLegitTestFacility();
    }
    
    public boolean getIsWebProject()
    {
    	return isWebProject;
    }
    
	public void setClientTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
	{
      typeRuntimeServer_ = typeRuntimeServer;  
	}
	  
	public void setModule( String module )
	{
	  module_ = module;
	}
	  
	public void setModuleType( String moduleType)
	{
	}
	
	public void setResourceContext( ResourceContext resourceContext )
	{
	}
	
	public void setInitialSelection( IStructuredSelection selection )
    {
      selection_ = selection;  
    }
  }
  
  private class GenSampleRootCommandFragment extends SequenceFragment
  {
    public GenSampleRootCommandFragment()
    {
      add( new SimpleFragment( new ClientWizardWidgetDefaultingCommand(true), "" ) );
      add( new SimpleFragment( new ClientWizardWidgetOutputCommand(), "" ));
      add( new SimpleFragment( new WSDLSelectionWidgetDefaultingCommand(), ""));
      add( new SimpleFragment( new ClientRuntimeSelectionWidgetDefaultingCommand(), ""));
      add( new SimpleFragment( new ClientExtensionDefaultingCommand( true ), ""));
      add( new SimpleFragment( new InitializeProxyCommand(), "" ));
      add( new SimpleFragment( new ClientExtensionOutputCommand(), "" ) );
      add( new SimpleFragment( new TestDefaultingFragment(),""));
      add( new ClientTestFragment( "ClientTestWidget") ); 
      add( new FinishFragment() );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      //Map SelectionCommand
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialSelection", null);
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", InitializeProxyCommand.class );
      
      // Map ClientWizardWidgetDefaultingCommand command.
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "TestService", ClientWizardWidgetOutputCommand.class, "TestService", new ForceTrue() );
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "RunTestClient", ClientWizardWidgetOutputCommand.class);
      
      // Map ClientWizardWidgetOutputCommand command.
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ClientTypeRuntimeServer", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "TestService", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "RunTestClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientExtensionDefaultingCommand.class);
      
      // Map ClientRuntimeSelectionWidgetDefaultingCommand command
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class); 
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
      
      
      // Map WSDLSelectionWidgetDefaultingCommand command.
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", WSDLSelectionWidgetDefaultingCommand.class );
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "GenWSIL", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WsilURI", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WebServiceURI", ClientExtensionDefaultingCommand.class, "WsdlURI", new EclipseIPath2URLStringTransformer());
             
      // Map ClientExtensionDefaultingCommand command.
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
           
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", WebServiceClientTestArrivalCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", WebServiceClientTestArrivalCommand.class);
      
      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestFragment.class );     

      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientNeedEAR", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarProjectName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarProjectName", ClientTestDelegateCommand.class,"ClientEarComponentName",null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ServiceServerInstanceId", FinishDefaultCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "WsdlURI", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "RunTestClient", ClientTestDelegateCommand.class);
      
	  dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", InitializeProxyCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", InitializeProxyCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProject", InitializeProxyCommand.class, "Module", null );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectType", InitializeProxyCommand.class, "ModuleType", null);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectEAR", InitializeProxyCommand.class, "Ear", null );
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", InitializeProxyCommand.class);
	  dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "WsdlURI", InitializeProxyCommand.class );
	  
	  
	  
	  
      // Map InitializeProxyCommand command.
      dataRegistry.addMapping(InitializeProxyCommand.class, "WebServiceClient", ClientExtensionOutputCommand.class);      
      dataRegistry.addMapping(InitializeProxyCommand.class, "GenerateProxy", ClientExtensionDefaultingCommand.class);
      
      // Map ClientExtensionOutputCommand command.
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", WebServiceClientTestArrivalCommand.class);      
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", ClientTestDelegateCommand.class);      
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", TestDefaultingFragment.class);
	  dataRegistry.addMapping(InitializeProxyCommand.class, "CanGenerateProxy", ClientTestFragment.class);
	  dataRegistry.addMapping(InitializeProxyCommand.class, "CanRunTestClient", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", FinishTestFragment.class, "CanGenerateProxy", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "RunTestClient", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ServerInstanceId", FinishDefaultCommand.class);
	  
      // MAP post server config call      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", ClientExtensionOutputCommand.class, "EARProjectName", new StringToIProjectTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ClientExtensionOutputCommand.class, "ExistingServerId", null);
            
      
      // Map WebServiceClientTestArrivalCommand command.
      dataRegistry.addMapping(TestDefaultingFragment.class, "TestFacility",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "TestFacility",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Methods",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR",ClientTestDelegateCommand.class);
      
      
    }
  }
  
  private class ForceTrue implements Transformer
  {
    public Object transform(Object value) 
    {
      return new Boolean(true);
    }
  }
}
