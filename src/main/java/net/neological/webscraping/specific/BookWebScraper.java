package net.neological.webscraping.specific;

import net.neological.webscraping.WebScraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        Elements bookEntries = document.select("article.product_pod");
        return bookEntries.size();
    }

    /**
     * Calculate the average price of all book entries on the current page.
     *
     * @return the average price (e.g., in USD)
     */
    public double averagePricePerPage() {
        Elements priceElements = document.select("article.product_pod p.price_color");
        if (priceElements.isEmpty()) {
            return 0.0;
        }

        double totalPrice = 0.0;
        int count = 0;

        for (Element priceElement : priceElements) {
            String priceText = priceElement.text().trim();
            // Remove the £ symbol and parse the price
            if (priceText.startsWith("£")) {
                try {
                    double price = Double.parseDouble(priceText.substring(1));
                    totalPrice += price;
                    count++;
                } catch (NumberFormatException e) {
                    // Skip invalid prices
                }
            }
        }

        return count > 0 ? totalPrice / count : 0.0;
    }

    /**
     * Calculate the average rating of all book entries on the current page.
     *
     * @return the average rating (e.g., 1.0–5.0 scale)
     */
    public double averageRatingPerPage() {
        Elements ratingElements = document.select("article.product_pod p[class*='star-rating']");
        if (ratingElements.isEmpty()) {
            return 0.0;
        }

        double totalRating = 0.0;
        int count = 0;

        for (Element ratingElement : ratingElements) {
            String ratingClass = ratingElement.className();
            double rating = convertRatingClassToNumber(ratingClass);
            if (rating > 0) {
                totalRating += rating;
                count++;
            }
        }

        return count > 0 ? totalRating / count : 0.0;
    }

    /**
     * Calculate the average price of books in the given genre across all pages.
     *
     * @param genre the name of the genre to filter by
     * @return the average price for that genre (e.g., in USD)
     */
    public double averagePriceForGenre(String genre) throws IOException {
        if (!getAllGenres().contains(genre)) {
            return 0;
        }

        double totalPrice = 0;
        Document currentPage = getFirstPageForGenre(genre);
        int totalPages = getTotalPages(currentPage);

        for (int i = 0; i < totalPages; i++) {
            totalPrice += totalPriceOnPage(currentPage);
            currentPage = getNextPage(currentPage);
        }

        return totalPrice / getTotalResultsForGenre(genre);
    }

    /**
     * Calculate the average rating of books in the given genre across all pages.
     *
     * @param genre the name of the genre to filter by
     * @return the average rating for that genre (1.0–5.0 scale)
     */
    public double averageRatingForGenre(String genre) throws IOException {
        if (!getAllGenres().contains(genre)) {
            return 0;
        }

        double totalRating = 0;
        Document currentPage = getFirstPageForGenre(genre);
        int totalPages = getTotalPages(currentPage);

        for (int i = 0; i < totalPages; i++) {
            totalRating += totalRatingOnPage(currentPage);
            currentPage = getNextPage(currentPage);
        }

        return totalRating / getTotalResultsForGenre(genre);
    }

    /**
     * Helper method to convert rating class names to numeric values.
     *
     * @param ratingClass the CSS class containing the rating
     * @return the numeric rating (1-5) or 0 if invalid
     */
    private double convertRatingClassToNumber(String ratingClass) {
        if (ratingClass.contains("One")) return 1.0;
        if (ratingClass.contains("Two")) return 2.0;
        if (ratingClass.contains("Three")) return 3.0;
        if (ratingClass.contains("Four")) return 4.0;
        if (ratingClass.contains("Five")) return 5.0;
        return 0.0;
    }

    /**
     * Helper method to get the first‐page Document for the given genre.
     * Assumes `this.document` has the sidebar.
     *
     * @param genre exact genre name as shown in the sidebar
     * @return Jsoup Document for that genre’s first page
     * @throws IOException if fetching fails
     * @throws IllegalArgumentException if the genre isn’t in the sidebar
     */
    private Document getFirstPageForGenre(String genre) throws IOException {
        // Build regex to match the exact genre text (ignoring wrapping tags)
        String regex = "^\\s*" + Pattern.quote(genre) + "\\s*$";

        // Try to find an <a> whose own text matches the genre
        Element link = document.selectFirst(
                "div.side_categories ul.nav-list > li > ul > li > a:matchesOwn(" + regex + ")"
        );

        // Fallback: scan sidebar links and compare trimmed text
        if (link == null) {
            for (Element a : document.select("div.side_categories ul.nav-list > li > ul > li > a")) {
                if (a.text().trim().equals(genre)) {
                    link = a;
                    break;
                }
            }
        }

        if (link == null) {
            throw new IllegalArgumentException("Genre not found: " + genre);
        }

        // Fetch and return the first page for that genre
        String href = link.absUrl("href");
        return fetchDocument(href);
    }

    /**
     * Helper method to extract the total number of pages from the pagination element.
     *
     * @param document a Jsoup Document representing a listing page
     * @return the total page count (e.g., “2” from “Page 1 of 2”); returns 1 if not found or parse error
     */
    private int getTotalPages(Document document) {
        Element current = document.selectFirst("li.current");
        if (current == null) {
            return 1;
        }
        String text = current.text().trim();          // e.g. "Page 1 of 2"
        String[] parts = text.split("of");
        if (parts.length < 2) {
            return 1;
        }
        try {
            return Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Returns the Jsoup Document for the next pagination page, or null if none.
     *
     * @param page the current book‐listing page
     * @return the next page’s Document, or null if this is the last page
     * @throws IOException if fetching the next page fails
     */
    private Document getNextPage(Document page) throws IOException {
        Element nextLink = page.selectFirst("li.next > a");
        return (nextLink != null)
                ? fetchDocument(nextLink.absUrl("href"))
                : null;
    }

    /**
     * Helper method to calculate the total sum of all book prices on the current page.
     *
     * @return the sum of all prices (e.g., in GBP) on this.document
     */
    private double totalPriceOnPage(Document page) {
        double total = 0.0;
        Elements priceEls = page.select("article.product_pod p.price_color");
        for (Element e : priceEls) {
            String txt = e.text().replaceAll("[^\\d.]+", "");
            try {
                total += Double.parseDouble(txt);
            } catch (NumberFormatException ignored) {
                // skip malformed entries
            }
        }
        return total;
    }

    /**
     * Helper method to calculate the total sum of all book ratings on the given page.
     *
     * @param page a Jsoup Document representing a book‐listing page
     * @return the sum of all numeric ratings (1–5) on that page
     */
    private double totalRatingOnPage(Document page) {
        double total = 0.0;
        // select each <p class="star-rating ...">
        Elements ratingEls = page.select("article.product_pod p.star-rating");
        for (Element e : ratingEls) {
            // convert the element’s class names (e.g. "star-rating Three") to a number
            total += convertRatingClassToNumber(e.className());
        }
        return total;
    }
}
