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
/**
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;


public class ClientTestFragment extends BooleanFragment
{
  private boolean testService;
  private boolean genProxy;
  private boolean launchedTest = false;
  
  public ClientTestFragment( String id )
  {
    SequenceFragment clientTestRoot = new SequenceFragment();
    Condition        condition      = new Condition()
                                      {
                                        public boolean evaluate()
                                        {
                                          return testService && genProxy;
                                        }
                                      };
    setCondition( condition );
    
    clientTestRoot.add( new SimpleFragment( new WebServiceClientTestArrivalCommand(), "" ) );
    clientTestRoot.add( new SimpleFragment( id ) );
    setTrueFragment( clientTestRoot );
    
  }
  
  public void setGenerateProxy( boolean genProxy )
  {
    this.genProxy = genProxy;  
  }
  
  public void setTestService( Boolean testService )
  {
    this.testService = testService.booleanValue();  
  } 

  public boolean getLaunchedTest()
  {
  	return launchedTest;
  }
}
