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

package org.eclipse.jst.ws.internal.consumption.ui.server;

import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.server.core.IServer;

public class StartClientWidgetContributor implements INamedWidgetContributor 
{
  private IServer      server_;
	  
  public StartClientWidgetContributor( IServer server )
  {
	server_ = server;
  }
  
  public String getDescription() 
  {
	return ConsumptionUIMessages.PAGE_DESC_WS_START_SERVER;
  }

  public String getName() 
  {
	return "";
  }

  public String getTitle() 
  {
	return ConsumptionUIMessages.PAGE_TITLE_WS_START_SERVER;
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
