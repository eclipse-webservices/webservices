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
* This is the abstract class for a connection that ties a Rel to a Node.
* Normally Connection objects are manufactured and managed within the
* derived classes of the Model framework, and are not manipulated by
* the caller directly.
*/
public interface Connection
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Returns the Rel that owns this connection.
  * @return Rel The Rel that owns this connection.
  * This method never returns null, that is, a Connection
  * cannot exist without a Rel to own it.
  */
  public Rel getRel ();

  /**
  * Returns the Node that this connection points to.
  * @return Node The Node this connection points to.
  * This method never returns null, that is, a Connection
  * cannot exist without a Node to point to.
  */
  public Element getElement ();

  /**
  * Returns the opposing connection to this connection.
  * Connection objects always exist in pairs, that is,
  * if node "Parent" has a relationship named "Children"
  * containing a connection to node "Child", then there
  * must exist an opposing connection from "Child" to
  * "Parent" in some relationship of "Child" (for example,
  * in a relationship called "Parents").
  * @return Connection The opposing connection.
  * As a general rule, this method should never return null.
  * It may only return null during construction of the pair
  * of connections.
  */
  public Connection getOpposingConnection ();
}

