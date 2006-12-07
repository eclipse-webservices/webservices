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
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20060530   144358 kathy@ca.ibm.com - Kathy Chan
 * 20060530   144350 kathy@ca.ibm.com - Kathy Chan
 * 20060825   155114 pmoogk@ca.ibm.com - Peter Moogk
 * 20061011   159283 makandre@ca.ibm.com - Andrew Mak, project not associated to EAR when using ant on command-line
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.ant;

import org.eclipse.jst.ws.internal.consumption.ui.command.AntDefaultingOperation;
import org.eclipse.jst.ws.internal.consumption.ui.command.AntRestoringCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.CheckForServiceProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.ListOptionsCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.EclipseIPath2URLStringTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.FinishFragment;
import org.eclipse.jst.ws.internal.consumption.ui.common.ScenarioCleanupCommand;
import org.eclipse.jst.ws.internal.consumption.ui.extension.ClientRootFragment;
import org.eclipse.jst.ws.internal.consumption.ui.extension.PreClientDevelopCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestDelegateCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishDefaultCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.ws.internal.extensions.AssembleClientFragment;
import org.eclipse.wst.ws.internal.extensions.DeployClientFragment;
import org.eclipse.wst.ws.internal.extensions.DevelopClientFragment;

/**
 * 
 * Command fragment for generating a web service client using Ant task.
 * Run headless Eclipse or within workspace using Run as Ant Build.  This fragment eliminates any UI specific 
 * commands and data mappings between commands and widgets.  Widget to command mappings are
 * replaced by Ant property file to command mappings which are enabled by a antDataMapping extension 
 * point in the org.eclipse.wst.command.env plugin.
 * 
 * @author joan
 *
 */

public class AntClientRootCommandFragment extends SequenceFragment{
	
  public AntClientRootCommandFragment()
  {	 
	    
  add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
  
  add( new SimpleFragment(new ListOptionsCommand(), ""));
  add (new SimpleFragment(new AntDefaultingOperation(), ""));
  
  add( new SimpleFragment( new ClientWizardWidgetDefaultingCommand(true), "" ) );
  add( new SimpleFragment( new ClientWizardWidgetOutputCommand(), "" ));
  add( new SimpleFragment( new WSDLSelectionWidgetDefaultingCommand(), ""));
  
  add( new SimpleFragment( new WSDLSelectionOutputCommand(), ""));
  add( new SimpleFragment( new ClientRuntimeSelectionWidgetDefaultingCommand(), ""));
  
  add( new SimpleFragment( new ClientExtensionDefaultingCommand( true ), ""));
  add( new ClientRootFragment() );
  add( new SimpleFragment( new ClientExtensionOutputCommand(), "" ) );
 
  add(new FinishFragment());
  add( new SimpleFragment( new AntRestoringCommand(), "" ));
  add( new SimpleFragment( new ScenarioCleanupCommand(), "" ));
 
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {     
      // Map ClientWizardWidgetDefaultingCommand command.
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "TestService", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientWizardWidgetOutputCommand.class);
      
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "DevelopClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "AssembleClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "DeployClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "StartClient", ClientWizardWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetDefaultingCommand.class, "ResourceContext", ClientRuntimeSelectionWidgetDefaultingCommand.class);

      
      // Map ClientWizardWidgetOutputCommand command.
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ClientTypeRuntimeServer", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "TestService", WSDLSelectionOutputCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "TestService", ClientExtensionDefaultingCommand.class);   
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "TestService", ClientRuntimeSelectionWidgetDefaultingCommand.class);           
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientRuntimeSelectionWidgetDefaultingCommand.class);      
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "ResourceContext", ClientExtensionDefaultingCommand.class);
      
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "InstallClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "StartClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "DevelopClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "AssembleClient", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientWizardWidgetOutputCommand.class, "DeployClient", ClientExtensionDefaultingCommand.class);
      
      
      // Map ClientRuntimeSelectionWidgetDefaultingCommand command
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientRuntimeId", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarProjectName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientEarComponentName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentName", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientComponentType", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientJ2EEVersion", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientNeedEAR", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(ClientRuntimeSelectionWidgetDefaultingCommand.class, "Runtime2ClientTypes", CheckForServiceProjectCommand.class);
      
//    Map AntDefaultingOperation      
      dataRegistry.addMapping(AntDefaultingOperation.class, "ClientIdsFixed", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(AntDefaultingOperation.class, "StartService", PreClientDevelopCommand.class);
      dataRegistry.addMapping(AntDefaultingOperation.class, "InstallClient", PreClientDevelopCommand.class);        
      dataRegistry.addMapping(AntDefaultingOperation.class, "DeployClient", PreClientDevelopCommand.class);
          
      // Map WSDLSelectionWidgetDefaultingCommand command.
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "GenWSIL", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WsilURI", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "WebServiceURI", WSDLSelectionOutputCommand.class, "WsdlURI", new EclipseIPath2URLStringTransformer());
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "Project", WSDLSelectionOutputCommand.class);
      dataRegistry.addMapping(WSDLSelectionWidgetDefaultingCommand.class, "ComponentName", WSDLSelectionOutputCommand.class);
      
      // WSDLSelectionOutputCommand
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", ClientExtensionDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "Project", ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialProject", null);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "ComponentName", ClientRuntimeSelectionWidgetDefaultingCommand.class, "ClientInitialComponentName", null);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", ClientRuntimeSelectionWidgetDefaultingCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WsdlURI", CheckForServiceProjectCommand.class);
      dataRegistry.addMapping(WSDLSelectionOutputCommand.class, "WebServicesParser", CheckForServiceProjectCommand.class);      
	  
      // Setup the PreClientDevelopCommand.  
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientRuntimeId", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "TestService", PreClientDevelopCommand.class);           
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientJ2EEVersion", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProject", PreClientDevelopCommand.class, "Module", null );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectType", PreClientDevelopCommand.class, "ModuleType", null);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientProjectEAR", PreClientDevelopCommand.class, "Ear", null );
      dataRegistry.addMapping( ClientWizardWidgetOutputCommand.class, "ResourceContext", PreClientDevelopCommand.class);
	  dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "WsdlURI", PreClientDevelopCommand.class );
	  
	  dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "DevelopClient", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "AssembleClient", PreClientDevelopCommand.class);
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "DeployClient", PreClientDevelopCommand.class );
      dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "StartClient", PreClientDevelopCommand.class, "StartService", null);      
	  
      dataRegistry.addMapping( PreClientDevelopCommand.class, "WebService", ClientExtensionOutputCommand.class, "WebServiceClient", null );
			
      // Map ClientExtensionDefaultingCommand command.
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientExtensionFragment.class);          
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", ClientTestFragment.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientNeedEAR", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarProjectName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientEarComponentName", ClientTestDelegateCommand.class);
	  dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject",ClientTestDelegateCommand.class);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", ClientTestDelegateCommand.class);      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "WsdlURI", ClientTestDelegateCommand.class);
      
      // Map ClientExtensionOutputCommand command.
	  dataRegistry.addMapping(ClientExtensionOutputCommand.class, "ServerInstanceId", FinishDefaultCommand.class);

      // MAP post server config call      
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", ClientExtensionOutputCommand.class, "EarProjectName", null);
      dataRegistry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServerInstance", ClientExtensionOutputCommand.class, "ExistingServerId", null);     
            
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
          
      dataRegistry.addMapping(AntDefaultingOperation.class, "RendererValidation", AntRestoringCommand.class);          
   }        
}
