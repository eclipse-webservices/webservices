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
/*
 * Created on May 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;


/**
 * @author gilberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FinishJavaTestFragment extends BooleanFragment 
{
  private boolean generateProxy;
	
  public FinishJavaTestFragment()
  {
  	SequenceFragment javaTestRoot = new SequenceFragment();
  	Condition condition = new Condition()
	{
	  public boolean evaluate()
	  {
	    return generateProxy; 
	  }
	};
    setCondition(condition);
    javaTestRoot.add(new SimpleFragment(new TestClientDepartureInitCommand(), ""));
    javaTestRoot.add(new SimpleFragment(new BuildProjectCommand(), ""));
    setTrueFragment(javaTestRoot);
  }

  public void setGenerateProxy(boolean generateProxy)
  {
  	this.generateProxy = generateProxy;
  }
  
  public void setIsJSPGen(boolean jspGen)
  {
  	
  }
  
  
  
}
