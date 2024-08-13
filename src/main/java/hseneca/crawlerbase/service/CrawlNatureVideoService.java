package hseneca.crawlerbase.service;


import hseneca.crawlerbase.entity.Record;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CrawlNatureVideoService {
    @Autowired
    RecordRepository recordRepository;
    public static String DATE_FORMAT_INPUT = "yyyy-MM-dd";


    @Transactional
    public void crawlNatureArticle(Integer page) throws IOException {
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

                System.out.println(authors);
//                String publishDates = null;
//                String publishDate = element.selectFirst("time.c-meta__item");
//                if (publishDate != null) {
//                    publishDates = publishDate.attr("datetime");
//                }
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

                Record record = Record.builder()
                        .title(title)
                        .author(authors)
                        .description(desc)
                        .publishDate(convert(publishDate))
                        .url("https://www.nature.com" + link)
                        .imageUrl(imageUrl)
                        .source(Source.NATURE)
                        .sourceId(sourceId)
                        .build();

                recordRepository.save(record);
//                recordRepository.insertOrUpdate(authors, desc , imageUrl, convert(publishDate), Source.NATURE.toString(), sourceId, title, url);

            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static LocalDate convert(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_INPUT);
        return LocalDate.parse(dateStr, inputFormatter);
    }

}