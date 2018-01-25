/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

import org.eclipse.emf.common.notify.Notification;

/**
 * This interface should be implemented by classes that intend to do serialization for 
 * DOM objects.
 * 
 * @author Georgi Vachkov
 */
public interface IAnnotationSerializer 
{
	public void save(final Notification msg);
}
