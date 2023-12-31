/*******************************************************************************
 * Copyright (c) 2008, 2009 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 */
public class CXFCreationCorePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.jst.ws.cxf.creation.core"; //$NON-NLS-1$

    // The shared instance
    private static CXFCreationCorePlugin plugin;

    /**
     * The constructor
     */
    public CXFCreationCorePlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static CXFCreationCorePlugin getDefault() {
        return plugin;
    }

    public static void logMessage(int severity, String message) {
        CXFCreationCorePlugin.log(new Status(severity, CXFCreationCorePlugin.PLUGIN_ID, message));
    }

    public static void log(IStatus status) {
        CXFCreationCorePlugin.getDefault().getLog().log(status);
    }
    
    public static void log(Throwable exception) {
        CXFCreationCorePlugin.log(new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, 
            exception.getLocalizedMessage(), exception));
    }
}
