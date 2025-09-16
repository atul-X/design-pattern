package behavioural.mediator;

import java.util.ArrayList;
import java.util.List;

interface ChatMediator {
	void sendMessage(ChatUser sender, String message);
	void addUser(ChatUser user);
}
class ChatRoom implements ChatMediator {
	private List<ChatUser> users;

	public ChatRoom() {
		this.users = new ArrayList<>();
	}

	@Override
	public void addUser(ChatUser user) {
		users.add(user);
	}

	@Override
	public void sendMessage(ChatUser sender, String message) {
		for (ChatUser user : users) {
			if (user != sender) {
				user.receiveMessage(message, sender);
			}
		}
	}
}
class ChatUser   {
	private String name;
	private ChatMediator mediator;
	public ChatUser(String name,ChatMediator chatMediator) {
		this.name = name;
		this.mediator=chatMediator;
	}

	public String getName() {
		return name;
	}

	public void sendMessage(String message) {
		System.out.println(this.name +" "+ message+" sends message to all users" );
		mediator.sendMessage(this, message);
	}
	public void receiveMessage(String message, ChatUser sender) {
		System.out.println(this.name + " received message from " + sender.getName() + ": " + message);
	}
}
public class WithMediatorPattern {
	public static void main(String[] args) {
		ChatRoom chatMediator = new ChatRoom();
		ChatUser user1 = new ChatUser("Alice",chatMediator);
		ChatUser user2 = new ChatUser("Bob",chatMediator);
		ChatUser user3 = new ChatUser("Charlie",chatMediator);
		chatMediator.addUser(user1);
		chatMediator.addUser(user2);
		chatMediator.addUser(user3);
		user1.sendMessage("Hello Bob!");
	}
}
