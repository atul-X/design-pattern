package behavioural.iterator.notificationmanagement;
import java.util.Iterator;

public interface NotificationCollection {
	public Iterator<Notification> createIterator();
}