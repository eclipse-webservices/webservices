/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on May 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.ws.internal.consumption.ui.common;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.FinishTestFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;


/**
 * @author gilberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FinishFragment extends SequenceFragment
{
  public FinishFragment()
  {	
	add(new FinishTestFragment() );
  }
}
