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
package org.eclipse.jst.ws.internal.cxf.creation.ui.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFFactory;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.CXFDeployCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.JAXWSAnnotateJavaCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.Java2WSCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.Java2WSDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.Java2WSSelectSEICommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.Java2WSValidateInputCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.WSDL2JavaDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.WSDL2JavaProjectSelectionCommand;
import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebService;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

@SuppressWarnings("restriction")
public class CXFWebService extends AbstractWebService {

    public CXFWebService(WebServiceInfo info) {
        super(info);
    }

    @Override
    public ICommandFactory assemble(IEnvironment environment, IContext ctx, ISelection selection, 
            String projectName, String earProject) {
        return null;
    }

    @Override
    public ICommandFactory deploy(IEnvironment environment, IContext ctx, ISelection selection, 
            String projectName, String earProject) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ICommandFactory develop(IEnvironment environment, IContext ctx, ISelection selection,
            String projectName, String earProject) {
        Vector commands = new Vector();

        EclipseEnvironment eclipseEnvironment = (EclipseEnvironment) environment;
        registerDataMappings(eclipseEnvironment.getCommandManager().getMappingRegistry());

        if (ctx.getScenario().getValue() == WebServiceScenario.BOTTOMUP) {
            Java2WSDataModel java2WSDataModel = CXFFactory.eINSTANCE.createJava2WSDataModel();
            java2WSDataModel.setProjectName(projectName);
            java2WSDataModel.setJavaStartingPoint(this.getWebServiceInfo().getImplURL());
            commands.add(new Java2WSValidateInputCommand(java2WSDataModel));
            commands.add(new Java2WSDefaultingCommand(java2WSDataModel));
            commands.add(new Java2WSSelectSEICommand(java2WSDataModel));
            commands.add(new JAXWSAnnotateJavaCommand(java2WSDataModel));
            commands.add(new Java2WSCommand(java2WSDataModel));
        } else if (ctx.getScenario().getValue() == WebServiceScenario.TOPDOWN) {
            WSDL2JavaDataModel wsdl2JavaDataModel = CXFFactory.eINSTANCE.createWSDL2JavaDataModel();
            wsdl2JavaDataModel.setProjectName(projectName);
            commands.add(new WSDL2JavaProjectSelectionCommand(wsdl2JavaDataModel));
            commands.add(new WSDL2JavaDefaultingCommand(wsdl2JavaDataModel, projectName, 
            		getWebServiceInfo().getWsdlURL()));
            commands.add(new WSDL2JavaCommand(wsdl2JavaDataModel, this));
        } else {
            return null;
        }

        return new SimpleCommandFactory(commands);
    }
    
    public void registerDataMappings(DataMappingRegistry dataRegistry) {
        dataRegistry.addMapping(Java2WSCommand.class, "CXFDataModel", CXFDeployCommand.class); //$NON-NLS-1$
        dataRegistry.addMapping(WSDL2JavaCommand.class, "CXFDataModel", CXFDeployCommand.class); //$NON-NLS-1$
        dataRegistry.addMapping(CXFDeployCommand.class, "ClientComponentType",  //$NON-NLS-1$
                ClientExtensionDefaultingCommand.class);
    }

    @Override
    public ICommandFactory install(IEnvironment environment, IContext ctx, ISelection selection, 
            String projectName, String earProject) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ICommandFactory run(IEnvironment environment, IContext ctx, ISelection selection, 
            String projectName, String earProject) {
        Vector commands = new Vector();
        commands.add(new CXFDeployCommand(projectName, this));
        return new SimpleCommandFactory(commands);
    }

    @Override
    public WebServiceInfo getWebServiceInfo() {
        return super.getWebServiceInfo();
    }
}
