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

package org.eclipse.wst.ws.internal.extensions;

import java.util.Vector;

import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.wsrt.IContext;

public class RunClientFragment extends AbstractClientFragment 
{
	private IContext context_;
	
  public RunClientFragment()
  {
  }
  
  protected RunClientFragment( RunClientFragment fragment )
  {
	super( fragment );  
  }
  
  public Object clone() 
  {
	return new RunClientFragment();
  }

  public ICommandFactory getICommandFactory() 
  {
	ICommandFactory factory = null;
	
	if( webServiceClient_ == null || !context_.getRun())
	{
	  factory = new SimpleCommandFactory( new Vector() );
	}
	else
	{
	  factory = webServiceClient_.run( environment_, context_, selection_, project_, earProject_);	
	}
	
	return factory;
  }
  
  public void setContext(IContext context)
  {
	  context_=context;
  }
}