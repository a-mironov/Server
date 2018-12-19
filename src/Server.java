import java.io.*;
import java.net.ServerSocket;
import java.nio.file.*;
import java.net.Socket;
import java.util.regex.Pattern;

    public class Server {
        public static void main(String[] args) throws IOException {
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                System.out.println("Waiting for a client connection...");

                Socket clientSocket = serverSocket.accept();
                Server server = new Server(clientSocket);
                System.out.println("Client has connected successfully");
                server.readInputStream();
                server.writeOutputStream();
                server.clientSocket.close();
            }
        }
        public Socket clientSocket;
        private InputStream inputStream;
        private OutputStream outputStream;
        private String fileName;

        public Server(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
            //this.fileName = "site.html";
        }

        public void readInputStream() throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String s = in.readLine();
                if(s == null || s.trim().length() == 0)
                    break;
                sb.append(s);
                sb.append('\n');
                if(s.contains(".html")) {
                    this.fileName = s.substring(s.indexOf("File") + 4, s.length() - s.indexOf("File") - 1);
                }
            }

        }
        //String name_of_File = in.findInLine(Pattern.compile(".....html"));
        public void writeOutputStream() throws IOException {
            File file = new File(fileName);

            if (file.exists()) {        //reading "site.html"
                String s = new String(Files.readAllBytes(Paths.get(fileName)));
                String response = "HTTP/1.1 200 OK\n" +
                        "Type: text/html\n" +
                        "Length: " + s.length() + "\n" +
                        "Closing connection\n\n" + s;
                outputStream.write(response.getBytes());
                outputStream.flush();
            }
            else {
                outputStream.write("<html><h2>404:file not found</h2></html>\n".getBytes());
                outputStream.flush();
                outputStream.write(("File \"" + fileName + "\"  does not exist").getBytes());        //wrong name
                outputStream.flush();
            }

        }
    }
