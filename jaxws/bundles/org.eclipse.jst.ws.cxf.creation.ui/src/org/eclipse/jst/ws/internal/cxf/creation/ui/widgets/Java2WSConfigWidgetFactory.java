/*******************************************************************************
 * Copyright (c) 2008, 2010 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.Java2WSDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;

@SuppressWarnings("restriction")
public class Java2WSConfigWidgetFactory implements INamedWidgetContributorFactory {

    private SimpleWidgetContributor java2WSWidgetContributor;

    private Java2WSConfigWidget java2WSConfigWidget = new Java2WSConfigWidget();

    public Java2WSConfigWidgetFactory() {

    }

    public INamedWidgetContributor getFirstNamedWidget() {
        if (java2WSWidgetContributor == null) {
            init();
        }
        return java2WSWidgetContributor;
    }

    public INamedWidgetContributor getNextNamedWidget(INamedWidgetContributor widgetContributor) {
        return null;
    }

    public void registerDataMappings(DataMappingRegistry dataRegistry) {
        dataRegistry.addMapping(Java2WSDefaultingCommand.class,
                "Java2WSDataModel", Java2WSConfigWidgetFactory.class); //$NON-NLS-1$
    }

    public void setJava2WSDataModel(Java2WSDataModel model) {
        java2WSConfigWidget.setJava2WSDataModel(model);
    }

    private void init() {
        java2WSWidgetContributor = new SimpleWidgetContributor();
        CXFContext context = CXFCorePlugin.getDefault().getJava2WSContext();
        String title = CXFCreationUIMessages.bind(
                CXFCreationUIMessages.JAVA2WS_PAGE_TITLE, new Object[]{context.getDefaultRuntimeType(),
                        context.getDefaultRuntimeVersion()});

        java2WSWidgetContributor.setTitle(title);
        java2WSWidgetContributor
        .setDescription(CXFCreationUIMessages.bind(CXFCreationUIMessages.JAVA2WS_PAGE_DESCRIPTION,
                context.getDefaultRuntimeType()));
        java2WSWidgetContributor.setFactory(new WidgetContributorFactory() {
            public WidgetContributor create() {
                return java2WSConfigWidget;
            }
        });
    }
}
