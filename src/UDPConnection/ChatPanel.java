
package UDPConnection;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


//import texasholdem.Action;
//import texasholdem.gui.UIConstants;

/**
 * Panel with buttons to let a human player select a poker action.
 * 
 * 
 */
public class ChatPanel extends JPanel implements ActionListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** The Send Button. */
    private final JButton sendButton;
    
    /** The Chat String. */
    private String chatlist = "";
    
    /** The UDP client.*/
    private UDPClient sender;
    
    /** The text input field.*/
    private JTextField inputField;
    
    /** The chat text field.*/
    private JTextArea chatField;
    
    /**
     * Constructor.
     * 
     * @param main
     *            The main frame.
     */
    public ChatPanel(UDPClient sender) {
    	this.sender = sender;
    	
        //setBackground(UIConstants.TABLE_COLOR);
        setLayout(new FlowLayout());

       
        chatField = new JTextArea(5,30);
        chatField.setLineWrap(true);
        chatField.setEditable(false);
        JScrollPane scroll = new JScrollPane(chatField);
        add(scroll); 
        
        inputField = new JTextField(20);
        inputField.setText("");

        inputField.setEditable(true);
        add(inputField); 

        sendButton = new JButton("Send");
        sendButton.setSize(100, 30);
        sendButton.addActionListener(this);
        add(sendButton); 
        
    }
    
    public void updateChat(String message){
    	System.out.print("updateChat "+message +message);
		chatlist = message + chatlist;
		chatField.setText(chatlist);
    }
 
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == sendButton) {
        	String message = inputField.getText();
        	if(!message.equals("")){
        		sender.send("C##" + message);
        	}
            inputField.setText("");
        } 
    }
}
