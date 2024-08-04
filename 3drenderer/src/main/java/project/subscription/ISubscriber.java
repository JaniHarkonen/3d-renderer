package project.subscription;

public interface ISubscriber {

	public void on(int event, Object... objects);
}
