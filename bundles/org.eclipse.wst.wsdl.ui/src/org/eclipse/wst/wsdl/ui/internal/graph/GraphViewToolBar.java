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
package org.eclipse.wst.wsdl.ui.internal.graph;
                                                 
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.graph.model.WSDLGraphModelAdapterFactory;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;

 
public class GraphViewToolBar extends ViewForm
{
    ToolBar graphToolBar;  // the toolbar at the top of the graph view
    //ToolItem toolItem;   // the view tool item
    Composite frameBar;  // The composite that contains the toolbar
    WSDLEditor editor;                        

    ToolItem showBindingsButton;   // the view tool item   
    ToolItem backButton;

    public GraphViewToolBar(WSDLEditor wsdlEditor, Composite c, int style)
    {
      super(c, style);
      this.editor = wsdlEditor;
      frameBar = new Composite(this, SWT.NONE);
      /*
      CLabel label = new CLabel(frameBar, SWT.NONE);
      label.setText("View :");
      */
      org.eclipse.swt.layout.GridLayout frameLayout = new org.eclipse.swt.layout.GridLayout();
      frameLayout.numColumns = 3;
      frameLayout.horizontalSpacing = 0;
      frameLayout.marginWidth = 0;
      frameLayout.marginHeight = 0;     

      frameBar.setLayout(frameLayout);
      
      graphToolBar = new ToolBar(frameBar, SWT.FLAT);
      graphToolBar.addTraverseListener(new TraverseListener()
      {
        public void keyTraversed(TraverseEvent e)
        {
          if (e.detail == SWT.TRAVERSE_MNEMONIC)
           e.doit = false;
        }
      });
              

      backButton = new ToolItem(graphToolBar, SWT.PUSH);
      backButton.setImage(WSDLEditorPlugin.getInstance().getImage("icons/back.gif"));
      // backButton.setToolTipText(WSDLEditorPlugin.getWSDLString("_UI_BACK_TO", "Definition"));
      backButton.setToolTipText(WSDLEditorPlugin.getWSDLString("_UI_BACK"));
      backButton.setEnabled(false);
      backButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
        public void widgetSelected(SelectionEvent e)
        {
          Object object = editor.getGraphViewer().getComponentViewer().getInput();
          if (object instanceof XSDSchema)
          {
            editor.getGraphViewer().setInput(editor.getDefinition());
          }
          else if (object instanceof XSDConcreteComponent)
          {
            // Need to common this up with xsdeditor's BackAction code
            XSDSchema xsdSchema = ((XSDConcreteComponent)object).getSchema();
            boolean flag = true;
            while (flag)
            {
              List list = xsdSchema.getReferencingDirectives();
              if (list.size() > 0)
              {
                XSDSchemaDirective xsdSchemaDirective = (XSDSchemaDirective)list.get(0);
                if (xsdSchemaDirective.getSchema() != null)
                {
                  xsdSchema = xsdSchemaDirective.getSchema();
                }
                else
                {
                  flag = false;
                }
              }
              else
              {
                flag = false;
              }
            }

            editor.getGraphViewer().setInput(xsdSchema);
          }
          else
          {
            editor.getGraphViewer().setInput(editor.getDefinition());
          }
        }
      }); 

      // hack to make the button look like a toggle button.  SWT.TOGGLE is not an allowed style
      // for a ToolItem, but SWT.RADIO is.  SWT.RADIO makes it look like a toggle.......
      showBindingsButton = new ToolItem(graphToolBar, SWT.RADIO);
      showBindingsButton.setImage(WSDLEditorPlugin.getInstance().getImage("icons/hidebinding.gif"));
      showBindingsButton.setToolTipText(WSDLEditorPlugin.getWSDLString("_UI_HIDE_BINDINGS")); 
      showBindingsButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
      	private boolean currentlySelected = false;
      	
        public void widgetSelected(SelectionEvent e)
        {                                             
          ViewMode vm = WSDLGraphModelAdapterFactory.getViewMode((Definition)editor.getDefinition());
          vm.setBindingVisible(!vm.isBindingVisible());
          
          // hack to make the button look like a toggle button
          ToolItem item = (ToolItem) e.widget;
          if (currentlySelected) {
          	item.setSelection(false);
          	currentlySelected = false;
          }
          else {
          	currentlySelected = true;
          }
        }
      }); 

      setTopLeft(frameBar);
    }
    
    public void setBackButtonEnabled(boolean state)
    {
      backButton.setEnabled(state);
    }

    
//    public void updateHoverHelp(Object object)
//    {
//      if (object instanceof XSDSchema)
//      {
//        backButton.setToolTipText(WSDLEditorPlugin.getWSDLString("_UI_BACK_TO", "Definition"));
//      }
//      else if (object instanceof XSDConcreteComponent)
//      {
//        backButton.setToolTipText(WSDLEditorPlugin.getWSDLString("_UI_BACK_TO", "XML Schema"));
//      }
//    }
}