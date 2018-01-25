/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.parser;

import org.eclipse.osgi.util.NLS;

public class PluginMessages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.wst.ws.parser.plugin"; //$NON-NLS-1$
  
  static
  {
    NLS.initializeMessages(BUNDLE_NAME, PluginMessages.class);
  }

  public static String PUBLICUDDIREGISTRYTYPE_NAME_SAP;
  public static String PUBLICUDDIREGISTRYTYPE_NAME_SAP_TEST;
  public static String PUBLICUDDIREGISTRYTYPE_NAME_XMETHODS;
  public static String PUBLICUDDIREGISTRYTYPE_NAME_NTTCOMM;
}