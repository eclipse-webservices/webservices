/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.internal.util;


import org.w3c.dom.Element;


// TODO... why do we need this?
// 
public interface Reconcilable
{
  public void setElement(Element element);

  public Element getElement();

  public void reconcileAttributes(Element changedElement);

  public void reconcileReferences(boolean deep);
}
