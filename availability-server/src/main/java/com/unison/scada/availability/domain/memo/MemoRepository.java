package com.unison.scada.availability.domain.memo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Memo.MemoId> {
}
