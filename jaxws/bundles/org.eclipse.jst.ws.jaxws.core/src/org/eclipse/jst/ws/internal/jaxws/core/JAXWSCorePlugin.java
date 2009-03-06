/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author sclarke
 *
 */
public class JAXWSCorePlugin extends Plugin {
    public static final String PLUGIN_ID = "org.eclipse.jst.ws.jaxws.core"; //$NON-NLS-1$
    
    // The shared instance
    private static JAXWSCorePlugin plugin;
    
    public static JAXWSCorePlugin getDefault() {
      return plugin;
    }
    
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    public static void logMessage(int severity, String message) {
        JAXWSCorePlugin.log(new Status(severity, JAXWSCorePlugin.PLUGIN_ID, message));
    }

    public static void log(IStatus status) {
        JAXWSCorePlugin.getDefault().getLog().log(status);
    }

    public static void log(Throwable exception) {
        JAXWSCorePlugin.log(new Status(IStatus.ERROR, JAXWSCorePlugin.PLUGIN_ID, exception
                .getLocalizedMessage(), exception));
    }
}
