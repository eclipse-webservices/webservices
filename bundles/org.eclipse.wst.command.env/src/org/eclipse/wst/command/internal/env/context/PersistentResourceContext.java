/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.context;

import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.command.env.core.context.ResourceDefaults;
import org.eclipse.wst.command.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.plugin.EnvPlugin;


public class PersistentResourceContext extends PersistentContext implements ResourceContext
{
  private static PersistentResourceContext context_ = null;
  
  public static PersistentResourceContext getInstance()
  {
    if( context_ == null )
    {
      context_ = new PersistentResourceContext();
      context_.load();
    }
    
    return context_;
  }
  
  private PersistentResourceContext()
  {
    super(EnvPlugin.getInstance());
  }

  public void load()
  {
    setDefault(PREFERENCE_OVERWRITE, ResourceDefaults
        .getOverwriteFilesDefault());
    setDefault(PREFERENCE_CREATE_FOLDERS, ResourceDefaults
        .getCreateFoldersDefault());
    setDefault(PREFERENCE_CHECKOUT, ResourceDefaults.getCheckoutFilesDefault());
  }

  public void setOverwriteFilesEnabled(boolean enable)
  {
    setValue(PREFERENCE_OVERWRITE, enable);
  }

  public boolean isOverwriteFilesEnabled()
  {
    return getValueAsBoolean(PREFERENCE_OVERWRITE);
  }

  public void setCreateFoldersEnabled(boolean enable)
  {
    setValue(PREFERENCE_CREATE_FOLDERS, enable);
  }

  public boolean isCreateFoldersEnabled()
  {
    return getValueAsBoolean(PREFERENCE_CREATE_FOLDERS);
  }

  public void setCheckoutFilesEnabled(boolean enable)
  {
    setValue(PREFERENCE_CHECKOUT, enable);
  }

  public boolean isCheckoutFilesEnabled()
  {
    return getValueAsBoolean(PREFERENCE_CHECKOUT);
  }

  public ResourceContext copy()
  {
    ResourceContext cc = new TransientResourceContext();
    cc.setOverwriteFilesEnabled(isOverwriteFilesEnabled());
    cc.setCreateFoldersEnabled(isCreateFoldersEnabled());
    cc.setCheckoutFilesEnabled(isCheckoutFilesEnabled());
    return cc;
  }
}
