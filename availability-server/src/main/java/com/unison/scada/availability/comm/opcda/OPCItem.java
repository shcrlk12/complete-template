package com.unison.scada.availability.comm.opcda;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OPCItem represents an OPC Item as defined in OPC specification.
 * It can handle Integer, Float, Date, String nad Boolean Value.
 * @author nbrgulja
 */
public class OPCItem {

    //----- Constructor -----//
    public OPCItem(	int groupClientHandle,
                       int groupServerHandle,
                       String itemName,
                       String unit) {
        this.groupClientHandle = groupClientHandle;
        this.groupServerHandle = groupServerHandle;
        this.itemName = itemName;
        this.unit = unit;
    }

    //------- Native methods ----------//
    /** Native Method changes status of this OPCItem*/
    private native void setActiveState()throws OPCException;
    /** Native Method writes new value of this OPCItem to the server*/
    private native void writeItemValue()throws OPCException;
    //------- Native methods ----------//


    private void setNewStatus(boolean newItemState) throws OPCException {

        boolean oldState = this.activeState;	//Memorize old item state
        this.activeState = newItemState;	//Set activeState of current object to new State
        try{
            setActiveState(); //Native method call (Update Item changes to OPC server)
        } catch (OPCException opcEx) {
            this.activeState = oldState; //If native method has thrown an OPCException set activeState back to the old state
            throw new OPCException(opcEx.getMessage()); //Forward thrown Exception while throwing same one
        }
    }



    //------------------------------------------------------//
    //--------------- Item identification part -------------//
    //------------------------------------------------------//
    private int groupClientHandle;	//group OPCHANDLE Client side (required for JNI to resolve group where item belongs to)
    private int groupServerHandle;	//group OPCHANDLE Server side (required for JNI to resolve group where item belongs to)
    private int itemServerHandle;		//item OPCHANDLE Client side
    private int itemClientHandle;		//item OPCHANDLE Server side

    private String itemName;
    private String itemID;
    private String unit;
    private boolean activeState;
    private int accessRights;
    private boolean valueChanged = false;
    private int vtType;
//	private int vtCanonicalType;	//By server Required VARIANT TYPE


    //------ Identification part -----//
    //This is required for OPCDatatCallback subscription to identify the right OPCItem before changing its value
    protected int getGroupClientHandle() {return this.groupClientHandle; }
    //	protected int getGroupServerHandle() {return this.groupServerHandle; }
    protected int getItemClientHandle() {return this.itemClientHandle; }
//	protected int getItemServerHandle() {return this.itemServerHandle; }


    //--------- Item Name ---------//
    public String getName() { return this.itemName; }

    //--------- Unit ---------//
    public String getUnit() { return this.unit; }

    //--------- Item ID ---------//
    public String getID() { return this.itemID; }

    //--------- Activate / Deactivate OPC Item ---------//
    public boolean isActive() { return this.activeState; }
    public void activate() throws OPCException { setNewStatus(true); } //Update Item status to server
    public void deactivate() throws OPCException { setNewStatus(false); } //Update Item status to server

    //--------- Access Rigths ---------//
    public int getAccessRights() { return this.accessRights; }

    //--------- Value Changed Flag -------//
    public boolean isValueChanged() { return this.valueChanged; }
    protected void markValueChanged() { this.valueChanged = true; }
    public void acknValueChanged() { this.valueChanged = false; }


    /** VT Type VT DATA TYPE must be one of the values specified in VariantType interface **/
    public int getVtType() { return this.vtType; }


    //------------------------------------------------//
    //------------  Item value part  -----------------//
    //------------------------------------------------//

    private byte 	byteValue;			//VT_I1
    private short 	shortValue;			//VT_I2
    private int 	intValue;			//VT_I4
    private short 	uByteValue;			//VT_UI1	//Unsigned byte must be mapped over short
    private int 	uShortValue;		//VT_UI2	//Unsigned Short must be mapped over integer value because of its size.
    private long 	uIntValue;			//VT_UI4	//Unsigned Int must be mapped over long value because of its size.
    private float 	floatValue;			//VT_R4
    private double 	doubleValue;		//VT_R8
    private boolean booleanValue;		//VT_BOOL
    private String 	stringValue;		//VT_BSTR
    private double 	dateValue;			//VT_DATE
    //Timestamp and quality
    private long 	timeStampMilisec;
    private int 	quaity;

