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
package org.eclipse.jst.ws.internal.jaxws.ui;

import org.eclipse.osgi.util.NLS;

public class JAXWSUIMessages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages"; //$NON-NLS-1$
    
    public static String ANNOTATION_ARRAY_CELL_EDITOR_ADD_LABEL;
    public static String ANNOTATION_ARRAY_CELL_EDITOR_BROWSE_LABEL;
    public static String ANNOTATION_ARRAY_CELL_EDITOR_DOWN_LABEL;
    public static String ANNOTATION_ARRAY_CELL_EDITOR_EDIT_ARRAY_VALUES_TITLE;
    public static String ANNOTATION_ARRAY_CELL_EDITOR_REMOVE_LABEL;
    public static String ANNOTATION_ARRAY_CELL_EDITOR_SELECT_CLASS_TITLE;
    public static String ANNOTATION_ARRAY_CELL_EDITOR_UP_LABEL;
    public static String ANNOTATION_ARRAY_CELL_EDITOR_NESTED_ARRAYS_NOT_SUPPORTED;
    
    public static String ANNOTATION_EDITING_SUPPORT_NOT_VALID_MESSAGE_PREFIX;

    public static String ANNOTATIONS_VIEW_ANNOTATIONS_COLUMN_NAME;
    public static String ANNOTATIONS_VIEW_ANNOTATIONS_NOT_AVAILABLE_ON_SELECTION;
    public static String ANNOTATIONS_VIEW_ANNOTATIONS_VALUES_COLUMN_NAME;

    public static String ANNOTATIONS_VIEW_FILTER_ACTION_NAME;
    public static String ANNOTATIONS_VIEW_FILTER_ACTION_SELECT_CATEGORIES_MESSAGE;

    public static String ANNOTATIONS_VIEW_NO_SUITABLE_LIBRARY_FOUND;
    public static String ANNOTATIONS_VIEW_OTHER_ANNOTATION_LIBRARIES_USE;
    
    public static String CONFIGURE_JAVA_1_6_LIBRARY;
    
    public static String ADD_ANNOTATION;
    public static String ADD_MISSING_METHOD;
    public static String ADD_UNIMPLEMENTED_METHODS;
    public static String CHANGE_METHOD_VISIBILITY;
    public static String CHANGE_METHOD_RETURN_TYPE;
    public static String CREATE_CONSTRUCTOR;
    public static String REMOVE_ANNOTATION;
    public static String REMOVE_METHOD;
    public static String REMOVE_FINAL_MODIFIER;
    public static String REMOVE_ILLEGAL_MODIFIER;
    public static String REMOVE_STATIC_MODIFIER;
    public static String REMOVE_MEMBERVALUEPAIR;
    public static String REMOVE_ALL_OTHER_MEMBERVALUEPAIRS;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, JAXWSUIMessages.class);
    }

    private JAXWSUIMessages() {
    }
}
