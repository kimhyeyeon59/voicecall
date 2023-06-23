package VoiceCall;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;
public class AudioPlayer {
    // 서버에서 수신한 음성 데이터를 재생, 스피커로 출력
    public void play(InputStream audioStream) {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("오디오 라인이 지원되지 않습니다. 프로그램 실행을 중단합니다.");
                return;
            }

            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = audioStream.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.close();
            audioStream.close();

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
