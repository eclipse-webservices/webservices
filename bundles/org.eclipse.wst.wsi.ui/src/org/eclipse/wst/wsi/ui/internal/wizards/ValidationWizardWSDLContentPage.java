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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.wst.wsi.ui.internal.WSIUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page that allows the user to specify the location of the 
 * WSDL document for the Web service.
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */

public class ValidationWizardWSDLContentPage extends WizardPage implements SelectionListener
{
  IStructuredSelection selection;

  public static final String PORT = "port";
  public static final String OPERATION = "operation";
  public static final String BINDING = "binding";
  public static final String PORTTYPE = "porttype";
  public static final String MESSAGE = "message";
  /**
   * Access to the includeWSDL flag.  
   */
  protected Button wsdlPortButton;
  protected Button wsdlBindingButton;
  protected Button wsdlPortTypeButton;
  protected Button wsdlOperationButton;
  protected Button wsdlMessageButton;

  protected Combo nameCombo;
  protected Text namespaceText;
  //protected Combo parentCombo;
  //protected Text nameText;
  protected Text parentnameText;

  protected List ports = null;
  protected List operations = null;
  protected List bindings = null;
  protected List porttypes = null;
  protected List messages = null;

  /**
   * Constructor.
   * 
   * @param selection: selection in the Resource Navigator view.
   */
  public ValidationWizardWSDLContentPage(IStructuredSelection selection)
  {
    super("ValidationWizardWSDLContentPage");
    this.selection = selection;
    this.setTitle(WSIUIPlugin.getResourceString("_UI_WIZARD_V_SELECT_WSDL_ELEMENT_HEADING"));
    this.setDescription(WSIUIPlugin.getResourceString("_UI_WIZARD_V_SELECT_WSDL_ELEMENT_EXPL"));
  }

  /**
   * Always returns true.
   */
  public boolean isPageComplete()
  {
    if (((ValidationWizard) getWizard()).includeWSDLFile())
    {
      int selection = nameCombo.getSelectionIndex();
      if (selection != -1 && !nameCombo.getItem(selection).equals(""))
      {
        return true;
      }
      return false;
    }
    return true;
  }

  /*
   * Creates the top level control for this page under the given 
   * parent composite. Implementors are responsible for ensuring
   * that the created control can be accessed via getControl 
   *
   * @param parent - the parent composite
   */
  public void createControl(Composite parent)
  {
    Composite base = new Composite(parent, SWT.NONE);
    base.setLayout(new GridLayout());
    GridData data;

    // create a group
    Group wsdlElementGroup = new Group(base, SWT.SHADOW_ETCHED_IN);
    wsdlElementGroup.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_GROUP_TEXT_ELEMENT"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    wsdlElementGroup.setLayoutData(data);
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    wsdlElementGroup.setLayout(layout);

    // create a base panel with 2 columns
    Composite basePanel = new Composite(wsdlElementGroup, SWT.NONE);
    layout = new GridLayout();
    layout.numColumns = 2;
    layout.makeColumnsEqualWidth = false;
    basePanel.setLayout(layout);
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    basePanel.setLayoutData(data);

    // content for first column
    createWSDLElementTypeContent(basePanel);

    // content for second column
    createWSDLElementContent(basePanel);

    setControl(base);
  }

