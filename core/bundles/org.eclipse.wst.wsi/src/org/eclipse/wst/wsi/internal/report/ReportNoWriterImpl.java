/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
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
package org.eclipse.wst.wsi.internal.report;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.report.Report;
import org.w3c.dom.Document;

/**
 * WS-I conformance test ReportWriter for wsi plugin.
 * This ReportWriter writes nothing so no report file will be created.
 */
public class ReportNoWriterImpl implements org.eclipse.wst.wsi.internal.core.report.ReportWriter
{

  /**
   * @see java.lang.Object#Object()
   */
  public ReportNoWriterImpl()
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#getDocument()
   */
  public Document getDocument() throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#setWriter(String)
   */
  public void setWriter(String documentLocation) throws IOException
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#write(Reader)
   */
  public void write(Reader reader) throws WSIException, IllegalStateException
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#write(Reader, Writer)
   */
  public void write(Reader reader, Writer writer) throws WSIException
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportWriter#write(Report)
   */
  public void write(Report report) throws IllegalStateException
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportWriter#write(Report, Writer)
   */
  public void write(Report report, Writer writer)
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#close()
   */
  public void close() throws WSIException, IllegalStateException
  {
  }
}
