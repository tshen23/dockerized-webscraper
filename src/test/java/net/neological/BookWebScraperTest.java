package net.neological;

import net.neological.webscraping.specific.BookWebScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for fully implemented BookWebScraper methods,
 * using local HTML fixtures.
 */
@DisplayName("BookWebScraper Complete Methods Tests")
public class BookWebScraperTest {

    private BookWebScraper scraper;

    private static final String FIXTURE1 = "/genre1.html";
    private static final String FIXTURE2 = "/genre2.html";

    private static final String URL1 =
            "https://books.toscrape.com/catalogue/category/books/philosophy_7/index.html";
    private static final String URL2 =
            "https://books.toscrape.com/catalogue/category/books/historical-fiction_4/index.html";

    @BeforeEach
    public void setUp() {
        scraper = new BookWebScraper("TestAgent/1.0", 5000) {
            @Override
            protected Document fetchDocument(String url) throws IOException {
                String fixture = null;
                if (URL1.equals(url)) {
                    fixture = FIXTURE1;
                } else if (URL2.equals(url)) {
                    fixture = FIXTURE2;
                } else {
                    throw new IOException("Unexpected URL in test: " + url);
                }
                try (InputStream in = getClass().getResourceAsStream(fixture)) {
                    if (in == null) {
                        throw new IOException("Could not load fixture: " + fixture);
                    }
                    return Jsoup.parse(in, "UTF-8", url);
                }
            }
        };
    }

    @Test
    @DisplayName("countEntriesPerPage for Philosophy yields 11")
    public void testCountEntriesPerPage_Philosophy() throws IOException {
        scraper.scrape(URL1);
        assertEquals(11, scraper.countEntriesPerPage());
    }

    @Test
    @DisplayName("averagePricePerPage for Philosophy within tolerance")
    public void testAveragePricePerPage_Philosophy() throws IOException {
        scraper.scrape(URL1);
        double expected = 33.558181818181815;
        assertEquals(expected, scraper.averagePricePerPage(), 0.0001);
    }

    @Test
    @DisplayName("averageRatingPerPage for Philosophy within tolerance")
    public void testAverageRatingPerPage_Philosophy() throws IOException {
        scraper.scrape(URL1);
        double expected = 2.3636363636363638;
        assertEquals(expected, scraper.averageRatingPerPage(), 0.0001);
    }

    @Test
    @DisplayName("averagePriceForGenre for Philosophy returns expected hard-coded value")
    public void testAveragePriceForGenre_Philosophy() throws IOException {
        scraper.scrape(URL1);
        assertEquals(33.55818, scraper.averagePriceForGenre("Philosophy"), 0.0001,
                "Expected average price 33.55818 for Philosophy");
    }

    @Test
    @DisplayName("averageRatingForGenre for Philosophy returns expected hard-coded value")
    public void testAverageRatingForGenre_Philosophy() throws IOException {
        scraper.scrape(URL1);
        assertEquals(2.36364, scraper.averageRatingForGenre("Philosophy"), 0.0001,
                "Expected average rating 2.36364 for Philosophy");
    }

    @Test
    @DisplayName("countEntriesPerPage for Historical Fiction yields 20")
    public void testCountEntriesPerPage_HistoricalFiction() throws IOException {
        scraper.scrape(URL2);
        assertEquals(20, scraper.countEntriesPerPage());
    }

    @Test
    @DisplayName("averagePricePerPage for Historical Fiction within tolerance")
    public void testAveragePricePerPage_HistoricalFiction() throws IOException {
        scraper.scrape(URL2);
        double expected = 35.379;
        assertEquals(expected, scraper.averagePricePerPage(), 0.0001);
    }

    @Test
    @DisplayName("averageRatingPerPage for Historical Fiction within tolerance")
    public void testAverageRatingPerPage_HistoricalFiction() throws IOException {
        scraper.scrape(URL2);
        double expected = 2.95;
        assertEquals(expected, scraper.averageRatingPerPage(), 0.0001);
    }

    @Test
    @DisplayName("averagePriceForGenre for Historical Fiction matches page average")
    public void testAveragePriceForGenre_HistoricalFiction() throws IOException {
        scraper.scrape(URL2);
        double pageAvg = scraper.averagePricePerPage();
        assertEquals(pageAvg, scraper.averagePriceForGenre("Historical Fiction"), 0.0001);
    }

    @Test
    @DisplayName("averageRatingForGenre for Historical Fiction matches page average")
    public void testAverageRatingForGenre_HistoricalFiction() throws IOException {
        scraper.scrape(URL2);
        double pageAvg = scraper.averageRatingPerPage();
        assertEquals(pageAvg, scraper.averageRatingForGenre("Historical Fiction"), 0.0001);
    }
}
