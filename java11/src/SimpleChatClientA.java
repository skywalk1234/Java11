import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SimpleChatClientA {
    public static void main(String[] args) {
        new SimpleChatClientA().go();

    }
    public void go() {
        setUpNetworking();
    }

    private void setUpNetworking() {
        // open a SocketChannel to the server
        SocketChannel svr = null;
        try {
            svr = SocketChannel.open(new InetSocketAddress("192.168.118.128", 8080));
            //用open这个静态方法创建SocketChannel对象，传入一个包含IP地址和端口号的InetSocketAddress对象
            //用于连接服务器。
            System.out.println("连接成功");
            BufferedReader br = new BufferedReader(new InputStreamReader(svr.socket().getInputStream()));
            //BufferedReader对象用于接收服务器返回的字节流，转成字符流并包装。
            System.out.println("现在可以向服务器发送消息，输入-1退出");
            String input = " ";
            Scanner sc = new Scanner(System.in);
            //从终端中读入用户输入
            while (!"-1".equals(input)) {
                //如果这里用==，是比较两个字符串引用指向的内存地址是否相同，很显然不同（即使值都是“-1”）
                //String对象重写了equals方法用于比较两个字符串的内容是否相同，所以这里要用equals
                input = sc.nextLine();
                //该方法会读入一行文本直到遇到换行符，input本身不会包括换行符
                sendMessage(svr, input);
                System.out.println(br.readLine());
                //输出服务器返回的消息
            }
            System.out.println("与服务器的连接已断开....");
            svr.close();
            sc.close();
        } catch (IOException e) {
            System.out.println("与服务器交互失败");
            //throw new RuntimeException(e);
        }

    }
    public void sendMessage(SocketChannel svr, String msg) throws IOException {
        svr.write(StandardCharsets.UTF_8.encode(msg+"\n"));
        //先将字符串编码成字节数组，并通过write方法以字节流的形式写入到连接通道（SocketChannel）中
        //在服务端的readline方法是一个阻塞方法，需要读到换行符才会返回值，但是nextline方法不会读入换行符，需要手动添加
    }

}
