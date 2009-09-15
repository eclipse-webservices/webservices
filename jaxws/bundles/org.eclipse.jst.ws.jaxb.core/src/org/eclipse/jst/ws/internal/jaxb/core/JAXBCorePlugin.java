/*******************************************************************************
 * Copyright (c) 2009 Progress Software, Shane Clarke
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation for JAXWS
 *    Oisin Hurley - adapted for JAXB
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxb.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class JAXBCorePlugin extends Plugin {
    public static final String PLUGIN_ID = "org.eclipse.jst.ws.jaxb.core"; //$NON-NLS-1$
    
    // The shared instance
    private static JAXBCorePlugin plugin;
    
    public static JAXBCorePlugin getDefault() {
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
        JAXBCorePlugin.log(new Status(severity, JAXBCorePlugin.PLUGIN_ID, message));
    }

    public static void log(IStatus status) {
        JAXBCorePlugin.getDefault().getLog().log(status);
    }

    public static void log(Throwable exception) {
        JAXBCorePlugin.log(new Status(IStatus.ERROR, JAXBCorePlugin.PLUGIN_ID, exception
                .getLocalizedMessage(), exception));
    }
}
