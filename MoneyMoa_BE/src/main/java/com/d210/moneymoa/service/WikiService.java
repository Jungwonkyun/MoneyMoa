package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Wiki;
import java.util.List;
import java.util.Optional;

public interface WikiService {

    Wiki createWiki(Wiki wiki);
    List<Wiki> createWikiList(List<Wiki> wikiList);
    Optional<Wiki> getWiki(Long id);
    List<Wiki> getAllWikis();
    void deleteWiki(Long id);
}
