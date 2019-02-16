/*******************************************************************************
 * Copyright (c) 2011 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.operation;

import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * Utility interface for running operations
 * 
 * @author Danail Branekov
 */
public interface IOperationRunner
{
	/**
	 * Runs the operation specified
	 * 
	 * @param runnable
	 *            the operation to run
	 */
	public void run(final IRunnableWithProgress runnable);
}
