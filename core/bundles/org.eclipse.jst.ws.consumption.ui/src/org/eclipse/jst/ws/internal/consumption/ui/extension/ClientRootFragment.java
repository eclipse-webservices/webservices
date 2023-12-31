/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner          
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.extension;

import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.DataObjectCommand;
import org.eclipse.wst.ws.internal.extensions.AssembleClientFragment;
import org.eclipse.wst.ws.internal.extensions.DeployClientFragment;
import org.eclipse.wst.ws.internal.extensions.DevelopClientFragment;
import org.eclipse.wst.ws.internal.extensions.InstallClientFragment;
import org.eclipse.wst.ws.internal.extensions.RunClientFragment;

public class ClientRootFragment extends SequenceFragment 
{
  public ClientRootFragment()
  {
    add( new SimpleFragment( new PreClientDevelopCommand(), "" ) );
    add( new DevelopClientFragment() );
    add( new SimpleFragment( new PreClientAssembleCommand(), "" ) );
    add( new AssembleClientFragment() );
    add( new SimpleFragment( new PreClientDeployCommand(), "" ) );
    add( new DeployClientFragment() );
    add( new SimpleFragment( new PreClientInstallCommand(), "" ) );
    add( new InstallClientFragment() );
    add( new SimpleFragment( new PreClientRunCommand(), 
    	 "org.eclipse.jst.ws.internal.consumption.ui.extension.PreClientRunCommand" ) );
    add( new RunClientFragment() );
  }

  public void registerDataMappings(DataMappingRegistry registry) 
  {
  	registry.addMapping( PreClientDevelopCommand.class, "WebService", DevelopClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Environment", DevelopClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Context", DevelopClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Selection", DevelopClientFragment.class );
    registry.addMapping( PreClientDevelopCommand.class, "Project", DevelopClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Module", DevelopClientFragment.class );  
    registry.addMapping( PreClientDevelopCommand.class, "EarProject", DevelopClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Ear", DevelopClientFragment.class );
  	
	// Map the PreClientDevelopCommand into the dataObject.
	registry.addMapping( PreClientDevelopCommand.class, "DataObject", DataObjectCommand.class );
		
  	registry.addMapping( PreClientDevelopCommand.class, "WebService", AssembleClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Environment", AssembleClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Context", AssembleClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Selection", AssembleClientFragment.class );
    registry.addMapping( PreClientDevelopCommand.class, "Project", AssembleClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Module", AssembleClientFragment.class );  
    registry.addMapping( PreClientDevelopCommand.class, "EarProject", AssembleClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Ear", AssembleClientFragment.class );
	
  	registry.addMapping( PreClientDevelopCommand.class, "WebService", DeployClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Environment", DeployClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Context", DeployClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Selection", DeployClientFragment.class );
    registry.addMapping( PreClientDevelopCommand.class, "Project", DeployClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Module", DeployClientFragment.class );  
    registry.addMapping( PreClientDevelopCommand.class, "EarProject", DeployClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Ear", DeployClientFragment.class );
	
  	registry.addMapping( PreClientDevelopCommand.class, "WebService", InstallClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Environment", InstallClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Context", InstallClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Selection", InstallClientFragment.class );
    registry.addMapping( PreClientDevelopCommand.class, "Project", InstallClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Module", InstallClientFragment.class );  
    registry.addMapping( PreClientDevelopCommand.class, "EarProject", InstallClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Ear", InstallClientFragment.class );
	
  	registry.addMapping( PreClientDevelopCommand.class, "WebService", RunClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Environment", RunClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Context", RunClientFragment.class );  
  	registry.addMapping( PreClientDevelopCommand.class, "Selection", RunClientFragment.class );  
    registry.addMapping( PreClientDevelopCommand.class, "Project", RunClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Module", RunClientFragment.class );  
    registry.addMapping( PreClientDevelopCommand.class, "EarProject", RunClientFragment.class );
  	registry.addMapping( PreClientDevelopCommand.class, "Ear", RunClientFragment.class );
    
    //Mappings from framework to framework commands
  	registry.addMapping( PreClientDevelopCommand.class, "Context", PreClientAssembleCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Context", PreClientDeployCommand.class );
    
    registry.addMapping( PreClientDevelopCommand.class, "WebService", PreClientAssembleCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Project", PreClientAssembleCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Module", PreClientAssembleCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "EarProject", PreClientAssembleCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Ear", PreClientAssembleCommand.class );
    
    registry.addMapping( PreClientDevelopCommand.class, "WebService", PreClientInstallCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Project", PreClientInstallCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Context", PreClientInstallCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Module", PreClientInstallCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "EarProject", PreClientInstallCommand.class );
    registry.addMapping( PreClientDevelopCommand.class, "Ear", PreClientInstallCommand.class );   

    registry.addMapping( PreClientDevelopCommand.class, "WebService", PreClientRunCommand.class );    
    registry.addMapping( PreClientDevelopCommand.class, "Context", PreClientRunCommand.class );
	
  }
}
