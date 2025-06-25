package net.neological.webscraping;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.time.Duration;

/**
 * Abstract base class that uses Selenium (with WebDriverManager) to load the page
 * (allowing any JavaScript to execute), then hands the fully rendered HTML to Jsoup
 * for parsing. Subclasses must implement parse(...) and isValid(...).
 */
public abstract class WebScraper {
    protected final String userAgent;
    protected final int timeoutMillis;
    protected Document document;

    /**
     * Constructor.
     *
     * @param userAgent     the User-Agent header to present when fetching pages.
     * @param timeoutMillis the timeout (in milliseconds) for page loading in Selenium.
     */
    protected WebScraper(String userAgent, int timeoutMillis) {
        this.userAgent = userAgent;
        this.timeoutMillis = timeoutMillis;
    }

    /**
     * Fetches the fully rendered HTML at the given URL via Selenium.
     *
     * @param url the full URL of the page to scrape.
     * @throws IOException if there is a problem launching ChromeDriver or parsing the response.
     */
    public void scrape(String url) throws IOException {
        if (!isValid(url)) {
            throw new IllegalArgumentException("URL failed isValid() check: " + url);
        }

        document = fetchDocument(url);
    }

    /**
     * Uses WebDriverManager to set up ChromeDriver, launches headless Chrome with the specified User-Agent,
     * navigates to the URL, waits up to {@code timeoutMillis} for the page to load, then grabs the page source
     * and parses it via Jsoup.
     *
     * @param url the URL to fetch and render.
     * @return a Jsoup Document representing the fully rendered page.
     * @throws IOException if Selenium fails or Jsoup cannot parse the HTML.
     */
    protected Document fetchDocument(String url) throws IOException {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--blink-settings=imagesEnabled=false"); // turn off image loading
        options.addArguments("--user-agent=" + userAgent);

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(timeoutMillis));

            driver.get(url);

            String html = driver.getPageSource();

            return Jsoup.parse(html, url);
        } catch (Exception e) {
            throw new IOException("Failed to fetch/render page via Selenium: " + e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }

    /**
     * Check if the URL format is valid for this scraper.
     * Subclasses should implement their own logic (e.g., matching a URL prefix or regex).
     *
     * @param url the URL to validate.
     * @return true if acceptable, false otherwise.
     */
    public abstract boolean isValid(String url);
}
