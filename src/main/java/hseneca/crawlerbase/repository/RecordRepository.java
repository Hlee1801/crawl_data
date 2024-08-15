package hseneca.crawlerbase.repository;

import hseneca.crawlerbase.entity.Record;
import hseneca.crawlerbase.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Record findBySourceAndSourceId(Source source, String sourceId);

    @Modifying
    @Query(value = "INSERT INTO records (author, description, image_url, publish_date, source, source_id, title, url, article_type, created_at, updated_at) " +
            "VALUES (:author, :description , :imageUrl, :publishDate, :#{#source?.name()} , :sourceId, :title, :url , :articleType, :createdAt, :updatedAt) " +
            "ON CONFLICT (source, source_id) DO UPDATE SET" +
            "    author = EXCLUDED.author," +
            "    description = EXCLUDED.description," +
            "    image_url = EXCLUDED.image_url," +
            "    publish_date = EXCLUDED.publish_date," +
            "    title = EXCLUDED.title," +
            "    article_type = EXCLUDED.article_type," +
            "    updated_at = EXCLUDED.updated_at," +
            "    url = EXCLUDED.url", nativeQuery = true)

    void insertOrUpdate(
            @Param("author") String author,
            @Param("description") String description,
            @Param("imageUrl") String imageUrl,
            @Param("publishDate") LocalDate publishDate,
            @Param("source") Source source,
            @Param("sourceId") String sourceId,
            @Param("title") String title,
            @Param("url") String url,
            @Param("articleType") String articleType,
            @Param("createdAt") ZonedDateTime createdAt,
            @Param("updatedAt") ZonedDateTime updatedAt
    );

//    void insertOrUpdate(
//            @Param("imageUrl") String imageUrl,
//            @Param("publishDate") LocalDate publishDate,
//            @Param("source") Source source,
//            @Param("sourceId") String sourceId,
//            @Param("title") String title,
//            @Param("url") String url,
//            @Param("createdAt") ZonedDateTime createdAt,
//            @Param("updatedAt") ZonedDateTime updatedAt);
}

