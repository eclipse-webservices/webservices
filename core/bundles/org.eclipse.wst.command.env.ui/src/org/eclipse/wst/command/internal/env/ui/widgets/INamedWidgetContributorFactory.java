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

package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;

public interface INamedWidgetContributorFactory 
{
  /**
   * 
   * @return returns the first named widget.
   */
  INamedWidgetContributor getFirstNamedWidget();
  
  /**
   * 
   * @param widgetContributor
   * @return returns the next named widget after the one specified.
   */
  INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor );
  
  /**
   * This method is used by extenions to register the data mappings
   * that their widgets use.
   * 
   * @param dataRegistry
   */
  public void registerDataMappings( DataMappingRegistry dataRegistry );
  
}
