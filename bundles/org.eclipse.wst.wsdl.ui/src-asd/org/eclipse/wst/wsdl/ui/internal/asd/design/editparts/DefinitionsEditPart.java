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
import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.BindingColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.InterfaceColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.ServiceColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.xsd.ui.internal.design.figures.SpacingFigure;

public class DefinitionsEditPart extends BaseEditPart {
	  List collections = null;
	  Figure contentPane;
	  
	  protected IFigure createFigure()
	  {    
	    Panel panel = new Panel();    
	    // why do we need to use a container layout?  can we just set a
	    // margin border and get the same effect?
	    ContainerLayout clayout = new ContainerLayout();
	    clayout.setBorder(60);
	    panel.setLayoutManager(clayout);
	    
	    // issue (cs) : why did we need a free form layer?
	    contentPane = new Figure();
	    panel.add(contentPane);
	        
	    ToolbarLayout layout = new ToolbarLayout(true);
	    layout.setStretchMinorAxis(true);
	    layout.setSpacing(0);
	    contentPane.setLayoutManager(layout);
	    return panel;
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

	    return collections;
	  }
	  
	  public void setInput(Object object)
	  {    
	  }
	  
	  // TODO: See if we really need this class.  See above.  If so, where should we
	  // put this class?
	  public class ContainerLayout extends AbstractLayout
	  {                                         
	    protected boolean isHorizontal;
	    protected int spacing = 0;
	    protected int border = 0; 

	    public ContainerLayout()
	    { 
	      this(true, 0); 
	    }             

	    public ContainerLayout(boolean isHorizontal, int spacing)
	    {
	      this.isHorizontal = isHorizontal;   
	      this.spacing = spacing;
	    }  

	    public void setHorizontal(boolean isHorizontal)
	    {
	      this.isHorizontal = isHorizontal;
	    }  

	    public void setSpacing(int spacing)
	    {
	      this.spacing = spacing;
	    }  

	    public void setBorder(int border)
	    {
	      this.border = border;
	    }  

	    protected int alignFigure(IFigure parent, IFigure child)
	    { 
	      return -1;
	    }

	    /**
	     * Calculates and returns the preferred size of the container 
	     * given as input.
	     * 
	     * @param figure  Figure whose preferred size is required.
	     * @return  The preferred size of the passed Figure.
	     * @since 2.0
	     */
	    protected Dimension calculatePreferredSizeHelper(IFigure parent)
	    { 
	      Dimension	preferred = new Dimension();
	    	List children = parent.getChildren();
	  		                                        
	  	  for (int i=0; i < children.size(); i++)
	      {
	  		  IFigure child = (IFigure)children.get(i);      
	      
	        Dimension	childSize = child.getPreferredSize();
	  	  
	        if (isHorizontal)
	        {
	  		    preferred.width += childSize.width;
	  		    preferred.height = Math.max(preferred.height, childSize.height);
	        }
	        else
	        {  
	          preferred.height += childSize.height;
	          preferred.width = Math.max(preferred.width, childSize.width);
	        }
	  	  }   

	      int childrenSize = children.size();
	      if (childrenSize > 1)
	      {                      
	        if (isHorizontal)    
	        {
	          preferred.width += spacing * (childrenSize - 1);
	        }
	        else
	        {
	  		    preferred.height += spacing * (childrenSize - 1);
	        } 
	      }
	                            
	      preferred.width += border * 2;
	      preferred.height += border * 2;
	  	  preferred.width += parent.getInsets().getWidth();
	  	  preferred.height += parent.getInsets().getHeight();       
	    
	    	return preferred;
	    }

	    protected Dimension calculatePreferredSize(IFigure parent, int width, int height)
	    {    
	      Dimension	preferred = null;                                              
	                                    
	      // Here we ensure that an unexpanded container is given a size of (0,0)
	      //
//	      if (parent instanceof IExpandable)
//	      {
//	        IExpandable expandableFigure = (IExpandable)parent;
//	        if (!expandableFigure.isExpanded())
//	        {
//	          preferred = new Dimension(); 
//	        }
//	      }   
//	      
	      if (preferred == null)
	      {
	  	    preferred = calculatePreferredSizeHelper(parent);    
	      }
	      
	      return preferred;
	    }
	       

	    protected void adjustLayoutLocation(IFigure parent, Dimension dimension)
	    {     
	    }   

	    public void layout(IFigure parent)
	    {       
	    	List children = parent.getChildren();
	   
	      int rx = 0;
	      Dimension	dimension = new Dimension();                                          


	  	  for (int i=0; i < children.size(); i++)
	      {
	  		  IFigure child = (IFigure)children.get(i);
	  		  Dimension	childSize = child.getPreferredSize();
	        if (isHorizontal)
	        {   
	          dimension.height = Math.max(dimension.height, childSize.height);
	          rx += childSize.width;
	        }
	        else
	        {
	          dimension.width = Math.max(dimension.width, childSize.width);
	        }
	      }

	  	  //dimension.width += parent.getInsets().left;
	      //dimension.height += parent.getInsets().top;

	      if (isHorizontal)
	      {
	        dimension.height += border*2;
	      	dimension.width += border;
	      }
	      else
	      {
	        dimension.width += border*2;
	      	dimension.height += border;
	      }
	      adjustLayoutLocation(parent, dimension);    

	      for (int i=0; i < children.size(); i++)
	      {
	        IFigure child = (IFigure)children.get(i);
	  	    Dimension	childSize = child.getPreferredSize();
	          
	        if (isHorizontal)
	        {   
	          int y = -1; 
	      
	          y = alignFigure(parent, child);
	      
	          if (y == -1)
	          {
	             y = (dimension.height - childSize.height) / 2;                                      
	          }                      
	                                                     
	          Rectangle rectangle = new Rectangle(dimension.width, y, childSize.width, childSize.height);
	          rectangle.translate(parent.getClientArea().getLocation());                           


	          child.setBounds(rectangle);                           
	  	      dimension.width += childSize.width; 
	          dimension.width += spacing;       

	          if (child instanceof SpacingFigure)
	          {          
	            int availableHorizontalSpace = parent.getClientArea().width - rx;
	            dimension.width += availableHorizontalSpace;
	          }           
	        }
	        else
	        {
	          Rectangle rectangle = new Rectangle(0, dimension.height, childSize.width, childSize.height);
	  	      rectangle.translate(parent.getClientArea().getLocation());                          
	          

	          child.setBounds(rectangle);  
	          dimension.height += childSize.height;
	          dimension.height += spacing;
	        }
	  	  }	      
	    }                                      
	  }
}
