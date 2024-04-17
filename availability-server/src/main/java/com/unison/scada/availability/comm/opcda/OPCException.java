package com.unison.scada.availability.comm.opcda;


/**
 * OPCException will be thrown if OPC call fails.
 * @author nbrgulja
 */
public class OPCException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 5677132892970247884L;

    /**
     Creates a OPCException object.
     */
    public OPCException() {
        super("OPCException");
    }

    /**
     Creates a OPCException object intialized with a given message.
     */
    public OPCException(String message) {
        super(message);
    }

}