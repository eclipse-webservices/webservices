/*******************************************************************************
 * Copyright (c) 2002, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer;

public class JettyRunnable {

	private static JettyRunnable jettyRunnable_;

	private JettyRunnable() {
	}

	public static JettyRunnable getJettyRunnable() {
		if (jettyRunnable_ == null) {
			jettyRunnable_ = new JettyRunnable();
			jettyRunnable_.init();
		}
		return jettyRunnable_;
	}

	public int getJettyPort() {
		return WebappManager.getPort();
	}

	public String getJettyHost() {
		return WebappManager.getHost();
	}

	
	private void init() {
		try {
			WebappManager.start("wse");
		} catch (Throwable t) {
		}
	}
}
