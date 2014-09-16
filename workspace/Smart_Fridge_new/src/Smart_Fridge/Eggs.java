package Smart_Fridge;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class Eggs implements Item,Serializable{
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private Date purchase_date;
	private Date expiry_date;
	private int temperature;
	public String item_name;
	
	//Constructor
	public Eggs () {
		
		purchase_date = new Date();
		expiry_date = new Date();
		temperature=0;
		item_name="";
	}
   
	//set purchase date of eggs
	public void set_purchase_date (String pdate) {
		//pdate=JOptionPane.showInputDialog(null,"Please enter the purchase date for eggs in dd-MM-yyyy format");
		try {
			formatter.setLenient(false); 
			purchase_date=formatter.parse(pdate);
			formatter.getCalendar();
			
		} catch (ParseException e) {
			Message_pop.infoBox("Please enter a valid date","Info");
			e.printStackTrace();
		}
		
	}
	
	//set expiry date of eggs
	public void set_expiry_date (String pdate) {
		Calendar c = Calendar.getInstance();
		c.setTime(purchase_date);
		c.add(Calendar.DATE,7);//setting expiry date for eggs as 7 days from purchase date.
		expiry_date=c.getTime();
	}
	
	
	//set temperature threshold for eggs
	public void set_temperature (int temp) {

		 try {
				temperature=temp;
			}
		        catch (NumberFormatException e) {
		        	Message_pop.infoBox("Please enter a integer", "Info");
		        }
	}
	
	//set name for egg object
	public void set_name (String name) {
		item_name=name;
	}
	
	//returns temperature threshold of eggs
	public int get_temperature () {
		return temperature;
	}
	
	//returns purchase date of eggs
	public Date get_purchase_date () {
		return purchase_date;
		
	}
	
	//returns expiry date of eggs
	public Date get_expiry_date () {
		return expiry_date;		
	}
	
	//returns name of egg object
	public String get_name() {
		return item_name;
	}
}
