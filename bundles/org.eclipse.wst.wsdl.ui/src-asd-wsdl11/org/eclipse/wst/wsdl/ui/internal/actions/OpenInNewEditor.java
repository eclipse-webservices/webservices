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
package org.eclipse.wst.wsdl.ui.internal.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;

public class OpenInNewEditor extends BaseSelectionAction
{
  public static final String ID = "org.eclipse.wst.wsdl.ui.OpenInNewEditor"; //$NON-NLS-1$
  
  public OpenInNewEditor(IWorkbenchPart part)
  {
    super(part);
    setText(Messages.getString("_UI_ACTION_OPEN_IN_NEW_EDITOR")); //$NON-NLS-1$
    setId(ID);
    setImageDescriptor(ASDEditorPlugin.getImageDescriptorFromPlugin("wsdl_file_obj.gif")); //$NON-NLS-1$
  }
  
  protected boolean calculateEnabled()
  {
    return true;
  }

  public void run()
  {
    if (getSelectedObjects().size() > 0)
    {
      Object o = getSelectedObjects().get(0);
      // should make this generic and be able to get the owner from a facade object
      if (o instanceof WSDLBaseAdapter)
      {
        WSDLBaseAdapter baseAdapter = (WSDLBaseAdapter)o;

        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IEditorPart editorPart = workbenchWindow.getActivePage().getActiveEditor();
        Object object = editorPart.getAdapter(Definition.class);
        if (object instanceof Definition)
        {
          EObject eObject = (EObject)baseAdapter.getTarget();
          OpenOnSelectionHelper openHelper = new OpenOnSelectionHelper((Definition)object);
          openHelper.openEditor(eObject);
        }
      }
    }
  }
}
