/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 */
package org.eclipse.jst.ws.internal.creation.ui.widgets.test;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WSDLTestLaunchCommand;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;


public class ServiceTestFragment extends BooleanFragment
{
  private boolean testService;
  private boolean generateProxy;
  
  
  public ServiceTestFragment( String id )
  {
  	SequenceFragment testRoot = new SequenceFragment();
  	Condition condition = new Condition()
                          {
                            public boolean evaluate()
                            {
                              return testService;
                            }
                          };
    setCondition( condition );
    
    testRoot.add( new SimpleFragment( new WebServiceTestDefaultingCommand(), "" ) );
    testRoot.add( new SimpleFragment( id ) );
    setTrueFragment( testRoot );
      
  }
 
  
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
  	
  	dataRegistry.addMapping(WebServiceTestDefaultingCommand.class, "TestID",WSDLTestLaunchCommand.class);
  	
  	//from the wizard 
  	dataRegistry.addMapping(ServiceTestWidget.class, "TestID",WSDLTestLaunchCommand.class);
  	dataRegistry.addMapping(WebServiceTestDefaultingCommand.class, "ExternalBrowser",WSDLTestLaunchCommand.class);
  }
  
  public void setGenerateProxy(boolean generateProxy)
  {
  	this.generateProxy = generateProxy;
  }
  
  public boolean getGenerateProxy()
  {
  	return generateProxy;
  }
  
  /**
   * @return Returns the testService.
   */
  public void setTestService( boolean testService )
  {
    this.testService = testService;
  }

  
  
}
