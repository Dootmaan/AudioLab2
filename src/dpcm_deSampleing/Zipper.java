package dpcm_deSampleing;

import java.util.ArrayList;
import java.util.List;

public class Zipper {
  public List<List<Integer>> data;

  public Zipper(List<List<Integer>> data) {
    this.data = data;
  }

  /**
   * Compress the audio, currently only support 16bit file
   * 
   * @return
   */
  public List<Byte> zip() {
    List<Byte> result = new ArrayList<>();
    double noise=0;
    double signal=0;
    for (List<Integer> data : this.data) {
      int first=data.get(0);  //both the origin and the after-change
      
      //dont forget that the first two bytes are the first point!!!!!!!!!
      result.add((byte) ((first&0x0000FF00)>>8));
      result.add((byte) (first&0x000000FF));
      
      int length=data.size();
    //the eliminator is 2.
      int times=2048;
      int i;
      int pre=first;   //the point before
      for(i=1;i<length-3;i+=4) {   //skip the first point
        int one=data.get(i+1);
        int two=data.get(i+3);
        
        int diff_one=one-pre;
      
        diff_one+=times/2;   //四舍五入
      
        if(diff_one>7*times)
          diff_one=7*times;
        if(diff_one<-8*times)
          diff_one=-8*times;
        
        diff_one+=8*times;   //变成正数处理
        
        diff_one=diff_one>>11;   //correct only when times is 2048.
        
        pre=pre+(diff_one-8)*times;
        signal+=Math.pow(pre, 2);
        noise+=Math.pow((one-pre),2);
        
        int diff_two=two-pre;
        
        diff_two+=times/2;
        
        if(diff_two>7*times)
          diff_two=7*times;
        if(diff_two<-8*times)
          diff_two=-8*times;
        
        diff_two+=8*times;
        
        diff_two=diff_two>>11;
        
        pre=pre+(diff_two-8)*times;
        signal+=Math.pow(pre, 2);
        noise+=Math.pow((two-pre),2);
        
        byte twopoints=(byte)(((diff_one & 0x0F)<<4) | (diff_two & 0x0F));
        result.add(twopoints);
      }
    }
    double snr=10*Math.log10(signal/noise);
    System.out.println("信噪比："+snr);
    
    return result;
  }
}
