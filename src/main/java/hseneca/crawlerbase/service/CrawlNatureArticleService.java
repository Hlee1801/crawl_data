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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlNatureArticleService {
    @Autowired
    RecordRepository recordRepository;
    public static String DATE_FORMAT_INPUT = "dd MMM yyyy";


    @Transactional
    public void crawlNatureArticle(Integer page) throws IOException {
        String url = "https://www.nature.com/nature/research-articles?searchType=journalSearch&sort=PubDate&page=" + page;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(".app-article-list-row__item");
            if (elements.isEmpty()) {
                throw new RuntimeException();
            }

            for (Element element : elements) {


                String title = element.select(".c-card__title").text();

                String desc = element.select(".c-card__summary").text();

                List<String> authorList = new ArrayList<>();
                Element authors = element.selectFirst(".c-author-list");
                for (Element authorElement : authors.children()) {
                    authorList.add(authorElement.text());
                }

                String publishDate = element.select("time.c-meta__item").text();

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

                recordRepository.insertOrUpdate(String.join(",", authorList), desc , imageUrl, convert(publishDate), Source.NATURE.toString(), sourceId, title, url);



//                Record record = Record.builder()
//                        .title(title)
//                        .author(String.join(",", authorList))
//                        .description(desc)
//                        .publishDate(convert(publishDate))
//                        .url("https://www.nature.com" + link)
//                        .imageUrl(imageUrl)
//                        .source(Source.NATURE)
//                        .sourceId(sourceId)
//                        .build();
//                recordRepository.save(record);
//                System.out.println(Source.NATURE);


//                Record recordInDB = recordRepository.findBySourceAndSourceId(Source.NATURE, sourceId);
//                if (recordInDB == null) {
//                    Record savedRecord = recordRepository.save(record);
//                    System.out.printf("Persisted record: %s, %s%n", Source.NATURE, savedRecord.getSourceId());
//                } else {
//                    record.setCreatedAt(recordInDB.getCreatedAt());
//                    record.setId(recordInDB.getId());
//                    Record savedRecord = recordRepository.save(record);
//                    System.out.printf("Updated record: %s, %s%n", Source.NATURE, savedRecord.getSourceId());
//
//                }



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