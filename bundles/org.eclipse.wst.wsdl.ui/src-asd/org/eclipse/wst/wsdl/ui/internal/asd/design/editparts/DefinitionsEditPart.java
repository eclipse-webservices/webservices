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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.AbstractModelCollection;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.BindingColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.InterfaceColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.ServiceColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;

public class DefinitionsEditPart extends BaseEditPart {
	  List collections = null;
	  Figure contentPane;
      Label messageLabel; 
      InternalLayoutListener internalLayoutListener = new InternalLayoutListener();
	  
	  protected IFigure createFigure()
	  {    
	    Panel panel = new Panel();    
        ToolbarLayout toolbarLayout = new ToolbarLayout(true);
        panel.setLayoutManager(toolbarLayout);
        panel.setBorder(new MarginBorder(60, 30, 30, 30));
	    contentPane = new Figure();
	    panel.add(contentPane);

        messageLabel = new Label("");
        contentPane.add(messageLabel);
      
	    ToolbarLayout layout = new ToolbarLayout(true);
	    layout.setStretchMinorAxis(true);
	    layout.setSpacing(0);
	    contentPane.setLayoutManager(layout);
	    return panel;
	  }
	  
      public void activate()
      {
        super.activate();
        
        // here we want to ensure the binding column is listening
        // to the interface column so that it can re-layout when 
        // the interface changes
        for (Iterator i = getChildren().iterator(); i.hasNext(); )
        {
          AbstractGraphicalEditPart child = (AbstractGraphicalEditPart)i.next();
          if (child.getModel() instanceof InterfaceColumn)
          {
            child.getFigure().addLayoutListener(internalLayoutListener);
            break;
          }  
        }           
      }
                
	  public void refresh() {
		  super.refresh();
		  
		  // Refresh the connecting lines
		  refreshConnections();
	  }
	  
	  public IFigure getContentPane()
	  {
	    return contentPane;
	  }
	  

	  protected void createEditPolicies()
	  {
	    // TODO Auto-generated method stub
	  }
	  
      public void setModelChildren(List list)
      {
        collections = list;
      }
      
	  protected List getModelChildren()
	  { 
	    if (collections == null)
	    {
	      collections = new ArrayList();
	      
	      IDescription description = (IDescription) getModel();
	      if (description != null)
	      {  
	        collections.add(new ServiceColumn(description));
	        collections.add(new BindingColumn(description));
	        collections.add(new InterfaceColumn(description));
	      }
	    }
      
      boolean hasChildren = false;
      for (Iterator i = collections.iterator(); i.hasNext(); )
      {
        AbstractModelCollection column = (AbstractModelCollection)i.next();
        if (column.hasChildren())
        {
          hasChildren = true;
        }
      }

      if (hasChildren)
      {
        messageLabel.setText("");
        if (contentPane.getChildren().contains(messageLabel))
          contentPane.remove(messageLabel);
      }
      else
      {
        messageLabel.setText(Messages.getString("_UI_LABEL_RIGHT_CLICK_TO_INSERT_CONTENT")); //$NON-NLS-1$
        if (!contentPane.getChildren().contains(messageLabel))
          contentPane.add(messageLabel);
      }
      

	    return collections;
	  }
	  
	  public void setInput(Object object)
	  {    
	  }	 
      
      private class InternalLayoutListener extends LayoutListener.Stub
      {
        public void postLayout(IFigure container)
        {
          for (Iterator i = getChildren().iterator(); i.hasNext(); )
          {
            EditPart child = (EditPart)i.next();
            if (child instanceof BindingColumnEditPart)
            {
              ((BindingColumnEditPart)child).refreshBindingEditParts();
              break;
            }
          }          
        }   
      }        
}
