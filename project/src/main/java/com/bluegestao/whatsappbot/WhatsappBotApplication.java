package com.bluegestao.whatsappbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class WhatsappBotApplication {
    public static void main(String[] args) {
        System.out.println("🤖 Iniciando WhatsApp Bot da Blue Gestão...");
        SpringApplication.run(WhatsappBotApplication.class, args);
        System.out.println("✅ WhatsApp Bot da Blue Gestão iniciado com sucesso!");
        System.out.println("📱 Acesse: http://localhost:8080 para configurar o QR Code");
    }
}