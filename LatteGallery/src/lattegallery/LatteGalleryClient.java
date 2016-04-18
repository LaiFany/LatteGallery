/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lattegallery;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import static lattegallery.LatteGalleryServer.customerNameList;
import static lattegallery.LatteGalleryServer.s;

/**
 *
 * @author LaiFany
 */
public class LatteGalleryClient extends JFrame{
    
    static JList customerNameList = new JList();
    static JList artistNameList = new JList();
    static JList artworkTitleList = new JList();
    
    static JList artPurchasesList = new JList();
    
    static JList artistArtworksArtistNameList = new JList();
    static JList artistArtworksArtworkNameList = new JList();
    
    static JList availableArtworkTitleList = new JList();
    
    static JList archiveArtworkTitleList = new JList();
    
    //defaultlistmodels
    DefaultListModel customerListModel = new DefaultListModel();
    DefaultListModel artistListModel = new DefaultListModel();
    DefaultListModel artworkListModel = new DefaultListModel();
    
    DefaultListModel artPurchasesListModel = new DefaultListModel();
    
    DefaultListModel artistArtworksArtistListModel = new DefaultListModel();
    DefaultListModel artistArtworksArtworkListModel = new DefaultListModel();
    
    DefaultListModel availableArtworkTitleListModel = new DefaultListModel();
    
    DefaultListModel archiveArtworkTitleListModel = new DefaultListModel();
    
    // IO streams
    private static DataOutputStream toServer;
    private static DataInputStream fromServer;
    
    //customer list information
    static String[] customerListArr;
    static String[] phoneNumberArr;
    static String[] addressArr;
    static String[] artPurchasesArr;
    static String[] artistPrefArr;
    
    //artist list information
    static String[] artistListArr;
    static String[] specialtyArr;
    static String[] aliveArr;
    static String[] priceRangeArr;
    
    //artwork list information
    static String[] artworkListArr;
    static String[] datePurchasedArr;
    static String[] dateSoldArr;
    static String[] artistArr;
    static String[] purchasePriceArr;
    static String[] sellingPriceArr;
    
    //artwork by artist
    static String[] artistArtworkTitleArr;
    static String[] artistArtworkDatePurchasedArr;
    static String[] artistArtworkDateSoldArr;
    static String[] artistArtworksPurchasePriceArr;
    static String[] artistArtworksSellingPriceArr;
    
    //available artwork
    static String[] availableArtworkTitleArr;
    static String[] availableArtworkDatePurchasedArr;
    static String[] availableArtworkArtistArr;
    static String[] availableArtworkPurchasePriceArr;
    static String[] availableArtworkSellingPriceArr;
    
    //archive artwork
    static String[] archiveArtworkTitleArr;
    static String[] archiveArtworkDatePurchasedArr;
    static String[] archiveArtworkDateSoldArr;
    static String[] archiveArtworkArtistArr;
    static String[] archiveArtworkCustomerArr;
    static String[] archiveArtworkPurchasePriceArr;
    static String[] archiveArtworkSellingPriceArr;
    
    //dialog
    final JDialog warningDialog = new JDialog();
    JLabel warningMessageLabel = new JLabel();
    
    final JDialog editCustomerDialog = new JDialog();
    JLabel editCustomerMessageLabel = new JLabel();
    final JDialog editArtistDialog = new JDialog();
    JLabel editArtistMessageLabel = new JLabel();
    final JDialog editArtworkDialog = new JDialog();
    JLabel editArtworkMessageLabel = new JLabel();
    
    final JDialog deleteCustomerDialog = new JDialog();
    JLabel deleteCustomerMessageLabel = new JLabel();
    final JDialog deleteArtistDialog = new JDialog();
    JLabel deleteArtistMessageLabel = new JLabel();
    final JDialog deleteArtworkDialog = new JDialog();
    JLabel deleteArtworkMessageLabel = new JLabel();
    
    final JDialog availableArtworkPurchaseDialog = new JDialog();
    JLabel availableArtworkPurchaseMessageLabel = new JLabel();
    
    //customer tab artistPref jcombobox
    JComboBox artistPrefCB = new JComboBox();
    JComboBox artistPrefCB1 = new JComboBox();
    
    //artist tab alive jcombobox
    JComboBox aliveCB = new JComboBox();
    JComboBox aliveCB1 = new JComboBox();
    
    //artwork tab artist jcombobox
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    JComboBox artistCB = new JComboBox();
    JComboBox artistCB1 = new JComboBox();
    
    //current inventory tab customer jcombobox
    DefaultComboBoxModel customerModel = new DefaultComboBoxModel();
    JComboBox customerCB = new JComboBox();
    
    //get customer by artwork 3D string
    String[][][] customer3D;
    
    public LatteGalleryClient(){
        
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(
              socket.getInputStream());

            // Create an output stream to send data to the server
            toServer =
              new DataOutputStream(socket.getOutputStream());
            
            initializeCustomerList();
            initializeArtistList();
            initializeArtworkList();
            updateArtistComboBox();
            updateCustomerComboBox();
            
            customerNameList = new JList(customerListModel);
            artistNameList = new JList(artistListModel);
            artworkTitleList = new JList(artworkListModel);
            
            artPurchasesList = new JList(artPurchasesListModel);
            
            artistArtworksArtistNameList = new JList(artistArtworksArtistListModel);
            artistArtworksArtworkNameList = new JList(artistArtworksArtworkListModel);
            
            availableArtworkTitleList = new JList(availableArtworkTitleListModel);
            archiveArtworkTitleList = new JList(archiveArtworkTitleListModel);
            
          }
          catch (IOException ex) {
            ex.printStackTrace();
          }
        
        //start of dialog
        //warning dialog
        JPanel message = new JPanel();
        message.add(warningMessageLabel);
        
        JPanel button = new JPanel();
        JButton ok = new JButton("OK");
        
