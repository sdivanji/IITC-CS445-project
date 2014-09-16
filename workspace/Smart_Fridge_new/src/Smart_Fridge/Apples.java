package Smart_Fridge;



import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class Apples implements Item,Serializable{
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private Date purchase_date;
	private Date expiry_date;
	private int temperature;
	private String item_name;
	
	//Constructor
	public Apples () {
		
		purchase_date = new Date();
		expiry_date = new Date();
		temperature=0;
		item_name="";

	}
   
	
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#set_purchase_date(java.lang.String)
	 * Sets the purchase date of the item.
	 * Parameters: Strng
	 * Returns: Null
	 * Throws:Parse exception, Illegal format exception
	 */
	public void set_purchase_date (String pdate) {
	
		try {
			formatter.setLenient(false); 
			purchase_date=formatter.parse(pdate);
			formatter.getCalendar();
			
		} catch (ParseException e) {
			Message_pop.infoBox("Please enter a valid date","Info");
			e.printStackTrace();
		}
		
		catch (IllegalArgumentException iae) {
			iae.getStackTrace();
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#set_expiry_date(java.lang.String)
	 * Sets expiry date of the item. For apples it is 2 days.
	 * Parameters String. Returns none
	 * 
	 */
	public void set_expiry_date (String pdate) {
		Calendar c = Calendar.getInstance();
		c.setTime(purchase_date);
		c.add(Calendar.DATE,7);
		expiry_date=c.getTime();
	}
	
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#set_temperature(int)
	 * Sets the temeprature threshold for the item.
	 * Parameters int. Returns none
	 * Throws number format exception.
	 */
	
	public void set_temperature (int tmp) {
		
        try {
		temperature=tmp;
	}
        catch (NumberFormatException e) {
        	Message_pop.infoBox("Please enter a integer","Info");
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#set_name(java.lang.String)
	 * Sets the name of the string. Parameters String. Returns none
	 */
	public void set_name(String name) {
		item_name=name;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#get_temperature()
	 * Returns the temperature threshold of the item. Parameters none. Return string
	 */
	public int get_temperature () {
		return temperature;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#get_purchase_date()
	 * Returns purchase date of the item. Parameters none. Returns Date.
	 */
	public Date get_purchase_date () {
		return purchase_date;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#get_expiry_date()
	 * Returns purchase date of the item. 
	 */
	public Date get_expiry_date () {
		return expiry_date;		
	}
	/*
	 * (non-Javadoc)
	 * @see Smart_Fridge.Item#get_name()
	 *Returns name of the item.
	 */
	public String get_name() {
		return item_name;
	}
}
