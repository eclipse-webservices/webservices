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

import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;


public class LaunchFragment extends BooleanFragment
{
  private boolean publishToPublicUDDI_;  
  private boolean publishToPrivateUDDI_;
  
  public LaunchFragment()
  {
    Condition condition = new Condition()
    {
      public boolean evaluate()
      {
        return (publishToPublicUDDI_ || publishToPrivateUDDI_);
      }
    };
    setCondition( condition );
    setTrueFragment( new SimpleFragment( new WSExplorerLauncherCommand(), "" ) );
  }
  
  public void setPublishToPublicUDDI(boolean publishToPublicUDDI)
  {
    publishToPublicUDDI_ = publishToPublicUDDI;
  }
  
  public void setPublishToPrivateUDDI(boolean publishToPrivateUDDI)
  {
  	publishToPrivateUDDI_ = publishToPrivateUDDI;
  }
   
}
