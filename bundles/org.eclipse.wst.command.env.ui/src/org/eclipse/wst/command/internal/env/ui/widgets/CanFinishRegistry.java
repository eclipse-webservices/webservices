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
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.wst.command.internal.env.core.common.Condition;

/**
 * This is an interface for an abstract can finish registry.
 * This registry is usually associate with an Eclipse wizard.  This
 * registry allow condition object to be added to it in order to
 * determine if the wizard can finish or not.
 *
 */
public interface CanFinishRegistry
{
  /**
   * Add a condition object to the registry.
   * @param condition the condition.
   */
  public void addCondition( Condition condition );
}
