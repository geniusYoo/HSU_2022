//JavaObjServer.java ObjectStream ��� ä�� Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.swing.plaf.synth.SynthFormattedTextFieldUI;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Vector;
import java.util.jar.Attributes.Name;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class JavaObjServer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector UserVec = new Vector(); // ����� ����ڸ� ������ ����
	private Vector LoggedUserVec = new Vector();
	private Vector RoomVector = new Vector();
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����

	public ImageIcon defaultImgIcon = new ImageIcon(JavaObjServer.class.getResource("/icons/default_profile.jpeg"));
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjServer frame = new JavaObjServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JavaObjServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("����ڷκ��� ���� object : " + str+"\n");
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.append("room_id = " + msg.getRoomId() + "\n");
		textArea.append("userlist = " + msg.getUserList() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		private Vector<String> username_vc = new Vector<>();
		public String UserName;
		public String UserStatus;
		public String loggedName = "";
		public String logoutName = "";
		public String room_Id;
		public String room_userList;
		public Vector room_userVec = new Vector();
		public int defaultRoomId=1234;

		
		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// �Ű������� �Ѿ�� �ڷ� ����
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

			} catch (Exception e) {
				AppendText("userService error");
			}
		}
		
		public String getUsername() { 
			return this.UserName;
		}
		public void Login(ChatMsg cm) {
			AppendText("���ο� ������ " + UserName + " ����.");
			String msg = UserName;
			LoggedUserVec.add(msg);
			
			WriteOthers(cm); // ���� user_vc�� ���� ������ user�� ���Ե��� �ʾҴ�.

			for (int j=0;j<LoggedUserVec.size();j++) {
				if(!LoggedUserVec.elementAt(j).equals(UserName)){
					loggedName = (String) LoggedUserVec.elementAt(j);

					ChatMsg obcm = new ChatMsg("SERVER", "101", "0","0",loggedName);
					WriteOneObject(obcm);
				}
			}
			
		}

		public void Logout() {
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			UserVec.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			WriteAll(msg); // ���� ������ �ٸ� User�鿡�� ����
			AppendText("����� " + "[" + UserName + "] ����. ���� ������ �� " + UserVec.size());
			logoutName = UserName;
			for (int k=0;k<LoggedUserVec.size();k++) {
				if(LoggedUserVec.elementAt(k).equals(UserName)) {
					System.out.println(">>>>> logout username "+ UserName);
					LoggedUserVec.remove(k);
					System.out.println(">>>>> LoggedUserVec size "+LoggedUserVec.size());
					break;
				}else {
					ChatMsg obcm = new ChatMsg("SERVER","102","0","0",logoutName);
					WriteAllObject(obcm);
				}
			}
		}
		
		// ���� ������
		public void makeChatRoom(String room_id, String room_userlist) {
			room_Id = room_id;
			room_userList = room_userlist;
			ChatRoom room = new ChatRoom(room_id, room_userList);
			RoomVector.add(room);
			String [] temp = null;

			ChatMsg obcm = new ChatMsg("SERVER", "301", room_Id, room_userList, "make Room");
			AppendObject(obcm);
			temp = room_userList.split(" ");

			for(int i=0;i<temp.length;i++) {
				for(int k=0;k<user_vc.size();k++) {
					UserService user = (UserService) user_vc.elementAt(k);
					if(user.UserName.equals(temp[i])) {
						user.WriteOneObject(obcm);
					} else {
						continue;
					}
				}
			}
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(str);
			}
		}
		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// ���� ������ User�鿡�� ���. ������ UserService Thread�� WriteOne() �� ȣ���Ѵ�.
		public void WriteOthers(ChatMsg cm) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O") {
					user.MakeProfile(cm);
				}
			}
		}
		
		// ���ο� �����ڰ� �α����� �� ���ο� �����ڸ� ������ �����ڵ鿡�� ���ο� �������� �������� ������ �����ϴ� �Լ�.
		public void MakeProfile(ChatMsg cm) {
			try {
				ChatMsg obcm = new ChatMsg("SERVER", "101", "0","0",cm.getId());
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}
		
		// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
		public void WriteOne(String msg) {
			try {

				ChatMsg obcm = new ChatMsg("SERVER", "200","0","0", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {

					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}

		// �ӼӸ� ����
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("�ӼӸ�", "200", "0","0",msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}
		public void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.getCode().matches("100")) {
						UserName = cm.getId();
						username_vc.add(cm.getId());
						UserStatus = "O"; // Online ����
						Login(cm);
					} else if (cm.getCode().matches("200")) {
						msg = String.format("[%s] rood_id: %s, userlist: %s, msg: %s", cm.id, cm.getRoomId(), cm.getUserList(), cm.data);
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
						if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
							UserStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
						} else if (args[1].matches("/list")) {
							WriteOne("User list\n");
							WriteOne("Name\tStatus\n");
							WriteOne("-----------------------------\n");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								WriteOne(user.UserName + "\t" + user.UserStatus + "\n");
							}
							WriteOne("-----------------------------\n");
						} else if (args[1].matches("/sleep")) {
							UserStatus = "S";
						} else if (args[1].matches("/wakeup")) {
							UserStatus = "O";
						} else if (args[1].matches("/to")) { // �ӼӸ�
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// ���� message �κ�
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to ����.. [�ӼӸ�] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									//user.WriteOne("[�ӼӸ�] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // �Ϲ� ä�� �޽���
							UserStatus = "O";
							ChatMsg newcm = new ChatMsg(cm.getId(), "200", cm.getRoomId(), cm.getUserList(), cm.getData());
							String currentRoomId = cm.getRoomId();
							String currentUserList = "";
							for(int i=0; i<RoomVector.size(); i++) {
								ChatRoom room = (ChatRoom) RoomVector.elementAt(i);
								if(room.getRoomId().equals(currentRoomId)) {
									currentUserList = room.getUserList();
								}
							}
							String [] currentUserListArray = currentUserList.split(" "); // �ش� roomid�� ���� user���� �迭
							for(int k=0; k<currentUserListArray.length; k++) {
								for (int i = 0; i < user_vc.size(); i++) {
									UserService user = (UserService) user_vc.elementAt(i);
									if(user.UserName.matches(currentUserListArray[k])) {
										user.WriteOneObject(newcm);
									}
								}
							}
							
						}
					} 
					else if (cm.getCode().matches("103")) { // ������ ���� �ٲٱ�
						WriteAllObject(cm);
					}
					else if (cm.getCode().matches("201")) { // �̹��� ����
						
						UserStatus = "O";
						ChatMsg newcm = new ChatMsg(cm.getId(), "201", cm.getRoomId(), cm.getUserList(), cm.getData());
						newcm.setImg(cm.getImg());

						String currentRoomId = cm.getRoomId();
						String currentUserList = "";
						for(int i=0; i<RoomVector.size(); i++) {
							ChatRoom room = (ChatRoom) RoomVector.elementAt(i);
							if(room.getRoomId().equals(currentRoomId)) {
								currentUserList = room.getUserList();
							}
						}
						String [] currentUserListArray = currentUserList.split(" "); // �ش� roomid�� ���� user���� �迭
						for(int k=0; k<currentUserListArray.length; k++) {
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if(user.UserName.matches(currentUserListArray[k])) {
									user.WriteOneObject(newcm);
								}
							}
						}
					} else if (cm.getCode().matches("300")) { // plus_chat ��ư�� ������ ���� ���� (create room)
						
						room_Id = Integer.toString(((int)(Math.random()*100)));
						String userlist = cm.getUserList();
						System.out.println(">> room_id : " + room_Id + " userlist : " + userlist);
						makeChatRoom(room_Id, userlist);
						
					} else if (cm.getCode().matches("301")) { // ���� ���� �����ϱ� (remove room)
						
					} else if (cm.getCode().matches("400")) { // roomList�� �ִ� �� �� �ϳ��� ���� (����) (enter room)
						
					} else if (cm.getCode().matches("401")) { // ���� ���� (exit room)
						
					} else if (cm.getCode().matches("500")) { // �濡 ģ���� �ʴ�
						
					}
					
					else if (cm.getCode().matches("600")) { // Exit ó�� (������ ������)
						Logout();
						break;
					} 
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
	}

}