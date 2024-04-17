package com.unison.scada.availability.comm.opcda;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * OPCGroup represents an OPC group
 * @author nbrgulja
 */
public class OPCGroup {

//------- Native methods ----------//
    /** Native Method.
     *  Makes a COM call to OPCserver and refreshes current OPCGroup object state
     *  according to values returned by OPC server
     */
    private native void refreshGroupStatus()throws OPCException;

    /** Native Method updates OPC group on server side accoding to
     *  current OPCGroup object parameters.
     */
    private native void setGroupState()throws OPCException;

    /** Native Method creates an OPC Item at the OPC Group
     *	Note: You must pass hClientGroup and hServerGroup to recognize the group where to add item!!
     */
    private native OPCItem addOPCItem(String itemName, String itemID, boolean activeState, String unit)throws OPCException; //Add OPCItem to the server


    /** Native Method removes an OPC Item from OPC group*/
    private native void removeOPCItem(OPCItem remItem)throws OPCException;

    /** Native Method reads items from server synchroneously*/
    private native OPCItem[] opcSyncRead(OPCItem groupItems[], int dataSource)throws OPCException;
    //------- Native methods ----------//



    //------ Global variables ----------//
    DataCallbackHandler dataCallbackHandler = null;
    protected static ThreadGroup dataCallbackThreadGroup = new ThreadGroup("Data Callback Thread Group");


    //---------- Group Identification ----------//
    private int groupClientHandle;	//group OPCHANDLE Client side (required for JNI to resolve group where item belongs to)
    private int groupServerHandle;	//group OPCHANDLE Server side (required for JNI to resolve group where item belongs to)
    //---------- Group Identification ----------//


    //--------------------------------------------------//
    //--------------	(sync/async) Read	----------------//
    //--------------------------------------------------//
    public void syncRead(int dataSource) throws OPCException{

        if (dataSource == OPC.OPC_DS_CACHE || dataSource == OPC.OPC_DS_DEVICE) {
            //OPCItem[] currentItems = (OPCItem[])groupItems.toArray(new OPCItem[groupItems.size()]) ; //Array of items
            OPCItem[] currentItems = groupItems.toArray(new OPCItem[groupItems.size()]) ; //Array of items
            OPCItem[] updatedItems = opcSyncRead(currentItems, dataSource);

            groupItems.clear(); //Cleas old items
            //groupItems.addAll(Arrays.asList(updatedItems)); //Put new items into
            groupItems.addAll(Arrays.asList(updatedItems)); //Put new items into
        } else {
            throw new OPCException("No such an Datasource available! Please use standard data source as defined in OPC specification.");
        }
    }


    //-------------------------------------------------//
    //--------------- Item Management -----------------//
    //-------------------------------------------------//

    private List<OPCItem> groupItems = new ArrayList<>(); /*Item List*/

    /**
     * Method creates an new OPCItem object and addes it to the groupItems List
     * @param itemName	Name of the Item that will be added
     * @param itemID Item ID as specified on OPC Server
     * @param activeState [true - active] [false - inactive]
//     * @param reqVarType Requested VARIANT type for this item.
     * @throws OPCException
     */
    public void addNewItem(String itemName,
                           String itemID,
                           boolean activeState,
                           String unit) throws OPCException {

        //System.out.println("groupItems.size() = " + groupItems.size());

        for (int i=0; i<groupItems.size(); i++) {
            if (groupItems.get(i).getID().equals(itemID) ||
                    groupItems.get(i).getName().equals(itemName)) {
                throw new OPCException("Duplicate Items!! Item ID("+itemID+") and Item Name("+itemName+") must be unique!!");
            }
        }

        try {
            //Calling native method (Will return an OPC group object)
            OPCItem newAdedItem = addOPCItem(itemName, itemID, activeState, unit);

            if (newAdedItem != null){
                groupItems.add(newAdedItem); //Add new created OPC group to the OPC Groups list.
            } else {
                throw new OPCException("Item: "+itemName+" cannot be added. Server has returned an empty object!!");
            }
        }
        catch(OPCException oe)
        {
            throw new OPCException(itemName + "[" + itemID + "] is INVALID!!!!" );
            //oe.printStackTrace();
        }
    }
    //inserted by bbong for BACHMANN PLC, 20100504
    public void addNewItem(String itemName,
                           String itemID,
                           boolean activeState,
                           String unit, String dataType, String description) throws OPCException {

        //System.out.println("Add Item: " + itemName + ", " + itemID + ", " + unit);

        if( dataType==null && description==null )
        {
            if( unit == null ) unit = "";
            addNewItem(itemName, itemID, activeState, unit);
            return;
        }

        for (int i=0; i<groupItems.size(); i++) {
            if (groupItems.get(i).getID().equals(itemID) ||
                    groupItems.get(i).getName().equals(itemName))
            {
                throw new OPCException("Duplicate Items!! Item ID and Item Name must be unique!!");
            }
        }

        try
        {
            //Calling native method (Will return an OPC group object)
            OPCItem newAddedItem = addOPCItem(itemName, itemID, activeState, unit);
            if (newAddedItem != null){
                groupItems.add(newAddedItem); //Add new created OPC group to the OPC Groups list.
            } else {
                throw new OPCException("Item: "+itemName+" cannot be added. Server has returned an empty object!!");
            }
        }
        catch(OPCException oe)
        {
            throw new OPCException(itemName + "[" + itemID + "] is INVALID!!!!" );
        }

    }
    /**
     * This method looks in the list for an Item with coresponding name.
     * @param itemName
     * @return OPCITem Returns an OPCITem object for an specific Item name
     * @throws OPCException When no item found it will cause an OPCException
     */
    public OPCItem getItemByName(String itemName) throws OPCNotFoundException {

        OPCItem requestedItem = null;

        try
        {
            for (int i=0; i<groupItems.size(); i++) {
                if (groupItems.get(i).getName().equals(itemName)) {
                    requestedItem = groupItems.get(i);
                    break;
                }
            }
        }
        catch(Exception e)
        {
            throw new OPCNotFoundException("Item "+itemName+" not existant at [" + opcGroupName + "] group.");
        }

        return requestedItem;
    }

