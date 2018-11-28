package dpcm_raw;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      // Start zipping
      AudioReader reader =
          new AudioReader("C:\\Users\\samma\\Desktop\\Computer Vision and Audio\\Lab1\\”Ô¡œ\\1.wav");
      List<Integer> result = reader.readWithoutFrame();

      Zipper zipper = new Zipper(result);
      List<Byte> zipped = zipper.zip();

      AudioWriter writer = new AudioWriter(zipped, "D:\\1.dpc");
      writer.writeDPC();

      // Start unzipping
      FileInputStream input = new FileInputStream("D:\\1.dpc");
      byte[] zippedbyte = new byte[input.available()]; // max file 256K
      input.read(zippedbyte);
      input.close();
      Unzipper unzipper = new Unzipper(zippedbyte);
      List<Integer> unzipped = unzipper.unzip();

      List<Byte> unzippedbyte = new ArrayList<>();
      for (Integer i : unzipped) {
        byte byte1 = (byte) (i & 0xFF);
        byte byte2 = (byte) ((i >> 8) & 0xFF);
        unzippedbyte.add(byte1);
        unzippedbyte.add(byte2);
      }
      AudioWriter writer2 = new AudioWriter(unzippedbyte, "D:\\1.pcm");
      writer2.writePCM();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
