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
package org.eclipse.wst.command.internal.env.context;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.wst.command.env.core.context.Context;

/**
 * This class is used as the base class for types that what to persist preference
 * data in the plugin preferences area.
 *
 *
 */
public abstract class PersistentContext implements Context
{
 protected Preferences preferences_;
 protected Plugin plugin_;

 public PersistentContext ( Plugin plugin)
 	{
 		plugin_ = plugin;
 	  	preferences_ = plugin.getPluginPreferences();
 	}

 /**
  * Sets the default for a boolean preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setDefault (String name, boolean value) {
 	preferences_.setDefault(name, value);
 	}
 
 /**
  * Sets the default for a string preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setDefault (String name, String value) {
 	preferences_.setDefault(name,value);
 	}
 
 /**
  * Sets the default for a int preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setDefault (String name, int value) {
 	preferences_.setDefault(name,value);
 	}
 
 /**
  * Sets the value for a string preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setValue (String name, String value) {
 	preferences_.setValue(name,value);
 	plugin_.savePluginPreferences();
 	}
 
 /**
  * Sets the value for a boolean preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setValue (String name, boolean value) {
 	preferences_.setValue(name, value);
 	plugin_.savePluginPreferences();
 	}
 
 /**
  * Sets the value for a int preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setValue (String name, int value) {
 	preferences_.setValue(name, value);
 	plugin_.savePluginPreferences();
	}

 /**
  * Gets the value for a string preference.
  * @param name the preference name.
  * @return the preference value.
  */
 public String getValueAsString ( String name) {
 	return preferences_.getString(name);
 	}
 
 /**
  * Gets the value for a boolean preference.
  * @param name the preference name.
  * @return the preference value.
  */
  public boolean getValueAsBoolean ( String name) {
 	return preferences_.getBoolean(name);
 	}
  
  /**
   * Gets the value for a int preference.
   * @param name the preference name.
   * @return the preference value.
   */
 public int getValueAsInt( String name) {
 		return preferences_.getInt(name);
 	}
 
 /**
  * Gets the default value for a string preference.
  * @param name the preference name.
  * @return the default preference value.
  */
 public String getDefaultString(String name)
 {
 	return preferences_.getDefaultString(name);
 }
 
 /**
  * Gets the default value for a boolean preference.
  * @param name the preference name.
  * @return the default preference value.
  */
 public boolean getDefaultBoolean(String name)
 {
 	return preferences_.getDefaultBoolean(name);
 }
 
 /**
  * Gets the default value for a int preference.
  * @param name the preference name.
  * @return the default preference value.
  */
 public int getDefaultInt(String name)
 {
 	return preferences_.getDefaultInt(name);
 }
 
 /**
  * Sets the default value for a string preference if a default value has
  * not already been set.
  * @param name the preference name.
  * @param value the default preference value
  */
 public void setDefaultStringIfNoDefault( String key, String value )
 {
   // If the key already has a default value we don't want to override it.
   if( preferences_.getDefaultString( key ).equals("") ) 
   {
     preferences_.setDefault( key, value );
   }
 }
 
 /**
  * Sets the default value for a boolean preference if a default value has
  * not already been set.
  * @param name the preference name.
  * @param value the default preference value
  */
 public void setDefaultBooleanIfNoDefault( String key, boolean value )
 {
   // If the key already has a default value we don't want to override it.
   if( preferences_.getDefaultString( key ).equals("") ) 
   {
     preferences_.setDefault( key, value );
   }
 }
 
 /**
  * Sets the default value for a int preference if a default value has
  * not already been set.
  * @param name the preference name.
  * @param value the default preference value
  */
 public void setDefaultIntIfNoDefault( String key, int value )
 {
   // If the key already has a default value we don't want to override it.
   if( preferences_.getDefaultString( key ).equals("") ) 
   {
     preferences_.setDefault( key, value );
   }
 }
}
