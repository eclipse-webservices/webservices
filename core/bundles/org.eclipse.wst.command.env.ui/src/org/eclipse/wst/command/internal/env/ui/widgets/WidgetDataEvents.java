/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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

/**
 * This interface provides a way for internalizing and externalizing state
 * data in a WidgetContributor.  The internalize method will be called
 * before the widget is displayed so that it can set the default data
 * for that widget.  The externalize method provides a way of getting data
 * from the widget after it is displayed.
 *
 */
public interface WidgetDataEvents 
{
  /**
   * Implementations should set widget state data here.
   *
   */
  public void internalize();
  
  /**
   * Implemenations should get widget state data here.
   *
   */
  public void externalize();  
}