  /**
   * Create a group of radio buttons indicating the type of WSDL element.
   * The five tpes are: port, binding, portType, operation and message.
   * @param parent the parent container. 
   */
  protected void createWSDLElementTypeContent(Composite parent)
  {
    // create a group
    Group wsdlTypeGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
    wsdlTypeGroup.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_GROUP_TEXT_TYPE"));
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = false;
    wsdlTypeGroup.setLayoutData(data);
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    wsdlTypeGroup.setLayout(layout);

    wsdlPortButton = new Button(wsdlTypeGroup, SWT.RADIO);
    wsdlPortButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_RADIO_PORT"));
    wsdlPortButton.setData("wsdlPortButton");
    wsdlPortButton.setSelection(true);
    wsdlPortButton.addSelectionListener(new PortButtonSelectionListener());

    wsdlBindingButton = new Button(wsdlTypeGroup, SWT.RADIO);
    wsdlBindingButton.setData("wsdlBindingButton");
    wsdlBindingButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_RADIO_BINDING"));
    wsdlBindingButton.setSelection(false);
    wsdlBindingButton.addSelectionListener(new BindingButtonSelectionListener());

    wsdlPortTypeButton = new Button(wsdlTypeGroup, SWT.RADIO);
    wsdlPortTypeButton.setData("wsdlPortTypeButton");
    wsdlPortTypeButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_RADIO_PORT_TYPE"));
    wsdlPortTypeButton.setSelection(false);
    wsdlPortTypeButton.addSelectionListener(new PortTypeButtonSelectionListener());

    wsdlOperationButton = new Button(wsdlTypeGroup, SWT.RADIO);
    wsdlOperationButton.setData("wsdlOperationButton");
    wsdlOperationButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_RADIO_OPERATION"));
    wsdlOperationButton.setSelection(false);
    wsdlOperationButton.addSelectionListener(new OperationButtonSelectionListener());

    wsdlMessageButton = new Button(wsdlTypeGroup, SWT.RADIO);
    wsdlMessageButton.setData("wsdlMessageButton");
    wsdlMessageButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_RADIO_MESSAGE"));
    wsdlMessageButton.setSelection(false);
    wsdlMessageButton.addSelectionListener(new MessageButtonSelectionListener());
  }

  /**
   * Create a panel to capture the name, namespace and parent of an
   * WSDL element.
   * @param parent the parent container. 
   */
  protected void createWSDLElementContent(Composite parent)
  {
    // create a grouping with 2 columns, one for labels and theother for fields
    Composite fieldColumn = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.makeColumnsEqualWidth = false;
    fieldColumn.setLayout(layout);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    fieldColumn.setLayoutData(data);

    // The WSDL element name label
    Label label = new Label(fieldColumn, SWT.LEFT);
    label.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_LABEL_NAME"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    label.setLayoutData(data);

    // The WSDL element name field
    nameCombo = new Combo(fieldColumn, SWT.READ_ONLY);
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    nameCombo.setLayoutData(data);
    nameCombo.addSelectionListener(new NameSelectionListener());
    //nameText = new Text(fieldColumn, SWT.SINGLE | SWT.BORDER);
    //data = new GridData();
    //data.horizontalAlignment = GridData.FILL;
    //data.grabExcessHorizontalSpace = true;
    //data.widthHint = 50;
    //nameText.setLayoutData(data);
    //nameText.addModifyListener(new TextBoxListener());

    // The WSDL element namespace label
    label = new Label(fieldColumn, SWT.LEFT);
    label.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_LABEL_NAMESPACE"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    label.setLayoutData(data);

    // The WSDL element namespace field
    namespaceText = new Text(fieldColumn, SWT.SINGLE | SWT.BORDER);
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.widthHint = 50;
    namespaceText.setLayoutData(data);
    namespaceText.addModifyListener(new TextBoxListener());
    namespaceText.setEditable(false);

    // The WSDL element parent label
    label = new Label(fieldColumn, SWT.LEFT);
    label.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_LABEL_PARENT"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    label.setLayoutData(data);

    // The WSDL element parent field
    //    parentCombo = new Combo(fieldColumn, SWT.READ_ONLY);
    //    data = new GridData();
    //    data.horizontalAlignment = GridData.FILL;
    //    data.grabExcessHorizontalSpace = true;
    //    parentCombo.setLayoutData(data);
    parentnameText = new Text(fieldColumn, SWT.SINGLE | SWT.BORDER);
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.widthHint = 50;
    parentnameText.setLayoutData(data);
    parentnameText.addModifyListener(new TextBoxListener());
    parentnameText.setEditable(false);
  }

  /**
   * Always return true.
   */
  public boolean performFinish()
  {
    return true;
  }

  /**
   * Sent when default selection occurs in the control.
   * @param e - an event containing information about the selection
   */
  public void widgetDefaultSelected(SelectionEvent e)
  {}

