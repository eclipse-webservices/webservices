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

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.binding.soap.internal.generator.SOAPContentGenerator;
import org.eclipse.wst.wsdl.internal.generator.BaseGenerator;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;


public class SoapBindingOptionsPage implements ContentGeneratorOptionsPage, SelectionListener
{
  protected Button docLiteral;
  protected Button rpcLiteral;
  protected Button rpcEncoded;
  protected Composite control;
  protected BaseGenerator generator;

  public SoapBindingOptionsPage()
  {
  }

  public void init(BaseGenerator generator)
  {
    this.generator = generator;
  }

  public Composite createControl(Composite parent)
  {
    control = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    control.setLayout(layout);

    Label separator = new Label(control, SWT.SEPARATOR | SWT.HORIZONTAL);
    GridData gd= new GridData();
    gd.horizontalAlignment= GridData.FILL;
    gd.grabExcessHorizontalSpace= true;
    separator.setLayoutData(gd);

    Label optionsHeading = new Label(control, SWT.NONE);
    optionsHeading.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_SOAP_BINDING_OPTIONS"));

    docLiteral = new Button(control, SWT.RADIO);
    docLiteral.setText(WSDLEditorPlugin.getWSDLString("_UI_RADIO_DOCUMENT_LITERAL"));
    docLiteral.setSelection(true);

    rpcLiteral = new Button(control, SWT.RADIO);
    rpcLiteral.setText(WSDLEditorPlugin.getWSDLString("_UI_RADIO_RPC_LITERAL"));
    
    rpcEncoded = new Button(control, SWT.RADIO);
    rpcEncoded.setText(WSDLEditorPlugin.getWSDLString("_UI_RADIO_RPC_ENCODED"));

    if (generator.getName() != null)
    {
      Definition definition = generator.getDefinition();
      QName qname = new QName(definition.getTargetNamespace(), generator.getName());
      Binding binding = (Binding) definition.getBinding(qname);

      if (binding != null)
      {
        List eeList = binding.getEExtensibilityElements();
        if (eeList.size() > 0)
        {
          ExtensibilityElement ee = (ExtensibilityElement) eeList.get(0);

          Element element = WSDLEditorUtil.getInstance().getElementForObject(ee);
          String style = element.getAttribute("style");
          
          if ("rpc".equals(style))
          {
            // Try to determine if it's RPC Literal or RPC Encoded
          	String use = "encoded";
          	List operations = binding.getEBindingOperations();
          	if (operations.size() > 0) {
          		element = null;
          		BindingOperation operation = (BindingOperation) operations.get(0);
          		if (operation.getEBindingInput() != null && operation.getEBindingInput().getEExtensibilityElements().size() > 0) {
          			Object object = operation.getEBindingInput().getEExtensibilityElements().get(0);
          			element = WSDLEditorUtil.getInstance().getElementForObject(object);
          		}
          		else if (operation.getEBindingOutput() != null && operation.getEBindingOutput().getEExtensibilityElements().size() > 0) {
          			Object object = operation.getEBindingOutput().getEExtensibilityElements().get(0);
          			element = WSDLEditorUtil.getInstance().getElementForObject(object);
          		}
          		else if (operation.getEBindingFaults().size() > 0) {
          			BindingFault fault = (BindingFault) operation.getEBindingFaults().get(0);
          			List faultEE = fault.getExtensibilityElements();
          			
          			if (faultEE.size() > 0) {
          				element = WSDLEditorUtil.getInstance().getElementForObject(faultEE.get(0));
          			}
          		}
          		
          		if (element != null) {
          			use = element.getAttribute("use");
          		}
          	}
          	
          	if (use != null && "literal".equals(use)) {
          		docLiteral.setSelection(false);
          		rpcLiteral.setSelection(true);
          		rpcEncoded.setSelection(false);
          	}
          	else {          	
          		docLiteral.setSelection(false);
          		rpcLiteral.setSelection(false);
          		rpcEncoded.setSelection(true);
          	}
          }
        }
      }
    }

    docLiteral.addSelectionListener(this);
    rpcLiteral.addSelectionListener(this);
    rpcEncoded.addSelectionListener(this);
    computeOptions();

    return control;
  }

  public Composite getControl() {
	  return control;
  }
  
  public boolean isOverwriteApplicable()
  {
    return true;
  }

  public void widgetSelected(SelectionEvent event)
  {
    computeOptions();
  }

  protected void computeOptions()
  {
	  if (generator.getContentGenerator() instanceof SOAPContentGenerator) {
		  SOAPContentGenerator soapGenerator = (SOAPContentGenerator) generator.getContentGenerator();
		  if (docLiteral.getSelection()) {
			  soapGenerator.setStyle(SOAPContentGenerator.STYLE_DOCUMENT);
			  soapGenerator.setUse(SOAPContentGenerator.USE_LITERAL);
		  }	  
		  else if (rpcLiteral.getSelection()) {
			  soapGenerator.setStyle(SOAPContentGenerator.STYLE_RPC);
			  soapGenerator.setUse(SOAPContentGenerator.USE_LITERAL);
		  }
		  else if (rpcEncoded.getSelection()) {
			  soapGenerator.setStyle(SOAPContentGenerator.STYLE_RPC);
			  soapGenerator.setUse(SOAPContentGenerator.USE_ENCODED);
		  }
	  }
  }
  
  public void setOptionsOnGenerator() {
	  computeOptions();
  }

  public void widgetDefaultSelected(SelectionEvent event)
  {
  }
}
