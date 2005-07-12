package org.eclipse.jst.ws.internal.axis.creation.ui.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.AxisDeployCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.Java2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.AxisOutputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.AxisRunInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUAxisInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.ComputeAxisSkeletonBeanCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.CopyDeploymentFileCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.JavaToWSDLMethodCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.TDAxisInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.UpdateWEBXMLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.BUCheckAxisDeploymentDescriptors;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.DefaultsForServerJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.LiteralSupportMessageTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.MoveDeploymentFilesTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.MoveJavaFilesTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.Skeleton2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.TDCheckAxisDeploymentDescriptors;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.UpdateAxisWSDDFileTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.AxisBeanFragment;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.BUAxisDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.AxisSkeletonDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.SkeletonConfigWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.WSINonCompliantRuntimeCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ProjectName2IProjectTransformer;
import org.eclipse.wst.command.internal.env.common.WaitForAutoBuildCommand;
import org.eclipse.wst.command.internal.provisional.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;

public class AxisWebService extends AbstractWebService
{
	private AxisWebServiceInfo axisWebServiceInfo_;

	public AxisWebService(WebServiceInfo info)
	{
		super(info);
	}

	public ICommandFactory assemble(Environment env, IContext ctx,
			ISelection sel, String project, String module, String earProject, String ear)
	{
		return null;
	}

	public ICommandFactory deploy(Environment env, IContext ctx, ISelection sel,
			String project, String module, String earProject, String ear)
	{
		return null;
	}

