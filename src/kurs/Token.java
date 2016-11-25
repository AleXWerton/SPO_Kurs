package kurs;

public class Token
{
	private String name;
	private String value;

	public Token(String name, String value)
	{
		this.name=name;
		this.value=value;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	public String getValue()
	{
		return value;
	}
	
	
	public void setValue(String value)
	{
		this.value =value;
	}
	
	
	public String toString()
	{
		return "Token name: "+name+", "+"value: "+value;
	}
	
	
	public boolean equals(Token obj)
	{
		if(obj.getValue().equals(value)&&obj.getName().equals(name))
			return true;
		else
			return false;
	}

}
