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
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.extensions;

import java.util.Vector;

import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;

public class DeployClientFragment extends AbstractClientFragment 
{
  public DeployClientFragment()
  {
  }
  
  protected DeployClientFragment( DeployClientFragment fragment )
  {
	super( fragment );  
  }
  
  public Object clone() 
  {
	return new DeployClientFragment();
  }

  public ICommandFactory getICommandFactory() 
  {
	ICommandFactory factory = null;
	
	if( webServiceClient_ == null || context_ == null || !context_.getDeploy())
	{
	  factory = new SimpleCommandFactory( new Vector() );
	}
	else
	{
	  factory = webServiceClient_.deploy( environment_, context_, selection_, project_, earProject_);	
	}
	
	return factory;
  }
}
