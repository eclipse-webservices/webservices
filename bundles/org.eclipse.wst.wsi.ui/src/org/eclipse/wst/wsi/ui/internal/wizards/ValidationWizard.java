/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal.wizards;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.wst.wsi.ui.internal.Resource;
import org.eclipse.wst.wsi.ui.internal.SoapMonitorPlugin;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * This wizard allows the user to specify the location of the 
 * WS-I Message Log file for purposes of validation.
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */

public class ValidationWizard extends Wizard
{
  /**
   * Wizard page to specify the location of the 
   * WS-I Message Log file for purposes of validation.
   */
  protected ValidationWizardLogPage logPage;

  /**
   * Wizard page to optionally specify the location
   * of the WSDL document for the Web service.
   */
  protected ValidationWizardWSDLPage wsdlPage;

  /**
   * Wizard page to specify the name, type and namespace
   * for the WSDL element to analyze.
   */
  protected ValidationWizardWSDLContentPage wsdlContentPage;

  /**
   * Default WS-I Message filename passed in the Constructor.
   */
  protected String filename;

  /**
   * The selected WS-I Message Log file.
   */
  protected IFile file;

  /**
   * The container holding the selected WS-I Message Log file.
   */
  protected IPath containerFullPath;

  /**
   * Flag indicating whether selected file is valid.
   */
  protected boolean isValid;

  /**
   * The name of the element to validate.
   */
  protected String elementname;

  /**
   * The namespace of the element to validate.
   */
  protected String namespace;

  /**
   * The name of the parent of the element to validate.
   */
  protected String parentname;

  /**
   * The type of the element to validate.
   */
  protected String type;

  /**
   * The WSDL file to use for validation;
   */
  protected String wsdlfile;

  /**
   * True if a WSDL file is included, false otherwise.
   */
  protected boolean includewsdlfile;

  
  /**
   * This list of WSDL locations the user can choose from.
   */
  protected String[] wsdllocations = null;
  
  /**
   * Constructor.
   * @param filename Default message log file name.
   */
  public ValidationWizard(String filename)
  {
    this.filename = filename;
    this.setWindowTitle(SoapMonitorPlugin.getResourceString("_UI_WIZARD_VALIDATE_LOG_TITLE"));
    setDefaultPageImageDescriptor(
      ImageDescriptor.createFromFile(SoapMonitorPlugin.class, Resource.VALIDATE_WSI_LOGFILE_WIZ));
    setNeedsProgressMonitor(true);
  }

  /**
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages()
  {
    logPage = new ValidationWizardLogPage((IStructuredSelection) getResourceNavigatorSelection(), filename);
    wsdlPage = new ValidationWizardWSDLPage((IStructuredSelection) getResourceNavigatorSelection());
    wsdlContentPage = new ValidationWizardWSDLContentPage((IStructuredSelection) getResourceNavigatorSelection());
    addPage(logPage);
    addPage(wsdlPage);
    addPage(wsdlContentPage);

  }

  /**
   * @return True if a WSDL file is included, false otherwise.
   */
  public boolean includeWSDLFile()
  {
    if (!isValid)
    {
      return wsdlPage.includeWSDLFile();
    }
    return includewsdlfile;
  }

  /**
   * Get the element name to validate.
   * 
   * @return The element name to validate.
   */
  public String getElementName()
  {
    return elementname;
  }

  /**
   * Get the namespace for the element.
   * 
   * @return The namespace for the element.
   */
  public String getNamespace()
  {
    return namespace;
  }

  /**
   * Get the name of the parent of the element.
   * 
   * @return The name of the parent of the element.
   */
  public String getParentName()
  {
    return parentname;
  }

  /**
   * Get the type of the element.
   * 
   * @return The type of the element.
   */
  public String getType()
  {
    return type;
  }

  public String getWSDLFile()
  {
    if (!isValid)
    {
      return wsdlPage.getWSDLFile();
    }
    return wsdlfile;
  }

  /**
   * @see org.eclipse.jface.wizard.IWizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
   */
  public IWizardPage getNextPage(IWizardPage currentPage)
  {
    if (currentPage == wsdlPage)
    {
      if (!wsdlPage.includeWSDLFile())
      {
        return null;
      }
      else
      {
        try
        {
          WSDLFactory factory = WSDLFactory.newInstance();
          WSDLReader reader = factory.newWSDLReader();
          Definition defElem = reader.readWSDL(getWSDLFile());
        }
        catch (Exception e)
        {
          // If there is a problem with the WSDL file there is no next page.
          return null;
        }
      }
    }
    return super.getNextPage(currentPage);
  }

  /**
   * Returns true if the selected file is a valid WS-I Message Log filename.
   * 
   * @return True if the selected file is a valid WS-I Message Log filename.
   */
  public boolean isValid()
  {
    return isValid;
  }

  /**
   * @see org.eclipse.jface.wizard.IWizard#performFinish()
   */
  public boolean performFinish()
  {
    file = logPage.getFile();
    containerFullPath = logPage.getContainerFullPath();
    includewsdlfile = wsdlPage.includeWSDLFile();
    wsdlfile = wsdlPage.getWSDLFile();
    elementname = wsdlContentPage.getElementName();
    namespace = wsdlContentPage.getNamespace();
    parentname = wsdlContentPage.getParentName();
    type = wsdlContentPage.getType();
    isValid = true;
    return true;
  }

  /**
   * Returns the file selected by the user in the choose page.
   * 
   * @return the file selectedby the user in the choose page.
   */
  public IFile getFile()
  {
    return file;
  }

  /**
   * Returns the IPath containing the selected IFile.
   * 
   * @return the IPath containing the selected IFile.
   */
  public IPath getContainerFullPath()
  {
    return containerFullPath;
  }

  /**
   * Returns the selection in the Resource Navigator view.
   * 
   * @return the selection in the Resource Navigator view.
   */
  public ISelection getResourceNavigatorSelection()
  {
    IViewReference viewParts[] =
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();

    for (int i = 0; i < viewParts.length; i++)
    {
      if (viewParts[i] instanceof ResourceNavigator)
      {
        return ((ResourceNavigator) viewParts[i]).getViewSite().getSelectionProvider().getSelection();
      }
    }
    return StructuredSelection.EMPTY;
  }

  /**
   * Set the list of WSDL locations to let the user choose from.
   * 
   * @param wsdllocations A list of WSDL document locations.
   */
  public void setWSDLLocations(String[] wsdllocations) 
  {
	this.wsdllocations = wsdllocations;
	
  }
  
  /**
   * Get the list of WSDL locations the user can choose from.
   * 
   * @return The list of WSDL locations.
   */
  public String[] getWSDLLocations()
  {
  	return wsdllocations;
  }
}
