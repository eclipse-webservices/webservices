/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/*
 * A ModelDiagnosticInfo object contains "additional" information about a model object.
 * It is passed from a model (facade) object to an EditPart.  The EditPart uses the ModelDiagnosticInfo
 * object to display any additional information necessary (for example, an error message when the model
 * object is in some way invalid.
 */
public class ModelDiagnosticInfo {
	public static int INFORMATIONAL_TYPE = 0;
	public static int ERROR_TYPE = 1;
	public static int WARNING_TYPE = 2;
	
	private int type = 1;
	private String text = "";
	private Color textColor;
	
	public ModelDiagnosticInfo(String txt, int infoType, Color color) {
		text = txt;
		type = infoType;
		textColor = color;
	}
	
	public void setDescriptionText(String txt) {
		text = txt;
	}
	
	public void setDescriptionTextColor(Color color) {
		textColor = color;
	}
	
	public void setType(int infoType) {
		type = infoType;
	}
	
	public String getDescriptionText() {
		return text;
	}
	
	public int getType() {
		return type;
	}
	
	public Color getDescriptionTextColor() {
		if (textColor != null) {
			return textColor;
		}
		
		if (type == ERROR_TYPE) {
			return ColorConstants.red;
		}
		else if (type == WARNING_TYPE) {
			return ColorConstants.lightGray;
		}
		
		return ColorConstants.black;
	}
}
