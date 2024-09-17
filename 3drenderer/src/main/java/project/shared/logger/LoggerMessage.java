package project.shared.logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoggerMessage {

	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String system;
	private int severity;
	private Object caller;
	private List<Object> messages;
	
	public LoggerMessage(String system, int severity, Object caller) {
		this.startTime = null;
		this.endTime = null;
		this.system = system;
		this.severity = severity;
		this.caller = caller;
		this.messages = new ArrayList<>();
	}
	
	
	void refreshTimestamp() {
		this.startTime = this.getSystemTime();
	}
	
	void timestamp() {
		if( this.startTime == null ) {
			this.startTime = this.getSystemTime();
		} else {
			this.endTime = this.getSystemTime();
		}
	}
	
	private LocalDateTime getSystemTime() {
		return LocalDateTime.now();
	}
	
	public void addMessage(Object message) {
		this.messages.add(message);
	}
	
	
	public String getTimestamp() {
		if( this.startTime == null ) {
			return null;
		}
		
		return (
			Logger.get().formatDateTimeString(this.startTime) +  
			(this.endTime == null ? "" : " - " + Logger.get().formatDateTimeString(this.endTime))
		);
	}
	
	public String getSystem() {
		return this.system;
	}
	
	public int getSeverity() {
		return this.severity;
	}
	
	public Object getCaller() {
		return this.caller;
	}
}
