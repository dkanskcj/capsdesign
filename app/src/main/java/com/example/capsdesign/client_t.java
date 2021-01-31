package com.example.capsdesign;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import static java.lang.System.exit;

public class client_t{
    Socket socket;
    //  클라이언트 시작
    public static void main(String[] args) {
        new client_t().start();
    }
    //Client 정의
    private client_t() {
        try {
//          host 는 localhost, 포트는 5555번으로 연결
            socket = new Socket("localhost", 5555);
            System.out.println(.getTime() + "서버에 연결되었습니다...");
//            서버가 닫혀있다면 종료
        } catch (ConnectException ce) {
            System.out.println("서버가 동작하지 않아 클라이언트를 종료합니다...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Client를 실행하고 문자열을 보내기 위한 스레드 생성
    private void start() {
        Thread sender = new Thread(new ClientSender(socket));
        sender.start();
    }
    //   서버에게 문자열을 보내는 스레드
    static class ClientSender extends Thread {
        Socket socket;
        PrintWriter pw;
        BufferedReader br;

        ClientSender(Socket socket) {
            this.socket = socket;
            try {
                pw = new PrintWriter(socket.getOutputStream());
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (SocketException se) {
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //스레드 구동
        public void run() {
            try {
//              입력할 문자열을 System.in 즉 키보드로 입력받음
                BufferedReader insert = new BufferedReader(new InputStreamReader(System.in));
//              문자열을 대입할 문자열 len
                String len;
                System.out.print(Server.getTime() + "문자열을 입력하세요 : ");
//              len에다가 키보드로 입력한 문자열 입력
                len = insert.readLine();
//                입력된 문자열이 exit이라면 종료
                if (len.equals("exit"))
                {
//                      서버에게 exit이라는 문자열을 보냄-> 이 경우는 문자열의 길이를 받지 않음
                    pw.println(len);
                    pw.flush();
                    System.out.println("Client를 종료하겠습니다");
                    pw.close();
                    br.close();
                    insert.close();
                    socket.close();
                    exit(0);
                }
                if(len.equals("Server exit"))
                {
                    pw.println(len);
                    pw.flush();
                    System.out.println("서버와 클라이언트를 종료하겠습니다.");
                    pw.close();
                    br.close();
                    insert.close();
                    socket.close();
                    exit(0);
                }
                else
                {
//                      exit이 아니라면 다른 문자열들을 서버로 보내서 문자열 길이를 받고 종료한다.
                    pw.println(len);
                    pw.flush();
                    System.out.println(Server.getTime() + "서버로부터 받은 문자열의 길이 : " + br.readLine());
                    pw.close();
                    br.close();
                    socket.close();
                    exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

// 머신러닝의 판독 결과가 50% 넘을 경우 스팸