/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060216   115144 pmoogk@ca.ibm.com - Peter Moogk
 * 20060509   125094 sengpl@ca.ibm.com - Seng Phung-Lu, Use WorkspaceModifyOperation
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060728   145426 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.ui.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientInputCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientOutputCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.DefaultsForClientJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ClientCodeGenOperation;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;

public class AxisWebServiceClient extends AbstractWebServiceClient
{		

	public AxisWebServiceClient(WebServiceClientInfo info)
	{
		super(info);
		// TODO Auto-generated constructor stub
	}

	public ICommandFactory assemble(IEnvironment env, IContext ctx,
			ISelection sel, String project, String earProject)
	{
		return null;
	}

	public ICommandFactory deploy(IEnvironment env, IContext ctx, ISelection sel,
			String project, String earProject)
	{
		return null;
	}

	public ICommandFactory develop(IEnvironment env, IContext ctx, ISelection sel,
			String project, String earProject)
	{
    EclipseEnvironment environment = (EclipseEnvironment)env;
		registerDataMappings( environment.getCommandManager().getMappingRegistry());
		
		Vector commands = new Vector();
		commands.add(new AxisClientInputCommand(this, ctx, project));
		commands.add(new AxisClientDefaultingCommand());
//		commands.add(new SimpleFragment("AxisClientStart"));
//		commands.add(new SimpleFragment("AxisClientBeanMapping"));
		commands.add(new DefaultsForHTTPBasicAuthCommand());
//		commands.add(new CopyAxisJarCommand());
		commands.add(new DefaultsForClientJavaWSDLCommand());
		commands.add(new ValidateWSDLCommand());
		commands.add(new ClientCodeGenOperation());
		commands.add(new AxisClientOutputCommand(this,ctx));
		commands.add(new BuildProjectCommand());
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory install(IEnvironment env, IContext ctx, ISelection sel,
			String project, String earProject)
	{
		return null;
	}

	public ICommandFactory run(IEnvironment env, IContext ctx, ISelection sel,
			String project, String earProject)
	{
		return null;
	}
		
	public void registerDataMappings(DataMappingRegistry registry) 
	{
		// AxisClientDefaultingCommand
		registry.addMapping(AxisClientInputCommand.class, "ClientProject", AxisClientDefaultingCommand.class, "ClientProject",
				new StringToIProjectTransformer());
//		registry.addMapping(ClientExtensionDefaultingCommand.class, "ClientRuntime", AxisClientDefaultingCommand.class, "ClientRuntimeID",
//				null);
//		registry.addMapping(ClientExtensionDefaultingCommand.class, "WebServicesParser", AxisClientDefaultingCommand.class);
//		registry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", AxisClientDefaultingCommand.class,
//				"ClientProjectEAR", new StringToIProjectTransformer());
		registry.addMapping(AxisClientInputCommand.class, "WsdlURL", AxisClientDefaultingCommand.class); // URI
																																	// to
		// URL
		// transformer
		// req'd??
//		registry.addMapping(ClientExtensionDefaultingCommand.class, "TestProxySelected", AxisClientDefaultingCommand.class,
//				"TestProxySelected", null);
		registry.addMapping(AxisClientInputCommand.class, "ClientServer", AxisClientDefaultingCommand.class);
//		registry.addMapping(ClientExtensionDefaultingCommand.class, "IsClientScenario", AxisClientDefaultingCommand.class);
		registry.addMapping(AxisClientInputCommand.class, "GenerateProxy", AxisClientDefaultingCommand.class);
		// DefaultsForHTTPBasicAuthCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", DefaultsForHTTPBasicAuthCommand.class); //OK
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL", DefaultsForHTTPBasicAuthCommand.class, "WsdlServiceURL", null); //OK
		registry.addMapping(AxisClientDefaultingCommand.class, "WebServicesParser", DefaultsForHTTPBasicAuthCommand.class); //OK    
		
//		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", CopyAxisJarCommand.class, "Project", null);

		// DefaultsForClientJavaWSDLCommand() // javaParam_, model_
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", DefaultsForClientJavaWSDLCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", DefaultsForClientJavaWSDLCommand.class, "ProxyProject",
				null);
		//		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL",
		// DefaultsForClientJavaWSDLCommand.class,"WSDLServicePathname",null);
		// //
		// URL to URL??
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL", DefaultsForClientJavaWSDLCommand.class, "WSDLServiceURL", null); // URI
		// to
		// URL??
		// ValidateWSDLCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", ValidateWSDLCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlServiceURL", ValidateWSDLCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WebServicesParser", ValidateWSDLCommand.class);

		// WSDL2JavaCommand() // javaParam_
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", ClientCodeGenOperation.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL", ClientCodeGenOperation.class, "WsdlURI", null);

		// RefreshProjectCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", ClientCodeGenOperation.class, "Project", null);		
		
		// Stub2BeanCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "WebServicesParser", ClientCodeGenOperation.class);
		registry.addMapping(DefaultsForClientJavaWSDLCommand.class, "OutputFolder", ClientCodeGenOperation.class );
		
		// BuildProjectCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", BuildProjectCommand.class, "Project", null);
		registry.addMapping(AxisClientDefaultingCommand.class, "ForceBuild", BuildProjectCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "ValidationManager", BuildProjectCommand.class);
		
		registry.addMapping(ClientCodeGenOperation.class, "ProxyBean", AxisClientOutputCommand.class, "ProxyBean", null);
		registry.addMapping(ClientCodeGenOperation.class, "ProxyEndpoint", AxisClientOutputCommand.class);
//		registry.addMapping(AxisClientDefaultingCommand.class, "GenerateProxy", ClientExtensionOutputCommand.class);
//		registry.addMapping(AxisClientDefaultingCommand.class, "SetEndpointMethod", ClientExtensionOutputCommand.class);
	}
}
