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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;


/**
 * This interface provides a way for developers to create validatable widgets
 * that can be used in many different Eclipse UI contexts. (ie. in a Windows, Diaglogs,
 * wizard pages, etc)
 *
 * The main functionality this interface has over just a regular Eclipse Composite
 * is that it provides a mechanism for validating the Widget.  When validating
 * there are two aspects to keep in mind.  The first aspect deals with WHEN validation
 * occurs and the second aspect deals with WHAT to do for a validation event.
 * 
 * The first aspect of triggering validation events is handled by the statusListener
 * parameter of the addControls method.  If a particular control that is contributed can
 * affect the validity of this WidgetContributor then the statusListener object
 * should be added to the listener list of that control.  For example, if the text in
 * a Text widget can affect the validity of this WidgetContributor then you would
 * add the statusListener to the Text widgets Modify listener list:
 * 
 * 
 *   Text someText = new Text( parent, SWT.NONE );
 *   someText.addListener( SWT.Modify, statusListener );
 * 
 * Note: if you want to force validation of this widget you can call
 *       the statusListener.handleEvent(null) method directly.  This
 *       is not the prefered may to trigger validation, however.
 *       
 * The second aspect of valiation is what to do when the validation event occurs.
 * This is handled by getStatus method.  Any code that ensures that this widget is
 * valid should be put in the getStatus implementation. 
 * 
 * 
*/
public interface WidgetContributor 
{
  /**
   * This method should be used to implement the UI contributions for this widget.
   * 
   * @param parent The composite parent that sub widgets should use.
   * @param statusListener This listener should be added to any widget that can
   *                       affect the validity of this WidgetContributor.
   * @return returns a WidgetDataEvents object.  This object is used by the
   *         framework to internalize and externalize data for this widget.
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener );
  
  /**
   * This method should implement validation code for this WidgetContributor.
   * 
   * @return returns a status object indicating the validity of this widget.
   *         If the status severity is ERROR then this widget is considered to
   *         be invalid.  For any other status severity or if the status returned
   *         is null this widget is considered to be valid.
   */
  public IStatus getStatus();
}
