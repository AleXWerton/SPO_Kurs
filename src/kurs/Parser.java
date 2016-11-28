package kurs;

import java.util.*;

public class Parser
{
	private List<Token> tokens;
	private Token currentToken;
	private Stack<Token> bracketsStack = new Stack<Token>();
	private int currentTokenNumber = 0;
	Map<String, List<Token>> structTable = new HashMap<String, List<Token>>();
	
	
	public void setTokens(List<Token> tokens) 
	{
		this.tokens = tokens;	
	}
	
	
	public void lang() throws Exception
	{
		boolean exist = false;
		while (currentTokenNumber < tokens.size() && expr())
		{
			exist = true;
		}
		if(!exist)
		{
			throw new Exception("expr expected, but "
					+currentToken +"found.");
		}
		currentTokenNumber=0;
	}
	
	
	public boolean expr() throws Exception 
	{
		if(struct()|| declare()||assign()||whileLoop()||doLoop())
		{
			return true;
		}
		else 
		{
			return false;
		}	
	}
	

	private boolean struct() throws Exception // structure
	{
		if(structKW())
		{
			if (var())
			{
				Token tempKey = currentToken;
				if(fOpenBr())
				{
					List<Token> tempValue = new ArrayList<Token>();
					if(var())
					{
						tempValue.add(currentToken);
						currentTokenNumber--;
					}
					if(declare()||assign())
					{
						do 
						{
							if (var()) 
							{
								tempValue.add(currentToken);
								currentTokenNumber--;
							}
						}
						while (declare()||assign());
						if(fCloseBr())
						{
							structTable.put(tempKey.getValue(), tempValue);
							System.out.println(structTable);
							return true;
						}
						else 
						{
							throw new Exception("} expected, but " + currentToken + " found");
						}
					}
					else
						throw new Exception("declare or Assign expected");
				}
				else
					throw new Exception("{ expected");
			}
			else
				throw new Exception("Struct name expected, but token "+ currentToken+" found");
		}
		else
			return false;
	}

	
	private boolean structKW()// structure keyword
	{
		int temp=currentTokenNumber;
		wsIgnore();
		if(!currentToken.getName().equals("STRUCT_KW"))
		{
			currentTokenNumber=temp;
		}
		return currentToken.getName().equals("STRUCT_KW");
	}

	
	private boolean structVar() throws Exception 
	{
		if(var())
		{
			Token tempStructName = currentToken;
			if(dot())
			{
				if(var()) 
				{
					Token tempStructVar = currentToken;
					if(structTable.containsKey(tempStructName.getValue()))
					{
						for(Token t: structTable.get(tempStructName.getValue()))
						{
							if(t.equals(tempStructVar))
								return true;
						}
						throw new Exception("Struct with name '"+tempStructName.getValue()+"' hasn't var '"+tempStructVar+"'");
					}
					else
						throw new Exception("Struct with name '"+tempStructName.getValue()+"' doesn't exist");
				}
				else
					throw new Exception("Expected var, but "+currentToken+" found");
			}
			else 
			{
				currentTokenNumber--;
				return false;
			}
		}
		else
			return false;
	}



	private boolean doLoop() throws Exception
	{
		if(doKw())
		{
			if(body())
			{
				if(whileKw())
				{
					if(condition())
					{
						if(sm())
							return true;
						else
							throw new Exception("Expected ';' ");
					}
					else 
						return false;
				}
				else
					return false;
			}
			else 
				return false;
		}
		else
			return false;
	}
	
	private boolean whileLoop() throws Exception
	{
		if(whileKw())
		{
			if(condition())
			{
				if(body())
					return true;
				else
					return false;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	
	private boolean whileKw() //while keyword
	{       
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("WHILE_KW"))
		{
			currentTokenNumber=temp;
		}
		return currentToken.getName().equals("WHILE_KW");
	}
	
	
	private boolean doKw() //do keyword
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("DO_KW"))
		{
			currentTokenNumber=temp;
		}
		return currentToken.getName().equals("DO_KW");
	}
	
	
	private boolean condition() throws Exception
	{
		if(openBr())
		{
			if(structVar()||var()||digit())
			{
				if(more()||less()||moreEq()||lessEq())
				{
					if(structVar()||var()||digit())
					{
						if(closeBr())
							return true;
						else
							throw new Exception("Close br expected, but " + currentToken +"found.");
					}
					else
						throw new Exception("VAR or DIGIT expected, but " + currentToken +"found.");
				}
				else
					throw new Exception("condition op expected, but " + currentToken +"found.");
			}
			else
				throw new Exception("VAR or DIGIT expected, but " + currentToken +"found.");
		}
		else
			throw new Exception("Open br expected, but " + currentToken +"found.");
	}
	
	
	private boolean body() throws Exception
	{
		if(fOpenBr())
		{
			if(expr())
			{
				do
				{
					
				}
				while(expr());
				if(fCloseBr())
					return true;
				else
					throw new Exception("Close figure br expected, but " + currentToken +"found.");
			}
			else
				throw new Exception("Error in body expr");
		}
		else
			throw new Exception("Open figure br expected, but " + currentToken +"found.");
	}
	
	
	private boolean fOpenBr() 
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("FBR_OP"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("FBR_OP");
	}
	
	
	private boolean fCloseBr() 
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("FBR_CL"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("FBR_CL");
	}


	
	private boolean more()// ">"
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("MORE"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("MORE");
	}
	
	
	private boolean less()// "<"
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("LESS"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("LESS");
	}
	
	
	private boolean moreEq() // ">="
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("MORE_EQ"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("MORE_EQ");
	}
	
	
	private boolean lessEq() // "<="
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("LESS_EQ"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("LESS_EQ");
	}


