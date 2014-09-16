package Smart_Fridge;

import java.util.Date;

public interface Item {
	
	
	//abstract methods
	void set_purchase_date(String pdate);
	 void set_expiry_date(String pdate);
	 Date get_purchase_date();
	 Date get_expiry_date();
	void set_temperature(int tmp);
	 int get_temperature();
	 void set_name(String name);
     String get_name();
}
