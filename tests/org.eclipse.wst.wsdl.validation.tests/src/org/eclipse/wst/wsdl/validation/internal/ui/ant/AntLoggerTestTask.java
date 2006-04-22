/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * A dummy task that will allow testing of the and logger.
 */
public class AntLoggerTestTask extends Task 
{
  protected List errors = new ArrayList();
  protected List warnings = new ArrayList();
  protected List infos = new ArrayList();
  protected List verboses = new ArrayList();
  
  /* (non-Javadoc)
   * @see org.apache.tools.ant.Task#log(java.lang.String, int)
   */
  public void log(String msg, int msgLevel) 
  {
	if(msgLevel == Project.MSG_ERR)
	{
	  errors.add(msg);
	}
	else if(msgLevel == Project.MSG_WARN)
	{
	  warnings.add(msg);
	}
	else if(msgLevel == Project.MSG_INFO)
	{
	  infos.add(msg);
	}
	else if(msgLevel == Project.MSG_VERBOSE)
	{
	  verboses.add(msg);
	}
  }

  /* (non-Javadoc)
   * @see org.apache.tools.ant.Task#log(java.lang.String)
   */
  public void log(String msg) 
  {
	log(msg, Project.MSG_INFO);
  }
  
  /**
   * Get the errors that were logged.
   * 
   * @return
   * 		The errors that were logged.
   */
  public List getErrors()
  {
	return errors;
  }
  
  /**
   * Get the warnings that were logged.
   * 
   * @return
   * 		The warnings that were logged.
   */
  public List getWarnings()
  {
	return warnings;
  }
  
  /**
   * Get the infos that were logged.
   * 
   * @return
   * 		The infos that were logged.
   */
  public List getInfos()
  {
	return infos;
  }
  
  /**
   * Get the verboses that were logged.
   * 
   * @return
   * 		The verboses that were logged.
   */
  public List getVerboses()
  {
	return verboses;
  }
}
