/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.wizard.test;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jst.j2ee.internal.webservices.WebServicesClientDataHelper;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientGenerator;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.env.core.common.MessageUtils;

/*
 * Add the stuff below to the properties file to make this work. 
#
#Test only - do not commit
#
wsdlURI=wsdlURI
clientProject=clientProject
serviceQName=serviceQName
outputWSDLpath=outputWSDLpath
GO=GO
pop1=pop1
pop2=pop2
*/
public class TestWizardPage extends WizardPage
{
  private String pluginId_= "org.eclipse.jst.ws.axis.consumption.ui";
  private MessageUtils msgUtils_;
  
  private Text wsdlURIText_;
  private Text clientProjectText_;
  private Text serviceQNameText_;
  private Text outputWSDLPathText_;
  private Button goButton_;
  private Button pop1Button_;
  private Button pop2Button_;
  
  public TestWizardPage(String name)
  {
    super(name);
    setTitle("Test Page");
    setDescription("Test page description");
  }
  
  public void createControl(Composite parent)
  {
    Composite page = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    page.setLayout(gl);
    GridData gd = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
    page.setLayoutData(gd);
    page.setSize(500,500);

   
	//Composite outerGroup_ = new Composite(page,SWT.H_SCROLL | SWT.V_SCROLL);
	Composite outerGroup_ = new Composite(page,SWT.NONE);
	GridLayout gl2 = new GridLayout();
	gl2.numColumns = 2;
	gl2.marginHeight = 0;
	gl2.marginWidth = 0;
	outerGroup_.setLayout(gl2);
	//GridData gd2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	GridData gd2 = new GridData(GridData.FILL_BOTH);
	outerGroup_.setLayoutData(gd2);
	
	msgUtils_ = new MessageUtils(pluginId_ + ".plugin", this);
    UIUtils uiUtils = new UIUtils(msgUtils_, pluginId_);
    
    wsdlURIText_ = uiUtils.createText(outerGroup_,"wsdlURI","wsdlURI","wsdlURI",SWT.SINGLE|SWT.BORDER);
    clientProjectText_ = uiUtils.createText(outerGroup_,"clientProject","clientProject","clientProject",SWT.SINGLE|SWT.BORDER);
    serviceQNameText_ = uiUtils.createText(outerGroup_,"serviceQName","serviceQName","",SWT.SINGLE|SWT.BORDER);
    outputWSDLPathText_ = uiUtils.createText(outerGroup_,"outputWSDLpath","outputWSDLpath","",SWT.SINGLE|SWT.BORDER);
    goButton_ = uiUtils.createButton(SWT.PUSH,outerGroup_,"GO","GO","");
    goButton_.addSelectionListener(new SelectionListener()
        {
          public void widgetSelected(SelectionEvent e)
          {
            handleGOPushed();
          }
          public void widgetDefaultSelected(SelectionEvent e)
          {
            handleGOPushed();
        }
        });
    pop1Button_ = uiUtils.createButton(SWT.PUSH,outerGroup_,"pop1","pop1","");
    pop1Button_.addSelectionListener(new SelectionListener()
        {
          public void widgetSelected(SelectionEvent e)
          {
            handlePop1Pushed();
          }
          public void widgetDefaultSelected(SelectionEvent e)
          {
            handlePop1Pushed();
        }
        });    
    pop2Button_ = uiUtils.createButton(SWT.PUSH,outerGroup_,"pop2","pop2","");
    pop2Button_.addSelectionListener(new SelectionListener()
        {
          public void widgetSelected(SelectionEvent e)
          {
            handlePop2Pushed();
          }
          public void widgetDefaultSelected(SelectionEvent e)
          {
            handlePop2Pushed();
        }
        });    
 
    setControl(page);
  }
  
  private void handlePop1Pushed()
  {
    wsdlURIText_.setText("http://www.xmethods.net/sd/2001/TemperatureService.wsdl");
    clientProjectText_.setText("cwp");
    serviceQNameText_.setText("http://www.xmethods.net/sd/TemperatureService.wsdl:TemperatureService");
    outputWSDLPathText_.setText("/cwp/Web Content/WEB-INF/wsdl/TemperatureService.wsdl");
  }
  
