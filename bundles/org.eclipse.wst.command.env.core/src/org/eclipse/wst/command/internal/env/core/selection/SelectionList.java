/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.core.selection;

/**
 * This class stores a list strings along with a selectionIndex which
 * stores the current string that selected.  There is also a selection
 * value.  If this value is one of the strings in the the string list
 * then selectionIndex will contain the index of this string.  If it
 * is not in the list then the selectionValue string will contain this
 * value and selectionIndex will be set to -1.  This class can be
 * used to store the state data for a Combo box.
 */
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
  
  /**
   * Sets the selection value for this object.
   * 
   * @param value the value.
   */
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
  
  /*
   * @return returns the string list.
   */
  public String[] getList()
  {
    return list_;
  }
  
  /*
   * Sets the currently selected string in the list.
   */
  public void setIndex( int index )
  {
    selectionIndex_ = index;
    selectionValue_ = null;
  }
 
  /*
   * @return returns the index of the current string.  The value
   * will be -1 if the selection value is not in the string list.
   */
  public int getIndex()
  {
    return selectionIndex_;  
  }
  
  /*
   * @return returns the current string selection for this object.
   */
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
