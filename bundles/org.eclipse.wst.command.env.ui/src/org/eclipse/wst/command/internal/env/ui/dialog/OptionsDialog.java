/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.provisional.env.core.common.Choice;


/**
 * A dialog to display one or more errors to the user, as contained in an
 * <code>Status</code> object. If an error contains additional detailed
 * information then a Details button is automatically supplied, which shows or
 * hides an error details viewer when pressed by the user.
 *  
 */
public class OptionsDialog extends MessageDialog
{
  private Choice[] choices;
  private int returnCode_;

  public OptionsDialog(
    Shell parentShell,
    String dialogTitle,
    String message,
    IStatus status,
    int displayMask,
    Choice[] choices)
  {
    super(parentShell, dialogTitle, message, status, displayMask);
    this.choices = choices;
  }

  /*
   * (non-Javadoc) This should also be overwritten Method declared on Dialog.
   */
  protected void createButtonsForButtonBar(Composite parent)
  {
    // create a button for each options
    for (int i = 0; i < choices.length; i++)
    {
      createButton(parent, choices[i].getShortcut(), choices[i].getLabel(), true);
    }
    if (status.isMultiStatus() )
    {
      detailsButton =
        createButton(
          parent,
          StatusDialogConstants.DETAILS_ID,
          IDialogConstants.SHOW_DETAILS_LABEL,
          false);
    }
  }

  protected void buttonPressed(int id)
  {
    if (id == StatusDialogConstants.DETAILS_ID)
      // was the Details button pressed?
      super.buttonPressed(id);
    else
    {
      setReturnCode(id);
      close();
    }
  }

  protected void setReturnCode(int id)
  {
    returnCode_ = id;
  }

  public int getReturnCode()
  {
    return returnCode_;
  }
  /*
   * This is one of the few methods that needs to be overwritten by the
   * subclasses. The image names can be found in the Dialog class
   */
  protected Image getDialogImage()
  {
    // create image
    switch (status.getSeverity())
    {
      case IStatus.INFO :
        return PlatformUI.getWorkbench().getDisplay().getSystemImage(SWT.ICON_INFORMATION);
      case IStatus.WARNING :
        return PlatformUI.getWorkbench().getDisplay().getSystemImage(SWT.ICON_WARNING);
      default :
        return PlatformUI.getWorkbench().getDisplay().getSystemImage(SWT.ICON_ERROR);
    }
  }

}
