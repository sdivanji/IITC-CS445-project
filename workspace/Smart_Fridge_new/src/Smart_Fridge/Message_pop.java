package Smart_Fridge;

import javax.swing.JOptionPane;

public class Message_pop{

public static void infoBox(String infoMessage, String location)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + location, JOptionPane.INFORMATION_MESSAGE);
    }
}