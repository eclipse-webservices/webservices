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
package org.eclipse.jst.ws.internal.cxf.creation.ui.selection;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

/**
 * @author sclarke
 */
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
