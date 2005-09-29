/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitoractions;

import java.util.Vector;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;





/**
* Objects of this class represent a VisitorAction.
* It will automatically walk the methods in the JavaClass
* */
public abstract class VisitorActionImpl implements VisitorAction 
{

  private Vector beansVisited = null;
  private Vector messages = null;
  private MessageUtils msgUtils_;

  public VisitorActionImpl( Vector messages, Vector beansVisited)
  	{
  	 this.messages = messages;
  	 this.beansVisited = beansVisited;
     msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
  	}
  
  public void initialize(String resident)
  {
    //nothing to be done but must be implemented
  }

 /**
  * Returns the vector of all messages found
  */
  public Vector getMessages ()
  {
  	if (messages == null) 
  		messages = new Vector();
    return messages;
  }

  public Vector getBeansVisited()
  	{
		if ( beansVisited == null)
  			beansVisited = new Vector(); 
        return beansVisited;
   }

  public void addVisitedBean( JavaClass bean)
  	{
  		if ( beansVisited == null)
  			beansVisited = new Vector();
  		beansVisited.add(bean);
  	}

  public boolean isBeanVisited (JavaClass bean)
  	{
  		return (beansVisited != null && beansVisited.contains(bean));
  	}

  /**
  * sets the visitor that calls the visit
  * @parameter Visitor
  */

  public void setVisitor(Visitor visitor)
  {
  }

  protected void addMessage ( String key )
  {
    getMessages().add(msgUtils_.getMessage(key));
  }

  protected void addMessage ( String key, Object[] args )
  {
    getMessages().add(msgUtils_.getMessage(key,args));
  }

}
