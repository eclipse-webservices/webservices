/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.actions;

// A class for reporting form input exceptions.
public class FormInputException extends Exception
{
  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257846601685938742L;

public FormInputException(String msg)
  {
    super(msg);
  }
}
