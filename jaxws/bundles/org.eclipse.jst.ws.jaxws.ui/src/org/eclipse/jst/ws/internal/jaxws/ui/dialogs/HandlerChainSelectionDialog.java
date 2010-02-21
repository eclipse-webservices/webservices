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

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSHandlerUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class HandlerChainSelectionDialog extends ElementTreeSelectionDialog {
    private static final String XML_FILE_EXTENSION = "xml";  //$NON-NLS-1$

    private IStatus ok_status = new Status(IStatus.OK, JAXWSUIPlugin.PLUGIN_ID, "");  //$NON-NLS-1$

    public HandlerChainSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
        setTitle(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_EDIT_DIALOG_TITLE);
        setMessage(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_EDIT_DIALOG_DESCRIPTION);
        setAllowMultiple(false);
        setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                if (selection.length > 0) {
                    Object selected = selection[0];
                    if (selected instanceof IFile) {
                        IFile file = (IFile) selected;
                        try {
                            if (file.getFileExtension().equals(XML_FILE_EXTENSION) &&
                                    JAXWSHandlerUtils.isHandlerChainFile(file)) {
                                return ok_status;
                            }
                        } catch (IOException ioe) {
                            JAXWSUIPlugin.log(ioe);
                        }
                    }
                }
                return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_EDIT_DIALOG_INVALID);
            }
        });

    }

}
