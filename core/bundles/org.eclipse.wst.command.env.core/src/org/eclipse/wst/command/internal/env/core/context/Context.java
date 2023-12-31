/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.core.context;

/**
 * This interface provides a way to get and set preference information.
 *
 */
public interface Context 
{
 /**
  * Loads the preference data into this Context object.
  *
  */
 public void load();

 /**
  * Sets the default for a boolean preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setDefault (String name, boolean value); 
 
 /**
  * Sets the default for a string preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setDefault (String name, String value);
 
 /**
  * Sets the default for a int preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setDefault (String name, int value); 

 /**
  * Sets the value for a string preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setValue (String name, String value);
 
 /**
  * Sets the value for a boolean preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setValue (String name, boolean value);
 
 /**
  * Sets the value for a int preference.
  * @param name the preference name.
  * @param value the preference value.
  */
 public void setValue (String name, int value);   

 /**
  * Gets the value for a string preference.
  * @param name the preference name.
  * @return the preference value.
  */
 public String getValueAsString ( String name);
 
 /**
  * Gets the value for a boolean preference.
  * @param name the preference name.
  * @return the preference value.
  */
 public boolean getValueAsBoolean ( String name);
 
 /**
  * Gets the value for a int preference.
  * @param name the preference name.
  * @return the preference value.
  */
 public int getValueAsInt ( String name);  		
}
