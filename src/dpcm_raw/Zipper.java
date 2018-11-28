package dpcm_raw;

import java.util.ArrayList;
import java.util.List;

public class Zipper {
  public List<Integer> data;
  
  public Zipper(List<Integer> data) {
    this.data=data;
  }
  
  /**
   * Compress the audio, currently only support 16bit file
   * @return
   */
  public List<Byte> zip() {
    int first=data.get(0);  //both the origin and the after-change
    double noise=0;
    double signal=0;
    List<Byte> result=new ArrayList<>();
    
    //dont forget that the first two bytes are the first point!!!!!!!!!
    result.add((byte) ((first&0x0000FF00)>>8));
    result.add((byte) (first&0x000000FF));
    
    int length=data.size();
  //the eliminator is 2.
    int times=2048;
    int i;
    int pre=first;   //the point before
    for(i=1;i<length;i+=2) {   //skip the first point
      int one=data.get(i);
      int two=data.get(i+1);
      
      int diff_one=one-pre;
      pre=one;
      int diff_two=two-pre;
      pre=two;
      
      diff_one+=times/2;   //四舍五入
      diff_two+=times/2;
      
      if(diff_one>7*times)
        diff_one=7*times;
      if(diff_one<-8*times)
        diff_one=-8*times;
      
      if(diff_two>7*times)
        diff_two=7*times;
      if(diff_two<-8*times)
        diff_two=-8*times;
      
      noise+=Math.pow(diff_one,2);
      noise+=Math.pow(diff_two,2);
      
      diff_one+=8*times;   //变成正数处理
      diff_two+=8*times;
      
      diff_one=diff_one>>11;   //correct only when times is 2048.
      diff_two=diff_two>>11;
      
      byte twopoints=(byte)(((diff_one & 0x0F)<<4) | (diff_two & 0x0F));
      result.add(twopoints);
    }
    double snr=10*Math.log10(signal/noise);
    System.out.println("信噪比："+snr);
    return result;
  }
}
