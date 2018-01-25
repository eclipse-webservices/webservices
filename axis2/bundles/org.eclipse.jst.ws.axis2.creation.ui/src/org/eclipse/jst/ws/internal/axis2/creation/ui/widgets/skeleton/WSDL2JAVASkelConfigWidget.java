/*******************************************************************************
 * Copyright (c) 2007, 2010 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070425   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070518   187311 sandakith@wso2.com - Lahiru Sandakith, Fixing test resource addition
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 * 20070823   200413 sandakith@wso2.com - Lahiru Sandakith, Namespace to Package table fix
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20080319   207616 makandre@ca.ibm.com - Andrew Mak, Table in Axis2 Web Service Skeleton Java Bean Configuration Page not Accessible
 * 20080621   200069 samindaw@wso2.com - Saminda Wijeratne, saving the retrieved WSDL so no need to retrieve it again
 * 20090307   196954 samindaw@wso2.com - Saminda Wijeratne, Support XMLBeans data binding
 * 20091207   193996 samindaw@wso2.com - Saminda Wijeratne, selecting a specific service/portname
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.creation.ui.widgets.skeleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.WSDLPropertyReader;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.utils.ClassLoadingUtil;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class WSDL2JAVASkelConfigWidget extends SimpleWidgetDataContributor 
{
	private DataModel model;
	IStatus status = Status.OK_STATUS;

	//controls
	//check box for server side interface
	private Button generateServerSideInterfaceCheckBoxButton;
	private Button generateAllCheckBoxButton;
	private Combo databindingTypeCombo;
	// Text box to have the portname
	private Combo portNameCombo;
	//Text box to have the service name
	private Combo serviceNameCombo;
	private WSDLPropertyReader reader;
	private java.util.List serviceQNameList = null;
	private Table namespace2packageTable = null;
	//Label holding the full qualified package name for generated code
	private Text packageText;
	//Checkbox to enable the generation of test case classes 
	// for the generated implementation of the webservice.
	Label      label, fillLabel, fillLabel1, fillLabel2, fillLabel3, fillLabel4, fillLabel5, fillLabel6;
	Axis2EmitterContext context;

	public WSDL2JAVASkelConfigWidget( DataModel model )	{
		context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
		this.model = model;  
	}

	public WidgetDataEvents addControls( Composite parent, final Listener statusListener ){
		Composite  mainComp = new Composite( parent, SWT.NONE );
		GridLayout layout   = new GridLayout();
		mainComp.setLayout(layout);

		layout.numColumns = 3;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		mainComp.setLayoutData( gd );

		// service name
		label = new Label(mainComp, SWT.NULL);
		label.setText(Axis2CreationUIMessages.LABEL_SERVICE_NAME_CAPTION);

		serviceNameCombo = new Combo(mainComp, SWT.DROP_DOWN | SWT.BORDER
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		serviceNameCombo.setLayoutData(gd);
		serviceNameCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = serviceNameCombo.getSelectionIndex();
				if (selectionIndex != -1) {
					Object object = reader.getServiceList().get(selectionIndex);
					model.setServiceQName(object);
				}
				loadPortNames();
				populatePackageName();
				loadNamespaces(reader.getDefinitionNamespaceMap());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// port name
		label = new Label(mainComp, SWT.NULL);
		label.setText(Axis2CreationUIMessages.LABEL_PORTNAME);
		portNameCombo = new Combo(mainComp, SWT.DROP_DOWN | SWT.BORDER
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		portNameCombo.setLayoutData(gd);
		portNameCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setPortName(portNameCombo.getText());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Databinding
		label = new Label(mainComp, SWT.NULL);
		label.setText(Axis2CreationUIMessages.LABEL_DATABINDING_CAPTION);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		databindingTypeCombo = new Combo(mainComp, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		databindingTypeCombo.setLayoutData(gd);
		fillDatabinderCombo();
		databindingTypeCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setDatabindingType(databindingTypeCombo.getText());
			};
			public void widgetDefaultSelected(SelectionEvent e) {
			};
		});

		// package name
		label = new Label(mainComp, SWT.NULL);
		label.setText(Axis2CreationUIMessages.LABEL_PACKEGE_NAME);
		packageText = new Text(mainComp, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;

		packageText.setLayoutData(gd);
		packageText.setText(""); // get this text from the
		packageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				model.setPackageText(packageText.getText());
			}
		});

		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);

		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel2 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);

		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel3 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);

		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel4 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);

		model.setServerXMLCheck(true);

		//the server side interface option
		generateServerSideInterfaceCheckBoxButton = new Button(mainComp, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		generateServerSideInterfaceCheckBoxButton.setLayoutData(gd);
		generateServerSideInterfaceCheckBoxButton
							.setSelection(context.isServiceInterfaceSkeleton());
		generateServerSideInterfaceCheckBoxButton.setText(Axis2CreationUIMessages.
														  LABEL_GENERATE_SERVERSIDE_INTERFACE);
		model.setGenerateAllCheck(context.isServiceInterfaceSkeleton());
		generateServerSideInterfaceCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setGenerateServerSideInterface(
						generateServerSideInterfaceCheckBoxButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// generate all
		generateAllCheckBoxButton = new Button(mainComp, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		generateAllCheckBoxButton.setLayoutData(gd);
		generateAllCheckBoxButton.setSelection(context.isServiceGenerateAll());
		generateAllCheckBoxButton.setText(Axis2CreationUIMessages.LABEL_GENERATE_ALL);
		generateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setGenerateAllCheck(generateAllCheckBoxButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel5 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);

		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel6 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);
		
		//add a table to set namespace to package mapping
		gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gd.verticalSpan = 5;
        
        namespace2packageTable = new Table(mainComp,SWT.BORDER|SWT.FULL_SELECTION);
        namespace2packageTable.setLinesVisible(true);
        namespace2packageTable.setHeaderVisible(true); 
        namespace2packageTable.setEnabled(true);
        namespace2packageTable.setLayoutData(gd);
       
        declareColumn(namespace2packageTable,
        		350, //a default width until we adjust
        		Axis2CreationUIMessages.LABEL_NAMESPACE);
        declareColumn(namespace2packageTable,
        		200,//a default width until we adjust
        		Axis2CreationUIMessages.LABEL_PACKAGE);
        
        namespace2packageTable.setVisible(true);
        
        // add the table editor
        final TableEditor editor = new TableEditor(namespace2packageTable);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        namespace2packageTable.addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
              Rectangle clientArea = namespace2packageTable.getClientArea();
              Point pt = new Point(event.x, event.y);
              int index = namespace2packageTable.getTopIndex();
              while (index < namespace2packageTable.getItemCount()) {
                boolean visible = false;
                TableItem item = namespace2packageTable.getItem(index);
                Rectangle rect = item.getBounds(1);
                if (rect.contains(pt)) {
                	editCell(item, editor);
                	return;
                }
                if (!visible && rect.intersects(clientArea)) {
                    visible = true;
                }
                if (!visible)
                  return;
                index++;
              }
            }
          });
        
        namespace2packageTable.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (namespace2packageTable.getSelectionIndex() == -1)
					namespace2packageTable.setSelection(0);
			}
        });
        
        namespace2packageTable.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent e) {
        		if (e.keyCode != 32)
        			return;
				
        		TableItem[] item = namespace2packageTable.getSelection();
				if (item.length != 1)
					return;
				
				editCell(item[0], editor);
			}
        });
        
		populateParamsFromWSDL();
		populateModel();

		return this;
	}

	private void editCell(final TableItem item, TableEditor editor) {
		
		final Text textEdit = new Text(namespace2packageTable, SWT.NONE);		
		textEdit.setText(item.getText(1));						
		textEdit.selectAll();
		textEdit.setFocus();
		
		textEdit.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				item.setText(1, textEdit.getText());
				textEdit.dispose();
			}			
		});
		
		textEdit.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				switch (e.detail) {
					case SWT.TRAVERSE_RETURN:
						item.setText(1, textEdit.getText());
					case SWT.TRAVERSE_ESCAPE:
						textEdit.dispose();
						e.doit = false;
				}
			}			
		});
		
		textEdit.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 32) {
					item.setText(1, textEdit.getText());
					textEdit.dispose();
				}
			}			
		});
		
		textEdit.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				model.setNamespaseToPackageMapping(getNs2PkgMapping());				
			}			
		});
		
		editor.setEditor(textEdit, item, 1);
	}

	private void populateModel() {
		int selectionIndex = serviceNameCombo.getSelectionIndex();
		if (selectionIndex != -1) {
			Object object = reader.getServiceList().get(selectionIndex);
			model.setServiceQName(object);
		}
		model.setPortName(portNameCombo.getText());
		model.setPackageText(packageText.getText());
		model.setDatabindingType(databindingTypeCombo.getText());
		model.setGenerateAllCheck(generateAllCheckBoxButton.getSelection());
		model.setGenerateServerSideInterface(
				generateServerSideInterfaceCheckBoxButton.getSelection());
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
			reader = new WSDLPropertyReader(model);
		try {
			String lname = model.getWsdlURI();
			if (!"".equals(lname.trim())) {

				reader.readWSDL(model.getWebProjectName(), lname);

				this.serviceQNameList = reader.getServiceList();
				if (!serviceQNameList.isEmpty()) {
					serviceNameCombo.removeAll();
					for (int i = 0; i < serviceQNameList.size(); i++) {
						// add the local part of the
						Object serviceQnameInstance = serviceQNameList.get(i);
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
			if (e.getClass().getName().equals("javax.wsdl.WSDLException"))
			status = StatusUtils.errorStatus(
					NLS.bind(Axis2CreationUIMessages.ERROR_INVALID_WSDL_FILE_READ_WRITEL,
							 new String[]{e.getLocalizedMessage()}), e);
		}
	}

	private void loadPortNames() {
		int selectionIndex = serviceNameCombo.getSelectionIndex();
		if (selectionIndex != -1) {
			java.util.List ports = reader.getPortNameList(serviceQNameList
					.get(selectionIndex));
			if (!ports.isEmpty()) {
				portNameCombo.removeAll();
				for (int i = 0; i < ports.size(); i++) {
					// add the local part of the
					portNameCombo.add(ports.get(i).toString());
				}
				portNameCombo.select(0);
				model.setPortName(portNameCombo.getText());
			} else {
				// TODO error no ports found
			}
		}
	}

	/**
	 * Fill the combo with proper databinding names
	 */
	private void fillDatabinderCombo() {
		databindingTypeCombo.add(Axis2Constants.DATA_BINDING_ADB);
		databindingTypeCombo.add(Axis2Constants.DATA_BINDING_XMLBEANS);
		databindingTypeCombo.add(Axis2Constants.DATA_BINDING_NONE);
		int selected = databindingTypeCombo.indexOf(context.getServiceDatabinding().toUpperCase());
		if (selected==-1)
			databindingTypeCombo.select(0);
		else
			databindingTypeCombo.select(selected);
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
	
	
	/**
	 * get the package derived by  Namespace
	 */
	public String getPackageFromNamespace(String namespace){
		
		Object result = null;
		try {
			//Class URLProcessor = Class.forName("org.apache.axis2.util.URLProcessor");
			Class URLProcessor = ClassLoadingUtil
						.loadClassFromAntClassLoader("org.apache.axis2.util.URLProcessor");
			Class parameterTypes[] = new Class[1];
			parameterTypes[0] = String.class;
			Method makePackageNameMethod = URLProcessor
									.getMethod("makePackageName", parameterTypes);
			Object args[] = new Object[1];
			args[0] = namespace;
			result = makePackageNameMethod.invoke(makePackageNameMethod, args);
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
		return (String)result;
	}

}
