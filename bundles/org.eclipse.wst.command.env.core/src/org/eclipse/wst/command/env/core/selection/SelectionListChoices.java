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

public class SelectionListChoices
{
  private SelectionList selectionList_;
  private Vector        choices_;
  private SelectionList newValueSelectionList_;
  
  /**
   * 
   * @param list     The selection list.
   * @param choices  This is a vector of SelectionListChoies and can be
   *                 null if there are no choices.  This index of list
   *                 is used to index into choices.  If index is -1 then
   *                 the newValueSelectionList is used.
   */
  public SelectionListChoices( SelectionList list, Vector choices )
  {
    selectionList_ = list;
    choices_       = choices;
    
    newValueSelectionList_ = new SelectionList( new String[0], -1 );
  }
  
  public SelectionListChoices( SelectionList list, Vector choices, SelectionList newList )
  {
    this( list, choices );
    newValueSelectionList_ = newList;
  }
  
  public SelectionList getList()
  {
    return selectionList_;
  }
  
  public Vector getChoices()
  {
    return choices_;
  }
  
  public Vector getChoicesAtLevel( int level )
  {
    SelectionListChoices choices = this;
    
    for( int index = 0; index < level; index++ )
    {
      choices = choices.getChoice();
    }
    
    return choices.getChoices();
  }
  
  public SelectionListChoices getChoice()
  {
    int                  selectionIndex = selectionList_.getIndex();
    SelectionListChoices result         = null;
    
    if( selectionIndex == -1 )
    {
      result = new SelectionListChoices( newValueSelectionList_, null );
    }
    else if( choices_ != null && choices_.size() != 0 ) 
    {
      result = (SelectionListChoices)choices_.elementAt( selectionIndex );
    }
    
    return result;
  }
  
  public SelectionListChoices getChoice(int index)
  {
    SelectionListChoices result = null;
    if (index < 0 || index >= getList().getList().length)
    {
      result = new SelectionListChoices( newValueSelectionList_, null );      
    }
    else
    {
      result = (SelectionListChoices)choices_.elementAt( index );
    }
      
    return result;
  }
}
