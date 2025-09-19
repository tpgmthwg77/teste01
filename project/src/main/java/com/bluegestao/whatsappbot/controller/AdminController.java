package com.bluegestao.whatsappbot.controller;

import com.bluegestao.whatsappbot.model.Contact;
import com.bluegestao.whatsappbot.model.Message;
import com.bluegestao.whatsappbot.repository.ContactRepository;
import com.bluegestao.whatsappbot.repository.MessageRepository;
import com.bluegestao.whatsappbot.service.WhatsAppApiService;
import com.bluegestao.whatsappbot.service.WhatsAppBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private WhatsAppApiService whatsAppApiService;
    
    @Autowired
    private WhatsAppBotService whatsAppBotService;
    
    @GetMapping("/")
    public String dashboard(Model model) {
        List<Contact> contacts = contactRepository.findAll();
        List<Message> recentMessages = messageRepository.findTop10ByOrderByTimestampDesc();
        
        model.addAttribute("totalContacts", contacts.size());
        model.addAttribute("contacts", contacts);
        model.addAttribute("recentMessages", recentMessages);
        model.addAttribute("isConnected", whatsAppApiService.isConnected());
        
        return "dashboard";
    }
    
    @GetMapping("/qr")
    @ResponseBody
    public String getQrCode() {
        return whatsAppApiService.getQrCode();
    }
    
    @GetMapping("/status")
    @ResponseBody
    public String getStatus() {
        boolean connected = whatsAppApiService.checkConnection();
        return "{\"connected\": " + connected + "}";
    }
    
    @PostMapping("/send-message")
    @ResponseBody
    public String sendMessage(@RequestParam String phoneNumber, @RequestParam String message) {
        try {
            whatsAppBotService.sendCustomMessage(phoneNumber, message);
            return "{\"success\": true, \"message\": \"Mensagem enviada com sucesso!\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Erro ao enviar mensagem: " + e.getMessage() + "\"}";
        }
    }
}