package Smart_Fridge;



import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class Meat implements Item,Serializable{
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private Date purchase_date;
	private Date expiry_date;
	private int temperature;
	private String item_name;
	public Meat () {
		
		purchase_date = new Date();
		expiry_date = new Date();
		item_name="";
		temperature=0;
		
	}
   
	
	
	public void set_purchase_date (String pdate) {
		try {
			formatter.setLenient(false); 
			purchase_date=formatter.parse(pdate);
			formatter.getCalendar();
			
		} catch (ParseException e) {
			Message_pop.infoBox("Please enter a valid date","Info");
			e.printStackTrace();
		}
		
	}
	
	public void set_expiry_date (String pdate) {
		Calendar c = Calendar.getInstance();
		c.setTime(purchase_date);
		c.add(Calendar.DATE,2);//set expiry date 2 days from purchase date
		expiry_date=c.getTime();
	}
	
	
	public void set_temperature (int temp) {

		 try {
				temperature=temp;
			}
		        catch (NumberFormatException e) {
		        	Message_pop.infoBox("Please enter a integer", "Info");
		        }
	}

	
	public void set_name (String name) {
	item_name=name;
	}
	public int get_temperature () {
		return temperature;
	}
	
	public Date get_purchase_date () {
		return purchase_date;
		
	}
	
	public Date get_expiry_date () {
		return expiry_date;		
	}
	
	public String get_name () {
		return item_name;
	}
	
}
