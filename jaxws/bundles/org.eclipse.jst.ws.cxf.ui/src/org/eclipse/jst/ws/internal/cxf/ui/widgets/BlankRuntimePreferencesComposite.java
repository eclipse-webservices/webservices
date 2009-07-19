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

import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class BlankRuntimePreferencesComposite extends Composite {

    public BlankRuntimePreferencesComposite(Composite parent, int style) {
        super(parent, style);

        addControls();
    }

    private void addControls() {
        this.setLayout(new GridLayout());
        CLabel setIntallDirLabel = new CLabel(this, SWT.NONE);
        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        setIntallDirLabel.setLayoutData(gridData);
        setIntallDirLabel.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
                ISharedImages.IMG_OBJS_ERROR_TSK));
        setIntallDirLabel.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_RUNTIME_NOT_SET);
    }

}
