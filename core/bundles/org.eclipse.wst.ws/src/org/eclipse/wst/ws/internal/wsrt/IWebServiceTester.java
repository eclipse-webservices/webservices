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
package org.eclipse.wst.ws.internal.wsrt;

import org.eclipse.wst.command.internal.env.core.ICommandFactory;

public interface IWebServiceTester {
  public ICommandFactory generate(TestInfo testInfo);
  public ICommandFactory launch(TestInfo testInfo);
}
