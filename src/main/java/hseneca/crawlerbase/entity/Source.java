package hseneca.crawlerbase.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Source {
//    NATURE,
//    ARXIV

    NATURE("Nature"),
    ARXIV("ARXIV");

    private final String source;



}
