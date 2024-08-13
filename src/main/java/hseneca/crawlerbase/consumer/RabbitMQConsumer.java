package hseneca.crawlerbase.consumer;

import hseneca.crawlerbase.service.CrawlNatureArticleService;
import hseneca.crawlerbase.service.CrawlNatureVideoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQConsumer { 

    @Autowired
    CrawlNatureArticleService crawlNatureArticleServiceService;
    @Autowired
    private CrawlNatureVideoService crawlNatureVideoService;

//    @RabbitListener(queues = "nature-analysis")
//    public void receiveMessage(String page) throws IOException {
////        System.out.println();
//        try {
//            crawlNatureArticleServiceService.crawlNatureArticle(Integer.parseInt(page));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @RabbitListener(queues = "nature-analysis")
    public void receiveMessage(String page) throws IOException {
//        System.out.println();
        try {
            crawlNatureVideoService.crawlNatureArticle(Integer.parseInt(page));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}