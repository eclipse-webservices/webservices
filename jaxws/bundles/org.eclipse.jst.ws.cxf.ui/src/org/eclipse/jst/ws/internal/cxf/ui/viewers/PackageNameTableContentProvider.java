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
package org.eclipse.jst.ws.internal.cxf.ui.viewers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.wsdl.Definition;

import org.eclipse.jface.viewers.ArrayContentProvider;

/**
 * @author sclarke
 */
public class PackageNameTableContentProvider extends ArrayContentProvider {

    @Override
    @SuppressWarnings("unchecked")
    public Object[] getElements(Object inputElement) {
        java.util.List<Object> elements = new ArrayList<Object>();
        if (inputElement instanceof Definition) {
            Definition definition = (Definition) inputElement;
            Map namespaces = definition.getNamespaces();
            Collection values = namespaces.values();
            for (Object namespaceValue : values) {
                String namespace = namespaceValue.toString();
                if (!namespace.equals(definition.getTargetNamespace()) && !elements.contains(namespace)) {
                    elements.add(namespace);
                }
            }
        }
        return elements.toArray(new Object[elements.size()]);
    }

}
