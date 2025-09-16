package mediator;
class User {
	private String name;

	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void sendMessage(String message, User recipient) {
		System.out.println(this.name +" "+ message+" sends message to " + recipient.getName()  );
	}
}

public class WithOutMediatorPattern {
	public static void main(String[] args) {
		User user1 = new User("Alice");
		User user2 = new User("Bob");
		User user3 = new User("Charlie");
		user1.sendMessage("Hello Bob!", user2);
		user1.sendMessage("Hi Alice!", user3);
	}
}
