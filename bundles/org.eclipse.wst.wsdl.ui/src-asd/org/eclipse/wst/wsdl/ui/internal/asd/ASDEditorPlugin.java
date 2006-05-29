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
package org.eclipse.wst.wsdl.ui.internal.asd;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class ASDEditorPlugin extends AbstractUIPlugin {
	public static int DEPENDECIES_CHANGED_POLICY_RELOAD = 2;
	
	protected static ASDEditorPlugin instance;
	  
	  public ASDEditorPlugin() {		  
	  }
	  
//	/**
//	 * Get the singleton instance.
//	 */
//	public static ASDEditorPlugin getInstance()
//	{
//		if (instance == null) {
//			instance = new ASDEditorPlugin();
//		}
//		return instance;
//	}
//	
//	public Image getImage(String iconName)
//	{
//		ImageRegistry imageRegistry = getImageRegistry();
//		
//		if (imageRegistry.get(iconName) != null)
//		{
//			return imageRegistry.get(iconName);
//		}
//		else
//		{
//			imageRegistry.put(iconName, ImageDescriptor.createFromFile(getClass(), iconName));
//			return imageRegistry.get(iconName);
//		}
//	}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		// We should not know about WSDLEditorPlugin.java.  Eventually, we should move these
		// 'generic', 'top-level' icons to the ASD level.....
		return WSDLEditorPlugin.getImageDescriptor(path);
	}
	
  public static ImageDescriptor getImageDescriptorFromPlugin(String path) {
    // We should not know about WSDLEditorPlugin.java.  Eventually, we should move these
    // 'generic', 'top-level' icons to the ASD level.....
    return WSDLEditorPlugin.getImageDescriptorFromPlugin(path);
  }

	public static IEditorPart getActiveEditor() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}
}
