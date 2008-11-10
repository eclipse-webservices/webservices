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
package org.eclipse.jst.ws.internal.cxf.creation.ui.viewers;

import java.util.Map;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.swt.graphics.Image;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class AnnotationColumnLabelProvider extends ColumnLabelProvider {
    private Image addAnnotationImage;
    private Image removeAnnotationImage;
    private Image disabled;

    private Java2WSDataModel model;
    private Map<IMethod, Map<String, Boolean>> methodMap;
    private String annotationKey;
    private IType type;

    public AnnotationColumnLabelProvider(Java2WSDataModel model, String annotationKey, IType type) {
        this.model = model;
        this.methodMap = model.getMethodMap();
        this.annotationKey = annotationKey;
        this.type = type;
        addAnnotationImage = CXFCreationUIPlugin.imageDescriptorFromPlugin(CXFCreationUIPlugin.PLUGIN_ID,
                "icons/obj16/true.gif").createImage(); //$NON-NLS-1$
        removeAnnotationImage = CXFCreationUIPlugin.imageDescriptorFromPlugin(CXFCreationUIPlugin.PLUGIN_ID,
                "icons/obj16/false.gif").createImage(); //$NON-NLS-1$
        disabled = CXFCreationUIPlugin.imageDescriptorFromPlugin(CXFCreationUIPlugin.PLUGIN_ID,
                "icons/obj16/disabled.gif").createImage(); //$NON-NLS-1$
    }

    public String getText(Object element) {
        return ""; //$NON-NLS-1$
    }

    @Override
    public Image getImage(Object element) {
        if (element instanceof SourceMethod) {
            if (methodMap == null || annotationKey == null || type == null) {
                return null;
            }

            IMethod method = (SourceMethod) element;
            if (model.getJavaStartingPoint().equals(type.getFullyQualifiedName())) {
                if (AnnotationUtils.isAnnotationPresent(type.findMethods(method)[0], annotationKey)) {
                    return disabled;
                }
            }
            if (methodMap.get(method) != null) {
                Boolean addAnnotation =  methodMap.get(method).get(annotationKey);
                if (addAnnotation) {
                    return addAnnotationImage;
                } else {
                    return removeAnnotationImage;
                }                
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        addAnnotationImage.dispose();
        removeAnnotationImage.dispose();
        disabled.dispose();
    }

}
