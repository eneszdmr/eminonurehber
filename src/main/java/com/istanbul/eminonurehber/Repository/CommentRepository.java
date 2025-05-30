package com.istanbul.eminonurehber.Repository;

import com.istanbul.eminonurehber.Entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByCompanyId(Long companyId);
}
