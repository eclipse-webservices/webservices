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
package org.eclipse.jst.ws.internal.uddiregistry.wizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;


public interface PrivateUDDIRegistryType extends CommandWidgetBinding
{
  // operation types
  public static byte OP_DEPLOY = 0x0;
  public static byte OP_UPDATE = 0x1;
  public static byte OP_REMOVE = 0x2;

  // general information
  public String getID();
  public String getName();
  public boolean isPrivateUDDIRegistryInstalled();
  public IStatus getOperationStatus(byte operation);

  // registry URLs
  public String getInquiryAPI();
  public String getPublishAPI();
}
