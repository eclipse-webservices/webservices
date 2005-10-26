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
package org.eclipse.wst.command.internal.env.core.common;

import java.util.Vector;

/**
 * This Vector makes the protected method removeRange public.
 *
 */
public class RangeVector extends Vector
{
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4048793476659230773L;

  public void removeRange( int start, int end )
  {
    super.removeRange( start, end );
  }
}
