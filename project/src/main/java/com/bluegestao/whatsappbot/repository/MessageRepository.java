package com.bluegestao.whatsappbot.repository;

import com.bluegestao.whatsappbot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByPhoneNumberOrderByTimestampDesc(String phoneNumber);
    List<Message> findTop10ByOrderByTimestampDesc();
}