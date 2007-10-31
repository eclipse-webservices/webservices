/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070513   186430 sandakith@wso2.com - Lahiru Sandakith, fix for 186430
 *										  Text not accessible on AXIS2 wizard pages.
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 * 20070823   200413 sandakith@wso2.com - Lahiru Sandakith, Namespace to Package table fix
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20071030	  207618 zina@ca.ibm.com - Zina Mostafia, Page GUI sequence using tab is not correct ( violates Accessibility)
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.widgets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.data.DataModel;
import org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUIMessages;
import org.eclipse.jst.ws.axis2.consumption.core.utils.WSDLPropertyReader;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.utils.ClassLoadingUtil;
import org.eclipse.jst.ws.axis2.ui.plugin.WebServiceAxis2UIPlugin;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

public class Axis2ProxyWidget extends SimpleWidgetDataContributor {
	
	DataModel model;
	IStatus status = Status.OK_STATUS;


	public Axis2ProxyWidget(DataModel model) {
		this.model=model;
	}

	//private String pluginId_ = "org.eclipse.jst.ws.axis2.consumption.ui";
	
	private Button genProxyCheckbox_;
	private Button syncAndAsyncRadioButton;
	private Button syncOnlyRadioButton;
	private Button asyncOnlyRadioButton;
	//private Button testCaseCheckBoxButton;
	//private Button generateAllCheckBoxButton;
	private Combo databindingTypeCombo;
	private Combo portNameCombo;
	private Combo serviceNameCombo;
	private Text packageText;
	
	//private java.util.List serviceQNameList2 = null;
	private Table namespace2packageTable = null;
	private Button generateAllCheckBoxButton;
	private Button testCaseCheckBoxButton;
	
	private WSDLPropertyReader reader;
	private List serviceQNameList = null;
	Axis2EmitterContext context;


