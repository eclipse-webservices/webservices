package org.eclipse.jst.ws.internal.barney;

import java.util.Vector;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;

public class BarneyWebService extends AbstractWebService
{
	

	public BarneyWebService(WebServiceInfo info)
	{
		super(info);
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
	 IWebService ws;
	 public TestCommand1(IWebService ws)
	 {
		 this.ws=ws;
	 }
	 public Status execute(Environment env)
	 {
		System.out.println("In barney develop command 1");
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
			System.out.println("In barney develop command 2");
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
			System.out.println("In barney assemble command");
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
			System.out.println("In barney deploy command");
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
			System.out.println("In barney install command");
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
			System.out.println("In barney run command");
			return super.execute(env);
		}
	}	
}	

