package assignment;


import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class VerifyMotherTongue {
	
	private WebDriver driver;
	
	@BeforeClass
	void setup()
	{
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.MILLISECONDS);
		
	}
	@AfterClass
	void close()
	{
		driver.close();
	}
	@Test(dataProvider="dataProvider")
	void login(String[] data) throws InterruptedException
	{
		
		String username = data[0];
		String password = data[1];
		String url = data[2];
		String motherTongue = data[3];
		driver.get(url);
		driver.findElement(By.className("buttonWrap")).click();
		WebElement emailelement = driver.findElement(By.name("email"));
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.MILLISECONDS);
		emailelement.sendKeys(username);
		
		WebElement passwordelement = driver.findElement(By.name("password1"));
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.MILLISECONDS);
		passwordelement.sendKeys(password);
		
		
		WebElement element = driver.findElement(By.className("Dropdown-placeholder")); 
		element.click();
		Actions action = new Actions(driver);
		action.moveToElement(element).moveToElement(driver.findElement(By.xpath("//div[contains(text(),'Brother')]"))).click().build().perform();
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-md btn-block']")).click();
		
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.MILLISECONDS);
		WebDriverWait wait2 = new WebDriverWait(driver,10);
		wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[2]/div[1]/div[7]/form[1]/div[5]/div[1]/div[1]/div[1]/div[1]")));
		
		WebElement element1 = driver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[1]/div[7]/form[1]/div[5]/div[1]/div[1]/div[1]/div[1]"));
		
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.MILLISECONDS);
		
		String actual_defaultlanguage = element1.getText();
		String expexcted_defaultlanguage = motherTongue;
		Assert.assertEquals(expexcted_defaultlanguage,actual_defaultlanguage);
		
	}
	@DataProvider(name="dataProvider")
	public String[][] readJson() throws IOException, ParseException
	{
		JSONParser jsonParser = new JSONParser();
		FileReader filereader = new FileReader("data.json");
		
		Object object = jsonParser.parse(filereader);
		JSONObject userloginJsonObject = (JSONObject) object;
		JSONArray userloginsarray = (JSONArray)userloginJsonObject.get("userlogins");
		String arr[][]=new String[userloginsarray.size()][4];
		
		for(int i=0;i<userloginsarray.size();i++)
		{
			String[] data = new String[4];
			JSONObject users =(JSONObject) userloginsarray.get(i);
			String username = (String) users.get("username");
			String password = (String) users.get("password");
			String url = (String) users.get("url");
			String motherTongue = (String) users.get("motherTongue");
			data[0]=username;
			data[1]=password;
			data[2]=url;
			data[3]=motherTongue;
			
			arr[i] = data;
			
		}
		
		return arr;
	}

}