  private void handlePop2Pushed()
  {
    wsdlURIText_.setText("file:/D:/Eclipse/eclipse/runtime-workspace_t1/wp2/Web Content/Converters.wsdl");
    clientProjectText_.setText("cwp2");
    serviceQNameText_.setText("http://sample.converters:ConvertDistanceService");
    outputWSDLPathText_.setText("/cwp2/Web Content/WEB-INF/wsdl/Converters.wsdl");
  }

  private void handleGOPushed()
  {
    AxisClientGenerator generator = new AxisClientGenerator();
    TestWSCDataHelper dataModel = new TestWSCDataHelper();
    dataModel.populate(wsdlURIText_.getText(), serviceQNameText_.getText(), clientProjectText_.getText(), outputWSDLPathText_.getText(),false);
    
    generator.genWebServiceClientArtifacts(dataModel);
    System.out.println("Generated service interface= "+dataModel.getServiceInterfaceName());
    String[] seis = dataModel.getServiceEndpointInterfaceNames();
    System.out.println("Generated service endpoint interfaces:");
    for (int i=0;i<seis.length;i++)
    {
    	System.out.println(seis[i]);
    }
    System.out.println("Touched descriptors? "+dataModel.shouldGenDescriptors());
  }
  
  private class TestWSCDataHelper implements WebServicesClientDataHelper
  {
  	private String wsdlUrl_;
  	private String serviceQName_;
  	private String projectName_;
  	private String outputWSDLFileName_;
  	private String serviceInterfaceName_;
  	private String[] serviceEndpointInterfaceNames_;
  	private boolean shouldDeploy_;
  	private boolean shouldGenDescriptors_;
  	
  	public void populate(String wsdlURL, String serviceQName, String projectName, String outputWSDLFileName, boolean shouldDeploy)
  	{
  		wsdlUrl_ = wsdlURL;
  		serviceQName_ = serviceQName;
  		projectName_ = projectName;
  		outputWSDLFileName_ = outputWSDLFileName;
  		shouldDeploy_ = shouldDeploy;
  	}
	/**
	 * @return the URL for the associated original WSDL file referenced by the client
	 */
	public String getWSDLUrl()
	{
		return wsdlUrl_;
	}
	
	/**
	 * @return the QName for the associated web service for the client
	 */
	public String getServiceQName()
	{
		return serviceQName_;
	}
	
	/**
	 * @return the name of the target project for the web service client
	 */
	public String getProjectName()
	{
		return projectName_;
	}
	
	/**
	 * @return the fileName location for the copied WSDL file into the client project
	 */
	public String getOutputWSDLFileName()
	{
		return outputWSDLFileName_;
	}
	
	/**
	 * @return the qualified classname of the generated Service Interface
	 */
	public String getServiceInterfaceName()
	{
		return serviceInterfaceName_;
	}
	
	/**
	 * @return the qualified classname of the generated Service Endpoint Interface
	 */
	public String[] getServiceEndpointInterfaceNames()
	{
		return serviceEndpointInterfaceNames_;
	}
	
	/**
	 * @return should generate code for deployment?
	 */
	public boolean shouldDeploy()
	{
		return shouldDeploy_;
	}
	
	/**
	 * @return whether the extension generated the descriptors or not
	 */
	public boolean shouldGenDescriptors()
	{
		return shouldGenDescriptors_;
	}
	
	/**
	 * Set the SI classname for the web service client
	 */
	public void setServiceInterfaceName(String name)
	{
		serviceInterfaceName_ = name;
	}

	/**
	 * Set the SEI classnames for the web service client
	 */
	public void setServiceEndpointInterfaceNames(String[] names)
	{
		serviceEndpointInterfaceNames_ = names;
	}

	/**
	 * Set whether descriptors were generated
	 */
	public void setDidGenDescriptors(boolean b)
	{
		shouldGenDescriptors_ = b;
	}
	
	/**
	 * This method will invoke any operations required to set up web services client, SI, and SEI
	 */
	public void genWebServiceClientArtifacts(WebServicesClientDataHelper dataModel)
	{
		// Do nothing. This shouldn't be on the interface!
	}
	
  }
}
