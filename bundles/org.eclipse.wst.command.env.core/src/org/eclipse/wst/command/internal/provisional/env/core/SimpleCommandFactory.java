package org.eclipse.wst.command.internal.provisional.env.core;

import java.util.Vector;


public class SimpleCommandFactory implements ICommandFactory 
{
    private Vector commands_;
	private int    index_;
	
	public SimpleCommandFactory( Vector commands )
	{
	  commands_ = commands;
	  index_    = 0;
	}
	
	public AbstractDataModelOperation getNextCommand() 
	{
		return (AbstractDataModelOperation)next();
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
