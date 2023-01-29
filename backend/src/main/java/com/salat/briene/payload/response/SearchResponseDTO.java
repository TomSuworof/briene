package com.salat.briene.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SearchResponseDTO {
    private Suggest suggest;
    private PageResponseDTO<ArticleDTO> page;
}
