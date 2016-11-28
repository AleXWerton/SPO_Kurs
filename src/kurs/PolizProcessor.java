package kurs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class PolizProcessor
{	
	private Map<String, Integer> varTable = new HashMap<String, Integer>();
	private List<List<PostfixToken>> numOfStr;
	private Stack<PostfixToken> stack = new Stack<PostfixToken>();
	private PostfixToken right, left;
	private int currentToketnNumber;
	public PolizProcessor(List<List<PostfixToken>> numOfStr)
	{
	this.numOfStr=numOfStr;
	}
	
	
	public void go() 
	{
		for(currentToketnNumber=0; currentToketnNumber<numOfStr.size(); currentToketnNumber++)
		{
			List<PostfixToken> currentStr = numOfStr.get(currentToketnNumber);
			for(PostfixToken currentToken: currentStr)
			{
				switch (currentToken.getName())
				{
					case Lexer.DIGIT: 
					case Lexer.VAR:
					case Lexer.MARK:
						stack.push(currentToken);
						break;
					case Lexer.ASSIGN_OP:
						assignOp();
						break;
					case Lexer.PL:
						plusOp();
						break;
					case Lexer.MN:
						minusOp();
						break;
					case Lexer.DL:
						divOp();
						break;
					case Lexer.UMN:
						multOp();
						break;
					case Lexer.LESS:
						lessOp();
						break;
					case Lexer.MORE:
						moreOp();
						break;
					case Lexer.MORE_EQ:
						moreEqOp();
						break;
					case Lexer.LESS_EQ:
						lessEqOp();
						break;
					case Lexer.JNF:
						jnfOp();
						break;
					case Lexer.JF:
						jfOp();
						break;
					case Lexer.JMP:
						jmpOp();
						break;
				}
			}
		}
	}
	

	public void printVarTable()
	{
		Set<Map.Entry<String, Integer>> set = varTable.entrySet();
		System.out.println("Var table:");
		for (Map.Entry<String, Integer> me : set) 
		{
			System.out.println(me.getKey() + ": "+me.getValue());
		}
	}
	
	
	private int valueOfToken(PostfixToken token)
	{
		if(token.getName().equals("DIGIT"))
			return Integer.valueOf(token.getValue());
		if(token.getName().equals("VAR"))
		{
			return Integer.valueOf(varTable.get(token.getValue()));
		}
		else
			return 0;
	}
	
	
	private void assignOp()
	{
		takeOperands();
		varTable.put(left.getValue(), Integer.valueOf(right.getValue()));
	}
	
	
	private void plusOp()
	{
		takeOperands();
		stack.push(new PostfixToken("DIGIT", String.valueOf(valueOfToken(left)+valueOfToken(right))));
	}
	
	
	private void minusOp()
	{
		takeOperands();
		stack.push(new PostfixToken("DIGIT", String.valueOf(valueOfToken(left)-valueOfToken(right))));
	}
	
	
	private void multOp()
	{
		takeOperands();
		stack.push(new PostfixToken("DIGIT", String.valueOf(valueOfToken(left)*valueOfToken(right))));
	}
	
	
	private void divOp()
	{
		takeOperands();
		stack.push(new PostfixToken("DIGIT", String.valueOf(valueOfToken(left)/valueOfToken(right))));
	}
	
	
	private void takeOperands()
	{
		right = stack.pop();
		left = stack.pop();
	}
	
	
	private void lessEqOp() 
	{
		takeOperands();
			if(valueOfToken(left)<=valueOfToken(right))
				stack.push(new PostfixToken("BOOL", String.valueOf(true)));
			else
				stack.push(new PostfixToken("BOOL", String.valueOf(false)));
	}
	

	private void moreEqOp()
	{
		takeOperands();
		if(valueOfToken(left)>=valueOfToken(right))
			stack.push(new PostfixToken("BOOL", String.valueOf(true)));
		else
			stack.push(new PostfixToken("BOOL", String.valueOf(false)));
	}

	
	private void moreOp()
	{
		takeOperands();
		if(valueOfToken(left)>valueOfToken(right))
			stack.push(new PostfixToken("BOOL", String.valueOf(true)));
		else
			stack.push(new PostfixToken("BOOL", String.valueOf(false)));
	}

	
	private void lessOp()
	{
		takeOperands();
		if(valueOfToken(left)<valueOfToken(right))
			stack.push(new PostfixToken("BOOL", String.valueOf(true)));
		else
			stack.push(new PostfixToken("BOOL", String.valueOf(false)));
	}
	
	
	private void jnfOp()
	{
		//System.out.println(stack.peek());
		takeOperands();
		if(!Boolean.valueOf(left.getValue()))
			currentToketnNumber=Integer.valueOf(right.getValue())-1;		
	}
	
	
	private void jfOp() 
	{
		//System.out.println(stack.peek());
		takeOperands();
		if(Boolean.valueOf(left.getValue()))
			currentToketnNumber=Integer.valueOf(right.getValue())-1;		
	}

	
	private void jmpOp()
	{
		currentToketnNumber=Integer.valueOf(stack.pop().getValue())-1;
	}

	
}
