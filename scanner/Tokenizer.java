package scanner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Tokenizer {
	BufferedReader in;
    // BufferedWriter output;
    FileWriter file;
	TokenTuple currentToken;
	String endToken = ";|,|:|!|(|)|\\[|\\]|=|<|>|\\+|-|\\*|\\|";

	public Tokenizer(String fileName) throws IOException{
		in = new BufferedReader(new FileReader(fileName));
        //output = new BufferedWriter(new FileWriter(fileNameOut));
		/*System.out.println(currentToken.token);
		nextToken();
		System.out.println(currentToken.id);
		nextToken();
		System.out.println(currentToken.content);*/
        
		/*do {
			nextToken();
			System.out.println(currentToken.id + ", " + currentToken.content);
			// output.write(currentToken.id + " ");
        } while(currentToken.token != Tokens.EOF);*/
		
		// without do-while (i.e. just pull first token)
		nextToken();
	}
	// nextToken() == skipToken()
	// (skipToken was confusing during implementation, but it functions the same as described in the slides)
	// skips past white space until next token starts
	public void nextToken() throws IOException{
		int next = in.read();
		while (Character.isWhitespace(next) && next != -1)
		{
			next = in.read();
		}
		if (next == -1)
		{
			currentToken =  new TokenTuple(Tokens.EOF, 33, "");
		}else{

			switch((char) next){

			case ';': currentToken = new TokenTuple(Tokens.SEMICOLON, 12, ";"); break;
			case ',': currentToken = new TokenTuple(Tokens.COMMA, 13, ","); break;
			case '[': currentToken = new TokenTuple(Tokens.LBRACKET, 16, "["); break;
			case ']': currentToken = new TokenTuple(Tokens.RBRACKET, 17, "]"); break;
			case '(': currentToken = new TokenTuple(Tokens.LPAREN, 20, "("); break;
			case ')': currentToken = new TokenTuple(Tokens.RPAREN, 21, ")"); break;
			case '+': currentToken = new TokenTuple(Tokens.PLUS, 22, "+"); break;
			case '-': currentToken = new TokenTuple(Tokens.MINUS, 23, "-"); break;
			case '*': currentToken = new TokenTuple(Tokens.TIMES, 24, "*"); break;
			case '&': currentToken = new TokenTuple(Tokens.AND, 18, "&"); break;
			case '|': currentToken = new TokenTuple(Tokens.BAR, 19, "|"); break;
			
			// possible tokens of 2 characters
			case '=':{
				in.mark(1);
				next = in.read();
				if ((char) next == '='){
					currentToken = new TokenTuple(Tokens.EQ, 26, "==");
				}else{
					in.reset();
					currentToken = new TokenTuple(Tokens.ASSIGNMENT, 14, "="); break;
				}
				 break;
			}
			case '!':{
				in.mark(1);
				next = in.read();
				if ((char) next == '='){
					currentToken = new TokenTuple(Tokens.NOTEQ, 25, "!=");
				}else{
					in.reset();
					currentToken = new TokenTuple(Tokens.NOT, 15, "!");
				}
				 break;
			}
			case '<':{
				in.mark(1);
				next = in.read();
				if ((char) next == '='){
					currentToken = new TokenTuple(Tokens.LTEQ, 29, "<=");
				}else{
					in.reset();
					currentToken = new TokenTuple(Tokens.LT, 27, "<");
				}
				 break;
			}
			case '>':{
				in.mark(1);
				next = in.read();
				if ((char) next == '='){
					currentToken = new TokenTuple(Tokens.GTEQ, 30, ">=");
				}else{
					in.reset();
					currentToken = new TokenTuple(Tokens.GT, 28, ">");
				}
				 break;
			}
			// if token is either constantm, id, or invalid
			default:{
				boolean continued = true;
				StringBuilder buffer = new StringBuilder();
				while(continued){
					buffer.append((char) next);
					in.mark(1);
					next = in.read();
					continued &= next != -1;
					continued &= !Character.isWhitespace(next);
					continued &= !Character.toString((char)next).matches(endToken);
					if(!continued){
						in.reset();
					}
				}
				currentToken = new TokenTuple(buffer.toString());
			}
			 break;
			}
		}
	}
	// returns current token tuple
	public TokenTuple getToken(){
		return currentToken;
	}

	// returns int id of the current token
	public int intVal(){
		return currentToken.id;
	}

	// returns String name of current token
	public String idName(){
		return currentToken.content;
	}
}

