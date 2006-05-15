<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060515   128602 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
  org.eclipse.wst.ws.internal.explorer.WSExplorerContext,
  java.util.Enumeration,
  java.util.Hashtable,
  org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils" %>

<%
  request.setCharacterEncoding("UTF-8"); 
  String key = null;
  Hashtable options = new Hashtable();
  Enumeration paramNames = request.getParameterNames();
  while (paramNames.hasMoreElements())
  {
    String paramName = (String)paramNames.nextElement();
    String[] paramValues = request.getParameterValues(paramName);
    if (paramValues != null && paramValues.length > 0)
    {
      if (key == null && URLUtils.decode(paramName).equals(WSExplorerContext.ID))
        key = paramValues[0];
      else
        options.put(paramName, paramValues);
    }
  }
  if (key != null)
  {
    LaunchOptionsManager manager = LaunchOptionsManager.getInstance();
    manager.manage(key, options, application);
  }
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
</html>
