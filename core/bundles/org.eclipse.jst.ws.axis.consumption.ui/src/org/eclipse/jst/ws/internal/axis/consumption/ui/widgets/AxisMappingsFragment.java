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
package org.eclipse.jst.ws.internal.axis.consumption.ui.widgets;

import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;


public class AxisMappingsFragment extends BooleanFragment
{
  private boolean showMappings_;

  public AxisMappingsFragment()
  {
    super();
    setTrueFragment( new SimpleFragment( "AxisMappingsWidget" ));
    setCondition( new Condition()
                  {
                    public boolean evaluate()
                    {
                      return showMappings_;
                    }
                  } );
  }
  
  public void setShowMapping( boolean showMappings )
  {
    showMappings_ = showMappings;
  }      
}
