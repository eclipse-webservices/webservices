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
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner     
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/

package org.eclipse.jst.ws.internal.creation.ui.extension;

import org.eclipse.jst.ws.internal.consumption.ui.extension.PreClientDevelopCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.DataObjectCommand;
import org.eclipse.wst.ws.internal.extensions.AssembleServiceFragment;
import org.eclipse.wst.ws.internal.extensions.DeployServiceFragment;
import org.eclipse.wst.ws.internal.extensions.DevelopServiceFragment;
import org.eclipse.wst.ws.internal.extensions.InstallServiceFragment;
import org.eclipse.wst.ws.internal.extensions.RunServiceFragment;

public class ServiceRootFragment extends SequenceFragment 
{
  public ServiceRootFragment()
  {
    add( new SimpleFragment( new PreServiceDevelopCommand(), "" ) );
    add( new DevelopServiceFragment() );
    add( new SimpleFragment( new PreServiceAssembleCommand(), "" ) );
    add( new AssembleServiceFragment() );
    add( new SimpleFragment( new PreServiceDeployCommand(), "" ) );
    add( new DeployServiceFragment() );
    add( new SimpleFragment( new PreServiceInstallCommand(), "" ) );
    add( new InstallServiceFragment() );
    add( new SimpleFragment( new PreServiceRunCommand(), 
    	 "org.eclipse.jst.ws.internal.creation.ui.extension.PreServiceRunCommand" ) );
    add( new RunServiceFragment() );
  }

  public void registerDataMappings(DataMappingRegistry registry) 
  {
		//Mappings from framework to extension fragments
  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", DevelopServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Environment", DevelopServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Context", DevelopServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Selection", DevelopServiceFragment.class );  
		registry.addMapping( PreServiceDevelopCommand.class, "Project", DevelopServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Module", DevelopServiceFragment.class );
		registry.addMapping( PreServiceDevelopCommand.class, "EarProject", DevelopServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Ear", DevelopServiceFragment.class );
	
	// Map the PreServiceDevelopCommand into the dataObject and the PreClientDevelopCommand.
	registry.addMapping( PreServiceDevelopCommand.class, "WebService", DataObjectCommand.class, "DataObject", null );
	registry.addMapping( PreServiceDevelopCommand.class, "WebService", PreClientDevelopCommand.class, "DataObject", null );
		
  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", AssembleServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Environment", AssembleServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Context", AssembleServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Selection", AssembleServiceFragment.class );
		registry.addMapping( PreServiceDevelopCommand.class, "Project", AssembleServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Module", AssembleServiceFragment.class );  
		registry.addMapping( PreServiceDevelopCommand.class, "EarProject", AssembleServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Ear", AssembleServiceFragment.class );
	
  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", DeployServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Environment", DeployServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Context", DeployServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Selection", DeployServiceFragment.class );
		registry.addMapping( PreServiceDevelopCommand.class, "Project", DeployServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Module", DeployServiceFragment.class );  
		registry.addMapping( PreServiceDevelopCommand.class, "EarProject", DeployServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Ear", DeployServiceFragment.class );
	
  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", InstallServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Environment", InstallServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Context", InstallServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Selection", InstallServiceFragment.class );
		registry.addMapping( PreServiceDevelopCommand.class, "Project", InstallServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Module", InstallServiceFragment.class );  
		registry.addMapping( PreServiceDevelopCommand.class, "EarProject", InstallServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Ear", InstallServiceFragment.class );
	
  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", RunServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Environment", RunServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Context", RunServiceFragment.class );  
  	registry.addMapping( PreServiceDevelopCommand.class, "Selection", RunServiceFragment.class );  
		registry.addMapping( PreServiceDevelopCommand.class, "Project", RunServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Module", RunServiceFragment.class );  
		registry.addMapping( PreServiceDevelopCommand.class, "EarProject", RunServiceFragment.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Ear", RunServiceFragment.class );
		
		//Mappings from framework to framework commands
  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", PreServiceAssembleCommand.class );
		registry.addMapping( PreServiceDevelopCommand.class, "Project", PreServiceAssembleCommand.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Module", PreServiceAssembleCommand.class );
		registry.addMapping( PreServiceDevelopCommand.class, "EarProject", PreServiceAssembleCommand.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Ear", PreServiceAssembleCommand.class );
		
    registry.addMapping( PreServiceDevelopCommand.class, "Context", PreServiceDeployCommand.class );
    
  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", PreServiceInstallCommand.class );
		registry.addMapping( PreServiceDevelopCommand.class, "Project", PreServiceInstallCommand.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Module", PreServiceInstallCommand.class );
		registry.addMapping( PreServiceDevelopCommand.class, "EarProject", PreServiceInstallCommand.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Ear", PreServiceInstallCommand.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Context", PreServiceInstallCommand.class );

  	registry.addMapping( PreServiceDevelopCommand.class, "WebService", PreServiceRunCommand.class );
  	registry.addMapping( PreServiceDevelopCommand.class, "Context", PreServiceRunCommand.class );
	
  }
}
