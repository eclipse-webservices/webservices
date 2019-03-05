//
// Copyright (c) 2002, 2019 IBM Corporation and others.
// This program and the accompanying materials
// are made available under the terms of the Eclipse Public License 2.0
// which accompanies this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/
//
// SPDX-License-Identifier: EPL-2.0
//
// Contributors:
//     IBM Corporation - initial API and implementation
//
function resumeProxyLoadPage()
{
  var proxyPage = parent.document.getElementById("proxyPage");
  proxyPage.setAttribute("rows", "0,100%");
  parent.frames[0].handleCompletion();
}
