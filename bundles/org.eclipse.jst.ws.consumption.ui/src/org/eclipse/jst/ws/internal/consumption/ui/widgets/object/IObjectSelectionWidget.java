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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataContributor;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

/**
 * This interface defines extra methods that Object Selection Widgets
 * need to implement.  Object Selection Widgets are displayed on page 2
 * of the Web services wizard.
 */
public interface IObjectSelectionWidget extends WidgetDataContributor
{
  /**
   * The framework will call this method to set the initial selection
   * value that the user has specified.
   * 
   * @param initialSelection the initial selection the user specified.
   */
  public void setInitialSelection(IStructuredSelection initialSelection);
  
  /**
   * The intent of the Object Selection Widget is that the user has
   * the opportunity change/find the object that they want to turn into
   * a Web service.
   * 
   * @return returns the an IStructuredSelection object that the user
   * want to turn into a Web service.  This object may be different than
   * the initialSelection passed in by the setInitialSelection method.
   */
  public IStructuredSelection getObjectSelection();
  
  /**
   * Validates whether the objectSelection parameter is valid for
   * this Object selection widget.  An error status should be returned
   * if this object is not value.  Otherwise, and ok status should be
   * returned
   * 
   * @param objectSelection the object to be validated.
   * @return the status of the validation.
   */
  public Status validateSelection(IStructuredSelection objectSelection);
  
  /**
   * 
   * @return returns the IProject that contains the selection
   * object that the user has chosen with this widget.  The value can be 
   * null if there is no IProject for this object.
   */
  public IProject getProject();
  
  /**
   * 
   * @return returns the name of the IVirtualComponent that contains the selection
   * object that the user has chosen with this widget.  The value can be 
   * null if there is no IVirtualComponent for this object
   */
  public String getComponentName();  
}