package project.subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionManager {

	private Map<Integer, List<ISubscriber>> subscribers;
	
	public SubscriptionManager() {
		this.subscribers = new HashMap<>();
	}
	
	
	public void addSubscriber(ISubscriber subscriber, int event) {
		List<ISubscriber> subscriberList = this.subscribers.get(event);
		
		if( subscriberList == null ) {
			subscriberList = new ArrayList<>();
			this.subscribers.put(event, subscriberList);
		}
		
		subscriberList.add(subscriber);
	}
	
	public void notifySubscribers(int event, Object... objects) {
		for( ISubscriber subscriber : this.subscribers.get(event) ) {
			subscriber.on(event, objects);
		}
	}
}
