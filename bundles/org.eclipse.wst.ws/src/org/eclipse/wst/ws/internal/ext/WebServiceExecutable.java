/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.ext;

import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
* All Extensions with exectutable extensions should implement this 
* to fit our framework
*/
public interface WebServiceExecutable
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  /**
  *Returns a wizard fragment for this exention type 
  *
  */
  public AbstractDataModelOperation getFinishCommand();
}
