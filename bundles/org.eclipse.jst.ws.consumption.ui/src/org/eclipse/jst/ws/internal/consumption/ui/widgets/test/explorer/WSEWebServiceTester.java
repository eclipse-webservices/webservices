package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.explorer;

import java.util.Vector;

import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceTester;
import org.eclipse.wst.ws.internal.wsrt.TestInfo;

public class WSEWebServiceTester implements IWebServiceTester  {

  public ICommandFactory generate(TestInfo testInfo){
    Vector commands = new Vector();
	return new SimpleCommandFactory(commands);
  }
	  
  public ICommandFactory launch(TestInfo testInfo){
    Vector commands = new Vector();
	commands.add(new WSEGenerateCommand(testInfo));
	return new SimpleCommandFactory(commands);	
  }

}
