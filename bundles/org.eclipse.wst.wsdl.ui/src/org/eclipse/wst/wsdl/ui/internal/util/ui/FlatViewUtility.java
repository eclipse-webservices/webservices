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
package org.eclipse.wst.wsdl.ui.internal.util.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.flatui.FlatPageHeader;
import org.eclipse.wst.wsdl.ui.internal.util.flatui.WidgetFactory;

public class FlatViewUtility implements PaintListener
{
  Color backgroundColor, foregroundColor;
  Color comboBackgroundColor, comboDisabledColor;
  static Color borderColor;

  // Added for createFlatPageHeader()
  private boolean headingVisible=true;
  private Image headingImage = null;
  private Composite headerControl = null;
  private int TITLE_VMARGIN = 5;
  private FlatPageHeader flatPageHeader = null;

  private KeyboardHandler keyboardHandler;

  public static final int H_SCROLL_INCREMENT = 5;
  public static final int V_SCROLL_INCREMENT = 64;
  
  // this defaults to the flat style, but users can change it if they wish
  private int flatStyle = SWT.FLAT;
  private int border = 0;

  public FlatViewUtility()
  { 
    Display display = Display.getCurrent();
    if (borderColor == null)
    {
      borderColor = new Color(Display.getCurrent(), 195, 191, 179);  	
    }
    backgroundColor = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
    foregroundColor = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
    comboBackgroundColor = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
    comboDisabledColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
    keyboardHandler = new KeyboardHandler();
  }
  
  public FlatViewUtility(boolean isFlat)
  {
    this();
    
    setFlat(isFlat);
  }
  
