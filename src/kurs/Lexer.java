package kurs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	
	
	List<Token> tokens = new ArrayList<Token>();
	Map<String, Pattern> keyWords = new HashMap<String, Pattern>(); //Создание хэш_таблицы ключевых слов
	Map<String, Pattern> terminals = new HashMap<String, Pattern>();//Создание хэш_таблицы терминалов
	String accum="";
	String currentLucky = null;
	int i;
	
	
	public static final String PL = "PL";
	public static final String MN = "MN";
	public static final String DL = "DL";
	public static final String UMN = "UMN";
	public static final String ASSIGN_OP = "ASSIGN_OP";
	public static final String VAR = "VAR";
	public static final String DIGIT = "DIGIT";
	public static final String WS = "WS";
	public static final String MORE = "MORE";
	public static final String LESS = "LESS";
	public static final String MORE_EQ = "MORE_EQ";
	public static final String LESS_EQ = "LESS_EQ";
	public static final String MARK = "MARK";
	public static final String KAV = "KAV";
	public static final String STR_KV = "STR_KV";
	public static final String JF = "JF";
	public static final String JMP = "JMP";
	public static final String JNF = "JNF";
	
	

	Pattern struct_kw = Pattern.compile("^struct$");
	Pattern dot = Pattern.compile("^[.]$");
	Pattern sm = Pattern.compile("^;$");
	Pattern while_kw = Pattern.compile("^while$");
	Pattern do_kw = Pattern.compile("^do$");
	Pattern more = Pattern.compile("^[>]$");
	Pattern less = Pattern.compile("^<$");
	Pattern more_equally = Pattern.compile("^>=$");
	Pattern less_equally = Pattern.compile("^<=$");
	Pattern figur_br_op = Pattern.compile("^[{]$");
	Pattern figur_br_cl = Pattern.compile("^[}]$");
	Pattern varKeyWordPattern = Pattern.compile("^var$");
	Pattern assign_op = Pattern.compile("^=$");
	Pattern plus_op = Pattern.compile("^[+]$");
	Pattern minus_op = Pattern.compile("^[-]$");
	Pattern del_op = Pattern.compile("^[/]$");
	Pattern umn_op = Pattern.compile("^[*]$"); 
	Pattern brk_op = Pattern.compile("^[(]$");
	Pattern brc_cl = Pattern.compile("^[)]$");
	Pattern digit = Pattern.compile("^0|[1-9]{1}[0-9]*$");
	Pattern var = Pattern.compile("^[a-zA-Z]+$*");
	Pattern ws = Pattern.compile("^\\s*$");
	
	
	public Lexer()
	{
		keyWords.put("STRUCT_KW", struct_kw);
		keyWords.put("VAR_KW", varKeyWordPattern);
		keyWords.put("WHILE_KW", while_kw);
		keyWords.put("DO_KW", do_kw);
		
		
		terminals.put("DOT", dot);
		terminals.put("MORE", more);
		terminals.put("LESS", less);
		terminals.put("MORE_EQ", more_equally);
		terminals.put("LESS_EQ", less_equally);
		terminals.put("FBR_OP", figur_br_op);
		terminals.put("FBR_CL", figur_br_cl);
		terminals.put("SM", sm);
		terminals.put("ASSIGN_OP", assign_op);
		terminals.put("DIGIT", digit);
		terminals.put("VAR", var);
		terminals.put("WS", ws);
		terminals.put("PL", plus_op);
		terminals.put("MN", minus_op);
		terminals.put("DL", del_op);
		terminals.put("UMN", umn_op);
		terminals.put("BR_OP", brk_op);
		terminals.put("BR_CL", brc_cl);
	}

	
	
	
	
	public void processInput(String filename) throws IOException 
	{
		File file = new File(filename);  // чтение из файла строк
		Reader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		
		while((line = bufferedReader.readLine()) != null)
		{
			processLine(line);	
		}
		System.out.println("TOKEN("+currentLucky+") recognized with value:"+ accum);
		tokens.add(new Token(currentLucky, accum));
		
		for(Token token: tokens)
		{
			System.out.println(token);
		}
		bufferedReader.close();
	}
	
	
	
	private void processLine(String line)
	{
		for(i=0; i<line.length(); i++)
		{
			accum = accum + line.charAt(i);
			processAccum();
		}
	}

	private void processAccum()
	{
		boolean found = false;
		
		for(String regExpName: terminals.keySet()) // получение ключей из хеш_таблицы терминалов
		{
			Pattern currentPattern = terminals.get(regExpName); // получение значения ключа 
			Matcher m = currentPattern.matcher(accum);  // считанные символы сравниваются со значением ключа пол. из таблицы
			
			if(m.matches())// в случае совпадения
			{ 
				currentLucky = regExpName; // записать ключ 
				found=true; // верное значение, считан удачно
			}
			else{}
		}
		
		if(currentLucky!=null&&!found)
		{
			System.out.println("TOKEN("+currentLucky+") recognized with value:"+ accum.substring(0, accum.length()-1));
			tokens.add(new Token(currentLucky, accum.substring(0, accum.length()-1)));
			i--;
			accum="";
			currentLucky = null;
		}
		
		for(String regExpName: keyWords.keySet()) // получение ключей из хеш_таблицы ключевых слов
		{
			Pattern currentPattern = keyWords.get(regExpName); // получение значения ключа 
			Matcher m = currentPattern.matcher(accum);  // считанные символы сравниваются со значением ключа пол. из таблицы
			
			if(m.matches()) // в случае совпадения
			{ 
				currentLucky = regExpName; // записать ключ 
				found=true; // верное значение, считан удачно
			}
			else{}
		}
		
		if(currentLucky!=null&&!found)
		{
			System.out.println("TOKEN("+currentLucky+") recognized with value:"+ accum.substring(0, accum.length()-1));
			tokens.add(new Token(currentLucky, accum.substring(0, accum.length()-1)));
			i--;
			accum="";
			currentLucky = null;
		}
	}
	
	public List<Token> getTokens()
	{
		return tokens;
	}
}