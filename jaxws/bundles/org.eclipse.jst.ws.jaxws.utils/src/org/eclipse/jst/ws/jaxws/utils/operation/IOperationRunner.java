/*******************************************************************************
 * Copyright (c) 2011 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
