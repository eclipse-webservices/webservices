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
package org.eclipse.jst.ws.internal.axis.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.DefaultsForClientJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.Stub2BeanCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Condition;


public class AxisClientCommandsFragment extends BooleanFragment
{
  private boolean genProxy_;
  
  public AxisClientCommandsFragment()
  {
    SequenceFragment root = new SequenceFragment();
    
    root.add(new SimpleFragment(new DefaultsForHTTPBasicAuthCommand(), ""));
    root.add(new SimpleFragment(new CopyAxisJarCommand(), ""));
    root.add(new SimpleFragment(new AddJarsToProjectBuildPathTask(), ""));
    root.add(new SimpleFragment(new DefaultsForClientJavaWSDLCommand(), "")); 
    root.add(new SimpleFragment(new ValidateWSDLCommand(), ""));
    root.add(new SimpleFragment(new WSDL2JavaCommand(), "")); 
    root.add(new SimpleFragment(new RefreshProjectCommand(), ""));
    root.add(new SimpleFragment(new Stub2BeanCommand(), ""));
    root.add(new SimpleFragment(new BuildProjectCommand(), ""));
    
    setCondition( new Condition()
                  {
                    public boolean evaluate()
                    {
                      return genProxy_;
                    }
                  });
    setTrueFragment( root );
  }
  
  public void setGenerateProxy( boolean genProxy )
  {
    genProxy_ = genProxy;
  }
}
