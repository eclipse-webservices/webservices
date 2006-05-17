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
package org.eclipse.wst.wsdl.ui.internal.soap.customizations;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.ui.internal.util.NodeAssociationManager;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.custom.DialogNodeEditorConfiguration;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.custom.NodeEditorConfiguration;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.custom.NodeEditorProvider;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class is provided as an example of how a NodeEditorProvider can utilize the WSDL model if required
 * (and is not limited to the DOM model).  It's hope that potential adopters can provide feedback and open
 * bugs where they find support is lacking or inconvenient. 
 */
public class SOAPNodeEditorProvider extends NodeEditorProvider
{
  public NodeEditorConfiguration getNodeEditorConfiguration(String parentName, String nodeName)
  {
    // TODO (cs) this extension is only designed to work when used via the WSDL Editor context.
    // Reuse from other context is possible too if the editor provides the appropriate 'getAdapter' behaviour.
    // We need to revisit this and get more feedback from adopters to see if different approach is required 
    // in order to obtain a WSDL model.
//    /
    IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    final Definition definition = (Definition)editor.getAdapter(Definition.class);
    if (definition != null)
    {  
      if (parentName.equals("body") && nodeName.equals("parts"))
      {
        // gee... this case sounds sorta morbid eh?
        //
        return new DialogNodeEditorConfiguration()
        {                
          public void invokeDialog()
          {                   
            Node parentNode = getParentNode();
            if (parentNode instanceof Element)
            {           
              Element element = (Element)getParentNode();
              
              // TODO (cs) I think we should push this function down to the WSDL model (ala XSD model)
              //
              NodeAssociationManager nodeAssociationManager = new NodeAssociationManager();
              Object o = nodeAssociationManager.getModelObjectForNode(definition, element);
              if (o instanceof SOAPBody)
              {  
                SOAPSelectPartsDialog dialog = new SOAPSelectPartsDialog(Display.getCurrent().getActiveShell(), definition, (SOAPBody)o);
                dialog.create();
                dialog.getShell().setText("Specify Parts");          
                int rc = dialog.open();
                if (rc == Dialog.OK)
                {                  
                  String value = "";
                  Part[] parts = dialog.getSelectedParts();
                  for (int i = 0; i < parts.length; i++)
                  {  
                    Part part = parts[i];
                    value += part.getName();
                    value += " ";
                  }  
                  
                  // TODO (cs) we need to provide 'command' support so that we can handle undo properly
                  //                   
                  element.setAttribute("parts", value);                  
                }  
              }               
            } 
          }        
          public String getButonText()
          {
            return "...";
          }
        };
      }      
    }
    return null;     
  }
}