  /**
   * Handles the includeWSDL checkbox.
   * Sent when selection occurs in the control. 
   * @param e - an event containing information about the selection
   */
  public void widgetSelected(SelectionEvent e)
  {}

  /**
   * Get the element name.
   * 
   * @return The element name.
   */
  public String getElementName()
  {
    int selection = nameCombo.getSelectionIndex();
    if(selection != -1)
    {  
      return nameCombo.getItem(selection);
    }
    return "";
  }

  /**
   * Get the namespace.
   * 
   * @return The namespace.
   */
  public String getNamespace()
  {
    return namespaceText.getText();
  }

  /**
   * Get the name of the parent of the element.
   * 
   * @return The name of the parent of the element.
   */
  public String getParentName()
  {
    //return parentCombo.getItem(parentCombo.getSelectionIndex());
    return parentnameText.getText();
  }

  /**
   * Return the type of the element.
   * 
   * @return the type of the element.
   */
  public String getType()
  {
    if (wsdlBindingButton.getSelection())
    {
      return BINDING;
    }
    else if (wsdlMessageButton.getSelection())
    {
      return MESSAGE;
    }
    else if (wsdlOperationButton.getSelection())
    {
      return OPERATION;
    }
    else if (wsdlPortButton.getSelection())
    {
      return PORT;
    }
    else if (wsdlPortTypeButton.getSelection())
    {
      return PORTTYPE;
    }
    return null;

  }

  public void addElement(String type, String name, String namespace, String parent)
  {
    if (ports == null)
    {
      resetElements();
    }
    if (type.equals(PORT))
    {
      ports.add(new WSDLElement(name, namespace, parent));
    }
    else if (type.equalsIgnoreCase(OPERATION))
    {
      operations.add(new WSDLElement(name, namespace, parent));
    }
    else if (type.equalsIgnoreCase(BINDING))
    {
      bindings.add(new WSDLElement(name, namespace, parent));
    }
    else if (type.equalsIgnoreCase(PORTTYPE))
    {
      porttypes.add(new WSDLElement(name, namespace, parent));
    }
    else if (type.equalsIgnoreCase(MESSAGE))
    {
      messages.add(new WSDLElement(name, namespace, parent));
    }
  }

  public void resetElements()
  {
    ports = new Vector();
    operations = new Vector();
    bindings = new Vector();
    porttypes = new Vector();
    messages = new Vector();
    nameCombo.clearSelection();
    nameCombo.removeAll();
  }

  /**
   * Add value to the list box
   */
  class WorkspaceButtonListener implements SelectionListener
  {
    public void widgetDefaultSelected(SelectionEvent e)
    {}

    public void widgetSelected(SelectionEvent e)
    {}
  }

  /**
   * Add value to the list box
   */
  class BrowseButtonListener implements SelectionListener
  {
    public void widgetDefaultSelected(SelectionEvent e)
    {}

    public void widgetSelected(SelectionEvent e)
    {}
  }

  /**
   * Add value to the list box
   */
  class SearchUDDIButtonListener implements SelectionListener
  {
    public void widgetDefaultSelected(SelectionEvent e)
    {}

