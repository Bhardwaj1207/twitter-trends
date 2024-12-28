package com.TwitterTrending.Twitter_trends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.List;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;



@SpringBootApplication
public class TwitterTrendsScraper {

	public static void main(String[] args) {
		SpringApplication.run(TwitterTrendsScraper.class, args);
		// MongoDB setup
//		MongoClient mongoClient = new MongoClient("localhost", 27017);
//		MongoDatabase database = mongoClient.getDatabase("twitter_trends");
//		MongoCollection<Document> collection = database.getCollection("trending_topics");

		// Selenium WebDriver setup
		//options.addArguments("--proxy-server=http://USERNAME:PASSWORD@proxymesh.com:31280"); // Replace with ProxyMesh credentials
		WebDriver driver = new ChromeDriver();

		try {
			// Login to Twitter
			driver.get("https://x.com/home");
			Thread.sleep(5000);
			WebElement signIn = driver.findElement(By.xpath("//a[@href='/login' and @role='link' and contains(@class, 'css-175oi2r') and @data-testid='loginButton']"));
			WebElement username = driver.findElement(By.xpath("//input[@autocomplete = 'username']"));
			WebElement signInPageNextButton = driver.findElement(By.xpath("//span[text() = 'Next']"));
			WebElement password = driver.findElement(By.xpath("//input[@name = 'password']"));
			WebElement loginButton = driver.findElement(By.xpath("//span[text() = 'Log in']"));
			signIn.click();
			Thread.sleep(2000);
			username.sendKeys("your-username"); // Replace with your Twitter username
			Thread.sleep(2000);
			signInPageNextButton.click();
			Thread.sleep(20000);
			password.sendKeys("your-password"); // Replace with your Twitter password
			Thread.sleep(2000);
			loginButton.click();

			Thread.sleep(5000); // Wait for login to complete

			// Navigate to Twitter home
			driver.get("https://x.com/home");
			Thread.sleep(5000); // Wait for page to load

			// Fetch "What’s Happening" trending topics
			List<WebElement> trendingElements = driver.findElements(By.xpath("//section[contains(@aria-labelledby, 'accessible-list')]/div//span"));
			String[] trends = new String[5];
			int index = 0;
			for (WebElement element : trendingElements) {
				if (!element.getText().isEmpty() && index < 5) {
					trends[index++] = element.getText();
				}
			}

			// Fetch IP address used
			driver.get("https://api.myip.com");
			Thread.sleep(3000);
			String json = driver.findElement(By.tagName("body")).getText();
			String ipAddress = json.split("\"ip\":\"")[1].split("\"")[0];

			// Generate unique ID and timestamp
			String uniqueId = UUID.randomUUID().toString();
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

			// Store in MongoDB
			Document record = new Document("_id", uniqueId).append("trend1", trends[0]).append("trend2", trends[1]).append("trend3", trends[2]).append("trend4", trends[3]).append("trend5", trends[4]).append("timestamp", timestamp).append("ip_address", ipAddress);

			//collection.insertOne(record);

			System.out.println("Record inserted: " + record.toJson());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
			//mongoClient.close();
		}
	}
}