	public WidgetDataEvents addControls( Composite parent, Listener statusListener )
	{
		context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
		UIUtils uiUtils = new UIUtils(WebServiceAxis2UIPlugin.PLUGIN_ID);
		
		Composite  topComp = uiUtils.createComposite(parent, 2 );
	
		// service name
		serviceNameCombo = uiUtils.createCombo(topComp, Axis2ConsumptionUIMessages.LABEL_SERVICE_NAME_CAPTION, null, null, SWT.READ_ONLY);
		serviceNameCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//loadPortNames();
				model.setServiceName(serviceNameCombo.getText());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// port name
		portNameCombo = uiUtils.createCombo(topComp, Axis2ConsumptionUIMessages.LABEL_PORTNAME, null, null, SWT.READ_ONLY);
		portNameCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setPortName(portNameCombo.getText());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Databinding
		databindingTypeCombo = uiUtils.createCombo(topComp, Axis2ConsumptionUIMessages.LABEL_DATABINDING_CAPTION, null, null, SWT.READ_ONLY);
		fillDatabinderCombo();
		databindingTypeCombo.select(0);
		databindingTypeCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setDatabindingType(databindingTypeCombo.getText());
			};
			public void widgetDefaultSelected(SelectionEvent e) {
			};
		});
		
		// package name
		packageText = uiUtils.createText(topComp, Axis2ConsumptionUIMessages.LABEL_PACKEGE_NAME, null, null,SWT.BORDER);
		packageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				model.setPackageText(packageText.getText());
			}
		});
		
	  	//Client type label 
		Group clientTypeGroup = uiUtils.createGroup(parent, Axis2ConsumptionUIMessages.LABEL_CLIENT_SIDE, null, null);
				
		syncAndAsyncRadioButton = uiUtils.createRadioButton(clientTypeGroup, Axis2ConsumptionUIMessages.LABEL_SYNC_AND_ASYNC, null, null);
		syncAndAsyncRadioButton.setSelection(
				((context.isAsync() || context.isSync())==false)
				?true
				:(context.isAsync() && context.isSync()));
		syncAndAsyncRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//Because default setting in axis2 to be both false in thie case
				//File a JIRA to fix this.
				model.setSync(!syncAndAsyncRadioButton.getSelection());
				model.setASync(!syncAndAsyncRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		syncOnlyRadioButton = uiUtils.createRadioButton(clientTypeGroup, Axis2ConsumptionUIMessages.LABEL_SYNC, null, null);
		syncOnlyRadioButton.setSelection(context.isSync() && !context.isAsync());
		syncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setSync(syncOnlyRadioButton.getSelection());
				model.setASync(!syncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		asyncOnlyRadioButton = uiUtils.createRadioButton(clientTypeGroup, Axis2ConsumptionUIMessages.LABEL_ASYNC, null, null);
		asyncOnlyRadioButton.setSelection(context.isAsync() && !context.isSync());
		asyncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setASync(asyncOnlyRadioButton.getSelection());
				model.setSync(!asyncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		//Set the sync async to default 
		model.setSync(context.isSync());
		model.setASync(context.isAsync());
		
	   	// generate test case option
		
		Composite checkBoxes = uiUtils.createComposite(parent, 1);
		testCaseCheckBoxButton = uiUtils.createCheckbox(checkBoxes, Axis2ConsumptionUIMessages.LABEL_GENERATE_TESTCASE_CAPTION, null, null);
		testCaseCheckBoxButton.setSelection(context.isClientTestCase());
		model.setTestCaseCheck(context.isClientTestCase());
		testCaseCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setTestCaseCheck(testCaseCheckBoxButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});


		// generate all
		generateAllCheckBoxButton = uiUtils.createCheckbox(checkBoxes, Axis2ConsumptionUIMessages.LABEL_GENERATE_ALL, null, null);
		generateAllCheckBoxButton.setSelection(context.isClientGenerateAll());
		generateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setGenerateAllCheck(generateAllCheckBoxButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	   
	    new Label(parent,SWT.HORIZONTAL);
        namespace2packageTable = new Table(parent,SWT.BORDER|SWT.MULTI);
        namespace2packageTable.setLinesVisible(true);
        namespace2packageTable.setHeaderVisible(true); 
        namespace2packageTable.setEnabled(true);
              
        declareColumn(namespace2packageTable,
        		350, //a default width until we adjust
        		Axis2ConsumptionUIMessages.LABEL_NAMESPACE);
        declareColumn(namespace2packageTable,
        		200,//a default width until we adjust
        		Axis2ConsumptionUIMessages.LABEL_PACKAGE);
        
        namespace2packageTable.setVisible(true);
        
        // add the table editor
        final TableEditor editor = new TableEditor(namespace2packageTable);
        editor.setColumn(1);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        namespace2packageTable.addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
              Rectangle clientArea = namespace2packageTable.getClientArea();
              Point pt = new Point(event.x, event.y);
              int index = namespace2packageTable.getTopIndex();
              while (index < namespace2packageTable.getItemCount()) {
                boolean visible = false;
                final TableItem item = namespace2packageTable.getItem(index);
                for (int i = 0; i < namespace2packageTable.getColumnCount(); i++) {
                  Rectangle rect = item.getBounds(i);
                  if (rect.contains(pt)) {
                    final int column = i;
                    final Text text = new Text(namespace2packageTable, SWT.NONE);
                    Listener textListener = new Listener() {
                      public void handleEvent(final Event e) {
                        switch (e.type) {
                        case SWT.FocusOut:
                          item.setText(column, text.getText());
                          text.dispose();
                          break;
                        case SWT.Traverse:
                          switch (e.detail) {
                          case SWT.TRAVERSE_RETURN:
                            item
                                .setText(column, text
                                    .getText());
                          // FALL THROUGH
                          case SWT.TRAVERSE_ESCAPE:
                            text.dispose();
                            e.doit = false;
                          }
                          break;
                        }
                      }
                    };
                    text.addListener(SWT.FocusOut, textListener);
                    text.addListener(SWT.Traverse, textListener);
                    editor.setEditor(text, item, i);
                    text.setText(item.getText(i));
                    text.selectAll();
                    text.setFocus();
                    return;
                  }
                  if (!visible && rect.intersects(clientArea)) {
                    visible = true;
                  }
                }
                if (!visible)
                  return;
                index++;
              }
              model.setNamespaseToPackageMapping(getNs2PkgMapping());
            }
          });
        
        namespace2packageTable.addListener(SWT.MouseExit, new Listener() {
            public void handleEvent(Event event) {
            	 model.setNamespaseToPackageMapping(getNs2PkgMapping());
            }
        });
        

