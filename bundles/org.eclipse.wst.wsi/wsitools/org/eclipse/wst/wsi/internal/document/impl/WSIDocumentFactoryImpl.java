/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.document.impl;


import org.eclipse.wst.wsi.internal.report.Report;
import org.eclipse.wst.wsi.internal.report.ReportWriter;
import org.eclipse.wst.wsi.internal.report.impl.ReportImpl;
import org.eclipse.wst.wsi.internal.report.impl.ReportNoWriterImpl;

/**
 * DocumentFactoryImpl
 * 
 * Extends the WS-I Test Tools document factory to specify specific Report
 * and ReportWriter classes needed for inclusion of the tools in WSAD.
 */
public class WSIDocumentFactoryImpl extends org.eclipse.wst.wsi.internal.document.impl.DocumentFactoryImpl
{

  /**
   * Constructor for DocumentFactoryImpl.
   */
  public WSIDocumentFactoryImpl()
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
    return new ReportImpl();
  }

}
