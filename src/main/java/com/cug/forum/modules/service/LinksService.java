package com.cug.forum.modules.service;

import com.cug.forum.modules.entity.Links;

import java.util.List;

public interface LinksService {
    List<Links> findAll();
    void update(Links links);
    void delete(long id);
}
