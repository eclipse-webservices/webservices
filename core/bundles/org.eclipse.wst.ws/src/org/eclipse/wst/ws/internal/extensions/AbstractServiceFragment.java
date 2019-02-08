/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.extensions;

import org.eclipse.wst.command.internal.env.core.fragment.CommandFactoryFragment;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public abstract class AbstractServiceFragment extends CommandFactoryFragment
{
  protected IWebService webService_;
  protected IEnvironment environment_;
  protected IContext    context_;
  protected ISelection  selection_;
  protected String      project_;
  protected String      module_;
  protected String      earProject_;
  protected String      ear_;
    
  public AbstractServiceFragment()
  {
  }
  
  protected AbstractServiceFragment( AbstractServiceFragment fragment )
  {
	super( fragment ); 
	
	webService_  = fragment.webService_;
	environment_ = fragment.environment_;
	context_     = fragment.context_;
	selection_   = fragment.selection_;
	module_      = fragment.module_;
	ear_         = fragment.ear_;
  }
    
  public void setWebService( IWebService webService )
  {
	webService_ = webService;  
  }

  public void setEnvironment( IEnvironment environment )
  {
	environment_ = environment;
  }
  
  public void setContext( IContext context )
  {	  
	context_ = context;
  }
  
  public void setSelection( ISelection selection )
  {
	selection_ = selection;  
  }
  
  public void setProject( String project)
  {
    project_ = project;
  }
  
  public void setModule( String module )
  {
	module_ = module;
  }
  
  public void setEarProject(String earProject)
  {
    earProject_ = earProject;  
  }
  
  public void setEar( String ear )
  {
	ear_ = ear;  
  }
}
