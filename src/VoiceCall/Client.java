package VoiceCall;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("서버에 연결되었습니다.");

            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            // 오디오 라인 생성
            DataLine.Info microphoneInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(microphoneInfo);
            microphone.open(audioFormat);
            microphone.start();

            // 클라이언트에서 서버로 오디오 데이터 전송
            byte[] buffer = new byte[4096];
            OutputStream outputStream = socket.getOutputStream();
            int bytesRead;
            while ((bytesRead = microphone.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            microphone.stop();
            microphone.close();
            outputStream.close();
            socket.close();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
