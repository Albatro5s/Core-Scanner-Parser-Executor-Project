package scanner;

//tuple of current token and the string representation of it.
public class TokenTuple {
	public Tokens token;
	public int id;
	public String content;
	//explicit constructor, takes token and content
	public TokenTuple(Tokens token, int id, String content){
		this.token=token;
		this.id=id;
		this.content=content;
	}
	//generates token and content from string
	public TokenTuple(String strtoken) {
		//legal ID possibilties
		String id = "[A-Z][A-Z0-9]*";
		//legal constants
		String constant = "-?[0-9]+";
		this.content = strtoken;

		if (strtoken.equals("program")){
			this.token = Tokens.PROGRAM;
			this.id = 1;
		}else if (strtoken.equals("begin")){
			this.token = Tokens.BEGIN;
			this.id = 2;
		}else if (strtoken.equals("end")){
			this.token = Tokens.END;
			this.id = 3;
		}else if (strtoken.equals("int")){
			this.token = Tokens.INT;
			this.id = 4;
		}else if (strtoken.equals("if")){
			this.token = Tokens.IF;
			this.id = 5;
		}else if (strtoken.equals("then")){
			this.token = Tokens.THEN;
			this.id = 6;
		}else if (strtoken.equals("else")){
			this.token = Tokens.ELSE;
			this.id = 7;
		}else if (strtoken.equals("while")){
			this.token = Tokens.WHILE;
			this.id = 8;
		}else if (strtoken.equals("loop")){
			this.token = Tokens.LOOP;
			this.id = 9;
		}else if (strtoken.equals("read")){
			this.token = Tokens.READ;
			this.id = 10;
		}else if (strtoken.equals("write")){
			this.token = Tokens.WRITE;
			this.id = 11;
		}else if (strtoken.matches(id)){
			this.token = Tokens.ID;
			this.id = 32;
		}else if (strtoken.matches(constant)){
			this.token = Tokens.CONST;
			this.id = 31;
		}else{
			throw new IllegalStateException("Invalid token");
		}
	}
	public String toString(){
		return this.token.toString() + " " + this.content;
	}
}