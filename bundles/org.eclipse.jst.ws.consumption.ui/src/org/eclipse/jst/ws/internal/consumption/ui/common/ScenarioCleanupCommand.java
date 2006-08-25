/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060825   155114 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.common;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.common.WSDLParserFactory;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionRegistry;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 *  This command is intended to clean up data after either the
 *  client or service wizards.  Currently is just resets the
 *  parser factory. 
 *
 */
public class ScenarioCleanupCommand extends AbstractDataModelOperation
{

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  WSDLParserFactory.killParser();
    ObjectSelectionRegistry.getInstance().cleanup();
	
	  return Status.OK_STATUS;
  }

  
  public IStatus undo( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  // Even if the user cancels out of the wizard we want to
	  // kill the parser.
	  WSDLParserFactory.killParser();
    ObjectSelectionRegistry.getInstance().cleanup();
    
    return Status.OK_STATUS;
  }
}
