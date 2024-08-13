package hseneca.crawlerbase.repository;

import hseneca.crawlerbase.entity.Record;
import hseneca.crawlerbase.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Record findBySourceAndSourceId(Source source, String sourceId);

    @Modifying
    @Query(value = "INSERT INTO records (author, description, image_url, publish_date, source, source_id, title, url) " +
            "VALUES (:author, :description , :imageUrl, :publishDate, :source, :sourceId, :title, :url ) "+
            "ON CONFLICT (source, source_id) DO UPDATE SET" +
            " author = EXCLUDED.author,"+
             "    description = EXCLUDED.description, " +
             "    image_url = EXCLUDED.image_url, " +
             "    publish_date = EXCLUDED.publish_date, " +
             "    title = EXCLUDED.title, " +
             "    url = EXCLUDED.url", nativeQuery = true)
//    "SET content = EXCLUDED.content", nativeQuery = true)
    void insertOrUpdate(
            @Param("author") String author,
            @Param("description") String description,
            @Param("imageUrl") String imageUrl,
            @Param("publishDate") LocalDate publishDate,
            @Param("source") String source,
            @Param("sourceId") String sourceId,
            @Param("title") String title,
            @Param("url") String url);
}

