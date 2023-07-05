package com.jingjiang.gb28181.domain;

import com.jingjiang.gb28181.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Stream extends BaseEntity {

    private String host;

    private String channelId;

    private String branchId;

    private String callId;

    private String fromTag;

    private String viaTag;

}
