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
package org.eclipse.jst.ws.internal.cxf.creation.ui.selection;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

@SuppressWarnings("restriction")
public class IStructuredSelectionToIProjectTransformer implements Transformer {

    public Object transform(Object value) {
        IStructuredSelection structuredSelection = (IStructuredSelection) value;
        Object firstElement = structuredSelection.getFirstElement();
        if (firstElement instanceof IResource) {
            IResource resource = (IResource) firstElement;
            return resource.getProject();
        }
        return null;
    }

}
