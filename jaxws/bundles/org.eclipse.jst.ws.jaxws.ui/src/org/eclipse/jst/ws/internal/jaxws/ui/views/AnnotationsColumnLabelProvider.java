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
package org.eclipse.jst.ws.internal.jaxws.ui.views;

import java.lang.reflect.Method;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

public class AnnotationsColumnLabelProvider extends ColumnLabelProvider {

    @Override
    public String getText(Object element) {
        if (element instanceof Class) {
            return ((Class<?>)element).getName();
        }
        
        if (element instanceof Method) {
            return ((Method)element).getName();
        }
        return ""; //$NON-NLS-1$
    }

    @Override
    public Image getImage(Object element) {
        if (element instanceof Class) {
            return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_ANNOTATION);
        }
        if (element instanceof Method) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(
                    org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
        }
        return null;
    }
}
