package org.eclipse.wst.wsdl.ui.internal.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.adapters.visitor.W11XSDVisitorForFields;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;

public class WSDLVisitorForParameters
{
    public List concreteComponents = new ArrayList();
    public List thingsToListenTo = new ArrayList();

    public void visitMessageReference(MessageReference messageReference)
    {
      if (messageReference.getEMessage() != null)
      {
        visitMessage(messageReference.getEMessage());
      }
    }
    
    public void visitMessage(Message message)
    {
      // for now we assume that the first part is the only one that
      // should be used to deduce the parameters
      // TODO (cs) we need to revist this, multiple parts need to be considered
      //
        if (message != null) {
            thingsToListenTo.add(message);
            for (Iterator i = message.getEParts().iterator(); i.hasNext(); )
            { 
              visitPart((Part)i.next());
            }
        }
    }

    void visitPart(Part part)
    {
      thingsToListenTo.add(part);      
      if (part.getElementDeclaration() != null)
      {
        visitXSDElementDeclaration(part.getElementDeclaration());
      }
      else
      // if (part.getTypeDefinition() instanceof XSDSimpleTypeDefinition)
      {
        concreteComponents.add(part);
      }
    }

    void visitXSDElementDeclaration(XSDElementDeclaration ed)
    {
      XSDTypeDefinition td = ed.getTypeDefinition();
      if (td != null)
      {  
        if (td.getName() != null)
        {
          concreteComponents.add(ed);
        }
        else if (td instanceof XSDComplexTypeDefinition)
        {
          // we're dealing with an anonymous locally defined type
          // se we need to visit it's children
          thingsToListenTo.add(ed);
          visitXSDComplextTypeDefinition((XSDComplexTypeDefinition) td);
        }
      }
    }

    void visitXSDComplextTypeDefinition(XSDComplexTypeDefinition td)
    {
      // TODO (cs) revisit to see if it makes sense for the WSDL editor to redisplay
      // the 'Fields' (in XSD editor lingo) as paramters
      // perhaps the WSDL Editor should simply reuse the XSD Editor's
      // ComplexType edit part when displaying these sections?
      W11XSDVisitorForFields fieldVisitor = new W11XSDVisitorForFields();
      fieldVisitor.visitComplexTypeDefinition(td);
      concreteComponents.addAll(fieldVisitor.getConcreteComponentList());
      thingsToListenTo.addAll(fieldVisitor.getThingsWeNeedToListenTo());
    }
  }