package com.unison.scada.availability.comm.opcda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class OPCServer
{
    static Logger logger = LoggerFactory.getLogger(OPCServer.class);

    /* Load class DLL library with native code implementation */
    static {
        try {
            OPCServerStatus oss = new OPCServerStatus();
            logger.info("OPC SERVER STATUS :: " + oss.getStatus());
            System.loadLibrary("availabilityopcda");
            logger.info("Library loading SUCCESS.[availabilityopcda.dll]");
        } catch(Exception e) {
            logger.error("Library loading FAILURE.[availabilityopcda.dll]");
            System.exit(-1);
        }
    }

    //--- Default Constructor ---//
    public OPCServer(String opcServerName) throws OPCException {
        if( opcServerName==null || opcServerName.isEmpty())
        {
            throw new OPCException("opcServerName is NULL or Empty!!!");
        }

        startServer(opcServerName);    //Calling native method
    }


    //------------- Native methods ---------------//
    /** Method initializes COM and connects to OPC server */
    private native void startServer(String serverName) throws OPCException; //Starts OPC server

    /** Method disconnects from OPC server and uninitializes COM */
    private native void stopOPCServer() throws OPCException; //Stops OPC server

    /** Method returns OPC server object populated with server status values. */
    public native OPCServerStatus getServerStatus() throws OPCException; //Returns server status

    /** Native Method creates an OPC group on the OPC server*/
    private native OPCGroup addOPCGroup(String groupName, int updateRate, float percentDeadband, boolean activeState)throws OPCException; //Add OPCgroup to the server

    /** Native Method removes an OPC group from OPC server*/
    private native void removeOPCGroup(OPCGroup removingGroup)throws OPCException; //Add OPCgroup to the server
    //------- Native methods ----------//


    //------ Server Name (Example: Matrikon.OPC.Simulation.1) ------//
    private String serverName;
    public String getServerName() { return this.serverName; }


    public void stopServer() throws OPCException {

//        log.info("Stopping OPC Server[" + this.serverName + "]");
        //Step1: Stop OPCServer on Com Level
        stopOPCServer();	//JNI method call (Will release all groups and OPCDataCollbacks on COM level)

        //Step2: Stop all Data Callback Threads
        //Loop thro OPCGroups and stop all CallbackData threads
        //Iterator groupsIter = (new ArrayList(opcGroups.values())).iterator();
        Iterator<OPCGroup> groupsIter = (new ArrayList<OPCGroup>(opcGroups.values())).iterator();

        while(groupsIter.hasNext()) {
            //Will mark all data callbacks to be stopped
            OPCGroup group = groupsIter.next();

            if( group != null ) group.destroyDataCallback();

//			System.out.println("Destory Data Callback.[" + group.getOpcGroupName() + "]");
        }
        OPCGroup.dataCallbackThreadGroup.interrupt();	//Will wakeup all DataCallback Threads


        //Step3: Clear opcGroups Map on Java Level
        this.opcGroups.clear();

//        log.info("Stopped OPC Server[" + this.serverName + "]");
    }


    //----------------------------------------------------//
    //------------ OPC Server Group management -----------//
    //----------------------------------------------------//
    private Map<String, OPCGroup> opcGroups = new HashMap<String, OPCGroup>();
    /**
     * Method creates an OPC Group at the server
     * @param opcGroupName	Name for the OPCGroup that will be created
     * @param reqUpdateRate Requested group update Rate
     * @param percentDeadband Percent deadband
     * @param activeState [rue - active] [false - inactive]
     * @throws OPCException
     */
    public void addNewGroup(	String opcGroupName,
                                int reqUpdateRate,
                                int percentDeadband,
                                boolean activeState) throws OPCException {
        logger.info("addNewGroup[" + opcGroupName + "] Created.[updateRate=" + reqUpdateRate + "]");

        if (opcGroups.containsKey(opcGroupName)) {
            throw new OPCException("Duplicate groupname: "+opcGroupName+"! Groupname must be unique!!");
        }

        OPCGroup newAdedGroup;
        newAdedGroup = addOPCGroup(opcGroupName, reqUpdateRate, percentDeadband, activeState);
        opcGroups.put(opcGroupName, newAdedGroup); //Add new created OPC group to the OPC Groups list.
    }

    /**
     * Method removes an OPCGroup from opcGroups List and from the OPC server on the remote side
     * @param groupName	OPCGroup object representing OPC group that should be removed
     */
    public void removeGroup(String groupName) throws OPCException {
        System.out.println("Removing Group: "+groupName);

        OPCGroup remGroup = opcGroups.get(groupName);
        if (remGroup != null) {
            removeOPCGroup(remGroup);	//Native method call
            opcGroups.remove(remGroup.getOpcGroupName()); //Remove OPC group from OPC Groups list.
        } else {
            throw new OPCException("Cannot remove: "+groupName+" group! No such an group available on server.");
        }
    }
    /**
     * Method returns an Map containing all OPCGroups created on this OPCServer
     * @return opcGroups Map
     */
    public Map<String, OPCGroup> getAllOPCGroups(){
        return this.opcGroups;
    }

    /**
     * Method returns an OPCGroup object for a gaven OPC Group name.
     * @param groupName	Name of the group that is being requested.
     * @return requestedGroup OPCGroup object representing the requested group.
     */
    public OPCGroup getGroupByName(String groupName) throws OPCNotFoundException {
//        log.info("opcGroups length : " + opcGroups.size());
        OPCGroup requestedGroup = opcGroups.get(groupName);
        if (requestedGroup == null){
            throw new OPCNotFoundException("Group ["+groupName+"] not existant on OPC server.");
        }
        return requestedGroup;
    }

}