    /* Simply checks access rigths and whether or not the Item is active.
     * If elerything is OK performs writing to Server
     */
    private void writeNewValue() throws OPCException {
        if (this.accessRights > OPC.OPC_READABLE && this.activeState == true) {
            writeItemValue();
        } else if (this.activeState == false) {
            setNewStatus(true); //If Item inactive activate first and than write new value
            if(this.activeState){ writeItemValue(); } //If Item activated call JNI method an write new value
        } else {
            throw new OPCException("Item "+this.itemName+" is READ ONLY!!");
        }
    }


    //----- Byte Value VT_I1 [java.lang.Byte] ----//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected byte getVTI1Value() { return this.byteValue; }
    protected void setVTI1Value(byte newValue) { this.byteValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Byte getByteValue() { return this.byteValue; }
    /**
     * Method writes value of an OPCItem from type byte [VT_I1]<br>
     * <strong>NOTE: Byte value is an value between -128 and 127.
     * This is same on COM and on java side.<br>
     *
     * @param newByteValue Byte value that will be written to OPCserver
     * @throws OPCException
     */
    public void writeByteValue(Byte newByteValue) throws OPCException {
        if (this.vtType == VariantType.VT_I1) {
            this.byteValue = newByteValue.byteValue();
            writeNewValue();
        } else {
            throw new OPCException("Cannot write Byte. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
        }
    }

    //----- Short Value VT_I2 [java.lang.Short] ----//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected short getVTI2Value() { return this.shortValue; }
    protected void setVTI2Value(short newValue) { this.shortValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Short getShortValue() { return this.shortValue; }
    public void writeShortValue(Short newShortValue) throws OPCException {
        if (this.vtType == VariantType.VT_I2) {
            this.shortValue = newShortValue.shortValue();
            writeNewValue();
        } else {
            throw new OPCException("Cannot write Short. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
        }
    }

    //----- Integer Value VT_I4 [java.lang.Integer] ----//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected int getVTI4Value() { return this.intValue; }
    protected void setVTI4Value(int newValue) { this.intValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Integer getIntValue() { return this.intValue; }

    public void writeIntValue(Integer newIntValue) throws OPCException{
        if (this.vtType == VariantType.VT_I4) {
            this.intValue = newIntValue.intValue();
            writeNewValue();
        } else {
            throw new OPCException("Cannot write Integer. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
        }
    }



    //----- Unsigned Byte Value VT_UI1 [java.lang.Short] (only positive values!!)----//
    public static final short MAX_UBYTE = ((short)Byte.MAX_VALUE*2) +1; //Value is 255
    public static final short MIN_UBYTE = 0;

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected short getVTUI1Value() { return this.uByteValue; }
    protected void setVTUI1Value(short newValue) { this.uByteValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Short getUByteValue() { return this.uByteValue; }
    /**
     * Method writes value of an OPCItem from type UNSIGNEDbyte [VT_UI1] (On C++it is an unsigned char)<br>
     * Accepted values are all values between 0 and MAX_UBYTE=255 (2^8-1) double size of Byte
     * @param newUByteValue
     * @throws OPCException
     */
    public void writeUByteValue(Short newUByteValue) throws OPCException {
        if(newUByteValue.shortValue() >= MIN_UBYTE && newUByteValue.byteValue() <= MAX_UBYTE) {

            if (this.vtType == VariantType.VT_UI1) {
                this.uByteValue = newUByteValue.shortValue();
                writeNewValue();
            } else {
                throw new OPCException("Cannot write Unsigned Byte. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
            }

        } else {
            throw new OPCException("Variable of VT_UI1 type [unsigned Byte] must be an value between 0 and 255. CurrentValue is: "+newUByteValue.toString());
        }
    }


    //----- Unsigned Short Value VT_UI2 [java.lang.Short] (only positive values!!)----//
    public static final int MAX_USHORT = ((int)Short.MAX_VALUE*2) +1; //Value is 65535
    public static final int MIN_USHORT = 0;

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected int getVTUI2Value() { return this.uShortValue; }
    protected void setVTUI2Value(int newValue) { this.uShortValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Integer getUShortValue() { return this.uShortValue; }
    /**
     * Method writes value of an OPCItem from type UNSIGNEDshort [VT_UI2]<br>
     * Accepted values are all values between 0 and MAX_USHORT=65535 (2^16-1) double size of Short
     * @param newUShortValue
     * @throws OPCException
     */
    public void writeUShortValue(Integer newUShortValue) throws OPCException {
        if(newUShortValue.intValue() >= MIN_USHORT && newUShortValue.intValue() <= MAX_USHORT) {

            if (this.vtType == VariantType.VT_UI2) {
                this.uShortValue = newUShortValue.intValue();
                writeNewValue();
            } else {
                throw new OPCException("Cannot write Unsigned Short. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
            }

        } else {
            throw new OPCException("Variable of VT_UI2 type [unsigned Short] must be an value between 0 and 65535. CurrentValue is: "+newUShortValue.toString());
        }
    }

    //----- Unsigned Short Value VT_UI4 [java.lang.Integer] (only positive values!!)----//
    public static final long MAX_UINT = ((long)Integer.MAX_VALUE*2) +1; //Value is 4294967295 (Unsigned Integer is double as big as an normal integer)
    public static final long MIN_UINT = 0;

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected long getVTUI4Value() { return this.uIntValue; }
    protected void setVTUI4Value(long newValue) { this.uIntValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Long getUIntValue() { return this.uIntValue; }
    public void writeUIntValue(Long newUIntValue) throws OPCException {
        if(newUIntValue.longValue() >= MIN_UINT && newUIntValue.longValue() <= MAX_UINT) {

            if (this.vtType == VariantType.VT_UI4) {
                this.uIntValue = newUIntValue.longValue();
                writeNewValue();
            } else {
                throw new OPCException("Cannot write Unsigned Integer. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
            }

        } else {
            throw new OPCException("Variable of VT_UI4 type [unsigned Integer] must be an value between 0 and 4294967295. CurrentValue is: "+newUIntValue.toString());
        }
    }

    //----- Float Value VT_R4 [java.lang.Float] ----//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected float getVTR4Value() { return this.floatValue; }
    protected void setVTR4Value(float newValue) { this.floatValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Float getFloatValue() { return this.floatValue; }
    public void writeFloatValue(Float newFloatValue) throws OPCException {
        if (this.vtType == VariantType.VT_R4) {
            this.floatValue = newFloatValue.floatValue();
            writeNewValue();
        } else {
            throw new OPCException("Cannot write Float. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
        }
    }

    //----- Float Value VT_R8 [java.lang.Double] ----//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected double getVTR8Value() { return this.doubleValue; }
    protected void setVTR8Value(double newValue) { this.doubleValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Double getDoubleValue() { return this.doubleValue; }
    public void writeDoubleValue(Double newDoubleValue) throws OPCException {
        if (this.vtType == VariantType.VT_R8) {
            this.doubleValue = newDoubleValue.doubleValue();
            writeNewValue();
        } else {
            throw new OPCException("Cannot write Double. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
        }
    }

    //----- Boolean Value VT_BOOL [java.lang.Boolean] ----//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected boolean getVTBOOLValue() { return this.booleanValue; }
    protected void setVTBOOLValue(boolean newValue) { this.booleanValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Boolean getBooleanValue() { return this.booleanValue; }
    public void writeBooleanValue(Boolean newBooleanValue) throws OPCException {
        if (this.vtType == VariantType.VT_BOOL) {
            this.booleanValue = newBooleanValue.booleanValue();
            writeNewValue();
        } else {
            throw new OPCException("Cannot write Boolean. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
        }
    }

    //----- String Value VT_BSTR [java.lang.String] ----//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected String getVTBSTRValue() { return this.stringValue; }
    protected void setVTBSTRValue(String newValue) { this.stringValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public String getStringValue() { return this.stringValue; }
    public void writeStringValue(String newStringValue) throws OPCException{
        if (this.vtType == VariantType.VT_BSTR) {
            this.stringValue = newStringValue;
            writeNewValue();
        } else {
            throw new OPCException("Cannot write String. Item "+this.itemName+" requres an value from Variant type ID: "+this.vtType);
        }
    }

    //----- Date Value VT_DATE [java.lang.Date] ------//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected double getVTDATEValue() { return this.dateValue; }
    protected void setVTDATEValue(double newValue) { this.dateValue = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Date getDateValue() {
        Date startVTDate 		= null;
        Date startJavaDate 	= new Date(0); //Java Date Jan 01 1970 [UTC is Date(0)]
        final long ONE_HOUR = 60 * 60 * 1000L;
        try	{
            SimpleDateFormat dateFormater = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
            startVTDate = dateFormater.parse( "1899-12-30T00:00:00UTC" );	//Microsoft variant VT_DATE starting count date
        }	catch ( ParseException theEx )	{
            theEx.printStackTrace();
        }

        //Calculate epoch difference between Java and Microsoft VT_DATE
        long EPOCH_DIFF = (startJavaDate.getTime() - startVTDate.getTime() + ONE_HOUR) / (ONE_HOUR * 24);
        int days = (int)this.dateValue; //Take only full days out of date value
        double hours = this.dateValue - days; //Take only hours out of date value

        long dateMiliseconds = (((long)days - EPOCH_DIFF)* 24 * ONE_HOUR);
        long hoursMiliseconds = Math.round(hours * 24 * ONE_HOUR);

        return new Date(dateMiliseconds + hoursMiliseconds);
    }


    /**
     * Method returns current value as String
     * @return currentValue as String
     */
    public String getValueAsString(){
        String currentValue = null;

        switch (this.vtType) {
            case VariantType.VT_I1:
                currentValue = getByteValue().toString();
                break;
            case VariantType.VT_I2:
                currentValue = getShortValue().toString();
                break;
            case VariantType.VT_I4:
                currentValue = getIntValue().toString();
                break;
            case VariantType.VT_UI1:
                currentValue = getUByteValue().toString();
                break;
            case VariantType.VT_UI2:
                currentValue = getUShortValue().toString();
                break;
            case VariantType.VT_UI4:
                currentValue = getUIntValue().toString();
                break;
            case VariantType.VT_R4:
                currentValue = getFloatValue().toString();
                break;
            case VariantType.VT_R8:
                currentValue = getDoubleValue().toString();
                break;
            case VariantType.VT_BOOL:
                currentValue = getBooleanValue().toString();
                break;
            case VariantType.VT_BSTR:
                currentValue = getStringValue();
                break;
            case VariantType.VT_DATE:
                currentValue = getDateValue().toString();
                break;
        }
        return currentValue;
    }

    //----- TimeStamp ------//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    //Get - set value as passed or received form Microsoft COM according to VT Type
    protected void setTimestampMilisec(long newValue) { this.timeStampMilisec = newValue; }
    protected long getTimestampMilisec() { return this.timeStampMilisec; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public Date getTimeStamp() { return new Date (this.timeStampMilisec); }


    //----- Quality --------//

    //Protected access for data callback and asynchroneous read / write (Access from OPCGroup)
    protected void setQuality(int newValue) { this.quaity = newValue; }

    //Public assess to all other Java clases (Values transformed into Java format)
    public int getQuality() { return this.quaity; }

    /**
     * Method allocates the Item quality as a String value.
     * @return itemQuality as String
     */
    public String getQualityAsString() {
        String itemQuality;

        switch (this.quaity) {

            case OPC.OPC_QUALITY_BAD :
                itemQuality = "QUALITY_BAD";
                break;

            case OPC.OPC_QUALITY_COMM_FAILURE :
                itemQuality = "COMM_FAILURE";
                break;

            case OPC.OPC_QUALITY_CONFIG_ERROR :
                itemQuality = "CONFIG_ERROR";
                break;

            case OPC.OPC_QUALITY_DEVICE_FAILURE :
                itemQuality = "DEVICE_FAILURE";
                break;

            case OPC.OPC_QUALITY_EGU_EXCEEDED :
                itemQuality = "EGU_EXCEEDED";
                break;

            case OPC.OPC_QUALITY_GOOD :
                itemQuality = "QUALITY_GOOD";
                break;

            case OPC.OPC_QUALITY_LAST_KNOWN :
                itemQuality = "QUALITY_LAST_KNOWN";
                break;

            case OPC.OPC_QUALITY_LAST_USABLE :
                itemQuality = "QUALITY_LAST_USABLE";
                break;

            case OPC.OPC_QUALITY_LOCAL_OVERRIDE :
                itemQuality = "QUALITY_LOCAL_OVERRIDE";
                break;

            case OPC.OPC_QUALITY_NOT_CONNECTED :
                itemQuality = "NOT_CONNECTED";
                break;

            case OPC.OPC_QUALITY_OUT_OF_SERVICE :
                itemQuality = "OUT_OF_SERVICE";
                break;

            case OPC.OPC_QUALITY_SENSOR_CAL :
                itemQuality = "SENSOR_CAL";
                break;

            case OPC.OPC_QUALITY_SENSOR_FAILURE :
                itemQuality = "SENSOR_FAILURE";
                break;

            case OPC.OPC_QUALITY_SUB_NORMAL :
                itemQuality = "SUB_NORMAL";
                break;

            case OPC.OPC_QUALITY_UNCERTAIN :
                itemQuality = "UNCERTAIN";
                break;

            default :
                itemQuality = "---> Unknown quality value <---";
        }

        return itemQuality;
    }
}