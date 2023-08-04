package com.devfam.price.check.crawler;

import com.devfam.price.check.crawler.domain.service.EmailSenderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PriceCheckCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceCheckCrawler.class);

    @Autowired
    private EmailSenderService emailSenderService;

    public void checkDiscountAndNotify(List<String> recipients) {
        String productUrl = "https://www.emag.ro/tastatura-apple-magic-pentru-ipad-pro-11-2020-layout-int-en-black-mxqt2z-a/pd/D4G51BMBM/?ref=fav_pd-title";
        double desiredPrice = 1700.00;

        List<String> decimalCharacters = new ArrayList<>();
        decimalCharacters.add(".");
        decimalCharacters.add(",");

        List<String> replaceWith = new ArrayList<>();
        replaceWith.add("");
        replaceWith.add(".");

        try {
            // Connect to the course URL and fetch the HTML content
            Document doc = Jsoup.connect(productUrl).get();

            // Extract the price and discount elements from the HTML
            Element priceElement = doc.selectFirst(".product-new-price");

            // Extract the price value as a string and preprocess it
            String priceString = priceElement.text().replaceAll("[^0-9.,]", "");

            priceString = priceString.replace(decimalCharacters.get(0), replaceWith.get(0)); // Remove thousands separators (commas)
            priceString = priceString.replace(decimalCharacters.get(1), replaceWith.get(1)); // Replace the decimal separator with a period
            double price = Double.parseDouble(priceString);

            if (price < desiredPrice) {
                sendEmailNotification(recipients, productUrl, price);
            }
        } catch (IOException e) {
            LOGGER.error("Error fetching product data: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            LOGGER.error("Error parsing price: " + e.getMessage(), e);
        }
    }

    private void sendEmailNotification(List<String> recipients, String productUrl, double price) {
        String subject = "eMAG Product Price Alert";
        String body = "The product at URL: " + productUrl + " is on sale for only " + price + " RON.";

        for (String toEmail : recipients) {
            emailSenderService.send(toEmail, subject, body);
        }
    }
}