	public ICommandFactory develop(Environment env, IContext ctx, ISelection sel,
			String project, String module, String earProject, String ear)
	{
		
		Vector commands = new Vector();
		
		if (ctx.getScenario().getValue() == WebServiceScenario.BOTTOMUP) {
			
			registerBUDataMappings( env.getCommandManager().getMappingRegistry());
			
			commands.add(new BUAxisInputCommand(this, project, module));
//			commands.add(new ValidateObjectSelectionCommand());
			commands.add(new BUAxisDefaultingCommand());
			commands.add(new WSINonCompliantRuntimeCommand());
			commands.add(new DefaultsForServerJavaWSDLCommand(module));
			commands.add(new JavaToWSDLMethodCommand());
			// commands.add(new SimpleFragment( "BeanConfig" ));
			// commands.add(new SimpleFragment( "AxisServiceBeanMapping" ));
			commands.add(new BUCheckAxisDeploymentDescriptors(module));
			commands.add(new LiteralSupportMessageTask());
			commands.add(new CopyAxisJarCommand(module));
			commands.add(new AddJarsToProjectBuildPathTask(module));
			commands.add(new WaitForAutoBuildCommand());
			commands.add(new Java2WSDLCommand());
			commands.add(new RefreshProjectCommand());
			commands.add(new WSDL2JavaCommand());
			commands.add(new MoveJavaFilesTask(module));
			commands.add(new UpdateAxisWSDDFileTask(module));
			commands.add(new UpdateWEBXMLCommand(module));
			commands.add(new RefreshProjectCommand());
			commands.add(new BuildProjectCommand());
			commands.add(new AxisOutputCommand(this));
			
		} else if (ctx.getScenario().getValue() == WebServiceScenario.TOPDOWN) {
			
			registerTDDataMappings( env.getCommandManager().getMappingRegistry());
			
			commands.add(new TDAxisInputCommand(this, project, module));
			commands.add(new AxisSkeletonDefaultingCommand());
		    commands.add(new ValidateWSDLCommand());
		    commands.add(new SkeletonConfigWidgetDefaultingCommand(module));
//			commands.add(new SimpleFragment( "SkeletonConfig" ));
//			commands.add(new SimpleFragment( "AxisMappingsWidget" ));
		    commands.add(new TDCheckAxisDeploymentDescriptors(module));
			commands.add(new CopyAxisJarCommand(module));
		    commands.add(new AddJarsToProjectBuildPathTask(module));
		    commands.add(new WSDL2JavaCommand());
		    commands.add(new MoveDeploymentFilesTask(module));
		    commands.add(new Skeleton2WSDLCommand(module));
		    commands.add(new UpdateWEBXMLCommand(module));
		    commands.add(new RefreshProjectCommand());
		    commands.add(new BuildProjectCommand());
			commands.add(new AxisOutputCommand(this));
			
		} else {
			System.out.println("Error - WebServiceScenario should not be Client for AxisWebService");
			return null;
		}
		
		
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory install(Environment env, IContext ctx, ISelection sel,
			String project, String module, String earProject, String ear)
	{
		return null;
	}

	public ICommandFactory run(Environment env, IContext ctx, ISelection sel,
			String project, String module, String earProject, String ear)
	{
		Vector commands = new Vector();

		if (ctx.getScenario().getValue() == WebServiceScenario.CLIENT) {
			System.out
					.println("Error - WebServiceScenario should not be Client for AxisWebService");
			return null;
		} else {// For BOTTOM_UP and TOP_DOWN
			commands.add(new AxisRunInputCommand(this, project, module));
//			commands.add(new StartProjectCommand(module));
			commands.add(new AxisDeployCommand());
			commands.add( new CopyDeploymentFileCommand( project, module ) );
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
	    
	    //WSINonCompliantRuntimeCommand
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", WSINonCompliantRuntimeCommand.class, "ServiceProject", new StringToIProjectTransformer());
	    
	    //BUAxisCommands2 - these run after BeanClassWidget
	    //DefaultsForServerJavaWSDLCommand
	    registry.addMapping(BUAxisDefaultingCommand.class, "JavaWSDLParam", DefaultsForServerJavaWSDLCommand.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", DefaultsForServerJavaWSDLCommand.class, "ServiceProject", new StringToIProjectTransformer());
	    registry.addMapping(BUAxisDefaultingCommand.class, "JavaBeanName", DefaultsForServerJavaWSDLCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "Parser", DefaultsForServerJavaWSDLCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "CustomizeServiceMappings", AxisBeanFragment.MappingFragment.class);
	    
	    //JavaWSDLMethodCommand
	    registry.addMapping(DefaultsForServerJavaWSDLCommand.class, "JavaWSDLParam", JavaToWSDLMethodCommand.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", JavaToWSDLMethodCommand.class, "ServiceProject", new StringToIProjectTransformer());

	    // BUAxisCommands3 - these run after BeanConfigWidget
	    //LiteralSupportMessageTask
	    registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", LiteralSupportMessageTask.class);
	    
	    //CheckAxisDeploymentDescriptorsTask
	    //registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", CheckAxisDeploymentDescriptorsTask.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", BUCheckAxisDeploymentDescriptors.class, "ServerProject", new StringToIProjectTransformer());
	    
	    //CopyAxisJarCommand
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", CopyAxisJarCommand.class, "Project", new StringToIProjectTransformer());
	    
	    //AddJarsToProjectBuildPathTask
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", AddJarsToProjectBuildPathTask.class, "Project", new StringToIProjectTransformer());
	    
	    //Java2WSDLCommand
	    registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", Java2WSDLCommand.class);
	    
	    //RefreshProjectCommand
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", new StringToIProjectTransformer());
	    
	    //WSDL2JavaCommand
	    registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", WSDL2JavaCommand.class);
	    

	    //MoveJavaFilesTask
	    registry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", MoveJavaFilesTask.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", MoveJavaFilesTask.class, "ServiceProject", new StringToIProjectTransformer());
	    
	    //UpdateAxisWSDDFileTask
	    registry.addMapping(MoveJavaFilesTask.class, "JavaWSDLParam", UpdateAxisWSDDFileTask.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", UpdateAxisWSDDFileTask.class, "ServiceProject", new StringToIProjectTransformer());
	    
	    //UpdateWEBXMLCommand
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", UpdateWEBXMLCommand.class, "ServerProject", new StringToIProjectTransformer());
	    
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
	    registry.addMapping(Java2WSDLCommand.class, "WsdlURI", AxisOutputCommand.class);
		registry.addMapping(UpdateAxisWSDDFileTask.class, "JavaWSDLParam", AxisOutputCommand.class);    
		
		// Run extension
		
	    //AxisDeployCommand
	    registry.addMapping(AxisRunInputCommand.class, "JavaWSDLParam", AxisDeployCommand.class);
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
	    
	    // CheckAxisDeploymentDescriptorsTask
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", TDCheckAxisDeploymentDescriptors.class, "ServerProject", projectTransformer);

	    // AddJarsToProjectBuildPathTask
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", AddJarsToProjectBuildPathTask.class, "Project", projectTransformer);
	    
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WsdlURI", WSDL2JavaCommand.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "HttpBasicAuthUsername", WSDL2JavaCommand.class);
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "HttpBasicAuthPassword", WSDL2JavaCommand.class);

//		WSDL2JavaCommand
	  	dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "JavaWSDLParam", WSDL2JavaCommand.class);
		
	    // MoveDeploymentFilesTask
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", MoveDeploymentFilesTask.class, "ServerProject", projectTransformer);
	    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", MoveDeploymentFilesTask.class);

	    // CopyAxisJarCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", CopyAxisJarCommand.class, "Project", projectTransformer);

	    // Skeleton2WSDLCommand
	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", Skeleton2WSDLCommand.class);
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", Skeleton2WSDLCommand.class, "ServerProject", projectTransformer);
	    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", Skeleton2WSDLCommand.class);

	    // UpdateWEBXMLCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", UpdateWEBXMLCommand.class, "ServerProject", projectTransformer);

	    // RefreshProjectCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", projectTransformer);
	    
	    // BuildProjectCommand
	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", BuildProjectCommand.class, "Project", projectTransformer);

	    // StartProjectCommand
//	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerProject", StartProjectCommand.class, "ServiceProject", projectTransformer);
//	    dataRegistry.addMapping(TDAxisInputCommand.class, "ServerServer", StartProjectCommand.class, "ServiceExistingServer", new ServerName2IServerTransformer());
//	    dataRegistry.addMapping(CopyAxisJarCommand.class, "ProjectRestartRequired", StartProjectCommand.class, "IsWebProjectStartupRequested", null);

	    // AxisDeployCommand
	    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", AxisDeployCommand.class);
	    
	    // AxisOutputCommand
	    dataRegistry.addMapping(Skeleton2WSDLCommand.class, "WsdlURI", AxisOutputCommand.class);
		dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", AxisOutputCommand.class); 
//	    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", AxisOutputCommand.class);
	    
	    // ComputeAxisSkeletonBeanCommand
	    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", ComputeAxisSkeletonBeanCommand.class);
	    //dataRegistry.addMapping(Skeleton2WSDLCommand.class, "WsdlURI", ComputeAxisSkeletonBeanCommand.class);
	    //dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", ComputeAxisSkeletonBeanCommand.class);
	    
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

