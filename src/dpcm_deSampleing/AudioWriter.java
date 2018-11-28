package dpcm_deSampleing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AudioWriter {
  private List<Byte> data;
  String dest;

  public AudioWriter(List<Byte> data, String dest) {
    this.data = data;
    this.dest = dest;
  }

  public void writePCM() throws IOException {
    System.out.println("将PCM数据存储至：" + dest);
    if (!dest.contains(".pcm"))
      dest += ".pcm";
    FileOutputStream audiofile = new FileOutputStream(dest);

    for (Byte b : data) {
      audiofile.write(b);
    }
    audiofile.close();
  }

  public void writeDPC() throws IOException {
    System.out.println("将数据压缩存储至：" + dest);
    if (!dest.contains(".dpc"))
      dest += ".dpc";

    FileOutputStream audiofile = new FileOutputStream(dest);
    for (Byte b : data) {
      audiofile.write(b);
    }
    audiofile.close();
  }
}
