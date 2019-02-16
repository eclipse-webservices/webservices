/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
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
package org.eclipse.jst.ws.jaxws.testutils;

import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;

/**
 * Interface which defines a condition upon which the execution can continue 
 * @author Danail Branekov
 */
public interface IWaitCondition
{
	/**
	 * Method which checks whether a given condition is met
	 * @return <code>true</code> in case the condition is met, <code>false</code> otherwise
	 * @throws ConditionCheckException when implementors decide that they cannot deal with a given exceptional situation
	 */
	public boolean checkCondition() throws ConditionCheckException;
}
