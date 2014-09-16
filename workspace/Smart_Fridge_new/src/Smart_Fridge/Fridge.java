package Smart_Fridge;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import java.awt.Container;	
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;



public class Fridge extends JFrame implements Serializable{
    
	private Date current_date;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private List <Item> shopping_list = new ArrayList <Item>();
	private List <Item> discard_list =new ArrayList <Item>();
	private int temperature;
	private Container contents;
	private JButton add_contents,checkout,set_thresholds,generate_shopping_list,get_notifications,set_temp;
	private JLabel message = new JLabel();
	
    private boolean aflag=false,meflag=false,miflag=false,orflag=false,eflag=false;
	
	List <Item> apple_list;
	List <Item> egg_list;
	List <Item> meat_list;
	List <Item> milk_list; 
	List <Item> orange_list;     
	Item item;
	
	JPanel panel = new JPanel();
    
	private int min_thresh_apple,min_thresh_egg,min_thresh_meat,min_thresh_milk,min_thresh_orange;
	private int max_thresh_apple,max_thresh_egg,max_thresh_meat,max_thresh_milk,max_thresh_orange;
	
    FileOutputStream fos;
    ObjectOutputStream oos; 

	
	//Constructor
	public Fridge () {	
	  super("Welcome to your smart fridge.");
	  contents = getContentPane( );
	  contents.setLayout( new FlowLayout( ) );

	  current_date =new Date();
      temperature =0;
 
      JLabel welcome = new JLabel();      
      
      //Window components
      add_contents = new JButton("Add Contents");
      checkout = new JButton("Checkout");
      set_thresholds=new JButton("Set Thresholds");
      generate_shopping_list=new JButton("Generate Shopping List");
      get_notifications= new JButton("Get Notifications");
      set_temp=new JButton("Set Temperature");
      
      //add components to the window
      contents.add(add_contents);
      contents.add(checkout);
      contents.add(set_thresholds);
      contents.add(generate_shopping_list);
      contents.add(get_notifications);
      contents.add(set_temp);
      
      setSize(300,200);
      setLocation(500,280);
      panel.setLayout (null); 

      welcome.setBounds(70,50,150,60);

      panel.add(welcome);
      getContentPane().add(panel);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
      
     
	    //Register the buttons for actionlistener
        ButtonHandler bh = new ButtonHandler( );
	    // add event handler as listener for all buttons
	    add_contents.addActionListener( bh );
	    checkout.addActionListener( bh );
	    set_thresholds.addActionListener(bh);
	    generate_shopping_list.addActionListener(bh);
	    get_notifications.addActionListener(bh);
	    set_temp.addActionListener(bh);
	    
	    setSize( 500,500 );
	    setVisible( true );
	    
	    //Dummy file to avoid filenotfound exception
	    try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "size.txt",true));
            writer.write( "");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    
	   		       
	}
	
	    private class ButtonHandler implements ActionListener  {
		// implement actionPerformed method
		public void actionPerformed( ActionEvent ae )
		throws NumberFormatException {

		 try    {
		  // identify which button was pressed
		  if ( ae.getSource( ) == add_contents) {
			  Drop_down d1 = new Drop_down("Apples", "Oranges", "Milk", "Meat", "Eggs");
			  String result =d1.get_result();
			 
			  //Switch for the result of the drop down selection
			  
			  if (result=="Apples") {
				  
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
				  int item_count=Integer.parseInt(item_str);
				  System.out.println("Count is " + item_count);				  
				  String pdate = JOptionPane.showInputDialog( null, "Please enter date in dd-mm-yyyy format" );
				  int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));//read no. of apples from size.txt file
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("apple".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  int count =size+item_count;//upddate size of apple list
				  System.out.println("Count is " +count);
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));//get max threshold for apples
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("apple".equals(split[0])) {
							String [] split_new =split[1].split("/");
							max_thresh_apple=Integer.parseInt(split_new[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  if (size+item_count<max_thresh_apple) {//if max threshold is not reached yet
					  if(!aflag) {//check if apples are already present
						  System.out.println("Flag is " +aflag);
						  apple_list=new ArrayList <Item>();
					  }
					  
					  else {
						  System.out.println("Flag is " +aflag);
						   apple_list=new ArrayList <Item>();
						  try {
							  String current_line;
							  BufferedReader br = new BufferedReader(new FileReader("apple.txt"));
							   while ((current_line = br.readLine()) != null) {
								System.out.println("Current line is " +current_line);
								String[] split = current_line.split("/");
								System.out.println("SPlit is succesful");
								String pur_date=split[0];
								int temp=Integer.parseInt(split[1]);
								String name=split[2];
								Apples apple=new Apples();
								apple.set_purchase_date(pur_date);//build apple arraylist from apple.txt file
								apple.set_expiry_date(pur_date);
								apple.set_name(name);
								apple.set_temperature(temp);
								apple_list.add(apple);
							   }
							   br.close();
							  }
							  
							  catch (IOException fe) {
								   fe.printStackTrace();
								   
							   }

					  }
					  
						int temp = Integer.parseInt(JOptionPane.showInputDialog( null, "Please enter the temperature threshold for apples in Celcius scale" ));  
					  
						String message= "Following items are added\n";
						for (int i=size;i<size+item_count;i++) {
					 item=new Apples ();
					 item.set_purchase_date(pdate);
					 item.set_expiry_date(pdate);
					 item.set_temperature(temp);
					 item.set_name("Apple_no_"+i);
					  apple_list.add(item);
					  message=message+item.get_name()+"\n";
					  System.out.println("apple_list(i) is " +apple_list.get(i));
					//write the apple list to a text file  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "apple.txt",true));
			                System.out.println("Purchase date is "+pdate);
			                writer.write( pdate + "/"+item.get_temperature()+"/"+item.get_name()+"\n");
			                writer.close();
			            } 
					  catch (IOException e) {
			                e.printStackTrace();
			       }
				  }	 
				  try {
		                BufferedWriter writer = new BufferedWriter(new FileWriter(
		                        "size.txt",true));
		                writer.write( "apple:"+ count+ "\n");//update apple list sixe in size.txt file
		                writer.close();
		            } 
				  catch (IOException e) {
		                e.printStackTrace();
		       }
				  Message_pop.infoBox(message, "Info");
				  Message_pop.infoBox("Inventory Updated. New Items added", "Info");
				  
			  }
				
				  //if max threshold already hit do not add any further items.
			  else {
				  Message_pop.infoBox("Cannot add!!! Max threshold reached/threshold not set", "Error");
			  }

		 }  
			  
			  
			  else  if (result=="Oranges") {
				  
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
				  int item_count=Integer.parseInt(item_str);
				  System.out.println("Count is " + item_count);

				  String pdate = JOptionPane.showInputDialog( null, "Please enter date in dd-mm-yyyy format" );
				  System.out.println("pdate is " +pdate);
				  int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("orange".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  int count =size+item_count;
				  System.out.println("Count is " +count);
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("orange".equals(split[0])) {
							String [] split_new =split[1].split("/");
							max_thresh_orange=Integer.parseInt(split_new[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  if (size+item_count<max_thresh_orange) {
					  if(!orflag) {
						  System.out.println("Flag is " +orflag);
						  orange_list=new ArrayList <Item>();
					  }
					  
					  else {
						  System.out.println("Flag is " +orflag);
						   orange_list=new ArrayList <Item>();
						  try {
							  String current_line;
							  BufferedReader br = new BufferedReader(new FileReader("orange.txt"));
							   while ((current_line = br.readLine()) != null) {
								System.out.println("Current line is " +current_line);
								String[] split = current_line.split("/");
								System.out.println("SPlit is succesful");
								String pur_date=split[0];
								int temp=Integer.parseInt(split[1]);
								String name=split[2];
								Oranges orange=new Oranges();
								orange.set_purchase_date(pur_date);
								orange.set_expiry_date(pur_date);
								orange.set_name(name);
								orange.set_temperature(temp);
								orange_list.add(orange);
							   }
							   br.close();
							  }
							  
							  catch (IOException fe) {
								   fe.printStackTrace();
								   
							   }

					  }
					  
						int temp = Integer.parseInt(JOptionPane.showInputDialog( null, "Please enter the temperature threshold for apples in Celcius scale" ));  

					String message="Following items have been added\n";	
					 for (int i=size;i<size+item_count;i++) {
					 item=new Oranges ();
					 item.set_purchase_date(pdate);
					 item.set_expiry_date(pdate);
					 item.set_temperature(temp);
					 item.set_name("orange_no_"+i);
					 message=message+item.get_name()+"\n";
					  orange_list.add(item);
					  System.out.println("orange_list(i) is " +orange_list.get(i));
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "orange.txt",true));
			                writer.write( pdate + "/"+item.get_temperature()+"/"+item.get_name()+"\n");
			                writer.close();
			            } 
					  catch (IOException e) {
			                e.printStackTrace();
			       }
				  }	 
				  try {
		                BufferedWriter writer = new BufferedWriter(new FileWriter(
		                        "size.txt",true));
		                writer.write( "orange:"+ count+ "\n");
		                writer.close();
		            } 
				  catch (IOException e) {
		                e.printStackTrace();
		       }
				  
				  Message_pop.infoBox(message, "info");
				  Message_pop.infoBox("Inventory Updated. New Items added", "Info");

			  }
				  
			  else {
				  Message_pop.infoBox("Cannot add!!! Max threshold reached/threshold not set", "Error");
			  }

		 } 
			  
			  
			  else if (result=="Meat") {
				  
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
				  int item_count=Integer.parseInt(item_str);
				  System.out.println("Count is " + item_count);

				  
				  String pdate = JOptionPane.showInputDialog( null, "Please enter date in dd-mm-yyyy format" );
				  				  
				  int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("meat".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  int count =size+item_count;
				  System.out.println("Count is " +count);
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("meat".equals(split[0])) {
							String [] split_new =split[1].split("/");
							max_thresh_meat=Integer.parseInt(split_new[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  if (size+item_count<max_thresh_meat) {
					  if(!meflag) {
						  System.out.println("Flag is " +meflag);
						  meat_list=new ArrayList <Item>();
					  }
					  
					  else {
						  System.out.println("Flag is " +meflag);
						   meat_list=new ArrayList <Item>();
						  try {
							  String current_line;
							  BufferedReader br = new BufferedReader(new FileReader("meat.txt"));
							   while ((current_line = br.readLine()) != null) {
								System.out.println("Current line is " +current_line);
								String[] split = current_line.split("/");
								System.out.println("SPlit is succesful");
								String pur_date=split[0];
								int temp=Integer.parseInt(split[1]);
								String name=split[2];
								Meat meat=new Meat();
								meat.set_purchase_date(pur_date);
								meat.set_expiry_date(pur_date);
								meat.set_name(name);
								meat.set_temperature(temp);
								meat_list.add(meat);
							   }
							   br.close();
							  }
							  
							  catch (IOException fe) {
								   fe.printStackTrace();
								   
							   }

					  }
					  
						int temp = Integer.parseInt(JOptionPane.showInputDialog( null, "Please enter the temperature threshold for apples in Celcius scale" ));  
					  
				    String message="Following items have been added\n";
					for (int i=size;i<size+item_count;i++) {
					 item=new Meat ();
					 item.set_purchase_date(pdate);
					 item.set_expiry_date(pdate);
					 item.set_temperature(temp);
					 item.set_name("meat_no_"+i);
					 message=message+item.get_name()+"\n";
					 
					  meat_list.add(item);
					  System.out.println("meat_list(i) is " +meat_list.get(i));
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "meat.txt",true));
			                writer.write( pdate + "/"+item.get_temperature()+"/"+item.get_name()+"\n");
			                writer.close();
			            } 
					  catch (IOException e) {
			                e.printStackTrace();
			       }
				  }	 
				  try {
		                BufferedWriter writer = new BufferedWriter(new FileWriter(
		                        "size.txt",true));
		                writer.write( "meat:"+ count+ "\n");
		                writer.close();
		            } 
				  catch (IOException e) {
		                e.printStackTrace();
		       }
				  Message_pop.infoBox(message, "Info");
				  Message_pop.infoBox("Inventory Updated. New Items added", "Info");
 
			  }
				  
			  else {
				  Message_pop.infoBox("Cannot add!!! Max threshold reached/threshold not set", "Error");
			  }

		 } 
			  
			  
			  else if (result=="Milk") {
				  
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
				  int item_count=Integer.parseInt(item_str);
				  System.out.println("Count is " + item_count);
				  String pdate = JOptionPane.showInputDialog( null, "Please enter date in dd-mm-yyyy format" );
				  
				  int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("milk".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  int count =size+item_count;
				  System.out.println("Count is " +count);
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("milk".equals(split[0])) {
							String [] split_new =split[1].split("/");
							max_thresh_milk=Integer.parseInt(split_new[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  if (size+item_count<max_thresh_milk) {
					  if(!miflag) {
						  System.out.println("Flag is " +miflag);
						  milk_list=new ArrayList <Item>();
					  }
					  
					  else {
						  System.out.println("Flag is " +miflag);
						   milk_list=new ArrayList <Item>();
						  try {
							  String current_line;
							  BufferedReader br = new BufferedReader(new FileReader("milk.txt"));
							   while ((current_line = br.readLine()) != null) {
								System.out.println("Current line is " +current_line);
								String[] split = current_line.split("/");
								System.out.println("SPlit is succesful");
								String pur_date=split[0];
								int temp=Integer.parseInt(split[1]);
								String name=split[2];
								Milk milk=new Milk();
								milk.set_purchase_date(pur_date);
								milk.set_expiry_date(pur_date);
								milk.set_name(name);
								milk.set_temperature(temp);
								milk_list.add(milk);
							   }
							   br.close();
							  }
							  
							  catch (IOException fe) {
								   fe.printStackTrace();
								   
							   }

					  }
					  
						int temp = Integer.parseInt(JOptionPane.showInputDialog( null, "Please enter the temperature threshold for apples in Celcius scale" ));  
					  
				     String message="Following items have been added\n";
					 for (int i=size;i<size+item_count;i++) {
					 item=new Milk ();
					 item.set_purchase_date(pdate);
					 item.set_expiry_date(pdate);
					 item.set_temperature(temp);
					 item.set_name("milk_no_"+i);
					 message=message+item.get_name()+"\n";
					 
					 
					  milk_list.add(item);
					  System.out.println("milk_list(i) is " +milk_list.get(i));
						  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "milk.txt",true));
			                writer.write( pdate + "/"+item.get_temperature()+"/"+item.get_name()+"\n");
			                writer.close();
			            } 
					  catch (IOException e) {
			                e.printStackTrace();
			       }
				  }	 
				  try {
		                BufferedWriter writer = new BufferedWriter(new FileWriter(
		                        "size.txt",true));
		                writer.write( "milk:"+ count+ "\n");
		                writer.close();
		            } 
				  catch (IOException e) {
		                e.printStackTrace();
		       }
				  Message_pop.infoBox(message,"Info");
				  Message_pop.infoBox("Inventory Updated. New Items added", "Info");

			  }
				  
			  else {
				  Message_pop.infoBox("Cannot add!!! Max threshold reached/threshold not set", "Error");
			  }

		 } 
			  
			  
			  else if (result=="Eggs") {
				  
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
				  int item_count=Integer.parseInt(item_str);
				  System.out.println("Count is " + item_count);

				    
				  String pdate = JOptionPane.showInputDialog( null, "Please enter date in dd-mm-yyyy format" );
				  
				  int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("egg".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  int count =size+item_count;
				  System.out.println("Count is " +count);
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("egg".equals(split[0])) {
							String [] split_new =split[1].split("/");
							max_thresh_egg=Integer.parseInt(split_new[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }
				  
				  if (size+item_count<max_thresh_egg) {
					  if(!eflag) {
						  System.out.println("Flag is " +eflag);
						  egg_list=new ArrayList <Item>();
					  }
					  
					  else {
						  System.out.println("Flag is " +eflag);
						   egg_list=new ArrayList <Item>();
						  try {
							  String current_line;
							  BufferedReader br = new BufferedReader(new FileReader("egg.txt"));
							   while ((current_line = br.readLine()) != null) {
								System.out.println("Current line is " +current_line);
								String[] split = current_line.split("/");
								System.out.println("SPlit is succesful");
								String pur_date=split[0];
								int temp=Integer.parseInt(split[1]);
								String name=split[2];
								Eggs egg=new Eggs();
								egg.set_purchase_date(pur_date);
								egg.set_expiry_date(pur_date);
								egg.set_name(name);
								egg.set_temperature(temp);
								egg_list.add(egg);
							   }
							   br.close();
							  }
							  
							  catch (IOException fe) {
								   fe.printStackTrace();
								   
							   }

					  }
					  
						int temp = Integer.parseInt(JOptionPane.showInputDialog( null, "Please enter the temperature threshold for apples in Celcius scale" ));  
					  
				    String message="Following items have been added\n";
					for (int i=size;i<size+item_count;i++) {
					 item=new Eggs ();
					 item.set_purchase_date(pdate);
					 item.set_expiry_date(pdate);
					 item.set_temperature(temp);
					 item.set_name("egg_no_"+i);
					 
					 message=message+item.get_name()+"\n";
					  egg_list.add(item);
					  System.out.println("egg_list(i) is " +egg_list.get(i));
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "egg.txt",true));
			                writer.write( pdate + "/"+item.get_temperature()+"/"+item.get_name()+"\n");
			                writer.close();
			            } 
					  catch (IOException e) {
			                e.printStackTrace();
			       }
				  }	 
				  try {
		                BufferedWriter writer = new BufferedWriter(new FileWriter(
		                        "size.txt",true));
		                writer.write( "egg:"+ count+ "\n");
		                writer.close();
		            } 
				  catch (IOException e) {
		                e.printStackTrace();
		       }
				  Message_pop.infoBox(message, "Info");
				  Message_pop.infoBox("Inventory Updated. New Items added", "Info");

			  }
				  
			  else {
				  Message_pop.infoBox("Cannot add!!! Max threshold reached/threshold not set", "Error");
			  }

		 }   			 
		 }  	    
		  
		  else if ( ae.getSource( ) == checkout ) {
			  Drop_down d1 = new Drop_down("Apples", "Oranges", "Milk", "Meat", "Eggs");
			  String result =d1.get_result();
			  
			  if(result=="Apples") {
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");//get the no. of apples to be removed
				  String item_str=chw.get_result();
 int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("apple".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }

				  int item_count=Integer.parseInt(item_str);
				  System.out.println("No. of apples is " +size);	
				  
				  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));//get the min threshold
				   while ((current_line = br.readLine()) != null) {
					System.out.println("Cuurent line: " + current_line);   
					String[] split = current_line.split(":");
					System.out.println("Split[0]: "+ split[0]+ " Split[1]: " +split[1]);
					if ("apple".equals(split[0])) {
						System.out.println("Found apple");
						String [] split_new =split[1].split("/");
						min_thresh_apple=Integer.parseInt(split_new[0]);
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
				  
				  System.out.println("Min threshold for apple is " + min_thresh_apple);
				  if (size-item_count<min_thresh_apple) {//if min threshold is reached do not checkout
					  System.out.println("Cannot checkout!!! Min threshold reached/threshold not set");
				      Message_pop.infoBox("Cannot checkout!!! Min threshold reached/threshold not set", "Error");
				  }
				  
				  else {
					  System.out.println("Count is " +size);
					    apple_list=new ArrayList <Item>();
					   String pur_date=null;
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("apple.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split("/");
							 pur_date=split[0];
							int temp=Integer.parseInt(split[1]);
							String name=split[2];
							Apples apple=new Apples();
							apple.set_purchase_date(pur_date);
							apple.set_expiry_date(pur_date);
							apple.set_name(name);
							apple.set_temperature(temp);
							apple_list.add(apple);//Build apple array list from apple.txt file
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }

					  
					  for (int i=item_count; i>=0;i--) {//remove no.of objects from the arraylist as per selection
						  System.out.println("apple_list(i) is "+ apple_list.get(i));
						  apple_list.remove(i);
						  System.out.println("Size is " +apple_list.size());
					  }	
					  
					 
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split(":");
							if ("apple".equals(split[0])) {
								size=Integer.parseInt(split[1]);
							}
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }
                       System.out.println("Size of apple list is " + size);
                       size=size-item_count;//update size of apple arraylist
					  String current_line;
					  File input_file=new File("apple.txt");
					  File temp_file = new File("tmp.txt");
              	    Scanner sc = new Scanner((input_file));   
              	    int i=0;
                       while(((sc.hasNextLine())) && i<size)  {
                       	   try {
                       		  current_line=sc.nextLine(); 
                       		  System.out.println("Current line is "+current_line);
   			                BufferedWriter writer = new BufferedWriter(new FileWriter(
   			                        temp_file,true));//update the apple.txt file as per the current size.
   			               writer.write( current_line);
   			                writer.write("\n");
    	                     writer.close();
   			                i++;
                    	   }
   					  catch (IOException e) {
   			                e.printStackTrace();
   			       }
                    	   
                       }
                       boolean succ=temp_file.renameTo(input_file);
                       
                       System.out.println("FIle rename result is "+succ);
                       
					  System.out.println("Size of apples list is " +size);
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "size.txt",true));
			                writer.write( "apple:"+ size+"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
                     Message_pop.infoBox("Enjoy your meal. Inventory updated", "Info");
				  }
			  }
			  
			  			  
			  
			 else if(result=="Oranges") {
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
 int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("orange".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }

				  int item_count=Integer.parseInt(item_str);
				  System.out.println("No. of oranges is " +size);	
				  
				  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					System.out.println("Cuurent line: " + current_line);   
					String[] split = current_line.split(":");
					System.out.println("Split[0]: "+ split[0]+ " Split[1]: " +split[1]);
					if ("orange".equals(split[0])) {
						System.out.println("Found orange");
						String [] split_new =split[1].split("/");
						min_thresh_orange=Integer.parseInt(split_new[0]);
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
				  
				  System.out.println("Min threshold for orange is " + min_thresh_orange);
				  if (size-item_count<min_thresh_orange) {
					  System.out.println("Cannot checkout!!! Min threshold reached/threshold not set");
				      Message_pop.infoBox("Cannot checkout!!! Min threshold reached/threshold not set", "Error");
				  }
				  
				  else {
					  System.out.println("Count is " +size);
					 		   orange_list=new ArrayList <Item>();
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("orange.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split("/");
							String pdate=split[0];
							int temp=Integer.parseInt(split[1]);
							String name=split[2];
							Oranges orange=new Oranges();
							orange.set_purchase_date(pdate);
							orange.set_expiry_date(pdate);
							orange.set_name(name);
							orange.set_temperature(temp);
							orange_list.add(orange);
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }

					  
					  for (int i=item_count; i>=0;i--) {
						  System.out.println("orange_list(i) is "+ orange_list.get(i));
						  orange_list.remove(i);
						  System.out.println("Size is " +orange_list.size());
					  }	
					  
					 	  
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split(":");
							if ("orange".equals(split[0])) {
								size=Integer.parseInt(split[1]);
							}
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }
                       System.out.println("Size of orange list is " + size);
                       size=size-item_count;
                       String current_line;
 					  File input_file=new File("orange.txt");
 					  File temp_file = new File("tmp.txt");
               	    Scanner sc = new Scanner((input_file));   
               	    int i=0;
                        while(((sc.hasNextLine())) && i<size)  {
                        	   try {
                        		  current_line=sc.nextLine(); 
                        		  System.out.println("Current line is "+current_line);
    			                BufferedWriter writer = new BufferedWriter(new FileWriter(
    			                        temp_file,true));
    			               writer.write( current_line);
    			                writer.write("\n");
     	                     writer.close();
    			                i++;
                     	   }
    					  catch (IOException e) {
    			                e.printStackTrace();
    			       }
                     	   
                        }
                        boolean succ=temp_file.renameTo(input_file);

                        
                        System.out.println("FIle rename result is "+succ);

					  
					  System.out.println("Size of oranges list is " +size);
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "size.txt",true));
			                writer.write( "orange:"+ size+"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
	                     Message_pop.infoBox("Enjoy your meal. Inventory updated", "Info");

				  }
			  }
			  
			  
			 else if(result=="Meat") {
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
 int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("meat".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }

				  int item_count=Integer.parseInt(item_str);
				  System.out.println("No. of meats is " +size);	
				  
				  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					System.out.println("Cuurent line: " + current_line);   
					String[] split = current_line.split(":");
					System.out.println("Split[0]: "+ split[0]+ " Split[1]: " +split[1]);
					if ("meat".equals(split[0])) {
						System.out.println("Found meat");
						String [] split_new =split[1].split("/");
						min_thresh_meat=Integer.parseInt(split_new[0]);
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
				  
				  System.out.println("Min threshold for meat is " + min_thresh_meat);
				  if (size-item_count<min_thresh_meat) {
					  System.out.println("Cannot checkout!!! Min threshold reached/threshold not set");
				      Message_pop.infoBox("Cannot checkout!!! Min threshold reached/threshold not set", "Error");
				  }
				  
				  else {
					  System.out.println("Count is " +size);
					   meat_list=new ArrayList <Item>();
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("meat.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split("/");
							String pdate=split[0];
							int temp=Integer.parseInt(split[1]);
							String name=split[2];
							Meat meat=new Meat();
							meat.set_purchase_date(pdate);
							meat.set_expiry_date(pdate);
							meat.set_name(name);
							meat.set_temperature(temp);
							meat_list.add(meat);
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }

					  
					  for (int i=item_count; i>=0;i--) {
						  System.out.println("meat_list(i) is "+ meat_list.get(i));
						  meat_list.remove(i);
						  System.out.println("Size is " +meat_list.size());
					  }	
					  
					   
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split(":");
							if ("meat".equals(split[0])) {
								size=Integer.parseInt(split[1]);
							}
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }
                       System.out.println("Size of meat list is " + size);
                       size=size-item_count;
					 
                       String current_line;
 					  File input_file=new File("meat.txt");
 					  File temp_file = new File("tmp.txt");
               	    Scanner sc = new Scanner((input_file));   
               	    int i=0;
                        while(((sc.hasNextLine())) && i<size)  {
                        	   try {
                        		  current_line=sc.nextLine(); 
                        		  System.out.println("Current line is "+current_line);
    			                BufferedWriter writer = new BufferedWriter(new FileWriter(
    			                        temp_file,true));
    			               writer.write( current_line);
    			                writer.write("\n");
     	                     writer.close();
    			                i++;
                     	   }
    					  catch (IOException e) {
    			                e.printStackTrace();
    			       }
                     	   
                        }
                        boolean succ=temp_file.renameTo(input_file);

                        
                        System.out.println("FIle rename result is "+succ);

					  
					  System.out.println("Size of meats list is " +size);
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "size.txt",true));
			                writer.write( "meat:"+ size+"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
	                     Message_pop.infoBox("Enjoy your meal. Inventory updated", "Info");

				  }
			  }
			  
			  
			 else if(result=="Milk") {
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
 int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("milk".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }

				  int item_count=Integer.parseInt(item_str);
				  System.out.println("No. of milks is " +size);	
				  
				  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					System.out.println("Cuurent line: " + current_line);   
					String[] split = current_line.split(":");
					System.out.println("Split[0]: "+ split[0]+ " Split[1]: " +split[1]);
					if ("milk".equals(split[0])) {
						System.out.println("Found milk");
						String [] split_new =split[1].split("/");
						min_thresh_milk=Integer.parseInt(split_new[0]);
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
				  
				  System.out.println("Min threshold for milk is " + min_thresh_milk);
				  if (size-item_count<min_thresh_milk) {
					  System.out.println("Cannot checkout!!! Min threshold reached/threshold not set");
				      Message_pop.infoBox("Cannot checkout!!! Min threshold reached/threshold not set", "Error");
				  }
				  
				  else {
					  System.out.println("Count is " +size);
					   milk_list=new ArrayList <Item>();
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("milk.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split("/");
							String pdate=split[0];
							int temp=Integer.parseInt(split[1]);
							String name=split[2];
							Milk milk=new Milk();
							milk.set_purchase_date(pdate);
							milk.set_expiry_date(pdate);
							milk.set_name(name);
							milk.set_temperature(temp);
							milk_list.add(milk);
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }

					  
					  for (int i=item_count; i>=0;i--) {
						  System.out.println("milk_list(i) is "+ milk_list.get(i));
						  milk_list.remove(i);
						  System.out.println("Size is " +milk_list.size());
					  }	
					  
					 	  
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split(":");
							if ("milk".equals(split[0])) {
								size=Integer.parseInt(split[1]);
							}
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }
                       System.out.println("Size of milk list is " + size);
                       size=size-item_count;
                    
                       String current_line;
 					  File input_file=new File("milk.txt");
 					  File temp_file = new File("tmp.txt");
               	    Scanner sc = new Scanner((input_file));   
               	    int i=0;
                        while(((sc.hasNextLine())) && i<size)  {
                        	   try {
                        		  current_line=sc.nextLine(); 
                        		  System.out.println("Current line is "+current_line);
    			                BufferedWriter writer = new BufferedWriter(new FileWriter(
    			                        temp_file,true));
    			               writer.write( current_line);
    			                writer.write("\n");
     	                     writer.close();
    			                i++;
                     	   }
    					  catch (IOException e) {
    			                e.printStackTrace();
    			       }
                     	   
                        }
                        boolean succ=temp_file.renameTo(input_file);

                        
                        System.out.println("FIle rename result is "+succ);

					  
					  System.out.println("Size of milks list is " +size);
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "size.txt",true));
			                writer.write( "milk:"+ size+"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
	                     Message_pop.infoBox("Enjoy your meal. Inventory updated", "Info");


				  }
			  }
			  
			  
			 else if(result=="Eggs") {
				  Drop_down chw = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=chw.get_result();
 int size=0;
				  
				  try {
					  String current_line;
					  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
					   while ((current_line = br.readLine()) != null) {
						String[] split = current_line.split(":");
						if ("egg".equals(split[0])) {
							size=Integer.parseInt(split[1]);
						}
					   }
					   br.close();
					  }
					  
					  catch (IOException fe) {
						   fe.printStackTrace();
						   
					   }

				  int item_count=Integer.parseInt(item_str);
				  System.out.println("No. of eggs is " +size);	
				  
				  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					System.out.println("Cuurent line: " + current_line);   
					String[] split = current_line.split(":");
					System.out.println("Split[0]: "+ split[0]+ " Split[1]: " +split[1]);
					if ("egg".equals(split[0])) {
						System.out.println("Found egg");
						String [] split_new =split[1].split("/");
						min_thresh_egg=Integer.parseInt(split_new[0]);
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
				  
				  System.out.println("Min threshold for egg is " + min_thresh_egg);
				  if (size-item_count<min_thresh_egg) {
					  System.out.println("Cannot checkout!!! Min threshold reached/threshold not set");
				      Message_pop.infoBox("Cannot checkout!!! Min threshold reached/threshold not set", "Error");
				  }
				  
				  else {
					  System.out.println("Count is " +size);
					    egg_list=new ArrayList <Item>();
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("egg.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split("/");
							String pdate=split[0];
							int temp=Integer.parseInt(split[1]);
							String name=split[2];
							Eggs egg=new Eggs();
							egg.set_purchase_date(pdate);
							egg.set_expiry_date(pdate);
							egg.set_name(name);
							egg.set_temperature(temp);
							egg_list.add(egg);
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }

					  
					  for (int i=item_count; i>=0;i--) {
						  System.out.println("egg_list(i) is "+ egg_list.get(i));
						  egg_list.remove(i);
						  System.out.println("Size is " +egg_list.size());
					  }	
					  
					   
					  try {
						  String current_line;
						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split(":");
							if ("egg".equals(split[0])) {
								size=Integer.parseInt(split[1]);
							}
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }
                       System.out.println("Size of egg list is " + size);
                       size=size-item_count;
                     
                       String current_line;
 					  File input_file=new File("egg.txt");
 					  File temp_file = new File("tmp.txt");
               	    Scanner sc = new Scanner((input_file));   
               	    int i=0;
                        while(((sc.hasNextLine())) && i<size)  {
                        	   try {
                        		  current_line=sc.nextLine(); 
                        		  System.out.println("Current line is "+current_line);
    			                BufferedWriter writer = new BufferedWriter(new FileWriter(
    			                        temp_file,true));
    			               writer.write( current_line);
    			                writer.write("\n");
     	                     writer.close();
    			                i++;
                     	   }
    					  catch (IOException e) {
    			                e.printStackTrace();
    			       }
                     	   
                        }
                        boolean succ=temp_file.renameTo(input_file);

                        
                        System.out.println("FIle rename result is "+succ);
  
					  System.out.println("Size of eggs list is " +size);
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "size.txt",true));
			                writer.write( "egg:"+ size+"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
	                     Message_pop.infoBox("Enjoy your meal. Inventory updated", "Info");
				  }
			  }
			  
			 			  
		  }
		  
		  else if ( ae.getSource( ) == set_thresholds ) {
			  Drop_down d1 = new Drop_down("Apples", "Oranges", "Milk", "Meat", "Eggs");
			  String result =d1.get_result();
			 
			  if (result == "Apples") {
				  Message_pop.infoBox("Select min threshold", "Info");
				  Drop_down min = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=min.get_result();
				  min_thresh_apple=Integer.parseInt(item_str);
				  Message_pop.infoBox("Select max threshold", "Info");
				  Drop_down max = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  item_str=max.get_result();
				  max_thresh_apple=Integer.parseInt(item_str);	
				  
				  if(max_thresh_apple <= min_thresh_apple) {
					  Message_pop.infoBox("Max threshold cannot be less than min threshold. Please enter again", "Error");
					  System.out.println("Max threshold cannot be less than min threshold. Please enter again");
				  }
				  
				  else {
				  
				  try {
		                BufferedWriter writer = new BufferedWriter(new FileWriter(
		                        "threshold.txt",true));
		                writer.write( "apple:" + min_thresh_apple + "/" + max_thresh_apple +"\n");
		                writer.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
				  }

				  
			  }
			  
			  else if (result == "Oranges") {
				  Message_pop.infoBox("Select min threshold", "Info");
				  Drop_down min = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=min.get_result();
				  min_thresh_orange=Integer.parseInt(item_str);
				  Message_pop.infoBox("Select max threshold", "Info");
				  Drop_down max = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  item_str=max.get_result();
				  max_thresh_orange=Integer.parseInt(item_str);
				  if(max_thresh_orange <= min_thresh_orange) {
					  Message_pop.infoBox("Max threshold cannot be less than min threshold. Please enter again", "Error");
					  System.out.println("Max threshold cannot be less than min threshold. Please enter again");
				  }
				  
				  else {
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "threshold.txt",true));
			                writer.write( "orange:" + min_thresh_orange + "/" + max_thresh_orange +"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
					  }

				  			  }
			  
			  else if (result == "Milk") {
				  Message_pop.infoBox("Select min threshold", "Info");
				  Drop_down min = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=min.get_result();
				  min_thresh_milk=Integer.parseInt(item_str);
				  Message_pop.infoBox("Select max threshold", "Info");
				  Drop_down max = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  item_str=max.get_result();
				  max_thresh_milk=Integer.parseInt(item_str);
				  if(max_thresh_milk <= min_thresh_milk) {
					  Message_pop.infoBox("Max threshold cannot be less than min threshold. Please enter again", "Error");
					  System.out.println("Max threshold cannot be less than min threshold. Please enter again");
				  }
				  
				  else {
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "threshold.txt",true));
			                writer.write( "milk:" + min_thresh_milk + "/" + max_thresh_milk +"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
					  }

				  
			  }
			  else if (result == "Meat") {
				  Message_pop.infoBox("Select min threshold", "Info");
				  Drop_down min = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  String item_str=min.get_result();
				  min_thresh_meat=Integer.parseInt(item_str);
				  Message_pop.infoBox("Select max threshold", "Info");
				  Drop_down max = new Drop_down("1","2","3","4","5","6","7","8","9","10");
				  item_str=max.get_result();
				  max_thresh_meat=Integer.parseInt(item_str);
				  if(max_thresh_meat <= min_thresh_meat) {
					  Message_pop.infoBox("Max threshold cannot be less than min threshold. Please enter again", "Error");
					  System.out.println("Max threshold cannot be less than min threshold. Please enter again");
				  }
				  
				  else {
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "threshold.txt",true));
			                writer.write( "meat:" + min_thresh_meat + "/" + max_thresh_meat +"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
					  }

				  
			  }	 
				 else if (result == "Eggs") {
					 Message_pop.infoBox("Select min threshold", "Info");
					  Drop_down min = new Drop_down("1","2","3","4","5","6","7","8","9","10");
					  String item_str=min.get_result();
					  min_thresh_egg=Integer.parseInt(item_str);
					  Message_pop.infoBox("Select max threshold", "Info");
					  Drop_down max = new Drop_down("1","2","3","4","5","6","7","8","9","10");
					  item_str=max.get_result();
					  max_thresh_egg=Integer.parseInt(item_str);	
					  
					  if(max_thresh_egg <= min_thresh_egg) {
						  Message_pop.infoBox("Max threshold cannot be less than min threshold. Please enter again", "Error");
						  System.out.println("Max threshold cannot be less than min threshold. Please enter again");
					  }
					  
					  else {
					  
					  try {
			                BufferedWriter writer = new BufferedWriter(new FileWriter(
			                        "threshold.txt",true));
			                writer.write( "egg:" + min_thresh_egg + "/" + max_thresh_egg +"\n");
			                writer.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
					  }
				 } 
			}		
		  
		  else if ( ae.getSource( ) == generate_shopping_list ) {
			  String message= null;
			  
			  int asize=0,mesize=0,misize=0,osize=0,esize=0;
			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("apple".equals(split[0])) {
						asize=Integer.parseInt(split[1]);//get size of apple list
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("meat".equals(split[0])) {
						mesize=Integer.parseInt(split[1]);//get size of meat list
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("milk".equals(split[0])) {
						misize=Integer.parseInt(split[1]);//get size of milk list
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("orange".equals(split[0])) {
						osize=Integer.parseInt(split[1]);//get size of orange list
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("egg".equals(split[0])) {
						esize=Integer.parseInt(split[1]);//get size of egg list
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
			  
			    
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("apple".equals(split[0])) {
						String [] split_new =split[1].split("/");
						max_thresh_apple=Integer.parseInt(split_new[1]);//get max threshold of apples
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }
                
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("orange".equals(split[0])) {
						String [] split_new =split[1].split("/");
						max_thresh_orange=Integer.parseInt(split_new[1]);//max threshold of oranges
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }

			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("egg".equals(split[0])) {
						String [] split_new =split[1].split("/");
						max_thresh_egg=Integer.parseInt(split_new[1]);//max threshold of eggs
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }

			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("meat".equals(split[0])) {
						String [] split_new =split[1].split("/");
						max_thresh_meat=Integer.parseInt(split_new[1]);//max threshold of meat
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }

			  
			  try {
				  String current_line;
				  BufferedReader br = new BufferedReader(new FileReader("threshold.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split(":");
					if ("milk".equals(split[0])) {
						String [] split_new =split[1].split("/");
						max_thresh_milk=Integer.parseInt(split_new[1]);//max thteshold of milk
					}
				   }
				   br.close();
				  }
				  
				  catch (IOException fe) {
					   fe.printStackTrace();
					   
				   }

			  
	          	int apple_size=max_thresh_apple-asize;
	          	int milk_size=max_thresh_milk-misize;
	          	int meat_size=max_thresh_meat-mesize;
	          	int orange_size=max_thresh_orange-osize;
	          	int egg_size=max_thresh_egg-esize;
	          	
	          		          	
	          	//send email
	          	
	          	
	          	message = "No. of apples:" + apple_size+ "\n"+"No. of milk cartons:" + milk_size+ "\n"
	          			+ "No. of meat:" +meat_size+ "\n"+ "No. of oranges:" + orange_size + "\n" + "No. of eggs" + egg_size + "\n";
	          	
	          	//Shopping list = max threshold of item-current quantiity of item
	          	JOptionPane.showMessageDialog(null, message, "InfoBox: " + "Shopping List", JOptionPane.INFORMATION_MESSAGE);
	          	SendMailTLS(message);
		  }
		  
		  else if (ae.getSource( )==get_notifications) {
			  
			  //get list of items to be discarded
			  discard_list=get_discard_list();
			  
			  Iterator<Item> it1 =discard_list.iterator();	
			  String  message="Following items have been discarded becuase of temperature/expiry date and are not fit for consumption";
			  while (it1.hasNext()) {
				  Item disc = it1.next();
				  String name1 = disc.get_name();
                  message=message+"\n"+name1;
                 
                  //Delete discarded items from file
                  if (aflag) {
                  File input_file=new File("apple.txt");
    			  File temp_file = new File("tmp.txt");
    			  String current_line;
          	    Scanner sc = new Scanner((input_file));   
          	    int i=0;
          	    int count=0;
                   while((sc.hasNextLine()))  {
                   	   try {
                   		  current_line=sc.nextLine(); 
                   		  String [] split=current_line.split("/");
                   		  String name2=split[2];
                   		  System.out.println("Current line is "+current_line);
    		                BufferedWriter writer = new BufferedWriter(new FileWriter(
    		                        temp_file,true));//update the apple.txt file as per the current size.
 		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
    		                if(name1.equals(name2)) {
    		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
    		            	   count++;//keep a count of discarded items.
    		            	   continue;
    		               }
    		               else {
    		                writer.write( current_line);
    		                writer.write("\n");
    		               }
                         writer.close();
    		                i++;
                	   }
    				  catch (IOException e) {
    		                e.printStackTrace();
    		       }
                	   
                   }
                   
                   
                   
                   boolean succ=temp_file.renameTo(input_file);
                   
                   System.out.println("FIle rename result is "+succ);
                   int size=0;
                   try {
						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
						   while ((current_line = br.readLine()) != null) {
							String[] split = current_line.split(":");
							if ("apple".equals(split[0])) {
								size=Integer.parseInt(split[1]);
							}
						   }
						   br.close();
						  }
						  
						  catch (IOException fe) {
							   fe.printStackTrace();
							   
						   }
  size=size-count;//update size of list and write it in file.
                   try {
		                BufferedWriter writer = new BufferedWriter(new FileWriter(
		                        "size.txt",true));
		                writer.write( "apple:"+ size+"\n");
		                writer.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
                  }
                  
                  if (orflag) {
                  File input_file=new File("orange.txt");
     			  File temp_file = new File("tmp.txt");
     			  String current_line;
           	   Scanner sc = new Scanner((input_file));   
           	    int i=0;
           	    int count=0;
                    while((sc.hasNextLine()))  {
                    	   try {
                    		  current_line=sc.nextLine(); 
                    		  String [] split=current_line.split("/");
                    		  String name2=split[2];
                    		  System.out.println("Current line is "+current_line);
     		                BufferedWriter writer = new BufferedWriter(new FileWriter(
     		                        temp_file,true));//update the apple.txt file as per the current size.
  		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
     		                if(name1.equals(name2)) {
     		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
     		            	   count++;
     		            	   continue;
     		               }
     		               else {
     		                writer.write( current_line);
     		                writer.write("\n");
     		               }
                          writer.close();
     		                i++;
                 	   }
     				  catch (IOException e) {
     		                e.printStackTrace();
     		       }
                 	   
                    }
                    
                    
                    
                   boolean succ=temp_file.renameTo(input_file);
                    
                    System.out.println("FIle rename result is "+succ);
                    int size=0;
                    try {
 						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
 						   while ((current_line = br.readLine()) != null) {
 							String[] split = current_line.split(":");
 							if ("orange".equals(split[0])) {
 								size=Integer.parseInt(split[1]);
 							}
 						   }
 						   br.close();
 						  }
 						  
 						  catch (IOException fe) {
 							   fe.printStackTrace();
 							   
 						   }
                   size=size-count;//update size of list and write it in file.
                    try {
 		                BufferedWriter writer = new BufferedWriter(new FileWriter(
 		                        "size.txt",true));
 		                writer.write( "orange:"+ size+"\n");
 		                writer.close();
 		            } catch (IOException e) {
 		                e.printStackTrace();
 		            }

                  }
                    if (meflag) {
                    File input_file=new File("meat.txt");
       			  File temp_file = new File("tmp.txt");
       			  String current_line;
             	   Scanner sc = new Scanner((input_file));   
             	   int i=0;
             	   int count=0;
                      while((sc.hasNextLine()))  {
                      	   try {
                      		  current_line=sc.nextLine(); 
                      		  String [] split=current_line.split("/");
                      		  String name2=split[2];
                      		  System.out.println("Current line is "+current_line);
       		                BufferedWriter writer = new BufferedWriter(new FileWriter(
       		                        temp_file,true));//update the apple.txt file as per the current size.
    		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
       		                if(name1.equals(name2)) {
       		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
       		            	   count++;
       		            	   continue;
       		               }
       		               else {
       		                writer.write( current_line);
       		                writer.write("\n");
       		               }
                            writer.close();
       		                i++;
                   	   }
       				  catch (IOException e) {
       		                e.printStackTrace();
       		       }
                   	   
                      }
                      
                      
                      
                      boolean succ=temp_file.renameTo(input_file);
                      
                      System.out.println("FIle rename result is "+succ);
                      
                      int size=0;
                      try {
   						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
   						   while ((current_line = br.readLine()) != null) {
   							String[] split = current_line.split(":");
   							if ("meat".equals(split[0])) {
   								size=Integer.parseInt(split[1]);
   							}
   						   }
   						   br.close();
   						  }
   						  
   						  catch (IOException fe) {
   							   fe.printStackTrace();
   							   
   						   }
     size=size-count;//update size of list and write it in file.
                      try {
   		                BufferedWriter writer = new BufferedWriter(new FileWriter(
   		                        "size.txt",true));
   		                writer.write( "meat:"+ size+"\n");
   		                writer.close();
   		            } catch (IOException e) {
   		                e.printStackTrace();
   		            }

                    }
                      if(miflag) {
                     File input_file=new File("milk.txt");
         			 File temp_file = new File("tmp.txt");
         			  String current_line;
               	   Scanner sc = new Scanner((input_file));   
               	   int i=0;
               	   int count=0;
                        while((sc.hasNextLine()))  {
                        	   try {
                        		  current_line=sc.nextLine(); 
                        		  String [] split=current_line.split("/");
                        		  String name2=split[2];
                        		  System.out.println("Current line is "+current_line);
         		                BufferedWriter writer = new BufferedWriter(new FileWriter(
         		                        temp_file,true));//update the apple.txt file as per the current size.
      		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
         		                if(name1.equals(name2)) {
         		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
         		            	   count++;
         		            	   continue;
         		               }
         		               else {
         		                writer.write( current_line);
         		                writer.write("\n");
         		               }
                              writer.close();
         		                i++;
                     	   }
         				  catch (IOException e) {
         		                e.printStackTrace();
         		       }
                     	   
                        }
                        
                        
                        
                       boolean succ=temp_file.renameTo(input_file);
                        
                        System.out.println("FIle rename result is "+succ);
                        
                        int size=0;
                        try {
     						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
     						   while ((current_line = br.readLine()) != null) {
     							String[] split = current_line.split(":");
     							if ("milk".equals(split[0])) {
     								size=Integer.parseInt(split[1]);
     							}
     						   }
     						   br.close();
     						  }
     						  
     						  catch (IOException fe) {
     							   fe.printStackTrace();
     							   
     						   }
       size=size-count;//update size of list and write it in file.
                        try {
     		                BufferedWriter writer = new BufferedWriter(new FileWriter(
     		                        "size.txt",true));
     		                writer.write( "milk:"+ size+"\n");
     		                writer.close();
     		            } catch (IOException e) {
     		                e.printStackTrace();
     		            }

                      } 
                        if(eflag) {
                       File input_file=new File("egg.txt");
           			 File temp_file = new File("tmp.txt");
           			  String current_line;
                 	    Scanner sc = new Scanner((input_file));   
                 	    int i=0;
                 	    int count=0;
                          while((sc.hasNextLine()))  {
                          	   try {
                          		  current_line=sc.nextLine(); 
                          		  String [] split=current_line.split("/");
                          		  String name2=split[2];
                          		  System.out.println("Current line is "+current_line);
           		                BufferedWriter writer = new BufferedWriter(new FileWriter(
           		                        temp_file,true));//update the apple.txt file as per the current size.
        		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
           		                if(name1.equals(name2)) {
           		            	   System.out.println("Name1 is " +name1+ " and name2 is "+name2);
           		            	   count++;
           		            	   continue;
           		               }
           		               else {
           		                writer.write( current_line);
           		                writer.write("\n");
           		               }
                                writer.close();
           		                i++;
                       	   }
           				  catch (IOException e) {
           		                e.printStackTrace();
           		       }
                       	   
                          }
                          
                          
                          
                         boolean succ=temp_file.renameTo(input_file);
                          
                          System.out.println("FIle rename result is "+succ);
                          int size=0;
                          try {
       						  BufferedReader br = new BufferedReader(new FileReader("size.txt"));
       						   while ((current_line = br.readLine()) != null) {
       							String[] split = current_line.split(":");
       							if ("egg".equals(split[0])) {
       								size=Integer.parseInt(split[1]);
       							}
       						   }
       						   br.close();
       						  }
       						  
       						  catch (IOException fe) {
       							   fe.printStackTrace();
       							   
       						   }
         size=size-count;//update size of list and write it in file.
                          try {
       		                BufferedWriter writer = new BufferedWriter(new FileWriter(
       		                        "size.txt",true));
       		                writer.write( "egg:"+ size+"\n");
       		                writer.close();
       		            } catch (IOException e) {
       		                e.printStackTrace();
       		            }

                        }   

			  }
	          	
			  JOptionPane.showMessageDialog(null, message, "InfoBox: " + "Discard Item", JOptionPane.INFORMATION_MESSAGE);			  
			  
			  discard_list.clear();
		  }
		  
		  else if (ae.getSource() ==set_temp) {
			  set_temperature();
		  }
		 }
		 catch( Exception e )    {
			    e.getStackTrace();
			   }
			  }
		
		
		
			 }


	
	 public void before_start() {   

		 //check if item has already been populated in a previous run of application
	  System.out.println("Inside before_start");
      File file = new File("apple.txt");
      
      if (file.exists()) {
    	  aflag=true;
      }
      
		file = new File("orange.txt");
	
		 if (file.exists()) {
	    	  orflag=true;
	      }
		 
		 file = new File("meat.txt");
			
		 if (file.exists()) {
	    	  meflag=true;
	      }
	
		 file = new File("milk.txt");
			
		 if (file.exists()) {
	    	  miflag=true;
	      }
	
		 file = new File("egg.txt");
			
		 if (file.exists()) {
	    	  eflag=true;
	      }
		 
		 set_temperature();
	
      
	 }
	 
	
	public void set_date (Date date) {
		current_date=date;
		
	}
	
	
	public Date get_date() {
		return current_date;
	}
	
	public List<Item> get_shopping_list () {
		
		return shopping_list;
	}
	
	public List <Item> get_discard_list () {
		
		String pdate;
		apple_list=new ArrayList <Item>();
		try {
			  String current_line;
			  BufferedReader br = new BufferedReader(new FileReader("apple.txt"));
			   while ((current_line = br.readLine()) != null) {
				System.out.println("Current line is " +current_line);
				String[] split = current_line.split("/");
				System.out.println("Split is succesful");
				pdate=split[0];
				System.out.println("pdate " +pdate);
				int temp=Integer.parseInt(split[1]);
				String name=split[2];
				Apples apple=new Apples();
				apple.set_purchase_date(pdate);
				apple.set_expiry_date(pdate);
				apple.set_name(name);
				apple.set_temperature(temp);
				apple_list.add(apple);//build apple list from apple.txt
			   }
			   br.close();
			  }
			  
			  catch (IOException fe) {
				   fe.printStackTrace();
				   
			   }
		
		
		orange_list=new ArrayList <Item>();
		try {
			  String current_line;
			  BufferedReader br = new BufferedReader(new FileReader("orange.txt"));
			   while ((current_line = br.readLine()) != null) {
				System.out.println("Current line is " +current_line);
				String[] split = current_line.split("/");
				System.out.println("SPlit is succesful");
				pdate=split[0];
				int temp=Integer.parseInt(split[1]);
				String name=split[2];
				Oranges orange=new Oranges();
				orange.set_purchase_date(pdate);
				orange.set_expiry_date(pdate);
				orange.set_name(name);
				orange.set_temperature(temp);
				orange_list.add(orange);
			   }
			   br.close();
			  }
			  
			  catch (IOException fe) {
				   fe.printStackTrace();
				   
			   }
		
		
	meat_list=new ArrayList <Item>();
		try {
			  String current_line;
			  BufferedReader br = new BufferedReader(new FileReader("meat.txt"));
			   while ((current_line = br.readLine()) != null) {
				System.out.println("Current line is " +current_line);
				String[] split = current_line.split("/");
				System.out.println("SPlit is succesful");
				pdate=split[0];
				int temp=Integer.parseInt(split[1]);
				String name=split[2];
				Meat meat=new Meat();
				meat.set_purchase_date(pdate);
				meat.set_expiry_date(pdate);
				meat.set_name(name);
				meat.set_temperature(temp);
				meat_list.add(meat);
			   }
			   br.close();
			  }
			  
			  catch (IOException fe) {
				   fe.printStackTrace();
				   
			   }
	
		
		milk_list=new ArrayList <Item>();
		try {
			  String current_line;
			  BufferedReader br = new BufferedReader(new FileReader("milk.txt"));
			   while ((current_line = br.readLine()) != null) {
				System.out.println("Current line is " +current_line);
				String[] split = current_line.split("/");
				System.out.println("SPlit is succesful");
				pdate=split[0];
				int temp=Integer.parseInt(split[1]);
				String name=split[2];
				Milk milk=new Milk();
				milk.set_purchase_date(pdate);
				milk.set_expiry_date(pdate);
				milk.set_name(name);
				milk.set_temperature(temp);
				milk_list.add(milk);
			   }
			   br.close();
			  }
			  
			  catch (IOException fe) {
				   fe.printStackTrace();
				   
			   }
		
		
		egg_list=new ArrayList <Item>();
		try {
			  String current_line;
			  BufferedReader br = new BufferedReader(new FileReader("egg.txt"));
			   while ((current_line = br.readLine()) != null) {
				System.out.println("Current line is " +current_line);
				String[] split = current_line.split("/");
				System.out.println("SPlit is succesful");
				pdate=split[0];
				int temp=Integer.parseInt(split[1]);
				String name=split[2];
				Eggs egg=new Eggs();
				egg.set_purchase_date(pdate);
				egg.set_expiry_date(pdate);
				egg.set_name(name);
				egg.set_temperature(temp);
				egg_list.add(egg);
			   }
			   br.close();
			  }
			  
			  catch (IOException fe) {
				   fe.printStackTrace();
				   
			   }
		

		Iterator<Item> it1 =apple_list.iterator();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		while(it1.hasNext()) {
			Item item=it1.next();
			
			int temp=item.get_temperature();
			System.out.println("Temp is "+temp+" and temperature is "+temperature);
			if(temp<temperature) {
				discard_list.add(item);//if temp threshold of item less than fridge temp discard item
				System.out.println("Discarded because of temp");
			}
			
			Date exp_date=item.get_expiry_date();
			Date cur_date=new Date();
			System.out.println("Current date is "+cur_date);
			if(exp_date.compareTo(cur_date)<0) {
				System.out.println("Expiry date for item " +item.get_name()+ " is "+exp_date+ " and current date is" +cur_date);
				discard_list.add(item);//if item has expired discard it.
			}
			else if(exp_date.compareTo(cur_date)==0) {
				String name= item.get_name();
				Message_pop.infoBox(name +"Will expire today!!!", "Warning");
			}
				
		}
		
		Iterator<Item> it2 =orange_list.iterator();
		

		while(it2.hasNext()) {
			Item item=it2.next();
			Date exp_date=item.get_expiry_date();
			Date cur_date=new Date();
			if(exp_date.compareTo(cur_date)<0) {
				discard_list.add(item);
			}
			else if(exp_date.compareTo(cur_date)==0) {
				String name= item.get_name();
				Message_pop.infoBox(name +"Will expire today!!!", "Warning");
			}
			int temp=item.get_temperature();
			if(temp<temperature) {
				discard_list.add(item);
			}	
		}

		
		Iterator<Item> it3 =meat_list.iterator();

		while(it3.hasNext()) {
			Item item=it3.next();
			Date exp_date=item.get_expiry_date();
			Date cur_date=new Date();
			if(exp_date.compareTo(cur_date)<0) {
				discard_list.add(item);
			}
			else if(exp_date.compareTo(cur_date)==0) {
				String name= item.get_name();
				Message_pop.infoBox(name +"Will expire today!!!", "Warning");
			}
			int temp=item.get_temperature();
			System.out.println("Got_temperarute is " +temp);
			if(temp<temperature) {
				System.out.println("Discarded item "+item.get_name());
				discard_list.add(item);
			}	
		}

		
		Iterator<Item> it4 =milk_list.iterator();

		while(it4.hasNext()) {
			Item item=it4.next();
			Date exp_date=item.get_expiry_date();
			Date cur_date=new Date();
			if(exp_date.compareTo(cur_date)<0) {
				discard_list.add(item);
			}
			else if(exp_date.compareTo(cur_date)==0) {
				String name= item.get_name();
				Message_pop.infoBox(name +"Will expire today!!!", "Warning");
			}
			int temp=item.get_temperature();
			if(temp<temperature) {
				discard_list.add(item);
			}	
		}

		
		Iterator<Item> it5 =egg_list.iterator();

		while(it5.hasNext()) {
			Item item=it5.next();
			Date exp_date=item.get_expiry_date();
			Date cur_date=new Date();
			if(exp_date.compareTo(cur_date)<0) {
				discard_list.add(item);
			}
			else if(exp_date.compareTo(cur_date)==0) {
				String name= item.get_name();
				Message_pop.infoBox(name +"Will expire today!!!", "Warning");
			}
			int temp=item.get_temperature();
			if(temp<temperature) {
				discard_list.add(item);
			}	
		}

		
		return discard_list;
	}
	
	public void set_temperature () {
		String temp = JOptionPane.showInputDialog( null, "Please enter the temperature in Celcius scale" );
        try {
		temperature=Integer.parseInt(temp);
	}
        catch (NumberFormatException e) {
        	Message_pop.infoBox("Please enter an integer","Info");
        }
	}
	
	public int get_temperature () {
		return temperature;
	}
    
	public void SendMailTLS (String msg) {
		 
		final String username = "sughoshtest@gmail.com";
		final String password = "java4452014";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
             
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			String id=JOptionPane.showInputDialog(null,"Please enter your id");
			boolean succ=false;
			String email_id=null;
			   try { 
				   
				   String current_line;
				   BufferedReader br = new BufferedReader(new FileReader("nuserinfo.txt"));
				   while ((current_line = br.readLine()) != null) {
					String[] split = current_line.split("/");
					String log = split[0];
					String pass= split[1];
					String email=split[2];
					if(id.equals(log)) {
						email_id=email;
						succ=true;
					}
					if(succ) {
						break;
					}
				   }
				   if(!succ) {
					   Message_pop.infoBox("ID not found", "Error");
				   }
			   }
			   catch (FileNotFoundException fnfe) {
				   Message_pop.infoBox("File nuserinfo.txt not found", "Error");
			   }
			   catch (IOException ioe) {
				   ioe.printStackTrace();
			   }
			   
			   if(succ) {
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email_id));
			message.setSubject("Shopping List");
			message.setText(msg);
 
			Transport.send(message);
            Message_pop.infoBox("Email has been sent to"+ email_id, "Info");
			System.out.println("Done");
			   }
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
