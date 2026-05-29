package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Publicite;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PubliciteRepository extends JpaRepository<Publicite, Long> {
    Page<Publicite> findAllByAssignedTooOrderByCreatedAtDesc(User user, Pageable pageable);
}
