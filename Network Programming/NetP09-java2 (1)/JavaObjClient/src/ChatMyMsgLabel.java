

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatMyMsgLabel extends JPanel {

	public JLabel usernameLabel;
	public JLabel userImageLabel;
	public JLabel userTimeLabel;
	public JTextPane chatPane;
	private int width;
	private int height;
	private ImageIcon defaultProfileIcon = new ImageIcon(ChatMyMsgLabel.class.getResource("/icons/default_profile.jpeg"));
	
	public ChatMyMsgLabel(String name, String time, String msg) {
		setBorder(null);
		setPreferredSize(new Dimension(380,70));
		setBackground(new Color(155,187,212));
		setLayout(null);	
		
		userImageLabel = new JLabel(defaultProfileIcon);
		userImageLabel.setBorder(null);
		userImageLabel.setBounds(328, 1, 50, 50);
		add(userImageLabel);
		
		usernameLabel = new JLabel(name);
		usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		usernameLabel.setBorder(null);
		usernameLabel.setBounds(160, 1, 160, 20);
		add(usernameLabel);
		
		chatPane = new JTextPane();
		chatPane.setBorder(null);
		chatPane.setLocation(150, 24);
		chatPane.setMinimumSize(new Dimension(100,20));
		chatPane.setMaximumSize(new Dimension(200,20));
		// Áß¾Ó Á¤·Ä 
		StyledDocument doc = chatPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setSpaceBelow(center, 3);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		chatPane.setText(msg);
		add(chatPane);
		
		userTimeLabel = new JLabel(time);
//		userTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		userTimeLabel.setBorder(null);
//		userTimeLabel.setBounds(300, 24, 50, 20);
		userTimeLabel.setSize(new Dimension(50,20));
		add(userTimeLabel);
	}

}
