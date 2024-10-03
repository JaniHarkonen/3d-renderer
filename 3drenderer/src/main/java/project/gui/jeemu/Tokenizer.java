package project.gui.jeemu;

import java.util.ArrayList;
import java.util.List;

import project.gui.props.Property;

public class Tokenizer {
	
	private class Token {
		private Property property;
	}
	
	private List<Token> tokens;
	private int cursor;
	
	public Tokenizer() {
		this.tokens = new ArrayList<>();
		this.cursor = 0;
	}

	
	public List<Token> tokenize() {
		
	}
	
	private String text() {
		return null;
	}
}
