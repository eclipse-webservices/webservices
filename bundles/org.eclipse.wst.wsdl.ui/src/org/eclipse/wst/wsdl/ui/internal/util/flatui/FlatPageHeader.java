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
package org.eclipse.wst.wsdl.ui.internal.util.flatui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class FlatPageHeader extends Canvas implements IPropertyChangeListener 
{
	private Image backgroundImage;  
	private String text;
	private Image buttonImage;
	private Color imageBackgroundColor;
	private Color textForegroundColor;
	private int textHMargin = 10;
	private int textVMargin = 5;
	private Font headerFont;

	private List listeners;
	private int buttonImageX;
	private int buttonImageY;
	
  // Usage: 
  //    See org.eclipse.wst.common.ui.FlatViewUtility.createFlatPageHeader() 
  //    methods.
  //
  //  Ex)
  //  Composite parent = ... ;
  //  Image image = ... ;
  //  Layout layout = ... ;
  //  String title = ... ;
  //  WidgetFactory factory = ... ;
  //
  //  header = new FlatPageHeader(parent,SWT.NONE);
  //  header.setLayout(layout);
  //  header.setBackgroundImage(image);
  //  header.setText(title);
  //  Composite formParent = factory.createComposite(header);
  //  
  //  GridLayout gl = new GridLayout();
  //  gl.numColumns = 1;
  //  formParent.setLayout(gl);
  //  formParent.setLayoutData(new GridData(GridData.FILL_BOTH));
  //
	public FlatPageHeader(Composite parent, int style) 
  {
		super(parent, style);
		
		// The default image background color is white
		// The default text forekground color is black
		imageBackgroundColor = new Color(null, 255, 255, 255);
		textForegroundColor = new Color(null, 0, 0, 0);
		setBackground(imageBackgroundColor);
		
		// The default text font is Header font
		headerFont = JFaceResources.getHeaderFont();
		
		// Need to dispose the Color
		addDisposeListener
      (new DisposeListener() 
        {
			    public void widgetDisposed(DisposeEvent e) 
          {
				    FlatPageHeader.this.widgetDisposed(e);
			    }
		    } 
      );
		 
		// need to paint
		addPaintListener
      (new PaintListener() 
        {
			    public void paintControl(PaintEvent e) 
          {
			      FlatPageHeader.this.paintControl(e);
			    }
		    }
      );

		// create a list of listeners
		listeners = new ArrayList();

		// add a mouse listener
		this.addMouseListener
      (new MouseAdapter() 
        {
			    public void mouseDown(MouseEvent event) 
          {
				    handleMouseDown(event);
			    }
		    }
      );	
		
		JFaceResources.getFontRegistry().addListener(this);
	}  

	// Compute size of the widget
	public Point computeSize(int wHint, int hHint, boolean changed) 
  {
		int width = getParent().getClientArea().width;
		int height = 0;
		if (backgroundImage != null) 
    {
			Rectangle bounds = backgroundImage.getBounds();
			height = bounds.height;
		}
		if (text != null) 
    {
			GC gc = new GC(this);
			int textHeight = getTextHeight(gc);
			gc.dispose();
			height = Math.max(height, textHeight);
		}
		if (buttonImage != null) 
    { 
			Rectangle bounds = buttonImage.getBounds();
			if (height < bounds.height)
				height = bounds.height;
		}
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;          
		return new Point(width + 2, height + 2);     
	}	

	protected void paintControl(PaintEvent e) 
  {
		GC gc = e.gc;
		if (backgroundImage != null) 
    {
			Rectangle imageBounds = backgroundImage.getBounds();
			int y = 0;
			int x = 0;

			if (imageBackgroundColor != null) 
      {
				gc.setBackground(imageBackgroundColor);
				gc.fillRectangle(0, 0, imageBounds.width, imageBounds.height);
			}

			if (SWT.getPlatform().equals("motif")==false) 
      {
				gc.drawImage(backgroundImage, x, y);
			}
			if (textForegroundColor != null)
				gc.setForeground(textForegroundColor);
			gc.setFont(headerFont);
//			gc.setFont(getFont());	
			gc.drawText(text, textHMargin, textVMargin, true);
			if (buttonImage != null) 
      {
				Rectangle parentBounds = getParent().getClientArea();
				Rectangle buttonBounds = buttonImage.getBounds();
				int p = parentBounds.x + parentBounds.width;
				buttonImageX = p - buttonBounds.width - 4;
				buttonImageY = 4;
				gc.drawImage(buttonImage, buttonImageX, buttonImageY);
			}
		}		
	}
	
	public void addSelectionListener(SelectionListener listener) 
  {
		listeners.add(listener);
	}
       
	public void removeSelectionListener(SelectionListener listener) 
  {
		listeners.remove(listener);
	}	
	
	protected void handleMouseDown(MouseEvent event) 
  {
		if (buttonImage == null)
			return;
		// chekc if the Button image is clicked
		int x = event.x;
		int y = event.y;
		if (x < buttonImageX || x > buttonImageX + buttonImage.getBounds().width) 
    {
			return;
		}
		if (y < buttonImageY || y > buttonImageY + buttonImage.getBounds().height) 
    {
			return;
		}
		
		int size = listeners.size();
		for (int i = 0; i < size; i++) 
    {
			SelectionListener listener = (SelectionListener)listeners.get(i);
			listener.widgetSelected(null);
		}
	}
	
	protected void widgetDisposed(DisposeEvent e) 
  {
		if (imageBackgroundColor != null) imageBackgroundColor.dispose();
		if (textForegroundColor != null) textForegroundColor.dispose();
		if (backgroundImage != null) backgroundImage.dispose();
	}
	
	// getters and setters
	public Image getBackgroundImage() 
  {
		return backgroundImage;
	}
      
	public void setBackgroundImage(Image backgroundImage) 
  {
		this.backgroundImage = backgroundImage;
		redraw();
	}

	public Image getButtonImage() 
  {
		return buttonImage;
	}
      
	public void setButtonImage(Image buttonImage) 
  {
		this.buttonImage = buttonImage;
		redraw();
	}
      
	public String getText() 
  {
		return text;
	}
      
	public void setText(String text) 
  {
		this.text = text;
		redraw();
	}
	
	public int getTextHMargin() 
  {
		return textHMargin;
	}
	
	public void setTextHMargin(int textHMargin) 
  {
		this.textHMargin = textHMargin;
	}
	
	public int getTextVMargin() 
  {
		return textVMargin;
	}
	
	public void setTextVMargin(int textVMargin) 
  {
		this.textVMargin = textVMargin;
	}
	
	public Color getImageBackgroundColor() 
  {
		return imageBackgroundColor;
	}
	
	public void setImageBackgroundColor(Color imageBackgroundColor) 
  {
		if (imageBackgroundColor == null)
			return;
		this.imageBackgroundColor = imageBackgroundColor;
		redraw();
	}
	
	public Color getTextForegroundColor() 
  {
		return textForegroundColor;
	}
	
	public void setTextForegroundColor(Color textForegroundColor) 
  {
		if (textForegroundColor == null)
			return;
		this.textForegroundColor = textForegroundColor;
		redraw();
	}

	private int getTextHeight(GC gc) 
  {
		int imageHeight = 0;
		if (backgroundImage!= null && SWT.getPlatform().equals("motif")==false) 
    {
			imageHeight = backgroundImage.getBounds().height;
		}
		gc.setFont(headerFont);
//		gc.setFont(getFont());
		FontMetrics fm = gc.getFontMetrics();
		int fontHeight = fm.getHeight();
		int height =  fontHeight + textVMargin + textVMargin;
		return Math.max(height, imageHeight);
	}

//	private int getTextWidth(GC gc) 
//  {
//		int imageWidth = 0;
//		if (backgroundImage!= null && SWT.getPlatform().equals("motif")==false) 
//    {
//			imageWidth = backgroundImage.getBounds().width;
//		}
//		gc.setFont(headerFont);
////		gc.setFont(getFont());
//		FontMetrics fm = gc.getFontMetrics();
//		int fontWidth = fm.getAverageCharWidth() + 5;
//		int width =  fontWidth * text.length() + textHMargin + textHMargin;
//		return Math.max(width, imageWidth);
//	}
	
	public void handleEvent(Event e) 
  {
//		Widget source = e.widget;	
	}
	
	public void propertyChange(PropertyChangeEvent event) 
  {
		if(event.getProperty() == JFaceResources.HEADER_FONT && this.isDisposed() == false)
		{
			headerFont = JFaceResources.getFontRegistry().get(JFaceResources.HEADER_FONT);
			this.layout(true);
			this.redraw();
		}
	}
}
