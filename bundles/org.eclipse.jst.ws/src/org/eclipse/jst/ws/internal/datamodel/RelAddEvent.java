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

/**
* Carries data relevant to when a property is added to the model.
*/
public class RelAddEvent
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * The model the changed.
  */
  protected Connection fOutBound;

  /**
  * The model the changed.
  */
  protected Connection fInBound;

  /**
  * The property that was added.
  */
  protected Property fProperty;

 
  /**
   * Constructor.
   * @param model The model that changed.
   * @param property The property that was added.
   * @param value The property's value.
   */
  public RelAddEvent (Connection[] pair)
  {
     fOutBound = pair[BasicConnection.OUTBOUND];
     fInBound = pair[BasicConnection.INBOUND];
  }

  /**
   * Returns the outbound connection that changed 
  **/
  public Connection getOutBoundConnection ()
  {
    return fOutBound;
  }

  /**
   * Returns the changed rel name 
  **/
  public String getOutBoundRelName()
  {
    return fOutBound.getRel().getName();
  }

  /**
   * Returns the changed rel
  **/
  public Rel getOutBoundRel()
  {
    return fOutBound.getRel();
  }

  /**
   * Returns the changed rel name 
  **/
  public String getInBoundRelName()
  {
    return fInBound.getRel().getName();
  }

  /**
   * Returns the changed rel
  **/
  public Rel getInBoundRel()
  {
    return fInBound.getRel();
  }

  /**
   * Returns the outbound element that changed 
  **/
  public Element getParentElement ()
  {
    return fOutBound.getElement();
  }

  
  /**
   * Returns the inbound connection that changed 
  **/
  public Element getChildElement ()
  {
    return fInBound.getElement();
  }

  
}

