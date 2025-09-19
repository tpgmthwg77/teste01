package com.bluegestao.whatsappbot.service;

import com.bluegestao.whatsappbot.model.Contact;
import com.bluegestao.whatsappbot.model.Message;
import com.bluegestao.whatsappbot.repository.ContactRepository;
import com.bluegestao.whatsappbot.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class WhatsAppBotService {
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppBotService.class);
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private WhatsAppApiService whatsAppApiService;
    
    public void processIncomingMessage(String phoneNumber, String senderName, String messageContent) {
        try {
            logger.info("📨 Processando mensagem de: {} ({})", senderName, phoneNumber);
            logger.info("💬 Conteúdo: {}", messageContent);
            
            // Salvar a mensagem recebida
            Message incomingMessage = new Message(phoneNumber, senderName, messageContent);
            messageRepository.save(incomingMessage);
            
            // Processar ou criar contato
            Contact contact = processContact(phoneNumber, senderName);
            
            // Gerar resposta automática
            String response = generateWelcomeResponse(contact, messageContent);
            
            // Enviar resposta via WhatsApp
            whatsAppApiService.sendMessage(phoneNumber, response);
            
            // Salvar a mensagem de resposta
            Message responseMessage = new Message(phoneNumber, "Blue Gestão Bot", response);
            messageRepository.save(responseMessage);
            
            logger.info("✅ Mensagem processada e resposta enviada para: {}", senderName);
            
        } catch (Exception e) {
            logger.error("❌ Erro ao processar mensagem de {}: {}", phoneNumber, e.getMessage());
        }
    }
    
    private Contact processContact(String phoneNumber, String senderName) {
        Optional<Contact> existingContact = contactRepository.findByPhoneNumber(phoneNumber);
        
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.incrementMessageCount();
            if (senderName != null && !senderName.isEmpty()) {
                contact.setName(senderName);
            }
            return contactRepository.save(contact);
        } else {
            Contact newContact = new Contact(phoneNumber, senderName);
            logger.info("👤 Novo contato registrado: {} ({})", senderName, phoneNumber);
            return contactRepository.save(newContact);
        }
    }
    
    private String generateWelcomeResponse(Contact contact, String messageContent) {
        StringBuilder response = new StringBuilder();
        
        // Mensagem principal de boas-vindas
        response.append("🎉 *Seja muito bem-vindo ao atendimento da Blue Gestão!*\n\n");
        
        if (contact.getMessageCount() == 1) {
            // Primeira mensagem do usuário
            response.append("Olá! Fico feliz em ter você aqui! 😊\n\n");
            response.append("Sou o assistente virtual da *Blue Gestão* e estou aqui para ajudá-lo da melhor forma possível.\n\n");
        } else {
            // Usuário já entrou em contato antes
            response.append("Que bom ter você de volta! 🤗\n\n");
            response.append("Esta é nossa ").append(contact.getMessageCount()).append("ª conversa. ");
            response.append("Estou sempre aqui para ajudá-lo!\n\n");
        }
        
        // Menu de opções
        response.append("📋 *Como posso ajudá-lo hoje?*\n");
        response.append("Digite uma das opções:\n\n");
        response.append("1️⃣ Informações sobre nossos serviços\n");
        response.append("2️⃣ Solicitar orçamento\n");
        response.append("3️⃣ Suporte técnico\n");
        response.append("4️⃣ Falar com atendente humano\n");
        response.append("5️⃣ Horários de funcionamento\n\n");
        
        response.append("💙 *Blue Gestão* - Sua parceira em soluções empresariais!\n");
        response.append("⏰ Horário de atendimento: Segunda a Sexta, 8h às 18h");
        
        return response.toString();
    }
    
    public void sendCustomMessage(String phoneNumber, String message) {
        try {
            whatsAppApiService.sendMessage(phoneNumber, message);
            
            Message customMessage = new Message(phoneNumber, "Blue Gestão Bot", message);
            messageRepository.save(customMessage);
            
            logger.info("📤 Mensagem personalizada enviada para: {}", phoneNumber);
        } catch (Exception e) {
            logger.error("❌ Erro ao enviar mensagem personalizada: {}", e.getMessage());
        }
    }
}