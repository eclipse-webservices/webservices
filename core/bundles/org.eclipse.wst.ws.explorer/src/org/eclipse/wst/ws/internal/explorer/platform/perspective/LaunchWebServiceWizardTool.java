/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

public abstract class LaunchWebServiceWizardTool extends FormTool {
    public LaunchWebServiceWizardTool(ToolManager toolManager, String alt) {
        super(toolManager, "images/launch_wswizard_enabled.gif", "images/launch_wswizard_highlighted.gif", alt);
    }

    public String getFormLink() {
        return "forms/LaunchWebServiceWizardForm.jsp";
    }

}
