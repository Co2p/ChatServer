package eson.co2p.se;

/**
 * Created by isidor on 2014-10-15.
 *
 *  Handles the abstract creation of messages, includes all methods shared by
 *  all messageclasses.
 */
public abstract class message {
    //op is used for testing purposes and is not supposed to be used by
    //the program itself
    private int op;
    private PDU rawdata;

    public void addOp(int op){
        rawdata = new PDU(1);
        this.op = op;
        rawdata.setByte(0, (byte)op);
    }

    public byte[] getData(){
        return rawdata.getBytes();
    }

    public PDU getRawdata(){
        return rawdata;
    }

    public int getOp(){
        return op;
    }


}
