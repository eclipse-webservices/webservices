/**
 * ConverterServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package wtp;

public class ConverterServiceLocator extends org.apache.axis.client.Service implements wtp.ConverterService {

    public ConverterServiceLocator() {
    }


    public ConverterServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConverterServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Converter
    private java.lang.String Converter_address = "http://localhost:8080/ConverterProj/services/Converter";

    public java.lang.String getConverterAddress() {
        return Converter_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConverterWSDDServiceName = "Converter";

    public java.lang.String getConverterWSDDServiceName() {
        return ConverterWSDDServiceName;
    }

    public void setConverterWSDDServiceName(java.lang.String name) {
        ConverterWSDDServiceName = name;
    }

    public wtp.Converter getConverter() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Converter_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getConverter(endpoint);
    }

    public wtp.Converter getConverter(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            wtp.ConverterSoapBindingStub _stub = new wtp.ConverterSoapBindingStub(portAddress, this);
            _stub.setPortName(getConverterWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setConverterEndpointAddress(java.lang.String address) {
        Converter_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (wtp.Converter.class.isAssignableFrom(serviceEndpointInterface)) {
                wtp.ConverterSoapBindingStub _stub = new wtp.ConverterSoapBindingStub(new java.net.URL(Converter_address), this);
                _stub.setPortName(getConverterWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Converter".equals(inputPortName)) {
            return getConverter();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://wtp", "ConverterService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://wtp", "Converter"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Converter".equals(portName)) {
            setConverterEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
