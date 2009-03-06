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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationDefinition;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationsManager;
import org.eclipse.ui.IMemento;

/**
 * 
 * @author sclarke
 *
 */
public class AnnotationsViewCategoryFilter extends ViewerFilter {
    private static final String TAG_CATEGORY = "AnnotationCategoryFilterInfo"; //$NON-NLS-1$
    private static final String TAG_CATEGORY_NAME = "categoryName"; //$NON-NLS-1$

    private AnnotationsView annotationsView;
    private final StructuredViewer viewer;

    private List<Object> categories;
    
    public AnnotationsViewCategoryFilter(AnnotationsView annotationsView, StructuredViewer viewer) {
        this.annotationsView = annotationsView;
        this.viewer = viewer;
        categories = new ArrayList<Object>();
    }
    
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof Class) {
            AnnotationDefinition annotationDefinition = AnnotationsManager.
                getAnnotationDefinitionForClass(element);
            if (annotationDefinition != null) {
                return !categories.contains(annotationDefinition.getCategory());
            }
        }
        return true;
    }

    public void filterAnnotations(List<Object> categories) {
        this.categories = categories;
        if (categories.size() > 0) {
            viewer.addFilter(this);
        } else {
            viewer.removeFilter(this);
        }
        annotationsView.refresh();
    }
    
    public List<Object> getCategories() {
        return categories;
    }
    
    public void init(IMemento memento) {
        IMemento catMemento = memento.getChild(TAG_CATEGORY);
        if (catMemento == null) {
            return;
        }
        for (int i = 0; i < AnnotationsManager.getAnnotationCategories().size(); i++) {
            String category = catMemento.getString(TAG_CATEGORY_NAME + i);
            if (category != null) {
                categories.add(category);
            }
        }
        filterAnnotations(categories);
    }
    
    public void saveState(IMemento memento) {
        if (categories == null || categories.size() == 0) {
            return;
        }
        IMemento catMemento = memento.createChild(TAG_CATEGORY);
        int i = 0;
        for (Object category : categories) {
            catMemento.putString(TAG_CATEGORY_NAME + i, category.toString());
            i++;
        }
    }    
}
