/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.asd.facade;

import org.eclipse.gef.commands.Command;

public interface IImport extends INamedObject {
	public String getNamespace();
	public String getNamespacePrefix();
	public String getLocation();
	
	public IDescription getOwnerDescription();

	public Command getUpdateCommand(String location, String namespaceURI, String prefix);
}
