/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icc_profilereader;

/**
 *
 * @author Vicky
 */
public class ProfileTagHeader {
int tagNumber,offset,size;
String signature;

public void setTagNumber(int num) {
    tagNumber = num;
}

public void setSignature(String sign) {
    signature = sign;
}

public void setOffset(int num) {
    offset = num;
}

public void setSize(int num) {
    size = num;
}

public int getTagNumber() {
    return tagNumber;
}

public String getSignature() {
    return signature;
}

public int getOffset() {
    return offset;
}

public int getSize() {
    return size;
}

public String getProfileString() {
    return (" "+tagNumber+"\t"+signature +"\t"+ String.valueOf(offset)+"\t"+String.valueOf(size));
}

}
