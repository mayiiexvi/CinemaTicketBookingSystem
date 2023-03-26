package common;

import java.util.Scanner;

public class DataValidation {
	public static double readPositiveDouble(String prompt) {
		Scanner keyboard = new Scanner(System.in);
		Double num = 0.0;
		boolean isValid = false;
		
		while(!isValid) {
			try {
				System.out.print(prompt);
				String priceString = keyboard.next();
				num = Double.parseDouble(priceString);
	    		if (num <= 0) {
	    			System.out.println("Please enter a number greater than 0.");
					isValid = false;
	    		} else {
	    			isValid = true;
	    		}
			} catch (Exception e) {
				System.out.println("Invalid input. Please try again.");
				isValid = false;
			}
		}
		return num;
	}
	
	public static int readPositiveInt(String prompt) {
		Scanner keyboard = new Scanner(System.in);
		int num = 0;
		boolean isValid = false;
		
		while(!isValid) {
			try {
				System.out.print(prompt);
				String numString = keyboard.next();
	    		num = Integer.parseInt(numString);
	    		if (num <= 0) {
	    			System.out.println("Please enter a number greater than 0.");
					isValid = false;
	    		} else {
	    			isValid = true;
	    		}
			} catch (Exception e) {
				System.out.println("Invalid input. Please try again.");
				isValid = false;
			}
		}
		return num;
	}
    
	public static String inputStringNotEmpty(String prompt) {
		Scanner keyboard = new Scanner(System.in);
		boolean isValid = false;
		String result = "";
		while(!isValid) {
			System.out.print(prompt);
			result = keyboard.nextLine();
    		if (result.isEmpty()) {
    			System.out.println("This field is manatory!");
    		} else {
    			isValid = true;
    		}
		}
		return result;
	}
}