        ok.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               warningDialog.setVisible(false);
           }
        });
        
        button.add(ok);
        
        warningDialog.add(message, BorderLayout.CENTER);
        warningDialog.add(button, BorderLayout.SOUTH);
        
        //edit customer confirmation dialog
        JPanel message1 = new JPanel();
        message1.add(editCustomerMessageLabel);
        
        JPanel button1 = new JPanel();
        JButton yes1 = new JButton("Yes");
        JButton no1 = new JButton("No");
        
        button1.add(yes1);
        button1.add(no1);
        
        editCustomerDialog.add(message1, BorderLayout.CENTER);
        editCustomerDialog.add(button1, BorderLayout.SOUTH);
        
        //delete customer confirmation dialog
        JPanel message2 = new JPanel();
        message2.add(deleteCustomerMessageLabel);
        
        JPanel button2 = new JPanel();
        JButton yes2 = new JButton("Yes");
        JButton no2 = new JButton("No");
        
        button2.add(yes2);
        button2.add(no2);
        
        deleteCustomerDialog.add(message2, BorderLayout.CENTER);
        deleteCustomerDialog.add(button2, BorderLayout.SOUTH);
        
        //edit artist confirmation dialog
        JPanel message3 = new JPanel();
        message3.add(editArtistMessageLabel);
        
        JPanel button3 = new JPanel();
        JButton yes3 = new JButton("Yes");
        JButton no3 = new JButton("No");
        
        button3.add(yes3);
        button3.add(no3);
        
        editArtistDialog.add(message3, BorderLayout.CENTER);
        editArtistDialog.add(button3, BorderLayout.SOUTH);
        
        //delete artist confirmation dialog
        JPanel message4 = new JPanel();
        message4.add(deleteArtistMessageLabel);
        
        JPanel button4 = new JPanel();
        JButton yes4 = new JButton("Yes");
        JButton no4 = new JButton("No");
        
        button4.add(yes4);
        button4.add(no4);
        
        deleteArtistDialog.add(message4, BorderLayout.CENTER);
        deleteArtistDialog.add(button4, BorderLayout.SOUTH);
        
        //edit artwork confirmation dialog
        JPanel message5 = new JPanel();
        message5.add(editArtworkMessageLabel);
        
        JPanel button5 = new JPanel();
        JButton yes5 = new JButton("Yes");
        JButton no5 = new JButton("No");
        
        button5.add(yes5);
        button5.add(no5);
        
        editArtworkDialog.add(message5, BorderLayout.CENTER);
        editArtworkDialog.add(button5, BorderLayout.SOUTH);
        
        //delete artwork confirmation dialog
        JPanel message6 = new JPanel();
        message6.add(deleteArtworkMessageLabel);
        
        JPanel button6 = new JPanel();
        JButton yes6 = new JButton("Yes");
        JButton no6 = new JButton("No");
        
        button6.add(yes6);
        button6.add(no6);
        
        deleteArtworkDialog.add(message6, BorderLayout.CENTER);
        deleteArtworkDialog.add(button6, BorderLayout.SOUTH);
        
        //available artwork customer purchase confirmation dialog
        JPanel message7 = new JPanel();
        message7.add(availableArtworkPurchaseMessageLabel);
        
        JPanel button7 = new JPanel();
        JButton yes7 = new JButton("Yes");
        JButton no7 = new JButton("No");
        
        button7.add(yes7);
        button7.add(no7);
        
        availableArtworkPurchaseDialog.add(message7, BorderLayout.CENTER);
        availableArtworkPurchaseDialog.add(button7, BorderLayout.SOUTH);
        
        //end of dialog
        
        
        final JTabbedPane customerTabbedPane = new JTabbedPane();
        final JTabbedPane artistTabbedPane = new JTabbedPane();
        final JTabbedPane artworkTabbedPane = new JTabbedPane();
        final JTabbedPane inventoryTabbedPane = new JTabbedPane();
        JPanel viewCustomer = new JPanel();
        JPanel addCustomer = new JPanel();
        JPanel viewArtist = new JPanel();
        JPanel artistArtworks = new JPanel();
        JPanel addArtist = new JPanel();
        JPanel viewArtwork = new JPanel();
        JPanel addArtwork = new JPanel();
        JPanel currentInventory = new JPanel();
        JPanel archiveInventory = new JPanel();
        
        //start of viewCustomer
        //listview (left)
        JScrollPane customerJSP = new JScrollPane(customerNameList);
        viewCustomer.setLayout(new BorderLayout());
        viewCustomer.add(customerJSP, BorderLayout.WEST);
        customerJSP.setPreferredSize(new Dimension(150,80));
        
        //info panel (right)
        JLabel customerNameLabel = new JLabel("Name");
        JLabel phoneNoLabel = new JLabel("Phone Number");
        JLabel addressLabel = new JLabel("Address");
        JLabel artPurchasesLabel = new JLabel("Art Purchases");
        JLabel artistPrefLabel = new JLabel("Artist Preferences");
        
        final JTextField nameTF = new JTextField(50);
        final JTextField phoneNoTF = new JTextField(50);
        final JTextArea addressTA = new JTextArea(4, 3);
        JScrollPane artPurchasesJSP = new JScrollPane(artPurchasesList);
        //final JTextArea artPurchasesTA = new JTextArea(4, 3);
        
        final JButton customerEdit = new JButton("Edit");
        final JButton customerDelete = new JButton("Delete");
        
        artistPrefCB.setSelectedIndex(-1);
        
        JPanel namePanel = new JPanel();
        JPanel phoneNoPanel = new JPanel();
        JPanel addressPanel = new JPanel();
        JPanel artPurchasesPanel = new JPanel();
        JPanel artistPrefPanel = new JPanel();
        JPanel customerButtonPanel = new JPanel();
        
        namePanel.setLayout(new GridLayout(1, 2));
        namePanel.add(customerNameLabel);
        namePanel.add(nameTF);
        
        phoneNoPanel.setLayout(new GridLayout(1, 2));
        phoneNoPanel.add(phoneNoLabel);
        phoneNoPanel.add(phoneNoTF);
        
        addressPanel.setLayout(new GridLayout(1, 2));
        addressPanel.add(addressLabel);
        addressPanel.add(addressTA);
        
        artPurchasesPanel.setLayout(new GridLayout(1, 2));
        artPurchasesPanel.add(artPurchasesLabel);
        artPurchasesPanel.add(artPurchasesJSP);
        
        artistPrefPanel.setLayout(new GridLayout(1, 2));
        artistPrefPanel.add(artistPrefLabel);
        artistPrefPanel.add(artistPrefCB);
        
        customerButtonPanel.setLayout(new GridLayout(1, 2));
        customerButtonPanel.add(customerEdit);
        customerButtonPanel.add(customerDelete);
        
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(6, 1, 10, 10));
        jp.add(namePanel);
        jp.add(phoneNoPanel);
        jp.add(addressPanel);
        jp.add(artPurchasesPanel);
        jp.add(artistPrefPanel);
        jp.add(customerButtonPanel);
        
        viewCustomer.add(jp, BorderLayout.CENTER);
        
        //jlist listener
        customerNameList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    if(customerNameList.getSelectedIndex() != -1){
                        System.out.println(customerNameList.getSelectedIndex());
                        nameTF.setText(customerListArr[customerNameList.getSelectedIndex()]);
                        phoneNoTF.setText(phoneNumberArr[customerNameList.getSelectedIndex()]);
                        addressTA.setText(addressArr[customerNameList.getSelectedIndex()]);
                        //artPurchasesTA.setText(artPurchasesArr[customerNameList.getSelectedIndex()]);
                        for(int i = 0; i < artistPrefCB.getItemCount(); i++){
                            if(artistPrefCB.getItemAt(i).toString().equals(artistPrefArr[customerNameList.getSelectedIndex()])){
                                artistPrefCB.setSelectedIndex(i);
                                break;
                            }else if(i + 1 == artistPrefCB.getItemCount()){
                                artistPrefCB.setSelectedIndex(-1);
                            }
                        }
                        populateArtPurchasesList(customerNameList.getSelectedIndex());
                    }
                }
            }
        });
        
        customerEdit.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               if(nameTF.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Name' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(phoneNoTF.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Phone Number' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(!phoneNoTF.getText().trim().matches(".*\\d+.*")){
                   warningMessageLabel.setText("Invalid phone number format. Only numbers are allowed.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(addressTA.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Address' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(artistPrefCB.getSelectedIndex() == -1){
                   warningMessageLabel.setText("Please select preferred artist.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else{
                   for(int i = 0; i < customerListArr.length; i++){
                       if(i != customerNameList.getSelectedIndex()){
                           if(customerListArr[i].equals(nameTF.getText().trim())){
                                warningMessageLabel.setText("The customer name " + "'" + nameTF.getText().trim() + "' is already registered. Please try another name.");
                                warningDialog.pack();
                                warningDialog.setTitle("Warning");
                                warningDialog.setLocationRelativeTo(null);
                                warningDialog.setVisible(true);
                                break;
                            }else if(i + 1 == customerListArr.length){
                                editCustomerDialog.setVisible(true);
                                editCustomerMessageLabel.setText("You are about to edit customer '" + customerListArr[customerNameList.getSelectedIndex()] + "'. Proceed?");
                                editCustomerDialog.pack();
                                editCustomerDialog.setTitle("Editing Customer");
                                editCustomerDialog.setLocationRelativeTo(null);
                            }
                       }
                        
                    }
                   
               }
           }
        });
        
        customerDelete.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               deleteCustomerDialog.setVisible(true);
               deleteCustomerMessageLabel.setText("You are about to delete customer '" + customerListArr[customerNameList.getSelectedIndex()] + "'. Proceed?");
               deleteCustomerDialog.pack();
               deleteCustomerDialog.setTitle("Deleting Customer");
               deleteCustomerDialog.setLocationRelativeTo(null);
           }
        });
        
        //end of viewCustomer
        
        
        //start of addCustomer
        //info panel   
        JLabel customerNameLabel1 = new JLabel("Name");
        JLabel phoneNoLabel1 = new JLabel("Phone Number");
        JLabel addressLabel1 = new JLabel("Address");
//        JLabel artPurchasesLabel1 = new JLabel("Art Purchases");
        JLabel artistPrefLabel1 = new JLabel("Artist Preferences");
        
        final JTextField nameTF1 = new JTextField(20);
        final JTextField phoneNoTF1 = new JTextField(20);
        final JTextArea addressTA1= new JTextArea(4, 3);
//        final JTextArea artPurchasesTA1 = new JTextArea(4, 3);
        //final JTextArea artistPrefTA1 = new JTextArea(4, 3);
        
        final JButton customerAdd = new JButton("Add");
        
        artistPrefCB1.setSelectedIndex(-1);
        
        JPanel namePanel1 = new JPanel();
        JPanel phoneNoPanel1 = new JPanel();
        JPanel addressPanel1 = new JPanel();
//        JPanel artPurchasesPanel1 = new JPanel();
        JPanel artistPrefPanel1 = new JPanel();
        
        JPanel addCustomerEmptyPanel = new JPanel();
        
        namePanel1.setLayout(new GridLayout(1, 2));
        namePanel1.add(customerNameLabel1);
        namePanel1.add(nameTF1);
        
        phoneNoPanel1.setLayout(new GridLayout(1, 2));
        phoneNoPanel1.add(phoneNoLabel1);
        phoneNoPanel1.add(phoneNoTF1);
        
        addressPanel1.setLayout(new GridLayout(1, 2));
        addressPanel1.add(addressLabel1);
        addressPanel1.add(addressTA1);
        
