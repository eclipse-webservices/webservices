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

package org.eclipse.jst.ws.internal.creation.ui.server;

import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.server.core.IServer;

public class StartServerWidgetContributor implements INamedWidgetContributor 
{
  private IServer      server_;
  private MessageUtils msgUtils_;
	  
  public StartServerWidgetContributor( IServer server )
  {
	server_ = server;
	msgUtils_ = new  MessageUtils( "org.eclipse.jst.ws.creation.ui.plugin", this );
  }
  
  public String getDescription() 
  {
	return msgUtils_.getMessage( "PAGE_DESC_WS_START_SERVER" );
  }

  public String getName() 
  {
	return "";
  }

  public String getTitle() 
  {
	return msgUtils_.getMessage( "PAGE_TITLE_WS_START_SERVER" );
  }

  public WidgetContributorFactory getWidgetContributorFactory() 
  {
	return new WidgetContributorFactory()
	       {
			 public WidgetContributor create() 
			 {
				return new StartServerWidget( server_ );
 			 }
	       };
  }
}
