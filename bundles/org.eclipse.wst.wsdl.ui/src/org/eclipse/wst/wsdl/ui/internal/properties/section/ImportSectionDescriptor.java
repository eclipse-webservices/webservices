/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.properties.ISection;
import org.eclipse.wst.common.ui.properties.ISectionDescriptor;
import org.eclipse.wst.wsdl.Import;

public class ImportSectionDescriptor extends AbstractSectionDescriptor implements ISectionDescriptor
{
  ImportSection importSection;
  /**
   * 
   */
  public ImportSectionDescriptor()
  {
    super();
    importSection = new ImportSection();
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.ISectionDescriptor#getId()
   */
  public String getId()
  {
    return "org.eclipse.wst.wsdl.ui.internal.section.import";
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.ISectionDescriptor#getInputTypes()
   */
  public List getInputTypes()
  {
    List list = new ArrayList();
    list.add(Import.class);
    return list;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.ISectionDescriptor#getSectionClass()
   */
  public ISection getSectionClass()
  {
    return importSection;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.ISectionDescriptor#getTargetTab()
   */
  public String getTargetTab()
  {
    return "com.ibm.xmlwebservices.general";
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.ISectionDescriptor#appliesTo(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
   */
  public boolean appliesTo(IWorkbenchPart part, ISelection selection)
  {
    importSection.setEditorPart(part.getSite().getWorkbenchWindow().getActivePage().getActiveEditor());
    Object object = null;
    if (selection instanceof StructuredSelection)
    {
      StructuredSelection structuredSelection = (StructuredSelection)selection;
      object = structuredSelection.getFirstElement();
      if (object instanceof Import)
      {
        return true;
      }
    }
    return false;
  }
  
  public String getAfterSection()
  {
    return "org.eclipse.wst.wsdl.ui.internal.section.name";
  }

}
