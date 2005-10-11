/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.provisional.env.core;

import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * This interface is used to create Command objects.
 *
 */
public interface CommandFactory 
{
  /**
   * 
   * @return returns a created Command object.
   */
  public AbstractDataModelOperation create();
}
