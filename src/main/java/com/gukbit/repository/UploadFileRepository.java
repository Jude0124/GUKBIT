package com.gukbit.repository;

import com.gukbit.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long>{
}

