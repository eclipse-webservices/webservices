/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.preferences;

import org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;


public class PersistentActionDialogsContext extends PersistentContext
{
 private ActionDialogPreferenceType [] dialogs_;

public PersistentActionDialogsContext () 
{
	super(  WebServiceUIPlugin.getInstance());
}
public void load() 
{
    ActionDialogPreferenceTypeRegistry reg = ActionDialogPreferenceTypeRegistry.getInstance();
    dialogs_ = reg.getActionDialogsPrefrences();
    for (int i = 0; i < dialogs_.length; i++) {
    	setDefault(dialogs_[i].getId(), true);
   }
}

public ActionDialogPreferenceType[] getDialogs()
{
  return dialogs_;
}

public void setActionDialogEnabled ( String id, boolean value)
{
 	setValue (id, value);
}

public boolean isActionDialogEnabled( String id)
{
     if (id == null ) 
     	return true;
	 return getValueAsBoolean(id);
}

}
