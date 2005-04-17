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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.env.core.common.Status;


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
  public Status getStatus()
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
