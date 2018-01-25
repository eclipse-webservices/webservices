/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

public interface IObjectSelectionLaunchable {
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
	  public IStatus validateSelection(IStructuredSelection objectSelection);
	 
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
	  
	  /**
	   * Called to launch a dialog which allows the user to make an object selection.
	   * This is an alternative to implementing IObjectSelectionWidget which can be used by the dialog framework.
	   * The implementation of this method should call the necessary getters on the dialog to update the 
	   * object selection. 
	   * 
	   * @return the status of completing the dialog launch
	   */
	  public int launch(Shell shell);
	  
	  /**
	   * 
	   * @return returns the string to displayed in the UI for the selected object
	   */
	  public  String getObjectSelectionDisplayableString();
	  
	  /**
	   * 
	   * @param s A string representation of the object selection
	   * @return true if the string represents a valid object selection.
       *  The implementation of this method should set the widget object selection
	   *  according to the input string so that a subsequent call to getObjectSelection
	   *  will retrieve the correct object.
	   */  
	  public boolean validate(String s);
}
