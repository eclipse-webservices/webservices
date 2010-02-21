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
package org.eclipse.jst.ws.internal.jaxws.ui.dialogs;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class NewHandlerChainDialog extends ElementTreeSelectionDialog implements ISelectionStatusValidator {

    private IStatus ok_status = new Status(IStatus.OK, JAXWSUIPlugin.PLUGIN_ID, "");  //$NON-NLS-1$

    private String handlerChainFileName = "handler-chain.xml";  //$NON-NLS-1$

    public NewHandlerChainDialog(Shell parent, ILabelProvider labelProvider,
            ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
        setTitle(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_TITLE);
        setMessage(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_DESCRIPTION);
        setAllowMultiple(false);
        setValidator(this);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Composite fileComposite = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        fileComposite.setLayout(gridLayout);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        fileComposite.setLayoutData(gridData);
        Label handleChainLabel = new Label(fileComposite, SWT.NONE);
        handleChainLabel.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_NAME);
        Text newHandlerChainText = new Text(fileComposite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        newHandlerChainText.setLayoutData(gridData);
        newHandlerChainText.setText(handlerChainFileName);
        newHandlerChainText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                handlerChainFileName = ((Text) e.widget).getText();
                updateOKStatus();
            }

        });
        updateOKStatus();
        return composite;
    }

    public IStatus validate(Object[] selection) {
        if (handlerChainFileName.trim().length() == 0) {
            return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                    JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_ENTER_NAME);
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IStatus result = workspace.validateName(handlerChainFileName, IResource.FILE);
        if (!result.isOK()) {
            return result;
        }

        if (selection == null || selection.length == 0 || selection[0] instanceof IJavaProject) {
            return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                    JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_CHOOSE_FOLDER);
        }

        if (!handlerChainFileName.endsWith(".xml")) {  //$NON-NLS-1$
            return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                    JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_INVALID_NAME);
        }

        if (workspace.getRoot().getFile(new Path(getFilePath())).exists()) {
            return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                    JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_EXISTS,
                            handlerChainFileName));
        }

        return ok_status;
    }

    public String getFileName() {
        return handlerChainFileName;
    }

    public String getFilePath() {
        Object element = getFirstResult();
        if (element instanceof IJavaProject) {
            return ((IJavaProject) element).getProject().getFullPath().toOSString() + File.separatorChar
            + handlerChainFileName;
        }

        if (element instanceof IJavaElement) {
            return ((IJavaElement) element).getResource().getFullPath().toOSString() + File.separatorChar
            + handlerChainFileName;
        }
        if (element instanceof IResource) {
            return ((IResource) element).getFullPath().toOSString() + File.separatorChar + handlerChainFileName;
        }
        return handlerChainFileName;
    }
}
