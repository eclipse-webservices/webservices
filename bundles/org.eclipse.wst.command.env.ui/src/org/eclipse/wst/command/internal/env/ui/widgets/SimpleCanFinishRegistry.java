/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import java.util.Vector;

import org.eclipse.wst.command.internal.env.core.common.Condition;



public class SimpleCanFinishRegistry extends Vector implements CanFinishRegistry
{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257285837870741305L;

  /* (non-Javadoc)
   * @see org.eclipse.env.widgets.CanFinishRegistry#addCondition(org.eclipse.env.command.fragment.Condition)
   */
  public void addCondition(Condition condition)
  {
    add( condition );
  }
}
