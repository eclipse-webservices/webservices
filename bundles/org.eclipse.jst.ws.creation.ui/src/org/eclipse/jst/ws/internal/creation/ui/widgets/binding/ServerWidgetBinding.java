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
package org.eclipse.jst.ws.internal.creation.ui.widgets.binding;

import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.ClientServerDeployableConfigCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.ComputeEndpointCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateClientProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateMonitorCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServiceProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.ManageServerStartUpCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.ServerDeployableConfigurationCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.CheckForMissingFiles;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ServerInstToIServerTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.PublishToPrivateUDDICommandFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.PublishWSWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.AddModuleDependenciesCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestDelegateCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishDefaultCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishJavaTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.TestDefaultingFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WSDLTestLaunchCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WebServiceClientTestArrivalCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceClientTypeRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidget;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.runtime.ServerRuntimeSelectionWidget;
import org.eclipse.jst.ws.internal.creation.ui.widgets.runtime.ServerRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.LaunchFragment;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.ServiceTestFragment;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.ServiceTestWidget;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.WebServiceTestDefaultingCommand;
import org.eclipse.jst.ws.internal.ui.wse.LaunchWebServicesExplorerCommand;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Condition;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.env.ui.widgets.SelectionCommand;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.env.ui.widgets.WidgetRegistry;

public class ServerWidgetBinding implements CommandWidgetBinding
{
  private CanFinishRegistry   canFinishRegistry_;
  private WidgetRegistry      widgetRegistry_;
  private DataMappingRegistry dataMappingRegistry_;
  private PublishToPrivateUDDICommandFragment publishToPrivateUDDICmdFrag;

