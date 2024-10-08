package hseneca.crawlerbase.service;


import hseneca.crawlerbase.entity.Source;
import hseneca.crawlerbase.repository.RecordRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZonedDateTime;

import static hseneca.crawlerbase.utils.DateTimeUtils.convert;

@Service
public class CrawlNatureVideoService {
    @Autowired
    private RecordRepository recordRepository;

    @Transactional
    public void crawlNatureVideo(Integer page) throws IOException {
        String url = "https://www.nature.com/nature/videos?searchType=journalSearch&sort=PubDate&type=nature-video&page=" + page;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(".app-article-list-row__item");
            if (elements.isEmpty()) {
                throw new RuntimeException();
            }

            for (Element element : elements) {

                String title = element.select(".c-card__title").text();

                String desc = element.select(".c-card__summary").text();

                String authors = element.select(".c-author-list").text();

                Elements typeOfArticleElement = element.select("span[data-test=article.type]");
                String typeOfArticle = typeOfArticleElement.text();

                String publishDate = null;
                Element timeElement = element.selectFirst("time.c-meta__item");
                if (timeElement != null) {
                    publishDate = timeElement.attr("datetime");  // Extract the datetime attribute
                }

                String link = null;
                String sourceId = null;
                Element urlElement = element.select("a.c-card__link").first();
                if (urlElement != null) {
                    link = urlElement.attr("href");
                    String[] urlSplit = link.split("/");
                    sourceId = urlSplit[urlSplit.length - 1];
                }

                String imageUrl = null;
                Element image = element.select("img").first();
                if (image != null) {
                    imageUrl = image.attr("src");
                }

                recordRepository.insertOrUpdate(authors, desc , imageUrl, convert(publishDate), Source.NATURE, sourceId, title, url, typeOfArticle, ZonedDateTime.now(), ZonedDateTime.now());

            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

}
