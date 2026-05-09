package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NoteRepository extends JpaRepository<Note , Long> {
}
