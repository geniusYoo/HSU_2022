import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

public class UserProfile extends JPanel{

	private JFrame frame;
	public JPanel profilePane;
	public JLabel profileImageLabel;
	public JLabel profileNameLabel;
	private ImageIcon icon;
	public FileDialog fd;
	public String userName;
	public ImageIcon defaultImgIcon = new ImageIcon(UserProfile.class.getResource("/icons/default_profile.jpeg"));

	public UserProfile(String username) {
		userName = username;
//		frame = new JFrame();
//		frame.setBounds(0,0,300,50);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		profilePane = new JPanel();
		profilePane.setBounds(0,0,300,50);
		
		profileImageLabel = new JLabel("");
		profileImageLabel.setIcon(defaultImgIcon);       
		profileImageLabel.setBounds(10,0,50,50);
        
        profileNameLabel = new JLabel(userName);
        profileNameLabel.setBounds(70,0,240,50);
        
		profilePane.setLayout(null);
		
		profilePane.add(profileImageLabel);
		profilePane.add(profileNameLabel);		
		
	}	
	
	
	
	public JPanel getProfile() {
		return this.profilePane;
	}
}
