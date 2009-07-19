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
package org.eclipse.jst.ws.internal.cxf.ui.widgets;

import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.context.Java2WSPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class AnnotationsComposite extends Composite {
    private Java2WSPersistentContext context = CXFCorePlugin.getDefault().getJava2WSContext();
    
    private Button generateWebMethodButton;
    private Button generateWebParamButton;
    private Button generateRequestWrapperButton;
    private Button generateResponseWrapperButton;

    private Button enableAPTButton;
    
    public AnnotationsComposite(Composite parent, int style) {
        super(parent, style);
        addControls();
    }

    private void addControls() {
        GridLayout preflayout = new GridLayout(1, true);
        this.setLayout(preflayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        this.setLayoutData(gridData);

        Group jaxwsAnnotationsGroup = new Group(this, SWT.SHADOW_IN);
        jaxwsAnnotationsGroup.setText(CXFUIMessages.JAXWS_ANNOTATIONS_GROUP_LABEL);
        jaxwsAnnotationsGroup.setToolTipText(CXFUIMessages.JAXWS_ANNOTATIONS_GROUP_TOOLTIP);
        GridLayout gridLayout = new GridLayout(1, true);
        jaxwsAnnotationsGroup.setLayout(gridLayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        jaxwsAnnotationsGroup.setLayoutData(gridData);

        generateWebMethodButton = new Button(jaxwsAnnotationsGroup, SWT.CHECK);
        generateWebMethodButton.setText(CXFUIMessages.JAXWS_GENERATE_WEB_METHOD);
        generateWebMethodButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                context.setGenerateWebMethodAnnotation(selected);
            }
        });
        generateWebMethodButton.setSelection(context.isGenerateWebMethodAnnotation());
        
        generateWebParamButton = new Button(jaxwsAnnotationsGroup, SWT.CHECK);
        generateWebParamButton.setText(CXFUIMessages.JAXWS_GENERATE_WEB_PARAM);
        generateWebParamButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                context.setGenerateWebParamAnnotation(selected);
            }
        });
        generateWebParamButton.setSelection(context.isGenerateWebParamAnnotation());
        
        generateRequestWrapperButton = new Button(jaxwsAnnotationsGroup, SWT.CHECK);
        generateRequestWrapperButton.setText(CXFUIMessages.JAXWS_GENERATE_REQUEST_WRAPPER);
        generateRequestWrapperButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                context.setGenerateRequestWrapperAnnotation(selected);
            }
        });
        generateRequestWrapperButton.setSelection(context.isGenerateRequestWrapperAnnotation());
        
        generateResponseWrapperButton = new Button(jaxwsAnnotationsGroup, SWT.CHECK);
        generateResponseWrapperButton.setText(CXFUIMessages.JAXWS_GENERATE_RESPONSE_WRAPPER);
        generateResponseWrapperButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                context.setGenerateResponseWrapperAnnotation(selected);
            }
        });
        generateResponseWrapperButton.setSelection(context.isGenerateResponseWrapperAnnotation());

        Group annotationProcessingGroup = new Group(this, SWT.SHADOW_IN);
        annotationProcessingGroup.setText(CXFUIMessages.JAXWS_ANNOTATIONS_PROCESSING_GROUP_LABEL);
        annotationProcessingGroup.setToolTipText(CXFUIMessages.bind(
                CXFUIMessages.JAXWS_ENABLE_ANNOTATION_PROCESSING_TOOLTIP, 
                context.getCxfRuntimeEdition()));
        gridLayout = new GridLayout(1, true);
        annotationProcessingGroup.setLayout(gridLayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        annotationProcessingGroup.setLayoutData(gridData);

        enableAPTButton = new Button(annotationProcessingGroup, SWT.CHECK);
        enableAPTButton.setText(CXFUIMessages.JAXWS_ENABLE_ANNOTATION_PROCESSING);
        enableAPTButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                context.setAnnotationProcessingEnabled(selected);
            }
        });
        enableAPTButton.setSelection(context.isAnnotationProcessingEnabled());
    }

    public void setDefaults() {
        generateWebMethodButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION));
        
        generateWebParamButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION));
        
        generateRequestWrapperButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, 
                CXFPackage.JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION));
        
        generateResponseWrapperButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION));
        
        enableAPTButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED));
    }
    
    public void storeValues() {
        context.setGenerateWebMethodAnnotation(generateWebMethodButton.getSelection());
        context.setGenerateWebParamAnnotation(generateWebParamButton.getSelection());
        context.setGenerateRequestWrapperAnnotation(generateRequestWrapperButton.getSelection());
        context.setGenerateResponseWrapperAnnotation(generateResponseWrapperButton.getSelection());
        context.setAnnotationProcessingEnabled(enableAPTButton.getSelection());
    }
}
