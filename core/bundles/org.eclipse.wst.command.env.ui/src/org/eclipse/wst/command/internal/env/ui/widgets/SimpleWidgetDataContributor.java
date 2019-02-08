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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;


/**
 * This class provides a simple WidgetDataContributor class that concrete 
 * WidgetDataContributors can subclass.
 *
 */
public class SimpleWidgetDataContributor implements WidgetDataContributor
{  
  /**
   * @see org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    return this;
  }

  /**
   * @see org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor#getStatus()
   */
  public IStatus getStatus()
  {
    return null;
  }

  /**
   * @see org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents#externalize()
   */
  public void externalize()
  {
  }

  /**
   * @see org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents#internalize()
   */
  public void internalize()
  {
  }
}
