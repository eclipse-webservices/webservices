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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.binding;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;
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
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.SelectionCommand;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceState;


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

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry)
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {                    
    // Before Client Test widget.
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService",FinishTestFragment.class);
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestWidget.class );
    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientTestWidget.class );
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject", ClientTestWidget.class );
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR", ClientTestWidget.class );
    dataRegistry.addMapping(TestDefaultingFragment.class, "TestFacility",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "RunClientTest",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Methods",ClientTestWidget.class);

    // After the client test widget   
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProjectEAR",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProject",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestFacility",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "Folder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "JspFolder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "RunClientTest",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "SampleMethods",ClientTestDelegateCommand.class);
    
    dataRegistry.addMapping(ClientTestWidget.class, "TestService",FinishTestFragment.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestID",FinishTestFragment.class);
    dataRegistry.addMapping(ClientTestWidget.class, "IsTestWidget",FinishTestFragment.class);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
    String       pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
            
    widgetRegistry.add( "ClientTestWidget", 
                        msgUtils.getMessage("PAGE_TITLE_WS_SAMPLE"),
                        msgUtils.getMessage("PAGE_DESC_WS_SAMPLE"),
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
	private String            earProject_;
	private String            ear_;
	private IWebServiceClient webServiceClient_;
	private String            j2eeLevel_;
	private String            wsdlURI_;
    
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
      Environment env = getEnvironment();
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
          earProject_ = ear_.substring(0,earIndex);
          ear_ = ear_.substring(earIndex+1);
        }
      }    
	  
	  WebServiceClientInfo clientInfo = new WebServiceClientInfo();
	  clientInfo.setImplURL(getProxyBean());
	  clientInfo.setJ2eeLevel(j2eeLevel_);
	  clientInfo.setServerFactoryId(typeRuntimeServer_.getServerId());
	  clientInfo.setServerInstanceId(typeRuntimeServer_.getServerInstanceId());
	  clientInfo.setState(WebServiceState.UNKNOWN_LITERAL);
	  clientInfo.setWebServiceRuntimeId(typeRuntimeServer_.getRuntimeId());
	  clientInfo.setWsdlURL(wsdlURI_);
	  if (clientInfo.getServerInstanceId()==null)
	  {
	    CreateServerCommand createServerCommand = new CreateServerCommand();
	    createServerCommand.setServerFactoryid(clientInfo.getServerFactoryId());
      createServerCommand.setEnvironment( env );
	    IStatus createServerStatus = createServerCommand.execute( null, null );
	    if (createServerStatus.getSeverity()==Status.OK){
	      clientInfo.setServerInstanceId(createServerCommand.getServerInstanceId());
	    }
	    else if (createServerStatus.getSeverity()==Status.ERROR){
	      env.getStatusHandler().reportError(  createServerStatus );
	    }               
	    
	  }
	  
	  AddModuleToServerCommand command = new AddModuleToServerCommand();
      command.setServerInstanceId(clientInfo.getServerInstanceId());
      if (earProject_ != null && earProject_.length()>0 && ear_!= null && ear_.length()>0)
      {
        command.setProject(earProject_);
        command.setModule(ear_);
      }
      else
      {
        command.setProject(project_);
        command.setModule(module_);       
      }

      command.setEnvironment( env );
      status = command.execute( monitor, null );
      if (status.getSeverity()==Status.ERROR)
      {
        env.getStatusHandler().reportError(status);
      }     

	  webServiceClient_ = new TestWebServiceClient(clientInfo);
	  return status;
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
	
    public boolean getGenerateProxy()
    {
      return true;
    }
    
	public void setClientTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
	{
      typeRuntimeServer_ = typeRuntimeServer;  
	}
	  
	public void setClientJ2EEVersion( String j2eeLevel )
	{
	  j2eeLevel_ = j2eeLevel;  
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
      add( new SimpleFragment( new ClientWizardWidgetDefaultingCommand(), "" ) );
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
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
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
      
      // Map ClientWizardWidgetOutputCommand command.
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ClientTypeRuntimeServer", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "TestService", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientExtensionDefaultingCommand.class);
      
      // Map ClientRuntimeSelectionWidgetDefaultingCommand command
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class); 
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", ClientExtensionDefaultingCommand.class);   
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ClientExtensionDefaultingCommand.class);
      
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
      
	  dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", InitializeProxyCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", InitializeProxyCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProject", InitializeProxyCommand.class, "Module", null );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectType", InitializeProxyCommand.class, "ModuleType", null);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectEAR", InitializeProxyCommand.class, "Ear", null );
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", InitializeProxyCommand.class);
	  dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "WsdlURI", InitializeProxyCommand.class );
	  
	  
	  
	  
      // Map InitializeProxyCommand command.
      dataRegistry.addMapping(InitializeProxyCommand.class, "WebServiceClient", ClientExtensionOutputCommand.class);      
      dataRegistry.addMapping(InitializeProxyCommand.class, "GenerateProxy", ClientExtensionOutputCommand.class);
      
      // Map ClientExtensionOutputCommand command.
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", WebServiceClientTestArrivalCommand.class);      
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", ClientTestDelegateCommand.class);      
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", FinishTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ServerInstanceId", FinishDefaultCommand.class);
	  
      // MAP post server config call      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", ClientExtensionOutputCommand.class, "EARProjectName", new StringToIProjectTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ClientExtensionOutputCommand.class, "ExistingServerId", null);
            
      
      // Map WebServiceClientTestArrivalCommand command.
      dataRegistry.addMapping(TestDefaultingFragment.class, "TestFacility",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "TestFacility",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "RunClientTest",ClientTestDelegateCommand.class);
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
