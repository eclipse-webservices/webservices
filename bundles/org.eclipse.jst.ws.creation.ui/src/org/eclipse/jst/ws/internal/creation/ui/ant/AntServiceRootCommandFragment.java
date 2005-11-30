/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.ant;

import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.ComputeEndpointCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateMonitorCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.ManageServerStartUpCommand;
import org.eclipse.jst.ws.internal.consumption.common.ScenarioCleanupCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.AntDefaultingFragment;
import org.eclipse.jst.ws.internal.consumption.ui.command.ListOptionsCommand;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.selection.SelectionTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.CheckWSDLValidationCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.PublishToPrivateUDDICommandFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.TestDefaultingFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WSDLTestLaunchCommand;
import org.eclipse.jst.ws.internal.creation.ui.extension.PreServiceDevelopCommand;
import org.eclipse.jst.ws.internal.creation.ui.extension.ServiceRootFragment;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.runtime.ServerRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.test.ServiceTestFragment;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
 
/**
 * 
 * Command fragment for generating web service top down or bottom up using Ant task.
 * Run headless Eclipse or within workspace using Run as Ant Build.  This fragment eliminates any UI specific 
 * commands and data mappings between commands and widgets.  Widget to command mappings are
 * replaced by Ant property file to command mappings which are enabled by a antDataMapping extension 
 * point in the org.eclipse.wst.command.env plugin.
 * 
 * @author joan
 *
 */

public class AntServiceRootCommandFragment extends SequenceFragment
{
	
	private DataMappingRegistry dataMappingRegistry_;
	private PublishToPrivateUDDICommandFragment publishToPrivateUDDICmdFrag;
	
  public AntServiceRootCommandFragment()
  { 

    add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
    add( new SimpleFragment(new ListOptionsCommand(), ""));
    add (new AntDefaultingFragment());
    add( new SimpleFragment( new ServerWizardWidgetDefaultingCommand(), ""));   
    
    add( new SimpleFragment( new ServerWizardWidgetOutputCommand(), "" ));    
    add( new ObjectSelectionFragment() );  
    
    add( new SimpleFragment( new CheckWSDLValidationCommand(), ""));   
    
    add( new SimpleFragment( new ServerRuntimeSelectionWidgetDefaultingCommand(), ""));
    add( new SimpleFragment( new ServerExtensionDefaultingCommand(), ""));
    
    add( new ServiceRootFragment() );  
    
    add( new SimpleFragment( new ServerExtensionOutputCommand(), "" ));
    add(new SimpleFragment(new CreateMonitorCommand(), ""));  
    add(new SimpleFragment(new ComputeEndpointCommand(), ""));
    add( new ServiceTestFragment( "TestService") );
    add( new SimpleFragment(new TestDefaultingFragment(),""));
    add( new SimpleFragment( "Publish") );
    
    publishToPrivateUDDICmdFrag = new PublishToPrivateUDDICommandFragment();  
    add(publishToPrivateUDDICmdFrag);  
    
    //TODO jvh - no class def found....  
    //add(new LaunchFragment());
    
    add(new FinishFragment());
    add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
  }
  
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
	publishToPrivateUDDICmdFrag.registerDataMappings(dataMappingRegistry_);   
    	
	dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialSelection", null);
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialSelection", null);
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "Project", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialProject", null);
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "Project", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialProject", null);
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ComponentName", ServerRuntimeSelectionWidgetDefaultingCommand.class, "InitialComponentName", null);
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ComponentName", ServerRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialComponentName", null);      
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "WebServicesParser", ServerExtensionDefaultingCommand.class );    
	
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);    
    //TODO: jvh - remove?  install/run - dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "StartService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "TestService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "PublishService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ResourceContext", ServerWizardWidgetOutputCommand.class);
    	
    dataRegistry.addMapping(AntDefaultingFragment.class, "StartService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(AntDefaultingFragment.class, "InstallService", PreServiceDevelopCommand.class);
    
    // Map ServerWizardWidgetOutputCommand.
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ServiceTypeRuntimeServer", ObjectSelectionFragment.class, "TypeRuntimeServer", null);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ClientTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ServiceTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "StartService", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "PublishService", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    
    //to the test wizard
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ServiceTestFragment.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", WSDLTestLaunchCommand.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", WSDLTestLaunchCommand.class);
     
    //Map AntDefaultingFragment
    dataRegistry.addMapping(AntDefaultingFragment.class, "ServiceIdsFixed", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    dataRegistry.addMapping(AntDefaultingFragment.class, "ClientIdsFixed", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    
    // Map ServerRuntimeSelectionWidgetDefaultingCommand
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerExtensionDefaultingCommand.class);    
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceProjectName", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceEarProjectName", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentName", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceEarComponentName", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceJ2EEVersion", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceNeedEAR", ServerExtensionDefaultingCommand.class);
    
    
    // Map ServerExtensionDefaultingCommand
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", ServerExtensionFragment.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "TestService", ServiceTestFragment.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "Publish", PublishToPrivateUDDICommandFragment.class);      
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer",WSDLTestLaunchCommand.class);
    	  
	// Setup the PreServiceDevelopCommand.	
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "StartService", PreServiceDevelopCommand.class);
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "TestService", PreServiceDevelopCommand.class);
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "PublishService", PreServiceDevelopCommand.class);      
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "GenerateProxy", PreServiceDevelopCommand.class);      
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "ResourceContext", PreServiceDevelopCommand.class);			
	
	dataRegistry.addMapping( ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentType", PreServiceDevelopCommand.class, "ModuleType", null);
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceRuntimeId", ServerExtensionDefaultingCommand.class);
	dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", PreServiceDevelopCommand.class );
	dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceRuntimeId", PreServiceDevelopCommand.class );
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceJ2EEVersion", PreServiceDevelopCommand.class);
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServerProject", PreServiceDevelopCommand.class, "Module", null );
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServerProjectEAR", PreServiceDevelopCommand.class, "Ear", null );
    
	dataRegistry.addMapping( ObjectSelectionOutputCommand.class, "ObjectSelection", PreServiceDevelopCommand.class, "Selection", new SelectionTransformer() );
	dataRegistry.addMapping( PreServiceDevelopCommand.class, "WebService", ServerExtensionOutputCommand.class );
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "ServiceServerInstanceId", CreateMonitorCommand.class);

    // Map ServerExtensionOutputCommand for ServerStart()
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProjectEAR", ServerExtensionOutputCommand.class, "EarProjectName", null);
	  
    // Map ManageServerStartUpCommand
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "IsWebProjectStartupRequested", ManageServerStartUpCommand.class);
    
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "StartService", ManageServerStartUpCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService", ManageServerStartUpCommand.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ManageServerStartUpCommand.class, "ServiceProject", new StringToIProjectTransformer());
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerServer", ManageServerStartUpCommand.class,"ServiceServerTypeId", null);
    
    //jvh - added - strays from widget bindings etc.
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "TestService",FinishTestFragment.class);
    dataRegistry.addMapping(CreateMonitorCommand.class, "MonitoredPort", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", CreateMonitorCommand.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WsdlURI", ComputeEndpointCommand.class);
    dataRegistry.addMapping(ServerExtensionOutputCommand.class, "WebServicesParser", ComputeEndpointCommand.class);
        
  }

}
