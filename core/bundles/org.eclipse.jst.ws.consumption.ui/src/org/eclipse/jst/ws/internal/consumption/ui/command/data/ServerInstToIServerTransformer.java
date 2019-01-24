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
 * Created on Apr 20, 2004
 */
package org.eclipse.jst.ws.internal.consumption.ui.command.data;

import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

/*
 * Transforms a server instance id to an IServer
 */
public class ServerInstToIServerTransformer implements Transformer {

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.data.Transformer#transform(java.lang.Object)
   */
  public Object transform(Object value) {
    
    String existingServerInstId = (String)value;
    IServer serverInst = null;
    if (existingServerInstId != null) {
    	serverInst = ServerCore.findServer(existingServerInstId);
    }
    return serverInst;
  }

}
