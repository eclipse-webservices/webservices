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
package org.eclipse.wst.wsdl.ui.internal.wizards;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.sse.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.ValidateHelper;
import org.eclipse.wst.xml.ui.dialogs.UpdateListener;

public class WSDLNewFileOptionsPage extends WizardPage implements ModifyListener, UpdateListener, SelectionListener
{
  protected Text targetNamespaceText;
  protected Text prefixText;
  protected boolean updatePortOpFieldBoolean = true;
  
  protected PageBook protocolPageBook;
  protected Combo protocolCombo;
  protected Composite soapPage;
  protected Composite httpPage;
  protected Button docLitRadio;
  protected Button rpcLitRadio;
  protected Button rpcEncRadio;
  protected Button httpGetRadio;
  protected Button httpPostRadio;
  
//  private BindingGenerator generator;

  /**
   * Constructor for WSDLNewFileOptionsPage.
   * @param pageName
   */
  public WSDLNewFileOptionsPage(String pageName)
  {
    super(pageName);
  }
  /**
   * Constructor for WSDLNewFileOptionsPage.
   * @param pageName
   * @param title
   * @param titleImage
   */
  public WSDLNewFileOptionsPage(String pageName, String title, ImageDescriptor titleImage)
  {
    super(pageName, title, titleImage);
    setDescription(WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_NEW_WSDL_FILE")); //$NON-NLS-1$
  }
  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent)
  {
    Composite base = new Composite(parent, SWT.NONE);
    WorkbenchHelp.setHelp(base, WSDLEditorPlugin.getWSDLString("_UI_HELP")); //$NON-NLS-1$
    base.setLayout(new GridLayout());

    //  Group wsdlGroup = ViewUtility.createGroup(base, 2, "WSDL", false);
    Composite wsdlGroup = new Composite(base, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    wsdlGroup.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    wsdlGroup.setLayoutData(data);
    
    GridData gd = (GridData)wsdlGroup.getLayoutData();
    gd.grabExcessHorizontalSpace = true;

    Label targetNamespace = new Label(wsdlGroup, SWT.LEFT);
    targetNamespace.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TARGET_NAMESPACE")); //$NON-NLS-1$
    GridData nsData = new GridData();
    nsData.horizontalAlignment = GridData.FILL;
    targetNamespace.setLayoutData(nsData);
    
    targetNamespaceText = new Text(wsdlGroup, SWT.SINGLE | SWT.BORDER);
    GridData textData = new GridData();
    textData.horizontalAlignment = GridData.FILL;
    textData.grabExcessHorizontalSpace = true;
    textData.widthHint = 10;
    targetNamespaceText.setLayoutData(textData);
    
    targetNamespaceText.addModifyListener(this);

    Label prefix = new Label(wsdlGroup, SWT.LEFT);
    prefix.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_PREFIX_WITH_COLON")); //$NON-NLS-1$\

    GridData prefixData = new GridData();
    prefixData.horizontalAlignment = GridData.FILL;
    prefix.setLayoutData(prefixData);
    
    prefixText = new Text(wsdlGroup, SWT.SINGLE | SWT.BORDER);
    GridData prefixTextData = new GridData();
    prefixTextData.horizontalAlignment = GridData.FILL;
    prefixTextData.grabExcessHorizontalSpace = true;
    prefixTextData.widthHint = 10;
    prefixText.setLayoutData(prefixTextData);

    prefixText.addModifyListener(this);
    
    //    XMLCatalog xmlCatalog = XMLCatalogRegistry.getInstance().lookupOrCreateXMLCatalog("default");
    //    SelectXMLCatalogIdPanel catalog = new SelectXMLCatalogIdPanel(base, xmlCatalog);

//  Determine if the user wishes to create a skeleton WSDL.  If yes, present the user with input fields.
    createSkeletonCheckBox = new Button(base, SWT.CHECK);
    createSkeletonCheckBox.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_CREATE_WSDL_SKELETON"));
    createSkeletonCheckBox.setSelection(true);
    
    wsdlSkeletonGroup = new Composite(base, SWT.NONE);
    GridLayout layout2 = new GridLayout();
    layout2.numColumns = 2;
    wsdlSkeletonGroup.setLayout(layout2);

    GridData data2 = new GridData();
    data2.verticalAlignment = GridData.FILL;
    data2.horizontalAlignment = GridData.FILL;
    wsdlSkeletonGroup.setLayoutData(data2);

   createLabel(wsdlSkeletonGroup, WSDLEditorPlugin.getWSDLString("_UI_LABEL_BINDING_PROTOCOL"));
   protocolCombo = new Combo(wsdlSkeletonGroup, SWT.READ_ONLY);
   GridData dataC = new GridData();
   dataC.horizontalAlignment = GridData.FILL;
   dataC.grabExcessHorizontalSpace = true;
   protocolCombo.setLayoutData(dataC);
   protocolCombo.add(SOAP_PROTOCOL);
   protocolCombo.add(HTTP_PROTOCOL);
   protocolCombo.select(0);
   protocolCombo.addModifyListener(this);
   
   sepLabel = new Label(base, SWT.SEPARATOR | SWT.HORIZONTAL);
   GridData sepData = new GridData();
   sepData.horizontalAlignment = GridData.FILL;
   sepData.grabExcessHorizontalSpace = true;
   sepLabel.setLayoutData(sepData);
   
   
   // Create PageBook and pages/controls for the PageBook
   protocolPageBook = new PageBook(base, SWT.NONE);

   ///////////////////////// Soap Page
   soapPage = new Composite(protocolPageBook, SWT.NONE);
   GridLayout pbLayout = new GridLayout();
   soapPage.setLayout(pbLayout);

   GridData pbData = new GridData();
   pbData.verticalAlignment = GridData.FILL;
   pbData.horizontalAlignment = GridData.FILL;
   soapPage.setLayoutData(pbData);

   createLabel(soapPage, WSDLEditorPlugin.getWSDLString("_UI_LABEL_SOAP_BINDING_OPTIONS"));
   docLitRadio = new Button(soapPage, SWT.RADIO);
   rpcLitRadio = new Button(soapPage, SWT.RADIO);
   rpcEncRadio = new Button(soapPage, SWT.RADIO);
   docLitRadio.setText(SOAP_DOCUMENT_LITERAL);
   rpcLitRadio.setText(SOAP_RPC_LITERAL);
   rpcEncRadio.setText(SOAP_RPC_ENCODED);
   docLitRadio.setSelection(true);
   
   ///////////////////////// Http Page
   httpPage = new Composite(protocolPageBook, SWT.NONE);
   pbLayout = new GridLayout();
   httpPage.setLayout(pbLayout);

   pbData = new GridData();
   pbData.verticalAlignment = GridData.FILL;
   pbData.horizontalAlignment = GridData.FILL;
   httpPage.setLayoutData(pbData);
   
   createLabel(httpPage, WSDLEditorPlugin.getWSDLString("_UI_LABEL_HTTP_BINDING_OPTIONS")); // Already Externalized
   httpGetRadio = new Button(httpPage, SWT.RADIO);
   httpPostRadio = new Button(httpPage, SWT.RADIO);
   httpGetRadio.setText(HTTP_GET);
   httpPostRadio.setText(HTTP_POST);
   httpGetRadio.setSelection(true);
   
   wsdlSkeletonGroup.setVisible(true);
   sepLabel.setVisible(true);
   protocolPageBook.showPage(soapPage);
   protocolPageBook.setVisible(true);
   
   ////////////////////////// Add Selection Listeners
   createSkeletonCheckBox.addSelectionListener(this);
   
//   BindingProtocolComponentControl component = new BindingProtocolComponentControl(base, generator, false);
//   component.initFields();
   
    setControl(base);
  }

  private NewWSDLWizard getNewWSDLWizard()
  {
    return (NewWSDLWizard)getWizard();
  }

  private String computeDefaultDefinitionName()
  {
    String name = "DefaultName";
    IPath path = getNewWSDLWizard().getNewFilePath();
    if (path != null)
    {
      name = path.removeFileExtension().lastSegment().toString();
    }
    return name;
  }

  private String computeDefaultNamespaceName()
  {
    String namespace = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"));
    if (!namespace.endsWith("/")) {
    	namespace = namespace.concat("/");
    }
    
    IPath path = getNewWSDLWizard().getNewFilePath();
    if (path != null)
    {
      namespace += path.removeFileExtension().toString() + "/";
    }
    return namespace;
  }

  public void setVisible(boolean visible)
  {
    super.setVisible(visible);

    if (visible)
    {
      // prime the fields    
      targetNamespaceText.setText(computeDefaultNamespaceName());
      prefixText.setText("tns");
    }
  }

  public String getTargetNamespace()
  {
    return targetNamespaceText.getText();
  }

  public String getPrefix()
  {
    return prefixText.getText().trim();
  }

  public String getDefinitionName()
  {
  	return computeDefaultDefinitionName();
  }

  public void modifyText(ModifyEvent e)
  {
    if (e.widget == targetNamespaceText)
    {
      startDelayedEvent(e);
    }
    else if (e.widget == prefixText)
    {
      setPageComplete(isPageComplete());
    }
	else if (e.widget == protocolCombo) {
		// Update what page/control we show in the PageBook
		if (protocolCombo.getText().equals("SOAP")) {
			protocolPageBook.showPage(soapPage);
		}
		else if (protocolCombo.getText().equals("HTTP")) {
			protocolPageBook.showPage(httpPage);
		}
	}
  }

  public void updateOccured(Object arg1, Object arg2)
  {
    setPageComplete(isPageComplete());
  }

  public boolean isPageComplete()
  {
    boolean ready = true;

    setErrorMessage(null);

    // so that the page doesn't immediately show up with an error
    if (targetNamespaceText.getText().trim().equals(""))
    {
      if (ready)
      {
        setErrorMessage(null);
      }
      return false;
    }

    if (!validateTargetNamespace(targetNamespaceText.getText()))
    {
      ready = false;
    }
    
    return ready;
  }

  protected boolean validatePrefix(String prefix)
  {
    String errorMessage = ValidateHelper.checkXMLPrefix(prefix);

    if (errorMessage == null || errorMessage.length() == 0)
    {
      return true;
    }
    return false;
  }

  protected boolean validateXMLName(String xmlName)
  {
    String errorMessage = ValidateHelper.checkXMLName(xmlName);

    if (errorMessage == null || errorMessage.length() == 0)
    {
      return true;
    }
    setErrorMessage(errorMessage);
    return false;
  }

  protected boolean validateTargetNamespace(String ns)
  {
    boolean test = true;
    try
    {
      URI testURI = new URI(ns);
      //      if (!testURI.isGenericURI())
      //      {
      //        setErrorMessage(WSDLEditorPlugin.getInstance().getWSDLString("_UI_ERROR_NAMESPACE_INVALID")); //$NON-NLS-1$
      //        test = false;
      //      }
    }
    catch (URISyntaxException e)
    {
      //			String errorMessage = ValidateHelper.checkXMLName(ns);
      //			if (errorMessage == null || errorMessage.length() == 0)
      //			{
      //				test = true;
      //			}
      //			else
      //			{
      setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_NAMESPACE_INVALID")); //$NON-NLS-1$
      test = false;
      //			}
    }

    return test;
  }

  /*
  private boolean arePrefixesUniqueAndValid()
  {
    java.util.List infoList = namespaceInfo.getNamespaceInfoList();
    java.util.List checkedList = namespaceInfo.getNamespaceCheckedList();

    Vector prefixList = new Vector();
    boolean test = true;
    boolean isOneBlank = false;

    String currentPrefix = prefixText.getText().trim();
    if (currentPrefix.length() == 0)
    {
      isOneBlank = true;
    }
    else
    {
      if (validatePrefix(currentPrefix))
      {
        prefixList.add(currentPrefix);
      }
      else
      {
        setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_PREFIX_IS_INVALID", currentPrefix)); //$NON-NLS-1$
        return false;
      }
    }

    for (int i = 0; i < infoList.size(); i++)
    {
      NamespaceInfo info = (NamespaceInfo)infoList.get(i);
      if (((String)checkedList.get(i)).equals("true")) //$NON-NLS-1$
      {
        String aPrefix = info.prefix.trim();
        if (aPrefix.length() > 0)
        {
          if (!prefixList.contains(aPrefix))
          {
            if (validatePrefix(aPrefix))
            {
              prefixList.add(aPrefix);
            }
            else
            {
              setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_PREFIX_IS_INVALID", info.prefix)); //$NON-NLS-1$
              test = false;
              break;
            }
          }
          else
          {
            setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_PREFIX_IS_A_DUPLICATE", info.prefix)); //$NON-NLS-1$
            test = false;
            break;
          }
        }
        else
        {
          if (!isOneBlank)
          {
            isOneBlank = true;
          }
          else
          {
            setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_MORE_THAN_ONE_PREFIX_IS_BLANK")); //$NON-NLS-1$
            test = false;
            break;
          }
        }
      }
    }
    return test;
  }
  */

  protected DelayedEvent delayedTask;
  protected void startDelayedEvent(ModifyEvent e)
  {
    if (delayedTask == null || delayedTask.getEvent() == null)
    {
      delayedTask = new DelayedEvent();
      delayedTask.setEvent(e);
      Display.getDefault().timerExec(500, delayedTask);
    }
    else
    {
      ModifyEvent delayedEvent = delayedTask.getEvent();

      if (e.widget == delayedEvent.widget)
      {
        // same event, just different data, delay new event
        delayedTask.setEvent(null);
      }
      delayedTask = new DelayedEvent();
      delayedTask.setEvent(e);
      Display.getDefault().timerExec(500, delayedTask);
    }
  }

  class DelayedEvent implements Runnable
  {
    protected ModifyEvent event;

    /*
     * @see Runnable#run()
     */
    public void run()
    {
      if (event != null)
      {
        setPageComplete(isPageComplete());
        event = null;
      }
    }

    public ModifyEvent getEvent()
    {
      return event;
    }

    public void setEvent(ModifyEvent event)
    {
      this.event = event;
    }
  }

  public void widgetDefaultSelected(SelectionEvent e) {
  }

  public void widgetSelected(SelectionEvent e) {
  	 if (e.widget == createSkeletonCheckBox) {
  	 	if (createSkeletonCheckBox.getSelection()) {
  	 		wsdlSkeletonGroup.setVisible(true);
  	 		sepLabel.setVisible(true);
  	 		protocolPageBook.setVisible(true);
  	 	}
  	 	else {
  	 		wsdlSkeletonGroup.setVisible(false);
  	 		sepLabel.setVisible(false);
  	 		protocolPageBook.setVisible(false);
  	 	}
  	 }
  }
  
  private static final String SOAP_PROTOCOL = "SOAP";
  private static final String HTTP_PROTOCOL = "HTTP";
  private static final String SOAP_RPC_ENCODED = WSDLEditorPlugin.getWSDLString("_UI_RADIO_RPC_ENCODED");
  private static final String SOAP_RPC_LITERAL = WSDLEditorPlugin.getWSDLString("_UI_RADIO_RPC_LITERAL");
  private static final String SOAP_DOCUMENT_LITERAL = WSDLEditorPlugin.getWSDLString("_UI_RADIO_DOCUMENT_LITERAL");
  private static final String HTTP_POST = "HTTP POST";
  private static final String HTTP_GET = "HTTP GET";

  Composite wsdlSkeletonGroup;
  Button createSkeletonCheckBox;
  
  Label sepLabel;
  
  private Text createTextField(Composite comp) {
    Text textField = new Text(comp, SWT.SINGLE | SWT.BORDER);
    GridData textData = new GridData();
    textData.horizontalAlignment = GridData.FILL;
    textData.grabExcessHorizontalSpace = true;
    textData.widthHint = 10;
    textField.setLayoutData(textData);

  	return textField;
  }
  
  private Label createLabel(Composite comp, String labelString) {
    Label label = new Label(comp, SWT.LEFT);
    label.setText(labelString);
    GridData nsData = new GridData();
    nsData.horizontalAlignment = GridData.FILL;
    label.setLayoutData(nsData);
    
    return label;
  }
  
  private Composite createComposite(Composite parent, int numColumns)
  {
    Composite composite = new Composite(parent, SWT.NONE);

    composite.setFont(new Font(Display.getCurrent(), "ms sans serif", 8, SWT.NORMAL));

    GridLayout layout = new GridLayout();
    layout.numColumns = numColumns;
    composite.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    composite.setLayoutData(data);
    return composite;
  }
  
  public Vector getNamespaceInfo() {
		Vector namespaces = new Vector();
  		
  		// Add Default Namespaces
		NamespaceInfo info1 = new NamespaceInfo();
		info1.prefix = "wsdl";
		info1.uri = "http://schemas.xmlsoap.org/wsdl/";
		namespaces.addElement(info1);
		
		NamespaceInfo info8 = new NamespaceInfo();
		info8.prefix = "xsd";
		info8.uri = "http://www.w3.org/2001/XMLSchema";
		namespaces.addElement(info8);
		
		return namespaces;
  }
  
  public boolean getCreateSkeletonBoolean() {
  	return createSkeletonCheckBox.getSelection();
  }
  
  public String getProtocol() {
  	return protocolCombo.getText();
  }
  
  public Object[] getProtocolOptions() {
  	Object bool[] = new Boolean[3];
  	if (protocolCombo.getText().equals(SOAP_PROTOCOL)) {
  		if (docLitRadio.getSelection()) {
  			bool[0] = new Boolean(true);
  		}
  		else {
  			bool[0] = new Boolean(false);
  		}
  		
  		if (rpcLitRadio.getSelection()) {
  			bool[2] = new Boolean(true);
  		}
  		else {
  			bool[2] = new Boolean(false);
  		}
  	}
  	else if (protocolCombo.getText().equals(HTTP_PROTOCOL)){
  		if (httpGetRadio.getSelection()) {
  			bool[0] = new Boolean(false);
  		}
  		else {
  			bool[0] = new Boolean(true);
  		}
  	}
  	
  	return bool;
  }
  
  public boolean isSoapDocLiteralProtocol() {
  	if (getProtocol().equals(SOAP_PROTOCOL) && docLitRadio.getSelection()) {
  		return true;
  	}
  	else {
  		return false;
  	}
  }
}
