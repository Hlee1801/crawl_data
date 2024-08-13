package hseneca.crawlerbase.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.swing.text.Element;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Table(name = "records", uniqueConstraints = @UniqueConstraint(columnNames = {"source", "source_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String author;
    private LocalDate publishDate;
    @Column(columnDefinition = "TEXT")
    private String url;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT")
    private Source source;
    private String sourceId;

    @CreationTimestamp
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    private ZonedDateTime updatedAt;


}
