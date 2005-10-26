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
/*
 * Created on Mar 25, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample.AddModuleDependenciesCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;


/**
 * @author gilberta
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class WebServiceTestClientDepartureFragment extends SequenceFragment 
{
  public WebServiceTestClientDepartureFragment()
  {
  	add(new SimpleFragment(new FinishDefaultCommand(),""));
   	add(new SimpleFragment(new ClientTestDelegateCommand(),""));
  }

  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
  	dataRegistry.addMapping(TestClientDepartureInitCommand.class, "ForceBuild",BuildProjectCommand.class);
  	dataRegistry.addMapping(FinishDefaultCommand.class, "SampleServerTypeID",AddModuleDependenciesCommand.class);
  	dataRegistry.addMapping(FinishDefaultCommand.class, "SampleExistingServer",AddModuleDependenciesCommand.class);
  	dataRegistry.addMapping(FinishDefaultCommand.class, "SampleServerTypeID",ClientTestDelegateCommand.class);
  	dataRegistry.addMapping(FinishDefaultCommand.class, "SampleExistingServer",ClientTestDelegateCommand.class);
  	    
  }

}
