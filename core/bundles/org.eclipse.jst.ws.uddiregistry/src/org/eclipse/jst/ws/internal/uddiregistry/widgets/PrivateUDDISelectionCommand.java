/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.uddiregistry.widgets;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.uddiregistry.wizard.PrivateUDDIRegistryType;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
/*
import org.eclipse.wst.command.internal.env.core.common.Environment;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.env.core.common.Status;
import org.eclipse.jst.ws.internal.uddiregistry.plugin.WebServiceUDDIRegistryPlugin;
*/

public class PrivateUDDISelectionCommand extends AbstractDataModelOperation
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

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return Status.OK_STATUS;
  }
}
