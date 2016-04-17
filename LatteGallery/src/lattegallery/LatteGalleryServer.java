/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lattegallery;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author LaiFany
 */
public class LatteGalleryServer extends JFrame{

    static Connection connection;
    static Statement s;
    static JList customerNameList;
    static JTextArea jta;
    static DataInputStream inputFromClient;
    static DataOutputStream outputToClient;
    
    public LatteGalleryServer(){
        jta = new JTextArea();
        JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(jsp);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        LatteGalleryServer lg = new LatteGalleryServer();
        lg.setSize(1000, 400);
        lg.setVisible(true);
        lg.setTitle("Latte Gallery");
        
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            System.out.println("sun.jdbc.odbc.JdbcOdbcDriver found");
            connection = DriverManager.getConnection("jdbc:odbc:LatteGalleryDS");
            if(connection != null){
                s = connection.createStatement();
                System.out.println("there is a connection");
                
                //create tables if not exist
                if(!sqlOperations.checkTableExist("Customer")){
                    s.executeUpdate("CREATE TABLE Customer(customerID AUTOINCREMENT,"
                            + "name varchar(100) NOT NULL,"
                            + "phoneNumber varchar(20) NOT NULL,"
                            + "address varchar(255) NOT NULL,"
                            + "artPurchases varchar(255) NOT NULL,"
                            + "artistPref varchar(255) NOT NULL,"
                            + "PRIMARY KEY(customerID))");
                }
                if(!sqlOperations.checkTableExist("Artist")){
                    s.executeUpdate("CREATE TABLE Artist(artistID AUTOINCREMENT,"
                            + "name varchar(100) NOT NULL,"
                            + "specialty varchar(255) NOT NULL,"
                            + "alive varchar(50) NOT NULL,"
                            + "priceRange varchar(255) NOT NULL,"
                            + "PRIMARY KEY(artistID))");
                }
                if(!sqlOperations.checkTableExist("Artwork")){
                    s.executeUpdate("CREATE TABLE Artwork(artworkID AUTOINCREMENT,"
                            + "title varchar(255) NOT NULL,"
                            + "datePurchased varchar(100) NOT NULL,"
                            + "dateSold varchar(100) NOT NULL,"
                            + "artist varchar(255) NOT NULL,"
                            + "PRIMARY KEY(artworkID))");
                }
                
                try {
                    // Create a server socket
                    ServerSocket serverSocket = new ServerSocket(8000);
                    jta.append("Latte Gallery Server started at " + new Date() + '\n');

                    // Number a client
                    int clientNo = 1;

                    while (true) {
                        // Listen for a connection request
                        Socket socket = serverSocket.accept();

                        // Display the client number
                        jta.append("Starting thread for client " + clientNo +
                          " at " + new Date() + '\n');

                        // Find the client's host name, and IP address
                        InetAddress inetAddress = socket.getInetAddress();
                        jta.append("Client " + clientNo + "'s host name is "
                          + inetAddress.getHostName() + "\n");
                        jta.append("Client " + clientNo + "'s IP Address is "
                          + inetAddress.getHostAddress() + "\n");
                        
                        // Create a new thread for the connection
                        HandleAClient task = new HandleAClient(socket);

                        // Start the new thread
                        new Thread(task).start();

                        // Increment clientNo
                    clientNo++;
                    }
                  }
                  catch(IOException ex) {
                    System.err.println(ex);
                  }
            }
          } catch (ClassNotFoundException cnfe) {
            System.out.println("Error: sun.jdbc.odbc.JdbcOdbcDriver not found");
          }catch(SQLException e){
              e.printStackTrace();
          }
    }
    
    // Define the thread class for handling new connection
    static class HandleAClient implements Runnable {
      private Socket socket; // A connected socket

      /** Construct a thread */
      public HandleAClient(Socket socket) {
        this.socket = socket;
      }

      /** Run a thread */
      public void run() {
        try {
          // Create data input and output streams
          inputFromClient = new DataInputStream(
            socket.getInputStream());
          outputToClient = new DataOutputStream(
            socket.getOutputStream());
            //post result of customer table to jlist

          // Continuously serve the client
          while (true) {
            // Receive radius from the client
            String operation = inputFromClient.readUTF();

            if(operation.equals("insertCustomer")){
                String name = inputFromClient.readUTF();
                String phoneNumber = inputFromClient.readUTF();
                String address = inputFromClient.readUTF();
                String artPurchases = inputFromClient.readUTF();
                String artistPref = inputFromClient.readUTF();
                sqlOperations.insertCustomer(name, phoneNumber, address, artistPref);
            }else if(operation.equals("updateCustomer")){
                String name = inputFromClient.readUTF();
                String phoneNumber = inputFromClient.readUTF();
                String address = inputFromClient.readUTF();
                String artPurchases = inputFromClient.readUTF();
                String artistPref = inputFromClient.readUTF();
                String newName = inputFromClient.readUTF();
                sqlOperations.updateCustomer(name, phoneNumber, address, artistPref, newName);
            }else if(operation.equals("deleteCustomer")){
                String name = inputFromClient.readUTF();
                String phoneNumber = inputFromClient.readUTF();
                String address = inputFromClient.readUTF();
                String artPurchases = inputFromClient.readUTF();
                String artistPref = inputFromClient.readUTF();
                sqlOperations.deleteCustomer(name, phoneNumber, address, artPurchases, artistPref);
            }else if(operation.equals("insertArtwork")){
                String title = inputFromClient.readUTF();
                String datePurchased = inputFromClient.readUTF();
                String dateSold = inputFromClient.readUTF();
                String artist = inputFromClient.readUTF();
                sqlOperations.insertArtwork(title, datePurchased, dateSold, artist);
            }else if(operation.equals("updateArtwork")){
                String title = inputFromClient.readUTF();
                String datePurchased = inputFromClient.readUTF();
                String dateSold = inputFromClient.readUTF();
                String artist = inputFromClient.readUTF();
                String newTitle = inputFromClient.readUTF();
                sqlOperations.updateArtwork(title, datePurchased, dateSold, artist, newTitle);
            }else if(operation.equals("deleteArtwork")){
                String title = inputFromClient.readUTF();
                String datePurchased = inputFromClient.readUTF();
                String dateSold = inputFromClient.readUTF();
                String artist = inputFromClient.readUTF();
                sqlOperations.deleteArtwork(title, datePurchased, dateSold, artist);
            }else if(operation.equals("insertArtist")){
                String name = inputFromClient.readUTF();
                String specialty = inputFromClient.readUTF();
                String alive = inputFromClient.readUTF();
                String priceRange = inputFromClient.readUTF();
                sqlOperations.insertArtist(name, specialty, alive, priceRange);
            }else if(operation.equals("updateArtist")){
                String name = inputFromClient.readUTF();
                String specialty = inputFromClient.readUTF();
                String alive = inputFromClient.readUTF();
                String priceRange = inputFromClient.readUTF();
                String newName = inputFromClient.readUTF();
                sqlOperations.updateArtist(name, specialty, alive, priceRange, newName);
            }else if(operation.equals("deleteArtist")){
                String name = inputFromClient.readUTF();
                String specialty = inputFromClient.readUTF();
                String alive = inputFromClient.readUTF();
                String priceRange = inputFromClient.readUTF();
                sqlOperations.deleteArtist(name, specialty, alive, priceRange);
            }else if(operation.equals("Customer")){
                returnList(operation, "name");
                returnList(operation, "phoneNumber");
                returnList(operation, "address");
                returnList(operation, "artPurchases");
                returnList(operation, "artistPref");
            }else if(operation.equals("Artist")){
                returnList(operation, "name");
                returnList(operation, "specialty");
                returnList(operation, "alive");
                returnList(operation, "priceRange");
            }else if(operation.equals("Artwork")){
                returnList(operation, "title");
                returnList(operation, "datePurchased");
                returnList(operation, "dateSold");
                returnList(operation, "artist");
            }else if(operation.equals("getArtworkByArtist")){
                String artist = inputFromClient.readUTF();
                returnArtworkList(artist, "title");
                returnArtworkList(artist, "datePurchased");
                returnArtworkList(artist, "dateSold");
            }else if(operation.equals("getAvailableArtwork")){
                returnAvailableArtworkList("title");
                returnAvailableArtworkList("datePurchased");
                returnAvailableArtworkList("artist");
            }else if(operation.equals("purchaseArtwork")){
                String name = inputFromClient.readUTF();
                String artPurchases = inputFromClient.readUTF();
                String title = inputFromClient.readUTF();
                String dateSold = inputFromClient.readUTF();
                sqlOperations.purchaseArtwork(name, artPurchases, title, dateSold);
            }else if(operation.equals("getCustomerByArtwork")){
                String title = inputFromClient.readUTF();
                returnCustomerByArtwork(title);
            }else if(operation.equals("refreshArtistList")){
                String alive = inputFromClient.readUTF();
                returnArtistListByAlive("name", alive);
                returnArtistListByAlive("specialty", alive);
                returnArtistListByAlive("alive", alive);
                returnArtistListByAlive("priceRange", alive);
            }else if(operation.equals("getSoldArtwork")){
//                returnSoldArtworkList("title");
//                returnSoldArtworkList("datePurchased");
//                returnSoldArtworkList("dateSold");
//                returnSoldArtworkList("artist");
            }
          }
        }
        catch(IOException e) {
          System.err.println(e);
        }
      }
      
      public void returnList(String table, String col){
          String list = "";
//          String param = "";
          try{
                String id = "";
                if(table.equals("Customer")){
                    id = "customerID";
                }else if(table.equals("Artwork")){
                    id = "artworkID";
                }else if(table.equals("Artist")){
                    id = "artistID";
                }
                ResultSet rs = s.executeQuery("SELECT " + col + " FROM " + table + " ORDER BY " + id + " DESC");
                while(rs.next()){
                    if(list.equals("")){
                        list = rs.getString(1);
                    }else{
                        list += "|" + rs.getString(1);
                    }
                }
                System.out.println("sending list to client");
                outputToClient.writeUTF(list);
                System.out.println("sent list to client");
            }catch(SQLException e){
              e.printStackTrace();
            }catch (IOException ex) {
              Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
      
      public void returnArtworkList(String artist, String col){
          String list = "";
          try{
                ResultSet rs = s.executeQuery("SELECT " + col + " FROM Artwork WHERE artist = '" + artist + "' ORDER BY artworkID DESC");
                while(rs.next()){
                    if(list.equals("")){
                        list = rs.getString(1);
                    }else{
                        list += "|" + rs.getString(1);
                    }
                }
                System.out.println("sending list to client");
                outputToClient.writeUTF(list);
                System.out.println("sent list to client");
            }catch(SQLException e){
              e.printStackTrace();
            }catch (IOException ex) {
              Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
      
      public void returnAvailableArtworkList(String col){
          String list = "";
          try{
                ResultSet rs = s.executeQuery("SELECT " + col + " FROM Artwork WHERE dateSold = 'null' ORDER BY artworkID DESC");
                while(rs.next()){
                    if(list.equals("")){
                        list = rs.getString(1);
                    }else{
                        list += "|" + rs.getString(1);
                    }
                }
                System.out.println("sending list to client");
                outputToClient.writeUTF(list);
                System.out.println("sent list to client");
            }catch(SQLException e){
              e.printStackTrace();
            }catch (IOException ex) {
              Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
      
      public void returnCustomerByArtwork(String title){
          String customer = "null";
          try{
                ResultSet rs = s.executeQuery("SELECT * FROM Customer ORDER BY customerID DESC");
                while(rs.next()){
                    if(rs.getString("artPurchases").contains(title)){
                        //System.out.println(rs.getString("artPurchases") + " " + rs.getString("name"));
                        customer = rs.getString("name");
                        break;
                    }
                }
                System.out.println("sending customer to client");
                outputToClient.writeUTF(customer);
                System.out.println("sent customer to client");
            }catch(SQLException e){
              e.printStackTrace();
            }catch (IOException ex) {
              Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
      
      public void returnArtistListByAlive(String col, String alive){
          String list = "";
          try{
                ResultSet rs = s.executeQuery("SELECT " + col + " FROM Artist WHERE alive='" + alive + "' ORDER BY artistID DESC");
                while(rs.next()){
                    if(list.equals("")){
                        list = rs.getString(1);
                    }else{
                        list += "|" + rs.getString(1);
                    }
                }
                System.out.println("sending list to client");
                outputToClient.writeUTF(list);
                System.out.println("sent list to client");
            }catch(SQLException e){
              e.printStackTrace();
            }catch (IOException ex) {
              Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
      
      public void returnSoldArtworkList(String col){
          String list = "";
          try{
                ResultSet rs = s.executeQuery("SELECT * FROM Artwork ORDER BY artworkID DESC");
                while(rs.next()){
                    if(!rs.getString("dateSold").contains("null")){
                        if(list.equals("")){
                            list = rs.getString(col);
                        }else{
                            list += "|" + rs.getString(col);
                        }
                    }
                }
                System.out.println("sending list to client");
                outputToClient.writeUTF(list);
                System.out.println("sent list to client");
            }catch(SQLException e){
              e.printStackTrace();
            }catch (IOException ex) {
              Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
    }
    
    static class sqlOperations{
        
        public static boolean checkTableExist(String table){
            try {
                s = connection.createStatement();
                s.executeQuery("SELECT * FROM " + table);
                System.out.println(table + " exist");
                return true;
            } catch (SQLException ex) {
                  return false;
            }
        }
        
        public static void insertCustomer(String name, String phoneNumber, String address, String artistPref){
            try{
                s = connection.createStatement();
                s.executeUpdate("INSERT INTO Customer(name, phoneNumber, address, artistPref) VALUES ('" + name + "', '" + phoneNumber + "', '" + address + "', '" + artistPref + "')");
                jta.append("inserted customer name : " + name + ", phone no. : " + phoneNumber + ", address : " + address + ", artist Preference : " + artistPref + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        public static void insertArtwork(String title, String datePurchased, String dateSold, String artist){
            try{
                s = connection.createStatement();
                s.executeUpdate("INSERT INTO Artwork(title, datePurchased, dateSold, artist) VALUES ('" + title + "', '" + datePurchased + "', '" + dateSold + "', '" + artist + "')");
                jta.append("inserted artwork title : " + title + ", datePurchased : " + datePurchased + ", dateSold : " + dateSold + ", artist : " + artist + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        public static void insertArtist(String name, String specialty, String alive, String priceRange){
            try{
                s = connection.createStatement();
                s.executeUpdate("INSERT INTO Artist(name, specialty, alive, priceRange) VALUES ('" + name + "', '" + specialty + "', '" + alive + "', '" + priceRange + "')");
                jta.append("inserted artist name : " + name + ", specialty : " + specialty + ", alive : " + alive + ", priceRange : " + priceRange + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        public static void updateCustomer(String name, String phoneNumber, String address, String artistPref, String newName){
            try{
                    s = connection.createStatement();
                    s.executeUpdate("UPDATE Customer SET name='" + newName + "', phoneNumber='" + phoneNumber + "', address='" + address + "', artistPref='" + artistPref + "' WHERE name='" + name + "'");
                    jta.append("updated customer name : " + newName + ", phone no. : " + phoneNumber + ", address : " + address + ", artist Preference : " + artistPref + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        
        public static void updateArtwork(String title, String datePurchased, String dateSold, String artist, String newTitle){
            try{
                    s = connection.createStatement();
                    s.executeUpdate("UPDATE Artwork SET title='" + newTitle + "', datePurchased='" + datePurchased + "', dateSold='" + dateSold + "', artist='" + artist + "' WHERE title='" + title + "'");
                    jta.append("updated artwork title : " + newTitle + ", datePurchased : " + datePurchased + ", dateSold : " + dateSold + ", artist : " + artist + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        
        public static void updateArtist(String name, String specialty, String alive, String priceRange, String newName){
            try{
                    s = connection.createStatement();
                    s.executeUpdate("UPDATE Artist SET name='" + newName + "', specialty='" + specialty + "', alive='" + alive + "', priceRange='" + priceRange + "' WHERE name='" + name + "'");
                    jta.append("inserted artist name : " + newName + ", specialty : " + specialty + ", alive : " + alive + ", priceRange : " + priceRange + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        
        public static void deleteCustomer(String name, String phoneNumber, String address, String artPurchases, String artistPref){
            try{
                    s = connection.createStatement();
                    //s.executeUpdate("DELETE FROM Customer WHERE name='" + name + "', phoneNumber='" + phoneNumber + "', address='" + address + "', artPurchases='" + artPurchases + "', artistPref='" + artistPref + "'");
                    s.executeUpdate("DELETE FROM Customer WHERE name='" + name + "'");
                    jta.append("deleted customer name : " + name + ", phone no. : " + phoneNumber + ", address : " + address + ", art purchases : " + artPurchases + ", artist Preference : " + artistPref + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        
        public static void deleteArtwork(String title, String datePurchased, String dateSold, String artist){
            try{
                    s = connection.createStatement();
                    //s.executeUpdate("DELETE FROM Artwork WHERE title='" + title + "', datePurchased='" + datePurchased + "', dateSold='" + dateSold + "', artistID='" + artistID + "'");
                    s.executeUpdate("DELETE FROM Artwork WHERE title='" + title + "'");
                    jta.append("deleted artwork title : " + title + ", datePurchased : " + datePurchased + ", dateSold : " + dateSold + ", artist : " + artist + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        
        public static void deleteArtist(String name, String specialty, String alive, String priceRange){
            try{
                    s = connection.createStatement();
                    //s.executeUpdate("DELETE FROM Artist WHERE name='" + name + "', specialty='" + specialty + "', alive='" + alive + "', priceRange='" + priceRange + "'");
                    s.executeUpdate("DELETE FROM Artist WHERE name='" + name + "'");
                    jta.append("deleted artist name : " + name + ", specialty : " + specialty + ", alive : " + alive + ", priceRange : " + priceRange + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        
        public static void purchaseArtwork(String name, String artPurchases, String title, String dateSold){
            try{
                    s = connection.createStatement();
                    s.executeUpdate("UPDATE Customer SET artPurchases='" + artPurchases + "' WHERE name='" + name + "'");
                    jta.append("updated customer name : " + name + ", art purchases : " + artPurchases + "\n");
                    s.executeUpdate("UPDATE Artwork SET dateSold='" + dateSold + "' WHERE title='" + title + "'");
                    jta.append("updated artwork title : " + title + ", dateSold : " + dateSold + "\n");
            }catch(SQLException e){
                Logger.getLogger(LatteGalleryServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
}
