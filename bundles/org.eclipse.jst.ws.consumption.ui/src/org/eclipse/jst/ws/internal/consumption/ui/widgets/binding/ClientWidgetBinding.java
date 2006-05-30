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
 * 20060204 124408    rsinha@ca.ibm.com - Rupam Kuehner          
 * 20060204 121605    rsinha@ca.ibm.com - Rupam Kuehner
 * 20060221 119111    rsinha@ca.ibm.com - Rupam Kuehner
 * 20060223 129020    rsinha@ca.ibm.com - Rupam Kuehner
 * 20060406 135350    kathy@ca.ibm.com - Kathy Chan
 * 20060407 135415    rsinha@ca.ibm.com - Rupam Kuehner
 * 20060425 138052    kathy@ca.ibm.com - Kathy Chan
 * 20060517 141880    pmoogk@ca.ibm.com - Peter Moogk
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.binding;

import org.eclipse.jst.ws.internal.consumption.command.common.GetMonitorCommand;
import org.eclipse.jst.ws.internal.consumption.common.ScenarioCleanupCommand;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.CheckForServiceProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.EclipseIPath2URLStringTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.extension.ClientRootFragment;
import org.eclipse.jst.ws.internal.consumption.ui.extension.PreClientDevelopCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.CheckWSDLValidationCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidget;
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
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WebServiceClientTestArrivalCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
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
import org.eclipse.wst.ws.internal.extensions.AssembleClientFragment;
import org.eclipse.wst.ws.internal.extensions.DeployClientFragment;
import org.eclipse.wst.ws.internal.extensions.DevelopClientFragment;
import org.eclipse.wst.ws.internal.extensions.InstallClientFragment;
import org.eclipse.wst.ws.internal.extensions.RunClientFragment;


public class ClientWidgetBinding implements CommandWidgetBinding
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
               return new ClientRootCommandFragment();  
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
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  { 
	  //jvh mapping for initialselection????
	  
    // Before ClientWizardWidget
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "InstallClient", ClientWizardWidget.class );    
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "TestService", ClientWizardWidget.class );
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "MonitorService", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientGeneration", ClientWizardWidget.class );
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientWizardWidget.class );
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "DevelopClient", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "AssembleClient", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "DeployClient", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "StartClient", ClientWizardWidget.class);
    
    // After ClientWizardWidget
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientTypeRuntimeServer", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "InstallClient", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "TestService", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "MonitorService", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "ResourceContext", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "DevelopClient", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "AssembleClient", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "DeployClient", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "StartClient", ClientWizardWidgetOutputCommand.class);

    //jvh added..
    dataRegistry.addMapping(ClientWizardWidget.class, "Project", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "WebServicesParser", ClientWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "WsdlURI", ClientWizardWidgetOutputCommand.class);
            
    //jvh - rerouted the defaulting command to the ClientWizardWidget 
    // Before ClientRuntimeSelectionWidget
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ClientWizardWidget.class);    
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ClientWizardWidget.class);
    dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ClientWizardWidget.class);    
    
    //jvh - rerouted from ClientWizardWidget instead of runtimeserverwidget- add getters to ClientWizardWidget for them
    // After ClientRuntimeSelectionWidget
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientRuntimeId", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class); 
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ClientWizardWidget.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
        
    // Before WSDLSelectionWidgetWrapper
    // jvh - rerouted these to ClientWizardWidget  
    dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WebServiceURI", ClientWizardWidget.class );
    dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "Project", ClientWizardWidget.class );
    dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "ComponentName", ClientWizardWidget.class );
    
