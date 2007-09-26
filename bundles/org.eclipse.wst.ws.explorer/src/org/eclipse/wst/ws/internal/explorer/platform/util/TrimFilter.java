/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class TrimFilter implements Filter
{

  /* (non-Javadoc)
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig arg0) throws ServletException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc) throws IOException, ServletException
  {
    // TODO Auto-generated method stub
    CharArrayWrapper responseWrapper = new CharArrayWrapper((HttpServletResponse)resp);
    // Hack for Tomcat performance problem with filters.
    OutputStreamWriter osw = new OutputStreamWriter(resp.getOutputStream(),HTMLUtils.UTF8_ENCODING);
    fc.doFilter(req,responseWrapper);
    String responseString = responseWrapper.toString();
    osw.write(responseString.trim());
    osw.close();
  }

  /* (non-Javadoc)
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // TODO Auto-generated method stub

  }

}
