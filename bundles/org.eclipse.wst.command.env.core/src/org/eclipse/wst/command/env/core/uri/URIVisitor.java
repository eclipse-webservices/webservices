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
package org.eclipse.wst.command.env.core.uri;

/**
 * This interface is implemented by classes that visit URIs.
 * Refer to {@link URI#visit}.
 */
public interface URIVisitor
{
  /**
   * Visits the given URI.
   * @param uri The resource to visit.
   * @return True if the resource's descendants should be visited,
   * or false if they should not be visited.
   * @throws URIException If the visit method fails.
   */
  public boolean visit ( URI uri ) throws URIException;
}
