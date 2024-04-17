package com.unison.scada.availability.comm.opcda;


/**
 * This interface contains all VARIANT types according to Microsoft COM VARIANT specification
 * Only OPCClient required VARIANT types are also maopped to OPCItem. Other VARIANT types are not supported.
 *
 * @author Nermin Brgulja
 */
public interface VariantType {

    //	public static final int	VT_EMPTY = 0;				//Not suported
//	public static final int	VT_NULL	= 1;				//Not suported
    public static final int	VT_I2	= 2;				//Maps to shortValue
    public static final int	VT_I4	= 3;				//Maps to integerValue
    public static final int	VT_R4	= 4;				//Maps to floatValue
    public static final int	VT_R8	= 5;				//Maps to doubleValue
    //	public static final int	VT_CY	= 6;				//Not suported
    public static final int	VT_DATE	= 7;				//Maps to dateValue
    public static final int	VT_BSTR	= 8;				//Maps to stringValue
    //	public static final int	VT_DISPATCH	= 9;			//Not suported
//	public static final int	VT_ERROR = 10;				//Not suported
    public static final int	VT_BOOL	= 11;				//Maps to booleanValue
    //	public static final int	VT_VARIANT = 12;			//Not suported
//	public static final int	VT_UNKNOWN = 13;			//Not suported
//	public static final int	VT_DECIMAL = 14;			//Not suported
    public static final int	VT_I1	= 16;				//Maps to byteValue
    public static final int	VT_UI1 = 17;				//Maps to uCharValue
    public static final int	VT_UI2 = 18;				//Maps to uShortValue
    public static final int	VT_UI4 = 19;				//Maps to uIntegerValue
//	public static final int	VT_I8	= 20;				//Maps to integerValue
//	public static final int	VT_UI8 = 21;				//Maps to integerValue
//	public static final int	VT_INT = 22;				//Maps to integerValue
//	public static final int	VT_UINT = 23;				//Maps to integerValue
//	public static final int	VT_VOID	= 24;				//Not suported
//	public static final int	VT_HRESULT = 25;			//Not suported
//	public static final int	VT_PTR = 26;				//Not suported
//	public static final int	VT_SAFEARRAY = 27;			//Not suported
//	public static final int	VT_CARRAY = 28;				//Not suported
//	public static final int	VT_USERDEFINED = 29;		//Not suported
//	public static final int	VT_LPSTR = 30;				//Not suported
//	public static final int	VT_LPWSTR = 31;				//Not suported
//	public static final int	VT_RECORD	= 36;			//Not suported
//	public static final int	VT_INT_PTR = 37;			//Not suported
//	public static final int	VT_UINT_PTR	= 38;			//Not suported
//	public static final int	VT_FILETIME	= 64;			//Not suported
//	public static final int	VT_BLOB	= 65;				//Not suported
//	public static final int	VT_STREAM	= 66;			//Not suported
//	public static final int	VT_STORAGE = 67;			//Not suported
//	public static final int	VT_STREAMED_OBJECT = 68;	//Not suported
//	public static final int	VT_STORED_OBJECT = 69;		//Not suported
//	public static final int	VT_BLOB_OBJECT = 70;		//Not suported
//	public static final int	VT_CF	= 71;				//Not suported
//	public static final int	VT_CLSID = 72;				//Not suported
//	public static final int	VT_VERSIONED_STREAM	= 73;	//Not suported
//	public static final int	VT_BSTR_BLOB = 4095;		//Not suported
//	public static final int	VT_ILLEGALMASKED = 4095;	//Not suported
//	public static final int	VT_TYPEMASK = 4095;			//Not suported
//	public static final int	VT_VECTOR	= 4096;			//Not suported
//	public static final int	VT_ARRAY = 8192;			//Not suported
//	public static final int	VT_BYREF = 16384;			//Not suported
//	public static final int	VT_RESERVED	= 32768;		//Not suported
//	public static final int	VT_ILLEGAL = 65535;			//Not suported

}
