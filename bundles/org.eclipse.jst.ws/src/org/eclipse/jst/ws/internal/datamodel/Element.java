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

package org.eclipse.jst.ws.internal.datamodel;

import java.util.Enumeration;

/**
* This is the abstract class for elements that can be managed by a Model.
* Every element has a name, a set of properties, and a set of relationships
* to other elements.
*/
public interface Element
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Returns a clone of this <code>Element</code>
  * of the same class and with the same name and model,
  * but with no Properties, Rels or Listeners except for
  * the {@link Property#NAME NAME} property.
  * @return Object A new Element.
  */
  public Element shallowClone ();

  /**
  * Returns a clone of this <code>Element</code>
  * of the same class and with the same name and model
  * and with a copy of the original Element's Properties.
  * The Rels and Listeners are not cloned.
  * @return Element A new Element.
  */
  public Element deepClone ();

  /**
  * Returns the model this element belongs to, or null if the element
  * has not been added to a model. See also {@link #isValid isValid()}.
  * @return Model The model this element belongs to, or null if none.
  */
  public Model getModel ();

  /**
  * Returns true if and only if this element belongs to a model.
  * See also {@link #getModel getModel()}.
  * @return boolean True if and only if this element belongs to a model.
  */
  public boolean isValid ();

  /**
  * Removes this element from the model to which it currently belongs.
  * After calling this method, {@link #getModel getModel()} will return
  * null and {@link #isValid isValid()} will return false.
  * @return boolean True if the element was successfully removed from
  * its model. This method returns false if the element does not belong
  * to any model.
  */
  public boolean remove ();

  /**
  * Sets the name of this element and updates the "name" property.
  * Every element includes a property called "name" (see Property.NAME)
  * whose value is the same string as passed into setName().
  * @param name The name of this element.
  */
  public void setName ( String name );

  /**
  *
  */
  public void addListener ( ElementListener listener );

  /**
  * Returns the name of this element.
  * @return String The name of this element.
  */
  public String getName ();

  /**
  * Adds or sets a property of this element.
  * Properties set using this method can be retrieved using either
  * {@link #getProperty getProperty()} or
  * {@link #getPropertyAsObject getPropertyAsObject()}.
  * Every element includes a property called "name" (see Property.NAME)
  * whose value is the same string as passed into {@link #setName setName()}.
  * Any previous property with the same name is replaced.
  * @param property The property to set.
  */
  public void setProperty ( Property property );

  /**
  * Adds or sets a property of this element.
  * Properties set using this method can be retrieved using either
  * {@link #getProperty getProperty()} or
  * {@link #getPropertyAsObject getPropertyAsObject()}.
  * Every element includes a property called "name" (see Property.NAME)
  * whose value is the same string as passed into {@link #setName setName()}.
  * Any previous property with the same name is replaced.
  * @param name The name of the property to set.
  * @param value The Object value of the property to set.
  */
  public void setPropertyAsObject ( String name, Object value );

  /**
  * Adds or sets a String property of this element.
  * Properties set using this method can be retrieved using either
  * {@link #getProperty getProperty()},
  * {@link #getPropertyAsObject getPropertyAsObject()} or
  * {@link #getPropertyAsString getPropertyAsString()}.
  * Every element includes a property called "name" (see Property.NAME)
  * whose value is the same string as passed into {@link #setName setName()}.
  * Any previous property with the same name is replaced.
  * @param name The name of the property to set.
  * @param value The String value of the property to set.
  */
  public void setPropertyAsString ( String name, String value );

  /**
  * Returns a property of the given name or null if there is none.
  * Every element includes a property called "name" (see Property.NAME)
  * whose value is the same string as passed into {@link #setName setName()}.
  * @param name The name of the property to return.
  * @return Property The property, or null if none.
  */
  public Property getProperty ( String name );

  /**
  * Returns the Object value of a property of the given name or null
  * if there is none.
  * Every element includes a property called "name" (see Property.NAME)
  * whose value is the same string as passed into {@link #setName setName()}.
  * @param name The name of the property to return.
  * @return Object The property value as an Object, or null if none.
  */
  public Object getPropertyAsObject ( String name );

  /**
  * Returns the String value of a property of the given name or null
  * if there is none.
  * Every element includes a property called "name" (see Property.NAME)
  * whose value is the same string as passed into {@link #setName setName()}.
  * @param name The name of the property to return.
  * @return String The property value as a String, or null if none.
  */
  public String getPropertyAsString ( String name );

  /**
  * Returns an enumeration of all properties of this element.
  * There is always at least one property that carries the
  * name of this element (see {@link #setName setName()}).
  * @return Enumeration An enumeration of all properties of this element.
  */
  public Enumeration getProperties ();

  /**
  * Creates a bidirectional relationship between this element and another.
  * Both relationships are identified by names. The names of all the
  * outbound relationships of a element must be mutually unique.
  * @param element The element to connect to.
  * @param outboundRelName The name of the relationship to contain the
  * connection to the element.
  * @param inboundRelName The name of the relationship to contain the
  * inverse connection back to this element.
  * @return boolean True if the connection is created successfully.
  * Both elements must belong to the same model, other false is returned.
  */
  public boolean connect ( Element element, String outboundRelName, String inboundRelName );

  /**
  * Dismantles the connection to another element in a given relationship.
  * The inverse connection is also automatically dismantled.
  * @param element The element to disconnect from.
  * @param outboundRelName The name of the relationship containing
  * the connection to the element.
  * @return boolean True if the connection is removed successfully.
  * Both elements must belong to the same model, other false is returned.
  * If the elements are not connected through the given relationship, then
  * false is returned.
  */
  public boolean disconnect ( Element element, String outboundRelName );

  /**
  * Dismantles the connection to all elements in the given relationship.
  * The inverse connections are also automatically dismantled.
  * @param outboundRelName The name of the relationship.
  * @return boolean True if all connections are removed successfully.
  * This method returns false if the element does not belong to a model.
  */
  public boolean disconnectRel ( String outboundRelName );

  /**
  * Dismantles all connections from this element.
  * @return boolean True if all connections are removed successfully.
  * This method returns false if the element does not belong to a model.
  */
  public boolean disconnectAll ();

  /**
  * Returns an enumeration of all elements
  * connected thru the given relationship.
  * @param relName The name of the relationship.
  * @return Enumeration The elements in the relationship.
  * This method never returns null.
  */
  public Enumeration getElements ( String relName );

  /**
  * Returns the number of elements in the given relationship.
  * @param relName The name of the relationship.
  * @return int The number of elements in the relationship.
  * This method never returns a negative value.
  */

  public Enumeration getListeners();

  public int getNumberOfElements ( String relName );

  /**
  * Returns the relationship object of the given name.
  * @param relName The name of the relationship.
  * @return int The relationship. This method never returns null.
  * In other words, any reference to a relationship name automatically
  * brings a corresponding Rel object into existence. Careless naming
  * may result in the {@link #getRels getRels()} method returning an enumeration of
  * more relationships than would be meaningful, efficient or useful.
  */
  public Rel getRel ( String relName );

  /**
  * Returns an enumeration of all known relationships.
  * @return Enumeration The outbound relationships of the element.
  * This method never returns null.
  */
  public Enumeration getRels ();


  /*
  * There is a need sometimes for each element in a model to have a unique identifier.
  * Normally this would be left to a name, but since the element maker gives it its name
  * there is a chance that a model could have two elements with the same name. If they 
  * are the same element type then there would be no way to differenciate between the two.
  * The following getter and setter will provide a unique identifier. This identifier will be known 
  * as the muid or Model Unique Identifier. Unlike a uuid it will only be assured uniqueness 
  * within its own model. The intention is to use a unique number appended to the end 
  * of the name.
  * The following is the getter
  * @return String the unique identifier
  */

  public String getMUID();
  
  


}

