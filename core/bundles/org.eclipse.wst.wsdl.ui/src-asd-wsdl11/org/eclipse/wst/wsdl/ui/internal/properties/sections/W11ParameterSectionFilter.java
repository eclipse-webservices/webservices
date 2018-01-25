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
package org.eclipse.wst.wsdl.ui.internal.properties.sections;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.xsd.ui.internal.adt.editor.EditorModeManager;

public class W11ParameterSectionFilter implements IFilter
{
  public boolean select(Object toTest)
  {
    boolean result = false;
    if (toTest instanceof IParameter)
    {  
      result = true;
      try
      {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        EditorModeManager manager = (EditorModeManager) editor.getAdapter(EditorModeManager.class);
        if (manager != null)
        {
          EditorModeSectionFilter filter = (EditorModeSectionFilter) manager.getCurrentMode().getAdapter(EditorModeSectionFilter.class);
          if (filter != null)
          {
            result = filter.isApplicable(W11ParameterSection.class, toTest);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return result;
  }
}