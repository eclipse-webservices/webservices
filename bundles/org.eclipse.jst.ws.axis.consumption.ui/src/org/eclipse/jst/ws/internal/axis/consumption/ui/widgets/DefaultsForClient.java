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

import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;

public class DefaultsForClient extends SimpleCommand
{
  private boolean customizeMappings_ = true;
  
  public boolean getCustomizeClientMappings()
  {
    return customizeMappings_;
  } 
  
  public void setCustomizeClientMappings( boolean value )
  {
    customizeMappings_ = value; 
  }
  
  public String getProxyFolder()
  {
     return "/proxyproject/myfolder";  
  }
}