    /**
     * Method removes an specific OPCItem from the opcItems List as well as
     * from OPCGroup object on server side.
//     * @param remItem	Item that should be removed
     * @throws OPCException
     */
    public void removeItem(String remItemName) throws OPCException {
        OPCItem remItem = null; //Item that will be removed

        ListIterator<OPCItem> items = groupItems.listIterator();
        while(items.hasNext()) {
            OPCItem thisItem = items.next();
            if(thisItem.getName().equals(remItemName)) {
                remItem = thisItem;
            }
        }

        try
        {
            if ( remItem != null) {
                //Calling native method (Will return an OPC group object)
                removeOPCItem(remItem);
                groupItems.remove(remItem); //Remove OPC group from OPC Groups list.
            }
        }
        catch(Exception e)
        {
            if( remItem != null)
                throw new OPCException("Item: "+remItem.getName()+"cannot be removed. No such an Item in this group!!");
        }

    }

    /**
     * Method returns an List populated with all Items belonging to this group
     * @return groupItems List of OPCItem-s
     */
    public List<OPCItem> getAllItems() {
        return this.groupItems;
    }

    //-------------------------------------------------//
    //--------------- Group Attributes ----------------//
    //-------------------------------------------------//

    private String opcGroupName;		/*Group Name*/
    private boolean activeState;		/*Group active state*/
    private int reqUpdateRate;			/*Group requested update rate*/
    private int realUpdateRate;			/*Group real update rate*/
    private float percentDeadband;	/*Group percent deadband*/


    //---------- Group Name ----------//
    /* Returns the group name */
    public String getOpcGroupName() { return this.opcGroupName; }

    //---------- Active State ----------//
    /* Returns true if group is active and false if group is inactive */
    public boolean isActive() throws OPCException{
        refreshGroupStatus();
        return this.activeState;
    }
    /* Activates the group */
    public void activate() throws OPCException {
        boolean oldState = this.activeState; //Memoorize old state
        this.activeState = true;	//Set new state to true
        try {
            setGroupState(); //Native method call
        }catch (OPCException opcEx) {
            this.activeState = oldState; //If exception occured change status back to old status
            throw new OPCException(opcEx.getMessage()); //Forward OPCException as thrown by JNI
        }
    }
    /* Deactivates the group */
    public void deactivate() throws OPCException {
        boolean oldState = this.activeState; //Memoorize old state
        this.activeState = false;	//Set new state to true
        try {
            setGroupState(); //Native method call
        }catch (OPCException opcEx) {
            this.activeState = oldState; //If exception occured change status back to old status
            throw new OPCException(opcEx.getMessage()); //Forward OPCException as thrown by JNI
        }
    }

