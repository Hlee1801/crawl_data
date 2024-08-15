package hseneca.crawlerbase.service;

import hseneca.crawlerbase.entity.Source;
import hseneca.crawlerbase.repository.RecordRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZonedDateTime;

import static hseneca.crawlerbase.utils.DateTimeUtils.convert;

@Service
public class CrawlNatureCareersService {

    @Autowired
    private RecordRepository recordRepository;

    @Transactional
    public void crawlNatureCareers(Integer page) throws IllegalAccessError, IOException {
        String url = "https://www.nature.com/careers?page=" + page;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(".c-article-list__item");

            if(elements.isEmpty()){
                throw new RuntimeException();
            }

            for (Element element : elements) {

                String title =element.selectFirst(".c-article-item__title").text();

                String typeOfArticle = element.selectFirst(".c-article-item__article-type").text();

                String link = null;
                String sourceId = null;
                Element urlElement = element.select(".c-article-item__content a").first();
                if (urlElement != null) {
                    link = urlElement.attr("href");
                    String[] urlSplit = link.split("/");
                    sourceId = urlSplit[urlSplit.length - 1];
                }

                String urlOfArticle = "https://www.nature.com" + link;

                String imageUrl = null;
                Element image = element.select("img").first();
                if (image != null) {
                    imageUrl = image.attr("src");
                }

                //Go inside Page
                Document docInside = Jsoup.connect(urlOfArticle).get();

                String publishDate = null;
                Element timeElement = docInside.selectFirst("ul.c-article-identifiers li.c-article-identifiers__item time");
                if (timeElement != null) {
                    publishDate = timeElement.attr("datetime");
                }

                Element authorElements = docInside.selectFirst("a[data-test=author-name]");
                String author = null;
                if (authorElements != null) {
                    author = authorElements.text();
                }else {
                    author = "NOT FOUND";
                }

                Elements descriptionElements = docInside.select(".c-article-teaser-text");
                String desc = null;
                if (descriptionElements != null) {
                    desc = descriptionElements.text();
                }else {
                    desc = null;
                }
                recordRepository.insertOrUpdate(author,desc,imageUrl,convert(publishDate),Source.NATURE,sourceId,title,urlOfArticle, typeOfArticle, ZonedDateTime.now(), ZonedDateTime.now());
                }

        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
