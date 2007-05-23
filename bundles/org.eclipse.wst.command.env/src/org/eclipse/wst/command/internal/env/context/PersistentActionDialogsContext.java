/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070522   184006 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.context;

import org.eclipse.wst.command.internal.env.plugin.EnvPlugin;
import org.eclipse.wst.command.internal.env.preferences.ActionDialogPreferenceType;


public class PersistentActionDialogsContext extends PersistentContext
{
  private ActionDialogPreferenceTypeRegistry registry;
  
  private static PersistentActionDialogsContext instance_ = null;

  private PersistentActionDialogsContext()
  {
    super(EnvPlugin.getInstance());
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

  // This method is usually called from the popup wizard with the Id
  // of the ObjectContribution defining the popup.
  public void setObjectActionDialogEnabled( String id, boolean value )
  {
    // Need to first find the dialog object that references the popup.
    ActionDialogPreferenceType dialog = registry.getActionDialogsPrefrence( id );
    
    if( dialog != null )
    {
      setActionDialogEnabled( dialog.getId(), value );
    }
  }
  
  public boolean isActionDialogEnabled(String id)
  {
    if (id == null) return true;
    return getValueAsBoolean(id);
  }
  
  public boolean showDialog( String id )
  {
    ActionDialogPreferenceType dialog = registry.getActionDialogsPrefrence( id );    
   
    // We are trying to determine if the popup wizard should be displayed
    // or not with this logic.  If the dialog variable is null then
    // there was no popup extension point point defined for this wizard.  In
    // this case we will assume that the popup should always be displayed.
    // In the case where the extension says that the show check box control
    // should be displayed we need to ensure that wizard should not always
    // be hidden, as well we need to check the current setting of the check
    // box via the isActionDialogEnabled call.  If the show check box state
    // defined in the extension is false, but always hide is also false then
    // we will display the pop wizard.
    return dialog == null ||
           ( ( dialog.getShowCheckbox() && 
               !dialog.getAlwaysHide() && 
               !isActionDialogEnabled( dialog.getId()) ) ||
             ( !dialog.getShowCheckbox() && 
               !dialog.getAlwaysHide() ) );
  }
  
  public boolean showCheckbox( String id )
  {
    ActionDialogPreferenceType dialog = registry.getActionDialogsPrefrence( id );    
    
    return dialog == null ? false : dialog.getShowCheckbox();
  }
}
