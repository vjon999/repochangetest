/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icc_profilereader;

import java.util.Date;
import java.io.*;
/**
 *
 * @author Vicky
 */
public class ICCProfile {
    
    GeneralProfile xprofile;
    String profileName,className,colorSpace,PCS,platform,deviceMfg,deviceModel;
    String deviceAttr,renderingIntent,CMMType,flagDesc,attributes,profileCreator,profileVersionNumber;
    String strDate,tagSignature;
    char[] profileSignature=new char[4];
    int versionNumber,profileSize,illuminantX,illuminantY,illuminantZ,numberOfTags;
    XYZNumber illuminant=new XYZNumber();
    Boolean flag1,flag2;
    Date date;
//    ProfileTag[] tag;
    byte fileBuffer[];
    FileInputStream f;
    DataInputStream dis;
    File file;
    
    
    ICCProfile(String fileName) {
        try {
        file=new File(fileName);
        f=new FileInputStream(file);
        fileBuffer=new byte[(int)file.length()];
        f.read(fileBuffer, 0,128);      
        profileSize=getProfileSize(0,4);
        CMMType=getCMMType(4,4);
        profileVersionNumber=getVersionNumber(8,4);
        className=getDeviceClass(12,4);
        colorSpace=getColorSpace(16,4);
        PCS=getProfileConnectionSpace(20,4);
        date=getCreationDate(24,12);
        profileSignature=getProfileSignature(36,4);
        platform=getPlatform(40,4);
        flagDesc=getFlags(44,4);
        deviceMfg=getDeviceMfg(48,4);
        deviceModel=getDeviceModel(52,4);
        deviceAttr=getDeviceAttr(56,8);
        renderingIntent=getRenderingIntent(64,4);
        illuminant=getIlluminant(68,12);
        profileCreator=getProfileCreator(80,4);
        numberOfTags=byteToInt(fileBuffer,128);
        
        //tagSignature=getTagSignature(128,4);
        

        
        f.close();
        }
        catch(FileNotFoundException e){System.out.println("File notfound !!!");}
        catch(IOException e){e.printStackTrace();}
    }
    
