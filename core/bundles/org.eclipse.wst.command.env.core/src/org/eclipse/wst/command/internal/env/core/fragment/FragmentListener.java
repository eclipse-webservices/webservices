/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.wst.command.internal.env.core.fragment;

/**
 * This interface should be implement for code that needs to listen to fragments
 * that are being traverse by the CommandFragmentEngine.
 *
 */
public interface FragmentListener 
{
  /*
   * Notifies this listener that a commandFragment is being visited during
   * a traversal.
   */
  public boolean notify( CommandFragment commandFragment );
}