//        artPurchasesPanel1.setLayout(new GridLayout(1, 2));
//        artPurchasesPanel1.add(artPurchasesLabel1);
//        artPurchasesPanel1.add(artPurchasesTA1);
        
        artistPrefPanel1.setLayout(new GridLayout(1, 2));
        artistPrefPanel1.add(artistPrefLabel1);
        artistPrefPanel1.add(artistPrefCB1);
        
        JPanel jp1 = new JPanel();
        jp1.setLayout(new GridLayout(6, 1, 10, 10));
        jp1.add(namePanel1);
        jp1.add(phoneNoPanel1);
        jp1.add(addressPanel1);
        jp1.add(artistPrefPanel1);
        jp1.add(addCustomerEmptyPanel);
        jp1.add(customerAdd);
        
        addCustomer.setLayout(new BorderLayout());
        addCustomer.add(jp1, BorderLayout.CENTER);
        
        customerAdd.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               initializeCustomerList();
               if(nameTF1.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Name' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(phoneNoTF1.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Phone Number' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(addressTA1.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Address' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(artistPrefCB1.getSelectedIndex() == -1){
                   warningMessageLabel.setText("Please select preferred artist.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else{
                   for(int i = 0; i < customerListArr.length; i++){
                        if(customerListArr[i].equals(nameTF1.getText().trim())){
                            warningMessageLabel.setText("The customer name " + "'" + nameTF1.getText().trim() + "' is already registered. Please try another name.");
                            warningDialog.pack();
                            warningDialog.setTitle("Warning");
                            warningDialog.setLocationRelativeTo(null);
                            warningDialog.setVisible(true);
                            break;
                        }else if(i + 1 == customerListArr.length){
                            if(!phoneNoTF1.getText().trim().matches(".*\\d+.*")){
                                warningMessageLabel.setText("Invalid phone number format. Only numbers are allowed.");
                                warningDialog.pack();
                                warningDialog.setTitle("Warning");
                                warningDialog.setLocationRelativeTo(null);
                                warningDialog.setVisible(true);
                            }else{
                                customerOperations("insertCustomer", nameTF1.getText().trim(), phoneNoTF1.getText().trim(), addressTA1.getText().trim(), "null", artistPrefCB1.getSelectedItem().toString().trim(), "");
                                warningMessageLabel.setText("Added customer '" + nameTF1.getText().trim() + "'.");
                                warningDialog.pack();
                                warningDialog.setTitle("Message");
                                warningDialog.setLocationRelativeTo(null);
                                warningDialog.setVisible(true);
                            }
                        }
                    }
                    initializeCustomerList();
                    updateArtistComboBox();
               }
           }
        });
        
        //end of addCustomer
        
        
        //start of viewArtist
        //listview (left)
        JScrollPane artistJSP = new JScrollPane(artistNameList);
        viewArtist.setLayout(new BorderLayout());
        viewArtist.add(artistJSP, BorderLayout.WEST);
        artistJSP.setPreferredSize(new Dimension(150,80));
        
        //info panel (right)
        JLabel artistNameLabel = new JLabel("Name");
        JLabel specialtyLabel = new JLabel("Specialty");
        JLabel aliveLabel = new JLabel("Alive");
        JLabel priceRangeLabel = new JLabel("Price Range");
        
        final JTextField artistNameTF = new JTextField(50);
        final JTextField specialtyTF = new JTextField(50);
        //final JTextField aliveTF = new JTextField(50);
        final JTextField priceRangeTF = new JTextField(50);
        
        String[] aliveCBArr = {"Yes", "No"};
        DefaultComboBoxModel m = new DefaultComboBoxModel(aliveCBArr);
        aliveCB.setModel(m);
        aliveCB.setSelectedIndex(-1);
        
        final JButton artistEdit = new JButton("Edit");
        final JButton artistDelete = new JButton("Delete");
        
        //toggle between show alive artist and all artist
        final JRadioButton showAliveArtist = new JRadioButton("Show Alive Artist Only");
        final JRadioButton showAllArtist = new JRadioButton("Show All Artist");
        
        showAllArtist.setSelected(true);
        priceRangeTF.setEnabled(false);
        
        JPanel artistNamePanel = new JPanel();
        JPanel specialtyPanel = new JPanel();
        JPanel alivePanel = new JPanel();
        JPanel priceRangePanel = new JPanel();
        JPanel artistButtonPanel = new JPanel();
        JPanel artistRadioButtonPanel = new JPanel();
        
        artistNamePanel.setLayout(new GridLayout(1, 2));
        artistNamePanel.add(artistNameLabel);
        artistNamePanel.add(artistNameTF);
        
        specialtyPanel.setLayout(new GridLayout(1, 2));
        specialtyPanel.add(specialtyLabel);
        specialtyPanel.add(specialtyTF);
        
        alivePanel.setLayout(new GridLayout(1, 2));
        alivePanel.add(aliveLabel);
        alivePanel.add(aliveCB);
        
        priceRangePanel.setLayout(new GridLayout(1, 2));
        priceRangePanel.add(priceRangeLabel);
        priceRangePanel.add(priceRangeTF);
        
        artistButtonPanel.setLayout(new GridLayout(1, 2));
        artistButtonPanel.add(artistEdit);
        artistButtonPanel.add(artistDelete);
        
        artistRadioButtonPanel.setLayout(new GridLayout(1, 2));
        artistRadioButtonPanel.add(showAllArtist);
        artistRadioButtonPanel.add(showAliveArtist);
        
        JPanel jp2 = new JPanel();
        jp2.setLayout(new GridLayout(6, 1, 10, 10));
        jp2.add(artistRadioButtonPanel);
        jp2.add(artistNamePanel);
        jp2.add(specialtyPanel);
        jp2.add(alivePanel);
        jp2.add(priceRangePanel);
        jp2.add(artistButtonPanel);
        
        viewArtist.add(jp2, BorderLayout.CENTER);
        
        //jlist listener
        artistNameList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    if(artistNameList.getSelectedIndex() != -1){
                        artistNameTF.setText(artistListArr[artistNameList.getSelectedIndex()]);
                        specialtyTF.setText(specialtyArr[artistNameList.getSelectedIndex()]);
                        
                        if(priceRangeArr[artistNameList.getSelectedIndex()].equals("null")){
                            priceRangeTF.setText("");
                        }else{
                            priceRangeTF.setText(priceRangeArr[artistNameList.getSelectedIndex()]);
                        }
                        
                        for(int i = 0; i < aliveCB.getItemCount(); i++){
                            if(aliveCB.getItemAt(i).toString().equals(aliveArr[artistNameList.getSelectedIndex()])){
                                aliveCB.setSelectedIndex(i);
                                break;
                            }
                        }
                        System.out.println(artistNameList.getSelectedIndex());
                    }
                }
            }
        });
        
        //radio button action listener
        showAllArtist.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               showAllArtist.setSelected(true);
               showAliveArtist.setSelected(false);
               initializeArtistList();
               artistNameTF.setText("");
               specialtyTF.setText("");
               priceRangeTF.setText("");
               aliveCB.setSelectedIndex(-1);
               System.out.println("Clicked on all artist");
           }
        });
        
        showAliveArtist.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               showAliveArtist.setSelected(true);
               showAllArtist.setSelected(false);
               refreshArtistList("Yes");
               artistNameTF.setText("");
               specialtyTF.setText("");
               priceRangeTF.setText("");
               aliveCB.setSelectedIndex(-1);
               System.out.println("Clicked on alive artist");
           }
        });
        
        //button action listener
        artistEdit.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               if(artistNameTF.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Name' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(specialtyTF.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Specialty' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(aliveCB.getSelectedIndex() == -1){
                   warningMessageLabel.setText("Please select 'Alive' status.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else{
                   for(int i = 0; i < artistListArr.length; i++){
                       if(i != artistNameList.getSelectedIndex()){
                            if(artistListArr[i].equals(artistNameTF.getText().trim())){
                                warningMessageLabel.setText("The artist name " + "'" + artistNameTF.getText().trim() + "' is already registered. Please try another name.");
                                warningDialog.pack();
                                warningDialog.setTitle("Warning");
                                warningDialog.setLocationRelativeTo(null);
                                warningDialog.setVisible(true);
                                break;
                            }else if(i + 1 == artistListArr.length){
                                editArtistDialog.setVisible(true);
                                editArtistMessageLabel.setText("You are about to edit artist '" + artistListArr[artistNameList.getSelectedIndex()] + "'. Proceed?");
                                editArtistDialog.pack();
                                editArtistDialog.setTitle("Editing Artist");
                                editArtistDialog.setLocationRelativeTo(null);
                            }
                       }
                    }
                   
               }
           }
        });
        
        artistDelete.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               deleteArtistDialog.setVisible(true);
               deleteArtistMessageLabel.setText("You are about to delete artist '" + artistListArr[artistNameList.getSelectedIndex()] + "'. Proceed?");
               deleteArtistDialog.pack();
               deleteArtistDialog.setTitle("Deleting Artist");
               deleteArtistDialog.setLocationRelativeTo(null);
           }
        });
        
        //end of viewArtist
        
        
        //start of addArtist
        //info panel   
        JLabel artistNameLabel1 = new JLabel("Name");
        JLabel specialtyLabel1 = new JLabel("Specialty");
        JLabel aliveLabel1 = new JLabel("Alive");
        
        final JTextField artistNameTF1 = new JTextField(50);
        final JTextField specialtyTF1 = new JTextField(50);
        
        final JButton artistAdd = new JButton("Add");
        
        aliveCB1.setModel(m);
        aliveCB1.setSelectedIndex(-1);
        
        JPanel artistNamePanel1 = new JPanel();
        JPanel specialtyPanel1 = new JPanel();
        JPanel alivePanel1 = new JPanel();
        JPanel addArtistEmptyPanel = new JPanel();
        JPanel addArtistEmptyPanel1 = new JPanel();
        
        artistNamePanel1.setLayout(new GridLayout(1, 2));
        artistNamePanel1.add(artistNameLabel1);
        artistNamePanel1.add(artistNameTF1);
        
        specialtyPanel1.setLayout(new GridLayout(1, 2));
        specialtyPanel1.add(specialtyLabel1);
        specialtyPanel1.add(specialtyTF1);
        
        alivePanel1.setLayout(new GridLayout(1, 2));
        alivePanel1.add(aliveLabel1);
        alivePanel1.add(aliveCB1);
        
        
        JPanel jp3 = new JPanel();
        jp3.setLayout(new GridLayout(6, 1, 10, 10));
        jp3.add(artistNamePanel1);
        jp3.add(specialtyPanel1);
        jp3.add(alivePanel1);
        jp3.add(addArtistEmptyPanel);
        jp3.add(addArtistEmptyPanel1);
        jp3.add(artistAdd);
        
        addArtist.setLayout(new BorderLayout());
        addArtist.add(jp3, BorderLayout.CENTER);
        
        artistAdd.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               initializeArtistList();
               if(artistNameTF1.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Name' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(specialtyTF1.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Specialty' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else{
                   for(int i = 0; i < artistListArr.length; i++){
                        if(artistListArr[i].equals(artistNameTF1.getText().trim())){
                            warningMessageLabel.setText("The artist name " + "'" + artistNameTF1.getText().trim() + "' is already registered. Please try another name.");
                            warningDialog.pack();
                            warningDialog.setTitle("Warning");
                            warningDialog.setLocationRelativeTo(null);
                            warningDialog.setVisible(true);
                            break;
                        }else if(i + 1 == artistListArr.length){
                            artistOperations("insertArtist", artistNameTF1.getText().trim(), specialtyTF1.getText().trim(), aliveCB1.getSelectedItem().toString().trim(), "null", "");    
                            warningMessageLabel.setText("Added artist '" + artistNameTF1.getText().trim() + "'.");
                            warningDialog.pack();
                            warningDialog.setTitle("Message");
                            warningDialog.setLocationRelativeTo(null);
                            warningDialog.setVisible(true);
                        }
                    }
                    updateArtistComboBox();
               }
           }
        });
        
        //end of addArtist
        
        
        //start of artistArtworks
        
        //list panel
        JPanel jspPanel = new JPanel();
        //artist list
        JScrollPane artistArtworksArtistJSP = new JScrollPane(artistArtworksArtistNameList);
        jspPanel.setLayout(new GridLayout(1, 2));
        jspPanel.add(artistArtworksArtistJSP);
        //artwork list
        JScrollPane artistArtworksArtworkJSP = new JScrollPane(artistArtworksArtworkNameList);
        jspPanel.add(artistArtworksArtworkJSP);
        
        //info panel
        JPanel artistArtworksInfoPanel = new JPanel();
        
        JLabel artworkTitleLabel2 = new JLabel("Title");
        JLabel datePurchasedLabel2 = new JLabel("Date Purchased");
        JLabel dateSoldLabel2 = new JLabel("Date Sold");
        JLabel artistLabel2 = new JLabel("Artist");
        JLabel artistArtworksCustomerLabel = new JLabel("Customer");
        JLabel artistArtworksPurchasePriceLabel = new JLabel("Purchase Price (RM)");
        JLabel artistArtworksSellingPriceLabel = new JLabel("Selling Price (RM)");
        
        final JTextField artworkTitleTF2 = new JTextField(50);
        final JTextField datePurchasedTF2 = new JTextField(50);
        final JTextField dateSoldTF2 = new JTextField(50);
        final JTextField artistTF2 = new JTextField(50);
        final JTextField artistArtworksCustomerTF = new JTextField(50);
        final JTextField artistArtworksPurchasePriceTF = new JTextField(50);
        final JTextField artistArtworksSellingPriceTF = new JTextField(50);
        
        artworkTitleTF2.setEnabled(false);
        datePurchasedTF2.setEnabled(false);
        dateSoldTF2.setEnabled(false);
        artistTF2.setEnabled(false);
        artistArtworksCustomerTF.setEnabled(false);
        artistArtworksPurchasePriceTF.setEnabled(false);
        artistArtworksSellingPriceTF.setEnabled(false);
        
        JPanel artworkTitlePanel2 = new JPanel();
        JPanel datePurchasedPanel2 = new JPanel();
        JPanel dateSoldPanel2 = new JPanel();
        JPanel artistPanel2 = new JPanel();
        JPanel artistArtworksCustomerPanel = new JPanel();
        JPanel artistArtworksPurchasePricePanel = new JPanel();
        JPanel artistArtworksSellingPricePanel = new JPanel();
        
        artworkTitlePanel2.setLayout(new GridLayout(1, 2));
        artworkTitlePanel2.add(artworkTitleLabel2);
        artworkTitlePanel2.add(artworkTitleTF2);
        
        datePurchasedPanel2.setLayout(new GridLayout(1, 2));
        datePurchasedPanel2.add(datePurchasedLabel2);
        datePurchasedPanel2.add(datePurchasedTF2);
        
        dateSoldPanel2.setLayout(new GridLayout(1, 2));
        dateSoldPanel2.add(dateSoldLabel2);
        dateSoldPanel2.add(dateSoldTF2);
        
        artistPanel2.setLayout(new GridLayout(1, 2));
        artistPanel2.add(artistLabel2);
        artistPanel2.add(artistTF2);
        
        artistArtworksCustomerPanel.setLayout(new GridLayout(1, 2));
        artistArtworksCustomerPanel.add(artistArtworksCustomerLabel);
        artistArtworksCustomerPanel.add(artistArtworksCustomerTF);
        
        artistArtworksPurchasePricePanel.setLayout(new GridLayout(1, 2));
        artistArtworksPurchasePricePanel.add(artistArtworksPurchasePriceLabel);
        artistArtworksPurchasePricePanel.add(artistArtworksPurchasePriceTF);
        
        artistArtworksSellingPricePanel.setLayout(new GridLayout(1, 2));
        artistArtworksSellingPricePanel.add(artistArtworksSellingPriceLabel);
        artistArtworksSellingPricePanel.add(artistArtworksSellingPriceTF);
        
        artistArtworksInfoPanel.setLayout(new GridLayout(7, 1, 10, 10));
        artistArtworksInfoPanel.add(artworkTitlePanel2);
        artistArtworksInfoPanel.add(datePurchasedPanel2);
        artistArtworksInfoPanel.add(dateSoldPanel2);
        artistArtworksInfoPanel.add(artistArtworksPurchasePricePanel);
        artistArtworksInfoPanel.add(artistPanel2);
        artistArtworksInfoPanel.add(artistArtworksCustomerPanel);
        artistArtworksInfoPanel.add(artistArtworksSellingPricePanel);

        artistArtworks.setLayout(new GridLayout(1, 2));
        artistArtworks.add(jspPanel);
        artistArtworks.add(artistArtworksInfoPanel);

        //jlist listener
        artistArtworksArtistNameList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    if(artistArtworksArtistNameList.getSelectedIndex() != -1){
                        getArtworkByArtist(artistListArr[artistArtworksArtistNameList.getSelectedIndex()]);
                        System.out.println(artistArtworksArtistNameList.getSelectedIndex());
                    }
                }
            }
        });
        
        //jlist listener
        artistArtworksArtworkNameList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    if(artistArtworksArtworkNameList.getSelectedIndex() != -1){
                        artworkTitleTF2.setText(artistArtworkTitleArr[artistArtworksArtworkNameList.getSelectedIndex()]);
                        datePurchasedTF2.setText(artistArtworkDatePurchasedArr[artistArtworksArtworkNameList.getSelectedIndex()]);
                        artistTF2.setText(artistListArr[artistArtworksArtistNameList.getSelectedIndex()]);
                        artistArtworksPurchasePriceTF.setText(artistArtworksPurchasePriceArr[artistArtworksArtworkNameList.getSelectedIndex()]);
                        
                        if(artistArtworkDateSoldArr[artistArtworksArtworkNameList.getSelectedIndex()].equals("null")){
                            dateSoldTF2.setText("");
                            artistArtworksCustomerTF.setText("");
                            artistArtworksSellingPriceTF.setText("");
                        }else{
                            dateSoldTF2.setText(artistArtworkDateSoldArr[artistArtworksArtworkNameList.getSelectedIndex()]);
                            String customer = "";
                            String sellingPrice = "";
                            try{
                                customer = customer3D[artistArtworksArtistNameList.getSelectedIndex()][artistArtworksArtworkNameList.getSelectedIndex()][0];
                                System.out.println("customer : " + customer +"...");
                                artistArtworksCustomerTF.setText(customer);
                                
                                artistArtworksSellingPriceTF.setText(artistArtworksSellingPriceArr[artistArtworksArtworkNameList.getSelectedIndex()]);
                            }catch(Exception e){
                                
                            }
                        }
                        System.out.println(artistArtworksArtworkNameList.getSelectedIndex());
                    }
                }
            }
        });
        
        //end of artistArtworks
        
        
        //start of viewArtwork
        //listview (left)
        JScrollPane artworkJSP = new JScrollPane(artworkTitleList);
        viewArtwork.setLayout(new BorderLayout());
        viewArtwork.add(artworkJSP, BorderLayout.WEST);
        artworkJSP.setPreferredSize(new Dimension(150,80));
        
        //info panel (right)
        JLabel artworkTitleLabel = new JLabel("Title");
        JLabel datePurchasedLabel = new JLabel("Date Purchased");
        JLabel dateSoldLabel = new JLabel("Date Sold");
        JLabel purchasePriceLabel = new JLabel("Purchase Price (RM)");
        JLabel sellingPriceLabel = new JLabel("Selling Price (RM)");
        JLabel artistLabel = new JLabel("Artist");
        
        final JTextField artworkTitleTF = new JTextField(50);
        final JTextField datePurchasedTF = new JTextField(50);
        final JTextField dateSoldTF = new JTextField(50);
        final JTextField purchasePriceTF = new JTextField(50);
        final JTextField sellingPriceTF = new JTextField(50);
        
        final JButton artworkEdit = new JButton("Edit");
        final JButton artworkDelete = new JButton("Delete");
        
        //dateSoldTF.setEnabled(false);
        
        artistCB.setSelectedIndex(-1);
        
        JPanel artworkTitlePanel = new JPanel();
        JPanel datePurchasedPanel = new JPanel();
        JPanel dateSoldPanel = new JPanel();
        JPanel artistPanel = new JPanel();
        JPanel artworkButtonPanel = new JPanel();
        JPanel purchasePricePanel = new JPanel();
        JPanel sellingPricePanel = new JPanel();
        
        artworkTitlePanel.setLayout(new GridLayout(1, 2));
        artworkTitlePanel.add(artworkTitleLabel);
        artworkTitlePanel.add(artworkTitleTF);
        
        datePurchasedPanel.setLayout(new GridLayout(1, 2));
        datePurchasedPanel.add(datePurchasedLabel);
        datePurchasedPanel.add(datePurchasedTF);
        
        dateSoldPanel.setLayout(new GridLayout(1, 2));
        dateSoldPanel.add(dateSoldLabel);
        dateSoldPanel.add(dateSoldTF);
        
        artistPanel.setLayout(new GridLayout(1, 2));
        artistPanel.add(artistLabel);
        artistPanel.add(artistCB);
        
        artworkButtonPanel.setLayout(new GridLayout(1, 2));
        artworkButtonPanel.add(artworkEdit);
        artworkButtonPanel.add(artworkDelete);
        
        purchasePricePanel.setLayout(new GridLayout(1, 2));
        purchasePricePanel.add(purchasePriceLabel);
        purchasePricePanel.add(purchasePriceTF);
        
        sellingPricePanel.setLayout(new GridLayout(1, 2));
        sellingPricePanel.add(sellingPriceLabel);
        sellingPricePanel.add(sellingPriceTF);
        
        JPanel jp4 = new JPanel();
        jp4.setLayout(new GridLayout(7, 1, 10, 10));
        jp4.add(artworkTitlePanel);
        jp4.add(datePurchasedPanel);
        jp4.add(dateSoldPanel);
        jp4.add(purchasePricePanel);
        jp4.add(sellingPricePanel);
        jp4.add(artistPanel);
        jp4.add(artworkButtonPanel);
        
        viewArtwork.add(jp4, BorderLayout.CENTER);
        
        //jlist listener
        artworkTitleList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    if(artworkTitleList.getSelectedIndex() != -1){
                        artworkTitleTF.setText(artworkListArr[artworkTitleList.getSelectedIndex()]);
                        datePurchasedTF.setText(datePurchasedArr[artworkTitleList.getSelectedIndex()]);
                        if(dateSoldArr[artworkTitleList.getSelectedIndex()].equals("null")){
                            dateSoldTF.setText("");
                        }else{
                            dateSoldTF.setText(dateSoldArr[artworkTitleList.getSelectedIndex()]);
                        }
                        purchasePriceTF.setText(purchasePriceArr[artworkTitleList.getSelectedIndex()]);
                        if(sellingPriceArr[artworkTitleList.getSelectedIndex()].equals("null")){
                            sellingPriceTF.setText("");
                        }else{
                            sellingPriceTF.setText(sellingPriceArr[artworkTitleList.getSelectedIndex()]);
                        }
                        for(int i = 0; i < artistCB.getItemCount(); i++){
                            if(artistCB.getItemAt(i).toString().equals(artistArr[artworkTitleList.getSelectedIndex()])){
                                artistCB.setSelectedIndex(i);
                                break;
                            }else if(i + 1 == artistCB.getItemCount()){
                                artistCB.setSelectedIndex(-1);
                            }
                        }
                        System.out.println(artworkTitleList.getSelectedIndex());
                    }
                }
            }
        });
        
        artworkEdit.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               if(artworkTitleTF.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Title' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(datePurchasedTF.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Date Purchased' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(!validDate(datePurchasedTF.getText().trim())){
                   warningMessageLabel.setText("Invalid date format. The format is 'dd/mm/yyyy'");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(!purchasePriceTF.getText().toString().matches("\\d+")){
                   warningMessageLabel.setText("Invalid price format. Only numbers are allowed.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(artistCB.getSelectedIndex() == -1){
                   warningMessageLabel.setText("Please select artist.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else{
                   for(int i = 0; i < artworkListArr.length; i++){
                       if(i != artworkTitleList.getSelectedIndex()){
                        if(artworkListArr[i].equals(artworkTitleTF.getText().trim())){
                            warningMessageLabel.setText("The artwork title " + "'" + artworkTitleTF.getText().trim() + "' is already registered. Please try another title.");
                            warningDialog.pack();
                            warningDialog.setTitle("Warning");
                            warningDialog.setLocationRelativeTo(null);
                            warningDialog.setVisible(true);
                            break;
                        }else if(i + 1 == artworkListArr.length){
                            editArtworkDialog.setVisible(true);
                            editArtworkMessageLabel.setText("You are about to edit artwork '" + artworkListArr[artworkTitleList.getSelectedIndex()] + "'. Proceed?");
                            editArtworkDialog.pack();
                            editArtworkDialog.setTitle("Editing Artwork");
                            editArtworkDialog.setLocationRelativeTo(null);
                        }
                    }
                   }
               }
               
           }
        });
        
        artworkDelete.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               deleteArtworkDialog.setVisible(true);
               deleteArtworkMessageLabel.setText("You are about to delete artwork '" + artworkListArr[artworkTitleList.getSelectedIndex()] + "'. Proceed?");
               deleteArtworkDialog.pack();
               deleteArtworkDialog.setTitle("Deleting Artwork");
               deleteArtworkDialog.setLocationRelativeTo(null);
           }
        });
        
        //end of viewArtwork
        
        
        //start of addArtwork
        //info panel   
        JLabel artworkTitleLabel1 = new JLabel("Title");
        JLabel datePurchasedLabel1 = new JLabel("Date Purchased");
        JLabel purchasePriceLabel1 = new JLabel("Purchase Price (RM)");
        JLabel artistLabel1 = new JLabel("Artist");
        
        final JTextField artworkTitleTF1 = new JTextField(50);
        final JTextField datePurchasedTF1 = new JTextField(50);
        final JTextField purchasePriceTF1 = new JTextField(50);
        
        final JButton artworkAdd = new JButton("Add");
        
        JPanel artworkTitlePanel1 = new JPanel();
        JPanel datePurchasedPanel1 = new JPanel();
        JPanel purchasePricePanel1 = new JPanel();
        JPanel artistPanel1 = new JPanel();
        JPanel addArtworkEmptyPanel = new JPanel();
        JPanel addArtworkEmptyPanel1 = new JPanel();
        
        artworkTitlePanel1.setLayout(new GridLayout(1, 2));
        artworkTitlePanel1.add(artworkTitleLabel1);
        artworkTitlePanel1.add(artworkTitleTF1);
        
        datePurchasedPanel1.setLayout(new GridLayout(1, 2));
        datePurchasedPanel1.add(datePurchasedLabel1);
        datePurchasedPanel1.add(datePurchasedTF1);
        
        purchasePricePanel1.setLayout(new GridLayout(1, 2));
        purchasePricePanel1.add(purchasePriceLabel1);
        purchasePricePanel1.add(purchasePriceTF1);
        
        artistPanel1.setLayout(new GridLayout(1, 2));
        artistPanel1.add(artistLabel1);
        artistPanel1.add(artistCB1);
        
        JPanel jp5 = new JPanel();
        jp5.setLayout(new GridLayout(6, 1, 10, 10));
        jp5.add(artworkTitlePanel1);
        jp5.add(datePurchasedPanel1);
        jp5.add(purchasePricePanel1);
        jp5.add(artistPanel1);
        jp5.add(addArtworkEmptyPanel1);
        jp5.add(artworkAdd);
        
        addArtwork.setLayout(new BorderLayout());
        addArtwork.add(jp5, BorderLayout.CENTER);
        
        artworkAdd.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               initializeArtworkList();
               if(artworkTitleTF1.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Title' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(datePurchasedTF1.getText().trim().equals("")){
                   warningMessageLabel.setText("Please fill up the 'Date Purchased' field.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(!validDate(datePurchasedTF1.getText().trim())){
                   warningMessageLabel.setText("Invalid date format. The format is 'dd/mm/yyyy'");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(!purchasePriceTF1.getText().toString().matches("\\d+")){
                   warningMessageLabel.setText("Invalid price format. Only numbers are allowed.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(artistCB1.getSelectedIndex() == -1){
                   warningMessageLabel.setText("Please select artist.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else{
                   for(int i = 0; i < artworkListArr.length; i++){
                        if(artworkListArr[i].equals(artworkTitleTF1.getText().trim())){
                            warningMessageLabel.setText("The artwork title " + "'" + artworkTitleTF1.getText().trim() + "' is already registered. Please try another title.");
                            warningDialog.pack();
                            warningDialog.setTitle("Warning");
                            warningDialog.setLocationRelativeTo(null);
                            warningDialog.setVisible(true);
                            break;
                        }else if(i + 1 == artworkListArr.length){
                            System.out.println(artistCB1.getSelectedItem().toString().trim());
                            String dateSold = "null";
                            artworkOperations("insertArtwork", artworkTitleTF1.getText().trim(), datePurchasedTF1.getText().trim(), dateSold, purchasePriceTF1.getText().trim(), "null", artistCB1.getSelectedItem().toString().trim(), "");    
                            warningMessageLabel.setText("Added artwork '" + artworkTitleTF1.getText().trim() + "'.");
                            warningDialog.pack();
                            warningDialog.setTitle("Message");
                            warningDialog.setLocationRelativeTo(null);
                            warningDialog.setVisible(true);
                            
                        }
                    }
                    initializeArtworkList();
                    updateArtistComboBox();
               }
           }
        });
        
        //end of addArtwork
        
        
        //start of currentInventory
        //listview (left)
        JScrollPane availableArtworkJSP = new JScrollPane(availableArtworkTitleList);
        currentInventory.setLayout(new BorderLayout());
        currentInventory.add(availableArtworkJSP, BorderLayout.WEST);
        availableArtworkJSP.setPreferredSize(new Dimension(150,80));
        
        //info panel (right)
        JLabel artworkTitleLabel3 = new JLabel("Title");
        JLabel datePurchasedLabel3 = new JLabel("Date Purchased");
        JLabel artistLabel3 = new JLabel("Artist");
        JLabel customerLabel3 = new JLabel("Customer");
        JLabel purchasePriceLabel3 = new JLabel("Purchase Price (RM)");
        JLabel sellingPriceLabel3 = new JLabel("Selling Price (RM)");
        
        final JTextField artworkTitleTF3 = new JTextField(50);
        final JTextField datePurchasedTF3 = new JTextField(50);
        final JTextField artistTF3 = new JTextField(50);
        final JTextField purchasePriceTF3 = new JTextField(50);
        final JTextField sellingPriceTF3 = new JTextField(50);
        
        //toggle between show alive artist and all artist
        final JRadioButton showOver3MonthsUnsoldArtwork = new JRadioButton("Unsold Artwork Over 3 Months");
        final JRadioButton showAllUnsoldArtwork = new JRadioButton("All Unsold Artwork");
        
        showAllUnsoldArtwork.setSelected(true);
        
        artworkTitleTF3.setEnabled(false);
        datePurchasedTF3.setEnabled(false);
        artistTF3.setEnabled(false);
        purchasePriceTF3.setEnabled(false);
        
        final JButton availableArtworkPurchase = new JButton("Purchase Artwork");
        
        customerCB.setSelectedIndex(-1);
        
        JPanel artworkTitlePanel3 = new JPanel();
        JPanel datePurchasedPanel3 = new JPanel();
        JPanel artistPanel3 = new JPanel();
        JPanel customerPanel3 = new JPanel();
        JPanel purchasePricePanel3 = new JPanel();
        JPanel sellingPricePanel3 = new JPanel();
        JPanel currentInventoryRadioButtonPanel = new JPanel();
        
        artworkTitlePanel3.setLayout(new GridLayout(1, 2));
        artworkTitlePanel3.add(artworkTitleLabel3);
        artworkTitlePanel3.add(artworkTitleTF3);
        
        datePurchasedPanel3.setLayout(new GridLayout(1, 2));
        datePurchasedPanel3.add(datePurchasedLabel3);
        datePurchasedPanel3.add(datePurchasedTF3);
        
        artistPanel3.setLayout(new GridLayout(1, 2));
        artistPanel3.add(artistLabel3);
        artistPanel3.add(artistTF3);
        
        customerPanel3.setLayout(new GridLayout(1, 2));
        customerPanel3.add(customerLabel3);
        customerPanel3.add(customerCB);
        
        purchasePricePanel3.setLayout(new GridLayout(1, 2));
        purchasePricePanel3.add(purchasePriceLabel3);
        purchasePricePanel3.add(purchasePriceTF3);
        
        sellingPricePanel3.setLayout(new GridLayout(1, 2));
        sellingPricePanel3.add(sellingPriceLabel3);
        sellingPricePanel3.add(sellingPriceTF3);
        
        currentInventoryRadioButtonPanel.setLayout(new GridLayout(1, 2));
        currentInventoryRadioButtonPanel.add(showAllUnsoldArtwork);
        currentInventoryRadioButtonPanel.add(showOver3MonthsUnsoldArtwork);
        
        JPanel availableArtworkJP = new JPanel();
        availableArtworkJP.setLayout(new GridLayout(8, 1, 10, 10));
        availableArtworkJP.add(currentInventoryRadioButtonPanel);
        availableArtworkJP.add(artworkTitlePanel3);
        availableArtworkJP.add(datePurchasedPanel3);
        availableArtworkJP.add(purchasePricePanel3);
        availableArtworkJP.add(artistPanel3);
        availableArtworkJP.add(customerPanel3);
        availableArtworkJP.add(sellingPricePanel3);
        availableArtworkJP.add(availableArtworkPurchase);
        
        currentInventory.add(availableArtworkJP, BorderLayout.CENTER);
        
        //jlist listener
        availableArtworkTitleList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    if(availableArtworkTitleList.getSelectedIndex() != -1){
                        artworkTitleTF3.setText(availableArtworkTitleArr[availableArtworkTitleList.getSelectedIndex()]);
                        datePurchasedTF3.setText(availableArtworkDatePurchasedArr[availableArtworkTitleList.getSelectedIndex()]);
                        artistTF3.setText(availableArtworkArtistArr[availableArtworkTitleList.getSelectedIndex()]);
                        purchasePriceTF3.setText(availableArtworkPurchasePriceArr[availableArtworkTitleList.getSelectedIndex()]);
                        System.out.println(availableArtworkTitleList.getSelectedIndex());
                    }
                }
            }
        });
        
        availableArtworkPurchase.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               if(customerCB.getSelectedIndex() == -1){
                   warningMessageLabel.setText("Please select a customer.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else if(!sellingPriceTF3.getText().trim().matches(".*\\d+.*")){
                   warningMessageLabel.setText("Invalid price format. Only numbers are allowed.");
                   warningDialog.pack();
                   warningDialog.setTitle("Warning");
                   warningDialog.setLocationRelativeTo(null);
                   warningDialog.setVisible(true);
               }else{
                   availableArtworkPurchaseDialog.setVisible(true);
                    availableArtworkPurchaseMessageLabel.setText("Customer '" + customerCB.getSelectedItem().toString() + "' will purchase artwork titled '" + availableArtworkTitleArr[availableArtworkTitleList.getSelectedIndex()] + "' for RM" + sellingPriceTF3.getText().trim() + ". Proceed?");
                    availableArtworkPurchaseDialog.pack();
                    availableArtworkPurchaseDialog.setTitle("Purchasing Artwork");
                    availableArtworkPurchaseDialog.setLocationRelativeTo(null);
               }
           }
        });
        
        //radio button action listener
        showAllUnsoldArtwork.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               showAllUnsoldArtwork.setSelected(true);
               showOver3MonthsUnsoldArtwork.setSelected(false);
               getAvailableArtwork();
               artworkTitleTF3.setText("");
               datePurchasedTF3.setText("");
               artistTF3.setText("");
               purchasePriceTF3.setText("");
               sellingPriceTF3.setText("");
               customerCB.setSelectedIndex(-1);
               System.out.println("Clicked on all unsold artwork");
           }
        });
        
        showOver3MonthsUnsoldArtwork.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               showOver3MonthsUnsoldArtwork.setSelected(true);
               showAllUnsoldArtwork.setSelected(false);
               refreshArtworkList();
               artworkTitleTF3.setText("");
               datePurchasedTF3.setText("");
               artistTF3.setText("");
               purchasePriceTF3.setText("");
               sellingPriceTF3.setText("");
               customerCB.setSelectedIndex(-1);
               System.out.println("Clicked on 3 months unsold artwork");
           }
        });
        
        //end of currentInventory
        
        
        //start of archiveInventory
        //listview (left)
        JScrollPane archiveJSP = new JScrollPane(archiveArtworkTitleList);
        archiveInventory.setLayout(new BorderLayout());
        archiveInventory.add(archiveJSP, BorderLayout.WEST);
        archiveJSP.setPreferredSize(new Dimension(150,80));
        
        //info panel (right)
        JLabel artworkTitleLabel4 = new JLabel("Title");
        JLabel datePurchasedLabel4 = new JLabel("Date Purchased");
        JLabel dateSoldLabel4 = new JLabel("Date Sold");
        JLabel artistLabel4 = new JLabel("Artist");
        JLabel customerLabel4 = new JLabel("Customer");
        JLabel purchasePriceLabel4 = new JLabel("Purchase Price (RM)");
        JLabel sellingPriceLabel4 = new JLabel("Selling Price (RM)");
        
        final JTextField artworkTitleTF4 = new JTextField(50);
        final JTextField datePurchasedTF4 = new JTextField(50);
        final JTextField dateSoldTF4 = new JTextField(50);
        final JTextField artistTF4 = new JTextField(50);
        final JTextField customerTF4 = new JTextField(50);
        final JTextField purchasePriceTF4 = new JTextField(50);
        final JTextField sellingPriceTF4 = new JTextField(50);
        
        artworkTitleTF4.setEnabled(false);
        datePurchasedTF4.setEnabled(false);
        dateSoldTF4.setEnabled(false);
        artistTF4.setEnabled(false);
        customerTF4.setEnabled(false);
        purchasePriceTF4.setEnabled(false);
        sellingPriceTF4.setEnabled(false);
        
        JPanel artworkTitlePanel4 = new JPanel();
        JPanel datePurchasedPanel4 = new JPanel();
        JPanel dateSoldPanel4 = new JPanel();
        JPanel artistPanel4 = new JPanel();
        JPanel customerPanel4 = new JPanel();
        JPanel purchasePricePanel4 = new JPanel();
        JPanel sellingPricePanel4 = new JPanel();
        //JPanel archiveInventoryEmptyPanel = new JPanel();
        
        artworkTitlePanel4.setLayout(new GridLayout(1, 2));
        artworkTitlePanel4.add(artworkTitleLabel4);
        artworkTitlePanel4.add(artworkTitleTF4);
        
        datePurchasedPanel4.setLayout(new GridLayout(1, 2));
        datePurchasedPanel4.add(datePurchasedLabel4);
        datePurchasedPanel4.add(datePurchasedTF4);
        
        dateSoldPanel4.setLayout(new GridLayout(1, 2));
        dateSoldPanel4.add(dateSoldLabel4);
        dateSoldPanel4.add(dateSoldTF4);
        
        artistPanel4.setLayout(new GridLayout(1, 2));
        artistPanel4.add(artistLabel4);
        artistPanel4.add(artistTF4);
        
        customerPanel4.setLayout(new GridLayout(1, 2));
        customerPanel4.add(customerLabel4);
        customerPanel4.add(customerTF4);
        
        purchasePricePanel4.setLayout(new GridLayout(1, 2));
        purchasePricePanel4.add(purchasePriceLabel4);
        purchasePricePanel4.add(purchasePriceTF4);
        
        sellingPricePanel4.setLayout(new GridLayout(1, 2));
        sellingPricePanel4.add(sellingPriceLabel4);
        sellingPricePanel4.add(sellingPriceTF4);
        
        JPanel archiveInventoryJP = new JPanel();
        archiveInventoryJP.setLayout(new GridLayout(8, 1, 10, 10));
        archiveInventoryJP.add(artworkTitlePanel4);
        archiveInventoryJP.add(datePurchasedPanel4);
        archiveInventoryJP.add(dateSoldPanel4);
        archiveInventoryJP.add(purchasePricePanel4);
        archiveInventoryJP.add(artistPanel4);
        archiveInventoryJP.add(customerPanel4);
        archiveInventoryJP.add(sellingPricePanel4);
        //archiveInventoryJP.add(availableArtworkPurchase);
        
        archiveInventory.add(archiveInventoryJP, BorderLayout.CENTER);
        
        //jlist listener
        archiveArtworkTitleList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    if(archiveArtworkTitleList.getSelectedIndex() != -1){
                        artworkTitleTF4.setText(archiveArtworkTitleArr[archiveArtworkTitleList.getSelectedIndex()]);
                        datePurchasedTF4.setText(archiveArtworkDatePurchasedArr[archiveArtworkTitleList.getSelectedIndex()]);
                        dateSoldTF4.setText(archiveArtworkDateSoldArr[archiveArtworkTitleList.getSelectedIndex()]);
                        artistTF4.setText(archiveArtworkArtistArr[archiveArtworkTitleList.getSelectedIndex()]);
                        customerTF4.setText(archiveArtworkCustomerArr[archiveArtworkTitleList.getSelectedIndex()]);
                        purchasePriceTF4.setText(archiveArtworkPurchasePriceArr[archiveArtworkTitleList.getSelectedIndex()]);
                        sellingPriceTF4.setText(archiveArtworkSellingPriceArr[archiveArtworkTitleList.getSelectedIndex()]);
                        System.out.println(archiveArtworkTitleList.getSelectedIndex());
                    }
                }
            }
        });

        //end of archiveInventory
        
        
        //start of dialog button listeners
        
        //edit customer dialog buttons
        yes1.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               customerOperations("updateCustomer", customerListArr[customerNameList.getSelectedIndex()], phoneNoTF.getText().trim(), addressTA.getText().trim(), "null", artistPrefCB.getSelectedItem().toString().trim(), nameTF.getText().trim());
               initializeCustomerList();
               updateCustomerComboBox();
               editCustomerDialog.setVisible(false);
           }
        });
        
        no1.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               editCustomerDialog.setVisible(false);
           }
        });
        //delete customer dialog buttons
        yes2.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               customerOperations("deleteCustomer", customerListArr[customerNameList.getSelectedIndex()], phoneNoTF.getText().trim(), addressTA.getText().trim(), "null", artistPrefCB.getSelectedItem().toString().trim(), nameTF.getText().trim());
               initializeCustomerList();
               nameTF.setText("");
               phoneNoTF.setText("");
               addressTA.setText("");
               //artPurchasesTA.setText("");
               artistPrefCB.setSelectedItem(0);
               deleteCustomerDialog.setVisible(false);
           }
        });
        
        no2.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               deleteCustomerDialog.setVisible(false);
           }
        });
        
        //edit artist dialog buttons
        yes3.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               artistOperations("updateArtist", artistListArr[artistNameList.getSelectedIndex()], specialtyTF.getText().trim(), aliveCB.getSelectedItem().toString().trim(), priceRangeTF.getText().trim(), artistNameTF.getText().trim());
               initializeArtistList();
               updateArtistComboBox();
               editArtistDialog.setVisible(false);
           }
        });
        
        no3.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               editArtistDialog.setVisible(false);
           }
        });
        //delete artist dialog buttons
        yes4.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               artistOperations("deleteArtist", artistListArr[artistNameList.getSelectedIndex()], specialtyTF.getText().trim(), aliveCB.getSelectedItem().toString().trim(), priceRangeTF.getText().trim(), artistNameTF.getText().trim());
               initializeArtistList();
               artistNameTF.setText("");
               specialtyTF.setText("");
               aliveCB.setSelectedItem(0);
               priceRangeTF.setText("");
               deleteArtistDialog.setVisible(false);
           }
        });
        
        no4.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               deleteArtistDialog.setVisible(false);
           }
        });
        
        //edit artwork dialog buttons
        yes5.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               String dateSold;
               String sellingPrice;
               if(dateSoldTF.getText().trim().equals("")){
                   dateSold = "null";
               }else{
                   dateSold = dateSoldTF.getText().trim();
               }
               if(sellingPriceTF.getText().trim().equals("")){
                   sellingPrice = "null";
               }else{
                   sellingPrice = sellingPriceTF.getText().trim();
               }
               artworkOperations("updateArtwork", artworkListArr[artworkTitleList.getSelectedIndex()], datePurchasedTF.getText().trim(), dateSold, purchasePriceTF.getText().trim(), sellingPrice, artistCB.getSelectedItem().toString(), artworkTitleTF.getText().trim());
               initializeArtworkList();
               editArtworkDialog.setVisible(false);
           }
        });
        
        no5.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               editArtworkDialog.setVisible(false);
           }
        });
        //delete artwork dialog buttons
        yes6.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               artworkOperations("deleteArtwork", artworkListArr[artworkTitleList.getSelectedIndex()], datePurchasedTF.getText().trim(), dateSoldTF.getText().trim(), purchasePriceTF.getText().trim(), "", artistCB.getSelectedItem().toString(), artworkTitleTF.getText().trim());
               initializeArtworkList();
               artworkTitleTF.setText("");
               datePurchasedTF.setText("");
               purchasePriceTF.setText("");
               dateSoldTF.setText("");
               sellingPriceTF.setText("");
               artistCB.setSelectedItem(0);
               deleteArtworkDialog.setVisible(false);
           }
        });
        
        no6.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               deleteArtworkDialog.setVisible(false);
           }
        });
        
        //purchase artwork dialog buttons
        yes7.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               //parse artPurchases
               String artPurchasesString = artPurchasesArr[customerCB.getSelectedIndex()];
               if(artPurchasesString.equals("null")){
                   artPurchasesString = availableArtworkTitleArr[availableArtworkTitleList.getSelectedIndex()];
               }else if(!artPurchasesString.endsWith("/") && artPurchasesString.length() > 1){
                   artPurchasesString += "/" + availableArtworkTitleArr[availableArtworkTitleList.getSelectedIndex()];
               }
               
               purchaseArtwork(customerCB.getSelectedItem().toString(), artPurchasesString, availableArtworkTitleArr[availableArtworkTitleList.getSelectedIndex()], new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())), sellingPriceTF3.getText().trim());
               initializeCustomerList();
               initializeArtworkList();
               artworkTitleTF3.setText("");
               datePurchasedTF3.setText("");
               artistTF3.setText("");
               purchasePriceTF3.setText("");
               sellingPriceTF3.setText("");
               customerCB.setSelectedItem(-1);
               availableArtworkPurchaseDialog.setVisible(false);
           }
        });
        
        no7.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               availableArtworkPurchaseDialog.setVisible(false);
           }
        });
        
        //end of dialog button listeners
        
        
        //add customer tab
        customerTabbedPane.setTabPlacement(SwingConstants.TOP);
        
        customerTabbedPane.addTab("Customer List", viewCustomer);
        customerTabbedPane.addTab("Add Customer", addCustomer);
        
        //add artist tab
        artistTabbedPane.setTabPlacement(SwingConstants.TOP);
        
        artistTabbedPane.addTab("Artist List", viewArtist);
        artistTabbedPane.addTab("Add Artist", addArtist);
        artistTabbedPane.addTab("Artist & Their Artworks", artistArtworks);
        
        artistTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(artistTabbedPane.getSelectedIndex() == 2){
                    initializeArtistList();
                }else if(artistTabbedPane.getSelectedIndex() == 0){
                    if(showAliveArtist.isSelected()){
                        refreshArtistList("Yes");
                    }else if(showAllArtist.isSelected()){
                        initializeArtistList();
                    }
                }
                System.out.println("Tab: " + artistTabbedPane.getSelectedIndex());
            }
        });
        
        //add artwork tab
        artworkTabbedPane.setTabPlacement(SwingConstants.TOP);
        
        artworkTabbedPane.addTab("Artwork List", viewArtwork);
        artworkTabbedPane.addTab("Add Artwork", addArtwork);
        
        //add inventory tab
        inventoryTabbedPane.setTabPlacement(SwingConstants.TOP);
        
        inventoryTabbedPane.addTab("Current Inventory", currentInventory);
        inventoryTabbedPane.addTab("Archive Inventory", archiveInventory);
        
        //menu
        JMenuBar menuBar = new JMenuBar();
        JMenu customer = new JMenu("Customer");
        JMenu artist = new JMenu("Artist");
        JMenu artwork = new JMenu("Artwork");
        JMenu inventory = new JMenu("Inventory");
        menuBar.add(customer);
        menuBar.add(artist);
        menuBar.add(artwork);
        menuBar.add(inventory);
        setJMenuBar(menuBar);
        
        customer.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent e){
                remove(artistTabbedPane);
                remove(artworkTabbedPane);
                remove(inventoryTabbedPane);
                add(customerTabbedPane);
                revalidate();
                repaint();
                System.out.println("menuSelected");
            }
            
            @Override
            public void menuDeselected(MenuEvent e) {
                System.out.println("menuDeselected");

            }

            @Override
            public void menuCanceled(MenuEvent e) {
                System.out.println("menuCanceled");

            }
        });
        
        artist.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent e){
                remove(customerTabbedPane);
                remove(artworkTabbedPane);
                remove(inventoryTabbedPane);
                add(artistTabbedPane);
                revalidate();
                repaint();
                System.out.println("menuSelected");
            }
            
            @Override
            public void menuDeselected(MenuEvent e) {
                System.out.println("menuDeselected");

            }

            @Override
            public void menuCanceled(MenuEvent e) {
                System.out.println("menuCanceled");

            }
        });
        
        artwork.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent e){
                remove(artistTabbedPane);
                remove(customerTabbedPane);
                remove(inventoryTabbedPane);
                add(artworkTabbedPane);
                revalidate();
                repaint();
                System.out.println("menuSelected");
            }
            
            @Override
            public void menuDeselected(MenuEvent e) {
                System.out.println("menuDeselected");

            }

            @Override
            public void menuCanceled(MenuEvent e) {
                System.out.println("menuCanceled");

            }
        });
        
        inventory.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent e){
                remove(artistTabbedPane);
                remove(customerTabbedPane);
                remove(artworkTabbedPane);
                add(inventoryTabbedPane);
                revalidate();
                repaint();
                System.out.println("menuSelected");
            }
            
            @Override
            public void menuDeselected(MenuEvent e) {
                System.out.println("menuDeselected");

            }

            @Override
            public void menuCanceled(MenuEvent e) {
                System.out.println("menuCanceled");

            }
        });

        add(customerTabbedPane);
        
    }
    
    public static void main(String[] args){
        LatteGalleryClient lg = new LatteGalleryClient();
        lg.setSize(600, 350);
        lg.setVisible(true);
        lg.setTitle("Latte Gallery");
        lg.setLocationRelativeTo(null);
    }
    
    public void initializeCustomerList(){
        try {
            toServer.writeUTF("Customer");
            toServer.flush();
            
            //Get customer list
            String customerList = fromServer.readUTF();
            String phoneNumberList = fromServer.readUTF();
            String addressList = fromServer.readUTF();
            String artPurchasesList = fromServer.readUTF();
            String artistPrefList = fromServer.readUTF();
            
            System.out.println("customerList : " + customerList);
            System.out.println("phoneNumberList : " + phoneNumberList);
            
            customerListArr = customerList.split("\\|");
            customerListModel.removeAllElements();
            for(int i = 0; i < customerListArr.length; i++){
                customerListModel.addElement(customerListArr[i]);
            }
                        
            phoneNumberArr = phoneNumberList.split("\\|");
            addressArr = addressList.split("\\|");
            artPurchasesArr = artPurchasesList.split("\\|");
            artistPrefArr = artistPrefList.split("\\|");
            
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void initializeArtistList(){
        try {
            toServer.writeUTF("Artist");
            toServer.flush();
            
            //Get artist list
            String artistList = fromServer.readUTF();
            String specialtyList = fromServer.readUTF();
            String aliveList = fromServer.readUTF();
            String priceRange = fromServer.readUTF();
            
            artistListArr = artistList.split("\\|");
            artistListModel.removeAllElements();
            artistArtworksArtistListModel.removeAllElements();
            for(int i = 0; i < artistListArr.length; i++){
                artistListModel.addElement(artistListArr[i]);
                artistArtworksArtistListModel.addElement(artistListArr[i]);
            }
            
            specialtyArr = specialtyList.split("\\|");
            aliveArr = aliveList.split("\\|");
            priceRangeArr = priceRange.split("\\|");
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void initializeArtworkList(){
        try {
            toServer.writeUTF("Artwork");
            toServer.flush();
            
            //Get customer list
            String artworkList = fromServer.readUTF();
            String datePurchasedList = fromServer.readUTF();
            String dateSoldList = fromServer.readUTF();
            String purchasePriceList = fromServer.readUTF();
            String sellingPriceList = fromServer.readUTF();
            String artistList = fromServer.readUTF();
            
            artworkListArr = artworkList.split("\\|");
            artworkListModel.removeAllElements();
            for(int i = 0; i < artworkListArr.length; i++){
                artworkListModel.addElement(artworkListArr[i]);
            }
            
            datePurchasedArr = datePurchasedList.split("\\|");
            dateSoldArr = dateSoldList.split("\\|");
            purchasePriceArr = purchasePriceList.split("\\|");
            sellingPriceArr = sellingPriceList.split("\\|");
            artistArr = artistList.split("\\|");
            //System.out.println("initializeArtowkrList");
            getAvailableArtwork();
            getSoldArtwork();
            
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void customerOperations(String operation, String name, String phoneNumber, String address, String artPurchases, String artistPref, String newName){
        try{
            toServer.writeUTF(operation);
            toServer.writeUTF(name);
            toServer.writeUTF(phoneNumber);
            toServer.writeUTF(address);
            toServer.writeUTF(artPurchases);
            toServer.writeUTF(artistPref);
            toServer.writeUTF(newName);
            toServer.flush();
        }catch(IOException e){
            
        }
    }
    
    public void artistOperations(String operation, String name, String specialty, String alive, String priceRange, String newName){
        try{
            toServer.writeUTF(operation);
            toServer.writeUTF(name);
            toServer.writeUTF(specialty);
            toServer.writeUTF(alive);
            toServer.writeUTF(priceRange);
            toServer.writeUTF(newName);
            toServer.flush();
        }catch(IOException e){
            
        }
    }
    
    public void artworkOperations(String operation, String title, String datePurchased, String dateSold, String purchasePrice, String sellingPrice, String artist, String newTitle){
        try{
            toServer.writeUTF(operation);
            toServer.writeUTF(title);
            toServer.writeUTF(datePurchased);
            toServer.writeUTF(dateSold);
            toServer.writeUTF(purchasePrice);
            toServer.writeUTF(sellingPrice);
            toServer.writeUTF(artist);
            toServer.writeUTF(newTitle);
            toServer.flush();
        }catch(IOException e){
            
        }
    }
    
    public void purchaseArtwork(String name, String artPurchases, String title, String dateSold, String sellingPrice){
        try{
            toServer.writeUTF("purchaseArtwork");
            toServer.writeUTF(name);
            toServer.writeUTF(artPurchases);
            toServer.writeUTF(title);
            toServer.writeUTF(dateSold);
            toServer.writeUTF(sellingPrice);
            toServer.flush();
        }catch(IOException e){
            
        }
    }
    
    public boolean validDate(String date){
        String dateFormat = "dd/MM/yyyy";
        try {
                DateFormat df = new SimpleDateFormat(dateFormat);
                df.setLenient(false);
                df.parse(date);
                return true;
            } catch (ParseException e) {
                return false;
            }
    }
    
    public void updateArtistComboBox(){
        initializeArtistList();
        model = new DefaultComboBoxModel(artistListArr);
        artistPrefCB.setModel(model);
        artistPrefCB1.setModel(model);
        artistCB.setModel(model);
        artistCB1.setModel(model);
    }
    
    public void populateArtPurchasesList(int code){
        //art purchases list in viewCustomer tab
        String[][] artPurchasesArrArr = new String[artPurchasesArr.length][];
        for(int i = 0; i < artPurchasesArr.length; i++){
        artPurchasesArrArr[i] = artPurchasesArr[i].split("/");
        }
        artPurchasesListModel.removeAllElements();
        for(int i = 0; i < artPurchasesArrArr[code].length; i++){
            if(artPurchasesArrArr[code][i].equals("null")){
                //artPurchasesListModel.addElement("");
            }else{
                artPurchasesListModel.addElement(artPurchasesArrArr[code][i]);
            }
        }
    }
    
    public void getArtworkByArtist(String artist){
        try {
            toServer.writeUTF("getArtworkByArtist");
            toServer.writeUTF(artist);
            
            //get artwork by artist
            String artistArtworkTitleList = fromServer.readUTF();
            String artistArtworkDatePurchasedList = fromServer.readUTF();
            String artistArtworkDateSoldList = fromServer.readUTF();
            String artistArtworkPurchasePriceList = fromServer.readUTF();
            String artistArtworksSellingPriceList = fromServer.readUTF();
            
            artistArtworkTitleArr = artistArtworkTitleList.split("\\|");
            
            //initialize customer3D
            customer3D = new String[artistListArr.length][artistArtworkTitleArr.length][1];
            
            for(int i = 0; i < artistListArr.length; i++){
                for(int j = 0; j < artistArtworkTitleArr.length; j++){
                    customer3D[i][j][0] = getCustomerByArtwork(artistArtworkTitleArr[j]);
                    System.out.println("lol " + customer3D[i][j][0]);
                }
            }
            
            artistArtworksArtworkListModel.removeAllElements();
            for(int i = 0; i < artistArtworkTitleArr.length; i++){
                artistArtworksArtworkListModel.addElement(artistArtworkTitleArr[i]);
            }

            artistArtworkDatePurchasedArr = artistArtworkDatePurchasedList.split("\\|");
            artistArtworkDateSoldArr = artistArtworkDateSoldList.split("\\|");
            artistArtworksPurchasePriceArr = artistArtworkPurchasePriceList.split("\\|");
            artistArtworksSellingPriceArr = artistArtworksSellingPriceList.split("\\|");
            
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void getAvailableArtwork(){
        try {
            toServer.writeUTF("getAvailableArtwork");
            
            //get available artwork
            String titleList = fromServer.readUTF();
            String datePurchasedList = fromServer.readUTF();
            String purchasePriceList = fromServer.readUTF();
            String artistList = fromServer.readUTF();
            
            availableArtworkTitleArr = titleList.split("\\|");
            availableArtworkTitleListModel.removeAllElements();
            for(int i = 0; i < availableArtworkTitleArr.length; i++){
                availableArtworkTitleListModel.addElement(availableArtworkTitleArr[i]);
            }
            
            availableArtworkDatePurchasedArr = datePurchasedList.split("\\|");
            availableArtworkPurchasePriceArr = purchasePriceList.split("\\|");
            availableArtworkArtistArr = artistList.split("\\|");
            
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getSoldArtwork(){
        try {
            toServer.writeUTF("getSoldArtwork");
            
            //get available artwork
            String titleList = fromServer.readUTF();
            String datePurchasedList = fromServer.readUTF();
            String dateSoldList = fromServer.readUTF();
            String purchasePriceList = fromServer.readUTF();
            String sellingPriceList = fromServer.readUTF();
            String artistList = fromServer.readUTF();
            
            archiveArtworkTitleArr = titleList.split("\\|");
            
            archiveArtworkCustomerArr = new String[archiveArtworkTitleArr.length];
            for(int i = 0; i< archiveArtworkTitleArr.length; i++){
                archiveArtworkCustomerArr[i] = getCustomerByArtwork(archiveArtworkTitleArr[i]);
                System.out.println("lol " + archiveArtworkCustomerArr[i]);
            }
            
            archiveArtworkTitleListModel.removeAllElements();
            for(int i = 0; i < archiveArtworkTitleArr.length; i++){
                archiveArtworkTitleListModel.addElement(archiveArtworkTitleArr[i]);
            }
            
            archiveArtworkDatePurchasedArr = datePurchasedList.split("\\|");
            archiveArtworkDateSoldArr = dateSoldList.split("\\|");
            archiveArtworkPurchasePriceArr = purchasePriceList.split("\\|");
            archiveArtworkSellingPriceArr = sellingPriceList.split("\\|");
            archiveArtworkArtistArr = artistList.split("\\|");
            
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateCustomerComboBox(){
        initializeCustomerList();
        customerModel = new DefaultComboBoxModel(customerListArr);
        customerCB.setModel(customerModel);
    }
    
    public String getCustomerByArtwork(String title){
        try {
            toServer.writeUTF("getCustomerByArtwork");
            toServer.writeUTF(title);
            
            //get customer
            String customer = fromServer.readUTF();
            return customer;
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
            return "null";
        }
    }
    
    public void refreshArtistList(String alive){
        try {
            toServer.writeUTF("refreshArtistList");
            toServer.writeUTF(alive);
            toServer.flush();
            
            //Get artist list
            String artistList = fromServer.readUTF();
            String specialtyList = fromServer.readUTF();
            String aliveList = fromServer.readUTF();
            String priceRange = fromServer.readUTF();
            
            artistListArr = artistList.split("\\|");
            artistListModel.removeAllElements();
            for(int i = 0; i < artistListArr.length; i++){
                artistListModel.addElement(artistListArr[i]);
            }
            
            specialtyArr = specialtyList.split("\\|");
            aliveArr = aliveList.split("\\|");
            priceRangeArr = priceRange.split("\\|");
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void refreshArtworkList(){
        try {
            toServer.writeUTF("refreshArtworkList");
            toServer.flush();
            
            //Get artist list
            String titleList = fromServer.readUTF();
            String datePurchasedList = fromServer.readUTF();
            String artistList = fromServer.readUTF();
            
            availableArtworkTitleArr = titleList.split("\\|");
            availableArtworkTitleListModel.removeAllElements();
            for(int i = 0; i < availableArtworkTitleArr.length; i++){
                availableArtworkTitleListModel.addElement(availableArtworkTitleArr[i]);
            }
            
            availableArtworkDatePurchasedArr = datePurchasedList.split("\\|");
            availableArtworkArtistArr = artistList.split("\\|");
        } catch (IOException ex) {
            Logger.getLogger(LatteGalleryClient.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
