package org.eclipse.jst.ws.internal.axis.creation.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.AxisDeployCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.Java2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CheckAxisDeploymentDescriptorsTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.AxisRunInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUAxisInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUAxisOutputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.JavaToWSDLMethodCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.UpdateWEBXMLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.DefaultsForServerJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.LiteralSupportMessageTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.MoveJavaFilesTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.UpdateAxisWSDDFileTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.AxisBeanFragment;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.BUAxisDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.ValidateObjectSelectionCommand;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.WSINonCompliantRuntimeCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.wst.command.env.common.WaitForAutoBuildCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.ui.widgets.SelectionCommand;
import org.eclipse.wst.command.internal.provisional.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;

public class AxisWebService extends AbstractWebService
{
	private AxisWebServiceInfo axisWebServiceInfo_;

	public AxisWebService(WebServiceInfo info)
	{
		super(info);
	}

	public ICommandFactory assemble(Environment env, IContext ctx,
			ISelection sel, String module, String ear)
	{
		return null;
	}

	public ICommandFactory deploy(Environment env, IContext ctx, ISelection sel,
			String module, String ear)
	{
		return null;
	}

	public ICommandFactory develop(Environment env, IContext ctx, ISelection sel,
			String module, String ear)
	{
		registerDataMappings( env.getCommandManager().getMappingRegistry());
		
		Vector commands = new Vector();
		
//		if (ctx.getScenario().getValue() == WebServiceScenario.BOTTOMUP) {
			
			commands.add(new BUAxisInputCommand(this, module));
			commands.add(new ValidateObjectSelectionCommand());
			commands.add(new BUAxisDefaultingCommand());
			commands.add(new WSINonCompliantRuntimeCommand());
			commands.add(new DefaultsForServerJavaWSDLCommand());
			commands.add(new JavaToWSDLMethodCommand());
			// commands.add(new SimpleFragment( "BeanConfig" ));
			// commands.add(new SimpleFragment( "AxisServiceBeanMapping" ));
			commands.add(new LiteralSupportMessageTask());
			commands.add(new CheckAxisDeploymentDescriptorsTask());
			commands.add(new CopyAxisJarCommand());
			commands.add(new AddJarsToProjectBuildPathTask());
			commands.add(new WaitForAutoBuildCommand());
			commands.add(new Java2WSDLCommand());
			commands.add(new RefreshProjectCommand());
			commands.add(new WSDL2JavaCommand());
			commands.add(new MoveJavaFilesTask());
			commands.add(new UpdateAxisWSDDFileTask());
			commands.add(new UpdateWEBXMLCommand());
			commands.add(new RefreshProjectCommand());
			commands.add(new BuildProjectCommand());
			commands.add(new StartProjectCommand());
			commands.add(new BUAxisOutputCommand(this));
			
//		} else if (ctx.getScenario().getValue() == WebServiceScenario.TOPDOWN) {
//			
//			commands.add(new TestCommand1(this));
//			commands.add(new AxisSkeletonDefaultingCommand());
//		    commands.add(new ValidateWSDLCommand());
//		    commands.add(new SkeletonConfigWidgetDefaultingCommand());
////			commands.add(new SimpleFragment( "SkeletonConfig" ));
////			commands.add(new SimpleFragment( "AxisMappingsWidget" ));
//		    commands.add(new CheckAxisDeploymentDescriptorsTask());
//		    commands.add(new AddJarsToProjectBuildPathTask());
//		    commands.add(new CopyAxisJarCommand());
//		    commands.add(new WSDL2JavaCommand());
//		    commands.add(new MoveDeploymentFilesTask());
//		    commands.add(new Skeleton2WSDLCommand());
//		    commands.add(new UpdateWEBXMLCommand());
//		    commands.add(new RefreshProjectCommand());
//		    commands.add(new BuildProjectCommand());
//		    commands.add(new StartProjectCommand());
//		    commands.add(new BuildProjectCommand());
//		    commands.add(new AxisDeployCommand());
//		    commands.add(new RefreshProjectCommand());
//			commands.add(new ComputeAxisSkeletonBeanCommand());
//		    commands.add(new OpenJavaEditorCommand());
//			
//		} else {
//			System.out.println("Error - WebServiceScenario should not be Client for AxisWebService");
//		}
		
		
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory install(Environment env, IContext ctx, ISelection sel,
			String module, String ear)
	{
		return null;
	}

	public ICommandFactory run(Environment env, IContext ctx, ISelection sel,
			String module, String ear)
	{
		Vector commands = new Vector();
		commands.add(new AxisRunInputCommand(this, module));
		commands.add(new AxisDeployCommand());
		commands.add(new RefreshProjectCommand());
		return new SimpleCommandFactory(commands);
	}
	
	public void registerDataMappings(DataMappingRegistry registry) 
	  {
		//BUAxisInputCommand
		registry.addMapping(BUAxisInputCommand.class, "ServiceServerTypeID", StartProjectCommand.class);

		
	    //ValidateObjectSelectionCommand
	    registry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ValidateObjectSelectionCommand.class);
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", ValidateObjectSelectionCommand.class,"ServiceProjectName", null );
	    
	    //BUAxisDefaultingCommand
	    registry.addMapping(SelectionCommand.class, "InitialSelection", BUAxisDefaultingCommand.class );
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
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", CheckAxisDeploymentDescriptorsTask.class, "ServerProject", new StringToIProjectTransformer());
	    
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
	    registry.addMapping(BUAxisInputCommand.class, "ServerProject", StartProjectCommand.class, "ServiceProject", new StringToIProjectTransformer());    
	    registry.addMapping(BUAxisDefaultingCommand.class, "SampleProject", StartProjectCommand.class);
//KSC   registry.addMapping(BUAxisDefaultingCommand.class, "ServiceServerTypeID", StartProjectCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "SampleServerTypeID", StartProjectCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "ServiceExistingServer", StartProjectCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "SampleExistingServer", StartProjectCommand.class);
	    registry.addMapping(BUAxisDefaultingCommand.class, "IsWebProjectStartupRequested",StartProjectCommand.class);
	        
		// BUAxisOutputCommand
	    registry.addMapping(Java2WSDLCommand.class, "WsdlURI", BUAxisOutputCommand.class);
		registry.addMapping(UpdateAxisWSDDFileTask.class, "JavaWSDLParam", BUAxisOutputCommand.class);    
		
		// Run extension
		
	    //AxisDeployCommand
	    registry.addMapping(AxisRunInputCommand.class, "JavaWSDLParam", AxisDeployCommand.class);
		//RefreshProjectCommand
	    registry.addMapping(AxisRunInputCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", new StringToIProjectTransformer());

	    
	    
	  }

	public AxisWebServiceInfo getAxisWebServiceInfo() {
		return axisWebServiceInfo_;
	}

	public void setAxisWebServiceInfo(AxisWebServiceInfo axisWebServiceInfo) {
		axisWebServiceInfo_ = axisWebServiceInfo;
	}
}	

