/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import java.util.Hashtable;



public class SimpleWidgetRegistry implements WidgetRegistry
{
  private Hashtable table_ = new Hashtable();
  
  public void add( String fragmentId,
                   String pageName,
                   String pageTitle,
                   WidgetContributorFactory widgetFactory )
  {
  	PageInfo entry = new PageInfo( pageName, pageTitle, widgetFactory );
  	table_.put( fragmentId, entry );
  }                   
  
  public PageInfo getPageInfo( String fragmentId )
  {
    return (PageInfo)table_.get( fragmentId );
  }
}
