/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsil;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.ws.parser.wsil.WWWAuthenticationException;
import org.eclipse.wst.ws.parser.wsil.WWWAuthenticationHandler;


public class DialogWWWAuthentication extends Dialog implements WWWAuthenticationHandler
{

  /*CONTEXT_ID DBAS0001 for the HTTP basic authentication user name*/
  private final String INFOPOP_DBAS_USERNAME = WebServiceConsumptionUIPlugin.ID + ".DBAS0001";

  /*CONTEXT_ID DBAS0002 for the HTTP basic authentication password*/
  private final String INFOPOP_DBAS_PASSWORD = WebServiceConsumptionUIPlugin.ID + ".DBAS0002";

  private String usernameString_;
  private String passwordString_;

  private Text username_;
  private Text password_;

  private WWWAuthenticationException wwwae_;

  public DialogWWWAuthentication(Shell shell)
  {
    super(shell);
    usernameString_ = null;
    passwordString_ = null;
    wwwae_ = null;
  }

  /**
  * Called when the Cancel button is pressed.
  */
  protected void cancelPressed()
  {
    usernameString_ = null;
    passwordString_ = null;
    super.cancelPressed();
  }

  /**
  * Called when the OK button is pressed.
  */
  protected void okPressed()
  {
    String usernameString = username_.getText();
    usernameString_ = (usernameString.length() > 0) ? usernameString : null;
    String passwordString = password_.getText();
    passwordString_ = (passwordString.length() > 0) ? passwordString : null;
    setReturnCode(Dialog.OK);
    super.okPressed();
  }

  /**
  * See {@link org.eclipse.jface.window.Window#configureShell}.
  * @param shell The shell.
  */
  protected void configureShell(Shell shell)
  {
    super.configureShell(shell);
    shell.setText(getMessage("%DIALOG_TITLE_HTTP_BASIC_AUTH"));
  }

  /** 
  * Creates the dialog area.
  * @param parent The parent composite.
  * @return The control area.
  */
  protected Control createDialogArea(Composite parent)
  {
    Composite composite = (Composite)super.createDialogArea(parent);
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
    gd.widthHint = 400;
    gd.grabExcessVerticalSpace = true;
    gd.grabExcessHorizontalSpace = true;
    composite.setLayoutData(gd);
    GridLayout gl = new GridLayout();
    gl.numColumns = 2;
    gl.verticalSpacing = 15;
    composite.setLayout(gl);

    Label label = new Label(composite, SWT.WRAP);
    label.setText(getMessage("%LABEL_URL"));
    label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    label = new Label(composite, SWT.WRAP);
    label.setText(wwwae_.getURL());
    label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    label = new Label(composite, SWT.WRAP);
    label.setText(getMessage("%LABEL_HTTP_BASIC_AUTH_USERNAME"));
    label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    username_ = new Text(composite, SWT.BORDER);
    username_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    username_.setToolTipText(getMessage("%TOOLTIP_HTTP_BASIC_AUTH_USERNAME"));
    PlatformUI.getWorkbench().getHelpSystem().setHelp(username_, INFOPOP_DBAS_USERNAME);

    label = new Label(composite, SWT.WRAP);
    label.setText(getMessage("%LABEL_HTTP_BASIC_AUTH_PASSWORD"));
    label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    password_ = new Text(composite, SWT.BORDER);
    password_.setEchoChar('*');
    password_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    password_.setToolTipText(getMessage("%TOOLTIP_HTTP_BASIC_AUTH_PASSWORD"));
    PlatformUI.getWorkbench().getHelpSystem().setHelp(username_, INFOPOP_DBAS_PASSWORD);

    return composite;
  }

  private String getMessage(String msgId)
  {
    return WebServiceConsumptionUIPlugin.getMessage(msgId);
  }

  public void handleWWWAuthentication(WWWAuthenticationException wwwae)
  {
    usernameString_ = null;
    passwordString_ = null;
    wwwae_ = wwwae;
    open();
  }

  public String getUsername()
  {
    return usernameString_;
  }

  public String getPassword()
  {
    return passwordString_;
  }
}
