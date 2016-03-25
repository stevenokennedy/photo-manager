package photoman.service.nef.lookup;

import java.util.HashMap;
import java.util.Map;

public class Lookup 
{
	public static final String UNKNOWN = "Unknown_"; 
	
	public Map<Integer, String> ENTRY = new HashMap<>();
	
	public Map<Integer, String> getEntries()
	{
		return ENTRY;
	}
	
	public String get(Integer code)
	{
		return ENTRY.get(code);
	}
}
