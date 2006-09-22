/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060530   144358 kathy@ca.ibm.com - Kathy Chan
 * 20060530   144350 kathy@ca.ibm.com - Kathy Chan
 * 20060823   154938 pmoogk@ca.ibm.com - Peter Moogk
 * 20060825   155114 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.ant;

import org.eclipse.jst.ws.internal.consumption.ui.command.AntDefaultingOperation;
import org.eclipse.jst.ws.internal.consumption.ui.command.ListOptionsCommand;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.common.ScenarioCleanupCommand;
import org.eclipse.jst.ws.internal.consumption.ui.selection.SelectionTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.jst.ws.internal.creation.ui.extension.PreServiceDevelopCommand;
import org.eclipse.jst.ws.internal.creation.ui.extension.ServiceRootFragment;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.runtime.ServerRuntimeSelectionWidgetDefaultingCommand;
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
	
  public AntServiceRootCommandFragment()
  { 
    add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
    add( new SimpleFragment(new ListOptionsCommand(), ""));
    add (new SimpleFragment(new AntDefaultingOperation(), ""));    
    add( new SimpleFragment( new ServerWizardWidgetDefaultingCommand(), ""));
    add( new SimpleFragment( new ServerWizardWidgetOutputCommand(), "" ));    
    add( new ObjectSelectionFragment() );
    add( new SimpleFragment( new ServerRuntimeSelectionWidgetDefaultingCommand(), ""));
    add( new SimpleFragment( new ServerExtensionDefaultingCommand(), ""));   
    add( new ServiceRootFragment() );
    add( new SimpleFragment( new ServerExtensionOutputCommand(), "" ));
    add(new FinishFragment());
    add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
  }
  
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  { 	
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
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "GenerateProxy", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "ResourceContext", ServerWizardWidgetOutputCommand.class);
    
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DevelopService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "AssembleService", ServerWizardWidgetOutputCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "DeployService", ServerWizardWidgetOutputCommand.class);
        	
    // Map ServerWizardWidgetOutputCommand.
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ServiceTypeRuntimeServer", ObjectSelectionFragment.class, "TypeRuntimeServer", null);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ClientTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "ServiceTypeRuntimeServer", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "StartService", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "GenerateProxy", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "DevelopService", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "AssembleService", ServerExtensionDefaultingCommand.class);
    dataRegistry.addMapping(ServerWizardWidgetOutputCommand.class, "DeployService", ServerExtensionDefaultingCommand.class);
        
    //Map AntDefaultingFragment
    dataRegistry.addMapping(AntDefaultingOperation.class, "ServiceIdsFixed", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    dataRegistry.addMapping(AntDefaultingOperation.class, "ClientIdsFixed", ServerRuntimeSelectionWidgetDefaultingCommand.class);
    dataRegistry.addMapping(AntDefaultingOperation.class, "StartService", PreServiceDevelopCommand.class);
    dataRegistry.addMapping(AntDefaultingOperation.class, "InstallService", PreServiceDevelopCommand.class);
    dataRegistry.addMapping(AntDefaultingOperation.class, "DeployService", PreServiceDevelopCommand.class);
    
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
    	  
	// Setup the PreServiceDevelopCommand.	
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "StartService", PreServiceDevelopCommand.class);
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "GenerateProxy", PreServiceDevelopCommand.class);      
    dataRegistry.addMapping( ServerWizardWidgetOutputCommand.class, "ResourceContext", PreServiceDevelopCommand.class);			
	
	dataRegistry.addMapping( ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentType", PreServiceDevelopCommand.class, "ModuleType", null);
	dataRegistry.addMapping( ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceComponentType", ServerExtensionDefaultingCommand.class );
    dataRegistry.addMapping(ServerRuntimeSelectionWidgetDefaultingCommand.class, "ServiceRuntimeId", ServerExtensionDefaultingCommand.class);
	dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", PreServiceDevelopCommand.class );
	dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceRuntimeId", PreServiceDevelopCommand.class );
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServiceJ2EEVersion", PreServiceDevelopCommand.class);
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServerProject", PreServiceDevelopCommand.class, "Module", null );
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "ServerProjectEAR", PreServiceDevelopCommand.class, "Ear", null );
    
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "DevelopService", PreServiceDevelopCommand.class);
    dataRegistry.addMapping( ServerExtensionDefaultingCommand.class, "AssembleService", PreServiceDevelopCommand.class);
    
    
    
	dataRegistry.addMapping( ObjectSelectionOutputCommand.class, "ObjectSelection", PreServiceDevelopCommand.class, "Selection", new SelectionTransformer() );
	dataRegistry.addMapping( PreServiceDevelopCommand.class, "WebService", ServerExtensionOutputCommand.class );

    // Map ServerExtensionOutputCommand for ServerStart()
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProjectEAR", ServerExtensionOutputCommand.class, "EarProjectName", null);
    
  }

}
