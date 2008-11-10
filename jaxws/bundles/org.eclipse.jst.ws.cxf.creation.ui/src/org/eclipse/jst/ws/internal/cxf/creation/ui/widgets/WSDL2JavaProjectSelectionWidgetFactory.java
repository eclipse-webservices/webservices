/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidget;
import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.cxf.creation.core.commands.WSDL2JavaProjectSelectionCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;

/**
 * This is stop gap workaround for bugs <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=243286">#243286</a>
 * and <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=243290">#243290</a>
 * <p>
 * Used in conjunction with <code>WSDL2JavaProjectSelectionCommand</code>
 * 
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class WSDL2JavaProjectSelectionWidgetFactory implements INamedWidgetContributorFactory {

	public INamedWidgetContributor getFirstNamedWidget() {
		return null;
	}

	public INamedWidgetContributor getNextNamedWidget(INamedWidgetContributor widgetContributor) {
		return null;
	}

	public void registerDataMappings(DataMappingRegistry dataRegistry) {
		dataRegistry.addMapping(ServerWizardWidgetDefaultingCommand.class, "InitialProject",  //$NON-NLS-1$
				WSDL2JavaProjectSelectionCommand.class);
	    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject",  //$NON-NLS-1$
	    		WSDL2JavaProjectSelectionCommand.class, "ServerProject", new StringToIProjectTransformer()); //$NON-NLS-1$
	    dataRegistry.addMapping(ServerWizardWidget.class, "Project", WSDL2JavaProjectSelectionCommand.class); //$NON-NLS-1$
	}
}
