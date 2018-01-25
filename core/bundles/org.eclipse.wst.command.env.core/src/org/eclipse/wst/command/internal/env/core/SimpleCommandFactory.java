/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.core;

import java.util.Vector;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


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
