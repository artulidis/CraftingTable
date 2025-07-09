public class JavaSockets {
    public static void main(String args[]) {
        String host = args[0];
        String port = args[1];

        Server.main(new String[] {port});
        Client.main(new String[] {host, port});
    }
}
