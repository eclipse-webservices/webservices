/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.env.core;

import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;

/**
 * This is a simple implementation of the Command interface that subclass
 * can extend.
 *
 */
public class SimpleCommand implements Command
{
  private String name_;
  private String description_;
  
  public SimpleCommand( String name, String description )
  {
    name_        = name;
    description_ = description;
  }
  
  public SimpleCommand()
  {  
    name_        = "";
    description_ = "";
  }
    
  /**
   * Sets the name of the command.
   * @param name the name.
   */
  public void setName( String name )
  {
    name_ = name;  
  }
  
  /**
   * Sets the description of the command.
   * @param description the description.
   */
  public void setDescription( String description )
  {
    description_ = description;
  }
  
  /**
   * @see org.eclipse.env.command.Command#execute(org.eclipse.env.common.Environment)
   */
  public Status execute(Environment environment)
  {
    return new SimpleStatus( "" );
  }
  
  /**
   * @see org.eclipse.env.command.Command#getDescription()
   */
  public String getDescription()
  {
    return description_;
  }

  /**)
   * @see org.eclipse.env.command.Command#getName()
   */
  public String getName()
  {
    return name_;
  }
  
  /**)
   * @see org.eclipse.env.command.Command#isRedoable()
   */
  public boolean isRedoable()
  {
    return false;
  }

  /**
   * @see org.eclipse.env.command.Command#isUndoable()
   */
  public boolean isUndoable()
  {
    return false;
  }

  /**
   * @see org.eclipse.env.command.Command#redo(org.eclipse.env.common.Environment)
   */
  public Status redo(Environment environment)
  {
    return new SimpleStatus( "" );
  }

  /**
   * @see org.eclipse.env.command.Command#undo(org.eclipse.env.common.Environment)
   */
  public Status undo(Environment environment)
  {
    return new SimpleStatus( "" );
  }
}
