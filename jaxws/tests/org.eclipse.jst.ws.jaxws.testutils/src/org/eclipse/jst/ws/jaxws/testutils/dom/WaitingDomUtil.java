/*******************************************************************************
 * Copyright (c) 2010 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.testutils.dom;

import java.text.MessageFormat;

import junit.framework.AssertionFailedError;

import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;

/**
 * Extension of {@link DomUtil} which upon searching for web service project would wait until the project gets into the model<br>
 * The utility is useful for test which implement tests which rely on the WS model
 * 
 * @author Danail Branekov
 */
public class WaitingDomUtil extends org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil
{
	/**
	 * The implementation, in contrast to its parent's one, would never return null. If the project is not found in the model, an {@link AssertionFailedError} is thrown
	 * @see super{@link #findProjectByName(IDOM, String)}
	 */
	@Override
	public IWebServiceProject findProjectByName(final IDOM dom, final String name)
	{
		final IWebServiceProject[] result = new IWebServiceProject[1];
		Assertions.waitAssert(new IWaitCondition()
		{
			public boolean checkCondition() throws ConditionCheckException
			{
				result[0] = WaitingDomUtil.super.findProjectByName(dom, name);
				return result[0] != null;
			}
		}, MessageFormat.format("Project {0} not found in WS DOM", name));

		return result[0];
	}
}
