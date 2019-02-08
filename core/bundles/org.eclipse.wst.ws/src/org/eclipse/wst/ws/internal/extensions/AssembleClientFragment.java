/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.extensions;

import java.util.Vector;

import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;

public class AssembleClientFragment extends AbstractClientFragment 
{
  public AssembleClientFragment()
  {
  }
  
  protected AssembleClientFragment( AssembleClientFragment fragment )
  {
	super( fragment );  
  }
  
  public Object clone() 
  {
	return new AssembleClientFragment();
  }

  public ICommandFactory getICommandFactory() 
  {
	ICommandFactory factory = null;
	
	if( webServiceClient_ == null || context_ == null || !context_.getAssemble())
	{
	  factory = new SimpleCommandFactory( new Vector() );
	}
	else
	{
	  factory = webServiceClient_.assemble( environment_, context_, selection_, project_, earProject_);	
	}
	
	return factory;
  }
}
