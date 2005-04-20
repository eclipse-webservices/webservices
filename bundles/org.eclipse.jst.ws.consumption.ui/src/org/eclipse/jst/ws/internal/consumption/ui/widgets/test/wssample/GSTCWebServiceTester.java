package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import java.util.Vector;

import org.eclipse.wst.command.internal.provisional.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommandFactory;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceTester;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

public class GSTCWebServiceTester implements IWebServiceTester  {

  public ICommandFactory generate(TestInfo testInfo){
    Vector commands = new Vector();
	commands.add(new GSTCGenerateCommand(testInfo));
	return new SimpleCommandFactory(commands);
  }
  
  public ICommandFactory launch(TestInfo testInfo){
	Vector commands = new Vector();
    commands.add(new GSTCLaunchCommand(testInfo));
    return new SimpleCommandFactory(commands);	
  }
	
}
