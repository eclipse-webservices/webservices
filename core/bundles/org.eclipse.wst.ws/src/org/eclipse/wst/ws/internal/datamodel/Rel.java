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

/**
* This is the abstract class for relationships in a Model.
*/
public interface Rel
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Returns the name of this relationship. Relationship names
  * cannot be changed.
  * @return String The name of the relationship.
  */
  public String getName ();

  /**
  * Returns the node that owns this relationship.
  * @return Node The node that owns this relationship.
  * This method never returns null, that is, relationships
  * cannot exist in the absence of a node.
  */
  public Element getSourceElement();

  /**
  * Returns an enumeration of all nodes in this relationship.
  * @return Enumeration The nodes in this relationship.
  * This method never returns null.
  */
  public Enumeration getTargetElements ();

  /**
  * Returns the number of nodes in this relationship.
  * @return int The number of nodes in this relationship.
  * This method never returns a negative value.
  */
  public int getNumberOfTargetElements ();

  /**
  * Adds a connection to this relationship. This method is
  * intended for use by derivations of the Model framework,
  * not by users of the framework. Connection objects should
  * only be constructed, added, removed and retrieved by
  * implementations of the Node and Rel interfaces.
  * @param connection The connection to add.
  */
  public void addConnection ( Connection connection );

  /**
  * Removes a connection from this relationship. This method is
  * intended for use by derivations of the Model framework,
  * not by users of the framework. Connection objects should
  * only be constructed, added, removed and retrieved by
  * implementations of the Node and Rel interfaces.
  * @param connection The connection to remove.
  * @return boolean True if the connection was found and removed.
  */
  public boolean removeConnection ( Connection connection );

  /**
  * Returns the Connection object for the given target node.
  * This method is intended for use by derivations of the Model
  * framework, not by users of the framework. Connection objects
  * should only be constructed, added, removed and retrieved by
  * implementations of the Node and Rel interfaces.
  * @param targetNode The node to find the Connection to.
  * @return Connection The connection, or null if none.
  */
  public Connection getConnectionTo ( Element targetElement );
}

