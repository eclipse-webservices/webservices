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
package org.eclipse.wst.wsdl.ui.internal;

import java.util.Map;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;

public class StructuredTextViewerConfigurationWSDL extends StructuredTextViewerConfigurationXML {
	protected Map getHyperlinkDetectorTargets(ISourceViewer sourceViewer) {
		Map targets = super.getHyperlinkDetectorTargets(sourceViewer);
		targets.put("org.eclipse.wst.wsdl.wsdlsource", null);  //$NON-NLS-1$
		return targets;
	}
}
