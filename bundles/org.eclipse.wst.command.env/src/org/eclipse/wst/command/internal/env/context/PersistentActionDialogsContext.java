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

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.command.env.context.PersistentContext;
import org.eclipse.wst.command.internal.env.preferences.ActionDialogPreferenceType;


public class PersistentActionDialogsContext extends PersistentContext
{
  private ActionDialogPreferenceTypeRegistry registry;
  
  private static PersistentActionDialogsContext instance_ = null;

  private PersistentActionDialogsContext()
  {
    super(Platform.getPlugin("org.eclipse.wst.env"));
  }
  
  static public PersistentActionDialogsContext getInstance()
  {
    if( instance_ == null )
    {
      instance_ = new PersistentActionDialogsContext();
      instance_.load();
    }
    
    return instance_;
  }

  public void load()
  {
    registry = ActionDialogPreferenceTypeRegistry.getInstance();
    ActionDialogPreferenceType[] dialogs = registry.getActionDialogsPrefrences();
  
    for (int i = 0; i < dialogs.length; i++)
    {
      setDefault(dialogs[i].getId(), false);
    }
  }

  public ActionDialogPreferenceType[] getDialogs()
  {
    return registry.getActionDialogsPrefrences();
  }

  public void setActionDialogEnabled(String id, boolean value)
  {
    setValue(id, value);
  }

  public boolean isActionDialogEnabled(String id)
  {
    if (id == null) return true;
    return getValueAsBoolean(id);
  }
  
  public boolean showDialog( String id )
  {
    ActionDialogPreferenceType dialog = registry.getActionDialogsPrefrence( id );    
   
    return (dialog.getShowCheckbox() && !dialog.getAlwaysHide() && !isActionDialogEnabled( id )) ||
            !dialog.getShowCheckbox() && !dialog.getAlwaysHide();
  }
  
  public boolean showCheckbox( String id )
  {
    ActionDialogPreferenceType dialog = registry.getActionDialogsPrefrence( id );    
    
    return dialog == null ? false : dialog.getShowCheckbox();
  }
}
