package jp.co.hakutakusoft.PB008.swing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary
{
	Map<String, Integer>	_mapDeclare = new HashMap<String, Integer>();
	Map<String, StringBuilder>	_mapRef = new HashMap<String, StringBuilder>();
		
	public String	_s;
	public int	_line;
	
	public void	parse(String s)
	{
		_mapDeclare.clear();
		_mapRef.clear();
		_s = s;
		
		boolean	isInComment = false;
		int	lineNum = 0;
		int	colNum = 0;
		
		int	wordSts = 0;	// 0:base 1:inWord 2:endWord
		
		BufferedReader	reader = new BufferedReader(new StringReader(s));
		String	lineOrg;
		StringBuilder	wordSB = null;
		int	wordLine = 0;
		int	wordCol = 0;
		String	wordLineStr = null;
		
		try
		{
			while ((lineOrg = reader.readLine()) != null)
			{
				lineNum += 1;
				colNum = 0;
				
				// process comment
				String	line = lineOrg;
				int	posCmt;
				if (isInComment)
				{
					if ((posCmt = line.indexOf("*/")) >= 0)	
					{
						line = line.substring(0, posCmt + 2);
						colNum = posCmt + 2;
						isInComment = false;
					}
					else
					{
						continue;
					}
				}
				if ((posCmt = line.indexOf("//")) >= 0)
				{
					line = line.substring(0, posCmt);
				}
				if ((posCmt = line.indexOf("/*")) >= 0)
				{
					isInComment = true;
					line = line.substring(0, posCmt);
				}
				
				// process words
				char[]	ca = line.toCharArray();
				for (int j = 0; j < ca.length; ++j)
				{
					char	c = ca[j];
					if ('A' <= c && c <= 'z')
					{	// word
						if (wordSts == 2)
						{
							// prev word is not declare
							procToken(wordSB, false, wordLine, wordCol, wordLineStr);
							wordSts = 0;
							wordSB = null;
						}
						if (wordSts == 0)
						{
							wordSB = new StringBuilder();
							wordSts = 1;
							wordLine = lineNum;
							wordCol = colNum + j;
							wordLineStr = lineOrg;
						}
						wordSB.append(c);
					}
					else
					{	// other char
						if (wordSts == 1)
						{
							wordSts = 2;	// to end word
						}
						if (c == '\n' || c == '\n' || c == ' ' || c == '\t')
						{
							;	// continue reading
						}
						else
						{
							if (c == ':' && wordSts == 2)
							{
								// prev word is declare
								procToken(wordSB, true, wordLine, wordCol, wordLineStr);
								wordSB = null;
							}					
							else
							{
								// prev word is not declare
								procToken(wordSB, false, wordLine, wordCol, wordLineStr);
								wordSB = null;
							}
							wordSts = 0;	// to base						
						}
					}
				}			
				if (wordSts == 1)
				{
					wordSts = 2;	// to end word
				}
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		_line = lineNum;
	}		
		
		
	
	private void	procToken(StringBuilder wordSB, boolean isDeclare, int lineNum, int colNum, String line)	
	{
		final Format	f = new DecimalFormat("000");
		if (wordSB == null)
		{
			return;
		}
		String	word = wordSB.toString();
//		System.out.println(word + " " + isDeclare);
		
		if (isDeclare)
		{
			_mapDeclare.put(word, lineNum);
		}
		else
		{
			StringBuilder	refs = _mapRef.get(word);
			if (refs == null)
			{
				refs = new StringBuilder();
				_mapRef.put(word, refs);
			}			
			refs.append(f.format(lineNum));
			refs.append(":");
			refs.append(line);
			refs.append("\n");
		}
	}

//	
//	
//	public static class	Token
//	{
//		private final String	_word;
//		private final int	_pos;
//		private final int	_line;
//		private final boolean	_isDeclare;
//		
//		public Token(String word, int pos, int line, boolean isDeclare)
//		{
//			_word = word;
//			_pos = pos;
//			_line = line;
//			_isDeclare = isDeclare;
//		}
//	}
//	
//	
//	public static class	Tokenizer
//	{
//		private String	_s;
//		private int	_idx;
//		private int	_line;
//		
//		private LineCountStr	_lcs;
//	
//		public Tokenizer(String s)
//		{
//			_s = s;
//			_idx = 0;
//			_line = 0;
//			
//			_lcs = new LineCountStr(s);
//		}
//
//		public Token	nextToken()
//		{	
//			LineCountStr	s = _lcs;
//			
//			StringBuilder	word = null;
//			boolean	isDeclare = false;
//			int	pos = 0;	// 単語開始位置
//next:		for (; _idx < s.length(); ++_idx)
//			{
//				char	c = s.charAt(_idx);
//				if (c == '/')
//				{
//					char	c2 = (_idx == s.length() - 1) ? ' ' : s.charAt(_idx + 1);
//					if (c2 == '*')
//					{	// comment. skip to end comment
//						for (_idx += 2; _idx + 1 < s.length(); ++_idx)
//						{
//							c = s.charAt(_idx);
//							c2 = s.charAt(_idx + 1);
//							
//							if (c == '*' && c2 == '/')
//							{
//								_idx += 1;
//								break;
//							}
//						}						
//					}
//					else if (c2 == '/')
//					{	// line comment. skip to line end
//						for (_idx += 2; _idx < s.length(); ++_idx)
//						{
//							c = s.charAt(_idx);							
//							if (c == '\n')
//							{
//								break;
//							}
//						}					
//					}
//				}
//				
//				if ('A' <= c && c <= 'z')
//				{	// word
//					if (word == null)
//					{
//						pos = _idx;
//						word = new StringBuilder();
//					}
//					word.append(c);					
//				}			
//				else if (word != null)
//				{	// other character
//					for (;_idx < s.length(); ++_idx)
//					{
//						c = s.charAt(_idx);							
//						if (c == '\n' || c == '\n' || c == ' ' || c == '\t')
//						{
//							;
//						}
//						else
//						{
//							break;
//						}
//					}	
//					if (c == ':')
//					{
//						isDeclare = true;
//						_idx += 1;
//					}
//					break;
//				}
//			}
//			
//			if (word != null)
//			{
//				return	new Token(word.toString(), pos, isDeclare);
//			}
//			else
//			{
//				return	null;
//			}
//		}
//	}
//
//	public static class	LineCountStr
//	{
//		private final String	_s;
//		private int	_lineNum;
//		public LineCountStr(String s)
//		{
//			_s = s;
//			_lineNum = 0;
//		}
//		public char charAt(int _idx)
//		{
//			// TODO 自動生成されたメソッド・スタブ
//			return 0;
//		}
//		public int length()
//		{
//			// TODO 自動生成されたメソッド・スタブ
//			return 0;
//		}
//		
//	}

	public int getDeclarePos(String word)
	{
		Integer	pos = _mapDeclare.get(word);
		return (pos == null) ? -1 : pos.intValue();
	}


	public String getRef(String word)
	{
		return	_mapRef.get(word).toString();
	}
}
