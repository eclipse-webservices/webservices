/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.analyzer;

/**
 * Exception for Assertion Not Applicable.
 * 
 * @author gturrell
 */
public class AssertionNotApplicableException extends Exception
{

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3905523782994112564L;

	/**
   * Constructor for AssertionNotApplicableException.
   */
  public AssertionNotApplicableException()
  {
    super();
  }

  /**
   * Constructor for AssertionNotApplicableException.
   * @param arg0 the detail message.
   */
  public AssertionNotApplicableException(String arg0)
  {
    super(arg0);
  }

}
