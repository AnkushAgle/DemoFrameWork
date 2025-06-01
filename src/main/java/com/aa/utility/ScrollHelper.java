package com.aa.utility;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ScrollHelper {
    

public static boolean scrollToElementNew(WebDriver driver, WebElement element) {
	JavascriptExecutor js = (JavascriptExecutor) driver;

	  Double finalPosition=0.000;
	
	 try {
	        Thread.sleep(200);
	

    // Get the initial position of the element relative to the viewport
    Double initialPosition = (Double) js.executeScript(
        "return arguments[0].getBoundingClientRect().top;", element);

    // Scroll the element into the middle of the viewport
    js.executeScript(
        "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
        + "var elementTop = arguments[0].getBoundingClientRect().top;"
        + "window.scrollBy(0, elementTop - (viewPortHeight / 2));", element);

    // Wait to ensure the scroll action is complete
    } catch (InterruptedException e) {
        e.printStackTrace();
    }


    try {
        Thread.sleep(200); // Wait to ensure the scroll action is complete
    
    // Get the new position of the element relative to the viewport
     finalPosition = (Double) js.executeScript(
        "return arguments[0].getBoundingClientRect().top;", element);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
	// Check if the final position is approximately in the middle of the viewport
    return finalPosition != null && Math.abs(finalPosition) < 50; // Adjust tolerance as needed
}

	public static void scrollToElementTestingMate(WebDriver driver, WebElement element) {

		
    String exceptionmate = "NO";
    JavascriptExecutor js = (JavascriptExecutor) driver;

    try {
        // Log the initial position of the element
        String initialPosition = (String) js.executeScript(
            "var rect = arguments[0].getBoundingClientRect();"
            + "return 'top: ' + rect.top + ', bottom: ' + rect.bottom;",
            element
        );
        Library.dialogErrorMessageLog("Initial Element Position: " + initialPosition);

        // Perform the scroll
        js.executeScript(
            "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
            + "var elementTop = arguments[0].getBoundingClientRect().top;"
            + "window.scrollBy(0, elementTop - (viewPortHeight / 2));",
            element
        );

        // Log the position after the scroll
        String postScrollPosition = (String) js.executeScript(
            "var rect = arguments[0].getBoundingClientRect();"
            + "return 'top: ' + rect.top + ', bottom: ' + rect.bottom;",
            element
        );
        Library.dialogErrorMessageLog("Post-Scroll Element Position: " + postScrollPosition);

        // Verify if the element is fully within the viewport
        Boolean isInViewport = (Boolean) js.executeScript(
            "var rect = arguments[0].getBoundingClientRect();"
            + "return (rect.top >= 0 && rect.bottom <= (window.innerHeight || document.documentElement.clientHeight));",
            element
        );

        if (!isInViewport) {
          //  throw new Exception("Element not successfully scrolled into view.");
        }

        // Log success
        ScrollHelper.scrollToElement(driver, element);
        Library.dialogErrorMessageLog("Element successfully scrolled into view.");
    } catch (Exception e) {
        exceptionmate = e.getMessage();
    } finally {
        if (!exceptionmate.equalsIgnoreCase("NO")) {
            Library.dialogErrorMessageLog("Error: " + exceptionmate);
        }
    }
}

    
    public static void scrollToElement(WebDriver driver, By Byelement) {
    	
    	

        try {
    		Thread.sleep(500);
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	WebElement element=driver.findElement(Byelement);
      	JavascriptExecutor js = (JavascriptExecutor) driver;

    	 js.executeScript("var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                 + "var elementTop = arguments[0].getBoundingClientRect().top;"
                 + "window.scrollBy(0, elementTop - (viewPortHeight / 2));", element);

    }
    
    
public static void Scrolldown(WebDriver driver,int Horizontalpixel, int Verticalpixel)  {
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		String xOffset = Integer.toString(Horizontalpixel);
		String yOffset = Integer.toString(Verticalpixel);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String script = "window.scrollBy(" + xOffset + "," + yOffset + ")";

		js.executeScript(script);
		
		System.out.println(script);
	}
// Utility function to get the current vertical scroll position of the window
public static int getCurrentVerticalScrollPosition(WebDriver driver) {
    // Get the current vertical scroll position using JavaScript
    return (int) (long) ((JavascriptExecutor) driver).executeScript("return window.scrollY;");
}

// Utility function to scroll the window up to the specified vertical position
public static void scrollToVerticalPosition(WebDriver driver, int targetPosition) {
    ((JavascriptExecutor) driver).executeScript("window.scrollTo(window.scrollX, " + targetPosition + ");");
}


public static void scrollToElement11032025(WebDriver driver, WebElement element) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
}

public static void scrollToElement(WebDriver driver, WebElement element) {
      
    //	Actions act=new Actions(driver);
    	
      	JavascriptExecutor js = (JavascriptExecutor) driver;

    	//act.moveToElement(element).build().perform();
    	
//        js.executeScript("arguments[0].scrollIntoView(true);", element);
//        
//        driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
//
//        // Zoom out
//        driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
//
//        // Reset zoom (100%)
//        driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));
//
//        
//        
//        
//        // Zoom in
//        // Increase the value to zoom in more (e.g., 1.25 for 125% zoom)
//        js.executeScript("document.body.style.zoom='1.25'");
//
//        // Zoom out
//        // Decrease the value to zoom out more (e.g., 0.75 for 75% zoom)
//        js.executeScript("document.body.style.zoom='0.75'");
//
//        // Reset zoom (100%)
//        js.executeScript("document.body.style.zoom='1'");
    	

//        try {
//    		Thread.sleep(100);
//    	} catch (InterruptedException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
    	
    	 // Execute JavaScript to scroll the element into the middle of the viewport
       
   	 js.executeScript("var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
             + "var elementTop = arguments[0].getBoundingClientRect().top;"
             + "window.scrollBy(0, elementTop - (viewPortHeight / 2));", element);

//  
//    try {
//		Thread.sleep(200);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		
//	}
    }
// Function to scroll down by a specified number of pixels
public static void scrollDownModalWindow(WebDriver driver, int pixels) {
    // JavaScript code to scroll the modal window by specified pixels
    String script = 
        "const modalWindow = document.querySelector('ngb-modal-window');" +
        "if (modalWindow) {" +
        "    modalWindow.scrollTop += arguments[0];" +
        "    console.log('Scrolled ngb-modal-window by ' + arguments[0] + ' pixels');" +
        "} else {" +
        "    console.log('Modal window not found');" +
        "}";

    // Execute the JavaScript code
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript(script, pixels);
}


// Function to scroll up by a specified number of pixels
public static void scrollUpModalWindow(WebDriver driver, int pixels) {
    // JavaScript code to scroll the modal window up by specified pixels
    String script = 
        "const modalWindow = document.querySelector('ngb-modal-window');" +
        "if (modalWindow) {" +
        "    modalWindow.scrollTop -= arguments[0];" +
        "    console.log('Scrolled ngb-modal-window up by ' + arguments[0] + ' pixels');" +
        "} else {" +
        "    console.log('Modal window not found');" +
        "}";

    // Execute the JavaScript code
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript(script, pixels);
}// Function to zoom in or out on the screen by a specified percentage


//Function to zoom in or out on the screen by a specified percentage
public static void zoomScreenByPercentage(WebDriver driver, double percentage) {
 // Validate the percentage input
 if (percentage <= 0) {
     throw new IllegalArgumentException("Percentage must be greater than 0");
 }

 // JavaScript code to set the zoom level based on the percentage
 String script = 
     "document.body.style.zoom = arguments[0] / 100;" +
     "console.log('Screen zoom set to ' + arguments[0] + '%');";

 // Execute the JavaScript code
 JavascriptExecutor js = (JavascriptExecutor) driver;
 js.executeScript(script, percentage);
}


}
