package project.shared;

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
	public void log(int logFlags, LoggerMessage message) {
		this.messageQueue.add(message);
		
		LoggerMessage queuedMessage;
		while( (queuedMessage = this.messageQueue.poll()) != null ) {
			System.out.println(
				Logger.get().createSignifier(Logger.LOG_TIMESTAMP, queuedMessage.getTimestamp()) + 
				Logger.get().createSignifier(Logger.LOG_SYSTEM, queuedMessage.getSystem()) +
				Logger.get().createSignifier(Logger.LOG_CALLER, queuedMessage.getCaller().toString()) +
				Logger.get().createSignifier(
					Logger.LOG_SYSTEM, Logger.get().getSeverityLabel(queuedMessage.getSeverity())
				) + ": "
			);
		}
	}
}
