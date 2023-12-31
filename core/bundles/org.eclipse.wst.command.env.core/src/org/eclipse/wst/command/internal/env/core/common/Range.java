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
package org.eclipse.wst.command.internal.env.core.common;

import org.eclipse.wst.common.environment.uri.IURI;

/**
 * Carries position information within a resource.
 */
public interface Range
{
  public static final int UNKNOWN = -1;

  /**
   * Returns the identifier of the resource the range applies to.
   */
  public IURI getURI ();

  /**
   * Returns the index, zero-indexed, of the first line
   * of the range.
   */
  public int getStartingLineNumber ();

  /**
   * Returns the index, zero-indexed, of the first character
   * of the range relative to the beginning of the line.
   */
  public int getStartingCharNumberInLine ();

  /**
   * Returns the index, zero-indexed, of the first character
   * of the range relative to the beginning of the file.
   */
  public int getStartingCharNumberInURI ();

  /**
   * Returns the index, zero-indexed, of the last line
   * of the range.
   */
  public int getEndingLineNumber ();

  /**
   * Returns the offset, zero-indexed, of the last character
   * of the range relative to the beginning of the line.
   */
  public int getEndingCharNumberInLine ();

  /**
   * Returns the index, zero-indexed, of the last character
   * of the range relative to the beginning of the file.
   */
  public int getEndingCharNumberInURI ();
}
