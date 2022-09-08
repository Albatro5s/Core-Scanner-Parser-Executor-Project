import java.io.IOException;

import executor.ID;
import executor.Executor;
import parser.ParseTree;
import parser.Parser;
import scanner.Tokenizer;


public class Main {

	public static void main(String[] args) {

		try {
			Tokenizer t = new Tokenizer(args[0]);
			
			// tokenizer -> parser
			ParseTree p = Parser.parseProgram(t);

			// parse tree -> pretty print
			System.out.println(p.programStr());

			// execute and print the output
			Executor e = new Executor(args[1], p);
			System.out.println(e.executeProgram());


		} catch (IOException e) {
			// check for errors
			System.out.println("ERROR: " + e.getMessage());
		}

	}

}