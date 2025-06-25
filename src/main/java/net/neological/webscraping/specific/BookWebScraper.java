package net.neological.webscraping.specific;

import net.neological.webscraping.WebScraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookWebScraper extends WebScraper {
    /**
     * Constructor.
     *
     * @param userAgent     the User-Agent header to present when fetching pages.
     * @param timeoutMillis the timeout (in milliseconds) for page loading in Selenium.
     */
    public BookWebScraper(String userAgent, int timeoutMillis) {
        super(userAgent, timeoutMillis);
    }

    @Override
    public boolean isValid(String url) {
        return url != null
                && url.startsWith("https://books.toscrape.com/");
    }

    /**
     * Returns all available genres from the sidebar menu on the current page.
     *
     * @return a List of genre names
     */
    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        Elements genreLinks = document.select(
                "div.side_categories ul.nav-list > li > ul > li > a"
        );
        for (Element link : genreLinks) {
            genres.add(link.text().trim());
        }
        return genres;
    }

    /**
     * Returns total number of results for the specified genre.
     *
     * @param genre the genre name to look up (case-sensitive match against sidebar text)
     * @return total count of books in that genre
     * @throws IOException if fetching the genre page fails
     * @throws IllegalArgumentException if the genre is not found in the sidebar
     */
    public int getTotalResultsForGenre(String genre) throws IOException {
        // Find the sidebar link matching exactly the given genre name
        Elements links = document.select("div.side_categories ul.nav-list > li > ul > li > a");
        Element match = null;
        for (Element link : links) {
            if (link.text().trim().equals(genre)) {
                match = link;
                break;
            }
        }
        if (match == null) {
            throw new IllegalArgumentException("Genre not found: " + genre);
        }

        // Fetch the genre page
        String genreUrl = match.absUrl("href");
        Document genreDoc = fetchDocument(genreUrl);

        // Parse the "Showing X results" count
        Element countElem = genreDoc.selectFirst("form.form-horizontal strong");
        if (countElem != null) {
            try {
                return Integer.parseInt(countElem.text().trim());
            } catch (NumberFormatException e) {
                // fall through to return 0
            }
        }
        return 0;
    }

    /**
     * Count the number of book entries on the current page.
     *
     * @return the count of entries
     */
    public int countEntriesPerPage() {
        // TODO: implement logic to count entries in the parsed Document
        return 0;
    }

    /**
     * Calculate the average price of all book entries on the current page.
     *
     * @return the average price (e.g., in USD)
     */
    public double averagePricePerPage() {
        // TODO: implement logic to compute average price from the parsed Document
        return 0.0;
    }

    /**
     * Calculate the average rating of all book entries on the current page.
     *
     * @return the average rating (e.g., 1.0–5.0 scale)
     */
    public double averageRatingPerPage() {
        // TODO: implement logic to compute average rating from the parsed Document
        return 0.0;
    }

    /**
     * Calculate the average price of books in the given genre on the current page.
     *
     * @param genre the name of the genre to filter by
     * @return the average price for that genre (e.g., in US)
     */
    public double averagePriceForGenre(String genre) {
        // TODO: implement logic to filter entries by genre and compute average price
        return 0.0;
    }

    /**
     * Calculate the average rating of books in the given genre on the current page.
     *
     * @param genre the name of the genre to filter by
     * @return the average rating for that genre (e.g., on a 1.0–5.0 scale)
     */
    public double averageRatingForGenre(String genre) {
        // TODO: implement logic to filter entries by genre and compute average rating
        return 0.0;
    }

}
