package com.unison.scada.availability.comm.opcda;

import java.util.Date;

/**
 * This class represents an OPC server status object
 * @author nbrgulja
 */
public class OPCServerStatus {

    //Default Constructor
    public OPCServerStatus(){}

    /**
     * Server Staus
     * int serverStatus - all status Values are predefined at OPC interface
     */
    private int serverStatus;
    public int getStatus() { return this.serverStatus; }

    //------ Vendor Name (Example: Matrikon Consulting Inc.) ------//
    private String vendorInfo;
    public String getVendorInfo() { return this.vendorInfo; }

    //------ Server Start Time ------//
    private long startTimeMillisec;
    public Date getStartTime() { return new Date (this.startTimeMillisec); }

    //------ Server Current Time ------//
    private long currentTimeMillisec;
    public Date getCurrentTime() { return new Date(this.currentTimeMillisec); }

    //------ Server Last Update Time for this client ------//
    private long lastUpdateTimeMillisec;
    public Date getLastUpdateTime() { return new Date(this.lastUpdateTimeMillisec); }
}