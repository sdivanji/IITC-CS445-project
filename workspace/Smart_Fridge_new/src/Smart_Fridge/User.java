package Smart_Fridge;

import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
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

public class User extends JFrame {
	
	 private Container contents;
	 private JLabel option;
	 private JButton login, create_account;
	 public User( )  {
	  super( "Welcome" );
	  contents = getContentPane( );
	  contents.setLayout( new FlowLayout( ) );
	  option = new JLabel( "Choose your option" ); 
	  create_account = new JButton( "Create Account" );
	  login= new JButton( "Login" );
	  

	  
	    // add components to the window
	    contents.add( option );
	    contents.add( create_account );
	    contents.add( login );
	    
	    
	    // instantiate our event handler
	    ButtonHandler bh = new ButtonHandler( );
	    // add event handler as listener for both buttons
	    create_account.addActionListener( bh );
	    login.addActionListener( bh );
	    
	    setSize( 500,500 );
	    setVisible( true );
	   }
	//private inner class event handler
	private class ButtonHandler implements ActionListener  {
	// implement actionPerformed method
	public void actionPerformed( ActionEvent ae )
	{
		
		
	 try    {
	  
	  
	  // identify which button was pressed
	  if ( ae.getSource( ) == create_account) {
		 CreateAccount acc1 = new CreateAccount();
	    
	  }
	  else if ( ae.getSource( ) == login ) {
		 Login login = new Login();
	  }
	 	 
	 }

	 catch( Exception e )    {
		    e.getStackTrace();
		   }
		  }
		 }

	
	public static void main (String[] args ) {
		User user =new User();
		user.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	
}

