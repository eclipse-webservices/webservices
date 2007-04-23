/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.datamodel;

import java.util.Enumeration;
import java.util.Vector;

/**
* This is the abstract class for a simple data model consisting of
* a network of named nodes (class Node). Every node carries a set
* of named properties (class Property). Nodes are interrelated by
* relationships (class Rel) and connections (class Connection).
* Each Model keeps a registry of all of its nodes, and keeps a
* reference to one "root" node that can be used as a starting
* point for navigation.
*/
public interface Model
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Sets the name of the model.
  * @param name The name of the model.
  */
  public void setName ( String name );

  /**
  * Gets the name of the model.
  * @return String The name of the model. This must not be null.
  */
  public String getName ();

  /**
  * Sets a node as the root of the model. If the node does not
  * belong to a model, then it is added automatically. If the
  * node already belongs to another model, then the root node
  * is not set and method returns false.
  * @param root The node to set as the root. The node must
  * belong either to this model or to no model. This must
  * not be null.
  * @return boolean True if the node was set as the root.
  * This method returns false if the given node already belongs
  * to another model.
  */
  public boolean setRootElement ( Element root );

  /**
  * Returns the root node. This method always returns a node
  * except on an empty Model. If setRootNode() has not been
  * called, or if the last root node was removed from the model,
  * then the method will select and return an arbitrary node as
  * the root. This method will consistently return the same node
  * until either
  * (a) setRootNode() is called with a different node or
  * (b) removeNode() is called to remove the node from the model.
  * @return Node The current root node, or null if the model is empty.
  */

  public Element getRootElement ();

  /**
  * Get the elements that have this name
  * @param String name the name of the element 
  * @return Vector a vector of elements that have this name
  * These elements may be of different types
  **/

  public Vector getElementsByName(String name);

  /**
  * Adds a node to the model. If the node already belongs to another
  * model then it will not be added.
  * @param node The node to add. This must not be null.
  * @return boolean True if the node was added successfully.
  * This method returns false if the given node already belongs to
  * the current model or to another model.
  */
  public boolean addElement ( Element element );

  /**
  * Removes a node from the model.
  * @param node The node to remove. This must not be null.
  * @return boolean True if the node was removed successfully.
  * This method returns false if the given node does not belong
  * to this model.
  */
  public boolean removeElement ( Element element );

  /**
  * Returns an enumeration of all nodes in the model and in no
  * particular order,
  * @return Enumeration An enumeration of all nodes in the model.
  * This method never returns null.
  */
  public Enumeration getElements ();

  /**
  * Returns the number of nodes in the model.
  * @return int The number of nodes in the model.
  * This method never returns a negative value.
  */
  public int getNumberOfElements ();

  /**
  * Determines if this model contains the given node.
  * @param node The node to check for. This must not be null.
  * @return boolean True if and only if the model contains the node.
  */
  public boolean containsElement ( Element element );

  /*
  * This function will provide the next number in the queue for the MUID
  *
  */
  public int getUniqueNumber();

  /*
  * heres what we call from the element
  */
  public String makeMUID(String name);


}

