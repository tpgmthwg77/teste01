package com.bluegestao.whatsappbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppApiService {
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppApiService.class);
    
    @Value("${whatsapp.api.url:http://localhost:3001}")
    private String whatsappApiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private boolean isConnected = false;
    
    public void sendMessage(String phoneNumber, String message) {
        try {
            String url = whatsappApiUrl + "/send-message";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("chatId", phoneNumber + "@c.us");
            requestBody.put("message", message);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            String response = restTemplate.postForObject(url, request, String.class);
            
            logger.info("📤 Mensagem enviada para {}: {}", phoneNumber, message.substring(0, Math.min(50, message.length())) + "...");
            
        } catch (Exception e) {
            logger.error("❌ Erro ao enviar mensagem via WhatsApp API: {}", e.getMessage());
            
            // Simulação em caso de erro (para desenvolvimento)
            logger.info("🔄 MODO SIMULAÇÃO - Mensagem que seria enviada para {}:", phoneNumber);
            logger.info("💬 {}", message);
        }
    }
    
    public boolean checkConnection() {
        try {
            String url = whatsappApiUrl + "/status";
            String response = restTemplate.getForObject(url, String.class);
            isConnected = response != null && response.contains("ready");
            return isConnected;
        } catch (Exception e) {
            logger.warn("⚠️ Não foi possível verificar conexão com WhatsApp API: {}", e.getMessage());
            isConnected = false;
            return false;
        }
    }
    
    public String getQrCode() {
        try {
            String url = whatsappApiUrl + "/qr";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            logger.error("❌ Erro ao obter QR Code: {}", e.getMessage());
            return null;
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
}