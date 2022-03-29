package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

@Getter
@Setter
@AllArgsConstructor
public class SearchResponseDTO {
    private Suggest suggest;
    private PageResponseDTO<ArticleDTO> page;
}
