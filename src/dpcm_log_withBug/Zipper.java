package dpcm_log_withBug;

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
    int times=1;
    int i;
    int pre=first+65535;   //the point before
    for(i=1;i<length;i+=2) {   //skip the first point
      int one=data.get(i)+65535;
      int two=data.get(i+1)+65535;
      
      int log_diff_one=(int)(8*(Math.log(one)-Math.log(pre)));
      if(log_diff_one>7*times)
        log_diff_one=7*times;
      if(log_diff_one<-8*times)
        log_diff_one=-8*times;
      
//      int diff_one=one-pre;
//    
      log_diff_one+=times/2;   //四舍五入
//    
//      if(diff_one>7*times)
//        diff_one=7*times;
//      if(diff_one<-8*times)
//        diff_one=-8*times;
      
      log_diff_one+=8*times;   //变成正数处理
      
      log_diff_one=log_diff_one>>0;   //correct only when times is 2048.
      
      pre=(int)Math.exp(Math.log(pre)+(log_diff_one-8)*times);   //现在pre是one的预测值
      signal+=Math.pow(pre, 2);
      noise+=Math.pow((one-pre),2);
      
      int log_diff_two=(int)(8*(Math.log(two)-Math.log(pre)));
      
//      int diff_two=two-pre;
      log_diff_two+=times/2;
      
      if(log_diff_two>7*times)
        log_diff_two=7*times;
      if(log_diff_two<-8*times)
        log_diff_two=-8*times;
      
      log_diff_two+=8*times;
      
      log_diff_two=log_diff_two>>0;
      
      pre=(int)Math.exp(Math.log(pre)+(log_diff_two-8)*times);
      signal+=Math.pow(pre, 2);
      noise+=Math.pow((two-pre),2);
      
      byte twopoints=(byte)(((log_diff_one & 0x0F)<<4) | (log_diff_two & 0x0F));
      result.add(twopoints);
    }
    double snr=10*Math.log10(signal/noise);
    System.out.println("信噪比："+snr);
    
    return result;
  }
}
