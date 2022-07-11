package com.example.tgbot.repository;

import com.example.tgbot.entity.ActiveChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveChatRepository extends JpaRepository<ActiveChat, Integer> {

    Optional<ActiveChat> findActiveChatByChatId(Long chatId);
}
