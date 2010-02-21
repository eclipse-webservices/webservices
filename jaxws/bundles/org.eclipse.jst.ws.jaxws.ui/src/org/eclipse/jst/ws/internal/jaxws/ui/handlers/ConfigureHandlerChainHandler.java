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
package org.eclipse.jst.ws.internal.jaxws.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.jst.ws.internal.jaxws.ui.wizards.ConfigureHandlerWizard;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class ConfigureHandlerChainHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection != null && selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (structuredSelection.getFirstElement() != null) {
                Object element = structuredSelection.getFirstElement();
                try {
                    IType type = null;
                    if (element instanceof IWebService) {
                        IWebService webService = (IWebService) element;
                        type = Dom2ResourceMapper.INSTANCE.findType(webService);
                    }
                    if (element instanceof IServiceEndpointInterface) {
                        IServiceEndpointInterface serviceEndpointInterface = (IServiceEndpointInterface) element;
                        type = Dom2ResourceMapper.INSTANCE.findType(serviceEndpointInterface);
                    }
                    if (type != null) {
                        ConfigureHandlerWizard configureHandlerWizard = new ConfigureHandlerWizard(type);
                        Shell shell = HandlerUtil.getActiveShell(event);
                        WizardDialog dialog = new WizardDialog(shell, configureHandlerWizard);
                        dialog.create();
                        dialog.open();
                    }

                } catch (JavaModelException jme) {
                    JAXWSUIPlugin.log(jme.getStatus());
                }
            }
        }
        return null;
    }

}
