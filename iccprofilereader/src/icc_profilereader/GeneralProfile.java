/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icc_profilereader;

/**
 *
 * @author Vicky
 */
public class GeneralProfile {
    ProfileTagHeader[] tag;
    String copyRightTag,descTag;
    int tagCount,count;
    
    GeneralProfile(int num) {
        tagCount=num;
        tag=new ProfileTagHeader[num];
    }
    
    void showDetails() {
        
    }
    
    void setCopyRight(String s){
        
    }
    void setDescription(String s){
        
    }
    void setWhitePoint(byte[] b,int len){
        
    }
    void setBlackPoint(byte[] b,int len){
        
    }
    void setrXYZ(byte[] b,int len){
        
    }
    void setgXYZ(byte[] b,int len){
        
    }
    void setbXYZ(byte[] b,int len){
        
    }     
    void setrTRC(int count,float[] val){
        
    }
    void setgTRC(int count,float[] val){
        
    }
    void setbTRC(int count,float[] val){
        
    }

}
