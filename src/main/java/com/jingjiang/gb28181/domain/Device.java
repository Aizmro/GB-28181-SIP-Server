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
public class Device extends BaseEntity {

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口
     */
    private Integer rPort;

    /**
     * 穿透地址
     */
    private String received;

    /**
     * 设备协议
     */
    private String protocol;

}
