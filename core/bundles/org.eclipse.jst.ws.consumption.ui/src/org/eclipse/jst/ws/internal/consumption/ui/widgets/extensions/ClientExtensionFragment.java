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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions;

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.fragment.ExtensionFragment;


public class ClientExtensionFragment extends ExtensionFragment
{  
  public ClientExtensionFragment()
  {
  }
  
  protected ClientExtensionFragment( ClientExtensionFragment fragment )
  {
    super( fragment );  
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer ids )
  {
    setExtensionIds( new String[]{ ids.getTypeId(), ids.getRuntimeId(), ids.getServerId() } );
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  public Object clone() 
  {
    return new ClientExtensionFragment( this );
  }  
}
