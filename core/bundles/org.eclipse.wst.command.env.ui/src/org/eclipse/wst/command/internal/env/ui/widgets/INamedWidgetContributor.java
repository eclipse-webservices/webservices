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


public interface INamedWidgetContributor 
{
  /**
   * 
   * @return returns the WidgetContributor for this Named widget.
   */
  public WidgetContributorFactory getWidgetContributorFactory();
  
  /**
   * 
   * @return returns the name.
   */
  public String getName ();
  
  /**
   * 
   * @return returns a description that might be displayed on
   * a wizard page.
   */
  public String getDescription ();
  
  /**
   * 
   * @return returns title that may be displayed on 
   * a wizard page.
   */
  public String getTitle();  
}
