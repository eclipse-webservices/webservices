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

package org.eclipse.wst.ws.internal.datamodel;

/**
* This is the abstract class for properties of a Node.
* Every property has an immutable name and a value of type Object.
*/
public interface Property
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Every Node has a property called "name" that is equivalent
  * to and kept synchronized with Node.getName() and Node.setName().
  */
  public static final String NAME = "name";

  /**
  * Returns a shallow clone of this <code>Property</code>.
  * Property key and value references are cloned,
  * but the value objects themselves are not cloned.
  * @return Property A new Property.
  */
  public Property shallowClone ();

  /**
  * Returns the name of this node.
  * @return String The name of this node.
  */
  public String getName ();

  /**
  * Sets the value of this property.
  * @param value The value to set. Any previous value is replaced.
  */
  public void setValue ( Object value );

  /**
  * Returns the value of this property.
  * @return Object The value of this property, possibly null.
  */
  public Object getValue ();

  /**
  * Sets the value of this property as a string.
  * @param value The string to set. Any previous value is replaced.
  * The type of the previous value, if any, is of no consequence.
  */
  public void setValueAsString ( String value );

  /**
  * Returns the value of this property as a string.
  * @return String the value of this property as a string, possibly null.
  * If the set value is an arbitrary Object, then Object.toString() is
  * returned.
  */
  public String getValueAsString ();
}
