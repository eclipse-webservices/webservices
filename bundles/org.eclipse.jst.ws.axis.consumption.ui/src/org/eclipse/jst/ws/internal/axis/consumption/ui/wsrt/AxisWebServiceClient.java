package org.eclipse.jst.ws.internal.axis.consumption.ui.wsrt;

import java.util.Vector;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientInputCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientOutputCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.DefaultsForClientJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.Stub2BeanCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.provisional.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;

public class AxisWebServiceClient extends AbstractWebServiceClient
{		

	public AxisWebServiceClient(WebServiceClientInfo info)
	{
		super(info);
		// TODO Auto-generated constructor stub
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
    EclipseEnvironment environment = (EclipseEnvironment)env;
		registerDataMappings( environment.getCommandManager().getMappingRegistry());
		
		Vector commands = new Vector();
		commands.add(new AxisClientInputCommand(this, ctx, project));
		commands.add(new AxisClientDefaultingCommand());
//		commands.add(new SimpleFragment("AxisClientStart"));
//		commands.add(new SimpleFragment("AxisClientBeanMapping"));
		commands.add(new DefaultsForHTTPBasicAuthCommand());
		commands.add(new CopyAxisJarCommand());
		commands.add(new DefaultsForClientJavaWSDLCommand());
		commands.add(new ValidateWSDLCommand());
		commands.add(new WSDL2JavaCommand());
		commands.add(new RefreshProjectCommand());
		commands.add(new Stub2BeanCommand());
		commands.add(new AxisClientOutputCommand(this,ctx));
		commands.add(new BuildProjectCommand());
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
		
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", CopyAxisJarCommand.class, "Project", null);

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
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", WSDL2JavaCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL", WSDL2JavaCommand.class, "WsdlURI", null); // URL
																													// to
																													// URI??
		// RefreshProjectCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", RefreshProjectCommand.class, "Project", null);
		// Stub2BeanCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", Stub2BeanCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WebServicesParser", Stub2BeanCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", Stub2BeanCommand.class);
		// BuildProjectCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", BuildProjectCommand.class, "Project", null);
		registry.addMapping(AxisClientDefaultingCommand.class, "ForceBuild", BuildProjectCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "ValidationManager", BuildProjectCommand.class);
		
		registry.addMapping(Stub2BeanCommand.class, "ProxyBean", AxisClientOutputCommand.class, "ProxyBean", null);
//		registry.addMapping(AxisClientDefaultingCommand.class, "GenerateProxy", ClientExtensionOutputCommand.class);
//		registry.addMapping(AxisClientDefaultingCommand.class, "SetEndpointMethod", ClientExtensionOutputCommand.class);
	}
}
