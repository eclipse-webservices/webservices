/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

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
