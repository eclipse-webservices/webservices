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

public class SelectionList
{
  private int      selectionIndex_;
  private String[] list_;
  private String   selectionValue_;
  
  public SelectionList( String[] list, int selectionIndex )
  {
    list_           = list;
    selectionIndex_ = selectionIndex;
  }
  
  public void setSelectionValue( String value )
  {
    selectionValue_ = value;  
    selectionIndex_ = -1;
    
    for( int index = 0; index < list_.length; index++ )
    {
      if( value.equals( list_[index] ) )
      {
        selectionIndex_ = index;
        break;
      }
    }
  } 
  
  public String[] getList()
  {
    return list_;
  }
  
  public void setIndex( int index )
  {
    selectionIndex_ = index;
    selectionValue_ = null;
  }
 
  public int getIndex()
  {
    return selectionIndex_;  
  }
  
  public String getSelection()
  {
    if( selectionValue_ != null ) return selectionValue_;
    
    if( selectionIndex_ == -1 || selectionIndex_ > list_.length-1 )
    {
      return "";
    }
    else
    {
      return list_[ selectionIndex_ ];
    }
  }
}
