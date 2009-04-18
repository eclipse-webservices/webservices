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

import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.WSDL2JavaDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class WSDL2JavaConfigWidgetFactory implements INamedWidgetContributorFactory {

    private SimpleWidgetContributor wsdl2JavaWidgetContributor;
    private SimpleWidgetContributor wsdl2JavaDefaultsWidgetContributor;

    private WSDL2JavaConfigWidget wsdl2JavaConfigWidget = new WSDL2JavaConfigWidget();
    private WSDL2JavaDefaultsConfigWidget wsdl2JavaDefaultsConfigWidget = new WSDL2JavaDefaultsConfigWidget();
    
    public INamedWidgetContributor getFirstNamedWidget() {
        if ((wsdl2JavaWidgetContributor == null || wsdl2JavaDefaultsWidgetContributor == null)) {
            init();
        }
        return wsdl2JavaWidgetContributor;
    }

    public INamedWidgetContributor getNextNamedWidget(INamedWidgetContributor widgetContributor) {
        if (widgetContributor == wsdl2JavaWidgetContributor) {
            return wsdl2JavaDefaultsWidgetContributor;
        }
        return null;
    }

    public void registerDataMappings(DataMappingRegistry dataRegistry) {
        dataRegistry.addMapping(WSDL2JavaDefaultingCommand.class,
                "WSDL2JavaDataModel", WSDL2JavaConfigWidgetFactory.class); //$NON-NLS-1$
    }

    public void setWSDL2JavaDataModel(WSDL2JavaDataModel model) {
        wsdl2JavaConfigWidget.setWSDL2JavaDataModel(model);
        wsdl2JavaDefaultsConfigWidget.setWSDL2JavaDataModel(model);
    }

    private void init() {
        wsdl2JavaWidgetContributor = new SimpleWidgetContributor();
        CXFContext context = CXFCorePlugin.getDefault().getWSDL2JavaContext();
        String wsdl2JavaPageTitle = CXFCreationUIMessages.bind(
                CXFCreationUIMessages.WSDL2JAVA_PAGE_TITLE, new Object[]{context.getCxfRuntimeEdition(),
                        context.getCxfRuntimeVersion()});
        wsdl2JavaWidgetContributor.setTitle(wsdl2JavaPageTitle);
        wsdl2JavaWidgetContributor
                .setDescription(CXFCreationUIMessages.WSDL2JAVA_PAGE_DESCRIPTION);
        wsdl2JavaWidgetContributor.setFactory(new WidgetContributorFactory() {
            public WidgetContributor create() {
                return wsdl2JavaConfigWidget;
            }
        });

        wsdl2JavaDefaultsWidgetContributor = new SimpleWidgetContributor();
        String wsdl2JavaDefaultsPageTitle = CXFCreationUIMessages.bind(
                CXFCreationUIMessages.WSDL2JAVA_DEFAULTS_PAGE_TITLE, new Object[]{
                        context.getCxfRuntimeEdition(), context.getCxfRuntimeVersion()});
        wsdl2JavaDefaultsWidgetContributor.setTitle(wsdl2JavaDefaultsPageTitle);
        wsdl2JavaDefaultsWidgetContributor
                .setDescription(CXFCreationUIMessages.WSDL2JAVA_DEFAULTS_PAGE_DESCRIPTION);
        wsdl2JavaDefaultsWidgetContributor.setFactory(new WidgetContributorFactory() {
            public WidgetContributor create() {
                return wsdl2JavaDefaultsConfigWidget;
            }
        });
    }
}
