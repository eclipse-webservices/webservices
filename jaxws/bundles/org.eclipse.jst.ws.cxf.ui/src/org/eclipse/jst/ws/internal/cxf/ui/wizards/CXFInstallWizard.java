/*******************************************************************************
 * Copyright (c) 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;

public class CXFInstallWizard extends Wizard {

    private CXFInstallWizardPage cxfInstallWizardPage;
    private CXFInstall cxfInstall;

    public CXFInstallWizard() {
        setWindowTitle(CXFUIMessages.CXF_INSTALL_WIZARD_ADD_WINDOW_TITLE);
    }

    public CXFInstallWizard(CXFInstall cxfInstall) {
        this.cxfInstall = cxfInstall;
        setWindowTitle(CXFUIMessages.CXF_INSTALL_WIZARD_EDIT_WINDOW_TITLE);
    }

    @Override
    public void addPages() {
        if (cxfInstallWizardPage == null) {
            cxfInstallWizardPage = new CXFInstallWizardPage();
            if (cxfInstall != null) {
                cxfInstallWizardPage.setCXFInstall(cxfInstall);
            }
        }
        addPage(cxfInstallWizardPage);
    }

    @Override
    public boolean performFinish() {
        return cxfInstallWizardPage.finish();
    }
}