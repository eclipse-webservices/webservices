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
package org.eclipse.wst.wsi.internal.core.report;

import java.util.TreeMap;

/**
 * Result for a specific test assertion.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public interface EntryResult
{
  /**
   * Add assertion result.
   * @param assertionResult assertion result.
   */
  public void addAssertionResult(AssertionResult assertionResult);

  /**
   * Get assertion results.
   * @return assertion results.
   */
  public TreeMap getAssertionResultList();

  /**
   * Get the assertion result for a specific test assertion.
   * @param assertionId test assertion id.
   * @return the assertion result for the specified test assertion ID.
   *         If an assertion result was not found, then null is returned.
   */
  public AssertionResult getAssertionResult(String assertionId);
}
