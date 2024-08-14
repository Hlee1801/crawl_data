package hseneca.crawlerbase.consumer;

import hseneca.crawlerbase.service.CrawlNatureArticleService;
import hseneca.crawlerbase.service.CrawlNatureCareersService;
import hseneca.crawlerbase.service.CrawlNaturePodcastService;
import hseneca.crawlerbase.service.CrawlNatureVideoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQConsumer { 

    @Autowired
    private CrawlNatureArticleService crawlNatureArticleService;
    @Autowired
    private CrawlNatureVideoService crawlNatureVideoService;
    @Autowired
    private CrawlNaturePodcastService crawlNaturePodcastService;
    @Autowired
    private CrawlNatureCareersService crawlNatureCareersService;

    @RabbitListener(queues = "research-articles")
    public void receiveMessageForArticle(String page) throws IOException {
        try {
            crawlNatureArticleService.crawlNatureArticle(Integer.parseInt(page));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "videos")
    public void receiveMessageVideo(String page) throws IOException {
        try {
            crawlNatureVideoService.crawlNatureVideo(Integer.parseInt(page));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "podcasts")
    public void receiveMessagePodcast(String page) throws IOException {
        try {
            crawlNaturePodcastService.crawlNaturePodcast(Integer.parseInt(page));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "careers")
    public void receiveMessageCareer(String page) throws IOException {
        try {
            crawlNatureCareersService.crawlNatureCareers(Integer.parseInt(page));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}