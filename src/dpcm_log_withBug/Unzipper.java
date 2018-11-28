package dpcm_log_withBug;

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
    int times=1;
    int i;
    int pre=first+65535;
    for(i=2;i<length;i++) {
      byte twopoints=data[i];
      double diff_one=(int)((twopoints&0xF0)>>4)&0x0000000F;
      double diff_two=(int)(twopoints&0x0000000F);
      
      diff_one=diff_one*times/8.0;   //À©´ó
      diff_two=diff_two*times/8.0;
      
//      int one=(pre+diff_one-8*(times));   //minus 7 can get the best result for some unknown reason
      int log_one=(int)Math.exp(Math.log(pre)+diff_one-8*times);
      pre=log_one;
//      int two=(pre+diff_two-8*(times));
      int log_two=(int)Math.exp(Math.log(pre)+diff_two-8*times);
      pre=log_two;
      
      result.add(log_one-65535);
      result.add(log_two-65535);
    }
    return result;
  }
}
