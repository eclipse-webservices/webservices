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



public class PageInfo 
{
  private String                   pageName_;
  private String                   pageTitle_;
  private WidgetContributorFactory widgetFactory_;
  
  public PageInfo( String                   pageName,
                   String                   pageTitle,
                   WidgetContributorFactory widgetFactory )
  {
    pageName_       = pageName;
    pageTitle_      = pageTitle;
    widgetFactory_  = widgetFactory;
  }   
  
  public String getPageName()
  {
    return pageName_;
  }
  
  public String getPageTitle()
  {
    return pageTitle_;
  }
  
  public WidgetContributorFactory getWidgetFactory()
  {
    return widgetFactory_;
  }
}
