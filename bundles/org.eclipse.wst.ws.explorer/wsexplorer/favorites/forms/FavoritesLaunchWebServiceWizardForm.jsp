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
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="formActionLink" class="java.lang.StringBuffer" scope="request">
<%
   formActionLink.append("favorites/actions/FavoritesLaunchWebServiceWizardActionJSP.jsp");
%>
</jsp:useBean>
<jsp:include page="/forms/LaunchWebServiceWizardForm.jsp" flush="true"/>
