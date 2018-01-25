/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsi.tests.internal;

import java.io.File;
import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.wst.wsi.internal.core.analyzer.BasicProfileAnalyzer;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultsOption;
import org.eclipse.wst.wsi.internal.core.analyzer.config.impl.AssertionResultsOptionImpl;
import org.eclipse.wst.wsi.internal.core.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.core.document.DocumentFactory;

public class BasicProfileAnalyzerTest extends TestCase {
	public void testReportWriterIsClosedAfterReportIsFinished() throws Exception {
		DocumentFactory documentFactory = DocumentFactory.newInstance();
		AnalyzerConfig analyzerconfig = documentFactory.newAnalyzerConfig();
		AssertionResultsOption aro = new AssertionResultsOptionImpl();
		aro.setShowMessageEntry(false);
		analyzerconfig.setAssertionResultsOption(aro);
		analyzerconfig.setAddStyleSheet(new AddStyleSheetImpl());
		File reportFile = File.createTempFile("wsi-report", ".xml");
		analyzerconfig.setReportLocation(reportFile.getAbsolutePath());
		BasicProfileAnalyzer analyzer = new BasicProfileAnalyzer(Collections.singletonList(analyzerconfig));
		
		analyzer.validateConformance();
		
		boolean deleted = reportFile.delete();
		assertTrue(deleted);
	}

}
