/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   136449 brunssen@us.ibm.com - Vince Brunssen
 * 20060427   136449 clbush@us.ibm.com - Christopher Bush  
 ******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.net.MalformedURLException;
import java.util.Vector;
import java.rmi.AlreadyBoundException;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessServices;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.BusinessInfos;
import org.uddi4j.response.RegisteredInfo;
import org.uddi4j.response.TModelInfo;
import org.uddi4j.response.TModelInfos;
import org.uddi4j.transport.TransportException;

public class LoginAdvancedAction extends PublishAction {
   public LoginAdvancedAction(Controller controller) {
      super(controller);
      propertyTable_.put(UDDIActionInputs.QUERY_ITEM, String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
      propertyTable_
            .put(UDDIActionInputs.QUERY_STYLE_BUSINESSES, String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET, String
            .valueOf(UDDIActionInputs.QUERY_MAX_SEARCH_SET));
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS, String
            .valueOf(UDDIActionInputs.QUERY_MAX_RESULTS));
   }

   protected final boolean processOthers(MultipartFormDataParser parser, FormToolPropertiesInterface formToolPI)
         throws MultipartFormDataException {
      String ownedChecked = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED);

      // Validate the data.
      boolean inputsValid = true;
      if (ownedChecked != null)
         propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_OWNED, ownedChecked);
      else
         removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_OWNED);

      return inputsValid;
   }
   
   private boolean attemptingDoubleLogin(){
	   String userId = (String) propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
	   String publishURL = (String) propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
	   Node rootNode = regNode_.getNodeManager().getRootNode();
       Vector allNodes = rootNode.getChildNodes();
       
       for(int k = 0; k < allNodes.size(); k++){
      	 Node n = (Node)allNodes.get(k);
      	 if (n instanceof RegistryNode){
      		 RegistryElement myElement = ((RegistryElement)n.getTreeElement());
      		 if (n.getNodeId() != regNode_.getNodeId() && publishURL.equals(myElement.getPublishURL()))
      			 if(userId.equals(myElement.getUserId()))
      				 return true;
      	 }
       }
       return false;
   }
   
   public final boolean run() {
      UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
      MessageQueue messageQueue = uddiPerspective.getMessageQueue();
      // System.out.println("+++++ Trying to login +++++");
      try {
         boolean shouldAddQueryNode = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE) == null);
         String publishURL = (String) propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
         String userId = (String) propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
         String password = (String) propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
         int maxResults = 300;
         
         // The action can be run under the context of either a registry or a
         // query node.
         RegistryElement regElement = (RegistryElement) regNode_.getTreeElement();
         
                 
         if(attemptingDoubleLogin())
        	 throw new AlreadyBoundException(uddiPerspective.getMessage("MSG_INFO_NO_MULTIPLE_LOGIN")); 
         
         if (!regElement.isLoggedIn())
            regElement.performLogin(publishURL, userId, password);

         UDDIProxy proxy = regElement.getProxy();

         BusinessInfos busInfos = null;
         TModelInfos tmInfos = null;

         RegisteredInfo ri = proxy.get_registeredInfo(regElement.getAuthInfoString());
         busInfos = ri.getBusinessInfos();
         tmInfos = ri.getTModelInfos();

         int finalNumberOfBusinessEntities = Math.min(maxResults, busInfos.size());
         Vector businessKeys = new Vector();
         for (int i = 0; i < finalNumberOfBusinessEntities; i++) {
            BusinessInfo busInfo = (BusinessInfo) busInfos.get(i);
            businessKeys.addElement(busInfo.getBusinessKey());
         }

         if (finalNumberOfBusinessEntities > 0) {
            if (shouldAddQueryNode) {
               Vector beVector = proxy.get_businessDetail(businessKeys).getBusinessEntityVector();
               BusinessEntity be = null;
               BusinessServices bs = null;
               int size = beVector.size();
               if (size > 0) {
                  for (int i = 0; i < size; i++) {
                     be = (BusinessEntity) beVector.elementAt(i);
                     addPublishedItemNode(be, regElement);
                     bs = be.getBusinessServices();
                     if (bs != null) {
                        Vector vServices = bs.getBusinessServiceVector();
                        int services = vServices.size();
                        for (int j = 0; j < services; j++) {
                           addPublishedItemNode(bs.get(j), regElement);
                        }
                     }
                  }
                  messageQueue
                        .addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESSES_FOUND", String.valueOf(size)));
               }
            }
         }

         int finalNumberOfTModels = Math.min(maxResults, tmInfos.size());
         Vector tModelKeys = new Vector();
         for (int i = 0; i < finalNumberOfTModels; i++) {
            TModelInfo tmInfo = (TModelInfo) tmInfos.get(i);
            tModelKeys.addElement(tmInfo.getTModelKey());
         }

         if (finalNumberOfTModels > 0) {
            if (shouldAddQueryNode) {
               Vector finalTModelVector = proxy.get_tModelDetail(tModelKeys).getTModelVector();

               int size = finalTModelVector.size();
               if (size > 0) {
                  TModel tm = null;
                  for (int i = 0; i < size; i++) {
                     tm = (TModel) finalTModelVector.elementAt(i);
                     addPublishedItemNode(tm, regElement);                        
                  }
                  messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_INTERFACES_FOUND", String
                        .valueOf(size)));
               }
            }
         }

         int id = 1;
         RegistryNode regNode = getRegistryNode();

         // we are logged in so change the loginTool to act as a logoutTool;
         regNode.getLoginTool().setAltText(uddiPerspective.getMessage("ALT_LOGOUT"));
         regNode.getLoginTool().setToLogoutLink();
         String registryName = regElement.getName();
         String[] s = new String[2];
         s[0] = registryName;
         s[1] = userId;
         regElement.setName(uddiPerspective.getMessage("MSG_INFO_LOGGED_IN_NODE", s));

         if (finalNumberOfBusinessEntities > 0) {
            Node pbeNode = regNode.getChildNode(regElement.getPublishedBusinessesElement());
            id = pbeNode.getNodeId();
         } else if (finalNumberOfTModels > 0) {
            Node psiNode = regNode.getChildNode(regElement.getPublishedServiceInterfacesElement());
            id = psiNode.getNodeId();
         } 
         // System.out.println("Node id = " + id);
         uddiPerspective.getNodeManager().setSelectedNodeId(id);
         uddiPerspective.getNodeManager().makeSelectedNodeVisible();

         if ((finalNumberOfBusinessEntities < 1) && (finalNumberOfTModels < 1))
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_NO_BUSINESSES_OR_SERVICE_INTERFACES_FOUND"));

         return true;
      } catch (TransportException e) {
         messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
         messageQueue.addMessage("TransportException");
         messageQueue.addMessage(e.getMessage());
      } catch (UDDIException e) {
         messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
         messageQueue.addMessage("UDDIException");
         messageQueue.addMessage(e.toString());
      } catch (MalformedURLException e) {
         messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
         messageQueue.addMessage("MalformedURLException");
         messageQueue.addMessage(e.getMessage());
      } catch (AlreadyBoundException e) {
    	 messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
    	 messageQueue.addMessage("AlreadyBoundException");
    	 messageQueue.addMessage(e.getMessage());
      }
      
      return false;
   }
}