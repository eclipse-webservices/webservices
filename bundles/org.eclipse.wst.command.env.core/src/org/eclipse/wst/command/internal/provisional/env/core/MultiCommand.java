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
package org.eclipse.wst.command.internal.provisional.env.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


/**
 * This command can be used to group multiple Commands together
 * into a single sequence list of commands to execute.
 *
 *
 */
public class MultiCommand extends SimpleCommand
{
	  
  protected String name        = null;
  protected String description = null;

  private List            commands_      = new LinkedList();
  private Command[]       commandsArray_ = null;

  /**
   * Creates a MultiCommand with no name, description, or list of commands.
   *
   */
  public MultiCommand ()
  {
  }

  /**
   * Creates a MutilCommands with an empty list of commands.
   * @param name The name of the command.
   * @param description A description of the command.
   */
  public MultiCommand ( String name, String description )
  {
    this.name = name;
    this.description = description;
  }

  /**
   * Creates a MultiCommand
   * @param name the name of the command.
   * @param description a description of the command.
   * @param commands an initial list of the commands to execute.
   */
  public MultiCommand ( String name, String description, Command[] commands )
  {
    this.name = name;
    this.description = description;
    commands_.addAll(Arrays.asList(commands));
  }

  /**
   * Adds a command to the list of commands to execute.
   * @param command the command to add.
   */
  public void add ( Command command )
  {
    commandsArray_ = null;
    commands_.add(command);
  }

  /**
   *  
   * @see org.eclipse.wst.command.internal.provisional.env.core.Command#getName()
   */
  public String getName ()
  {
    return name != null ? name : (commands_.size() > 0 ? ((Command)commands_.get(0)).getName() : null);
  }

  /**
   * 
   * @see org.eclipse.wst.command.internal.provisional.env.core.Command#getDescription()
   */
  public String getDescription ()
  {
    return description != null ? description : (commands_.size() > 0 ? ((Command)commands_.get(0)).getDescription() : null);
  }

  /**
   * Executes the list of commands in the MultiCommand.
   */
  public Status execute ( Environment environment )
  {
    Status status = null;
    Command[] commands = commandsArray();
    for (int i=0; i<commands.length; i++)
    {
      status = commands[i].execute(environment);
      
      if (status != null && status.matches(Status.ERROR))
      {
        for (i--; i>=0; i--)
        {
          commands[i].undo(environment);
        }
        return status;
      }
    }
    return status;
  }

  /**
   * Indicates if this command is undoable.  If one of the commands
   * in the list is undoable the entire list is considered to be 
   * undoable.
   */
  public boolean isUndoable ()
  {
    ListIterator i = commands_.listIterator();
    while (i.hasPrevious())
    {
      if (!((Command)i.previous()).isUndoable())
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Undos this command if it can.
   */
  public Status undo ( Environment environment )
  {
    Status status = null;
    ListIterator i = commands_.listIterator();
    while (i.hasPrevious())
    {
      status = ((Command)i.previous()).undo(environment);
    }
    return status;
  }

  /**
   * Returns whether this command is redoable or not.
   */
  public boolean isRedoable ()
  {
    ListIterator i = commands_.listIterator();
    while (i.hasNext())
    {
      if (!((Command)i.next()).isRedoable())
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Reexecutes this command.
   */
  public Status redo ( Environment environment )
  {
    Status status = null;
    Command[] commands = commandsArray();
    for (int i=0; i<commands.length; i++)
    {
      status = commands[i].redo(environment);
      if (status != null && status.matches(Status.ERROR))
      {
        for (i--; i>=0; i--)
        {
          commands[i].undo(environment);
        }
        return status;
      }
    }
    return status;
  }
  
  private Command[] commandsArray ()
  {
    if (commandsArray_ == null)
    {
      commandsArray_ = (Command[])commands_.toArray(new Command[0]);
    }
    return commandsArray_;
  }
}