//		UIUtils      uiUtils  = new UIUtils( pluginId_ );
//		parent.setToolTipText( Axis2ConsumptionUIMessages.TOOLTIP_PPAE_PAGE );
//		PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." +  this.pluginId_);
		
		populateParamsFromWSDL();
		populateModel();

		return this;
	}
	
	/**
	 * A util method to create a new column
	 * @param table
	 * @param width
	 * @param colName
	 */
	private void declareColumn(Table table, int width,String colName){
        TableColumn column = new TableColumn(table,SWT.NONE);
        column.setWidth(width);
        column.setText(colName);
    }
	
	/**
	 * get the package to namespace mappings
	 * @return
	 */
	public String getNs2PkgMapping(){
		String returnList="";
		TableItem[] items = namespace2packageTable.getItems();
		String packageValue; 
		for (int i=0;i<items.length;i++){
			packageValue = items[i].getText(1);
			if (packageValue!=null && !"".equals(packageValue)){
				returnList = returnList +
				             ("".equals(returnList)?"":",") +
				             items[i].getText(0)+ "=" + packageValue;
			}
			
		}
		return "".equals(returnList)?null:returnList;
	}

	public boolean isGenProxy()	{
		//boolean enabled = genProxyCheckbox_.getSelection();
		return true; //always

	}

	public void setGenerateProxy( Boolean genProxy ){
		genProxyCheckbox_.setSelection( genProxy.booleanValue() );
	}   
	
	/**
	 * Fill the combo with proper databinding names
	 */
	private void fillDatabinderCombo() {
		databindingTypeCombo.add(Axis2Constants.DATA_BINDING_ADB);
		databindingTypeCombo.add(Axis2Constants.DATA_BINDING_NONE);
		databindingTypeCombo.select(0);
	}
	
	private void populateModel() {
		model.setServiceName(serviceNameCombo.getText());
		model.setPortName(portNameCombo.getText());
		model.setPackageText(packageText.getText());
		model.setDatabindingType(databindingTypeCombo.getText());
		//model.setASync(asyncOnlyRadioButton.getSelection());
		//model.setSync(syncOnlyRadioButton.getSelection());
		if (syncAndAsyncRadioButton.getSelection()){
			model.setASync(!syncAndAsyncRadioButton.getSelection());
			model.setSync(!syncAndAsyncRadioButton.getSelection());
		}
		model.setGenerateAllCheck(generateAllCheckBoxButton.getSelection());
		model.setTestCaseCheck(testCaseCheckBoxButton.getSelection());
		model.setNamespaseToPackageMapping(getNs2PkgMapping());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
	 */
	public IStatus getStatus() 
	{
		IStatus result = null;
		return result;
	}

	private void populatePackageName() {
		this.packageText.setText(reader.packageFromTargetNamespace());
	}

	/**
	 * populate the service and the port from the WSDL this needs to be public
	 * since the WSDLselection page may call this
	 */
	public void populateParamsFromWSDL() {
		if (reader == null)
			reader = new WSDLPropertyReader();
		try {
			String lname = model.getWsdlURI();
			if (!"".equals(lname.trim())) {

				reader.readWSDL(model.getWebProjectName(), lname);

				this.serviceQNameList = reader.getServiceList();
				if (!serviceQNameList.isEmpty()) {
					serviceNameCombo.removeAll();
					for (int i = 0; i < serviceQNameList.size(); i++) {
						// add the local part of the
						Object serviceQnameInstance = serviceQNameList.get(0);
						Class QNameClass = ClassLoadingUtil
								.loadClassFromAntClassLoader("javax.xml.namespace.QName");
						Method GetLocalPartMethod  = QNameClass.getMethod("getLocalPart", null);
						Object resultLocalPart = GetLocalPartMethod
										.invoke(serviceQnameInstance, null);
						serviceNameCombo.add(resultLocalPart.toString());
					}
					;
					// select the first one as the default
					serviceNameCombo.select(0);

					// load the ports
					loadPortNames();

				} else {
					// service name list being empty means we are switching to
					// the interface mode
					if (serviceNameCombo!=null) serviceNameCombo.removeAll();
					if (portNameCombo!=null) portNameCombo.removeAll();

				}

				populatePackageName();

				//populate the namespacess
				loadNamespaces(reader.getDefinitionNamespaceMap());
			}
		} catch (Exception e) {
			status = StatusUtils.errorStatus(NLS.bind(
					Axis2ConsumptionUIMessages.ERROR_INVALID_WSDL_FILE_READ_WRITEL,
					new String[]{e.getLocalizedMessage()}), e);
		}
	}
	
	/**
	 * Loads the namespaces
	 * @param namespaceMap
	 */
	private void loadNamespaces(Map namespaceMap){
		Iterator namespaces = namespaceMap.values().iterator();
		namespace2packageTable.removeAll();
        TableItem[] items = new TableItem[namespaceMap.size()]; // An item for each field
        
        int i = 0;
        while(namespaces.hasNext()){
        	
           items[i] = new TableItem(namespace2packageTable, SWT.NULL);
           String namespace = (String)namespaces.next();
           items[i].setText(0,namespace);
           items[i].setText(1,getPackageFromNamespace(namespace));
           i++;
        }

        namespace2packageTable.setVisible(true);
		
	}

	private void loadPortNames() {
		int selectionIndex = serviceNameCombo.getSelectionIndex();
		if (selectionIndex != -1) {
			java.util.List ports = reader.getPortNameList( serviceQNameList
					.get(selectionIndex));
			if (!ports.isEmpty()) {
				portNameCombo.removeAll();
				for (int i = 0; i < ports.size(); i++) {
					// add the local part of the
					portNameCombo.add(ports.get(i).toString());
				}
				portNameCombo.select(0);
			} else {
				// error no ports found
			}
		}
	}
	
	/**
	 * get the package derived by  Namespace
	 */
	public String getPackageFromNamespace(String namespace){
		// Use reflection to invoke org.apache.axis2.util.URLProcessor 
		// makePackageName(namespace) statically
		Object stringReturn = null;
		try {
			Class URLProcessorClass = ClassLoadingUtil
					.loadClassFromAntClassLoader("org.apache.axis2.util.URLProcessor");
			Method makePackageNameMethod = URLProcessorClass
					.getMethod("makePackageName", new Class[]{String.class});
			stringReturn = makePackageNameMethod.invoke(null, new Object[]{namespace});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return  (String)stringReturn;

	}

}
