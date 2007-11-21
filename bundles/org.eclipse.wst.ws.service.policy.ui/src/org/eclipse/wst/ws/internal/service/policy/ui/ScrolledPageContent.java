/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071120   209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;


public class ScrolledPageContent extends SharedScrolledComposite {

	private FormToolkit fToolkit;
	
	public ScrolledPageContent(Composite parent) {
		this(parent, SWT.V_SCROLL | SWT.H_SCROLL);
	}
	
	public ScrolledPageContent(Composite parent, int style) {
		super(parent, style);
		
		setFont(parent.getFont());
		
		fToolkit= getDialogsFormToolkit();
		
		setExpandHorizontal(true);
		setExpandVertical(true);
		
		Composite body= new Composite(this, SWT.NONE);
		body.setFont(parent.getFont());
		setContent(body);
	}
	
	public FormToolkit getDialogsFormToolkit() {
		if (fToolkit == null) {
			FormColors colors= new FormColors(Display.getCurrent());
			colors.setBackground(null);
			colors.setForeground(null);	
			fToolkit= new FormToolkit(colors);
		}
		return fToolkit;
	}

	public void adaptChild(Control childControl) {
		fToolkit.adapt(childControl, true, true);
	}
	
	public Composite getBody() {
		return (Composite) getContent();
	}

}
