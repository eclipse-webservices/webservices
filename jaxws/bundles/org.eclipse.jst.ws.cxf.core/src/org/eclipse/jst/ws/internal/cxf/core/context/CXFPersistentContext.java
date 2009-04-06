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
package org.eclipse.jst.ws.internal.cxf.core.context;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.wst.command.internal.env.context.PersistentContext;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public abstract class CXFPersistentContext extends PersistentContext implements CXFContext {
    /**
     * String constant used to lookup the cxf runtime location general
     * preference from the plugins local preferences store.
     */
    private static final String PREFERENCE_CXF_RUNTIME_LOCATION = "cxfRuntimeLocation"; //$NON-NLS-1$

    /**
     * Stirng constant used to lookup the cxf runtime edition general preference from
     * the plugins local preferences store.
     */
    private static final String PREFERENCE_CXF_RUNTIME_EDITION = "cxfRuntimeEdition"; //$NON-NLS-1$

    /**
     * Stirng constant used to lookup the cxf version general preference from
     * the plugins local preferences store.
     */
    private static final String PREFERENCE_CXF_RUNTIME_VERSION = "cxfRuntimeVersion"; //$NON-NLS-1$
    
    private static final String PREFERENCE_EXPORT_CXF_CLASSPATH_CONTAINER = "exportCXFClasspathContainer"; //$NON-NLS-1$

    private static final String PREFERENCE_CXF_VERBOSE = "cxfVerbose"; //$NON-NLS-1$

    private static final String PREFERENCE_CXF_USE_SPRING_APP_CONTEXT = "cxfUseSpringAppContext"; //$NON-NLS-1$

    public CXFPersistentContext(Plugin plugin) {
        super(CXFCorePlugin.getDefault());
    }

    public void load() {
        setDefault(PREFERENCE_CXF_RUNTIME_LOCATION, ""); //$NON-NLS-1$
        setDefault(PREFERENCE_CXF_RUNTIME_EDITION, ""); //$NON-NLS-1$
        setDefault(PREFERENCE_CXF_RUNTIME_VERSION, ""); //$NON-NLS-1$
        
        setDefault(PREFERENCE_EXPORT_CXF_CLASSPATH_CONTAINER, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.CXF_CONTEXT, CXFPackage.CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER));

        setDefault(PREFERENCE_CXF_VERBOSE, CXFModelUtils.getDefaultBooleanValue(CXFPackage.CXF_CONTEXT,
                CXFPackage.CXF_CONTEXT__VERBOSE));

        setDefault(PREFERENCE_CXF_USE_SPRING_APP_CONTEXT, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.CXF_CONTEXT, CXFPackage.CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT));
    }

    public String getCxfRuntimeLocation() {
        return getValueAsString(PREFERENCE_CXF_RUNTIME_LOCATION);
    }

    public void setCxfRuntimeLocation(String runtimeLocation) {
        setValue(PREFERENCE_CXF_RUNTIME_LOCATION, runtimeLocation);
    }
    
    public String getCxfRuntimeEdition() {
        return getValueAsString(PREFERENCE_CXF_RUNTIME_EDITION);
    }

    public void setCxfRuntimeEdition(String runtimeEdition) {
        setValue(PREFERENCE_CXF_RUNTIME_EDITION, runtimeEdition);
    }

    public String getCxfRuntimeVersion() {
        return getValueAsString(PREFERENCE_CXF_RUNTIME_VERSION);
    }

    public void setCxfRuntimeVersion(String runtimeVersion) {
        setValue(PREFERENCE_CXF_RUNTIME_VERSION, runtimeVersion);
    }
    
    public boolean isExportCXFClasspathContainer() {
        return getValueAsBoolean(PREFERENCE_EXPORT_CXF_CLASSPATH_CONTAINER);
    }

    public void setExportCXFClasspathContainer(boolean exportCXFClasspathContainer) {
        setValue(PREFERENCE_EXPORT_CXF_CLASSPATH_CONTAINER, exportCXFClasspathContainer);
    }

    public boolean isVerbose() {
        return getValueAsBoolean(PREFERENCE_CXF_VERBOSE);
    }

    public void setVerbose(boolean verbose) {
        setValue(PREFERENCE_CXF_VERBOSE, true);
    }

    public boolean isUseSpringApplicationContext() {
        return getValueAsBoolean(PREFERENCE_CXF_USE_SPRING_APP_CONTEXT);
    }

    public void setUseSpringApplicationContext(boolean useSpringAppContext) {
        setValue(PREFERENCE_CXF_USE_SPRING_APP_CONTEXT, useSpringAppContext);
    }
}
