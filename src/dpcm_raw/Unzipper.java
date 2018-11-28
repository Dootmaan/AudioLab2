package dpcm_raw;

import java.util.ArrayList;
import java.util.List;

public class Unzipper {
  private byte[] data;
  
  public Unzipper(byte[] data) {
    this.data=data;
  }
  
  public List<Integer> unzip(){
    List<Integer> result=new ArrayList<>();
    int first=(data[0] & 0x0000FF00) | (((int) data[1]));  //attention
    
    result.add(first);
    
    int length=data.length;
    int times=2048;
    int i;
    int pre=first;
    for(i=2;i<length;i++) {
      byte twopoints=data[i];
      int diff_one=(int)((twopoints&0xF0)>>4)&0x0000000F;
      int diff_two=(int)(twopoints&0x0000000F);
      
      diff_one=diff_one*times;   //À©´ó
      diff_two=diff_two*times;
      
      int one=(pre+diff_one-8*(times));   //minus 7 can get the best result for some unknown reason
      pre=one;
      int two=(pre+diff_two-8*(times));
      pre=two;
      
      result.add(one);
      result.add(two);
    }
    
    return result;
  }
}
