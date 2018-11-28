package dpcm_deSampleing;

import java.util.ArrayList;
import java.util.List;

public class Unzipper {
  private byte[] data;
  
  public Unzipper(byte[] data) {
    this.data=data;
  }
  
  public List<Integer> unzip(){
    List<Integer> result=new ArrayList<>();
    int t=0;
    int total_length=data.length;
    while(t<total_length) {
      int q=0;
      byte[] data=new byte[65];
      data[q]=this.data[t]; //第一个一定要加上
      t++;
      q++;
      while(q<65 && t<total_length) {
        data[q]=this.data[t];
        q++;
        t++;
      }
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
      
      diff_one=diff_one*times;   //扩大
      diff_two=diff_two*times;
      
      int one=(pre+diff_one-8*(times));   //minus 7 can get the best result for some unknown reason
      int half=(one+pre)/2;
      pre=one;
      int two=(pre+diff_two-8*(times));
      int onehalf=(two+pre)/2;
      pre=two;
      
      result.add(half);
      result.add(one);
      result.add(onehalf);
      result.add(two);
    }
    }
    return result;
  }
}
