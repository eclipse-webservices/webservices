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

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.Java2WSDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;

/**
 * 
 * @author sclarke
 * 
 */
@SuppressWarnings("restriction")
public class Java2WSSelectSEIWidgetFactory implements INamedWidgetContributorFactory {
    private SimpleWidgetContributor classWidgetContributor;
    private SimpleWidgetContributor interfaceWidgetContributor;
    
    private Java2WSClassConfigWidget java2WSClassConfigWidget = new Java2WSClassConfigWidget();
    private Java2WSInterfaceConfigWidget java2WSInterfaceConfigWidget = new Java2WSInterfaceConfigWidget();

    private IType startingPointType;

    public INamedWidgetContributor getFirstNamedWidget() {
        if (interfaceWidgetContributor == null || classWidgetContributor == null) {
            init();
        }

        try {
            if (startingPointType.isInterface()) {
                return interfaceWidgetContributor;
            } else if (startingPointType.isClass()) {
                return classWidgetContributor;
            }
        } catch (JavaModelException jme) {
            CXFCreationUIPlugin.log(jme.getStatus());
        }

        return null;
    }

    public INamedWidgetContributor getNextNamedWidget(INamedWidgetContributor widgetContributor) {
        return null;
    }

    public void registerDataMappings(DataMappingRegistry dataRegistry) {
        dataRegistry.addMapping(Java2WSDefaultingCommand.class,
                "Java2WSDataModel", Java2WSSelectSEIWidgetFactory.class); //$NON-NLS-1$
        dataRegistry.addMapping(Java2WSDefaultingCommand.class,
                "JavaStartingPointType", Java2WSSelectSEIWidgetFactory.class); //$NON-NLS-1$
    }

    public void setJava2WSDataModel(Java2WSDataModel model) {
        java2WSClassConfigWidget.setJava2WSDataModel(model);
        java2WSInterfaceConfigWidget.setJava2WSDataModel(model);
    }

    public void setJavaStartingPointType(IType startingPointType) {
        this.startingPointType = startingPointType;
        java2WSClassConfigWidget.setJavaStartingPointType(startingPointType);
        java2WSInterfaceConfigWidget.setJavaStartingPointType(startingPointType);
    }

    private void init() {
        classWidgetContributor = new SimpleWidgetContributor();
        CXFContext context = CXFCorePlugin.getDefault().getJava2WSContext();
        String classConfigTitle = CXFCreationUIMessages.bind(
                CXFCreationUIMessages.JAVA2WS_CLASS_CONFIG_PAGE_TITLE, 
                new Object[]{CXFCorePlugin.getEdition(), context.getCxfRuntimeVersion()});
        classWidgetContributor.setTitle(classConfigTitle);
        classWidgetContributor
                .setDescription(CXFCreationUIMessages.JAVA2WS_CLASS_CONFIG_PAGE_DESCRIPTION);
        classWidgetContributor.setFactory(new WidgetContributorFactory() {
            public WidgetContributor create() {
                return java2WSClassConfigWidget;
            }
        });

        interfaceWidgetContributor = new SimpleWidgetContributor();
        String interfaceConfigTitle = CXFCreationUIMessages.bind(
                CXFCreationUIMessages.JAVA2WS_INTERFACE_CONFIG_PAGE_TITLE,
                new Object[]{CXFCorePlugin.getEdition(), context.getCxfRuntimeVersion()});
        interfaceWidgetContributor.setTitle(interfaceConfigTitle);
        interfaceWidgetContributor
                .setDescription(CXFCreationUIMessages.JAVA2WS_INTERFACE_CONFIG_PAGE_DESCRIPTION);
        interfaceWidgetContributor.setFactory(new WidgetContributorFactory() {
            public WidgetContributor create() {
                return java2WSInterfaceConfigWidget;
            }
        });
    }

}
