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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class BasicModel implements Model
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private String fName;
  private Hashtable fElements;
  private Element fRoot;
  //This is used by the MUID
  private int fUniqueNumberCounter;

  public BasicModel ( String name )
  {
    fName = name;
    fElements = new Hashtable();
    fRoot = null;
    fUniqueNumberCounter = 0;
  }

  public void setName ( String name )
  {
    fName = name;
  }

  public String getName ()
  {
    return fName;
  }


  /*
  * simple counter that increments each call 
  */
  public int getUniqueNumber()
  {
    fUniqueNumberCounter++;
    return fUniqueNumberCounter;
  }

  /*
  * This will use a unique number and append it to the end of the name 
  * @param String name of the element
  * @return String MUID
  */
  public String makeMUID(String name)
  {
    String num = String.valueOf(getUniqueNumber());
    String muid = name + num;
    return muid;
  }


  public boolean setRootElement ( Element root )
  {
    if (root.getModel() == null)
      addElement(root);
    else if (root.getModel() != this)
      return false;
    fRoot = root;
    return true;
  }

  public Element getRootElement ()
  {
    if (fRoot == null) fRoot = getFirstElement();
    return fRoot;
  }


  /**
  * Get the elements that have this name
  * @param String name the name of the element 
  * @return Vector a vector of elements that have this name
  * These elements may be of different types
  **/

  public Vector getElementsByName(String name)
  {
    Vector vector = new Vector();
    Enumeration e = fElements.keys();
    while (e.hasMoreElements()){
       Element element = (Element)e.nextElement();
       if (element.getName().equals(name)) vector.addElement(element);
    }
    return vector;
  }

 

  public boolean addElement ( Element element )
  {
    if (element.getModel() != null) return false;
    fElements.put(element,element);
    return true;
  }

  public boolean removeElement ( Element element )
  {
    if (element.getModel() != this) return false;
    element.disconnectAll();
    if (fRoot == element) fRoot = null;
    return (fElements.remove(element) == element);
  }

  public Enumeration getElements ()
  {
    return fElements.elements();
  }

  public int getNumberOfElements ()
  {
    return fElements.size();
  }

  public boolean containsElement ( Element element )
  {
    return fElements.contains(element);
  }

  private Element getFirstElement ()
  {
    Enumeration e = getElements();
    return (e.hasMoreElements() ? (Element)e.nextElement() : null);
  }

  public String toString ()
  {
    return getName();
  }
}

