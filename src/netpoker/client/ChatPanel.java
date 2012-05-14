
package netpoker.client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import netpoker.model.udp.ChatClient;

/**
 * Panel with buttons to let a human player select a poker action.
 * 
 * 
 */
public class ChatPanel extends JPanel implements ActionListener, KeyListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** The Send Button. */
    private final JButton sendButton;
    
    /** The Chat String. */
    private String chatlist = "";
    
    /** The UDP client.*/
    private ChatClient client;
    
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
    public ChatPanel(ChatClient client) {
    	this.client = client;
    	
        setBackground(UIConstants.TABLE_COLOR);
        setLayout(new FlowLayout());
        
        
        chatField = new JTextArea(5,30);
        chatField.setLineWrap(true);
        chatField.setEditable(false);
        JScrollPane scroll = new JScrollPane(chatField);
        add(scroll); 
        
        inputField = new JTextField(20);
        inputField.setText("");
        
        inputField.setEditable(true);
        inputField.setFocusable(true);
        inputField.addKeyListener(this);
        add(inputField); 
        
        sendButton = new JButton("Send");
        sendButton.setSize(100, 30);
        sendButton.addActionListener(this);
        add(sendButton); 
        
    }
    
    public void updateChat(String message){
    	System.out.println("updateChat "+message +message);
		chatlist = message + "\n" + chatlist;
		chatField.setText(chatlist);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == sendButton) {
        	sendMessage();
        } 
    }
    
    private void sendMessage(){
    	String message = inputField.getText();
    	if(!message.equals("")){
    		client.sendMessage(message);
    	}
        inputField.setText("");
    }
    
    
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 10){
            sendMessage();
		}			
	}
    
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