  public ServerWidgetBinding()
  {
    publishToPrivateUDDICmdFrag = new PublishToPrivateUDDICommandFragment();
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#create()
   */
  public CommandFragmentFactory create()
  {
    return new CommandFragmentFactory()
           {
             public CommandFragment create()
             {
               return new ServiceRootCommandFragment();  
             }
           };
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry)
  {
    canFinishRegistry_ = canFinishRegistry;
    publishToPrivateUDDICmdFrag.registerCanFinish(canFinishRegistry_);
  }

  /* (non-Javadoc), 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    dataMappingRegistry_ = dataRegistry;
    
    // Before ServerWizardWidget
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "MonitorService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "MonitorService", CreateMonitorCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "MonitorService", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "PublishService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ResourceContext", ServerWizardWidget.class);
    
    // After ServerWizardWidget
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceTypeRuntimeServer", ServerWizardWidgetOutputCommand.class );
    dataRegistry.addMapping(ServerWizardWidget.class, "StartService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "TestService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "MonitorService", CreateMonitorCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "MonitorService", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "PublishService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ResourceContext", ServerWizardWidgetOutputCommand.class);
    
    // Before ObjectSelectionWidget
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ClientTestWidget.class );
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService",FinishTestFragment.class);
    
    // Before ServerRuntimeSelectionWidget
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "GenerateProxy", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceProject2EARProject", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceJ2EEVersion", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceNeedEAR", ServerRuntimeSelectionWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ServerRuntimeSelectionWidget.class);
    
            
    // After ServerRuntimeSelectionWidget    
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ServiceTypeRuntimeServer", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ServiceJ2EEVersion", ServerExtensionDefaultingCommand.class );
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ClientJ2EEVersion", ClientExtensionDefaultingCommand.class );
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ServiceProject2EARProject", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ServiceNeedEAR", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
    

    // Map fragments that depend on data.   
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidget.class, "ServiceTypeRuntimeServer", ServerExtensionFragment.class);
    
    // CreateMonitorCommand
    dataRegistry.addMapping(CreateMonitorCommand.class, "MonitoredPort", ComputeEndpointCommand.class);
    
    // ComputeEndpointCommand
    dataRegistry.addMapping(ComputeEndpointCommand.class, "Endpoint", ServiceTestWidget.class);
    dataRegistry.addMapping(ComputeEndpointCommand.class, "Endpoint", ClientTestDelegateCommand.class);

    //ServiceTestWidget mappings    
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", ServiceTestWidget.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ServiceTestWidget.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", CreateMonitorCommand.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", ServiceTestWidget.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WebServicesParser", ComputeEndpointCommand.class);
    dataRegistry.addMapping(WebServiceTestDefaultingCommand.class, "ServiceTestFacilities", ServiceTestWidget.class);    
    dataRegistry.addMapping(WebServiceTestDefaultingCommand.class, "Environment", ServiceTestWidget.class);
    dataRegistry.addMapping(ServiceTestWidget.class, "LaunchedServiceTestName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServiceTestWidget.class, "LaunchedServiceTestName", FinishTestFragment.class);
    dataRegistry.addMapping(ServiceTestWidget.class, "LaunchedServiceTestName", TestDefaultingFragment.class);
    
    // CreateClientProjectCommand
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", CreateClientProjectCommand.class, "ProxyProject", new StringToIProjectTransformer());
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", CreateClientProjectCommand.class, "ProxyProjectEAR", new StringToIProjectTransformer());
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectType", CreateClientProjectCommand.class, "ClientProjectTypeId", null);  
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", CreateClientProjectCommand.class, "ServerFactoryId", null);
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", CreateClientProjectCommand.class, "J2EEVersion", null);
    dataRegistry.addMapping(ClientServerDeployableConfigCommand.class, "ServiceExistingServerInstId", CreateClientProjectCommand.class, "ExistingServerId", null);
    dataRegistry.addMapping(ClientServerDeployableConfigCommand.class, "AddedProjectToServer", CreateClientProjectCommand.class);
    
    // Before Client Test widget.
    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientTestWidget.class );
    
    // Before ClientTestWidget
        
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject", ClientTestWidget.class );
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR", ClientTestWidget.class );
    dataRegistry.addMapping(TestDefaultingFragment.class, "TestFacility",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "RunClientTest",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Methods",ClientTestWidget.class);
    
    
    // After ClientTestWidget    
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProjectEAR",FinishDefaultCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProject",FinishDefaultCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestFacility",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "Folder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "JspFolder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "RunClientTest",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "Methods",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestService",FinishTestFragment.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestID",FinishTestFragment.class);
    dataRegistry.addMapping(ClientTestWidget.class, "IsTestWidget",FinishTestFragment.class);
    // PublishWSWidget
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "PublishService", PublishWSWidget.class, "PublishToPublicUDDI", null);
    
    // PublishToPrivateUDDICommandFragment
    dataRegistry.addMapping(PublishWSWidget.class, "PublishToPrivateUDDI", PublishToPrivateUDDICommandFragment.class);
    
    // LaunchWebServicesExplorerCommand
    dataRegistry.addMapping(PublishWSWidget.class, "PublishToPrivateUDDI", LaunchFragment.class);
    dataRegistry.addMapping(PublishWSWidget.class, "PublishToPublicUDDI", LaunchFragment.class);
    // TODO Need defaults for these properties.
    dataRegistry.addMapping(PublishWSWidget.class, "ForceLaunchOutsideIDE", LaunchWebServicesExplorerCommand.class);
    dataRegistry.addMapping(PublishWSWidget.class, "LaunchOptions", LaunchWebServicesExplorerCommand.class);
  
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
    String       pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    
    widgetRegistry_ = widgetRegistry;
    publishToPrivateUDDICmdFrag.registerWidgetMappings(widgetRegistry_);

    widgetRegistry.add( "ServerWizardWidget", 
                        msgUtils.getMessage("PAGE_TITLE_WS_PROJECT"),
                        msgUtils.getMessage("PAGE_DESC_WS_PROJECT"),
				        new WidgetContributorFactory()
				        {
				          public WidgetContributor create()
				          {
				            return new ServerWizardWidget( true );
				          }
				        } );

    MessageUtils creationUIMessageUtils = new MessageUtils("org.eclipse.jst.ws.creation.ui.plugin", this);
    widgetRegistry.add( "ObjectSelectionWidget", 
                        creationUIMessageUtils.getMessage("PAGE_TITLE_OBJECT_SELECTION"),
                        creationUIMessageUtils.getMessage("PAGE_DESC_OBJECT_SELECTION"),
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new ObjectSelectionWidget();
                          }
                        });

    widgetRegistry.add( "ServerRuntimeSelectionWidget", 
                        msgUtils.getMessage("PAGE_TITLE_WS_RUNTIME_SELECTION"),
                        msgUtils.getMessage("PAGE_DESC_WS_RUNTIME_SELECTION"),
						new WidgetContributorFactory()
                        {
						  public WidgetContributor create()
						  {
						    return new ServerRuntimeSelectionWidget();
						  }
						} ); 
    
    widgetRegistry.add( "TestService", 
                        msgUtils.getMessage("PAGE_TITLE_WSTEST"),
                        msgUtils.getMessage("PAGE_DESC_WSTEST"),
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new ServiceTestWidget();
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
    
    widgetRegistry.add( "Publish", 
                        msgUtils.getMessage("PAGE_TITLE_WS_PUBLISH"),
                        msgUtils.getMessage("PAGE_DESC_WS_PUBLISH"),
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new PublishWSWidget(true);
                          }
                        } );
  }
    
  private class InitRegistries extends SimpleCommand
  {  
    private WebServiceClientTypeRegistry clientRegistry_ = WebServiceClientTypeRegistry.getInstance();
    private WebServiceServerRuntimeTypeRegistry serverRegistry_ = WebServiceServerRuntimeTypeRegistry.getInstance();
    
    public Status execute(Environment environment) 
    {
      clientRegistry_.setDataMappingRegistry( dataMappingRegistry_ );
      clientRegistry_.setWidgetRegistry( widgetRegistry_ );
      clientRegistry_.setCanFinishRegistry( canFinishRegistry_ );
      
      serverRegistry_.setDataMappingRegistry( dataMappingRegistry_ );
      serverRegistry_.setWidgetRegistry( widgetRegistry_ );
      serverRegistry_.setCanFinishRegistry( canFinishRegistry_ );
      
      return new SimpleStatus( "" );
    }
  }
  
  private class ClientFragment extends BooleanFragment
  {
    boolean genProxy_ = false;
    
    public ClientFragment()
    {      
      setCondition( new Condition() 
                    {
                      public boolean evaluate()
                      {
                        return genProxy_;
                      }
                    });
      
      SequenceFragment clientRoot = new SequenceFragment();
      
      clientRoot.add( new SimpleFragment( new ClientExtensionDefaultingCommand( false ), ""));
      clientRoot.add(new SimpleFragment(new ClientServerDeployableConfigCommand(), "")); //Note: added here for client
      clientRoot.add( new SimpleFragment( new CreateClientProjectCommand(), ""));      
      clientRoot.add( new ClientExtensionFragment() );
      clientRoot.add( new SimpleFragment( new ClientExtensionOutputCommand(), "" ));
      
      setTrueFragment( clientRoot );
    }
    
    public void setGenerateProxy( boolean genProxy )
    {
      genProxy_ = genProxy;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      // Map the output of the service scenario to the client scenario.
      dataRegistry.addMapping( ServerExtensionOutputCommand.class, "WebServicesParser", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping( ServerExtensionOutputCommand.class, "WsdlURI", ClientExtensionDefaultingCommand.class );
    }
  }
  
  private class ServiceRootCommandFragment extends SequenceFragment
  {
    public ServiceRootCommandFragment()
    {
      add( new SimpleFragment( new CheckForMissingFiles(), "" ) );
      add( new SimpleFragment( new InitRegistries(), "" ) );
      add( new SimpleFragment( new ServerWizardWidgetDefaultingCommand(), ""));
      add( new SimpleFragment( "ServerWizardWidget" ) );
      add( new SimpleFragment( new ServerWizardWidgetOutputCommand(), "" ));
      add( new ObjectSelectionFragment() );
      add( new SimpleFragment( new ServerRuntimeSelectionWidgetDefaultingCommand(), ""));
      add( new SimpleFragment( "ServerRuntimeSelectionWidget" ) );
      add( new SimpleFragment( new ServerExtensionDefaultingCommand(), ""));
      add(new SimpleFragment(new ServerDeployableConfigurationCommand(), "")); //Note: added here            
      add( new SimpleFragment( new CreateServiceProjectCommand(), ""));
      add( new ServerExtensionFragment() );
      add( new SimpleFragment( new ServerExtensionOutputCommand(), "" ));
      add(new SimpleFragment(new CreateMonitorCommand(), ""));
      add(new SimpleFragment(new ComputeEndpointCommand(), ""));
      add( new ServiceTestFragment( "TestService") );
      add( new SimpleFragment(new TestDefaultingFragment(),""));
      add( new ClientFragment() );
      add( new SimpleFragment(new ManageServerStartUpCommand(),""));
      add( new ClientTestFragment( "ClientTestWidget") );
      add( new SimpleFragment( "Publish") );
      add(publishToPrivateUDDICmdFrag);
      add(new LaunchFragment());
      add(new FinishFragment());
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      publishToPrivateUDDICmdFrag.registerDataMappings(dataMappingRegistry_);   
      
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ServerWizardWidgetDefaultingCommand.class );

      //dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialSelection", null);
      //dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialSelection", null);

      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialSelection", null);
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialSelection", null);
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "Project", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialProject", null);
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "Project", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialProject", null);
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "WebServicesParser", ServerExtensionDefaultingCommand.class );
      
      // Map ServerWizardWidgetDefaultingCommand
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);    
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "PublishService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ResourceContext", ServerWizardWidgetOutputCommand.class);
      
      // Map ServerWizardWidgetOutputCommand.
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ServiceTypeRuntimeServer", ObjectSelectionFragment.class, "TypeRuntimeServer", null);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ClientTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ServiceTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "StartService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "PublishService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "PublishService", PublishWSWidget.class, "PublishToPublicUDDI", null);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ServerRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ResourceContext", ClientExtensionDefaultingCommand.class);
      
      //to the test wizard
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ServiceTestFragment.class);
      dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", WSDLTestLaunchCommand.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", WSDLTestLaunchCommand.class);
      
      
      
      // Map ServerRuntimeSelectionWidgetDefaultingCommand
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceProject2EARProject", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceJ2EEVersion", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "GenerateProxy", ClientFragment.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "GenerateProxy", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceNeedEAR", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
      
      
      // Map ServerExtensionDefaultingCommand
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerExtensionFragment.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "TestService", ServiceTestFragment.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "Publish", PublishToPrivateUDDICommandFragment.class);      
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer",WSDLTestLaunchCommand.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", ClientTestDelegateCommand.class);
      
      // Map CreateServiceProjectCommand
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", CreateServiceProjectCommand.class, "ProjectName", null);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProjectEAR", CreateServiceProjectCommand.class, "EarProjectName", null);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerServer", CreateServiceProjectCommand.class,"ServerFactoryId",null);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceJ2EEVersion", CreateServiceProjectCommand.class, "J2EEVersion", null);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class,"ServiceNeedEAR",CreateServiceProjectCommand.class,"NeedEAR",null);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class,"IsServiceProjectEJB",CreateServiceProjectCommand.class);
      
 
      // Map ClientExtensionDefaultingCommand
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", WebServiceClientTestArrivalCommand.class, "ClientIds", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", FinishDefaultCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestFragment.class );
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestWidget.class );
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", WebServiceClientTestArrivalCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "LaunchedServiceTestName", WebServiceClientTestArrivalCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", AddModuleDependenciesCommand.class );
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", WebServiceClientTestArrivalCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", WebServiceClientTestArrivalCommand.class, "J2eeVersion", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "Runtime2ClientTypes", WebServiceClientTestArrivalCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", FinishJavaTestFragment.class);
      
      //ServerDeployableConfigurationCommand
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "StartService", ServerDeployableConfigurationCommand.class );
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ServerDeployableConfigurationCommand.class, "ServiceProject", new StringToIProjectTransformer());
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceServerInstanceId", ServerDeployableConfigurationCommand.class,"ServiceExistingServer", new ServerInstToIServerTransformer());
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerServer", ServerDeployableConfigurationCommand.class,"ServiceServerTypeID", null);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerDeployableConfigurationCommand.class, "ClientTypeRuntimeServer", null );
      dataRegistry.addMapping(ServerDeployableConfigurationCommand.class, "ServiceExistingServerInstId", ServerExtensionDefaultingCommand.class,"ServiceExistingServerInstanceId",null);
      dataRegistry.addMapping(ServerDeployableConfigurationCommand.class, "SampleExistingServerInstId", ClientExtensionDefaultingCommand.class,"ClientExistingServerInstanceId", null);
      dataRegistry.addMapping(ServerDeployableConfigurationCommand.class, "ServiceExistingServerInstId", CreateServiceProjectCommand.class,"ExistingServerId", null);
      dataRegistry.addMapping(ServerDeployableConfigurationCommand.class, "AddedProjectToServer", CreateServiceProjectCommand.class);      
      dataRegistry.addMapping(ServerDeployableConfigurationCommand.class, "ServiceExistingServerInstId", CreateMonitorCommand.class, "ServiceServerInstanceId", null);
      dataRegistry.addMapping(ClientServerDeployableConfigCommand.class, "ServiceExistingServerInstId", FinishDefaultCommand.class, "ExistingServerId", null);

      // Map ServerExtensionOutputCommand for ServerStart()
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProjectEAR", ServerExtensionOutputCommand.class, "EarProjectName", null);
      dataRegistry.addMapping(ServerDeployableConfigurationCommand.class, "ServiceExistingServerInstId", ServerExtensionOutputCommand.class,"ExistingServerId", null);
          
      // Map CreateClientProjectCommand
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", CreateClientProjectCommand.class, "ProxyProject", new StringToIProjectTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", CreateClientProjectCommand.class, "ProxyProjectEAR", new StringToIProjectTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", CreateClientProjectCommand.class, "ExistingServer", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", CreateClientProjectCommand.class, "ServerFactoryId", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", CreateClientProjectCommand.class, "J2EEVersion", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientNeedEAR", CreateClientProjectCommand.class, "NeedEAR", null);
        
      //ServerDeployableConfigurationCommand for client side
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", ClientServerDeployableConfigCommand.class, "SampleProject", new StringToIProjectTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientServerDeployableConfigCommand.class,"ClientTypeRuntimeServer", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ClientServerDeployableConfigCommand.class,"SampleExistingServer", new ServerInstToIServerTransformer());

      // MAP post server config call
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", ClientExtensionOutputCommand.class, "EarProjectName", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ClientExtensionOutputCommand.class, "ExistingServerId", null);
      
      // Map ClientExtensionOutputCommand
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", FinishTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", WebServiceClientTestArrivalCommand.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "SetEndpointMethod", ClientTestDelegateCommand.class);
      
      // Map ManageServerStartUpCommand
      dataRegistry.addMapping(ServerExtensionOutputCommand.class, "IsWebProjectStartupRequested", ManageServerStartUpCommand.class);
      
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "StartService", ManageServerStartUpCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ManageServerStartUpCommand.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ManageServerStartUpCommand.class, "ServiceProject", new StringToIProjectTransformer());
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerServer", ManageServerStartUpCommand.class,"ServiceServerTypeId", null);
      dataRegistry.addMapping(ServerDeployableConfigurationCommand.class, "ServiceExistingServerInstId", ManageServerStartUpCommand.class,"ServiceExistingServer", new ServerInstToIServerTransformer());
      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", ManageServerStartUpCommand.class, "SampleProject", new StringToIProjectTransformer());
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ManageServerStartUpCommand.class,"SampleExistingServer", new ServerInstToIServerTransformer());      
      
      // Map WebServiceClientTestArrivalCommand
      dataRegistry.addMapping(TestDefaultingFragment.class, "TestFacility",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "RunClientTest",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Methods",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR",ClientTestDelegateCommand.class);    
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject",AddModuleDependenciesCommand.class);
      dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR",AddModuleDependenciesCommand.class);     
    
      //Map Finish Command 
      
      
    }
  }
}
