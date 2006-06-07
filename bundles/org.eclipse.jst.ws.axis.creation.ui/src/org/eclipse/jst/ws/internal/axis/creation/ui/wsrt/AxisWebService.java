/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060330   124667 kathy@ca.ibm.com - Kathy Chan
 * 20060330   128827 kathy@ca.ibm.com - Kathy Chan
 * 20060424   120137 kathy@ca.ibm.com - Kathy Chan
 * 20060509   125094 sengpl@ca.ibm.com - Seng Phung-Lu, Use WorkspaceModifyOperation
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060517   142327 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060524   130755 kathy@ca.ibm.com - Kathy Chan
 * 20060607   144978 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.creation.ui.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.AxisDeployCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.GeronimoAxisDeployCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.AxisCheckCompilerLevelCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.AxisOutputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.AxisRunInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUAxisInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUCodeGenOperation;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.ComputeAxisSkeletonBeanCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.CopyDeploymentFileCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.JavaToWSDLMethodCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.ModifyWSDLEndpointAddressCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.TDAxisInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.TDCodeGenOperation;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.ValidateWSIComplianceCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.BUConfigCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.DefaultsForServerJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.TDConfigCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.BUAxisDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.AxisSkeletonDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.SkeletonConfigWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ProjectName2IProjectTransformer;
import org.eclipse.wst.command.internal.env.common.WaitForAutoBuildCommand;
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

public class AxisWebService extends AbstractWebService
{
	private AxisWebServiceInfo axisWebServiceInfo_;

	public AxisWebService(WebServiceInfo info)
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
    EclipseEnvironment environment = (EclipseEnvironment)env;
		
		if (ctx.getScenario().getValue() == WebServiceScenario.BOTTOMUP) {
			
			registerBUDataMappings( environment.getCommandManager().getMappingRegistry());
			
			commands.add(new BUAxisInputCommand(this, project));
			commands.add(new AxisCheckCompilerLevelCommand());
//			commands.add(new ValidateObjectSelectionCommand());
			commands.add(new BUAxisDefaultingCommand());
			commands.add(new DefaultsForServerJavaWSDLCommand());
			commands.add(new JavaToWSDLMethodCommand());
			// commands.add(new SimpleFragment( "BeanConfig" ));
			// commands.add(new SimpleFragment( "AxisServiceBeanMapping" ));
			commands.add(new BUConfigCommand());
			commands.add(new ValidateWSIComplianceCommand());
			commands.add(new WaitForAutoBuildCommand());
			commands.add(new BUCodeGenOperation());
//			commands.add(new RefreshProjectCommand());
			commands.add(new BuildProjectCommand());
			commands.add(new AxisOutputCommand(this));
			
		} else if (ctx.getScenario().getValue() == WebServiceScenario.TOPDOWN) {
			
			registerTDDataMappings( environment.getCommandManager().getMappingRegistry());
			
			commands.add(new TDAxisInputCommand(this, project));
			commands.add(new AxisSkeletonDefaultingCommand());
		    commands.add(new ValidateWSDLCommand());
		    commands.add(new SkeletonConfigWidgetDefaultingCommand());
//			commands.add(new SimpleFragment( "SkeletonConfig" ));
//			commands.add(new SimpleFragment( "AxisMappingsWidget" ));
		    commands.add(new TDConfigCommand());
			commands.add(new TDCodeGenOperation());
//		    commands.add(new RefreshProjectCommand());
		    commands.add(new BuildProjectCommand());
			commands.add(new AxisOutputCommand(this));
			
		} else {
			//TODO: Remove println and reassess this "error".
			System.out.println("Error - WebServiceScenario should not be Client for AxisWebService");
			return null;
		}
		
		
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
		Vector commands = new Vector();

