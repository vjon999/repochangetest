/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icc_profilereader;

/**
 *
 * @author Vicky
 */
class DisplayProfile extends GeneralProfile{
    XYZNumber rXYZ,gXYZ,bXYZ,whitePoint,blackPoint;
    float[] rTRC,gTRC,bTRC;
    DisplayProfile(int num){
        super(num);
        rXYZ=new XYZNumber();
        gXYZ=new XYZNumber();
        bXYZ=new XYZNumber();
        whitePoint=new XYZNumber();
        blackPoint=new XYZNumber();
    }
    @Override
    void showDetails(){
        String s[]=new String[super.tagCount];
        //s[0]=new String("");s[0]+=rXYZ.strXYZ;
        System.out.println(rXYZ.strXYZ);
        //s[1]=new String();s[1]=gXYZ.strXYZ;
        System.out.println(gXYZ.strXYZ);
        //s[2]=new String();s[2]=bXYZ.strXYZ;
        System.out.println(bXYZ.strXYZ);
        //s[3]=new String();s[3]="Black pt "+blackPoint.strXYZ;
        System.out.println(whitePoint.strXYZ);
        //s[4]=new String();s[4]="White pt "+whitePoint.strXYZ;
        System.out.println(blackPoint.strXYZ);
        //s[5]=new String();s[5]=super.copyRightTag;
        System.out.println(super.copyRightTag);
        //s[6]=new String();s[6]=super.descTag;
        System.out.println(super.descTag);
        //System.out.println();
    }
    
    @Override
    void setCopyRight(String s){
        copyRightTag=s;
    }
    @Override
    void setDescription(String s){
        descTag=s;
    }
    @Override
    void setWhitePoint(byte[] b,int len){
        whitePoint=new XYZNumber();
        whitePoint.setNumber(b, len);
    }
    @Override
    void setBlackPoint(byte[] b,int len){
        blackPoint=new XYZNumber();
        blackPoint.setNumber(b, len);
    }
    @Override
    void setrXYZ(byte[] b,int len){
        rXYZ=new XYZNumber();
        rXYZ.setNumber(b, len);
    }
    @Override
    void setgXYZ(byte[] b,int len){
        gXYZ=new XYZNumber();
        gXYZ.setNumber(b, len);
    }
    @Override
    void setbXYZ(byte[] b,int len){
        bXYZ=new XYZNumber();
        bXYZ.setNumber(b, len);
    }
    @Override
    void setrTRC(int count,float[] val){
        rTRC=new float[count];
        rTRC=val;
    }    
    @Override
    void setgTRC(int count,float[] val){
        gTRC=new float[count];
        gTRC=val;
    }
    @Override
    void setbTRC(int count,float[] val){
        bTRC=new float[count];
        bTRC=val;
    }

}
