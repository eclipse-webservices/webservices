/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.wst.command.internal.env.core.fragment;

/**
  * This interface can be used when a conditional object needs to be returned.
**/
public interface LoopCondition
{
  /**
    * Evaluates a loop condition.
    *
    * @param loop the loop fragment that is being evaluated.
    * @param fragment the child fragment of the loop 
    *        under evaluation.  Note: fragment can be null.
    * @return returns an object based on some evaluated condition.
  **/
  public boolean evaluate( LoopFragment loop, CommandFragment fragment );
}
