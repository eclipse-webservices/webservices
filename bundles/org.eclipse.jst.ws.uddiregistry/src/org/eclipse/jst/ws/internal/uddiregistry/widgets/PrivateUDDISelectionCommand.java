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
package org.eclipse.jst.ws.internal.uddiregistry.widgets;

import org.eclipse.jst.ws.internal.uddiregistry.wizard.PrivateUDDIRegistryType;
import org.eclipse.wst.command.env.core.SimpleCommand;
/*
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.jst.ws.internal.uddiregistry.plugin.WebServiceUDDIRegistryPlugin;
*/

public class PrivateUDDISelectionCommand extends SimpleCommand
{
  private byte operationType;
  private PrivateUDDIRegistryType registryType;
  
  /*
  public Status execute(Environment env)
  {
    MessageUtils msgUtils = new MessageUtils(WebServiceUDDIRegistryPlugin.ID + ".plugin", this);
  	if (registryType != null)
  	{
  	  switch (operationType)
  	  {
  	  	case PrivateUDDIRegistryType.OP_DEPLOY:
  	      return registryType.deploy(env);
  	  	case PrivateUDDIRegistryType.OP_UPDATE:
  	  	  return registryType.update(env);
  	  	case PrivateUDDIRegistryType.OP_REMOVE:
  	  	  return registryType.remove(env);
  	  	default:
  	  }
  	}
  	return new SimpleStatus("");
  }
  */

  public PrivateUDDIRegistryType getRegistryType()
  {
    return getPrivateUDDIRegistryType();
  }
  
  public byte getOperationType()
  {
    return operationType;
  }
  
  public PrivateUDDIRegistryType getPrivateUDDIRegistryType()
  {
  	return registryType;
  }

  public void setRegistryType(PrivateUDDIRegistryType regType)
  {
  	setPrivateUDDIRegistryType(regType);
  }
  
  public void setOperationType(byte type)
  {
    operationType = type;
  }
  
  public void setPrivateUDDIRegistryType(PrivateUDDIRegistryType regType)
  {
  	registryType = regType;
  }
}