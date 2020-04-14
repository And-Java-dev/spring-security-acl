package com.example.springsecurityacl.persistence.dao;

import com.example.springsecurityacl.persistence.entity.NoticeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface NoticeMessageRepository  extends JpaRepository<NoticeMessage, Integer> {

    @PostFilter("hasPermission(filterObject,'READ')")
    List<NoticeMessage> findAll();

    @PostAuthorize("hasPermission(returnObject, 'READ')")
   Optional<NoticeMessage>  findById(Integer id);

    @SuppressWarnings("unchekd")
    @PreAuthorize("hasPermission(#noriceMessage, 'WRITE')")
    NoticeMessage save(@Param("noticeMessage") NoticeMessage noticeMessage);


}
