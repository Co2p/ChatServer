package eson.co2p.se;

/**
 * Created by root on 2014-10-15.
 */
public class getServerMessage extends message {
    public getServerMessage(int op){
        this.addOp(op);
        this.getRawdata().extendTo(4);
    }

}