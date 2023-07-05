package com.jingjiang.gb28181.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StreamRepository extends JpaRepository<Stream, Long>, JpaSpecificationExecutor<Stream> {

    Optional<Stream> findStreamByHostAndChannelId(String host, String channelId);

}
