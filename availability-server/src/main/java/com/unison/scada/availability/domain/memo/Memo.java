package com.unison.scada.availability.domain.memo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name="Memo")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Memo {

    @EmbeddedId
    private MemoId memoId;

    @Column(nullable = false)
    private String engineerName;

    @Column(nullable = false)
    private LocalDateTime workTime;

    @Column(nullable = true)
    private String material;

    @Column(nullable = true)
    private int quantity;

    @Column(nullable = true)
    private String workType;

    @Column(nullable = true)
    private String inspection;

    @Column(nullable = true)
    private String etc;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isActive;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDelete;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private String updatedBy;

    @Data
    @Embeddable
    public static class MemoId implements Serializable{

        @Column
        private LocalDateTime registerTime;

        @Column
        private int turbineId;
    }
}
