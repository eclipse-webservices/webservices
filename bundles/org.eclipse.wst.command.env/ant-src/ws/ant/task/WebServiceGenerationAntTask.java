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
package ws.ant.task;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.eclipse.wst.command.env.ant.AntController;

public class WebServiceGenerationAntTask extends Task {
	
		public void execute()
		{
			//get properties set in the Ant file
			Project proj = getProject();
			
			// call the AntController to kick off generation
			new AntController(proj.getProperties());			
		}		
	}