  public void setFlat(boolean flat)
  {
    Display display = Display.getCurrent();
    if (flat)
    {
      flatStyle = SWT.FLAT;
      backgroundColor = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
      foregroundColor = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);  
    }
    else
    {
      flatStyle = 0;
      border = SWT.BORDER;
      backgroundColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
      foregroundColor = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
    }
  }
  
  public void setColor(Control c)
  {
    if (flatStyle == SWT.FLAT)
    {
      c.setBackground(backgroundColor);
    }
  }
  
  public void paintControl(PaintEvent event)
  {
    Control c = (Control) event.widget;
    if (c.isEnabled()==false)
    {
      if (c instanceof Text
//      || c instanceof Canvas
          || c instanceof List
          || c instanceof CCombo)
      {
        Rectangle b = c.getParent().getBounds();
        GC gc = event.gc;
        gc.setForeground(backgroundColor);
//      gc.drawRectangle(b.x - 1, b.y - 1, b.width + 1, b.height + 1);
        gc.setForeground(borderColor);
//        gc.drawRectangle(b.x - 2, b.y - 2, b.width + 3, b.height + 3);
      }
    }              

    // Defect 252891 : this causes some major problems on Linux GTK
    // This looks rather dodgy in any case since an cyclic painting behaviour
    // is likely occuring here
    //if (c.getParent() != null)
    //{
    //  c.getParent().redraw();
    //}
  }

  private static Font font;

  public static Font getFont()
  {
    if (font == null)
    {
      font = new Font(Display.getCurrent(), "ms sans serif", 8, SWT.NORMAL);
    }
    return font;
  }

  public static void setFont(Font newFont)
  {
    font = newFont;
  }

  public static void setComposite(Composite comp)
  {
    // deprecated.  Remove later
  }

  public Composite createSimpleComposite(Composite parent, int style)
  {
    Composite composite = new Composite(parent, style | flatStyle);
    composite.setFont(getFont());
    setColor(composite);

    GridLayout layout = new GridLayout();
    composite.setLayout(layout);
    return composite;
  }

  public Composite createComposite(Composite parent, int numColumns)
  {
    Composite composite = new Composite(parent, SWT.NONE | flatStyle);
    composite.setFont(getFont());
    GridLayout layout = new GridLayout();
    layout.numColumns = numColumns;
    composite.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    composite.setLayoutData(data);
    setColor(composite);
    if (flatStyle == SWT.FLAT)
    {
      composite.addPaintListener(new BorderPainter());
    }
    return composite;
  }

  public Composite createComposite(Composite parent, int numColumns, boolean horizontalFill)
  {
    if (!horizontalFill)
    {
      createComposite(parent, numColumns);
    }

    Composite composite = new Composite(parent, SWT.NONE | flatStyle);
    composite.setFont(getFont());
    GridLayout layout = new GridLayout();
    layout.numColumns = numColumns;
    composite.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    composite.setLayoutData(data);
    setColor(composite);
    if (flatStyle == SWT.FLAT)
    {
      composite.addPaintListener(new BorderPainter());
    }
    return composite;
  }

  public Composite createComposite(Composite parent, int numColumns, boolean horizontalFill, boolean verticalFill)
  {
    if (!horizontalFill && !verticalFill)
    {
      createComposite(parent, numColumns);
    }

    Composite composite = new Composite(parent, SWT.NONE | flatStyle);
    composite.setFont(getFont());

    GridLayout layout = new GridLayout();
    layout.numColumns = numColumns;
    composite.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.grabExcessVerticalSpace = true;
    composite.setLayoutData(data);
    setColor(composite);
    if (flatStyle == SWT.FLAT)
    {
      composite.addPaintListener(new BorderPainter());
    }
    return composite;
  }

  public SashForm createSashForm(Composite parent, int style)
  {
    SashForm sashForm = new SashForm(parent, style | flatStyle);
    setColor(sashForm);
    return sashForm;
  }

  public PageBook createPageBook(Composite parent, int style)
  {
    PageBook pageBook = new PageBook(parent, style | flatStyle);
    setColor(pageBook);
    if (flatStyle == SWT.FLAT)
    {
      pageBook.addPaintListener(new BorderPainter());
    }
    return pageBook;
  }

  public Label createHeadingLabel(Composite parent, String text, Color bg) {
    return createHeadingLabel(parent, text, bg, SWT.NONE);
  }
  public Label createHeadingLabel(Composite parent, String text, Color bg, int style) {
    Label label = new Label(parent, style);
    label.setText(text);
    setColor(label);
    if (flatStyle == SWT.FLAT)
    {
      label.setForeground(foregroundColor);
    }
    label.setFont(JFaceResources.getFontRegistry().get(JFaceResources.BANNER_FONT));
    return label;
  }

  public Label createHorizontalFiller(Composite parent, int horizontalSpan)
  {
    Label label = new Label(parent, SWT.LEFT | flatStyle);
    setColor(label);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.horizontalSpan = horizontalSpan;
    label.setLayoutData(data);
    return label;
  }

  /**
   * Helper method for creating labels.
   */
  public Label createLabel(Composite parent, int style, String text)
  {
    Label label = new Label(parent, style | flatStyle);
    setColor(label);
    label.setText(text);

    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    label.setLayoutData(data);
    return label;
  }

  public Label createLabel(Composite parent, String text)
  {
    Label label = new Label(parent, SWT.LEFT | flatStyle);
    setColor(label);
    label.setText(text);

    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    label.setLayoutData(data);
    return label;
  }

  public Label createLabel(Composite parent, String text, int alignment)
  {
    Label label = new Label(parent, SWT.LEFT | flatStyle);
    label.setText(text);
    setColor(label);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = alignment;
    label.setLayoutData(data);
    return label;
  }

  /**
   * Helper method for creating buttons.
   */
  public Button createPushButton(Composite parent, String label)
  {
    Button button = new Button(parent, SWT.PUSH | flatStyle);
    button.setText(label);
    setColor(button);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    button.setLayoutData(data);
    button.addKeyListener(keyboardHandler);

    return button;
  }

  public Table createTable(Composite parent)
  {
    Table table = new Table(parent, SWT.SINGLE | SWT.BORDER | flatStyle);
    setColor(table);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    table.setLayoutData(data);
    return table;
  }

  /**
   * Create radio button
   */
  public Button createRadioButton(Composite parent, String label)
  {
    Button button = new Button(parent, SWT.RADIO | flatStyle);
    button.setText(label);
    setColor(button);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    button.setLayoutData(data);
    button.addKeyListener(keyboardHandler);

    return button;
  }

  /**
   * Helper method for creating check box
   */
  public Button createCheckBox(Composite parent, String label)
  {
    Button button = new Button(parent, SWT.CHECK | flatStyle);
    button.setText(label);
    setColor(button);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    button.setLayoutData(data);
    return button;
  }

  public Combo createComboBox(Composite parent)
  {
    return createComboBox(parent, true);
  }

  public Combo createComboBox(Composite parent, boolean isReadOnly)
  {
    int style = isReadOnly == true ? SWT.READ_ONLY : SWT.DROP_DOWN;

    Combo combo = new Combo(parent, style | flatStyle | border);
    setColor(combo);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    combo.setLayoutData(data);
    return combo;
  }

  public CCombo createCComboBox(Composite parent)
  {
    return createCComboBox(parent, true);
  }

  public CCombo createCComboBox(Composite parent, boolean isReadOnly)
  {
    int style = isReadOnly == true ? SWT.READ_ONLY : SWT.DROP_DOWN;

    CCombo combo = new CCombo(parent, style | flatStyle | border);
    // setColor(combo);
    // Always use List Background
    combo.setBackground(comboBackgroundColor);

    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    combo.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      combo.addPaintListener(this);
    }
    else                        
    {                           
      /*   
       // Defect 252891 : I'm not sure if this code will also cause problems on Linux GTK
       // But since its potentially cyclic ... I'm commenting it out for now
       combo.addPaintListener(new PaintListener()
       {
       public void paintControl(PaintEvent event)
       {
       Control c = (Control) event.widget;
       if (c.isEnabled()==true)
       {
       c.setBackground(comboBackgroundColor);
       }
       else
       {
       c.setBackground(comboDisabledColor); 
       }
       final Control con = c;
       Runnable delayedUpdate = new Runnable()
       {
       public void run()
       {
       if (con != null)
       {
       if (!con.isDisposed())  // it gets disposed
       {
       con.redraw();
       }
       }
       }
       };
       Display.getCurrent().asyncExec(delayedUpdate);
       }
       });*/
    }
    return combo;
  }

  public List createListBox(Composite parent, int width, boolean isMultiSelect)
  {
    int style = isMultiSelect ? SWT.MULTI : SWT.SINGLE;
    List list = new List(parent, style  | flatStyle | border);
    setColor(list);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.widthHint = width;
    list.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      list.addPaintListener(this);
    }
    return list;
  }

  public List createListBox(Composite parent, int width, boolean isMultiSelect, boolean verticalFill)
  {
    int style = isMultiSelect ? SWT.MULTI : SWT.SINGLE;
    List list = new List(parent, style  | flatStyle | border);
    setColor(list);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.grabExcessVerticalSpace = verticalFill;
    data.widthHint = width;
    list.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      list.addPaintListener(this);
    }
    return list;
  }

  public List createListBox(Composite parent, int style)
  {
    List list = new List(parent, style | flatStyle | border);
    setColor(list);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.grabExcessVerticalSpace = true;
    list.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      list.addPaintListener(this);
    }
    return list;
  }

  public Text createTextField(Composite parent)
  {
    Text text = new Text(parent, SWT.SINGLE | flatStyle | border);
    setColor(text);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    text.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      text.addPaintListener(this);
    }
    text.addKeyListener(keyboardHandler);
    return text;
  }

  public Text createTextField(Composite parent, int width)
  {
    Text text = new Text(parent, SWT.SINGLE | flatStyle | border);
    setColor(text);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.widthHint = width;
    text.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      text.addPaintListener(this);
    }
    text.addKeyListener(keyboardHandler);
    return text;
  }

  public Text createTextField(Composite parent, int width, int style)
  {
    Text text = new Text(parent, style | SWT.SINGLE | flatStyle | border);
    setColor(text);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.widthHint = width;
    text.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      text.addPaintListener(this);
    }
    return text;
  }

  /**
   * <code>createWrappedMultiTextField</code> creates a wrapped multitext field
   *
   * @param parent a <code>Composite</code> value
   * @param width an <code>int</code> value
   * @param numLines an <code>int</code> value representing number of characters in height
   * @param verticalFill a <code>boolean</code> value
   * @return a <code>Text</code> value
   */
  public Text createWrappedMultiTextField(Composite parent, int width, int numLines, boolean verticalFill)
  {
    Text text = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | flatStyle | border);
    setColor(text);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    if (verticalFill)
    {
      data.verticalAlignment = GridData.FILL;
      data.grabExcessVerticalSpace = true;
    }
    data.widthHint = width;
    FontData[] fontData = getFont().getFontData();
    // hack for now where on Windows, only 1 fontdata exists
    data.heightHint = numLines * fontData[0].getHeight();
    text.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      text.addPaintListener(this);
    }
    return text;
  }

  public Text createMultiTextField(Composite parent, int width, int height, boolean verticalFill)
  {
    Text text = new Text(parent, SWT.MULTI |  SWT.H_SCROLL | SWT.V_SCROLL | flatStyle | border);
    setColor(text);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    if (verticalFill)
    {
      data.verticalAlignment = GridData.FILL;
      data.grabExcessVerticalSpace = true;
    }
    data.widthHint = width;
    data.heightHint = height;
    text.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      text.addPaintListener(this);
    }
    return text;
  }

  public Text createMultiTextField(Composite parent, boolean verticalFill)
  {
    Text text = new Text(parent, SWT.MULTI |  SWT.H_SCROLL | SWT.V_SCROLL | flatStyle | border);
    setColor(text);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    if (verticalFill)
    {
      data.verticalAlignment = GridData.FILL;
      data.grabExcessVerticalSpace = true;
    }
    text.setLayoutData(data);
    if (flatStyle == SWT.FLAT)
    {
      text.addPaintListener(this);
    }
    return text;
  }

  public Group createGroup(Composite parent, int numColumns, String text, boolean verticalFill)
  {
    Group group = new Group(parent, SWT.SHADOW_ETCHED_IN  | flatStyle);
    group.setText(text);
    setColor(group);

    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    if (verticalFill)
    {
      data.verticalAlignment = GridData.FILL;
      data.grabExcessVerticalSpace = true;
    }
    group.setLayoutData(data);

    GridLayout layout = new GridLayout();
    layout.numColumns = numColumns;
    group.setLayout(layout);
    if (flatStyle == SWT.FLAT)
    {
      group.addPaintListener(new BorderPainter());
    }
    return group;
  }

  public Group createGroup(Composite parent, int numColumns, String text, boolean verticalFill, int alignment)
  {
    Group group = new Group(parent, SWT.SHADOW_ETCHED_IN | flatStyle);
    group.setText(text);
    setColor(group);

    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.verticalAlignment = alignment;
    if (verticalFill)
    {
      data.verticalAlignment = GridData.FILL;
      data.grabExcessVerticalSpace = true;
    }
    group.setLayoutData(data);

    GridLayout layout = new GridLayout();
    layout.numColumns = numColumns;
    group.setLayout(layout);
    if (flatStyle == SWT.FLAT)
    {
      group.addPaintListener(new BorderPainter());
    }
    return group;
  }

  public Label createVerticalFiller(Composite parent, int verticalSpan)
  {
    Label label = new Label(parent, SWT.LEFT | flatStyle);
    label.setFont(getFont());
    setColor(label);
    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.verticalSpan = verticalSpan;
    label.setLayoutData(data);

    return label;
  }

  //
  // FlatPageHeader
  //

  // This will use the default background image.
  public Composite createFlatPageHeader
  (Composite parent,
      String title)
  {
    Image bgImage = createDefaultImage();
    return createFlatPageHeader(parent,bgImage,title);
  }

  // Provide your own background image.
  public Composite createFlatPageHeader
  (Composite parent,
      Image bgImage,
      String title)
  {
    FlatPageHeader header = new FlatPageHeader(parent,SWT.NONE);
    header.setLayout(new PageLayout());
    header.setBackgroundImage(bgImage);
    header.setText(title);
    WidgetFactory factory = new WidgetFactory();
    Composite formParent = factory.createComposite(header);
    //Composite formParent = createComposite(header,1);
    createPageContent(formParent);
    headerControl = header; // this is used in getTitleHeight()
    return formParent;
  }

  public void updateFlatPageHeaderTitle(String title)
  {
    ((FlatPageHeader)headerControl).setText(title);
  }

  private void createPageContent(Composite parent) 
  {
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    parent.setLayout(layout);
    parent.setLayoutData(new GridData(GridData.FILL_BOTH));
  }

  class PageLayout extends Layout 
  {
    protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) 
    {
      if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT)
        return new Point(wHint, hHint);
      int x = 0;
      Control client = composite.getChildren()[0];
      Point csize = client.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
      if (headingVisible) csize.y += getTitleHeight();
      
      return csize;
    }
    
    protected void layout(Composite composite, boolean flushCache) 
    {
      Rectangle clientArea = composite.getClientArea();
      Control client = composite.getChildren()[0];
      int theight = headingVisible ? getTitleHeight() : 0;
      client.setBounds(clientArea.x, clientArea.y + theight, clientArea.width, clientArea.height - theight);
    }
  }

  private int getTitleHeight() 
  {
    int imageHeight = 0;
    // Note - Find out why headingImage is always null.
    if (headingImage != null && SWT.getPlatform().equals("motif") == false) 
      imageHeight = headingImage.getBounds().height;

    GC gc = new GC(headerControl);
    gc.setFont(JFaceResources.getHeaderFont());
    FontMetrics fm = gc.getFontMetrics();
    int fontHeight = fm.getHeight();
    gc.dispose();

    int height = fontHeight + TITLE_VMARGIN + TITLE_VMARGIN;
    return Math.max(height,imageHeight);
  }


  private Image createDefaultImage()
  {
    return ImageDescriptor.createFromFile
    (WSDLEditorPlugin.class,
        "icons/page_banner.gif").createImage();
  }
  /**
   * Returns the backgroundColor.
   * @return Color
   */
  public Color getBackgroundColor()
  {
    return backgroundColor;
  }
  
  public class KeyboardHandler extends KeyAdapter
  {
    public void keyPressed(KeyEvent e)
    {
      Widget w = e.widget;
      if (w instanceof Control)
      {
        processKey(e.keyCode, (Control) w);
      }
    }

    private void scrollVertical(ScrolledComposite scomp, boolean up)
    {
      scroll(scomp, 0, up ? -V_SCROLL_INCREMENT : V_SCROLL_INCREMENT);
    }

    private void scrollHorizontal(ScrolledComposite scomp, boolean left)
    {
      scroll(scomp, left ? -H_SCROLL_INCREMENT : H_SCROLL_INCREMENT, 0);
    }

    private void scrollPage(ScrolledComposite scomp, boolean up)
    {
      Point origin = scomp.getOrigin();
      Rectangle clientArea = scomp.getClientArea();
      int increment = up ? -clientArea.height : clientArea.height;
      scroll(scomp, 0, increment);
    }

    private void scroll(ScrolledComposite scomp, int xoffset, int yoffset)
    {
      Point origin = scomp.getOrigin();
      Point contentSize = scomp.getContent().getSize();
      int xorigin = origin.x + xoffset;
      int yorigin = origin.y + yoffset;
      xorigin = Math.max(xorigin, 0);
      xorigin = Math.min(xorigin, contentSize.x - 1);
      yorigin = Math.max(yorigin, 0);
      yorigin = Math.min(yorigin, contentSize.y - 1);
      scomp.setOrigin(xorigin, yorigin);
    }
    
    protected ScrolledComposite getScrolledComposite(Control c)
    {
      Composite parent = c.getParent();
      
      while (parent != null)
      {
        if (parent instanceof ScrolledComposite)
        {
          return (ScrolledComposite) parent;
        }
        parent = parent.getParent();
      }
      return null;
    }
    
    protected void processKey(int keyCode, Control c)
    {
      ScrolledComposite scomp = getScrolledComposite(c);
      if (scomp != null)
      {
        switch (keyCode)
        {
        case SWT.ARROW_DOWN :
          scrollVertical(scomp, false);
          break;
        case SWT.ARROW_UP :
          scrollVertical(scomp, true);
          break;
        case SWT.ARROW_LEFT :
          scrollHorizontal(scomp, true);
          break;
        case SWT.ARROW_RIGHT :
          scrollHorizontal(scomp, false);
          break;
        case SWT.PAGE_UP :
          scrollPage(scomp, true);
          break;
        case SWT.PAGE_DOWN :
          scrollPage(scomp, false);
          break;
        }
      }
    }
  }
}