    public void widgetSelected(SelectionEvent e)
    {}
  }
  /**
   * Add value to the file field.
   */
  class TextBoxListener implements ModifyListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
    public void modifyText(ModifyEvent e)
    {
      getContainer().updateButtons();

    }

  }
  private class WSDLElement
  {
    private String name;
    private String namespace;
    private String parent;

    public WSDLElement(String name, String namespace, String parent)
    {
      this.name = name;
      this.namespace = namespace;
      this.parent = parent;
    }

    public String getName()
    {
      return name;
    }

    public String getNamespace()
    {
      return namespace;
    }

    public String getParentName()
    {
      return parent;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
   */
  public void setVisible(boolean visible)
  {
    resetElements();
    super.setVisible(visible);
    if (visible)
    {
      try
      {
        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        Definition defElem = reader.readWSDL(((ValidationWizard) getWizard()).getWSDLFile());
        QName defQName = defElem.getQName();
        String defname = "";
        if (defQName != null)
        {
          defname = defQName.getLocalPart();
        }
        String namespace = defElem.getTargetNamespace();

        // register the ports
        Map servmap = defElem.getServices();
        if (servmap != null)
        {
          Collection servs = servmap.values();
          if (servs != null)
          {
            Iterator servIter = servs.iterator();
            while (servIter.hasNext())
            {
              Service service = (Service) servIter.next();
              String servname = service.getQName().getLocalPart();

              Map portmap = service.getPorts();
              if (portmap != null)
              {
                Collection ports = portmap.values();
                if (ports != null)
                {
                  Iterator portIter = ports.iterator();
                  while (portIter.hasNext())
                  {
                    Port port = (Port) portIter.next();
                    String name = port.getName();
                    addElement(ValidationWizardWSDLContentPage.PORT, name, namespace, servname);
                  }
                }
              }
            }
          }
        }

        // Register the Bindings
        Map bindingmap = defElem.getBindings();
        if (bindingmap != null)
        {
          Collection bindings = bindingmap.values();
          if (bindings != null)
          {
            Iterator bindIter = bindings.iterator();
            while (bindIter.hasNext())
            {
              Binding binding = (Binding) bindIter.next();
              String name = binding.getQName().getLocalPart();
              addElement(ValidationWizardWSDLContentPage.BINDING, name, namespace, defname);
            }
          }
        }

        // Register the PortTypes and Operations
        Map porttypemap = defElem.getPortTypes();
        if (porttypemap != null)
        {
          Collection porttypes = porttypemap.values();
          if (porttypes != null)
          {
            Iterator porttypeIter = porttypes.iterator();
            while (porttypeIter.hasNext())
            {
              PortType porttype = (PortType) porttypeIter.next();
              String porttypename = porttype.getQName().getLocalPart();
              addElement(ValidationWizardWSDLContentPage.PORTTYPE, porttypename, namespace, defname);

              // Register the Operations
              List operations = porttype.getOperations();
              if (operations != null)
              {
                Iterator operIter = operations.iterator();
                while (operIter.hasNext())
                {
                  Operation operation = (Operation) operIter.next();
                  String name = operation.getName();
                  addElement(ValidationWizardWSDLContentPage.OPERATION, name, namespace, porttypename);
                }
              }
            }
          }
        }
        Map messagemap = defElem.getMessages();
        if (messagemap != null)
        {
          Collection messages = messagemap.values();
          if (messages != null)
          {
            Iterator messIter = messages.iterator();
            while (messIter.hasNext())
            {
              Message message = (Message) messIter.next();
              String name = message.getQName().getLocalPart();
              addElement(ValidationWizardWSDLContentPage.MESSAGE, name, namespace, defname);
            }
          }
        }
        nameCombo.removeAll();
        namespaceText.setText("");
        parentnameText.setText("");
        int numElems = ports.size();
        WSDLElement[] elems = (WSDLElement[]) ports.toArray(new WSDLElement[numElems]);
        for (int i = 0; i < numElems; i++)
        {
          nameCombo.add(elems[i].getName(), i);
          if (i == 0)
          {
            nameCombo.select(0);
            namespaceText.setText(elems[i].getNamespace());
            parentnameText.setText(elems[i].getParentName());

          }
        }
        wsdlPortButton.setSelection(true);
        wsdlBindingButton.setSelection(false);
        wsdlMessageButton.setSelection(false);
        wsdlOperationButton.setSelection(false);
        wsdlPortTypeButton.setSelection(false);

      }
      catch (WSDLException e)
      {
        // The WSDL file is not valid.
      }
    }
    else
    {
      resetElements();
    }

  }

  class PortButtonSelectionListener implements SelectionListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetDefaultSelected(SelectionEvent e)
    {
      widgetSelected(e);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
      nameCombo.removeAll();
      namespaceText.setText("");
      parentnameText.setText("");
      int numElems = ports.size();
      WSDLElement[] elems = (WSDLElement[]) ports.toArray(new WSDLElement[numElems]);
      for (int i = 0; i < numElems; i++)
      {
        nameCombo.add(elems[i].getName(), i);
        if (i == 0)
        {
          nameCombo.select(0);
          namespaceText.setText(elems[i].getNamespace());
          parentnameText.setText(elems[i].getParentName());

        }
      }
    }

  }
  class BindingButtonSelectionListener implements SelectionListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetDefaultSelected(SelectionEvent e)
    {
      widgetSelected(e);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
      nameCombo.removeAll();
      namespaceText.setText("");
      parentnameText.setText("");
      int numElems = bindings.size();
      WSDLElement[] elems = (WSDLElement[]) bindings.toArray(new WSDLElement[numElems]);
      for (int i = 0; i < numElems; i++)
      {
        nameCombo.add(elems[i].getName(), i);
        if (i == 0)
        {
          nameCombo.select(0);
          namespaceText.setText(elems[i].getNamespace());
          parentnameText.setText(elems[i].getParentName());
        }
      }

    }

  }
  class PortTypeButtonSelectionListener implements SelectionListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetDefaultSelected(SelectionEvent e)
    {
      widgetSelected(e);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
      nameCombo.removeAll();
      namespaceText.setText("");
      parentnameText.setText("");
      int numElems = porttypes.size();
      WSDLElement[] elems = (WSDLElement[]) porttypes.toArray(new WSDLElement[numElems]);
      for (int i = 0; i < numElems; i++)
      {
        nameCombo.add(elems[i].getName(), i);
        if (i == 0)
        {
          nameCombo.select(0);
          namespaceText.setText(elems[i].getNamespace());
          parentnameText.setText(elems[i].getParentName());
        }
      }

    }

  }
  class OperationButtonSelectionListener implements SelectionListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetDefaultSelected(SelectionEvent e)
    {
      widgetSelected(e);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
      nameCombo.removeAll();
      namespaceText.setText("");
      parentnameText.setText("");
      int numElems = operations.size();
      WSDLElement[] elems = (WSDLElement[]) operations.toArray(new WSDLElement[numElems]);
      for (int i = 0; i < numElems; i++)
      {
        nameCombo.add(elems[i].getName(), i);
        if (i == 0)
        {
          nameCombo.select(0);
          namespaceText.setText(elems[i].getNamespace());
          parentnameText.setText(elems[i].getParentName());
        }
      }

    }

  }
  class MessageButtonSelectionListener implements SelectionListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetDefaultSelected(SelectionEvent e)
    {
      widgetSelected(e);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
      nameCombo.removeAll();
      namespaceText.setText("");
      parentnameText.setText("");
      int numElems = messages.size();
      WSDLElement[] elems = (WSDLElement[]) messages.toArray(new WSDLElement[numElems]);
      for (int i = 0; i < numElems; i++)
      {
        nameCombo.add(elems[i].getName(), i);
        if (i == 0)
        {
          nameCombo.select(0);
          namespaceText.setText(elems[i].getNamespace());
          parentnameText.setText(elems[i].getParentName());
        }
      }

    }

  }

  class NameSelectionListener implements SelectionListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetDefaultSelected(SelectionEvent e)
    {
      widgetSelected(e);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
      int selection = ((Combo) e.getSource()).getSelectionIndex();
      String type = getType();
      WSDLElement elem = null;
      if (type.equals(PORT))
      {
        elem = (WSDLElement) ports.get(selection);
      }
      else if (type.equals(BINDING))
      {
        elem = (WSDLElement) bindings.get(selection);
      }
      else if (type.equals(PORTTYPE))
      {
        elem = (WSDLElement) porttypes.get(selection);
      }
      else if (type.equals(OPERATION))
      {
        elem = (WSDLElement) operations.get(selection);
      }
      else if (type.equals(MESSAGE))
      {
        elem = (WSDLElement) messages.get(selection);
      }

      if (elem != null)
      {
        namespaceText.setText(elem.getNamespace());
        parentnameText.setText(elem.getParentName());
      }
    }

  }
}
