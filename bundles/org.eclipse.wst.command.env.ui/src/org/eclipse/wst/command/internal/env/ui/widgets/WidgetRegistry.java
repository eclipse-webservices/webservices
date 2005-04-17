/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

/**
 * This interface defines how WidgetContributors are made known to the
 * dynamic wizard framework.  This widget will be displayed as a wizard page
 * with the name and title specified in the add method.  The fragmentId is 
 * used to associate this widget with a particular CommandFragment.  When
 * a CommandFragment with this fragmentId is traversed the corresponding
 * WidgetContributor will be displayed.
 *
 */
public interface WidgetRegistry
{
  /**
   * Adds a WidgetContributorFactory to the framework.  This factory
   * is used to get the WidgetContributor.  This WidgetContributor
   * will be used to render the wizard page.
   * 
   * @param fragmentId the CommandFragment id that this WidgetContributor is associated with.
   * @param pageName the name of this wizard page.
   * @param pageTitle the title of this wizard page.
   * @param widgetFactory the factory which will create the WidgetContributor.
   */
  public void add( String fragmentId,
				   String pageName,
				   String pageTitle,
				   WidgetContributorFactory widgetFactory );
  
}
