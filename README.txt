CSE 3341 - 
    10:20am MWF
Tristan Roman.198

File descriptions -

executor/...
    Executor.java
        provided both input file and parse tree representation, it creates an ID list and executed the program code, returning expected outputs
    ID.java
        stores and assigns names and values of different IDs

parser/...
    NodeTypes.java
        enum of the different types of parse tree nodes
    Parser.java
        provided a scanner it returns a parse tree of the input program code
    ParseTree.java
        parse tree representation of program code with methods for printing nodes

scanner/...
    Tokenizer.java
        given an input file name, returns the tokenized input to the terminal
    Tokens.java
        enum of different tokens
    TokenTuple.java
        utility class which stores token type, id number, and content
Main.java
    contains the main function


Compile/Running Instructions:

    javac scanner/Tokens.java
    javac scanner/TokenTuple.java
    javac scanner/Tokenizer.java

    javac parser/NodeTypes.java
    javac parser/ParseTree.java
    javac parser/Parser.java

    javac executor/ID.java
    javac executor/Executor.java

    javac Main.java

    java Main.java [input_program] [data_file_name]
