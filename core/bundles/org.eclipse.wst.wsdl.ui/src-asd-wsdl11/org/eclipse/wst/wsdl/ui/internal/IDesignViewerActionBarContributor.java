/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal;

import org.eclipse.ui.IActionBars;
import org.eclipse.wst.sse.ui.internal.ISourceViewerActionBarContributor;

public interface IDesignViewerActionBarContributor extends ISourceViewerActionBarContributor {
	public void initViewerSpecificContributions(IActionBars bars);
}