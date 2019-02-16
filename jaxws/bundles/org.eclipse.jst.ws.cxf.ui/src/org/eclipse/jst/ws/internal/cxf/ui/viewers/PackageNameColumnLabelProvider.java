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
package org.eclipse.jst.ws.internal.cxf.ui.viewers;

import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.WSDLUtils;

public class PackageNameColumnLabelProvider extends ColumnLabelProvider {
    private Map<String, String> includedNamespaces;

    public PackageNameColumnLabelProvider(WSDL2JavaDataModel model) {
        includedNamespaces = model.getIncludedNamespaces();
    }

    @Override
    public String getText(Object element) {
        if (includedNamespaces.containsKey(element.toString())) {
            return includedNamespaces.get(element.toString());
        }
        return WSDLUtils.getPackageNameFromNamespace(element.toString());
    }

}
