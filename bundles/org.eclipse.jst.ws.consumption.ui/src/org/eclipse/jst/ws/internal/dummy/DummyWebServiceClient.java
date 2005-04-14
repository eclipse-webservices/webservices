package org.eclipse.jst.ws.internal.dummy;

import java.util.Vector;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;

public class DummyWebServiceClient extends AbstractWebServiceClient
{		

	public DummyWebServiceClient(WebServiceClientInfo info)
	{
		super(info);
		// TODO Auto-generated constructor stub
	}

	public ICommandFactory assemble(Environment env, IContext ctx,
			ISelection sel, String project, String module, String earProject, String ear)
	{
		Vector commands = new Vector();
		commands.add(new TestCommand3(this));
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory deploy(Environment env, IContext ctx, ISelection sel,
      String project, String module, String earProject, String ear)
	{
		Vector commands = new Vector();
		commands.add(new TestCommand4(this));
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory develop(Environment env, IContext ctx, ISelection sel,
      String project, String module, String earProject, String ear)
	{
		Vector commands = new Vector();
		commands.add(new TestCommand1(this));
		commands.add(new TestCommand2(this));
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory install(Environment env, IContext ctx, ISelection sel,
      String project, String module, String earProject, String ear)
	{
		Vector commands = new Vector();
		commands.add(new TestCommand5(this));
		return new SimpleCommandFactory(commands);
	}

	public ICommandFactory run(Environment env, IContext ctx, ISelection sel,
      String project, String module, String earProject, String ear)
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
