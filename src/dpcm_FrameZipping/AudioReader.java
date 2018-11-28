package dpcm_FrameZipping;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioReader {
  private String filename;

  private FileInputStream fis = null;
  private BufferedInputStream bis = null;

  public AudioReader(String filename) {
    this.filename = filename;
  }

  /**
   * read two bytes at a time, and turn them into an integer
   * 
   * @return the integer that these two bytes represents
   */
  private int readInt() {
    byte[] buf = new byte[2];
    int res = 0;
    try {
      if (bis.read(buf) != 2)
        throw new IOException("no more data!!!");
      res = (buf[0] & 0x000000FF) | (((int) buf[1]) << 8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return res;
  }

  /**
   * Read the .wav audio file(16bit, but with a few changes it can also support 32bit file) in bytes
   * and analyze all the sample points. These points are stored in a List
   * 
   * @param data
   * @return A list that stores all the points in several frames. Return an empty List if failure.
   */
  public List<List<Integer>> read() {
    List<List<Integer>> data=new ArrayList<>();
    try {
      fis = new FileInputStream(this.filename);
      bis = new BufferedInputStream(fis);
      bis.skip(44); // Ìø¹ýÍ·²¿
      while (bis.available() >= 512) {
        List<Integer> frame = new ArrayList<>();
        for (int i = 1; i <= 256; i++) {
          frame.add(this.readInt());
        }
        data.add(frame);
      }

      List<Integer> frame = new ArrayList<>();
      if (bis.available() > 0) {
        while (bis.available() > 0) {
          frame.add(this.readInt());
        }
        data.add(frame);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (bis != null)
          bis.close();
        if (fis != null)
          fis.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    return data;
  }
  
  /**
   * Use the read() method in this class to create a new List that stores all the points without
   * frames.
   * @return A list that stores all the points directly.
   */
  public List<Integer> readWithoutFrame() {
    List<List<Integer>> list=this.read();
    List<Integer> result=new ArrayList<>();
    for(List<Integer> l:list) {
      for(Integer i:l) {
        result.add(i);
      }
    }
    return result;
  }
}
