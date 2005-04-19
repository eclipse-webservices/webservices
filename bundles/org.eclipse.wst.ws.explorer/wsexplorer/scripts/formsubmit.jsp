<%
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
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<script language="javascript">
  var requestSubmitted = false;

  function handleSubmit(form)
  {
    if (requestSubmitted)
    {
      alert("<%=HTMLUtils.JSMangle(controller.getMessage("MSG_SUBMIT_IN_PROGRESS"))%>");
      return false;
    }
    requestSubmitted = true;
    return true;
  }

  function resetSubmission()
  {
    requestSubmitted = false;
  }
</script>