		if (ctx.getScenario().getValue() == WebServiceScenario.CLIENT) {
			//TODO: Remove println and reassess this "error".
			System.out.println("Error - WebServiceScenario should not be Client for AxisWebService");
			return null;
		} else {// For BOTTOM_UP and TOP_DOWN
			commands.add(new AxisRunInputCommand(this, project));
			commands.add(new ModifyWSDLEndpointAddressCommand());
			if (getWebServiceInfo().getServerFactoryId().equals("org.eclipse.jst.server.geronimo.10")) {
				commands.add(new GeronimoAxisDeployCommand(project));
			}
			else {
			    commands.add(new AxisDeployCommand());
			}
			commands.add( new CopyDeploymentFileCommand( project, earProject ) );
			commands.add(new RefreshProjectCommand());
			if (ctx.getScenario().getValue() == WebServiceScenario.TOPDOWN) {
				commands.add(new ComputeAxisSkeletonBeanCommand());
				commands.add(new OpenJavaEditorCommand());
			}
			return new SimpleCommandFactory(commands);
		}
	}
	
	public void registerBUDataMappings(DataMappingRegistry registry) 
	  {
		//BUAxisInputCommand
//		registry.addMapping(BUAxisInputCommand.class, "ServiceServerTypeID", StartProjectCommand.class);
		registry.addMapping(BUAxisInputCommand.class, "JavaBeanName", BUAxisDefaultingCommand.class );
		
	    //ValidateObjectSelectionCommand
//	    registry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ValidateObjectSelectionCommand.class);
//	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", ValidateObjectSelectionCommand.class,"ServiceProjectName", null );
	    
	    //BUAxisDefaultingCommand
//	    registry.addMapping(SelectionCommand.class, "InitialSelection", BUAxisDefaultingCommand.class );
//KSC   registry.addMapping(BUAxisInputCommand.class, "ServiceTypeRuntimeServer", BUAxisDefaultingCommand.class );
	    	    
		// AxisCheckCompilerLevelCommand
		registry.addMapping(BUAxisInputCommand.class, "ServerProject", AxisCheckCompilerLevelCommand.class);
		
	    //BUAxisCommands2 - these run after BeanClassWidget
	    //DefaultsForServerJavaWSDLCommand
	    registry.addMapping(BUAxisDefaultingCommand.class, "JavaWSDLParam", DefaultsForServerJavaWSDLCommand.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", DefaultsForServerJavaWSDLCommand.class, "ServiceProject", new StringToIProjectTransformer());
        registry.addMapping(BUAxisInputCommand.class, "ServiceServerTypeID", DefaultsForServerJavaWSDLCommand.class);        
	    registry.addMapping(BUAxisDefaultingCommand.class, "JavaBeanName", DefaultsForServerJavaWSDLCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "Parser", DefaultsForServerJavaWSDLCommand.class);
	    
	    //JavaWSDLMethodCommand
	    registry.addMapping(DefaultsForServerJavaWSDLCommand.class, "JavaWSDLParam", JavaToWSDLMethodCommand.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", JavaToWSDLMethodCommand.class, "ServiceProject", new StringToIProjectTransformer());

	    // BUAxisCommands3 - these run after BeanConfigWidget
    	    
	    //CopyAxisJarCommand
	    //registry.addMapping(BUAxisInputCommand.class, "ServerProject", CopyAxisJarCommand.class, "Project", new StringToIProjectTransformer());
	    	    
	    // ValidateWSIComplianceCommand
	    registry.addMapping(DefaultsForServerJavaWSDLCommand.class, "JavaWSDLParam", ValidateWSIComplianceCommand.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", ValidateWSIComplianceCommand.class, "ServiceProject", new StringToIProjectTransformer());
	    
	    //Java2WSDLCommand
	    registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", BUCodeGenOperation.class);
	    
	    //RefreshProjectCommand
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", new StringToIProjectTransformer());
	    
	    //UpdateWEBXMLCommand
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", BUCodeGenOperation.class, "ServiceProject", new StringToIProjectTransformer());
	    
	    //BuildProjectCommand
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", BuildProjectCommand.class, "Project", new StringToIProjectTransformer());
	    registry.addMapping(BUAxisDefaultingCommand.class, "ForceBuild", BuildProjectCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "ValidationManager", BuildProjectCommand.class);
	    
	    //StartProjectCommand
//	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", StartProjectCommand.class, "ServiceProject", new StringToIProjectTransformer());    
//	    registry.addMapping(BUAxisDefaultingCommand.class, "SampleProject", StartProjectCommand.class);
//KSC   registry.addMapping(BUAxisDefaultingCommand.class, "ServiceServerTypeID", StartProjectCommand.class);
//	    registry.addMapping(BUAxisDefaultingCommand.class, "SampleServerTypeID", StartProjectCommand.class);
//	    registry.addMapping(BUAxisDefaultingCommand.class, "ServiceExistingServer", StartProjectCommand.class);
//	    registry.addMapping(BUAxisDefaultingCommand.class, "SampleExistingServer", StartProjectCommand.class);
//	    registry.addMapping(BUAxisDefaultingCommand.class, "IsWebProjectStartupRequested",StartProjectCommand.class);
	        
		// AxisOutputCommand
	    registry.addMapping(BUCodeGenOperation.class, "WsdlURI", AxisOutputCommand.class);
	    registry.addMapping(BUCodeGenOperation.class, "JavaWSDLParam", AxisOutputCommand.class);	    
   
		
		// Run extension
		
//		ModifyWSDLEndpointAddressCommand
	    registry.addMapping(AxisRunInputCommand.class, "JavaWSDLParam", ModifyWSDLEndpointAddressCommand.class);
	    registry.addMapping(AxisRunInputCommand.class, "ServerInstanceId", ModifyWSDLEndpointAddressCommand.class);
	    registry.addMapping(AxisRunInputCommand.class, "ServerFactoryId", ModifyWSDLEndpointAddressCommand.class);
	    registry.addMapping(AxisRunInputCommand.class, "ServerProject", ModifyWSDLEndpointAddressCommand.class, "ServiceProject", new StringToIProjectTransformer());
	    registry.addMapping(AxisRunInputCommand.class, "WsdlURI", ModifyWSDLEndpointAddressCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "Parser", ModifyWSDLEndpointAddressCommand.class, "WebServicesParser", null);

		// GeronimoAxisDeployCommand
		registry.addMapping(ModifyWSDLEndpointAddressCommand.class, "JavaWSDLParam", GeronimoAxisDeployCommand.class);		
	    
		//AxisDeployCommand
	    registry.addMapping(ModifyWSDLEndpointAddressCommand.class, "JavaWSDLParam", AxisDeployCommand.class);
		
	    // CopyDeploymentFileCommand
	    registry.addMapping(AxisRunInputCommand.class, "ServerInstanceId", CopyDeploymentFileCommand.class);
	    
	    //RefreshProjectCommand
	    registry.addMapping(AxisRunInputCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", new StringToIProjectTransformer());
	    
	  }

	public void registerTDDataMappings(DataMappingRegistry dataRegistry)
	  {
	    // Transformers
	    ProjectName2IProjectTransformer projectTransformer = new ProjectName2IProjectTransformer();

		// TODO:  map "InitialSelection" and "ObjectSelection" from TDAxisInputCommand
		
//	    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", AxisSkeletonDefaultingCommand.class);
//	    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", AxisSkeletonDefaultingCommand.class);
//	    dataRegistry.addMapping(TDAxisInputCommand.class, "WebServicesParser", AxisSkeletonDefaultingCommand.class);
	    
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "JavaWSDLParam", SkeletonConfigWidgetDefaultingCommand.class);
		dataRegistry.addMapping(TDAxisInputCommand.class, "WsdlURI", AxisSkeletonDefaultingCommand.class);
	    // ValidateWSDLCommand
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", ValidateWSDLCommand.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WsdlURI", ValidateWSDLCommand.class);
	    
	    // SkeletonConfigWidgetDefaultingCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", SkeletonConfigWidgetDefaultingCommand.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WsdlURI", SkeletonConfigWidgetDefaultingCommand.class);
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServiceServerTypeID", SkeletonConfigWidgetDefaultingCommand.class);
	       
	    // AxisSkeletonDefaultingCommand
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WsdlURI", TDCodeGenOperation.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "HttpBasicAuthUsername", TDCodeGenOperation.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "HttpBasicAuthPassword", TDCodeGenOperation.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "JavaWSDLParam", TDCodeGenOperation.class);
	    
	    // CopyAxisJarCommand
	    //dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", CopyAxisJarCommand.class, "Project", projectTransformer);

	    // BackupSkelImplCommand
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", TDCodeGenOperation.class);      
	    dataRegistry.addMapping(TDAxisInputCommand.class, "WebServiceInfo", TDCodeGenOperation.class);
	    
	    // Skeleton2WSDLCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", TDCodeGenOperation.class, "ServerProject", projectTransformer);
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServiceServerTypeID", TDCodeGenOperation.class);	    

	    // RefreshProjectCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", projectTransformer);
	    
	    // BuildProjectCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", BuildProjectCommand.class, "Project", projectTransformer);

	    //	ModifyWSDLEndpointAddressCommand
	    dataRegistry.addMapping(AxisRunInputCommand.class, "JavaWSDLParam", ModifyWSDLEndpointAddressCommand.class);
	    dataRegistry.addMapping(AxisRunInputCommand.class, "ServerInstanceId", ModifyWSDLEndpointAddressCommand.class);
	    dataRegistry.addMapping(AxisRunInputCommand.class, "ServerFactoryId", ModifyWSDLEndpointAddressCommand.class);
	    dataRegistry.addMapping(AxisRunInputCommand.class, "ServerProject", ModifyWSDLEndpointAddressCommand.class, "ServiceProject", new StringToIProjectTransformer());
	    dataRegistry.addMapping(AxisRunInputCommand.class, "WsdlURI", ModifyWSDLEndpointAddressCommand.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", ModifyWSDLEndpointAddressCommand.class);

	    // GeronimoAxisDeployCommand
	    dataRegistry.addMapping(ModifyWSDLEndpointAddressCommand.class, "JavaWSDLParam", GeronimoAxisDeployCommand.class);
	    
	    // AxisDeployCommand
	    dataRegistry.addMapping(ModifyWSDLEndpointAddressCommand.class, "JavaWSDLParam", AxisDeployCommand.class);
	    
	    // CopyDeploymentFileCommand
	    dataRegistry.addMapping(AxisRunInputCommand.class, "ServerInstanceId", CopyDeploymentFileCommand.class);
	    
	    // AxisOutputCommand
	    dataRegistry.addMapping(TDCodeGenOperation.class, "WsdlURI", AxisOutputCommand.class);
	    dataRegistry.addMapping(TDCodeGenOperation.class, "JavaWSDLParam", AxisOutputCommand.class); 
	    
	    // ComputeAxisSkeletonBeanCommand
	    dataRegistry.addMapping(TDCodeGenOperation.class, "JavaWSDLParam", ComputeAxisSkeletonBeanCommand.class);
	    
	    // OpenJavaEditorCommand
	    dataRegistry.addMapping(ComputeAxisSkeletonBeanCommand.class, "ClassNames", OpenJavaEditorCommand.class);
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", OpenJavaEditorCommand.class, "Project", projectTransformer);
	    
	  }
	public AxisWebServiceInfo getAxisWebServiceInfo() {
		return axisWebServiceInfo_;
	}

	public void setAxisWebServiceInfo(AxisWebServiceInfo axisWebServiceInfo) {
		axisWebServiceInfo_ = axisWebServiceInfo;
	}
}	

