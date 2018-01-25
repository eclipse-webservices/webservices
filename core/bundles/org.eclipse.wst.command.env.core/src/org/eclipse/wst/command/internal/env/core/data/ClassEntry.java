/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060313   130958 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.core.data;

import java.util.Vector;

public class ClassEntry
{
  // A list of getter methods for a particular class.
  public Vector getterList_;
  
  // A list of setter methods for a particular class.
  public Vector setterList_;
  
  // A list of instance objects for this class.  Only the last entry
  // should be looked at. 
  private Vector objectList_ = new Vector();
  
  // A list of Interger objects that represent an ordering of objects.
  // The number of entries in the objectList vector and the orderList vector
  // should be the same.  Each entry in the objectList vector is
  // corelated with each entry in the orderList vector.
  private Vector orderList_ = new Vector();
  
  public void addObject( Object object, int order )
  {
    objectList_.add(object);
    orderList_.add( new Integer(order) );
  }
  
  public Object getLastObject()
  {
    Object result = null;
    
    if( objectList_.size() > 0 )
    {
      result = objectList_.lastElement();
    }
      
    return result;
  }
  
  public int getLastOrder()
  {
    int result = -1;
    
    if( orderList_.size() > 0 )
    {
      result = ((Integer)orderList_.lastElement()).intValue();
    }
      
    return result;
  }
  
  public void removeObject( Object object )
  {
    int removalIndex = objectList_.indexOf(object);
    
    if( removalIndex != -1 )
    {
      objectList_.remove(removalIndex);
      orderList_.remove(removalIndex);
    }   
  }
}
