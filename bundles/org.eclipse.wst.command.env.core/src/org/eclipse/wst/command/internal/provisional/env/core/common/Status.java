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
package org.eclipse.wst.command.internal.provisional.env.core.common;

/**
 * Carries status information.
 */
public interface Status
{
  /**
   * Status: Everything is fine, and there are no messages or details.
   */
  public static final int OK = 0;

  /**
   * Status: Everything is fine, but there is information to report.
   */
  public static final int INFO = 1;

  /**
   * Status: Things are working, but possibly not as expected.
   */
  public static final int WARNING = 2;

  /**
   * Status: Blammo.
   */
  public static final int ERROR = 4;

  /**
   * Returns a non-translated application specific identifier.
   * May return null.
   */
  public String getId ();

  /**
   * Returns a translated (locale-sensitive) message.
   * May return null.
   */
  public String getMessage ();

  /**
   * Returns the severity.
   */
  public int getSeverity ();

  /**
   * Returns an exception (throwable) behind the status, if any.
   * May return null.
   */
  public Throwable getThrowable ();

  /**
   * Returns true if this status object has child status objects.
   */
  public boolean hasChildren ();

  /**
   * Returns the immediate child Status objects of this object.
   * Never returns null, but may return a zero-length array.
   */
  public Status[] getChildren ();

  /**
   * Returns true if this status' severity matches the severity
   * mask passed in. Strictly speaking:
   * this.matches(that) == (this.getSeverity() | that != 0)
   */
  public boolean matches ( int mask );
}
