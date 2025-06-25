package net.neological;

import net.neological.webscraping.specific.BookWebScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for fully implemented BookWebScraper methods,
 * covering single‐page, multi‐page, and genre‐wide calculations.
 */
@DisplayName("BookWebScraper Complete & Pagination Tests")
public class BookWebScraperTest {

    private BookWebScraper scraper;

    private static final String FIXTURE1         = "/genre1.html";
    private static final String FIXTURE2_PAGE1   = "/genre2.html";
    private static final String FIXTURE2_PAGE2   = "/genre2_page2.html";

    private static final String URL1             =
            "https://books.toscrape.com/catalogue/category/books/philosophy_7/index.html";
    private static final String URL2_PAGE1       =
            "https://books.toscrape.com/catalogue/category/books/historical-fiction_4/index.html";
    private static final String URL2_PAGE2       =
            "https://books.toscrape.com/catalogue/category/books/historical-fiction_4/page-2.html";

    @BeforeEach
    public void setUp() {
        scraper = new BookWebScraper("TestAgent/1.0", 5000) {
            @Override
            protected Document fetchDocument(String url) throws IOException {
                String fixture;
                if (URL1.equals(url)) {
                    fixture = FIXTURE1;
                } else if (URL2_PAGE1.equals(url)) {
                    fixture = FIXTURE2_PAGE1;
                } else if (URL2_PAGE2.equals(url)) {
                    fixture = FIXTURE2_PAGE2;
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

    // --- Philosophy (genre1.html) tests ---

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
        assertEquals(33.55818,
                scraper.averagePriceForGenre("Philosophy"),
                0.0001,
                "Expected average price 33.55818 for Philosophy");
    }

    @Test
    @DisplayName("averageRatingForGenre for Philosophy returns expected hard-coded value")
    public void testAverageRatingForGenre_Philosophy() throws IOException {
        scraper.scrape(URL1);
        assertEquals(2.36364,
                scraper.averageRatingForGenre("Philosophy"),
                0.0001,
                "Expected average rating 2.36364 for Philosophy");
    }

    // --- Historical Fiction Page 1 (genre2.html) tests ---

    @Test
    @DisplayName("countEntriesPerPage for Historical Fiction page 1 yields 20")
    public void testCountEntriesPerPage_HistoricalFiction_Page1() throws IOException {
        scraper.scrape(URL2_PAGE1);
        assertEquals(20, scraper.countEntriesPerPage());
    }

    @Test
    @DisplayName("averagePricePerPage for Historical Fiction page 1 within tolerance")
    public void testAveragePricePerPage_HistoricalFiction_Page1() throws IOException {
        scraper.scrape(URL2_PAGE1);
        double expected = 35.379;
        assertEquals(expected, scraper.averagePricePerPage(), 0.0001);
    }

    @Test
    @DisplayName("averageRatingPerPage for Historical Fiction page 1 within tolerance")
    public void testAverageRatingPerPage_HistoricalFiction_Page1() throws IOException {
        scraper.scrape(URL2_PAGE1);
        double expected = 2.95;
        assertEquals(expected, scraper.averageRatingPerPage(), 0.0001);
    }

    // --- Historical Fiction Page 2 (genre2_page2.html) tests ---

    @Test
    @DisplayName("countEntriesPerPage for Historical Fiction page 2 yields 6")
    public void testCountEntriesPerPage_HistoricalFiction_Page2() throws IOException {
        scraper.scrape(URL2_PAGE2);
        assertEquals(6, scraper.countEntriesPerPage());
    }

    @Test
    @DisplayName("averagePricePerPage for Historical Fiction page 2 within tolerance")
    public void testAveragePricePerPage_HistoricalFiction_Page2() throws IOException {
        scraper.scrape(URL2_PAGE2);
        double expected = 27.861666666666667;
        assertEquals(expected, scraper.averagePricePerPage(), 0.0001);
    }

    @Test
    @DisplayName("averageRatingPerPage for Historical Fiction page 2 within tolerance")
    public void testAverageRatingPerPage_HistoricalFiction_Page2() throws IOException {
        scraper.scrape(URL2_PAGE2);
        double expected = 4.166666666666667;
        assertEquals(expected, scraper.averageRatingPerPage(), 0.0001);
    }

    // --- Genre-wide Historical Fiction tests (both pages) ---

    @Test
    @DisplayName("averagePriceForGenre for Historical Fiction across both pages")
    public void testAveragePriceForGenre_HistoricalFiction_AcrossPages() throws IOException {
        scraper.scrape(URL2_PAGE1);
        double expectedCombinedPrice = 33.63653846153847;
        assertEquals(expectedCombinedPrice,
                scraper.averagePriceForGenre("Historical Fiction"),
                0.0001,
                "Expected combined avg price ≈33.63654 across pages for Historical Fiction");
    }

    @Test
    @DisplayName("averageRatingForGenre for Historical Fiction across both pages")
    public void testAverageRatingForGenre_HistoricalFiction_AcrossPages() throws IOException {
        scraper.scrape(URL2_PAGE1);
        double expectedCombinedRating = 3.230769230769231;
        assertEquals(expectedCombinedRating,
                scraper.averageRatingForGenre("Historical Fiction"),
                0.0001,
                "Expected combined avg rating ≈3.23077 across pages for Historical Fiction");
    }
}
