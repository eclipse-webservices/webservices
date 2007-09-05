/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.engine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.actions.Action;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ShowPerspectiveAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.engine.constants.ActionDataConstants;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.ActionDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.ScenarioDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.TransactionDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class ActionEngine
{
  public static final byte MODE_DISABLED = 0x0;
  public static final byte MODE_STOP = 0x1;
  public static final byte MODE_RECORD = 0x2;
  public static final byte MODE_PLAY = 0x3;

  private Controller controller;
  private byte mode;
  private Vector actionDescriptors;

  public ActionEngine(Controller controller)
  {
    this.controller = controller;
    mode = MODE_DISABLED;
    actionDescriptors = new Vector();
  }

  public void executeScenario(ScenarioDescriptor scenarioDescriptor)
  {
    TransactionDescriptor[] transactionDescriptors = scenarioDescriptor.getTransactionDescriptors();
    for (int i = 0; i < transactionDescriptors.length; i++)
      executeTransaction(transactionDescriptors[i]);
  }

  public boolean executeTransaction(TransactionDescriptor transactionDescriptor)
  {
    boolean result = true;
    ActionDescriptor[] actionDescriptors = transactionDescriptor.getActionDescriptors();
    for (int i = 0; i < actionDescriptors.length; i++)
    {
      if (result)
      {
        if (!executeAction(actionDescriptors[i]))
          result = false;
      }
      else
        actionDescriptors[i].setStatusId(ActionDataConstants.VALUE_STATUS_ID_UNATTEMPTED);
    }
    return result;
  }

  public boolean executeAction(ActionDescriptor actionDescriptor)
  {
    int attempts = actionDescriptor.getAttempts();
    for (int i = 0; i < attempts; i++)
    {
      try
      {
        Class classAction = Class.forName(actionDescriptor.getId());
        if (Action.class.isAssignableFrom(classAction))
        {
          Action action = (Action) newInstance(classAction);
          if (action != null)
          {
            Hashtable properties = actionDescriptor.getProperties();
            ITransformer[] transformers = action.getTransformers();
            for (int j = 0; j < transformers.length; j++)
              properties = transformers[j].deNormalize(properties);
            action.setPropertyTable(properties);
            boolean actionResult = action.run();
            // TODO actionDescriptor.addStatus(action.getStatus());
            if (actionResult)
            {
              actionDescriptor.setStatusId(ActionDataConstants.VALUE_STATUS_ID_PASSED);
              return true;
            }
          }
          else
            throw new ClassNotFoundException(actionDescriptor.getId());
        }
      }
      catch (ClassNotFoundException cnfe)
      {
        cnfe.printStackTrace();
        StringWriter sw = new StringWriter();
        cnfe.printStackTrace(new PrintWriter(sw));
        actionDescriptor.addStatus(sw.getBuffer().toString());
      }
      catch (Throwable t)
      {
        t.printStackTrace();
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        actionDescriptor.addStatus(sw.getBuffer().toString());
      }
    }
    actionDescriptor.setStatusId(ActionDataConstants.VALUE_STATUS_ID_FAILED);
    return false;
  }

  private ScenarioDescriptor newScenarioDescriptor()
  {
    return new ScenarioDescriptor();
  }

  private TransactionDescriptor newTransactionDescriptor()
  {
    return new TransactionDescriptor();
  }

  private ActionDescriptor newActionDescriptor(Action action)
  {
    ActionDescriptor actionDescriptor = new ActionDescriptor();
    actionDescriptor.setId(action.getClass().getName());
    Hashtable properties = new Hashtable(action.getPropertyTable());
    ITransformer[] transformers = action.getTransformers();
    for (int i = 0; i < transformers.length; i++)
      properties = transformers[i].normalize(properties);
    actionDescriptor.setProperties(properties);
    return actionDescriptor;
  }

  private Object newInstance(Class c)
  {
    try
    {
      // instantiates the action using the constructor that takes in a
      // controller object
      Constructor constructor = c.getConstructor(new Class[]{Controller.class});
      return constructor.newInstance(new Object[]{controller});
    }
    catch (NoSuchMethodException nsme)
    {
    }
    catch (InstantiationException ie)
    {
    }
    catch (IllegalAccessException iae)
    {
    }
    catch (InvocationTargetException ite)
    {
    }
    catch (Throwable t)
    {
    }
    Object instance = null;
    try
    {
      // instantiates the action using the default constructor
      Constructor constructor = c.getConstructor(new Class[0]);
      instance = constructor.newInstance(new Object[0]);
      Method method = c.getMethod("setController", new Class[]{Controller.class});
      method.invoke(instance, new Object[]{controller});
    }
    catch (NoSuchMethodException nsme)
    {
    }
    catch (InstantiationException ie)
    {
    }
    catch (IllegalAccessException iae)
    {
    }
    catch (IllegalArgumentException iae)
    {
    }
    catch (InvocationTargetException ite)
    {
    }
    catch (Throwable t)
    {
    }
    return instance;
  }
  
  public byte getMode()
  {
    return mode;
  }
  
  public void setMode(byte mode)
  {
    this.mode = mode;
    if (mode == MODE_RECORD)
    {
      actionDescriptors = new Vector();
      ShowPerspectiveAction showPerspectiveAction = new ShowPerspectiveAction(controller);
      showPerspectiveAction.addProperty(ActionInputs.PERSPECTIVE, String.valueOf(controller.getCurrentPerspective().getPerspectiveId()));
      actionDescriptors.add(newActionDescriptor(showPerspectiveAction));
    }
  }
  
  public boolean executeAction(Action action)
  {
    if (mode == MODE_RECORD)
    {
      ActionDescriptor actionDescriptor = newActionDescriptor(action);
      actionDescriptors.add(actionDescriptor);
    }
    return action.run();
  }
  
  public ScenarioDescriptor getScenario()
  {
    ScenarioDescriptor scenarioDescriptor = newScenarioDescriptor();
    TransactionDescriptor transactionDescriptor = newTransactionDescriptor();
    ActionDescriptor[] actionDescriptorArray = (ActionDescriptor[])actionDescriptors.toArray(new ActionDescriptor[0]);
    transactionDescriptor.setActionDescriptors(actionDescriptorArray);
    scenarioDescriptor.setTransactionDescriptors(new TransactionDescriptor[] {transactionDescriptor});
    return scenarioDescriptor;
  }
}
