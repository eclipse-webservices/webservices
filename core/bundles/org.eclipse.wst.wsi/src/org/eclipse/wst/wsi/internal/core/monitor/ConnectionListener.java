/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.monitor;

/**
 * This interface is for receiving connection events.
 * 
 * @author Sergey Suhoruchkin
 * @version 1.0.1
 */
public interface ConnectionListener
{
  /**
   * Invoked when connection is closed.
   * @param connection socket connection.
   */
  public void connectionClosed(SocketConnection connection);
}
