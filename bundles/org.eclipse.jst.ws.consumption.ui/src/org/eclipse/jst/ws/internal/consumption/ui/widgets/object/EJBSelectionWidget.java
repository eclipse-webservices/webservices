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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.Session;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.earcreation.IEARNatureConstants;
import org.eclipse.jst.j2ee.internal.ejb.project.EJBNatureRuntime;
import org.eclipse.jst.j2ee.internal.ejb.project.EJBProjectResources;
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
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;

public class EJBSelectionWidget extends AbstractObjectSelectionWidget implements IObjectSelectionWidget
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private Combo earList;
  private EJBTableViewer beanList;
  private EARNatureRuntime[] earProjects;
  private Integer selectedBeanIndex = null;
  private Vector ejbBeanNames;
  private Vector ejbProjectNames;
  private Listener  statusListener_;
  /* CONTEXT_ID PEBD0001 for the EAR Projects drop-down box */
  private String INFOPOP_PEBD_EAR_PROJECTS = "org.eclipse.jst.ws.consumption.ui.PEBD0001";
  /* CONTEXT_ID PEBD0002 for the table containing all of the bean names */
  private String INFOPOP_PEBD_TABLE_BEAN_NAMES = "org.eclipse.jst.ws.consumption.ui.PEBD0002";

  public EJBSelectionWidget()
  {
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
      return (String) (ejbProjectNames.elementAt(selectedBeanIndex.intValue()));
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
        setBeanList(earProjects[widget.getSelectionIndex()]);
        selectedBeanIndex = null;
        statusListener_.handleEvent(null);
      }
    });
    addEARNamesToList();
    earList.setToolTipText(msgUtils.getMessage("TOOLTIP_EAR_PROJECTS"));
    WorkbenchHelp.setHelp(earList, new Object[]{INFOPOP_PEBD_EAR_PROJECTS});
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
    WorkbenchHelp.setHelp(beanTable, new Object[]{INFOPOP_PEBD_TABLE_BEAN_NAMES});
    if (earProjects != null && earProjects.length > 0)
    {
      setBeanList(earProjects[0]);
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
    if (earProjects != null && earProjects.length > 0)
    {
      for (int index = 0; index < earProjects.length; index++)
      {
        earList.add(earProjects[index].getProject().getName());
      }
      earList.setText(earList.getItem(0));
    }
  }

  private void setBeanList(EARNatureRuntime earProject)
  {
    Vector ejbProjects = J2EEUtils.getEJBProjects(earProject);
    Table beanTable = beanList.getTable();
    beanTable.removeAll();
    ejbBeanNames = new Vector();
    ejbProjectNames = new Vector();
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
          for (int i = 0; i < earProjects.length; i++)
          {
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
  
  public Status validateSelection(IStructuredSelection objectSelection)
  {
    return new SimpleStatus("");
  }
  
  public Status getStatus()
  {  	
    if (selectedBeanIndex == null)
    {
    	return new SimpleStatus("","",Status.ERROR);
    }
    
  	return new SimpleStatus("");
  }
  
}