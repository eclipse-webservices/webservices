package org.eclipse.jst.ws.internal.axis.consumption.wsrt;

import java.util.Vector;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.DefaultsForClientJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.Stub2BeanCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisClientFragment.MappingFragment;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.provisional.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;

public class AxisWebServiceClient extends AbstractWebServiceClient
{		

	public AxisWebServiceClient(WebServiceClientInfo info)
	{
		super(info);
		// TODO Auto-generated constructor stub
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
		Vector commands = new Vector();
		commands.add(new TestCommand1(this));
		commands.add(new TestCommand2(this));
		commands.add(new AxisClientDefaultingCommand());
//		commands.add(new SimpleFragment("AxisClientStart"));
//		commands.add(new SimpleFragment("AxisClientBeanMapping"));
		commands.add(new DefaultsForHTTPBasicAuthCommand());
		commands.add(new CopyAxisJarCommand());
		commands.add(new AddJarsToProjectBuildPathTask());
		commands.add(new DefaultsForClientJavaWSDLCommand());
		commands.add(new ValidateWSDLCommand());
		commands.add(new WSDL2JavaCommand());
		commands.add(new RefreshProjectCommand());
		commands.add(new Stub2BeanCommand());
		commands.add(new BuildProjectCommand());
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
		return new SimpleCommandFactory(commands);
	}

	 private class TestCommand1 extends SimpleCommand
		{
		 IWebServiceClient ws;
		 public TestCommand1(IWebServiceClient ws)
		 {
			 this.ws=ws;
		 }
		 public Status execute(Environment env)
		 {
			System.out.println("In client develop command 1");
			return super.execute(env);
		 }
		}

		private class TestCommand2 extends SimpleCommand
		{
			 IWebServiceClient ws;
			 public TestCommand2(IWebServiceClient ws)
			 {
				 this.ws=ws;
			 }
			 
			public Status execute(Environment env)
			{
				System.out.println("In client develop command 2");
				return super.execute(env);
			}
		}
		
		private class TestCommand3 extends SimpleCommand
		{
			 IWebServiceClient ws;
			 public TestCommand3(IWebServiceClient ws)
			 {
				 this.ws=ws;
			 }
			 
			public Status execute(Environment env)
			{
				System.out.println("In client assemble command");
				return super.execute(env);
			}
		}
		
		private class TestCommand4 extends SimpleCommand
		{
			 IWebServiceClient ws;
			 public TestCommand4(IWebServiceClient ws)
			 {
				 this.ws=ws;
			 }
			 
			public Status execute(Environment env)
			{
				System.out.println("In client deploy command");
				return super.execute(env);
			}
		}
		
		private class TestCommand5 extends SimpleCommand
		{
			 IWebServiceClient ws;
			 public TestCommand5(IWebServiceClient ws)
			 {
				 this.ws=ws;
			 }
			 
			public Status execute(Environment env)
			{
				System.out.println("In client install command");
				return super.execute(env);
			}
		}	
		
		private class TestCommand6 extends SimpleCommand
		{
			 IWebServiceClient ws;
			 public TestCommand6(IWebServiceClient ws)
			 {
				 this.ws=ws;
			 }
			 
			public Status execute(Environment env)
			{
				System.out.println("In client run command");
				return super.execute(env);
			}
		}		
}
