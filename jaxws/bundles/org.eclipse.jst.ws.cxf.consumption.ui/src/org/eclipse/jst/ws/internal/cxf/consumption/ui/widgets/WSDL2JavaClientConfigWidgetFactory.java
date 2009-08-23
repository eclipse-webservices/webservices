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
package org.eclipse.jst.ws.internal.cxf.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.cxf.consumption.core.commands.WSDL2JavaClientDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.consumption.ui.CXFConsumptionUIMessages;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;

@SuppressWarnings("restriction")
public class WSDL2JavaClientConfigWidgetFactory implements INamedWidgetContributorFactory {
    private SimpleWidgetContributor wsdl2JavaClientWidgetContributor;
    private SimpleWidgetContributor wsdl2JavaClientDefaultsWidgetContributor;

    private WSDL2JavaClientConfigWidget wsdl2JavaConfigWidget = new WSDL2JavaClientConfigWidget();
    private WSDL2JavaClientDefaultsConfigWidget wsdl2JavaClientDefaultsConfigWidget = new WSDL2JavaClientDefaultsConfigWidget();

    public INamedWidgetContributor getFirstNamedWidget() {
        if ((wsdl2JavaClientWidgetContributor == null
                || wsdl2JavaClientDefaultsWidgetContributor == null)) {
            init();
        }
        return wsdl2JavaClientWidgetContributor;
    }

    public INamedWidgetContributor getNextNamedWidget(INamedWidgetContributor widgetContributor) {
        if (widgetContributor == wsdl2JavaClientWidgetContributor) {
            return wsdl2JavaClientDefaultsWidgetContributor;
        }
        return null;
    }

    public void registerDataMappings(DataMappingRegistry dataRegistry) {
        dataRegistry.addMapping(WSDL2JavaClientDefaultingCommand.class,
                "WSDL2JavaDataModel", WSDL2JavaClientConfigWidgetFactory.class); //$NON-NLS-1$
    }

    public void setWSDL2JavaDataModel(WSDL2JavaDataModel model) {
        wsdl2JavaConfigWidget.setWSDL2JavaDataModel(model);
        wsdl2JavaClientDefaultsConfigWidget.setWSDL2JavaDataModel(model);
    }

    private void init() {
        wsdl2JavaClientWidgetContributor = new SimpleWidgetContributor();
        CXFContext context = CXFCorePlugin.getDefault().getWSDL2JavaContext();
        String wsdl2JavaClientPageTitle = CXFConsumptionUIMessages.bind(
                CXFConsumptionUIMessages.WSDL2JAVA_CLIENT_PAGE_TITLE, new Object[]{
                        context.getCxfRuntimeEdition(), context.getCxfRuntimeVersion()});
        wsdl2JavaClientWidgetContributor.setTitle(wsdl2JavaClientPageTitle);
        wsdl2JavaClientWidgetContributor
                .setDescription(CXFConsumptionUIMessages.WSDL2JAVA_CLIENT_PAGE_DESCRIPTION);
        wsdl2JavaClientWidgetContributor.setFactory(new WidgetContributorFactory() {
            public WidgetContributor create() {
                return wsdl2JavaConfigWidget;
            }
        });
        
        wsdl2JavaClientDefaultsWidgetContributor = new SimpleWidgetContributor();
        String wsdl2JavaClientDefaultsPageTitle = CXFConsumptionUIMessages.bind(
        CXFConsumptionUIMessages.WSDL2JAVA_ClIENT_DEFAULTS_PAGE_TITLE, new Object[] {
                context.getCxfRuntimeEdition(), context.getCxfRuntimeVersion()});
        wsdl2JavaClientDefaultsWidgetContributor.setTitle(wsdl2JavaClientDefaultsPageTitle);
        wsdl2JavaClientDefaultsWidgetContributor
                .setDescription(CXFConsumptionUIMessages.WSDL2JAVA_CLIENT_DEFAULTS_PAGE_DESCRIPTION);
        wsdl2JavaClientDefaultsWidgetContributor.setFactory(new WidgetContributorFactory() {
            public WidgetContributor create() {
                return wsdl2JavaClientDefaultsConfigWidget;
            }
        });
    }

}
