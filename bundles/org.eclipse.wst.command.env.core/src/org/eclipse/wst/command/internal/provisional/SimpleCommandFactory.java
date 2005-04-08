package org.eclipse.wst.command.internal.provisional;

import java.util.Vector;

import org.eclipse.wst.command.env.core.Command;

public class SimpleCommandFactory implements ICommandFactory 
{
    private Vector commands_;
	private int    index_;
	
	public SimpleCommandFactory( Vector commands )
	{
	  commands_ = commands;
	  index_    = 0;
	}
	
	public Command getNextCommand() 
	{
		return (Command)next();
	}

	public void remove() 
	{
      throw new UnsupportedOperationException();
	}

	public boolean hasNext() 
	{
		return index_ < commands_.size();
	}

	public Object next() 
	{
		return commands_.elementAt(index_++);
	}

}
