package Smart_Fridge;

import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class Drop_down {
     
   private String result = null;

    //Constructor

    public  Drop_down(final String... values) {
            JPanel panel = new JPanel();
            //Add component to window
            panel.add(new JLabel("Please make a selection:"));
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            for (String value : values) {
                model.addElement(value);
            }
            JComboBox comboBox = new JComboBox(model);
            panel.add(comboBox);

            int iResult = JOptionPane.showConfirmDialog(null, panel, "Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (iResult) {
                case JOptionPane.OK_OPTION:
                    this.result = (String) comboBox.getSelectedItem();
                    break;
            }              
        }
    
    public String get_result( ) {
    	
    	System.out.println("You selected " + this.result);

    	return this.result;
    	
    }

    //inner class
    public class Response implements Runnable {

        private String[] values;
        private Drop_down response;

        public Response(String... values) {
            this.values = values;
        }

        @Override
        public void run() {
            response = new Drop_down(values);
        }

        
    }
    
    public static void main(String[] args) {
        Drop_down d1 = new Drop_down("Eggs","Meat","Milk","Apples","Oranges");
        d1.get_result();
    }
}