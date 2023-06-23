package VoiceCall;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("서버가 시작되었습니다. 클라이언트의 연결을 대기합니다.");

            Socket clientSocket = serverSocket.accept();
            System.out.println("클라이언트가 연결되었습니다: " + clientSocket.getInetAddress().getHostAddress());

            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            // 오디오 라인 생성
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            speaker.open(audioFormat);
            speaker.start();

            // 클라이언트로부터 오디오 데이터 수신
            byte[] buffer = new byte[4096];
            InputStream inputStream = clientSocket.getInputStream();
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                speaker.write(buffer, 0, bytesRead);
            }

            speaker.drain();
            speaker.close();
            inputStream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
