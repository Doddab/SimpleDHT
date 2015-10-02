package edu.buffalo.cse.cse486586.simpledht;

/**
 * Created by user on 1/4/15.
 */
import java.io.Serializable;
public class Serialization implements Serializable{
    public String pdsr;
    public boolean flag;
    public String csr;
    public static int ins=1;
    public static int que=2;
    public static int rc3 =5;
    public String scsr;
    public String value;
    public String key;

    public static int del=0;
    public static int r1=3;
    public static int rp2=4;


    public int id;
    public String n;
    public String spp2;

    public String spp;


    Serialization(int id,String n, String spp2, String pdsr, String scsr, String key,
                  String value,
                  String spp, boolean flag, String csr)
    {
        this.id=id;
        this.pdsr=pdsr;
        this.scsr=scsr;
        this.key=key;
        this.value=value;
        this.n = n;
        this.spp2=spp2;
        this.spp = spp;
        this.flag=flag;
        this.csr = csr;
    }
}