    public boolean declare() throws Exception
    {
                if(var())
                {
                    if(sm())
						return true;
                    else 
                    {
						currentTokenNumber--;
						return false;
					}
                }
                else
                {                 
                    return false;
                }
        }
    	
    
   
   
    
    
    
	public boolean assign() throws Exception // assign operation
	{
		if(structVar()||var())
		{
			if(assignOp())
			{
				if(stmt())
				{
                    if(!sm())
                        throw new Exception("Expected ';' but " + currentToken +" found.");
                    else
                        return true;
				}
                else
                {
                    throw new Exception("stmt  expected, but " + currentToken +"found.");
                }
			} 
			else
			{
				throw new Exception("assignOp  expected, but " + currentToken +"found.");

			}
		}
		else
		{
			return false;
		}

	}
	
	
	public boolean stmt() throws Exception
	{
		if(stmtUnit())
		{
			while(plus()||minus()||umn()||del())
			{
				if(!stmtUnit())
				{
					throw new Exception("stmt_unit  expected, but " + currentToken +"found.");
				}
			} 
			if(bracketsStack.empty())
				return true;
			else
				throw new Exception("Brañket ')' expected ");
		}
		else 
		{
			throw new Exception("stmt_unit  expected, but " + currentToken +"found.");
		}
		
	}
	public boolean stmtUnit() throws Exception
	{
		if (openBr()) //if bracket
		{
			bracketsStack.push(currentToken);
			if(!stmt())
			{
				throw new Exception("stmt expected, but " + currentToken +"found.");
			}
			else
				return true;
		}
		else if(structVar()||digit()||var())
		{
			if(closeBr())
			{
				do
				{
					if(bracketsStack.empty())
						throw new Exception("unexpected ')'");
					bracketsStack.pop();
				}
				
				while(closeBr());
				
			}
			return true;
		}
		return false;
		
	}
	

	
	public boolean sm() // ";"
	{
		int temp=currentTokenNumber; 
		next();
		if(!currentToken.getName().equals("SM"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("SM");
		
	}
	
	
	public boolean varKw() //variable keyword
	{
		int temp=currentTokenNumber; 
		next();
		if(!currentToken.getName().equals("VAR_KW"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("VAR_KW");
	}
	
	
	public boolean assignOp() // "="
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("ASSIGN_OP"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("ASSIGN_OP");
	}
	
	
	public boolean plus() // "+"
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("PL"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("PL");
	}
	
	
	public boolean minus() // "-"
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("MN"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("MN");
	}
	
	
	public boolean umn() // "*"
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("UMN"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("UMN");
	}
	
	
	public boolean del() // "/"
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("DL"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("DL");
	}
	
	
	public boolean openBr() // "("
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("BR_OP"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("BR_OP");	
	}
	
	
	public boolean closeBr() // ")"
	{ 
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("BR_CL"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("BR_CL");	
	}
	
	
	public boolean digit()
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("DIGIT"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("DIGIT");
	}
	
	
	public boolean var() // variable
	{
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("VAR"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("VAR");
	}
	

	private boolean dot() // "."
	{
		int temp=currentTokenNumber;
		next() ;
		if(!currentToken.getName().equals("DOT"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("DOT");
	}
	
	
	private void wsIgnore() // ignore " "
	{ 
		do
			next();
		while (currentToken.getName().equals("WS"));
	}
	
	
	public boolean next() // next Token
	{
		
		if (currentTokenNumber < tokens.size())
		{
			currentToken = tokens.get(currentTokenNumber);
			currentTokenNumber++;
			return true;
		} 
		else 
		{
			return false;
		}	
	}
	
	
	public List<List<PostfixToken>> getPostfixToken() throws Exception
	{
		return new GetPostfixToken().getPostfixToken();
	}
	
	
	
	
	
	
private class GetPostfixToken{
		List<List<PostfixToken>> numOfStr = new ArrayList<List<PostfixToken>>();
		
		public List<List<PostfixToken>> getPostfixToken() throws Exception {
			int p0ForDo=0;
			int p0ForWhile=0;
			boolean isDo = false;
			List<PostfixToken> poliz = new ArrayList<PostfixToken>();
			Stack<PostfixToken> stack = new Stack<PostfixToken>();
			String structName="";
			boolean inStruct=false;
			boolean endOfDoLoop = false;
			while(currentTokenNumber<tokens.size())
			{
				if(structVar())
				{
					poliz.add(new PostfixToken("VAR", tokens.get(currentTokenNumber-3).getValue()+
							tokens.get(currentTokenNumber-2).getValue()+tokens.get(currentTokenNumber-1).getValue()));
				}
				else if(var()||digit())
				{
					if(tokens.get(currentTokenNumber).getValue().equals("=")&&inStruct)
					{
						System.out.println("structVarDetected in " + structName + ": " + tokens.get(currentTokenNumber-1).getValue());
						poliz.add(new PostfixToken(currentToken.getName(), structName + "." + currentToken.getValue()));
					}
					else
						poliz.add(new PostfixToken(currentToken.getName(), currentToken.getValue()));
				}
				else if(structKW())
				{
					var();
					structName=currentToken.getValue();
					fOpenBr();
					inStruct=true;
				}
				else if(doKw())
				{
					p0ForDo=numOfStr.size();
					isDo=true;
				}
				else if(whileKw())
				{
					if(!isDo)
					{
						p0ForWhile=numOfStr.size();
					}
					else
					{
						endOfDoLoop=true;
					}
						
				}
				else if(fOpenBr())
				{
					if(!isDo)
					{
						while(!stack.empty())
						{
							poliz.add(stack.pop());
						}
						List<PostfixToken> temp = new ArrayList<PostfixToken>();
						temp.addAll(poliz);
						numOfStr.add(temp);
					    //System.out.println(numOfStr.get());
						poliz.clear();
					}
					getPostfixToken();
					
					if(!isDo)
					{
						int p1=numOfStr.size();
						List<PostfixToken> conditeon = numOfStr.get(p0ForWhile);
						conditeon.add(new PostfixToken("MARK", String.valueOf(p1)));
						conditeon.add(new PostfixToken("JNF", "!F"));
						numOfStr.set(p0ForWhile, conditeon);
						List<PostfixToken> endOfFBr = numOfStr.get(p1-1);
						endOfFBr.add(new PostfixToken("MARK", String.valueOf(p0ForWhile)));
						endOfFBr.add(new PostfixToken("JMP", "!"));
						numOfStr.set(p1-1, endOfFBr);
					}
					
					
				}
				else if(fCloseBr())
				{
					if(inStruct)
						inStruct=false;
					else if(currentTokenNumber!=tokens.size()-1)
					{
						return numOfStr;
					}
				} 
				else if (assignOp()||minus()||plus()||umn()||del()||less()||lessEq()||more()||moreEq())
				{
					if(!stack.empty())
					{
						while(!stack.empty()&&(getPrior(currentToken)<=getPrior(stack.peek())))
						{
							poliz.add(stack.pop());
						}
						
					}	
					stack.push(new PostfixToken(currentToken.getName(), currentToken.getValue()));
				}
				else if(openBr())
				{
					stack.push(new PostfixToken(currentToken.getName(), currentToken.getValue()));
				}
				else if(closeBr())
				{
					PostfixToken temp;
					temp=stack.pop();
				    
					while(!temp.getName().equals("BR_OP"))
					{
						poliz.add(temp);
						temp=stack.pop();
					}
				}
				else if(sm())
				{
					while(!stack.empty())
					{
						poliz.add(stack.pop());
					}
					List<PostfixToken> temp = new ArrayList<PostfixToken>();
					temp.addAll(poliz);
					
					if(isDo&&endOfDoLoop)
					{
						temp.add(new PostfixToken("MARK", String.valueOf(p0ForDo)));
						temp.add(new PostfixToken("JF", "F"));
						isDo=false;
						endOfDoLoop=false;
					}
					numOfStr.add(temp);
					poliz.clear();
				}
				else
					currentTokenNumber++;
			}
			while(!stack.empty())
			{
				poliz.add(stack.pop());
			}
			return numOfStr;
		}
		
		
		private int getPrior(Token op) //get priority
		{
			if(op.getName().equals("LESS") || op.getName().equals("MORE") || op.getName().equals("LESS_EQ") || op.getName().equals("MORE_EQ"))
				return 0;
			
			if(op.getName().equals("PL")||op.getName().equals("MN"))
			{
				return 1;	
			}
			else if(op.getName().equals("DL")||op.getName().equals("UMN"))
			{
				return 2;
			}
			else
			   return -1;
		}
		
	}
	
	
}