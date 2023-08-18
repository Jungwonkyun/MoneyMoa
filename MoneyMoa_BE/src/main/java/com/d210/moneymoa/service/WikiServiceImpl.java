package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Wiki;
import com.d210.moneymoa.repository.WikiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WikiServiceImpl implements WikiService {
    @Autowired
    private WikiRepository wikiRepository;

    @Override
    public Wiki createWiki(Wiki wiki) {
        return wikiRepository.save(wiki);
    }

    @Override
    public List<Wiki> createWikiList(List<Wiki> wikiList) {
        List<Wiki> createdWikiList = new ArrayList<>();
        for (Wiki wiki : wikiList) {
            createdWikiList.add(createWiki(wiki));
        }
        return createdWikiList;
    }

    @Override
    public Optional<Wiki> getWiki(Long id) {
        return wikiRepository.findById(id);
    }

    @Override
    public List<Wiki> getAllWikis() {
        return wikiRepository.findAll();
    }

    @Override
    public void deleteWiki(Long id) {
        wikiRepository.deleteById(id);
    }
}
