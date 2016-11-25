package kurs;

import java.util.List;

public class UI 
{
	static UtilHelper utilHelper = new UtilHelper();
	
	
	public static void main(String[] args) throws Exception
	{	
	validTest();	
	}
	
	
	public static void validTest() throws Exception
	{
		process("test-valid.input");
	}

	
	public static void process(String filename) throws Exception
	{
		Lexer lexer = new Lexer();
		lexer.processInput(filename);
		List<Token> tokens = lexer.getTokens();
		Parser parser = new Parser();
		parser.setTokens(tokens);
		parser.lang();
		List<List<PostfixToken>> numOfStr = parser.getPostfixToken();
		System.out.print("\nPOLIZ: \n\n");
		for(List<PostfixToken> currentStr: numOfStr)
		{
			System.out.print("[");
			for(PostfixToken currentToken: currentStr)
			{
				System.out.print(currentToken.getValue()+" ");
			}
			System.out.print("]");
		}
		System.out.println();
		PolizProcessor processor = new PolizProcessor(numOfStr);
		processor.go();
		processor.printVarTable();
	}

}