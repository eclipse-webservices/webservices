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
package org.eclipse.wst.wsdl.ui.internal.viewers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.extension.IDetailsViewerProvider;

public class WSDLDetailsViewerProvider implements IDetailsViewerProvider
{
  public Object getViewerKey(Object modelObject)
  {                              
    Object result = null;   
                        
    // todo.... use a switch
    if (modelObject instanceof Message)
    {
      result = "message";
    }
    else if (modelObject instanceof Binding)
    {
      result = "binding";
    }
    else if (modelObject instanceof BindingOperation)
    {
      result = "bindingOperation";
    }
    else if (modelObject instanceof Operation)
    {
      result = "operation";
    }
    else if (modelObject instanceof Definition)
    {
      result = "definition";
    }
    else if (modelObject instanceof Part)
    {
      result = "part";
    }
    else if (modelObject instanceof Port)
    {
      result = "port";
    }
    else if (modelObject instanceof Input || 
             modelObject instanceof Output ||
             modelObject instanceof Fault)
    {
      result = "inputOutputFault";
    }    
    else if (modelObject instanceof BindingInput || 
             modelObject instanceof BindingOutput || 
             modelObject instanceof BindingFault) 
    {
      result = "bindingInputOutputFault";
    }
    else if (modelObject instanceof PortType)
    {
      result = "portType";
    }
    else if (modelObject instanceof Service)
    {
      result = "service";
    }
    else if (modelObject instanceof ExtensibilityElement)
    {
      result = "extensibilityElement";
    }
    else if (modelObject instanceof Import)
    {
      result = "import"; 
    }
    else
    {
      result = "empty";
    }
    return result;
  }                                                                


  public Viewer createViewer(Object modelObject, Composite parent, IEditorPart editorPart)
  {                                       
    Viewer viewer = null;
    Object key = getViewerKey(modelObject);
    if (key != null)     
    {
      if (key.equals("message"))
      {
        viewer = new MessageViewer(parent, editorPart); 
      } 
      else if (key.equals("binding"))
      {
        viewer = new BindingViewer(parent, editorPart);
      }
      else if (key.equals("definition"))
      {
        viewer = new DefinitionViewer(parent, editorPart); 
      }
      else if (key.equals("bindingOperation"))
      {
        viewer = new BindingOperationViewer(parent, editorPart);
      }
      else if (key.equals("operation"))
      {
        viewer = new OperationViewer(parent, editorPart);
      }
      else if (key.equals("part"))
      {
        viewer = new PartViewer(parent, editorPart);
      }
      else if (key.equals("port"))
      {
        viewer = new PortViewer(parent, editorPart);
      }
      else if (key.equals("inputOutputFault"))
      {
        viewer = new InputOutputFaultViewer(parent, editorPart); 
      }    
      else if (key.equals("bindingInputOutputFault"))
      {
        viewer = new BindingInputOutputFaultViewer(parent, editorPart); 
      }        
      else if (key.equals("service"))
      {
        viewer = new ServiceViewer(parent, editorPart); 
      }
      else if (key.equals("extensibilityElement"))
      {
        viewer = new ExtensibilityElementViewer(parent, editorPart); 
      }  
      else if (key.equals("portType"))
      {
        viewer = new PortTypeViewer(parent, editorPart); 
      }
      else if (key.equals("import"))
      {
        viewer = new ImportViewer(parent, editorPart);
      }
      else
      {
        viewer = new EmptyViewer(parent, 0); 
      }
    }   
    return viewer;
  }
}