    //---------- Requested Update Rate ----------//
    public int getReqUpdateRate() { return this.reqUpdateRate; }
    public void setReqUpdateRate(int newReqUpdateRate) throws OPCException {
        int oldReqUpdateRate = this.reqUpdateRate; //Memoorize old update rate
        this.reqUpdateRate = newReqUpdateRate;
        try {
            setGroupState(); //Native method call
        }catch (OPCException opcEx) {
            this.reqUpdateRate = oldReqUpdateRate; //If exception occured change reqUpdateRate back to old value
            throw new OPCException(opcEx.getMessage()); //Forward OPCException as thrown by JNI
        }
    }

    //---------- Real Update Rate ----------//
    public int getRealUpdateRate() throws OPCException{
        refreshGroupStatus();
        return this.realUpdateRate;
    }

    //---------- Percent Deadband ----------//
    public float getPercentDeadband() throws OPCException{
        refreshGroupStatus();
        return this.percentDeadband;
    }
    public void setPercentDeadband(float newPercentDeadband) throws OPCException {
        float oldPercentDeadband = this.percentDeadband; //Memoorize old Deadband
        this.percentDeadband = newPercentDeadband;
        try {
            setGroupState(); //Native method call
        }catch (OPCException opcEx) {
            this.percentDeadband = oldPercentDeadband; //If exception occured change percentDeadband back to old value
            throw new OPCException(opcEx.getMessage()); //Forward OPCException as thrown by JNI
        }
    }






    /**
     * Method initiates OPC server to Data callback OPC client on value change.
     * It is also used for AsyncIO2
     * @throws OPCException
     */
    public void createDataCallback() throws OPCException{

        if(dataCallbackHandler == null) {
            dataCallbackHandler = new DataCallbackHandler();
        }

        if(!dataCallbackHandler.isRunning()) {
            dataCallbackHandler.startDataCallbackHandler();
        } else {
            throw new OPCException("DataCallbackThread is already running!!");
        }
    }

    /**
     * Method will destroy OPCDataCallback object on server side, and client will stop
     * receiving value change notifications.
     * @throws OPCException
     */
    public void destroyDataCallback() throws OPCException{
        //To be implemented
        if( dataCallbackHandler == null ) return;

        if(dataCallbackHandler.isRunning()) {
            dataCallbackHandler.stopDataCallbackHandler();
        }
    }




    //-------------- Inner class -------------------------//
    //----------------------------------------------------//
    /**
     * Over DataCallbackHandler the OPCItem value on OPC client will be changed
     * when the OPC server reports the value change.
     *
     * @author Nermin Brgulja
     */
    class DataCallbackHandler implements Runnable {

        private boolean running = false;		//Flag marking if Thread is running already.
        private Thread callbackThread = null;			//This Thread object

        private boolean subscribed = false;		//Flag marking if client is subsribed by the OPC server
        private List<OPCItem> changedItems = new ArrayList<OPCItem>();
        //----- Group identification -----//
        private int opcGroupClientHande;
        private int opcGroupServerHande;// = groupServerHandle;
        //----- Group identification -----//

        //----- Native Methods -----------//
        /** Native Method initiates OPC server to Data callback OPC client on value change*/
        //Should throw an OPCException if OPCServer not propertly initialized!!
        private native void subscribeOPCDataCallback()throws OPCException; //Initiate the OPC client for OPC data callback by server.
        //Will throw an OPCException if the unsubscription fails!!
        private native void unsubscribeOPCDataCallback()throws OPCException; //Unsubscribe the OPC client from OPC data callback by server.
        //----- Native Methods -----------//


        /**
         * Default constructor
         */
        public DataCallbackHandler() {
            this.opcGroupClientHande = groupClientHandle;
            this.opcGroupServerHande = groupServerHandle;
        }

        /**
         * Method will start DataCallbackHandler thread
         */
        public void startDataCallbackHandler() {
            if(callbackThread != null && callbackThread.isAlive()){
                throw new IllegalStateException("DataCallbackHandler already running");
            }
            callbackThread = new Thread(dataCallbackThreadGroup, this);
            callbackThread.setName("Data Callback Thread for "+getOpcGroupName()+" OPCGroup");
            callbackThread.setPriority(Thread.MAX_PRIORITY);
            callbackThread.setDaemon(false); // Don't want to interrupt this process.
            callbackThread.start();

        }

        /**
         * Method will stop DataCallbackHandler thread
         */
        public void stopDataCallbackHandler(){
            try{
                if(subscribed) {
                    unsubscribeOPCDataCallback();
                    subscribed = false;
                }
            } catch (OPCException opcE) {
                System.err.println("ERROR: Failed to unsubscribe DataCallbackHandler from OPC server. "+opcE);
            }
            this.running = false;
        }

