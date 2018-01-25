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
package org.eclipse.jst.ws.annotations.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * 
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 *
 */
public class AnnotationsCorePlugin extends Plugin {
    public static final String PLUGIN_ID = "org.eclipse.jst.ws.annotations.core"; //$NON-NLS-1$
    
    // The shared instance
    private static AnnotationsCorePlugin plugin;
    
    public static AnnotationsCorePlugin getDefault() {
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
        AnnotationsCorePlugin.log(new Status(severity, AnnotationsCorePlugin.PLUGIN_ID, message));
    }

    public static void log(IStatus status) {
        AnnotationsCorePlugin.getDefault().getLog().log(status);
    }

    public static void log(Throwable exception) {
        AnnotationsCorePlugin.log(new Status(IStatus.ERROR, AnnotationsCorePlugin.PLUGIN_ID, exception
                .getLocalizedMessage(), exception));
    }
}
