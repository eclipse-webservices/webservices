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

<jsp:useBean id="formActionLink" class="java.lang.StringBuffer" scope="request">
<%
   formActionLink.append("uddi/actions/UDDILaunchWebServiceWizardActionJSP.jsp");
%>
</jsp:useBean>
<jsp:include page="/forms/LaunchWebServiceWizardForm.jsp" flush="true"/>
