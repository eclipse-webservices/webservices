/*******************************************************************************
 * Copyright (c) 2005, 2019 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.parser.wsil;

import org.eclipse.osgi.util.NLS;

public class WSILMessages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.wst.ws.internal.parser.wsil.wsil"; //$NON-NLS-1$
  
  static
  {
    NLS.initializeMessages(BUNDLE_NAME, WSILMessages.class);
  }

  public static String MSG_ERROR_INVALID_ARGUMENTS;
  public static String MSG_ERROR_ILLEGAL_ARGUMENTS;
  public static String MSG_ERROR_INVALID_WSDL_URI;
  public static String MSG_ERROR_INVALID_WSIL_URI;
  public static String MSG_ERROR_UNRESOLVABLE_WSDL;
  public static String MSG_ERROR_UNEXPECTED_EXCEPTION;
  public static String MSG_ERROR_MALFORMED_WSDL;
  public static String MSG_ERROR_WRITE_WSIL;
}