/**
 * Converter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package wtp;

public interface Converter extends java.rmi.Remote {
    public float celsiusToFarenheit(float celsius) throws java.rmi.RemoteException;
    public float farenheitToCelsius(float farenheit) throws java.rmi.RemoteException;
}
