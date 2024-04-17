package com.unison.scada.availability.api.memo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Memo.MemoId> {
}
