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
package org.eclipse.wst.command.env.core.selection;

import java.util.Vector;

public class DynamicList 
{
  private Vector stringList_ = new Vector();
  private Vector dynamicListVectorList_ = new Vector();
  
  public void add( String[] values, Object target )
  {
    DynamicList currentList = this;
    
    for( int index = 0; index < values.length; index++ )
    {
      String value      = values[index];
      int    length     = currentList.stringList_.size();
      int    foundIndex = -1;
      
      // Find the string in the current list.
      for( int searchIndex = 0; searchIndex < length; searchIndex++ )
      {
        String string = (String)currentList.stringList_.elementAt( searchIndex ); 
        
        if( string.equals( value ) ) 
        {
          foundIndex = searchIndex;
          break;
        }
      }
      
      if( foundIndex == -1 )
      {
        // We have a new string so add it to the list.
        currentList.stringList_.add( value );
        currentList.dynamicListVectorList_.add( new DynamicList() );
        foundIndex = length;
      }
      
      currentList = (DynamicList)currentList.dynamicListVectorList_.elementAt( foundIndex );       
    }
    
    currentList.dynamicListVectorList_.add( target );
  }
  
  public SelectionListChoices toListChoices()
  {
    SelectionList list   = new SelectionList( (String[])stringList_.toArray( new String[0] ), 0);
    int           length = dynamicListVectorList_.size();
    Vector        vector = new Vector(); 
    
    for( int index = 0; index < length; index++ )
    {
      Object obj = dynamicListVectorList_.elementAt(index);
      
      if( obj instanceof DynamicList )
      {
        vector.add( ((DynamicList)obj).toListChoices() );
      }
      else
      {
        vector.add( obj );
      }
    }
    
    return new SelectionListChoices( list, vector );
  }
}
