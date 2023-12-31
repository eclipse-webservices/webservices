/*******************************************************************************
 * Copyright (c) 2008, 2009 IONA Technologies PLC
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
package org.eclipse.jst.ws.internal.cxf.consumption.ui.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.cxf.consumption.core.commands.WSDL2JavaClientCommand;
import org.eclipse.jst.ws.internal.cxf.consumption.core.commands.WSDL2JavaClientDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFFactory;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;

@SuppressWarnings("restriction")
public class CXFWebServiceClient extends AbstractWebServiceClient {

    public CXFWebServiceClient(WebServiceClientInfo info) {
        super(info);
    }

    @Override
    public ICommandFactory assemble(IEnvironment env, IContext ctx, ISelection sel, String project,
            String earProject) {
        return null;
    }

    @Override
    public ICommandFactory deploy(IEnvironment env, IContext ctx, ISelection sel, String project,
            String earProject) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ICommandFactory develop(IEnvironment env, IContext ctx, ISelection sel, String projectName,
            String earProject) {

        Vector commands = new Vector();

        WSDL2JavaDataModel wsdl2JavaModel = CXFFactory.eINSTANCE.createWSDL2JavaDataModel();
        commands.add(new WSDL2JavaClientDefaultingCommand(wsdl2JavaModel, projectName, 
        		getWebServiceClientInfo().getWsdlURL()));
        commands.add(new WSDL2JavaClientCommand(wsdl2JavaModel));

        return new SimpleCommandFactory(commands);
    }

    @Override
    public ICommandFactory install(IEnvironment env, IContext ctx, ISelection sel, String project,
            String earProject) {
        return null;
    }

    @Override
    public ICommandFactory run(IEnvironment env, IContext ctx, ISelection sel, String project,
            String earProject) {
        return null;
    }
}
