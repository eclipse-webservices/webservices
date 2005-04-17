/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.provisional.env.core.uri;

/**
 * This class contains several static utility methods for
 * working with URIs.
 */
public class URIUtils
{
  /**
   * Copies all bytes from the old URI to the new URI.
   */
  public static void copy ( URI oldUri, URI newUri ) throws URIException
  {
  }

  /**
   * Moves one URI to another.
   */
  public static void move ( URI oldUri, URI newUri ) throws URIException
  {
  }

  /**
   * Constructs a new, temporary leaf URI.
   * The scheme of the URI may be, but is not guaranteed to be, "file:".
   */
  public static URI getTemporaryLeafURI () throws URIException
  {
    return null;
  }

  /**
   * Constructs a new, temporary folder URI.
   * The scheme of the URI may be, but is not guaranteed to be, "file:".
   */
  public static URI getTemporaryFolderURI () throws URIException
  {
    return null;
  }
}
