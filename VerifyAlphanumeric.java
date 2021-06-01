import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyAlphanumeric {
	
	public static void main(String[] args)
	{
		String in1 = "Dixon M\\";
		
		// String to be scanned to find the pattern.
		String pattern = "([A-Za-z0-9 ]*)";
		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(in1);
		if (m.find( ) ) {
			String in2 = m.group(0);
			System.out.println("Found value: " + in2 );
			if(in1.length() != in2.length() )
				System.out.println("Non-alphanumeric characters found.");
		}else {
			System.out.println("NO MATCH");
		}

	}

/*
	public String verifyAlphanumeric(String input) {
		// String to be scanned to find the pattern.
		String pattern = "([A-Za-z0-9 ]*)([A-Za-z0-9 ]*)([A-Za-z0-9 ]*)";
		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(line);
		if (m.find( )) {
				System.out.println("Found value: " + m.group(0) + m.group(1) + m.group(2) );
		}else {
			System.out.println("NO MATCH");
		}
	}
*/
}

