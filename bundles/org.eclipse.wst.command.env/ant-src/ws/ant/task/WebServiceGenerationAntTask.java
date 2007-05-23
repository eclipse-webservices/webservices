/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070522   176943 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package ws.ant.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.eclipse.wst.command.internal.env.ant.AntController;

public class WebServiceGenerationAntTask extends Task 
{	
		public void execute()
		{			
			//get properties set in the Ant file
			Project proj = getProject();
      
      if( proj != null )
      {
        //call the AntController to kick off generation
    		AntController controller   = new AntController(proj.getProperties());
        String        errorMessage = controller.getErrorMessage();
        
        if( errorMessage != null )
        {
          throw new BuildException( errorMessage );
        }
      }           		
		}		
}
