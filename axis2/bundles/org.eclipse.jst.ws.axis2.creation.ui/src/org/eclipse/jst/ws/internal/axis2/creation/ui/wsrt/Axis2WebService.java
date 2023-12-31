/*******************************************************************************
 * Copyright (c) 2007, 2008 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 runtime to the framework for 168762
 * 20070425   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070815   187840 sandakith@wso2.com - Lahiru Sandakith
 * 20080129   209411 kathy@ca.ibm.com - Kathy Chan
 * 20080620   192527 samindaw@wso2.com - Saminda Wijeratne, Update the model information with the axis2 preference settings
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.creation.ui.wsrt;

import java.util.Vector;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.axis2.core.context.PersistentAxis2EmitterContext;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2BUCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2BUServiceCreationCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2BuildProjectCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2CleanupCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2DefaultingCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2ServicesXMLValidationCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2SkelImplCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2TDCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2TDServiceCreationCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2WSDL2JavaCommand;
import org.eclipse.jst.ws.axis2.creation.core.command.Axis2WebservicesServerCommand;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebService;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class Axis2WebService extends AbstractWebService
{
//	private Axis2WebServiceInfo axis2WebServiceInfo;

	public Axis2WebService(WebServiceInfo info)
	{
		super(info);
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
		Vector commands = new Vector();
		DataModel model = new DataModel();
		//EclipseEnvironment environment = (EclipseEnvironment)env;

		model.setWebProjectName(project);
		setupDataModelDefaultPreferenceValues(model);
		if (ctx.getScenario().getValue() == WebServiceScenario.BOTTOMUP)	{ 
			commands.add(new Axis2DefaultingCommand( model,this, ctx.getScenario().getValue() ) );
			commands.add(new Axis2BUCommand( model ) );
			commands.add(new Axis2ServicesXMLValidationCommand());
			commands.add(new Axis2BUServiceCreationCommand(model,this,project));
			commands.add(new Axis2WebservicesServerCommand(model, ctx.getScenario().getValue() ));
			//Including command to build for the fix 187840
			commands.add( new Axis2BuildProjectCommand(
					ResourcesPlugin.getWorkspace().getRoot().getProject(project),true));
		} 
		else if (ctx.getScenario().getValue() == WebServiceScenario.TOPDOWN) {  
			commands.add(new Axis2DefaultingCommand( model,this, ctx.getScenario().getValue()  ) );
			commands.add(new Axis2TDCommand( model) );
			commands.add(new Axis2WSDL2JavaCommand( model) );
			commands.add( new Axis2BuildProjectCommand(
						ResourcesPlugin.getWorkspace().getRoot().getProject(project),true));
			commands.add(new Axis2TDServiceCreationCommand(model,this,project));
			commands.add(new Axis2WebservicesServerCommand(model, ctx.getScenario().getValue()));
			//yes, again invoke the build command to redeploy the service after complete full build
			commands.add( new Axis2BuildProjectCommand(
						ResourcesPlugin.getWorkspace().getRoot().getProject(project),true));
			commands.add(new Axis2SkelImplCommand(this.getWebServiceInfo(),model));
			commands.add(new Axis2CleanupCommand());
		} 
		else 
		{
			return null;
		}

		return new SimpleCommandFactory(commands);
	}

	public void setupDataModelDefaultPreferenceValues(DataModel model){
		PersistentAxis2EmitterContext axis2Pref = PersistentAxis2EmitterContext.getInstance();
		model.setGenerateServerSideInterface(axis2Pref.isServiceInterfaceSkeleton());
		model.setGenerateAllCheck(axis2Pref.isServiceGenerateAll());
	}
	
	public ICommandFactory install(IEnvironment env, IContext ctx, ISelection sel,
			String project, String earProject)
	{
		return null;
	}

	public ICommandFactory run(IEnvironment env, IContext ctx, ISelection sel,
			String project, String earProject)
	{
		Vector commands = new Vector();

		
			commands.add(new Axis2CheckWSDLCommand(this));
			

			return new SimpleCommandFactory(commands);

	}
}
