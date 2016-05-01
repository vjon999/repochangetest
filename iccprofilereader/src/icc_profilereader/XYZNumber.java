/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package icc_profilereader;
/**
 *
 * @author Vicky
 */
public class XYZNumber {

    float X,Y,Z;
    String typeDesc,strXYZ;
    
    public void setNumber(byte[] b,int len) {      
        if(len==20){
        typeDesc=byteToString(b,4);
        X=byteToXYZ(b,8);
        Y=byteToXYZ(b,12);
        Z=byteToXYZ(b,16);
        strXYZ="X = " + String.valueOf(X)+"Y = " +String.valueOf(Y)+"Z = " +String.valueOf(Z);
        }
        else if(len==12){
            X=byteToXYZ(b,0);
            Y=byteToXYZ(b,4);
            Z=byteToXYZ(b,8);
            typeDesc="";
            strXYZ="X = " + String.valueOf(X)+"Y = " +String.valueOf(Y)+"Z = " +String.valueOf(Z);
        }
        //System.out.println(typeDesc);
    }
    
 float byteToXYZ(byte[] b,int pos) {
        int rv=0,fv=0;
        rv|=((int)(b[pos++] & 0xFF))<<8;
        rv|=((int)(b[pos++] & 0xFF));
        fv|=((int)(b[pos++] & 0xFF))<<8;
        fv|=((int)(b[pos++] & 0xFF));
        return (float)((float)rv+((float)fv/65536));
    }
    
 float byteToxy(byte[] b,int pos) {
         int i=0;
        i += ((int) b[pos++] & 0xFF) << 8;
        i += ((int) b[pos++] & 0xFF) << 0;
    return (float)i;
    }

    private String byteToString(byte[] b, int len) {
        String s="";
        for(int i=0;i<len;i++){
            s+=(char)b[i];
        }
        return s;
    }
    
}
