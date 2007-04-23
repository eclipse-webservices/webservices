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

import org.eclipse.wst.command.internal.env.core.data.Transformer;

/**
 * This transformer class selects a string at a particular level
 * in a SelectionListChoices class.
 */
public class ChoicesToString implements Transformer
{
  private int level_;
  
  public ChoicesToString( int level )
  {
    level_ = level;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.data.Transformer#transform(java.lang.Object)
   */
  public Object transform( Object value )
  {
    SelectionListChoices choices = (SelectionListChoices)value;
    
    for( int index = 0; index < level_; index++ )
    {
      choices = choices.getChoice();
    }
    
    return choices.getList().getSelection();
  }

  public SelectionList transform( SelectionListChoices choices )
  {
    return (SelectionList)transform( (Object)choices );
  }
}
