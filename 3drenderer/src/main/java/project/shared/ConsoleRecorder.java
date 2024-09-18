package project.shared;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import project.shared.logger.ILogRecorder;
import project.shared.logger.Logger;
import project.shared.logger.LoggerMessage;

public class ConsoleRecorder implements ILogRecorder {
	private final Queue<LoggerMessage> messageQueue;
	
	public ConsoleRecorder() {
		this.messageQueue = new ConcurrentLinkedQueue<>();
	}

	
	@Override
	public void log(int logFlags, LoggerMessage loggerMessage) {
		this.messageQueue.add(loggerMessage);
		
		Logger logger = Logger.get();
		LoggerMessage queuedMessage;
		while( (queuedMessage = this.messageQueue.poll()) != null ) {
			List<Object> messages = loggerMessage.getMessages();
			String messageString = messages.get(0).toString();
			
			for( int i = 1; i < messages.size(); i++ ) {
				messageString += "\n" + messages.get(i).toString();
			}
			
			String signTimestamp = logger.createSignifierIfLogged(
				Logger.LOG_TIMESTAMP, queuedMessage.getTimestamp()
			);
			String signSystem = logger.createSignifierIfLogged(
				Logger.LOG_SYSTEM, queuedMessage.getSystem()
			);
			String signCaller = logger.createSignifierIfLogged(
				Logger.LOG_CALLER, queuedMessage.getCaller().toString()
			);
			String signSeverity = logger.createSignifierIfLogged(
				Logger.LOG_SEVERITY, logger.getSeverityLabel(queuedMessage.getSeverity())
			);
			
			System.out.println(
				signTimestamp + signSystem + signCaller + signSeverity + ": " + 
				((messages.size() > 1) ? "\n" : "") + messageString
			);
		}
	}
}
