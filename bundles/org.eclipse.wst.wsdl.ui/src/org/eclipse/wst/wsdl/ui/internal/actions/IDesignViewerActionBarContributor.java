/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.actions;

import org.eclipse.ui.IActionBars;
import org.eclipse.wst.sse.ui.ISourceViewerActionBarContributor;

public interface IDesignViewerActionBarContributor extends ISourceViewerActionBarContributor {
	public void initViewerSpecificContributions(IActionBars bars);
}