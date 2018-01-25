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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFFactory;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.wst.command.internal.env.context.PersistentContext;

@SuppressWarnings("restriction")
public abstract class CXFPersistentContext extends PersistentContext implements CXFContext {
    private Map<String, CXFInstall> installations;

    /**
     * String constant used to lookup the CXF runtime location general
     * preference from the plugins local preferences store.
     */
    private static final String PREFERENCE_DEFAULT_RUNTIME_LOCATION = "defaultRuntimeLocation"; //$NON-NLS-1$

    /**
     * String constant used to lookup the CXF runtime type general preference from
     * the plugins local preferences store.
     */
    private static final String PREFERENCE_DEFAULT_RUNTIME_TYPE = "defaultRuntimeType"; //$NON-NLS-1$

    /**
     * String constant used to lookup the CXF version general preference from
     * the plugins local preferences store.
     */
    private static final String PREFERENCE_DEFAULT_RUNTIME_VERSION = "defaultRuntimeVersion"; //$NON-NLS-1$

    private static final String PREFERENCE_CXF_RUNTIME_VERSIONS = "cxfRuntimeVersions"; //$NON-NLS-1$
    private static final String PREFERENCE_CXF_RUNTIME_LOCATIONS = "cxfRuntimeLocations"; //$NON-NLS-1$
    private static final String PREFERENCE_CXF_RUNTIME_TYPES = "cxfRuntimeTypes"; //$NON-NLS-1$

    private static final String PREFERENCE_EXPORT_CXF_CLASSPATH_CONTAINER = "exportCXFClasspathContainer"; //$NON-NLS-1$

    private static final String PREFERENCE_CXF_VERBOSE = "cxfVerbose"; //$NON-NLS-1$

    private static final String PREFERENCE_CXF_USE_SPRING_APP_CONTEXT = "cxfUseSpringAppContext"; //$NON-NLS-1$

    public CXFPersistentContext(Plugin plugin) {
        super(CXFCorePlugin.getDefault());
    }

    public void load() {
        setDefault(PREFERENCE_DEFAULT_RUNTIME_LOCATION, ""); //$NON-NLS-1$
        setDefault(PREFERENCE_DEFAULT_RUNTIME_TYPE, ""); //$NON-NLS-1$
        setDefault(PREFERENCE_DEFAULT_RUNTIME_VERSION, ""); //$NON-NLS-1$

        setDefault(PREFERENCE_CXF_RUNTIME_VERSIONS, ""); //$NON-NLS-1$
        setDefault(PREFERENCE_CXF_RUNTIME_LOCATIONS, ""); //$NON-NLS-1$
        setDefault(PREFERENCE_CXF_RUNTIME_TYPES, ""); //$NON-NLS-1$

        setDefault(PREFERENCE_EXPORT_CXF_CLASSPATH_CONTAINER, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.CXF_CONTEXT, CXFPackage.CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER));

        setDefault(PREFERENCE_CXF_VERBOSE, CXFModelUtils.getDefaultBooleanValue(CXFPackage.CXF_CONTEXT,
                CXFPackage.CXF_CONTEXT__VERBOSE));

        setDefault(PREFERENCE_CXF_USE_SPRING_APP_CONTEXT, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.CXF_CONTEXT, CXFPackage.CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT));
    }

    public Map<String, CXFInstall> getInstallations() {
        if (installations == null) {
            installations = new HashMap<String, CXFInstall>();
            String versions = getValueAsString(PREFERENCE_CXF_RUNTIME_VERSIONS).trim();
            String loctions = getValueAsString(PREFERENCE_CXF_RUNTIME_LOCATIONS).trim();
            String types = getValueAsString(PREFERENCE_CXF_RUNTIME_TYPES).trim();
            if (versions.trim().length() > 0 && loctions.trim().length() > 0 && types.trim().length() > 0) {
                String[] cxfVersions = versions.split(",");
                String[] cxfLocations = loctions.split(",");
                String[] cxfTypes = types.split(",");
                for (int i = 0; i < cxfVersions.length; i++) {
                    CXFInstall cxfInstall = CXFFactory.eINSTANCE.createCXFInstall();
                    cxfInstall.setVersion(cxfVersions[i]);
                    cxfInstall.setLocation(cxfLocations[i]);
                    cxfInstall.setType(cxfTypes[i]);
                    installations.put(cxfVersions[i], cxfInstall);
                }
            }
        }
        return installations;
    }

    public void setInstallations(Map<String, CXFInstall> installations) {
        this.installations = installations;

        Collection<CXFInstall> installs = installations.values();

        StringBuilder versions = new StringBuilder();
        StringBuilder locations = new StringBuilder();
        StringBuilder types = new StringBuilder();
        Iterator<CXFInstall> installIter = installs.iterator();
        while (installIter.hasNext()) {
            CXFInstall cxfInstall = installIter.next();
            versions.append(cxfInstall.getVersion());
            locations.append(cxfInstall.getLocation());
            types.append(cxfInstall.getType());
            if (installIter.hasNext()) {
                versions.append(",");
                locations.append(",");
                types.append(",");
            }
        }
        setValue(PREFERENCE_CXF_RUNTIME_VERSIONS, versions.toString());
        setValue(PREFERENCE_CXF_RUNTIME_LOCATIONS, locations.toString());
        setValue(PREFERENCE_CXF_RUNTIME_TYPES, types.toString());
    }

    public String getDefaultRuntimeLocation() {
        return getValueAsString(PREFERENCE_DEFAULT_RUNTIME_LOCATION);
    }

    public void setDefaultRuntimeLocation(String runtimeLocation) {
        setValue(PREFERENCE_DEFAULT_RUNTIME_LOCATION, runtimeLocation);
    }

    public String getDefaultRuntimeType() {
        return getValueAsString(PREFERENCE_DEFAULT_RUNTIME_TYPE);
    }

    public void setDefaultRuntimeType(String runtimeEdition) {
        setValue(PREFERENCE_DEFAULT_RUNTIME_TYPE, runtimeEdition);
    }

    public String getDefaultRuntimeVersion() {
        return getValueAsString(PREFERENCE_DEFAULT_RUNTIME_VERSION);
    }

    public void setDefaultRuntimeVersion(String runtimeVersion) {
        setValue(PREFERENCE_DEFAULT_RUNTIME_VERSION, runtimeVersion);
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
