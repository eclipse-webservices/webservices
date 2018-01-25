/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jst.ws.internal.cxf.core.context.Java2WSPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.context.WSDL2JavaPersistentContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The activator class controls the plug-in life cycle
 * 
 */
public class CXFCorePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.jst.ws.cxf.core"; //$NON-NLS-1$

    //CXF Classpath container ID
    public static final String CXF_CLASSPATH_CONTAINER_ID = "org.eclipse.jst.ws.cxf.core.CXF_CLASSPATH_CONTAINER"; //$NON-NLS-1$

    public static final String CXF_VERSION_2_0 = "2.0"; //$NON-NLS-1$

    public static final String CXF_VERSION_2_1 = "2.1"; //$NON-NLS-1$

    // The shared instance
    private static CXFCorePlugin plugin;

    private Java2WSPersistentContext java2WSContext;
    private WSDL2JavaPersistentContext wsdl2JavaContext;

    private Version currentRuntimeVersion;

    /**
     * The constructor
     */
    public CXFCorePlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static CXFCorePlugin getDefault() {
        return plugin;
    }

    public Java2WSPersistentContext getJava2WSContext() {
        if (java2WSContext == null) {
            java2WSContext = new Java2WSPersistentContext();
            java2WSContext.load();
        }
        return java2WSContext;
    }

    public WSDL2JavaPersistentContext getWSDL2JavaContext() {
        if (wsdl2JavaContext == null) {
            wsdl2JavaContext = new WSDL2JavaPersistentContext();
            wsdl2JavaContext.load();
        }
        return wsdl2JavaContext;
    }

    public Version getCurrentRuntimeVersion() {
        //if (currentRuntimeVersion == null) {
        String cxfRuntimeVersion = CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeVersion();
        if (cxfRuntimeVersion.length() == 0) {
            cxfRuntimeVersion = "0.0.0";
        }
        this.currentRuntimeVersion = new Version(cxfRuntimeVersion);
        //}
        return currentRuntimeVersion;
    }

    public void setCurrentRuntimeVersion(Version version) {
        this.currentRuntimeVersion = version;
    }

    public void setCXFRuntimeVersion(IProject project, String cxfRuntimeVersion) {
        IEclipsePreferences prefs = getProjectPreferences(project);
        prefs.put(PLUGIN_ID + ".runtime.version", cxfRuntimeVersion);
        flush(prefs);
    }

    public String getCXFRuntimeVersion(IProject project) {
        IEclipsePreferences prefs = getProjectPreferences(project);
        return prefs.get(PLUGIN_ID + ".runtime.version",
                CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeVersion());
    }

    private static void flush(IEclipsePreferences prefs) {
        try {
            prefs.flush();
        } catch (BackingStoreException bse) {
            log(bse);
        }
    }

    /**
     * Return the CXF preferences for the specified Eclipse project.
     */
    public IEclipsePreferences getProjectPreferences(IProject project) {
        IScopeContext context = new ProjectScope(project);
        return context.getNode(PLUGIN_ID);
    }

    public static void logMessage(int severity, String message) {
        CXFCorePlugin.log(new Status(severity, CXFCorePlugin.PLUGIN_ID, message));
    }

    public static void log(IStatus status) {
        CXFCorePlugin.getDefault().getLog().log(status);
    }

    public static void log(Throwable exception) {
        CXFCorePlugin.log(new Status(IStatus.ERROR, CXFCorePlugin.PLUGIN_ID,
                exception.toString(), exception));
    }
}