    ICCProfile(File file) {
        try {
        //tag=new ProfileTag();
        this.file=file;
        f=new FileInputStream(file);
        fileBuffer=new byte[(int)file.length()];
        f.read(fileBuffer, 0,(int)file.length());
        profileSize=getProfileSize(0,4);
        CMMType=getCMMType(4,4);
        profileVersionNumber=getVersionNumber(8,4);
        className=getDeviceClass(12,4);
        colorSpace=getColorSpace(16,4);
        PCS=getProfileConnectionSpace(20,4);
        date=getCreationDate(24,12);
        profileSignature=getProfileSignature(36,4);
        platform=getPlatform(40,4);
        flagDesc=getFlags(44,4);
        deviceMfg=getDeviceMfg(48,4);
        deviceModel=getDeviceModel(52,4);
        deviceAttr=getDeviceAttr(56,8);
        renderingIntent=getRenderingIntent(64,4);
        illuminant=getIlluminant(68,12);
        profileCreator=getProfileCreator(80,4);
        numberOfTags=byteToInt(fileBuffer,128);
        
        if(className.equals("Monitor")){
            xprofile=new DisplayProfile(numberOfTags);
        }
        //System.out.println("Number of tags = " + numberOfTags);
        //tag=new ProfileTagHeader[numberOfTags];
        
        int addr=132;
        for(int i=0;i<numberOfTags;i++) {
            xprofile.tag[i]=new ProfileTagHeader();
            xprofile.tag[i].setTagNumber(i);
            xprofile.tag[i].setSignature(byteToString(addr,4));addr=addr+4;
            xprofile.tag[i].setOffset(byteToInt(fileBuffer,addr));addr=addr+4;
            xprofile.tag[i].setSize(byteToInt(fileBuffer,addr));addr=addr+4;
            viewTag(i);
            /*
            tag[i]=new ProfileTagHeader();
            tag[i].setTagNumber(i+1);                      
            tag[i].setSignature(byteToString(addr,4));addr=addr+4;
            tag[i].setOffset(byteToInt(fileBuffer,addr));addr=addr+4;
            tag[i].setSize(byteToInt(fileBuffer,addr));addr=addr+4;    
            viewTag(tag[i].getSignature(),tag[i].getOffset(),tag[i].getSize());
             * */
        }   
        xprofile.showDetails();
        f.close();
        }
        catch(FileNotFoundException e){System.out.println("File notfound !!!");}
        catch(IOException e){e.printStackTrace();}
    }
    
    
    
    
    int getProfileSize(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToInt(buf);
    }
    String getCMMType(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);
    }
    String getProfileName(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);
    }
    
    String getVersionNumber(int pos,int len) {
         byte buf[]=new byte[4];
         for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
            int i=((int)(buf[0] | 0x00));
            String s=String.valueOf(i);
            i=((int)(buf[1] | 0x00));
            s=s+"." +String.valueOf(i);
            i=((int)(buf[2] | 0x00));
            s=s+"." +String.valueOf(i);
            i=((int)(buf[3] | 0x00));
            s=s+"." +String.valueOf(i);
            return s;    
    }
    
    String getColorSpace(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);
    }
    
    String getProfileConnectionSpace(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);
    }
    
    Date getCreationDate(int pos,int len) {
        byte buf[]=new byte[12];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
      return byteToDate(buf);
    }
    
    String getDeviceClass(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return devClass(byteToString(buf));
        
    }
    
    char[] getProfileSignature(int pos,int len) {
        char ch[]=new char[4];
        for(int i=pos;i<pos+len;i++)
            ch[i-pos]=(char) fileBuffer[i];
      
       return ch; 
    }
    
    String getPlatform(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return platform(byteToString(buf));
    }
    
    String getFlags(int pos,int len) {
        boolean bool[]=new boolean[2];
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];

            bool[0]= byteToBoolean(buf[2]);
            bool[1]= byteToBoolean(buf[3]);
            if(bool[0]==true)
                flagDesc=" Embedded ,";
            else
                flagDesc=" not Embedded ,";
            if(bool[1]==true)
                flagDesc=flagDesc+"not independently";
            else
                flagDesc=flagDesc+"independently";
             
        return flagDesc;
    }
     
    String getDeviceMfg(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);      
    }
    
        String getDeviceModel(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);       
    }
        
     String getDeviceAttr(int pos,int len) {
        boolean bool[]=new boolean[2];
        byte buf[]=new byte[8];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];

            bool[0]= byteToBoolean(buf[6]);
            bool[1]= byteToBoolean(buf[7]);
            if(bool[0]==true)
                attributes=" Transperency ,";
            else
                attributes=" Reflective ,";
            if(bool[1]==true)
               attributes=attributes+"Matt";
            else
                attributes=attributes+"Glossy";      
        
        return attributes;
    }
     
     public String getRenderingIntent(int pos,int len) {
         int k=0;
         byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
             k += ((int) buf[2] & 0xFF )<< 8;
             k += ((int) buf[3] & 0xFF )<< 0;
         return strRenderingIntent(k);
     }
     
     public XYZNumber getIlluminant(int pos,int len) {
         byte[] b=new byte[len];         
         XYZNumber ill=new XYZNumber();
         for(int i=0;i<len;i++) {
             b[i]=fileBuffer[i+pos];
         }
         ill.setNumber(b, len);
         return ill;
     }
     
     public String getProfileCreator(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);             
     }
     
     public String strRenderingIntent(int i) {
         switch(i) {
             case 0:return "Perceptual";
             case 1:return "Relative Calorimetric";
             case 2:return "Saturation";
             case 3:return "Absolute Calorimetric";         
         }
         return null;
     }
    
    int byteToInt(byte[] b) {
         int i=0,pos=0;
        i += ((int) b[pos++] & 0xFF) << 24;
        i += ((int) b[pos++] & 0xFF) << 16;
        i += ((int) b[pos++] & 0xFF )<< 8;
        i += ((int) b[pos++] & 0xFF )<< 0;
    return i;
    }
    
        int byteToInt(byte[] b,int pos) {
         int i=0;
        i += ((int) b[pos++] & 0xFF) << 24;
        i += ((int) b[pos++] & 0xFF) << 16;
        i += ((int) b[pos++] & 0xFF )<< 8;
        i += ((int) b[pos++] & 0xFF )<< 0;
    return i;
    }
    float byteToFloat(byte[] b) {
        int rv=0,fv=0;
        rv|=((int)(b[0] & 0xFF))<<8;
        rv|=((int)(b[1] & 0xFF));
        fv|=((int)(b[2] & 0xFF))<<8;
        fv|=((int)(b[3] & 0xFF));
        return (float)((float)rv+((float)fv/65536));
    }
    
    public String byteToString(byte b[]){
        String s = null;
        int i=0;
            while(i!=b.length) {
                if(s==null) {
                     s =String.valueOf((char)b[i]);
                }
                else {
            s+=(char)b[i];
            }
        i++;
     }
    return s;
    }
    
    public String byteToString(int pos,int len){
    String s = null;
    int i=pos;
        while(i<pos+len) {
            if(s==null) {
                s =String.valueOf((char)fileBuffer[i]);
            }
            else s+=(char)fileBuffer[i];
        i++;
    }
       return s;
    }    
    
   public String byteToString(byte[] b,int startpos,int len){
    String s ="";
    int i=startpos;
        while(i<startpos+len) {
            if((char)b[i]=='\0') break;
            s+=(char)b[i];
        i++;
    }
       return s;
    }
    
    
    public boolean byteToBoolean(byte ch) {
        switch(ch) {
            case '0':return false;
            case '1':return true;
            default://throws (NumberFormatException);
        }
        return false;
    }
    
    public Date byteToDate(byte b[]) {
    int dd=0,mm=0,yy=0,hh=0,min=0,ss=0;
    String s;
    
    yy += ((int) b[0] & 0xFF) << 8;
    yy += ((int) b[1] & 0xFF) << 0;
    s=String.valueOf(yy);
    mm += ((int) b[2] & 0xFF) << 8;
    mm += ((int) b[3] & 0xFF) << 0;
    s+="/"+mm;
    dd += ((int) b[4] & 0xFF) << 8;
    dd += ((int) b[5] & 0xFF) << 0;
    s+="/"+dd;
    hh += ((int) b[6] & 0xFF) << 8;
    hh += ((int) b[7] & 0xFF) << 0;
    s+=" , "+hh;
    min += ((int) b[8] & 0xFF) << 8;
    min += ((int) b[9] & 0xFF) << 0;
    s+=":"+min;
    ss += ((int) b[10] & 0xFF) << 8;
    ss += ((int) b[11] & 0xFF) << 0;
    s+=":"+ss;
    Date dt=new Date(yy,mm,dd,hh,min,ss);
    strDate=s;
    //System.out.println(dt);
    return dt;
    }
    
    String platform(String plat) {
        if(plat.equals("APPL")){
            return "Macontish";
        }
        else if(plat.equals("MSFT")){
            return "Microsoft";
        }
        else if(plat.equals("SGI")){
            return "Silicon Graphics";
        }
        else if(plat.equals("SUNW")){
            return "Sun Micro Systems";
        }
        else if(plat.equals("TGNT")){
            return "Taligent";
        }
        return "unknown";
    }
        String devClass(String plat) {
        if(plat.equals("scnr")){
            return "Scanner and Digital Camera";
        }
        else if(plat.equals("mntr")){
            return "Monitor";
        }
        else if(plat.equals("prtr")){
            return "Printer";
        }
        
        return "unknown";
    }

    private String getTagSignature(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToString(buf);       
    }
    private int getOffset(int pos,int len) {
        byte buf[]=new byte[4];
        for(int i=pos;i<pos+len;i++)
            buf[i-pos]=fileBuffer[i];
        
        return byteToInt(buf);     
    }

    private void viewTag(int index) {
        byte b[]=new byte[xprofile.tag[index].size];
           for(int i=0;i<xprofile.tag[index].size;i++)
               b[i]=fileBuffer[xprofile.tag[index].offset+i];
               
       if(xprofile.tag[index].signature.equals("rXYZ")){
            xprofile.setrXYZ(b, xprofile.tag[index].size);
       }
       else if(xprofile.tag[index].signature.equals("gXYZ")){
            xprofile.setgXYZ(b, xprofile.tag[index].size);
       }
       else if(xprofile.tag[index].signature.equals("bXYZ")){
            xprofile.setbXYZ(b, xprofile.tag[index].size);
       } 
       else if(xprofile.tag[index].signature.equals("wtpt")) {
           xprofile.setWhitePoint(b, xprofile.tag[index].size);
       }
       else if(xprofile.tag[index].signature.equals("bkpt")) {
           xprofile.setBlackPoint(b, xprofile.tag[index].size);
       }        
       else if(xprofile.tag[index].signature.equals("cprt")) {
           xprofile.setCopyRight(byteToString(b,8,xprofile.tag[index].size));
       }
       else if(xprofile.tag[index].signature.equals("desc")) {
           byte[] tmp=new byte[4];
           for(int i=0;i<4;i++) tmp[i]=b[i+8];
           xprofile.count=byteToInt(tmp);
           xprofile.setDescription(byteToString(b,12,xprofile.count));
       }        
       else if(xprofile.tag[index].signature.equals("rTRC")) {
            float[] tmpf;
            int count=0;
            byte[] tmp=new byte[4];
           for(int i=0;i<4;i++) tmp[i]=b[i+8];
            count=byteToInt(tmp);
            tmpf=new float[count];
            for(int i=0;i<count;i++) {
                for(int j=0;j<4;j++) tmp[j]=b[i*4+j];
                tmpf[i]=byteToFloat(tmp);
            }
            xprofile.setrTRC(count, tmpf);            
       } 
       else if(xprofile.tag[index].signature.equals("gTRC")) {
            float[] tmpf;
            int count=0;
            byte[] tmp=new byte[4];
           for(int i=0;i<4;i++) tmp[i]=b[i+8];
            count=byteToInt(tmp);
            tmpf=new float[count];
            for(int i=0;i<count;i++) {
                for(int j=0;j<4;j++) tmp[j]=b[i*4+j];
                tmpf[i]=byteToFloat(tmp);
            }
            xprofile.setgTRC(count, tmpf);            
       }   
       else if(xprofile.tag[index].signature.equals("bTRC")) {
            float[] tmpf;
            int count=0;
            byte[] tmp=new byte[4];
           for(int i=0;i<4;i++) tmp[i]=b[i+8];
            count=byteToInt(tmp);
            tmpf=new float[count];
            for(int i=0;i<count;i++) {
                for(int j=0;j<4;j++) tmp[j]=b[i*4+j];
                tmpf[i]=byteToFloat(tmp);
            }
            xprofile.setbTRC(count, tmpf);            
       }        
    }

}
