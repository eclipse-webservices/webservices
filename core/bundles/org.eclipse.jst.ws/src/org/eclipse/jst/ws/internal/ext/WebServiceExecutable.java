/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ext;

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


