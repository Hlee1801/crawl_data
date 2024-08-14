package hseneca.crawlerbase.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class CrawlNatureCareersService {

    public void crawlNatureCareers(Integer page) throws IllegalAccessError, IOException {
        String url = "https://www.nature.com/careers?page=" + page;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(".c-article-list");

            if(elements.isEmpty()){
                throw new RuntimeException();
            }

            for (Element element : elements) {

                String title =element.select(".c-article-item__title").text();
//                System.out.println(title);
                String typeOfArticle = element.select(".c-article-item__article-type").text();
//                System.out.println(typeOfArticle);

                String publishDate = null;
                Element dateElement = element.selectFirst(".c-article-item__date");
                if (dateElement != null) {
                    publishDate = dateElement.text();
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

            }

        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
