/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.WSDLElement;

abstract public class AddMessageReferenceCommand extends WSDLElementCommand
{
  protected String name;
  protected MessageReference messageReference;
  protected Operation operation;
  protected boolean createMessage = false;
  private final String DEFAULT_MESSAGE_NAME = "NewMessage";

  public AddMessageReferenceCommand(Operation operation, String name)
  {
    this.operation = operation;
    this.name = name;
  }
  
  public AddMessageReferenceCommand(Operation operation, String name, boolean createMessage)
  {
    this.operation = operation;
    this.name = name;
    this.createMessage = createMessage;
  }

  protected void createMessage()
  {
    Definition definition = operation.getEnclosingDefinition();
    AddMessageCommand command = 
      new AddMessageCommand(definition,DEFAULT_MESSAGE_NAME,createMessage);      
    command.run();
    messageReference.setEMessage((Message)command.getWSDLElement());
  }
  
  /*
   * Overloaded createMessage(arg) method.  Similar to to createMessage() but takes in a MessageReference.
   * Method used to create a copy of the original Message (contained in the passed in MessageReference).
   */
  protected void createMessage(MessageReference originalMRef, String newMessageRefName) {
    Definition definition = operation.getEnclosingDefinition();
    Message tMessage = originalMRef.getEMessage();
    AddMessageCommand command = 
      new AddMessageCommand(definition, tMessage, newMessageRefName, true);
    command.run();
    messageReference.setEMessage((Message)command.getWSDLElement());
  }
  
  public WSDLElement getWSDLElement()
  {
    return messageReference;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
}
