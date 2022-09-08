package parser;
import java.io.IOException;
import scanner.Tokenizer;
import scanner.Tokens;

//recursive descent parser for core programming language
//based off Tokenizer t
public class Parser {

	//initial publicly available call
	//takes a fresh scanner t, returns full parse tree.
	//all trees from other calls are appended directly or indirectly to this tree
	public static ParseTree parseProgram(Tokenizer t) throws IOException{
		ParseTree p = new ParseTree(NodeTypes.PROG);
		if (t.getToken().token != Tokens.PROGRAM){
			throw new IllegalStateException("\"program\" expected");
		}
		t.nextToken();
        System.out.println(t.getToken());
		p.append(parseDeclSeq(t));
		if (t.getToken().token != Tokens.BEGIN){
			throw new IllegalStateException("\"begin\" expected");
		}
		t.nextToken();
        System.out.println(t.getToken());
		p.append(parseStmtSeq(t));
		if (t.getToken().token != Tokens.END){
			throw new IllegalStateException("\"end\" expected");
		}
		t.nextToken();
        System.out.println(t.getToken());
		if (t.getToken().token != Tokens.EOF){
			throw new IllegalStateException("eof expected");
		}

		return p;
	}

	//call for parseDeclSeq, returns ParseTree of type DECLSEQ
	private static ParseTree parseDeclSeq(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.DECLSEQ);
		if (t.getToken().token != Tokens.INT){
			throw new IllegalStateException("\"int\" expected");
		}
		p.append(parseDecl(t));
		while(t.getToken().token!= Tokens.BEGIN){
			p.append(parseDeclSeq(t));
		}
		return p;
	}

	//call for single decl
	private static ParseTree parseDecl(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.DECL);
		if (t.getToken().token != Tokens.INT){
			throw new IllegalStateException("\"int\" expected");
		}
		t.nextToken();
		p.append(parseIdList(t));
		if (t.getToken().token != Tokens.SEMICOLON){
			throw new IllegalStateException("\";\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for id-list
	private static ParseTree parseIdList(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.IDLIST);
		if (t.getToken().token != Tokens.ID){
			throw new IllegalStateException("ID expected");
		}
		p.append(new ParseTree(NodeTypes.ID, t.getToken().content));
		t.nextToken();
		if (t.getToken().token == Tokens.COMMA){
			t.nextToken();
			p.append(parseIdList(t));
		}
		return p;
	}
	//call for stmt-seq
	private static ParseTree parseStmtSeq(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.STMTSEQ);
		// System.out.println(t.getToken());
		
		if (t.getToken().token==Tokens.ID){
			p.append(parseAssign(t));
		}else if(t.getToken().token==Tokens.IF){
			p.append(parseIf(t));
		}else if(t.getToken().token==Tokens.WHILE){
			p.append(parseWhile(t));
		}else if(t.getToken().token==Tokens.INPUT){
			p.append(parseInput(t));
		}else if(t.getToken().token==Tokens.OUTPUT){
			p.append(parseOutput(t));
		}else if(t.getToken().token==Tokens.CASE){
			p.append(parseCase(t));
		}else if(t.getToken().token==Tokens.READ){
			// System.out.println("Tried to read");
			p.append(parseRead(t));
		}else{
			throw new IllegalStateException("statement expected");
		}
		boolean more = false;
		more |= t.getToken().token==Tokens.ID;
		// more |= t.getToken().token==Tokens.DO;
		more |= t.getToken().token==Tokens.INPUT;
		more |= t.getToken().token==Tokens.OUTPUT;
		more |= t.getToken().token==Tokens.CASE;
		if(more){
			p.append(parseStmtSeq(t));
		}
		return p;
	}
	//base call for case
	private static ParseTree parseCase(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.CASE);
		if (t.getToken().token != Tokens.CASE){
			throw new IllegalStateException("\"case\" expected");
		}
		t.nextToken();
		p.append(new ParseTree(NodeTypes.CONST, t.getToken().content));
		t.nextToken();
		if (t.getToken().token != Tokens.OF){
			throw new IllegalStateException("\"of\" expected");
		}
		t.nextToken();
		p.append(parseCaseList(t));
		if (t.getToken().token != Tokens.ELSE){
			throw new IllegalStateException("\"else\" expected");
		}
		t.nextToken();
		p.append(parseExpr(t));
		if (t.getToken().token != Tokens.END){
			throw new IllegalStateException("\"end\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for list of case options
	private static ParseTree parseCaseList(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.CASELIST);
		p.append(parseConstList(t));
		if (t.getToken().token != Tokens.COLON){
			throw new IllegalStateException("\":\" expected");
		}
		t.nextToken();
		p.append(parseExpr(t));
		if(t.getToken().token== Tokens.BAR){
			t.nextToken();
			p.append(parseCaseList(t));
		}
		return p;
	}
	//call for list of constants in case
	private static ParseTree parseConstList(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.CONSTLIST);
		if (t.getToken().token != Tokens.CONST){
			throw new IllegalStateException("CONST expected");
		}
		p.append(new ParseTree(NodeTypes.CONST, t.getToken().content));
		t.nextToken();
		if (t.getToken().token == Tokens.COMMA){
			t.nextToken();
			p.append(parseConstList(t));
		}
		return p;
	}
	//call for output
	private static ParseTree parseOutput(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.OUT);
		if (t.getToken().token != Tokens.OUTPUT){
			throw new IllegalStateException("\"output\" expected");
		}
		t.nextToken();
		p.append(parseIdList(t));
		if (t.getToken().token != Tokens.SEMICOLON){
			throw new IllegalStateException("\";\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for input
	private static ParseTree parseInput(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.IN);
		if (t.getToken().token != Tokens.INPUT){
			throw new IllegalStateException("\"input\" expected");
		}
		t.nextToken();
		p.append(parseIdList(t));
		if (t.getToken().token != Tokens.SEMICOLON){
			throw new IllegalStateException("\";\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for read
	private static ParseTree parseRead(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.IN);
		if (t.getToken().token != Tokens.READ){
			throw new IllegalStateException("\"read\" expected");
		}
		t.nextToken();
		p.append(parseIdList(t));
		if (t.getToken().token != Tokens.SEMICOLON){
			throw new IllegalStateException("\";\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for while
	private static ParseTree parseWhile(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.LOOP);
		/* if (t.getToken().token != Tokens.DO){
			throw new IllegalStateException("\"do\" expected");
		}*/
		t.nextToken();
		p.append(parseStmtSeq(t));
		if (t.getToken().token != Tokens.WHILE){
			throw new IllegalStateException("\"while\" expected");
		}
		t.nextToken();
		p.append(parseCond(t));
		if (t.getToken().token != Tokens.END){
			throw new IllegalStateException("\"end\" expected");
		}
		t.nextToken();
		if (t.getToken().token != Tokens.SEMICOLON){
			throw new IllegalStateException("\";\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for if
	private static ParseTree parseIf(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.IF);
		if (t.getToken().token != Tokens.IF){
			throw new IllegalStateException("\"if\" expected");
		}
		t.nextToken();
		p.append(parseCond(t));
		if (t.getToken().token != Tokens.THEN){
			throw new IllegalStateException("\"then\" expected");
		}
		t.nextToken();
		p.append(parseStmtSeq(t));
		if (t.getToken().token == Tokens.ELSE){
			t.nextToken();
			p.append(parseStmtSeq(t));
		}
		if (t.getToken().token != Tokens.END){ //ENDIF
			throw new IllegalStateException("\"endif\" expected");
		}
		t.nextToken();
		if (t.getToken().token != Tokens.SEMICOLON){
			throw new IllegalStateException("\";\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for cond
	private static ParseTree parseCond(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.COND);
		if(t.getToken().token == Tokens.NOT){
			p.append(new ParseTree(NodeTypes.NOT));
			t.nextToken();
			p.append(parseCond(t));
		}else if(t.getToken().token == Tokens.LPAREN){
			t.nextToken();
			p.append(parseCond(t));
			if(t.getToken().token == Tokens.AND){
				p.append(new ParseTree(NodeTypes.AND));
			} else if(t.getToken().token == Tokens.OR){
				p.append(new ParseTree(NodeTypes.OR));
			} else{
				throw new IllegalStateException("\"AND\" or \"OR\"expected");
			}
			t.nextToken();
			p.append(parseCond(t));
			if (t.getToken().token != Tokens.RPAREN){
				throw new IllegalStateException("\")\" expected");
			}
			t.nextToken();
		} else{
			p.append(parseCmpr(t));
		}
		return p;
	}

	//call for cmpr
	private static ParseTree parseCmpr(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.CMPR);			
		if (t.getToken().token != Tokens.LBRACKET){
			throw new IllegalStateException("\"[\" expected");
		}
		t.nextToken();
		p.append(parseExpr(t));
		boolean valid = false;
		valid |= t.getToken().token == Tokens.LT;
		valid |= t.getToken().token == Tokens.LTEQ;
		valid |= t.getToken().token == Tokens.EQ;
		valid |= t.getToken().token == Tokens.GT;
		valid |= t.getToken().token == Tokens.GTEQ;
		valid |= t.getToken().token == Tokens.NOTEQ;
		if (!valid){
			throw new IllegalStateException("Comparison operator expected");
		}
		p.append(new ParseTree(NodeTypes.CMPROP, t.getToken().content));
		t.nextToken();
		p.append(parseExpr(t));		
		if (t.getToken().token != Tokens.RBRACKET){
			throw new IllegalStateException("\"]\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for assign
	private static ParseTree parseAssign(Tokenizer t) throws IOException{
		ParseTree p = new ParseTree(NodeTypes.ASSIGN);
		if (t.getToken().token != Tokens.ID){
			throw new IllegalStateException("ID expected");
		}
		p.append(new ParseTree(NodeTypes.ID, t.getToken().content) );
		t.nextToken();
		if (t.getToken().token != Tokens.ASSIGNMENT){
			throw new IllegalStateException("\":=\" expected");
		}
		t.nextToken();
		p.append(parseExpr(t));
		if (t.getToken().token != Tokens.SEMICOLON){
			throw new IllegalStateException("\";\" expected");
		}
		t.nextToken();
		return p;
	}
	//call for expr
	private static ParseTree parseExpr(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.EXPR);
		p.append(parseTerm(t));
		if (t.getToken().token==Tokens.PLUS){
			p.append(new ParseTree(NodeTypes.PLUS));
			t.nextToken();
			p.append(parseExpr(t));
		}else if (t.getToken().token==Tokens.MINUS){
			p.append(new ParseTree(NodeTypes.MINUS));
			t.nextToken();
			p.append(parseExpr(t));
		}
		return p;
	}

	//call for term
	private static ParseTree parseTerm(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.TERM);
		p.append(parseFactor(t));
		if (t.getToken().token == Tokens.TIMES){
			p.append(new ParseTree(NodeTypes.TIMES));
			t.nextToken();
			p.append(parseTerm(t));
		}
		return p;
	}

	//call for factor
	private static ParseTree parseFactor(Tokenizer t) throws IOException {
		ParseTree p = new ParseTree(NodeTypes.FACTOR);
		if(t.getToken().token==Tokens.CONST){
			p.append(new ParseTree(NodeTypes.CONST, t.getToken().content));
			t.nextToken();
		}else if(t.getToken().token==Tokens.ID){
			p.append(new ParseTree(NodeTypes.ID, t.getToken().content));
			t.nextToken();
		}else if(t.getToken().token==Tokens.MINUS){
			p.append(new ParseTree(NodeTypes.MINUS));
			t.nextToken();
			p.append(parseFactor(t));
		}else if(t.getToken().token==Tokens.LPAREN){
			t.nextToken();
			p.append(parseExpr(t));
			if(t.getToken().token!= Tokens.RPAREN){
				throw new IllegalStateException("\")\" expected");
			}
			t.nextToken();
		}else{
			throw new IllegalStateException("factor expected");
		}
		return p;
	}

}