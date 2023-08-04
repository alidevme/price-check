package com.devfam.price.check.crawler.schedule;

import com.devfam.price.check.crawler.PriceCheckCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class PriceCheckScheduler {

    @Autowired
    private PriceCheckCrawler priceCheckCrawler;

    @Scheduled(cron = "0 0 12,18 * * ?") // 12:00 PM and 18:00 every day
    public void schedulePriceCheck() {
        List<String> recipients = new ArrayList<>();
        recipients.add("replace-with-your-address@email.com");
        priceCheckCrawler.checkDiscountAndNotify(recipients);
    }
}
