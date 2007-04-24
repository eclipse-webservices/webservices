/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070230   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.axis2.consumption.core.command.Axis2ClientCodegenCommand;
import org.eclipse.jst.ws.axis2.consumption.core.command.Axis2ClientDefaultingCommand;
import org.eclipse.jst.ws.axis2.consumption.core.command.Axis2WebservicesServerCommand;
import org.eclipse.jst.ws.axis2.consumption.core.data.DataModel;
import org.eclipse.jst.ws.internal.axis2.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;

public class Axis2WebServiceClient extends AbstractWebServiceClient {

	public Axis2WebServiceClient(WebServiceClientInfo info) {
		super(info);
	}

	public ICommandFactory assemble(IEnvironment env, IContext ctx,
			ISelection arg2, String arg3, String arg4) {
		return null;
	}

	public ICommandFactory deploy(IEnvironment env, IContext ctx,
			ISelection arg2, String arg3, String arg4) {
		return null;
	}

	public ICommandFactory develop(IEnvironment env, IContext ctx,
			ISelection sel, String project, String earProject) {
		DataModel model = new DataModel();
		
		EclipseEnvironment environment = (EclipseEnvironment)env;
		registerDataMappings( environment.getCommandManager().getMappingRegistry());
		model.setWebProjectName(project);
		Vector commands = new Vector();
		commands.add(new Axis2ClientDefaultingCommand(model,this));
		commands.add(new Axis2WebservicesServerCommand(model, project));
		commands.add(new Axis2ClientCodegenCommand(model));
		
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory install(IEnvironment env, IContext ctx,
			ISelection sel, String project, String earProject) {
		return null;
	}

	public ICommandFactory run(IEnvironment env, IContext ctx,
			ISelection sel, String project, String earProject) {
		return null;
	}

	public void registerDataMappings(DataMappingRegistry registry){
		// AxisClientDefaultingCommand
		registry.addMapping(Axis2ClientDefaultingCommand.class, 
							"WsdlURL", 
							DefaultsForHTTPBasicAuthCommand.class,
							"WsdlServiceURL", null); //OK

	}

}
