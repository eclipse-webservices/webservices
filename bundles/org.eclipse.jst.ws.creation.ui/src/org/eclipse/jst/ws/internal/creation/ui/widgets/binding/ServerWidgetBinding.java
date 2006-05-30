/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060204 124408   rsinha@ca.ibm.com - Rupam Kuehner
 * 20060204 121605   rsinha@ca.ibm.com - Rupam Kuehner           
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060407   135415 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060410   135562 joan@ca.ibm.com - Joan Haggarty
 * 20060417   136391 joan@ca.ibm.com - Joan Haggarty
 * 20060425   138052 kathy@ca.ibm.com - Kathy Chan
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20060530   144358 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.widgets.binding;

import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.ComputeEndpointCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateMonitorCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.ManageServerStartUpCommand;
import org.eclipse.jst.ws.internal.consumption.common.ScenarioCleanupCommand;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ServerInstToIServerTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.extension.ClientRootFragment;
import org.eclipse.jst.ws.internal.consumption.ui.extension.PreClientDevelopCommand;
import org.eclipse.jst.ws.internal.consumption.ui.selection.SelectionTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.PublishToPrivateUDDICommandFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.PublishWSWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestDelegateCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishDefaultCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.TestDefaultingFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WSDLTestLaunchCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WebServiceClientTestArrivalCommand;
import org.eclipse.jst.ws.internal.creation.ui.extension.PreServiceDevelopCommand;
import org.eclipse.jst.ws.internal.creation.ui.extension.ServiceRootFragment;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidget;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.runtime.ServerRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.LaunchFragment;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.ServiceTestFragment;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.ServiceTestWidget;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.WebServiceTestDefaultingCommand;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
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
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;

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
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    dataMappingRegistry_ = dataRegistry;

    // Before ServerWizardWidget
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceGeneration", ServerWizardWidget.class); 
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "MonitorService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "MonitorService", CreateMonitorCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "MonitorService", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "PublishService", ServerWizardWidget.class);    
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ClientGeneration", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InstallClient", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerWizardWidget.class);    
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ResourceContext", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DevelopService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "AssembleService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DeployService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InstallService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestService", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DevelopClient", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "AssembleClient", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DeployClient", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartClient", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestClient", ServerWizardWidget.class);

    
    // After ServerWizardWidget
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceTypeRuntimeServer", ServerWizardWidgetOutputCommand.class );
    dataRegistry.addMapping(ServerWizardWidget.class, "InstallService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "InstallClient", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "StartService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "TestService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "MonitorService", CreateMonitorCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "MonitorService", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "PublishService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ResourceContext", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ObjectSelection", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "DevelopService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "AssembleService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "DeployService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "DevelopClient", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "AssembleClient", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "DeployClient", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "StartClient", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "TestClient", ServerWizardWidgetOutputCommand.class);
        
    // Before ObjectSelectionWidget    
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "TestService", ClientTestWidget.class );
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "TestService",FinishTestFragment.class);
    
	dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", TestDefaultingFragment.class);
	dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ClientFragment.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceProjectName", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceEarProjectName", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentType", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerWizardWidget.class);    
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceNeedEAR", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ServerWizardWidget.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ServerWizardWidget.class); //jvh
                
    // After ServerWizardWidget  
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceTypeRuntimeServer", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceRuntimeId", ServerExtensionDefaultingCommand.class);    
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientRuntimeId", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceProjectName", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceEarProjectName", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceComponentType", ServerExtensionDefaultingCommand.class);        
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceNeedEAR", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "WebServicesParser", ServerExtensionDefaultingCommand.class );

    // Map fragments that depend on data.   
    dataRegistry.addMapping(ServerWizardWidget.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
    dataRegistry.addMapping(ServerWizardWidget.class, "ServiceTypeRuntimeServer", ServerExtensionFragment.class);
    
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
	dataRegistry.addMapping(ServerExtensionOutputCommand.class, "ServiceServerInstanceId", ServiceTestWidget.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WebServicesParser", ComputeEndpointCommand.class);
    dataRegistry.addMapping(WebServiceTestDefaultingCommand.class, "ServiceTestFacilities", ServiceTestWidget.class);    
    dataRegistry.addMapping(WebServiceTestDefaultingCommand.class, "Environment", ServiceTestWidget.class);
    dataRegistry.addMapping(ServiceTestWidget.class, "LaunchedServiceTestName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServiceTestWidget.class, "LaunchedServiceTestName", FinishTestFragment.class);
    dataRegistry.addMapping(ServiceTestWidget.class, "LaunchedServiceTestName", TestDefaultingFragment.class);
        
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
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "RunTestClient",ClientTestWidget.class);
    
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
    dataRegistry.addMapping(PublishWSWidget.class, "ForceLaunchOutsideIDE", WSExplorerLauncherCommand.class);
    dataRegistry.addMapping(PublishWSWidget.class, "LaunchOptions", WSExplorerLauncherCommand.class);
  
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
   
    widgetRegistry_ = widgetRegistry;
    publishToPrivateUDDICmdFrag.registerWidgetMappings(widgetRegistry_);

    widgetRegistry.add( "ServerWizardWidget", 
                        ConsumptionUIMessages.PAGE_TITLE_WS_PROJECT,
                        ConsumptionUIMessages.PAGE_DESC_WS_PROJECT,
				        new WidgetContributorFactory()
				        {
				          public WidgetContributor create()
				          {
				            return new ServerWizardWidget( true, false );
				          }
				        } );

    widgetRegistry.add( "TestService", 
                        ConsumptionUIMessages.PAGE_TITLE_WSTEST,
                        ConsumptionUIMessages.PAGE_DESC_WSTEST,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new ServiceTestWidget();
                          }
                        } );
    
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
    
    widgetRegistry.add( "Publish", 
                        ConsumptionUIMessages.PAGE_TITLE_WS_PUBLISH,
                        ConsumptionUIMessages.PAGE_DESC_WS_PUBLISH,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new PublishWSWidget(true);
                          }
                        } );
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
      //clientRoot.add(new SimpleFragment(new ClientServerDeployableConfigCommand(), "")); //Note: added here for client     
      //clientRoot.add( new ClientExtensionFragment() );
      clientRoot.add( new ClientRootFragment() );
      clientRoot.add( new SimpleFragment( new ClientExtensionOutputCommand(), "" ));
      
      setTrueFragment( clientRoot );
    }
    
    public void setGenerateProxy( boolean genProxy )
    {
      genProxy_ = genProxy;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      // Map the output of the service scenario to the client scenario.
      dataRegistry.addMapping( ServerExtensionOutputCommand.class, "WebServicesParser", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping( ServerExtensionOutputCommand.class, "WsdlURI", ClientExtensionDefaultingCommand.class );
	  dataRegistry.addMapping( ServerExtensionOutputCommand.class, "ServiceServerFactoryId", ClientExtensionDefaultingCommand.class );
	  dataRegistry.addMapping( ServerExtensionOutputCommand.class, "ServiceServerInstanceId", ClientExtensionDefaultingCommand.class );
	  
      
      // Setup the PreClientDevelopCommand.
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "TestService", PreClientDevelopCommand.class);
      
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "ResourceContext", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "DevelopClient", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "AssembleClient", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "DeployClient", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "InstallClient", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "StartClient", PreClientDevelopCommand.class, "StartService", null);      
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientRuntimeId", PreClientDevelopCommand.class );      
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProject", PreClientDevelopCommand.class, "Module", null );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectType", PreClientDevelopCommand.class, "ModuleType", null);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectEAR", PreClientDevelopCommand.class, "Ear", null );      
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "WsdlURI", PreClientDevelopCommand.class);
      dataRegistry.addMapping( PreClientDevelopCommand.class, "WebService", ClientExtensionOutputCommand.class, "WebServiceClient", null );      
    }
  }
  
  private class ServiceRootCommandFragment extends SequenceFragment
  {
    public ServiceRootCommandFragment()
    {
      add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
      
      add( new SimpleFragment( new ServerWizardWidgetDefaultingCommand(), ""));
      add (new SimpleFragment( new ObjectSelectionOutputCommand(), ""));
      add( new SimpleFragment( new ServerRuntimeSelectionWidgetDefaultingCommand(), ""));  
      add( new SimpleFragment( "ServerWizardWidget" ) );
      add( new SimpleFragment( new ServerWizardWidgetOutputCommand(), "" ));
      add( new SimpleFragment( new ServerExtensionDefaultingCommand(), ""));
      add( new ServiceRootFragment() );
      add( new SimpleFragment( new ServerExtensionOutputCommand(), "" ));
      add(new SimpleFragment(new CreateMonitorCommand(), ""));
      add(new SimpleFragment(new ComputeEndpointCommand(), ""));
      add( new ServiceTestFragment( "TestService") );
      add( new SimpleFragment(new TestDefaultingFragment(),""));
      add( new ClientFragment() );
      add( new ClientTestFragment( "ClientTestWidget") );
      add( new SimpleFragment( "Publish") );
      add(publishToPrivateUDDICmdFrag);
      add(new LaunchFragment());
      add(new FinishFragment());
      
      add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      publishToPrivateUDDICmdFrag.registerDataMappings(dataMappingRegistry_);   

      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ServerWizardWidgetDefaultingCommand.class );            

      // Map ServerWizardWidgetDefaultingCommand to ObjectSelectionOutputCommand
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InitialSelection", ObjectSelectionOutputCommand.class, "ObjectSelection", null);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ObjectSelectionOutputCommand.class, "TypeRuntimeServer", null);
      
      // Map ServerWizardWidgetDefaultingCommand to ServerWizardWidgetOutputCommand
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DevelopService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "AssembleService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DeployService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DevelopClient", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "AssembleClient", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DeployClient", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InstallClient", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartClient", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestClient", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InstallService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "RunTestClient", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "PublishService", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ResourceContext", ServerWizardWidgetOutputCommand.class);      
      
      // Map ServerWizardWidgetOutputCommand.     
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InitialProject", ServerRuntimeSelectionWidgetDefaultingCommand.class); 
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);      
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InstallService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestService", ServerExtensionDefaultingCommand.class);      
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ServerExtensionDefaultingCommand.class);      
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "PublishService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "PublishService", PublishWSWidget.class, "PublishToPublicUDDI", null);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InstallClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ClientExtensionDefaultingCommand.class); // KSC
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "RunTestClient", ClientExtensionDefaultingCommand.class); 
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ClientExtensionDefaultingCommand.class); // KSC
      dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ResourceContext", ClientExtensionDefaultingCommand.class);
      
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "DevelopService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "AssembleService", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "DeployService", ServerExtensionDefaultingCommand.class);
      
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "InstallClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "StartClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "DevelopClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "AssembleClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "DeployClient", ClientExtensionDefaultingCommand.class);

      
      //Map ObjectSelectionOutputCommand to ServerRuntimeSelectionWidgetDefaultingCommand
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialSelection", null);
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialSelection", null);
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "Project", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialProject", null);
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "Project", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialProject", null);      

      //Map ObjectSelectionOutputCommand to ServerWizardWidgetOutputCommand
      dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerWizardWidgetOutputCommand.class);      
      
      //to the test wizard
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ServiceTestFragment.class);
      dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", WSDLTestLaunchCommand.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", WSDLTestLaunchCommand.class);     
      
      // Map ServerRuntimeSelectionWidgetDefaultingCommand      
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceRuntimeId", ServerExtensionDefaultingCommand.class);      
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientRuntimeId", ClientExtensionDefaultingCommand.class);      
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceProject2EARProject", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceProjectName", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceEarProjectName", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentType", ServerExtensionDefaultingCommand.class);      
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentName", ServerExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceEarComponentName", ServerExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", ClientExtensionDefaultingCommand.class);      
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceJ2EEVersion", ServerExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "GenerateProxy", ClientFragment.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "GenerateProxy", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceNeedEAR", ServerExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
      
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "InstallService", ServerExtensionDefaultingCommand.class);  
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "StartService", ServerExtensionDefaultingCommand.class);  
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "TestService", ServerExtensionDefaultingCommand.class);  
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "MonitorService", ServerExtensionDefaultingCommand.class);  
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "PublishService", ServerExtensionDefaultingCommand.class);  
      
      
      // Map ServerExtensionDefaultingCommand
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerExtensionFragment.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "TestService", ServiceTestFragment.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "Publish", PublishToPrivateUDDICommandFragment.class);      
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer",WSDLTestLaunchCommand.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", ClientTestDelegateCommand.class);
	  
	    // Setup the PreServiceDevelopCommand.
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "DevelopService", PreServiceDevelopCommand.class);
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "AssembleService", PreServiceDevelopCommand.class);
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "DeployService", PreServiceDevelopCommand.class);
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "InstallService", PreServiceDevelopCommand.class);
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "StartService", PreServiceDevelopCommand.class);      
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "TestService", PreServiceDevelopCommand.class);    
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "PublishService", PreServiceDevelopCommand.class);
      
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "GenerateProxy", PreServiceDevelopCommand.class);      
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "ResourceContext", PreServiceDevelopCommand.class);
      dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "ObjectSelection", PreServiceDevelopCommand.class, "Selection", new SelectionTransformer() );
		
	  //dataRegistry.addMapping( ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentType", PreServiceDevelopCommand.class, "ModuleType", null);
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", PreServiceDevelopCommand.class );
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceRuntimeId", PreServiceDevelopCommand.class );
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceJ2EEVersion", PreServiceDevelopCommand.class);
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServerProject", PreServiceDevelopCommand.class, "Module", null );
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServerProjectEAR", PreServiceDevelopCommand.class, "Ear", null );
      dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceComponentType", PreServiceDevelopCommand.class, "ModuleType", null );      
			
	  dataRegistry.addMapping( PreServiceDevelopCommand.class, "WebService", ServerExtensionOutputCommand.class );
 
      // Map ClientExtensionDefaultingCommand
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", FinishDefaultCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestFragment.class );
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestWidget.class );
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "RunTestClient", ClientTestWidget.class );
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientNeedEAR", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarProjectName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarComponentName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", WebServiceClientTestArrivalCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", WebServiceClientTestArrivalCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "LaunchedServiceTestName", WebServiceClientTestArrivalCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "RunTestClient",ClientTestDelegateCommand.class); 
      
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", ClientTestDelegateCommand.class);
           
      dataRegistry.addMapping(ServerExtensionOutputCommand.class, "ServiceServerInstanceId", CreateMonitorCommand.class);

      // Map ServerExtensionOutputCommand for ServerStart()
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProjectEAR", ServerExtensionOutputCommand.class, "EarProjectName", null);

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
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ServerInstanceId", FinishDefaultCommand.class);
	  
      // Map ManageServerStartUpCommand
      dataRegistry.addMapping(ServerExtensionOutputCommand.class, "IsWebProjectStartupRequested", ManageServerStartUpCommand.class);
      
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "StartService", ManageServerStartUpCommand.class);
      dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ManageServerStartUpCommand.class);
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ManageServerStartUpCommand.class, "ServiceProject", new StringToIProjectTransformer());
      dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerServer", ManageServerStartUpCommand.class,"ServiceServerTypeId", null);
      
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
    
    
      //Map Finish Command 
      
      
    }
  }
}
