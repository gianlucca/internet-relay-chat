import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender implements Runnable{
    private DatagramSocket socket;
    private String hostname;

    Sender(DatagramSocket socket, String hostname) {
        this.socket = socket;
        this.hostname = hostname;
    }

    private void sendMessage(String message) throws Exception {
        byte buf[] = message.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Client.PORT);
        socket.send(packet);
    }

    public void run() {
        do {
            try {
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                System.out.println(Messages.INSERT_USERNAME);
                String sentence = inFromUser.readLine();
                sendMessage(sentence);
            } catch (Exception e) {

            }
        } while (!Client.connected);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String newSentence = in.readLine();
                if(newSentence.startsWith("/")){
                    String[] command = newSentence.split(" ", 2);
                    newSentence = command[0].replaceFirst("/", "").toUpperCase() + " " + command[1];
                }
                sendMessage(newSentence);
                if (newSentence.toLowerCase().startsWith("/quit")){
                    break;
                }
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}
