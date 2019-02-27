/*******************************************************************************
 * Copyright (c) 2002-2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.document;


import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.ReportWriter;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.report.NullReportImpl;
import org.eclipse.wst.wsi.internal.report.ReportNoWriterImpl;
import org.eclipse.wst.wsi.internal.report.SimpleReporter;

/**
 * DocumentFactoryImpl
 * 
 * Extends the WS-I Test Tools document factory to specify specific Report
 * and ReportWriter classes needed for inclusion of the tools in WSAD.
 */
public class DocumentFactoryImpl extends org.eclipse.wst.wsi.internal.core.document.impl.DocumentFactoryImpl
{

  /**
   * Constructor for DocumentFactoryImpl.
   */
  public DocumentFactoryImpl()
  {
    super();
  }

  /**
   * Create a new instance of a DocumentWriter.
   */
  public ReportWriter newReportWriter()
  {
    // Return implementation
    return new ReportNoWriterImpl();
  }

  /**
   * Create report.
   * @return newly created report.
   */
  public Report newReport()
  {
    return new NullReportImpl();
  }

  /**
   * Create a reporter.
   * @return newly created reporter.
   */
  public Reporter newReporter(Report report, ReportWriter reportWriter)
  {
    return new SimpleReporter(report, reportWriter);
  }
}
