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
package org.eclipse.wst.wsdl.ui.internal.search.actions;
import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.core.search.scope.WorkspaceSearchScope;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Interface;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Message;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;
import org.eclipse.wst.xsd.ui.internal.editor.ISelectionMapper;
import org.eclipse.wst.xsd.ui.internal.search.XSDSearchQuery;
import org.eclipse.wst.xsd.ui.internal.search.actions.FindAction;

public class WSDLFindReferencesAction extends FindAction
{
  public WSDLFindReferencesAction(IEditorPart editor)
  {
    super(editor);
  }

  public void setActionDefinitionId(String string)
  {
  }

  /**
   * To be used by subclass in its run() Returns the file where the selection of
   * a component (from the user) occurs ie. Returns the file that the user is
   * currently working on.
   * 
   * @return The IFile representation of the current working file.
   */
  protected IFile getCurrentFile()
  {
    if (editor != null)
    {
      IEditorInput input = editor.getEditorInput();
      if (input instanceof IFileEditorInput)
      {
        IFileEditorInput fileEditorInput = (IFileEditorInput) input;
        return fileEditorInput.getFile();
      }
    }
    return null;
  }

  /**
   * To be used by subclass in its run().. Determines the metaName of the WSDL
   * component given to this method.
   * 
   * @param component
   *          The component of which we want to determine the name
   * @return
   * 		The first one is for MetaName/Type, second for ElementQualifiedName
   */
  protected QualifiedName[] determineMetaAndQualifiedName(WSDLBaseAdapter component)
  {
    QualifiedName names[] = null;
    QName qName;
	if (component instanceof W11Message )
    {
		names = new QualifiedName[2];
    	names[0] = IWSDLSearchConstants.MESSAGE_META_NAME;
    	qName = ((Message) component.getTarget()).getQName();
    	names[1] = new QualifiedName(qName.getNamespaceURI(), qName.getLocalPart());
    }
    else if (component instanceof W11Interface)
    {
    	names = new QualifiedName[2];
    	names[0] = IWSDLSearchConstants.PORT_TYPE_META_NAME;
    	qName = ((PortType) component.getTarget()).getQName();
    	names[1] = new QualifiedName(qName.getNamespaceURI(), qName.getLocalPart());
    }
    else if (component instanceof W11Binding)
    {
    	names = new QualifiedName[2];
    	names[0] = IWSDLSearchConstants.BINDING_META_NAME;
    	qName = ((Binding) component.getTarget()).getQName();
    	names[1] = new QualifiedName(qName.getNamespaceURI(), qName.getLocalPart());

    }
    return names;
  }

  protected WSDLBaseAdapter getWSDLNamedComponent()
  {
    if (editor != null)
    {
      ISelectionProvider provider = (ISelectionProvider) editor.getAdapter(ISelectionProvider.class);
      ISelectionMapper mapper = (ISelectionMapper) editor.getAdapter(ISelectionMapper.class);
      if (provider != null)
      {
        ISelection selection = provider.getSelection();
        if (mapper != null)
        {
          selection = mapper.mapSelection(selection);
        }
        if (selection != null && selection instanceof IStructuredSelection)
        {
          IStructuredSelection s = (IStructuredSelection) selection;
          Object o = s.getFirstElement();
          if (o != null && o instanceof WSDLBaseAdapter)
          {
            return (WSDLBaseAdapter) o;
          }
        }
      }
    }
    // The expected component we get from the editor does not meet
    // our expectation
    return null;
  }  

  public void run()
  {
    String pattern = "";
    WSDLBaseAdapter component = getWSDLNamedComponent();
    IFile file = getCurrentFile();
    QualifiedName[] names = determineMetaAndQualifiedName(component);
    if (file != null && component != null && names != null)
    {
      SearchScope scope = new WorkspaceSearchScope();
      String scopeDescription = "Workspace";
      XSDSearchQuery searchQuery = new XSDSearchQuery(pattern, file, names[1], 
    		  names[0], XSDSearchQuery.LIMIT_TO_REFERENCES, scope, scopeDescription);
      NewSearchUI.activateSearchResultView();
      NewSearchUI.runQueryInBackground(searchQuery);
    }
  }
}
