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
package org.eclipse.wst.command.internal.provisional.env.core.data;

/**
 * This interface is used to transform an object from one class to another.
 *
 */
public interface Transformer
{
  /*
   * @return returns a transformed object based on the input value.
   */
  public Object transform( Object value );
}
