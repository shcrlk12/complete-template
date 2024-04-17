package com.unison.scada.availability.comm.opcda;


/**
 * OPC Interface contains all predefined values as required in OPC specification
 * @author nbrgulja
 */
public interface OPC {

    //----------------- OPC Server States -------------------//
    public static final int OPC_STATUS_RUNNING		= 1;
    public static final int OPC_STATUS_FAILED		= 2;
    public static final int OPC_STATUS_NOCONFIG		= 3;
    public static final int OPC_STATUS_SUSPENDED	= 4;
    public static final int OPC_STATUS_TEST			= 5;

    //----------------- OPC Data Source ---------------------//
    public static final int OPC_DS_CACHE	= 1;
    public static final int OPC_DS_DEVICE	= 2;

    //----------------- OPC Item Access rigths --------------//
    public static final int OPC_READABLE 	= 1;
    public static final int OPC_WRITEABLE 	= 2;

    //----------------- OPC Quality definitions --------------//
    public static final int OPC_QUALITY_MASK 			= 0xC0;
    public static final int OPC_STATUS_MASK 			= 0xFC;
    public static final int OPC_LIMIT_MASK				= 0x03;
    public static final int OPC_QUALITY_BAD				= 0x00;
    public static final int OPC_QUALITY_UNCERTAIN		= 0x40;
    public static final int OPC_QUALITY_GOOD			= 0xC0;
    public static final int OPC_QUALITY_CONFIG_ERROR	= 0x04;
    public static final int OPC_QUALITY_NOT_CONNECTED	= 0x08;
    public static final int OPC_QUALITY_DEVICE_FAILURE	= 0x0c;
    public static final int OPC_QUALITY_SENSOR_FAILURE	= 0x10;
    public static final int OPC_QUALITY_LAST_KNOWN		= 0x14;
    public static final int OPC_QUALITY_COMM_FAILURE	= 0x18;
    public static final int OPC_QUALITY_OUT_OF_SERVICE	= 0x1C;
    public static final int OPC_QUALITY_LAST_USABLE		= 0x44;
    public static final int OPC_QUALITY_SENSOR_CAL		= 0x50;
    public static final int OPC_QUALITY_EGU_EXCEEDED	= 0x54;
    public static final int OPC_QUALITY_SUB_NORMAL		= 0x58;
    public static final int OPC_QUALITY_LOCAL_OVERRIDE 	= 0xD8;
    public static final int OPC_LIMIT_OK 				= 0x00;
    public static final int OPC_LIMIT_LOW				= 0x01;
    public static final int OPC_LIMIT_HIGH				= 0x02;
    public static final int OPC_LIMIT_CONST				= 0x03;

}
