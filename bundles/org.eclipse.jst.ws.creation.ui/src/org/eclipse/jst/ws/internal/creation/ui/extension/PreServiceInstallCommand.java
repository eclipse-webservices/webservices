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

package org.eclipse.jst.ws.internal.creation.ui.extension;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;

public class PreServiceInstallCommand extends SimpleCommand 
{
	  public Status execute(Environment environment) 
	  {
		System.out.println( "In Pre service install command." );

		return super.execute(environment);
	  }

}
