<%
/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
%>
<%@ page contentType="text/html; charset=UTF-8" %>

<script language="javascript">
  <%// Requires browserdetect.js%>
  function createHiddenElement(elementName,value)
  {
    var hiddenInput;
    if (elementName.length > 0)
    {
      if (isMicrosoftInternetExplorer())
        hiddenInput = document.createElement("<input name='"+elementName+"'>");
      else
      {
        hiddenInput = document.createElement("input");
        hiddenInput.setAttribute("name",elementName);
      }
    }
    else
      hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.value = value;
    return hiddenInput;
  }

  function resetFormInputs()
  {
    document.location.reload();
  }
</script>
