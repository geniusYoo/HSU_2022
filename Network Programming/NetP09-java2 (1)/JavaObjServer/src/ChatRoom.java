import java.util.StringTokenizer;

public class ChatRoom {
	public String room_id;
	public String room_userList;
	public String room_ChatMsgList;
	
	
	public ChatRoom(String room_id, String room_userList) {
		this.room_id = room_id;
		this.room_userList = room_userList;
	}
	
	public String getRoomId() {
		return room_id;
	}
	public String getUserList() {
		return room_userList;
	}
}
