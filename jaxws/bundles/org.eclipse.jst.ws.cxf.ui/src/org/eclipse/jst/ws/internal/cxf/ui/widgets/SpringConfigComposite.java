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
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
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

/**
 * @author sclarke
 */
public class SpringConfigComposite extends Composite {
    private CXFContext context = CXFCorePlugin.getDefault().getJava2WSContext();

    private Button useCXFServletButton;
    private Button useSpringAppContextButton;
    
    public SpringConfigComposite(Composite parent, int style) {
        super(parent, style);
        addControls();
    }

    private void addControls() {
        GridLayout preflayout = new GridLayout(1, true);
        this.setLayout(preflayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        this.setLayoutData(gridData);

        Group springConfigGroup = new Group(this, SWT.SHADOW_IN);
        springConfigGroup.setText(CXFUIMessages.SPRING_CONFIG_GROUP_LABEL);
        GridLayout springConfigLayout = new GridLayout(2, true);
        springConfigGroup.setLayout(springConfigLayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        springConfigGroup.setLayoutData(gridData);

        useCXFServletButton = new Button(springConfigGroup, SWT.RADIO);
        useCXFServletButton.setText(CXFUIMessages.SPRING_CONFIG_USE_CXF_SERVLET);
        useCXFServletButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                context.setUseSpringApplicationContext(!selected);
            }
        });

        useSpringAppContextButton = new Button(springConfigGroup, SWT.RADIO);
        useSpringAppContextButton
                .setText(CXFUIMessages.SPRING_CONFIG_USE_CXF_SPRING_APPLICATION_CONTEXT);
        useSpringAppContextButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                context.setUseSpringApplicationContext(selected);
            }
        });

        if (context.isUseSpringApplicationContext()) {
            useSpringAppContextButton.setSelection(true);
        } else {
            useCXFServletButton.setSelection(true);
        }
    }
    
    public void setDefaults() {
        boolean useSpringAppcontext = CXFModelUtils.getDefaultBooleanValue(CXFPackage.CXF_CONTEXT,
                CXFPackage.CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT);
        useSpringAppContextButton.setSelection(useSpringAppcontext);
        useCXFServletButton.setSelection(!useSpringAppcontext);
    }
    
    public void storeValues() {
        context.setUseSpringApplicationContext(useSpringAppContextButton.getSelection());
    }
}
