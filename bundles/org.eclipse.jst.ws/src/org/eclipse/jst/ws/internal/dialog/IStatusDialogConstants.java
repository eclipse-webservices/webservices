/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.dialog;

import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;

/**
* Constants for status dialogs
*/
public interface IStatusDialogConstants {

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2002.";

  /*
  * Button ID for the "Yes" button
  */
  public static final int YES_ID = 2;
  /*
  * Button ID for the "Yes to all" button
  */
  public static final int YES_TO_ALL_ID = 4;
  /*
  * Button ID for the "Cancel" button
  */
  public static final int CANCEL_ID = 1;
  /*
  * Button ID for the "OK" button
  */
  public static final int OK_ID = 0;
  /*
  * Button ID for the "Details" button
  */
  public static final int DETAILS_ID = 13;

  /*
  * Button label for the "Yes" button
  */
  public static final String YES_LABEL = WebServicePlugin.getMessage("%STATUS_DIALOG_YES_LABEL");
  /*
  * Button label for the "Yes to all" button
  */
  public static final String YES_TO_ALL_LABEL = WebServicePlugin.getMessage("%STATUS_DIALOG_YES_TO_ALL_LABEL");
  /*
  * Button label for the "Cancel" button
  */
  public static final String CANCEL_LABEL = WebServicePlugin.getMessage("%STATUS_DIALOG_CANCEL_LABEL");
}
