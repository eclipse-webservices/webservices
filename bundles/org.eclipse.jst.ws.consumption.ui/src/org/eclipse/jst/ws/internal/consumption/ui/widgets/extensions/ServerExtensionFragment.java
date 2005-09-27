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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions;

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.fragment.ExtensionFragment;


public class ServerExtensionFragment extends ExtensionFragment
{  
  public ServerExtensionFragment()
  {

  }
  
  protected ServerExtensionFragment( ServerExtensionFragment fragment )
  {
    super( fragment );  
  }
  
  public void setServiceTypeRuntimeServer( TypeRuntimeServer ids )
  {
    setExtensionIds( new String[]{ ids.getTypeId(), ids.getRuntimeId(), ids.getServerId() } );
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  public Object clone() 
  {
    return new ServerExtensionFragment( this );
  }  
}
