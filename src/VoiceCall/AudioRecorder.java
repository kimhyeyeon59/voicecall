package VoiceCall;

import java.io.IOException;
import java.io.OutputStream;
import javax.sound.sampled.*;

public class AudioRecorder {
    // 클라이언트에서 마이크로 입력한 음성 데이터를 서버로 전송하기 위해 녹음
    public void startRecording(OutputStream audioStream) {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("오디오 라인이 지원되지 않습니다. 프로그램 실행을 중단합니다.");
                return;
            }

            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while (true) {
                bytesRead = line.read(buffer, 0, buffer.length);
                audioStream.write(buffer, 0, bytesRead);
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}