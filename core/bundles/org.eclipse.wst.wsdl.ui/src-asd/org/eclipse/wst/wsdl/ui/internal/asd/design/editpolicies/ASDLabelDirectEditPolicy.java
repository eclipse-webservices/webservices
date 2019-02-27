/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.EndPointEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.INamedObject;

public class ASDLabelDirectEditPolicy extends DirectEditPolicy {
	protected Command getDirectEditCommand(DirectEditRequest request) {
		Command command = null;
		String labelText = (String) request.getCellEditor().getValue();
		EditPart editPart= getHost();

		// TODO: rmah:  Start VERY UGLY HACK....  I don't see any other way to solve this issue at this time.
		// EndPointEditPart contains two labels which can be direct edited.  The facade driving EndPointEditPart
		// contains a getSetNameCommand() and a getSetAddressCommand()... however, this class (LabelDirectEditPolicy)
		// only knows about getSetNameCommand().....  This is the problem we face when combining two labels into
		// one EditPart.....
		if (editPart instanceof EndPointEditPart) {
			command = ((EndPointEditPart) editPart).getSetProperLabelCommand(labelText);
		}
		// End VERY UGLY HACK
		else if (editPart.getModel() instanceof INamedObject) {
			command = ((INamedObject) editPart.getModel()).getSetNameCommand(labelText);
		}

		return command;
	}

	protected void showCurrentEditValue(DirectEditRequest request) {
	}
}