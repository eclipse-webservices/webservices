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

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;

public class ActionDialogPreferenceTypeRegistry
{
  private static ActionDialogPreferenceTypeRegistry instance_;

  private Hashtable typesByName_;

  //
  // Loads WebServiceType objects into this registry.
  // This is done by querying the plugin registry for all extensions
  // hanging on the webServiceType extension point. Extensions
  // must implement the org.eclipse.jst.ws.ui.wizard.WebServiceType
  // interface.
  //
  private void loadDialogsPreferences ()
  {
    IPluginRegistry reg = Platform.getPluginRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.ui",
                                     "actionDialogPreferenceType");
    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];
      try 
      {
        Object actionDialogPreferenceType = elem.createExecutableExtension("class");
        if (actionDialogPreferenceType instanceof ActionDialogPreferenceType) 
        {
          ActionDialogPreferenceType dialog = (ActionDialogPreferenceType) actionDialogPreferenceType;
          dialog.setId((String)elem.getAttribute("id"));
          dialog.setName((String)elem.getAttribute("name"));
          dialog.setInfopop((String)elem.getAttribute("infopop"));
          dialog.setTooltip((String)elem.getAttribute("tooltip"));
          add(dialog);
        }
      } catch (CoreException e)
      {
      }
    }
  }

  //
  // Loads actionDialogPreferenceType objects into this registry.
  // See method getInstance().
  //
  private void load ()
  {
    typesByName_ = new Hashtable();
    loadDialogsPreferences();
  }

  //
  // Add the given WebServiceType to this registry.
  // See method load().
  //
  private void add ( ActionDialogPreferenceType dialog )
  {
    typesByName_.put(dialog.getId(),dialog);
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceTypeRegistry object.
  */
  public static ActionDialogPreferenceTypeRegistry getInstance ()
  {
    if (instance_ == null)
    {
      instance_ = new ActionDialogPreferenceTypeRegistry();
      instance_.load();
    }
    return instance_;
  }

  /**
  * Returns all registered <code>WebServiceType</code> objects.
  * @return All registered <code>WebServiceType</code> objects.
  */
  public ActionDialogPreferenceType[] getActionDialogsPrefrences ()
  {
    ActionDialogPreferenceType[] dialogs = new ActionDialogPreferenceType[typesByName_.size()];
    Enumeration e = typesByName_.elements();
    for (int i=0; e.hasMoreElements(); i++)
    {
      dialogs[i] = (ActionDialogPreferenceType)e.nextElement();
    }
    return dialogs;
  }

  public ActionDialogPreferenceType getActionDialogPrefrenceTypeByName (String name)
  {
  	return (name == null ? null : (ActionDialogPreferenceType)typesByName_.get(name));
  }
}