        /**
         * Method return true if thread is running and false if not
         */
        public boolean isRunning(){
            return this.running;
        }


        /**
         * Threads run() method
         */
        public void run(){
            try{
                //If not subscribed subsribe
                if(!subscribed) { subscribeDataCallback(); }		//Subscribe to OPC server for receiving changed Items
                running = true;	//Mark as running

                ///===== Loop and check for changed items =====///
                while (subscribed && running) {

                    try{
                        if(changedItems.size() > 0) {
                            handleNotifiedItems();	//If there are some changed Items than handle them
                        }
                        //Sleep for next 20 seconds ==> 1seconds
                        Thread.sleep(1000);
                    } catch (InterruptedException iEx) {
                        //If interrupted do nothing
                    }
                }


            } catch (OPCException opcE) {
                System.err.println("DataCallbackHandler run() method has thrown an OPCException. "+opcE);
            }
        }




        /**
         * Subscribe Thread to OPC Server for data callback
         */
        private void subscribeDataCallback() throws OPCException{
            if(! subscribed) {
                subscribeOPCDataCallback();
                subscribed = true;
            }
        }


        /**
         * For OPCItem value changed notification form server side.
         * Method is called from JNI when an value changed notification have been submitted.
         * @param changedItemsArray
         */
        synchronized public void notifyChangedItems(OPCItem[] changedItemsArray) {
            //Add submitted OPCItems to changedItems list
            changedItems.addAll(Arrays.asList(changedItemsArray));
            //Get Thread to work
            callbackThread.interrupt();
        }

        /**
         * Method will handle OPCItem value change notification, and will update
         * all OPCItems according to their OPCGroup membership
         */
        synchronized private void handleNotifiedItems() {

            ListIterator<OPCItem> changedItemsIter = changedItems.listIterator();
            while(changedItemsIter.hasNext()) {
                //Get changed OPC Item
                OPCItem chgItem = changedItemsIter.next();

                //Loop over group Items
                ListIterator<OPCItem> groupItemsIter = groupItems.listIterator();
                boolean itemFoud = false;

                while(groupItemsIter.hasNext() && !itemFoud) {
                    OPCItem thisItem = groupItemsIter.next();
                    if(thisItem.getItemClientHandle() == chgItem.getItemClientHandle()) {
                        if(thisItem.getVtType() == chgItem.getVtType()) {

                            //Change Item Value to new value
                            switch (thisItem.getVtType()) {
                                case VariantType.VT_I1:
                                    thisItem.setVTI1Value(chgItem.getVTI1Value());
                                    break;
                                case VariantType.VT_I2:
                                    thisItem.setVTI2Value(chgItem.getVTI2Value());
                                    break;
                                case VariantType.VT_I4:
                                    thisItem.setVTI4Value(chgItem.getVTI4Value());
                                    break;
                                case VariantType.VT_UI1:
                                    thisItem.setVTUI1Value(chgItem.getVTUI1Value());
                                    break;
                                case VariantType.VT_UI2:
                                    thisItem.setVTUI2Value(chgItem.getVTUI2Value());
                                    break;
                                case VariantType.VT_UI4:
                                    thisItem.setVTUI4Value(chgItem.getVTUI4Value());
                                    break;
                                case VariantType.VT_R4:
                                    thisItem.setVTR4Value(chgItem.getVTR4Value());
                                    break;
                                case VariantType.VT_R8:
                                    thisItem.setVTR8Value(chgItem.getVTR8Value());
                                    break;
                                case VariantType.VT_BOOL:
                                    thisItem.setVTBOOLValue(chgItem.getVTBOOLValue());
                                    break;
                                case VariantType.VT_BSTR:
                                    thisItem.setVTBSTRValue(chgItem.getVTBSTRValue());
                                    break;
                                case VariantType.VT_DATE:
                                    thisItem.setVTDATEValue(chgItem.getVTDATEValue());
                                    break;
                            }

                            //--------- Change value Timestamp
                            thisItem.setTimestampMilisec(chgItem.getTimestampMilisec());
                            //--------- Change quality
                            thisItem.setQuality(chgItem.getQuality());
                            //--------- Mark item as changed!!
                            thisItem.markValueChanged();

                        } else {
                            System.err.println("Server have changed VT_TYPE for "+thisItem.getName()+" item!!");
                        }
                    }	//if handle OK
                } //while over all Items
            }	//While over changed Items

            //Finaly clear List
            changedItems.clear();
        }


    }



}