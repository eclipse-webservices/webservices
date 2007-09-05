/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleAtomicDateTimeFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleAtomicDateTimeRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleAtomicEnumFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleAtomicEnumRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleAtomicFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleAtomicFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleAtomicRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleListFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleListRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleUnionFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDSimpleUnionRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.OptionVector;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.XSDTypeDefinitionUtil;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDVariety;

public class XSDSimpleTypeToFragmentMapper extends XSDToFragmentMapper {
  public XSDSimpleTypeToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition)config.getXSDComponent();
    if (simpleType != null && simpleType.getVariety() != null) {
      switch (simpleType.getVariety().getValue()) {
        case XSDVariety.ATOMIC:
          return getXSDSimpleAtomicFragment(config, id, name);
        case XSDVariety.LIST:
          return getXSDSimpleListFragment(config, id, name);
        case XSDVariety.UNION:
          return getXSDSimpleUnionFragment(config, id, name);
        default:
          return getXSDDefaultFragment(config, id, name);
      }
    }
    else
      return getXSDDefaultFragment(config, id, name);
  }

  private IXSDFragment getXSDSimpleAtomicFragment(XSDToFragmentConfiguration config, String id, String name) {
    boolean isFixed = ((config.getMinOccurs() == config.getMaxOccurs()) && config.getMaxOccurs() != FragmentConstants.UNBOUNDED);
    XSDSimpleTypeDefinition xsdSimpleTypeDef = (XSDSimpleTypeDefinition)config.getXSDComponent();
    XSDSimpleTypeDefinition xsdBaseSimpleTypeDef = (XSDSimpleTypeDefinition)XSDTypeDefinitionUtil.resolveToXSDBuiltInTypeDefinition(xsdSimpleTypeDef);
    String baseSimpleTypeDefNS = null;
    String baseSimpleTypeDefName = null;
    if (xsdBaseSimpleTypeDef != null)
    {
      baseSimpleTypeDefNS = xsdBaseSimpleTypeDef.getTargetNamespace();
      baseSimpleTypeDefName = xsdBaseSimpleTypeDef.getName();
    }
    boolean isEnum = isSimpleTypeEnumeration(xsdSimpleTypeDef);
    if (FragmentConstants.URI_XSD.equals(baseSimpleTypeDefNS))
    {
      if (baseSimpleTypeDefName.equals("date"))
        return getXSDSimpleDateTimeFragment(id, name, config, ActionInputs.CALENDAR_TYPE_DATE,xsdSimpleTypeDef,isEnum,isFixed);
      else if (baseSimpleTypeDefName.equals("dateTime"))
        return getXSDSimpleDateTimeFragment(id, name, config, ActionInputs.CALENDAR_TYPE_DATETIME,xsdSimpleTypeDef,isEnum,isFixed);
      else if (baseSimpleTypeDefName.equals("gYearMonth"))
        return getXSDSimpleDateTimeFragment(id, name, config, ActionInputs.CALENDAR_TYPE_GYEARMONTH,xsdSimpleTypeDef,isEnum,isFixed);
      else if (baseSimpleTypeDefName.equals("gMonthDay"))
        return getXSDSimpleDateTimeFragment(id, name, config, ActionInputs.CALENDAR_TYPE_GMONTHDAY,xsdSimpleTypeDef,isEnum,isFixed);
      else if (baseSimpleTypeDefName.equals("gDay"))
        return getXSDSimpleGDayFragment(id, name, config, xsdSimpleTypeDef,isEnum,isFixed);        
      else if (baseSimpleTypeDefName.equals("boolean"))
        return getXSDSimpleBooleanFragment(id, name, config, xsdSimpleTypeDef,isEnum,isFixed);
    }

    if (isEnum)
    {
      if (isFixed)
        return new XSDSimpleAtomicEnumFixFragment(id, name, config, xsdSimpleTypeDef.getEnumerationFacets());
      else
        return new XSDSimpleAtomicEnumRangeFragment(id, name, config, xsdSimpleTypeDef.getEnumerationFacets());
    }
    else
    {
      if (isFixed)
        return new XSDSimpleAtomicFixFragment(id, name, config);
      else
        return new XSDSimpleAtomicRangeFragment(id, name, config);
    }
  }

  private XSDSimpleAtomicFragment getXSDSimpleDateTimeFragment(String id, String name, XSDToFragmentConfiguration config, int calendarType,XSDSimpleTypeDefinition xsdDateTimeType,boolean isEnum,boolean isFixed)
  {
    if (isEnum)
    {
      if (isFixed)
        return new XSDSimpleAtomicEnumFixFragment(id, name, config, xsdDateTimeType.getEnumerationFacets());
      else
        return new XSDSimpleAtomicEnumRangeFragment(id, name, config, xsdDateTimeType.getEnumerationFacets());
    }
    else
    {
      if (isFixed)
        return new XSDSimpleAtomicDateTimeFixFragment(id, name, config, calendarType);
      else
        return new XSDSimpleAtomicDateTimeRangeFragment(id, name, config, calendarType);
    }
  }
  
  private XSDSimpleAtomicFragment getXSDSimpleGDayFragment(String id, String name, XSDToFragmentConfiguration config, XSDSimpleTypeDefinition xsdDateTimeType,boolean isEnum,boolean isFixed)
  {
    if (isEnum)
      return null;
    OptionVector optionVector = new OptionVector();
    StringBuffer value = new StringBuffer();
    for (int i=1;i<=31;i++)
    {
      value.setLength(0);
      value.append("---");
      if (i<10)
        value.append(0);
      value.append(i);
      optionVector.addOption(String.valueOf(i),value.toString());
    }
    if (isFixed)
      return new XSDSimpleAtomicEnumFixFragment(id, name, config, optionVector);
    else
      return new XSDSimpleAtomicEnumRangeFragment(id, name, config, optionVector);
  }

  private XSDSimpleAtomicFragment getXSDSimpleBooleanFragment(String id, String name, XSDToFragmentConfiguration config, XSDSimpleTypeDefinition xsdBooleanType,boolean isEnum,boolean isFixed)
  {
    if (isEnum)
      return null;
    OptionVector optionVector = new OptionVector();
    optionVector.addOption("true","true");
    optionVector.addOption("false","false");
    if (isFixed)
      return new XSDSimpleAtomicEnumFixFragment(id, name, config, optionVector);
    else
      return new XSDSimpleAtomicEnumRangeFragment(id, name, config, optionVector);
  }

  private IXSDFragment getXSDSimpleListFragment(XSDToFragmentConfiguration config, String id, String name) {
    
    boolean isFixed = ((config.getMinOccurs() == config.getMaxOccurs()) && config.getMaxOccurs() != FragmentConstants.UNBOUNDED);
    if (isFixed)
      return new XSDSimpleListFixFragment(id, name, config, getController());
    else
      return new XSDSimpleListRangeFragment(id, name, config, getController());
  }

  private IXSDFragment getXSDSimpleUnionFragment(XSDToFragmentConfiguration config, String id, String name) {
    
    boolean isFixed = ((config.getMinOccurs() == config.getMaxOccurs()) && config.getMaxOccurs() != FragmentConstants.UNBOUNDED);
    if (isFixed)
      return new XSDSimpleUnionFixFragment(id, name, config, getController());
    else
      return new XSDSimpleUnionRangeFragment(id, name, config, getController());
  }

  private boolean isSimpleTypeEnumeration(XSDSimpleTypeDefinition simpleType) {
    EList e = simpleType.getEnumerationFacets();
    return (e != null && e.size() > 0);
  }
}
