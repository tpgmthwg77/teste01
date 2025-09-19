package com.bluegestao.whatsappbot.controller;

import com.bluegestao.whatsappbot.service.WhatsAppBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    
    @Autowired
    private WhatsAppBotService whatsAppBotService;
    
    @PostMapping("/whatsapp")
    public ResponseEntity<String> receiveWhatsAppMessage(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("📨 Webhook recebido: {}", payload);
            
            // Processar mensagem do WhatsApp
            if (payload.containsKey("messages")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> message = (Map<String, Object>) payload.get("messages");
                
                String phoneNumber = extractPhoneNumber(message);
                String senderName = extractSenderName(message);
                String messageContent = extractMessageContent(message);
                
                if (phoneNumber != null && messageContent != null) {
                    whatsAppBotService.processIncomingMessage(phoneNumber, senderName, messageContent);
                }
            }
            
            return ResponseEntity.ok("Webhook processed successfully");
            
        } catch (Exception e) {
            logger.error("❌ Erro ao processar webhook: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error processing webhook");
        }
    }
    
    private String extractPhoneNumber(Map<String, Object> message) {
        try {
            if (message.containsKey("from")) {
                return message.get("from").toString().replace("@c.us", "");
            }
        } catch (Exception e) {
            logger.error("Erro ao extrair número de telefone: {}", e.getMessage());
        }
        return null;
    }
    
    private String extractSenderName(Map<String, Object> message) {
        try {
            if (message.containsKey("notifyName")) {
                return message.get("notifyName").toString();
            } else if (message.containsKey("from")) {
                return message.get("from").toString().replace("@c.us", "");
            }
        } catch (Exception e) {
            logger.error("Erro ao extrair nome do remetente: {}", e.getMessage());
        }
        return "Usuário";
    }
    
    private String extractMessageContent(Map<String, Object> message) {
        try {
            if (message.containsKey("body")) {
                return message.get("body").toString();
            }
        } catch (Exception e) {
            logger.error("Erro ao extrair conteúdo da mensagem: {}", e.getMessage());
        }
        return null;
    }
}