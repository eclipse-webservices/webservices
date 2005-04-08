package org.eclipse.jst.ws.internal.axis.creation.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.AxisDeployCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.Java2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CheckAxisDeploymentDescriptorsTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUAxisInputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUAxisOutputCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.ComputeAxisSkeletonBeanCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.JavaToWSDLMethodCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.UpdateWEBXMLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.DefaultsForServerJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.LiteralSupportMessageTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.MoveDeploymentFilesTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.MoveJavaFilesTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.Skeleton2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.UpdateAxisWSDDFileTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.AxisBeanFragment;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.BUAxisDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.ValidateObjectSelectionCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.AxisSkeletonDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.SkeletonConfigWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.WSINonCompliantRuntimeCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.wst.command.env.common.WaitForAutoBuildCommand;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.ui.widgets.SelectionCommand;
import org.eclipse.wst.command.internal.provisional.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;

public class AxisWebService extends AbstractWebService
{
	

	public AxisWebService(WebServiceInfo info)
	{
		super(info);
	}

	public ICommandFactory assemble(Environment env, IContext ctx,
			ISelection sel, String module, String ear)
	{
		Vector commands = new Vector();
		commands.add(new TestCommand3(this));
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory deploy(Environment env, IContext ctx, ISelection sel,
			String module, String ear)
	{
		Vector commands = new Vector();
		commands.add(new TestCommand4(this));
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory develop(Environment env, IContext ctx, ISelection sel,
			String module, String ear)
	{
		registerDataMappings( env.getCommandManager().getMappingRegistry());
		
		Vector commands = new Vector();
//		if (ctx.getScenario().getValue() == WebServiceScenario.BOTTOMUP) {
			
			commands.add(new TestCommand1(this));
			commands.add(new TestCommand2(this));
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
		Vector commands = new Vector();
		commands.add(new TestCommand5(this));
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory run(Environment env, IContext ctx, ISelection sel,
			String module, String ear)
	{
		Vector commands = new Vector();
		commands.add(new TestCommand6(this));
		commands.add(new AxisDeployCommand());
		commands.add(new RefreshProjectCommand());
		return new SimpleCommandFactory(commands);
	}
	
 private class TestCommand1 extends SimpleCommand
	{
	 IWebService ws;
	 public TestCommand1(IWebService ws)
	 {
		 this.ws=ws;
	 }
	 public Status execute(Environment env)
	 {
		System.out.println("In develop command 1");
		ws.getWebServiceInfo().setWsdlURL("http://someWSDLURL");
		return super.execute(env);
	 }
	}

	private class TestCommand2 extends SimpleCommand
	{
		 IWebService ws;
		 public TestCommand2(IWebService ws)
		 {
			 this.ws=ws;
		 }
		 
		public Status execute(Environment env)
		{
			System.out.println("In develop command 2");
			ws.getWebServiceInfo().setImplURL("file://someImplURL");
			return super.execute(env);
		}
	}
	
	private class TestCommand3 extends SimpleCommand
	{
		 IWebService ws;
		 public TestCommand3(IWebService ws)
		 {
			 this.ws=ws;
		 }
		 
		public Status execute(Environment env)
		{
			System.out.println("In assemble command");
			return super.execute(env);
		}
	}
	
	private class TestCommand4 extends SimpleCommand
	{
		 IWebService ws;
		 public TestCommand4(IWebService ws)
		 {
			 this.ws=ws;
		 }
		 
		public Status execute(Environment env)
		{
			System.out.println("In deploy command");
			ws.getWebServiceInfo().setEndPointURL("http://someEndpointURL");
			return super.execute(env);
		}
	}
	
	private class TestCommand5 extends SimpleCommand
	{
		 IWebService ws;
		 public TestCommand5(IWebService ws)
		 {
			 this.ws=ws;
		 }
		 
		public Status execute(Environment env)
		{
			System.out.println("In install command");
			return super.execute(env);
		}
	}	
	
	private class TestCommand6 extends SimpleCommand
	{
		 IWebService ws;
		 public TestCommand6(IWebService ws)
		 {
			 this.ws=ws;
		 }
		 
		public Status execute(Environment env)
		{
			System.out.println("In run command");
			return super.execute(env);
		}
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
	        
	    //AxisDeployCommand
	    registry.addMapping(UpdateAxisWSDDFileTask.class, "JavaWSDLParam", AxisDeployCommand.class);
	    
	    // ServerExtensionOutputCommand
	    registry.addMapping(Java2WSDLCommand.class, "WsdlURI", BUAxisOutputCommand.class);    
	  }
}	

