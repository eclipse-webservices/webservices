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
package org.eclipse.wst.command.internal.env.preferences;

/**
 * This class is used to define preference ids. It corresponds to information
 * specified by a actionDialogPreferenceType extension point.
 * Each actionDialogPreferenceType entry will result in an entry in the
 * action dialogs preference page if the showcheckbox field is true.
 *   
 * Here is an example of this extension point.
 * 
 *   <extension
 *       point="org.eclipse.wst.env.actionDialogPreferenceType">
 *     <actionDialogPreferenceType
 *           showcheckbox="true"
 *           name="%CHECKBOX_SHOW_GENERATE_JAVA_PROXY_DIALOG"
 *           category="org.eclipse.jst.wss.popup.category"
 *           tooltip="%TOOLTIP_PPAD_CHECKBOX_WSDL2PROXY"
 *           infopop="org.eclipse.jst.ws.consumption.ui.PPAD0004"
 *           alwayshide="false"
 *           id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard">
 *     </actionDialogPreferenceType>
 *
 * This actionDialogPreferenceType extension point is associated with an ObjectContribution
 * extension point.  For example: 
 * 
 *      <objectContribution
 *           objectClass="org.eclipse.core.resources.IFile"
 *           nameFilter="*.wsdl"
 *           id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard">
 * <!-- WSDL To Java Bean Proxy -->
 *        <action
 *              label="%ACTION_GENERATE_JAVA_PROXY"
 *              class="org.eclipse.wst.env.ui.widgets.popup.DynamicPopupWizard"
 *              menubarPath="org.eclipse.jst.ws.atk.ui.webservice.category.popupMenu/popupActions"
 *              id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard">
 *        </action>
 *     </objectContribution>
 *
 * The ObjectContribution entry is linked with the actionDialogPreferenceType entry
 * via the id attribute of the ObjectContirbution and the id attribute of actionDialogPreferenceType.
 * Note: the id in the action entry is always associated with the dynamic wizard that
 * should be popuped up.  In the example above the ObjectionContribution id and the
 * action ids are the same, but this need not be the case.  
 */
public class ActionDialogPreferenceType
{
  private String  id_;
  private String  name_;
  private String  infopop_;
  private String  tooltip_;
  private boolean showCheckbox_;
  private boolean alwaysHide_;
  private String  category_;

  /**
   * Sets the id for this popup action.  This id link the actionDialogPreference
   * with an ObjectContribution.
   * @param id the id.
   */
  public void setId(String id)
  {
    id_ = id;
  }

  /**
   * Gets the id for this popup action.
   * @return the id.
   */
  public String getId()
  {
    return id_;
  }

  /**
   * Sets the name for this popup action.  This name is displayed on the
   * dialog preferences page and must be translated.
   * @param name the name of the popup check box.
   */
  public void setName(String name)
  {
    name_ = name;
  }

  /**
   * Gets the display value for this popup check box.
   * Note: this value may be null if the getShowCheckBox method returns false.
   * @return the name.
   */
  public String getName()
  {
    return name_;
  }

  /**
   * Sets the info pop value for this popup check box.
   * @param infopop
   */
  public void setInfopop(String infopop)
  {
    infopop_ = infopop;
  }

  /**
   * Gets the info pop value for this popup check box.
   * Note: this value may be null if the getShowCheckBox method returns false.
   * 
   * @return the infopop value.
   */
  public String getInfopop()
  {
    return infopop_;
  }

  /**
   * Sets the tooltip value for this popup check box.
   * @param tooltip
   */
  public void setTooltip(String tooltip)
  {
    tooltip_ = tooltip;
  }

  /**
   * Gets the tooltip value for this popup check box.
   * Note: this value may be null if the getShowCheckBox method returns false.
   * 
   * @return the tooltip value.
   */
  public String getTooltip()
  {
    return tooltip_;
  }
  
  /**
   * Sets the show check box value for this popup.  If the value is true then
   * this popup will appear on an action dialogs preference page.  Also if this
   * value is true and the always hide value is false then a check box will be displayed
   * on the first page of this popup which asks the user if they want the popup
   * to be displayed the next time are to just execute the popup action.
   * @param value the show check box value.
   */
  public void setShowCheckbox( boolean value )
  {
    showCheckbox_ = value;
  }
  
  /**
   * Gets the show check box value.
   * @return the show check box value.
   */
  public boolean getShowCheckbox()
  {
    return showCheckbox_;
  }
  
  /**
   * Sets the always hide value.  This value specifies that the popup should always
   * be executed without bringing up the popup wizard.
   * @param value the always hide value.
   */
  public void setAlwaysHide( boolean value )
  {
    alwaysHide_ = value;
  }
  
  /**
   * 
   * @return returns the always hide value.
   */
  public boolean getAlwaysHide()
  {
    return alwaysHide_; 
  }
  
  /**
   * Sets the category id for popup action.  All popup actions with the same
   * category id will be grouped together on the same popup action preference page.
   * @param value the category.
   */
  public void setCategory( String value )
  {
    category_ = value;
  }
  
  /**
   * 
   * @return the category id.
   */
  public String getCategory()
  {
    return category_;
  }
}
