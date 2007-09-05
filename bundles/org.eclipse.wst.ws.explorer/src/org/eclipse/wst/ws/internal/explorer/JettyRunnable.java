/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer;

import org.eclipse.help.internal.appserver.AppserverPlugin;


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

	public boolean isJettyStarted() {
		try {
			return AppserverPlugin.getDefault().getAppServer().isRunning();
		} catch (Throwable t) {
			return false;
		}
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
