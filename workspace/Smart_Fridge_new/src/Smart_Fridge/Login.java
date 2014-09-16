package Smart_Fridge;

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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login extends JFrame
{
 private Container contents;
 private JLabel idLabel, passwordLabel, message;
 private JTextField id;
 private JPasswordField password;
 private JTextArea legal;
 private List <String> log_list = new ArrayList <String>();
 private List <String> pass_list = new ArrayList <String>();
 // Constructor
 public Login( )
 {
  super( "Login Screen" );
  contents = getContentPane( );
  contents.setLayout( new FlowLayout( ) );
  idLabel = new JLabel( "Enter id" ); 
  id = new JTextField( "", 12 );      
  passwordLabel = new JLabel( "Enter password" ); 
  password = new JPasswordField( 8 ); 
  password.setEchoChar( '*' );      
  message = new JLabel( "Log in above" ); 
legal = new JTextArea( "Access to this machine is restrcted. Please enter your credentials before entering." );
  legal.setEditable( false );        // disable typing in this field
//add all components to the window
 contents.add( idLabel );
 contents.add( id );
 contents.add( passwordLabel );
 contents.add( password );
 contents.add( message );
 contents.add( legal );
 // instantiate event handler for the text fields
 TextFieldHandler tfh = new TextFieldHandler( );
 // add event handler as listener for ID and password fields
 id.addActionListener( tfh );
 password.addActionListener( tfh );
 setSize( 250,200 );
 setVisible( true );
}
 // private inner class event handler
 private class TextFieldHandler implements ActionListener  {
  public void actionPerformed( ActionEvent e )   {
   try { 
	   
	   String current_line;
	   BufferedReader br = new BufferedReader(new FileReader("nuserinfo.txt"));
	   while ((current_line = br.readLine()) != null) {
		String[] split = current_line.split("/");
		String log = split[0];
		String pass= split[1];
		String email=split[2];
		
		log_list.add(log);
		pass_list.add(pass);
	   }
	   boolean succ =false;
	for (int i = 0;i<log_list.size();i++) {   
   if ( id.getText( ).equals( log_list.get(i) )
    && ( new String( password.getPassword( ) ) ).equals( pass_list.get(i) ) )    {
	Fridge f1 = new Fridge ();  
	f1.before_start();
    message.setForeground( Color.BLACK );
    message.setText( "Welcome!" );
    succ=true;
    break;
   }
   if(!succ)    {
    message.setForeground( Color.RED );
    message.setText( "Sorry: wrong login" );
   }
  }
}
   catch (IOException fe) {
	   fe.printStackTrace();
	   
   }
 }  
 } // End Inner Class
 public static void main( String [] args )
 {
  Login login = new Login( );
  login.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 }
}
// End COnstructor
