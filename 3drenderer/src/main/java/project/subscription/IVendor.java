package project.subscription;

public interface IVendor {

	public void subscribe(ISubscriber subscriber, int event);
	public void emit(int event, Object... objects);
}
