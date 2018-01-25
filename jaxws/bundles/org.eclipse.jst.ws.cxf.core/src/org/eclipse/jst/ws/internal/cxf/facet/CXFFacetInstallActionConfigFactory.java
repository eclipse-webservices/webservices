/*******************************************************************************
 * Copyright (c) 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.facet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFFactory;
import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;

public class CXFFacetInstallActionConfigFactory implements IActionConfigFactory {

    public Object create() throws CoreException {
        CXFDataModel dataModel = CXFFactory.eINSTANCE.createJava2WSDataModel();
        CXFContext context = CXFCorePlugin.getDefault().getJava2WSContext();

        dataModel.setDefaultRuntimeVersion(context.getDefaultRuntimeVersion());
        dataModel.setDefaultRuntimeLocation(context.getDefaultRuntimeLocation());
        dataModel.setDefaultRuntimeType(context.getDefaultRuntimeType());
        dataModel.setUseSpringApplicationContext(context.isUseSpringApplicationContext());
        return dataModel;
    }

}
