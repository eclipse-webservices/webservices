/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.listeners;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.ws.service.policy.IServicePolicy;

public interface IStatusChangeListener
{
  public void statusChange( IServicePolicy policy, IStatus oldStatus, IStatus newStatus );
}
