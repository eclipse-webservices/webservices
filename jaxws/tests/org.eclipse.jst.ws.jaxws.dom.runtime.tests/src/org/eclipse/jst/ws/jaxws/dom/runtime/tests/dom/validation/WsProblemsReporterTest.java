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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.util.Collection;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.WsProblemsReporter;

/**
 * This class tests whether the {@link WsProblemsReporter} is correctly registered in the extensions registry. The very implementation of the reporter is performed in the {@link ValidationTestsSetUp} extenders
 */
public class WsProblemsReporterTest extends TestCase
{
	public void testCorrectlyRegistered()
	{
		final Collection<IConfigurationElement> registeredInstances = findWsProblemsReporterListeners();
		assertEquals("One registered instance expected", 1, registeredInstances.size());

		final IConfigurationElement[] clientContexts = registeredInstances.iterator().next().getChildren("clientContext");
		assertEquals("One client context expected", 1, clientContexts.length);
		assertEquals("Unexpected client context", "org.eclipse.jst.ws.jaxws.dom.jee5.domContext", clientContexts[0].getAttribute("id"));
	}

	private Collection<IConfigurationElement> findWsProblemsReporterListeners()
	{
		final Collection<IConfigurationElement> result = new LinkedList<IConfigurationElement>();
		for (IConfigurationElement e : findRegisteredListeners())
		{
			if (e.getAttribute("class").equals(WsProblemsReporter.class.getName()))
			{
				result.add(e);
			}
		}

		return result;
	}

	private Collection<IConfigurationElement> findRegisteredListeners()
	{
		final Collection<IConfigurationElement> result = new LinkedList<IConfigurationElement>();

		final IExtensionPoint xp = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.emf.validation.validationListeners");
		for (final IExtension ext : xp.getExtensions())
		{
			for (IConfigurationElement configElement : ext.getConfigurationElements())
			{
				if (configElement.getName().equals("listener"))
				{
					result.add(configElement);
				}
			}
		}

		return result;
	}

}
