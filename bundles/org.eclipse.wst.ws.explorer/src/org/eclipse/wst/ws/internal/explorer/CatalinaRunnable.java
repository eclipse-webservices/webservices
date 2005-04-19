/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer;

import org.eclipse.core.runtime.Path;
import org.eclipse.help.internal.appserver.AppserverPlugin;
import org.eclipse.help.internal.appserver.WebappManager;

public class CatalinaRunnable {

	private static CatalinaRunnable catalinaRunnable_;

	private CatalinaRunnable() {
	}

	public static CatalinaRunnable getCatalinaRunnable() {
		if (catalinaRunnable_ == null) {
			catalinaRunnable_ = new CatalinaRunnable();
			catalinaRunnable_.init();
		}
		return catalinaRunnable_;
	}

	public boolean isTomcatStarted() {
		try {
			return AppserverPlugin.getDefault().getAppServer().isRunning();
		} catch (Throwable t) {
			return false;
		}
	}

	public int getTomcatPort() {
		return WebappManager.getPort();
	}

	private void init() {
		WSExplorer wsExplorer = WSExplorer.getInstance();
		String ctxt = wsExplorer.getContextName();
		String pluginID = wsExplorer.getParentPluginID();
		String warLocation = wsExplorer.getWARLocation();
		String webappLocation = wsExplorer.getWebAppLocation();
		try {
			if (warLocation != null)
				WebappManager.start(ctxt, pluginID, new Path(warLocation));
			else
				WebappManager.start(ctxt, pluginID, new Path(webappLocation));
		} catch (Throwable t) {
		}
	}
}