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
package org.eclipse.jst.ws.internal.uddiregistry.wizard;

import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


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
  public Status getOperationStatus(byte operation);

  // registry URLs
  public String getInquiryAPI();
  public String getPublishAPI();
}