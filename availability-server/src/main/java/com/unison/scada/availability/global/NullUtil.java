package com.unison.scada.availability.global;

public class NullUtil {

    public static boolean isNull(Object o){
        return o == null;
    }
    public static boolean isNotNull(Object o){
        return o != null;
    }
    public static String ifNullEmptyString(Object o){
        if(o == null)
            return "";
        return o.toString();
    }

    public static String formatIfNullEmpty(Object o, String format){
        if(o == null)
            return "";
        return String.format(format, o);
    }

    public static String formatIfNullZero(Object o, String format){
        if(o == null)
            return "0";
        return String.format(format, o);
    }

    public static boolean ifNullIsEmpty(Object o){
        if(o == null){
            return true;
        }
        else if(o == ""){
            return true;
        }
        return false;
    }
}
