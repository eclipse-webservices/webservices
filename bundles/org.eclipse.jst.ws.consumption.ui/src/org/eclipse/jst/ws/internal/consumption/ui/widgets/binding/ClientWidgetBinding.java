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

import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.ClientServerDeployableConfigCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.GetMonitorCommand;
import org.eclipse.jst.ws.internal.consumption.common.ScenarioCleanupCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.CheckForServiceProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.EclipseIPath2URLStringTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ServerInstToIServerTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.extension.ClientRootFragment;
import org.eclipse.jst.ws.internal.consumption.ui.extension.PreClientDevelopCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.CheckWSDLValidationCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionWidgetWrapper;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestDelegateCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishDefaultCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.TestDefaultingFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WebServiceClientTestArrivalCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceClientTypeRegistry;
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
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.ws.internal.extensions.AssembleClientFragment;
import org.eclipse.wst.ws.internal.extensions.DeployClientFragment;
import org.eclipse.wst.ws.internal.extensions.DevelopClientFragment;
import org.eclipse.wst.ws.internal.extensions.InstallClientFragment;
import org.eclipse.wst.ws.internal.extensions.RunClientFragment;


public class ClientWidgetBinding implements CommandWidgetBinding
{
  private CanFinishRegistry   canFinishRegistry_;
  private WidgetRegistry      widgetRegistry_;
  private DataMappingRegistry dataMappingRegistry_;
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#create()
   */
  public CommandFragmentFactory create()
  {
    return new CommandFragmentFactory()
           {
             public CommandFragment create()
             {
               return new ClientRootCommandFragment();  
             }
           };
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry)
  {
    canFinishRegistry_ = canFinishRegistry;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    dataMappingRegistry_ = dataRegistry;
    
    // Before ClientWizardWidget
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "TestService", ClientWizardWidget.class );
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "MonitorService", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientWizardWidget.class );
    
    // After ClientWizardWidget
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientTypeRuntimeServer", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "TestService", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "MonitorService", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "ResourceContext", ClientWizardWidgetOutputCommand.class);
            
    // Before ClientRuntimeSelectionWidget
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ClientRuntimeSelectionWidget.class, "J2EEVersion", null);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ClientRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", ClientRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", ClientRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ClientRuntimeSelectionWidget.class,"NeedEAR",null);
    
    // After ClientRuntimeSelectionWidget
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "J2EEVersion", ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", null);
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class); 
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientComponentName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientEarComponentName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
    
    // The ClientRuntimeSelectionWidget can control what client extension comes up.
    dataRegistry.addMapping(ClientRuntimeSelectionWidget.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
    
    // Before WSDLSelectionWidgetWrapper
    dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WebServiceURI", WSDLSelectionWidgetWrapper.class );
    dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "Project", WSDLSelectionWidgetWrapper.class );
    dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "ComponentName", WSDLSelectionWidgetWrapper.class );
    
    // After WSDLSelectionWidgetWrapper
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "WsdlURI", WSDLSelectionOutputCommand.class);
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "WebServicesParser", WSDLSelectionOutputCommand.class);
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "Project", WSDLSelectionOutputCommand.class);
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "ComponentName", WSDLSelectionOutputCommand.class);
        
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
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProjectEAR",FinishDefaultCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProject",FinishDefaultCommand.class);
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
    
    widgetRegistry_ = widgetRegistry;
    
    widgetRegistry.add( "ClientWizardWidget", 
                        msgUtils.getMessage("PAGE_TITLE_WS_PROJECT"),
                        msgUtils.getMessage("PAGE_DESC_WS_PROJECT"),
				        new WidgetContributorFactory()
				        {
				          public WidgetContributor create()
				          {
				            return new ClientWizardWidget();
				          }
				        } );
    
    widgetRegistry.add( "ClientRuntimeSelectionWidget", 
                        msgUtils.getMessage("PAGE_TITLE_WS_CLIENT_RUNTIME_SELECTION"),
                        msgUtils.getMessage("PAGE_DESC_WS_CLIENT_RUNTIME_SELECTION"),
						new WidgetContributorFactory()
                        {
						  public WidgetContributor create()
						  {
						    return new ClientRuntimeSelectionWidget();
						  }
						} );
    
    widgetRegistry.add( "WSDLSelectionWidgetWrapper", 
                        msgUtils.getMessage("PAGE_TITLE_WS_SELECTION"),
                        msgUtils.getMessage("PAGE_DESC_WS_SELECTION"),
		                new WidgetContributorFactory()
                        {
		                  public WidgetContributor create()
		                  {
		                    return new WSDLSelectionWidgetWrapper();
		                  }
		                } );
    
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

  private class InitClientRegistry extends SimpleCommand
  {  
    private WebServiceClientTypeRegistry clientRegistry_ = WebServiceClientTypeRegistry.getInstance();
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.internal.provisional.env.core.common.Environment)
     */
    public Status execute(Environment environment) 
    {
      clientRegistry_.setDataMappingRegistry( dataMappingRegistry_ );
      clientRegistry_.setWidgetRegistry( widgetRegistry_ );
      clientRegistry_.setCanFinishRegistry( canFinishRegistry_ );
      
      return new SimpleStatus( "" );
    }
  }
  
  private class ClientRootCommandFragment extends SequenceFragment
  {
    public ClientRootCommandFragment()
    {
      add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
      
      //add( new SimpleFragment( new CheckForMissingFiles(), "" ) );
      add( new SimpleFragment( new ClientWizardWidgetDefaultingCommand(), "" ) );
      add( new SimpleFragment( new InitClientRegistry(), "" ));
      add( new SimpleFragment( "ClientWizardWidget" ) );
	  //add( new TestCommandFactoryFragment() );
      add( new SimpleFragment( new ClientWizardWidgetOutputCommand(), "" ));
      add( new SimpleFragment( new WSDLSelectionWidgetDefaultingCommand(), ""));
      add( new SimpleFragment( "WSDLSelectionWidgetWrapper" ) );
      add( new SimpleFragment( new WSDLSelectionOutputCommand(), ""));
      add( new SimpleFragment( new CheckWSDLValidationCommand(), ""));
      add( new SimpleFragment( new ClientRuntimeSelectionWidgetDefaultingCommand(), ""));
	  //add( new TestCommandFactoryFragment2() );
      add( new SimpleFragment( "ClientRuntimeSelectionWidget" ) );
      //add( new SimpleFragment( new CheckForServiceProjectCommand(), ""));
      add( new SimpleFragment( new ClientExtensionDefaultingCommand( true ), ""));
      //add(new SimpleFragment(new ClientServerDeployableConfigCommand(false), "")); //Note: added here for client      
      //add( new ClientExtensionFragment() );
	  add( new ClientRootFragment() );
      add( new SimpleFragment( new ClientExtensionOutputCommand(), "" ) );
      add( new SimpleFragment(new GetMonitorCommand(), ""));
      add( new SimpleFragment(new TestDefaultingFragment(),""));
      add( new ClientTestFragment( "ClientTestWidget") ); 
      add(new FinishFragment());
      
      add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      //Map SelectionCommand
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientRuntimeSelectionWidgetDefaultingCommand.class, "InitialInitialSelection", null);
      
      // Map ClientWizardWidgetDefaultingCommand command.
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "TestService", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "MonitorService", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientWizardWidgetOutputCommand.class);
      
      // Map ClientWizardWidgetOutputCommand command.
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ClientTypeRuntimeServer", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "TestService", WSDLSelectionOutputCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "TestService", ClientExtensionDefaultingCommand.class);   
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "TestService", ClientRuntimeSelectionWidgetDefaultingCommand.class);           
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientRuntimeSelectionWidgetDefaultingCommand.class);      
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "MonitorService", GetMonitorCommand.class);
      
      // Map ClientRuntimeSelectionWidgetDefaultingCommand command
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class); 
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", CheckForServiceProjectCommand.class);
      
      // Map WSDLSelectionWidgetDefaultingCommand command.
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", WSDLSelectionWidgetDefaultingCommand.class );
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "GenWSIL", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WsilURI", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WebServiceURI", WSDLSelectionOutputCommand.class, "WsdlURI", new EclipseIPath2URLStringTransformer());
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "Project", WSDLSelectionOutputCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "ComponentName", WSDLSelectionOutputCommand.class);
      
      // WSDLSelectionOutputCommand
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", GetMonitorCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", GetMonitorCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "Project", ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialProject", null);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "ComponentName", ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialComponentName", null);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", CheckForServiceProjectCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", CheckForServiceProjectCommand.class);      
      
      //ServerDeployableConfigurationCommand for client 
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", ClientServerDeployableConfigCommand.class, "SampleProject", new StringToIProjectTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientServerDeployableConfigCommand.class, "ClientTypeRuntimeServer", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ClientServerDeployableConfigCommand.class,"SampleExistingServer", new ServerInstToIServerTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", ClientServerDeployableConfigCommand.class,"SampleServerTypeID", null);
	  
	  // Setup the PreClientDevelopCommand.
			//
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "TestService", PreClientDevelopCommand.class);           
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", PreClientDevelopCommand.class);						
			//
	    dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProject", PreClientDevelopCommand.class, "Module", null );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectType", PreClientDevelopCommand.class, "ModuleType", null);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectEAR", PreClientDevelopCommand.class, "Ear", null );
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", PreClientDevelopCommand.class);
	    dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "WsdlURI", PreClientDevelopCommand.class );
      
			dataRegistry.addMapping( PreClientDevelopCommand.class, "WebService", ClientExtensionOutputCommand.class, "WebServiceClient", null );
			
      // Map ClientExtensionDefaultingCommand command.
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
           
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", WebServiceClientTestArrivalCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", WebServiceClientTestArrivalCommand.class);
            
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestFragment.class );     
      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientNeedEAR", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarProjectName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarComponentName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "WsdlURI", ClientTestDelegateCommand.class);
      
      // Map ClientExtensionOutputCommand command.
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", WebServiceClientTestArrivalCommand.class);      
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", ClientTestDelegateCommand.class);      
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", FinishTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "SetEndpointMethod", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ServerInstanceId", FinishDefaultCommand.class);

	  
      // GetMonitorCommand
      dataRegistry.addMapping(GetMonitorCommand.class, "Endpoints", ClientTestDelegateCommand.class);

      // MAP post server config call      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", ClientExtensionOutputCommand.class, "EarProjectName", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ClientExtensionOutputCommand.class, "ExistingServerId", null);
      dataRegistry.addMapping(ClientServerDeployableConfigCommand.class, "SampleExistingServerInstId", ClientExtensionOutputCommand.class, "ExistingServerId", null);
                  
      
      // Map ClientServerDeployableConfigCommand

      dataRegistry.addMapping(ClientServerDeployableConfigCommand.class, "SampleExistingServerInstId", FinishDefaultCommand.class, "ExistingServerId", null);
      
      // Map WebServiceClientTestArrivalCommand command.
      dataRegistry.addMapping(TestDefaultingFragment.class, "TestFacility",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "TestFacility",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "RunClientTest",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Methods",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR",ClientTestDelegateCommand.class);
      
            
      //Mappings to enable peek-ahead for Page 3 of the client wizard
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", DevelopClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", DevelopClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", DevelopClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", DevelopClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", DevelopClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", DevelopClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", DevelopClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", DevelopClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", AssembleClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", AssembleClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", AssembleClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", AssembleClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", AssembleClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", AssembleClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", AssembleClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", AssembleClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", DeployClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", DeployClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", DeployClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", DeployClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", DeployClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", DeployClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", DeployClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", DeployClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", InstallClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", InstallClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", InstallClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", InstallClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", InstallClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", InstallClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", InstallClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", InstallClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", RunClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", RunClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", RunClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", RunClientFragment.class, "Ear", null );      
    }
  }
}
