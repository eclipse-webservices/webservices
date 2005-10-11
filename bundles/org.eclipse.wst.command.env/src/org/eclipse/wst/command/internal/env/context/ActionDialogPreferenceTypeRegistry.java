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

import java.util.Vector;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.command.internal.env.preferences.ActionDialogPreferenceType;


public class ActionDialogPreferenceTypeRegistry
{
  private Vector preferences_;
  
  private static ActionDialogPreferenceTypeRegistry registry_;

  private ActionDialogPreferenceTypeRegistry()
  {
    preferences_ = new Vector();
    
    loadDialogsPreferences();  
  }
  
  static public ActionDialogPreferenceTypeRegistry getInstance()
  {
    if( registry_ == null )
    {
      registry_ = new ActionDialogPreferenceTypeRegistry();
    }
    
    return registry_;
  }
  
  //
  private void loadDialogsPreferences ()
  {
  	IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor( "org.eclipse.wst.command.env",
                                                                      "actionDialogPreferenceType");
    
    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement      elem   = config[idx];     
      ActionDialogPreferenceType dialog = new ActionDialogPreferenceType();
      
      dialog.setId( elem.getAttribute("id") );
      dialog.setName( elem.getAttribute("name") );
      dialog.setInfopop( elem.getAttribute("infopop") );
      dialog.setTooltip( elem.getAttribute("tooltip") );
      dialog.setCategory( elem.getAttribute("category") );
      
      String showCheckbox = elem.getAttribute( "showcheckbox" );
      String alwaysHide   = elem.getAttribute( "alwayshide" );
      
      dialog.setShowCheckbox( showCheckbox == null ? true : showCheckbox.equals( "true" ) );
      dialog.setAlwaysHide( alwaysHide == null ? false : alwaysHide.equals( "true" ) );
      
      preferences_.add(dialog);        
    }
  }

  /**
  * Returns all registered <code>WebServiceType</code> objects.
  * @return All registered <code>WebServiceType</code> objects.
  */
  public ActionDialogPreferenceType[] getActionDialogsPrefrences ()
  {
    return (ActionDialogPreferenceType[])preferences_.toArray( new ActionDialogPreferenceType[0]);
  }
  
  public ActionDialogPreferenceType getActionDialogsPrefrence( String id )
  {
    int                        length = preferences_.size();
    ActionDialogPreferenceType result = null;
    
    for( int index = 0; index < length; index++ )
    {
      ActionDialogPreferenceType preference = (ActionDialogPreferenceType)preferences_.elementAt( index );
      
      if( preference.getId().equals( id ) )
      {
        result = preference;
        break;
      }
    }
    
    return result;
  }
}
