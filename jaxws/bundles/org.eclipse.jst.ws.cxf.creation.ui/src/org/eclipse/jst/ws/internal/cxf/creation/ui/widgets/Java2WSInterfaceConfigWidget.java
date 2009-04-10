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
package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.Java2WSWidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

/**
 * 
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class Java2WSInterfaceConfigWidget extends SimpleWidgetDataContributor {
    private Java2WSDataModel model;
    private IType startingPointType;

    public Java2WSInterfaceConfigWidget() {
    }

    public void setJava2WSDataModel(Java2WSDataModel model) {
        this.model = model;
    }

    public void setJavaStartingPointType(IType startingPointType) {
        this.startingPointType = startingPointType;
    }

    @Override
    public WidgetDataEvents addControls(Composite parent, Listener statusListener) {
        final Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        composite.setLayout(gridLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        composite.setLayoutData(gridData);
        
        Java2WSWidgetFactory.createSelectImplementationLabel(composite);
        
        Combo selectImplementationCombo = Java2WSWidgetFactory
            .createSelectImplementationCombo(composite, model, startingPointType);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        selectImplementationCombo.setLayoutData(gridData);
        /*
        CLabel underConstructionLabel = new CLabel(composite, SWT.NONE);
        gridData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        underConstructionLabel.setLayoutData(gridData);
        underConstructionLabel.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
                ISharedImages.IMG_OBJS_INFO_TSK));
        underConstructionLabel.setText("Under Construction");
        */
        return this;
    }

    @Override
    public IStatus getStatus() {
        return null;
    }

}