/*    // After WSDLSelectionWidgetWrapper
    //jvh - reroute these from the dialog to ClientWizardWidget - call setters on the other dialogs...
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "WsdlURI", WSDLSelectionOutputCommand.class);
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "WebServicesParser", WSDLSelectionOutputCommand.class);
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "Project", WSDLSelectionOutputCommand.class);
    dataRegistry.addMapping(WSDLSelectionWidgetWrapper.class, "ComponentName", WSDLSelectionOutputCommand.class);
*/    
	dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy",TestDefaultingFragment.class);
	
    // Before Client Test widget.
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService",FinishTestFragment.class);
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestWidget.class );
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "RunTestClient", FinishTestFragment.class );
    dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "RunTestClient", ClientTestWidget.class );
    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientTestWidget.class );
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProject", ClientTestWidget.class );
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "SampleProjectEAR", ClientTestWidget.class );
    dataRegistry.addMapping(TestDefaultingFragment.class, "TestFacility",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Folder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "JspFolder",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "RunTestClient",ClientTestWidget.class);
    dataRegistry.addMapping(WebServiceClientTestArrivalCommand.class, "Methods",ClientTestWidget.class);

    // After the client test widget   
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProjectEAR",FinishDefaultCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "SampleProject",FinishDefaultCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "TestFacility",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "Folder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "JspFolder",ClientTestDelegateCommand.class);
    dataRegistry.addMapping(ClientTestWidget.class, "RunTestClient",ClientTestDelegateCommand.class);
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
       
    widgetRegistry.add( "ClientWizardWidget", 
    		ConsumptionUIMessages.PAGE_TITLE_WS_PROJECT,
                        ConsumptionUIMessages.PAGE_DESC_WS_PROJECT,
				        new WidgetContributorFactory()
				        {
				          public WidgetContributor create()
				          {
				            return new ClientWizardWidget();
				          }
				        } );
    
    //jvh
    /*widgetRegistry.add( "ClientRuntimeSelectionWidget", 
                        ConsumptionUIMessages.PAGE_TITLE_WS_CLIENT_RUNTIME_SELECTION,
                        ConsumptionUIMessages.PAGE_DESC_WS_CLIENT_RUNTIME_SELECTION,
						new WidgetContributorFactory()
                        {
						  public WidgetContributor create()
						  {
						    return new ClientRuntimeSelectionWidget();
						  }
						} );*/
    
    //jvh
    /*widgetRegistry.add( "WSDLSelectionWidgetWrapper", 
                        ConsumptionUIMessages.PAGE_TITLE_WS_SELECTION,
                        ConsumptionUIMessages.PAGE_DESC_WS_SELECTION,
		                new WidgetContributorFactory()
                        {
		                  public WidgetContributor create()
		                  {
		                    return new WSDLSelectionWidgetWrapper();
		                  }
		                } );*/
    
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
  
  private class ClientRootCommandFragment extends SequenceFragment
  {
    public ClientRootCommandFragment()
    {
      add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
      
      //add( new SimpleFragment( new CheckForMissingFiles(), "" ) );
      add( new SimpleFragment( new ClientWizardWidgetDefaultingCommand(true), "" ) );
      add( new SimpleFragment( new WSDLSelectionWidgetDefaultingCommand(), "")); //jvh moved this up
      add( new SimpleFragment( new ClientRuntimeSelectionWidgetDefaultingCommand(), ""));  //jvh moved this up      
      add( new SimpleFragment( "ClientWizardWidget" ) );
	  //add( new TestCommandFactoryFragment() );
      add( new SimpleFragment( new ClientWizardWidgetOutputCommand(), "" ));

      add( new SimpleFragment( new CheckWSDLValidationCommand(), ""));
      
      //add( new TestCommandFactoryFragment2() );
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
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      //Map SelectionCommand
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientRuntimeSelectionWidgetDefaultingCommand.class, "InitialInitialSelection", null);
      
      // Map ClientWizardWidgetDefaultingCommand command.
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "InstallClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "TestService", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "RunTestClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "MonitorService", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "DevelopClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "AssembleClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "DeployClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "StartClient", ClientWizardWidgetOutputCommand.class);

      
      // Map ClientWizardWidgetOutputCommand command.
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "InstallClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "RunTestClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "TestService", ClientExtensionDefaultingCommand.class);      
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "TestService", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "StartClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "DevelopClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "AssembleClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "DeployClient", ClientExtensionDefaultingCommand.class);

      
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientRuntimeSelectionWidgetDefaultingCommand.class);      
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "MonitorService", GetMonitorCommand.class);
      
      // Map ClientRuntimeSelectionWidgetDefaultingCommand command      
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class); 
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientRuntimeId", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
      //dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", CheckForServiceProjectCommand.class);
      
      // Map WSDLSelectionWidgetDefaultingCommand command.
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", WSDLSelectionWidgetDefaultingCommand.class );
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "Project", ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialProject", null);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WebServiceURI", ClientRuntimeSelectionWidgetDefaultingCommand.class, "WsdlURI", new EclipseIPath2URLStringTransformer());
                  
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "GenWSIL", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WsilURI", ClientExtensionDefaultingCommand.class);
      //jvh - rerouted these to the ClientWizardWidgetOutputCommand 
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WebServiceURI", ClientWizardWidgetOutputCommand.class, "WsdlURI", new EclipseIPath2URLStringTransformer());
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "Project", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "ComponentName", ClientWizardWidgetOutputCommand.class);
      
      // WSDLSelectionOutputCommand
      //jvh - rerouted these from ClientWizardWidgetOutputCommand 
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "WsdlURI", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "WebServicesParser", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "WsdlURI", GetMonitorCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "WebServicesParser", GetMonitorCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "WsdlURI", CheckForServiceProjectCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "WebServicesParser", CheckForServiceProjectCommand.class);

      
      //jvh - add code to widget so that the appropriate sets happen and defaulting occurs again after object selection
      /*dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "Project", ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialProject", null);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", ClientRuntimeSelectionWidgetDefaultingCommand.class);*/
      
	  // Setup the PreClientDevelopCommand.
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "DeployClient", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "InstallClient", PreClientDevelopCommand.class);
      
      //Always start the client if it is installed.      
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "DevelopClient", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "AssembleClient", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "DeployClient", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "StartClient", PreClientDevelopCommand.class, "StartService", null);      

      
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "TestService", PreClientDevelopCommand.class);      
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", PreClientDevelopCommand.class);						
	  dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProject", PreClientDevelopCommand.class, "Module", null );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectType", PreClientDevelopCommand.class, "ModuleType", null);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectEAR", PreClientDevelopCommand.class, "Ear", null );
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", PreClientDevelopCommand.class);
	    dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "WsdlURI", PreClientDevelopCommand.class );
        dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientRuntimeId", PreClientDevelopCommand.class );
      
			dataRegistry.addMapping( PreClientDevelopCommand.class, "WebService", ClientExtensionOutputCommand.class, "WebServiceClient", null );
			
      // Map ClientExtensionDefaultingCommand command.
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);
           
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", WebServiceClientTestArrivalCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", WebServiceClientTestArrivalCommand.class);
            
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "TestService", ClientTestFragment.class );     
      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientNeedEAR", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarProjectName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarProjectName", ClientTestDelegateCommand.class,"ClientEarComponentName", null);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject",ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "RunTestClient",ClientTestDelegateCommand.class); 
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "WsdlURI", ClientTestDelegateCommand.class);
      
      // Map ClientExtensionOutputCommand command.
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", WebServiceClientTestArrivalCommand.class);      
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ProxyBean", ClientTestDelegateCommand.class);      
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "GenerateProxy", FinishTestFragment.class);
      dataRegistry.addMapping(ClientExtensionOutputCommand.class, "SetEndpointMethod", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ServerInstanceId", FinishDefaultCommand.class);

	  
      // GetMonitorCommand
      dataRegistry.addMapping(GetMonitorCommand.class, "Endpoints", ClientTestDelegateCommand.class);

      // MAP post server config call      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", ClientExtensionOutputCommand.class, "EarProjectName", null);
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
      
            
      //Mappings to enable peek-ahead for Page 3 of the client wizard
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", DevelopClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", DevelopClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", DevelopClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", DevelopClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", DevelopClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", DevelopClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", DevelopClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", DevelopClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", AssembleClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", AssembleClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", AssembleClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", AssembleClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", AssembleClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", AssembleClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", AssembleClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", AssembleClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", DeployClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", DeployClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", DeployClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", DeployClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", DeployClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", DeployClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", DeployClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", DeployClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", InstallClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", InstallClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", InstallClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", InstallClientFragment.class );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", InstallClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", InstallClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", InstallClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", InstallClientFragment.class, "Ear", null );
    
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "WebService", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Environment", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Context", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "Selection", RunClientFragment.class );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", RunClientFragment.class, "Project", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", RunClientFragment.class, "Module", null );  
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", RunClientFragment.class, "EarProject", null );
      dataRegistry.addMapping( ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", RunClientFragment.class, "Ear", null );      
    }
  }
}
