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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EJBResource;
import org.eclipse.jst.j2ee.ejb.Session;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class EJBSelectionWidget extends AbstractObjectSelectionWidget implements IObjectSelectionWidget
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private Combo earList;
  private EJBTableViewer beanList;
  private IVirtualComponent[] earComponents;
  //rskejb private EARNatureRuntime[] earProjects;
  private Integer selectedBeanIndex = null;
  private Vector ejbBeanNames;
  private Vector ejbComponentNames;
  private Vector ejbComponentProjectNames;
  //rskejb private Vector ejbProjectNames;
  private Listener  statusListener_;
  /* CONTEXT_ID PEBD0001 for the EAR Projects drop-down box */
  private String INFOPOP_PEBD_EAR_PROJECTS = "org.eclipse.jst.ws.consumption.ui.PEBD0001";
  /* CONTEXT_ID PEBD0002 for the table containing all of the bean names */
  private String INFOPOP_PEBD_TABLE_BEAN_NAMES = "org.eclipse.jst.ws.consumption.ui.PEBD0002";

  public EJBSelectionWidget()
  {
	earComponents = J2EEUtils.getAllEARComponents();
    /* rskejb 
    IProject[] projects = J2EEUtils.getEARProjects();
	
	
    Vector earVector = new Vector();
    for (int i = 0; i < projects.length; i++)
    {
      try
      {
        earVector.add(projects[i].getNature(IEARNatureConstants.NATURE_ID));
      }
      catch (CoreException ce)
      {
      }
    }
    earProjects = (EARNatureRuntime[]) earVector.toArray(new EARNatureRuntime[0]);
	rskejb */
  }

  public String getSelectedBean()
  {
    if (selectedBeanIndex != null)
    {
      return (String) (ejbBeanNames.elementAt(selectedBeanIndex.intValue()));
    }
    return null;
  }

  public String getSelectedProject()
  {
    if (selectedBeanIndex != null)
    {
      return (String) (ejbComponentProjectNames.elementAt(selectedBeanIndex.intValue()));
    }
    return null;
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener)
  {
    MessageUtils msgUtils = new MessageUtils(pluginId_ + ".plugin", this);
    statusListener_  = statusListener;
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    GridData gd = new GridData(GridData.FILL_BOTH);
    composite.setLayout(layout);
    composite.setLayoutData(gd);
    Composite projectComposite = new Composite(composite, SWT.NONE);
    layout = new GridLayout();
    layout.numColumns = 2;
    projectComposite.setLayout(layout);
    projectComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
    new Label(projectComposite, SWT.NULL).setText(msgUtils.getMessage("LABEL_EAR_PROJECTS"));
    earList = new Combo(projectComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
    earList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    earList.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent evt)
      {
        Combo widget = (Combo) (evt.widget);
        setBeanList(earComponents[widget.getSelectionIndex()]);
        selectedBeanIndex = null;
        statusListener_.handleEvent(null);
      }
    });
    addEARNamesToList();
    earList.setToolTipText(msgUtils.getMessage("TOOLTIP_EAR_PROJECTS"));
    PlatformUI.getWorkbench().getHelpSystem().setHelp(earList, INFOPOP_PEBD_EAR_PROJECTS);
    Group beanComposite = new Group(composite, SWT.NONE);
    layout = new GridLayout();
    beanComposite.setLayout(layout);
    beanComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
    beanComposite.setText(msgUtils.getMessage("LABEL_EJB_BEAN_NAME"));
    beanList = new EJBTableViewer(beanComposite);
    Table beanTable = beanList.getTable();
    gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
    gd.heightHint = 100;
    beanTable.setLayoutData(gd);
    beanTable.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent evt)
      {
        Table table = (Table) (evt.widget);
        selectedBeanIndex = new Integer(table.getSelectionIndex());
        statusListener_.handleEvent(null);
      }
    });
    beanTable.setToolTipText(msgUtils.getMessage("TOOLTIP_TABLE_BEAN_NAMES"));
    PlatformUI.getWorkbench().getHelpSystem().setHelp(beanTable, INFOPOP_PEBD_TABLE_BEAN_NAMES);
    if (earComponents != null && earComponents.length > 0)
    {
      setBeanList(earComponents[0]);
    }
    beanTable.getColumn(0).pack();
    beanTable.getColumn(1).pack();
    // This is a dummy label that forces the status label into the second
    // column.
    new Label(composite, SWT.NULL).setText("");
    return this;
  }

  private void addEARNamesToList()
  {
    if (earComponents != null && earComponents.length > 0)
    {
      for (int index = 0; index < earComponents.length; index++)
      {
        earList.add(earComponents[index].getName());
      }
      earList.setText(earList.getItem(0));
    }
  }

  private void setBeanList(IVirtualComponent earComponent)
  {
    IVirtualComponent[] ejbComponentsArray = J2EEUtils.getReferencingEJBComponentsFromEAR(earComponent.getProject());
    Table beanTable = beanList.getTable();
    beanTable.removeAll();
    ejbBeanNames = new Vector();
    ejbComponentNames = new Vector();
    ejbComponentProjectNames = new Vector();
    for (int index = 0; index < ejbComponentsArray.length; index++)
    {
      EJBArtifactEdit  ejbEdit = null;
      try {
        ejbEdit = EJBArtifactEdit.getEJBArtifactEditForRead(ejbComponentsArray[index]);
        EJBResource ejbRes = ejbEdit.getEJBJarXmiResource();
        EJBJar jar = ejbRes.getEJBJar();
        Vector beans = J2EEUtils.getBeanNames(jar);
        String componentName = ejbComponentsArray[index].getName();
        String projectName = ejbComponentsArray[index].getProject().getName();
        ejbBeanNames.addAll(beans);
        ejbComponentNames.addAll(Collections.nCopies(beans.size(), componentName));
        ejbComponentProjectNames.addAll(Collections.nCopies(beans.size(), projectName));                
      }
      finally {
        if (ejbEdit!=null)
          ejbEdit.dispose();
      }      
    }
    beanList.setData(ejbBeanNames, ejbComponentNames);
    beanList.setInput(ejbBeanNames);    
    
  }
  
  /*
   * @deprecated
   */
  /*
  private void setBeanList(EARNatureRuntime earProject)
  {
    Vector ejbProjects = J2EEUtils.getEJBProjects(earProject);
    Table beanTable = beanList.getTable();
    beanTable.removeAll();
    ejbBeanNames = new Vector();
    //rskejb ejbProjectNames = new Vector();
    ejbComponentNames = new Vector();
    ejbComponents = new Vector();
    for (int index = 0; index < ejbProjects.size(); index++)
    {
      EJBNatureRuntime ejbNature = (EJBNatureRuntime) (ejbProjects.elementAt(index));
      EJBProjectResources ejbResource = new EJBProjectResources(ejbNature.getProject());
      EJBJar jar = ejbResource.getEJBJar();
      Vector beans = J2EEUtils.getBeanNames(jar);
      String projectName = ejbNature.getProject().getName();
      ejbBeanNames.addAll(beans);
      ejbProjectNames.addAll(Collections.nCopies(beans.size(), projectName));
      ejbResource.cleanup();
    }
    beanList.setData(ejbBeanNames, ejbProjectNames);
    beanList.setInput(ejbBeanNames);
  }
  */
  
  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    if (initialSelection != null && !initialSelection.isEmpty())
    {
      Object object = initialSelection.getFirstElement();
      if (object instanceof Session)
      {
        Session session = (Session) object;
        EJBJar jar = session.getEjbJar();
        if (jar != null)
        {
          for (int i = 0; i < earComponents.length; i++)
          {
			      IVirtualComponent[] ejbComponents = J2EEUtils.getReferencingEJBComponentsFromEAR(earComponents[i].getProject());
			      for (int j=0; j <ejbComponents.length; j++)
			      {
              EJBArtifactEdit  ejbEdit = null;
              try {
                ejbEdit = EJBArtifactEdit.getEJBArtifactEditForRead(ejbComponents[j]);
                EJBResource ejbRes = ejbEdit.getEJBJarXmiResource();
                if (ejbRes.getEJBJar() == jar)
                {
                  earList.setText(earComponents[i].getName());
                  setBeanList(earComponents[i]);
                  beanList.setSelection(new StructuredSelection(new Integer[]{new Integer(ejbBeanNames.indexOf(session.getName()))}));
                  selectedBeanIndex = new Integer(ejbBeanNames.indexOf(session.getName()));
                  return;
                }                

              }
              finally {
                if (ejbEdit!=null)
                  ejbEdit.dispose();
              }
				
			      }
			      /* rskejb
            Vector ejbProjects = J2EEUtils.getEJBProjects(earProjects[i]);
            for (Iterator it = ejbProjects.iterator(); it.hasNext();)
            {
              EJBProjectResources ejbResource = new EJBProjectResources(((EJBNatureRuntime) it.next()).getProject());
              if (ejbResource.getEJBJar() == jar)
              {
                earList.setText(earProjects[i].getProject().getName());
                setBeanList(earProjects[i]);
                beanList.setSelection(new StructuredSelection(new Integer[]{new Integer(ejbBeanNames.indexOf(session.getName()))}));
                selectedBeanIndex = new Integer(ejbBeanNames.indexOf(session.getName()));
                ejbResource.cleanup();
                return;
              }
              ejbResource.cleanup();
            }
			      rskejb */
          }
        }
      }
    }
  }

  public IStructuredSelection getObjectSelection()
  {
    IStructuredSelection sel = (IStructuredSelection) beanList.getSelection();
    if (sel != null && !sel.isEmpty())
    {
      Object object = sel.getFirstElement();
      if (object instanceof Integer)
      {
        String selEJBName = (String) ejbBeanNames.get(((Integer) object).intValue());
        if (selEJBName != null)
        {
          IProject project = earComponents[earList.getSelectionIndex()].getProject();
          IVirtualComponent[] ejbComponents = J2EEUtils.getReferencingEJBComponentsFromEAR(project);
          for (int i=0; i<ejbComponents.length; i++)
          {
            EJBArtifactEdit  ejbEdit = null;
            try 
            {
              ejbEdit = EJBArtifactEdit.getEJBArtifactEditForRead(ejbComponents[i]);
              EJBResource ejbRes = ejbEdit.getEJBJarXmiResource();
              EJBJar jar = ejbRes.getEJBJar();
              java.util.List sessions = jar.getSessionBeans();
              for (Iterator it2 = sessions.iterator(); it2.hasNext();)
              {
                Session session = (Session) it2.next();
                if (selEJBName.equals(session.getName()))
                {
                  return new StructuredSelection(new Session[]{session});
                }
              }
            }
            finally {
              if (ejbEdit!=null)
                ejbEdit.dispose();
            }            
          }
          /* rskejb
          Vector ejbProjects = J2EEUtils.getEJBProjects(earProjects[earList.getSelectionIndex()]);
          for (Iterator it = ejbProjects.iterator(); it.hasNext();)
          {
            EJBProjectResources ejbResource = new EJBProjectResources(((EJBNatureRuntime) it.next()).getProject());
            EJBJar jar = ejbResource.getEJBJar();
            java.util.List sessions = jar.getSessionBeans();
            for (Iterator it2 = sessions.iterator(); it2.hasNext();)
            {
              Session session = (Session) it2.next();
              if (selEJBName.equals(session.getName()))
              {
                ejbResource.cleanup();
                return new StructuredSelection(new Session[]{session});
              }
            }
            ejbResource.cleanup();
          }
          rskejb */
        }
      }
    }
    return new StructuredSelection(new Object[0]);
  }
  
  public IProject getProject()
  {
    String projectName = getSelectedProject();
    if (projectName != null && projectName.length() > 0)
      return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    else
      return null;
  }
  
  public String getComponentName()
  {
    if (selectedBeanIndex != null)
    {
      return (String) (ejbComponentNames.elementAt(selectedBeanIndex.intValue()));
    }
    return null;
  }    
  
  public IStatus validateSelection(IStructuredSelection objectSelection)
  {
    return Status.OK_STATUS;
  }
  
  public IStatus getStatus()
  {  	
    if (selectedBeanIndex == null)
    {
    	return StatusUtils.errorStatus( "" );
    }
    
  	return Status.OK_STATUS;
  }
  
}