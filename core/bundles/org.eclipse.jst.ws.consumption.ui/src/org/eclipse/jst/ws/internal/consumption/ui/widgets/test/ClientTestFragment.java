/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
 * 20060727   144354 kathy@ca.ibm.com - Kathy Chan
 * 20070502   180304 gilberta@ca.ibm.com - Gilbert Andrews
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
  private boolean genProxy;  // This actually represent whether client test is requested
  private boolean launchedTest = false;
  private boolean canGenProxy;
  
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
    
    
    Condition jspCondition      = new Condition()
    {
      public boolean evaluate()
      {
        return canGenProxy;
      }
    };

    SimpleFragment simpleJSPFragment = new SimpleFragment( new WebServiceClientTestArrivalCommand(), "" );
    BooleanFragment choiceJSPFragment = new BooleanFragment();
    choiceJSPFragment.setTrueFragment(simpleJSPFragment);
    choiceJSPFragment.setCondition(jspCondition);
    clientTestRoot.add(choiceJSPFragment);
    
    clientTestRoot.add( new SimpleFragment( id ) );
    setTrueFragment( clientTestRoot );
    
  }
  
  public void setGenerateProxy( boolean genProxy )
  {
    this.genProxy = genProxy;  
  }
  
  public void setCanGenerateProxy( boolean canGenProxy )
  {
    this.canGenProxy = canGenProxy;  
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
