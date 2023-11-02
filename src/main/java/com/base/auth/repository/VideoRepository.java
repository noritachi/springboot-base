package com.base.auth.repository;

import com.base.auth.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VideoRepository extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {

}
