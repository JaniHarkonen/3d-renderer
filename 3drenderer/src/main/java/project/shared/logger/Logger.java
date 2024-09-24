package project.shared.logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class Logger {
	public static final int MUTED = 0;
	public static final int FATAL = 1;
	public static final int ERROR = 2;
	public static final int WARN  = 3;
	public static final int INFO  = 4;
	
	public static final int LOG_TIMESTAMP = 1;
	public static final int LOG_SYSTEM = 2;
	public static final int LOG_CALLER = 4;
	public static final int LOG_SEVERITY = 8;
	
	private static Logger instance;
	
	public static void configure(int logFlags, int verbosity) {
		if( instance == null ) {
			instance = new Logger(logFlags, verbosity);
		}
	}
	
	public static Logger get() {
		return instance;
	}
	
	
	private final Map<Integer, String> severityLabels;
	
	private int verbosity;
	private int logFlags;
	private List<ILogRecorder> recorders;
	
	private Logger(int logFlags, int verbosity) {
		this.verbosity = verbosity;
		this.logFlags = logFlags;
		this.recorders = new ArrayList<>();
		this.severityLabels = new HashMap<>();
		
		this.severityLabels.put(MUTED, "");
		this.severityLabels.put(FATAL, "FATAL ERROR");
		this.severityLabels.put(ERROR, "ERROR");
		this.severityLabels.put(WARN, "Warning");
		this.severityLabels.put(INFO, "Info");
	}
	
	public void registerTarget(ILogRecorder target) {
		this.recorders.add(target);
	}
	
	private LoggerMessage createMessage(int severity, Object me) {
		if( this.verbosity < severity ) {
			return null;
		}
		
		return new LoggerMessage(Thread.currentThread().getName(), severity, me);
	}
	
	public void log(int severity, Object me, Function<LoggerMessage, Boolean> batcher) {
		LoggerMessage loggerMessage = this.createMessage(severity, me);
		
		if( loggerMessage == null ) {
			return;
		}
		
		boolean isLogged;
		loggerMessage.timestamp();
		isLogged = batcher.apply(loggerMessage);
		
		if( !isLogged ) {
			return;
		}
		
		loggerMessage.timestamp();
		this.log(loggerMessage);
	}
	
	public void log(int severity, Object me, Object... messages) {
		LoggerMessage loggerMessage = this.createMessage(severity, me);
		
		if( loggerMessage == null ) {
			return;
		}
		
		for( Object message : messages ) {
			loggerMessage.addMessage(message);
		}
		
		loggerMessage.refreshTimestamp();
		this.log(loggerMessage);
	}
	
	private void log(LoggerMessage message) {
		for( ILogRecorder recorder : this.recorders ) {
			recorder.log(this.logFlags, message);
		}
	}
	
	public void info(Object me, Function<LoggerMessage, Boolean> batcher) {
		this.log(INFO, me, batcher);
	}
	
	public void info(Object me, Object... messages) {
		this.log(INFO, me, messages);
	}
	
	public void warn(Object me, Function<LoggerMessage, Boolean> batcher) {
		this.log(WARN, me, batcher);
	}
	
	public void warn(Object me, Object... messages) {
		this.log(WARN, me, messages);
	}
	
	public void error(Object me, Function<LoggerMessage, Boolean> batcher) {
		this.log(ERROR, me, batcher);
	}
	
	public void error(Object me, Object... messages) {
		this.log(ERROR, me, messages);
	}
	
	public void fatal(Object me, Function<LoggerMessage, Boolean> batcher) {
		this.log(FATAL, me, batcher);
	}
	
	public void fatal(Object me, Object... messages) {
		this.log(FATAL, me, messages);
	}
	
	public String createSignifierIfLogged(int signifierFlag, String signifier) {
		return ((this.logFlags & signifierFlag) == signifierFlag) ? "[" + signifier + "]" : "";
	}
	
	public String formatDateTimeString(LocalDateTime dateTime) {
		return (
			dateTime.getHour() + ":" + 
			dateTime.getMinute() + ":" + 
			dateTime.getSecond() + "." + 
			dateTime.getNano() / 1000000
		);
	}
	
	public String getSeverityLabel(int severity) {
		return this.severityLabels.get(severity);
	}
	
	public int getVerbosity() {
		return this.verbosity;
	}
	
	public boolean doesLog(int logFlags) {
		return (this.logFlags & logFlags) == logFlags;
	}
}
