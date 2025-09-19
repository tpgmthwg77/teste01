# 🤖 WhatsApp Bot - Blue Gestão

Sistema completo de WhatsApp Bot usando Java Spring Boot com integração Node.js para comunicação com o WhatsApp Web.

## 📋 Características Principais

- ✅ Resposta automática personalizada para Blue Gestão
- 📊 Dashboard administrativo completo
- 💾 Armazenamento de contatos e mensagens
- 📱 Interface web para gerenciamento
- 🔄 Funcionamento contínuo 24/7
- 📈 Sem limite de usuários
- 🛡️ Sistema robusto e escalável

## 🚀 Configuração e Instalação

### Pré-requisitos

- **Java 17+**
- **Maven 3.6+**
- **Node.js 16+**
- **VS Code** (recomendado)

### Passo 1: Configurar o Servidor WhatsApp (Node.js)

1. **Instalar dependências do Node.js:**
   ```bash
   npm install
   ```

2. **Iniciar o servidor WhatsApp:**
   ```bash
   npm start
   ```
   ou para desenvolvimento:
   ```bash
   npm run dev
   ```

### Passo 2: Configurar a Aplicação Spring Boot

1. **Compilar o projeto:**
   ```bash
   mvn clean install
   ```

2. **Executar a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

### Passo 3: Conectar o WhatsApp

1. **Acessar o dashboard:** http://localhost:8080
2. **Gerar QR Code** clicando no botão "Gerar QR Code"
3. **Escanear com WhatsApp** no seu celular
4. **Aguardar confirmação** de conexão

## 🎯 Como Usar

### Dashboard Administrativo

Acesse `http://localhost:8080` para:

- ✅ Visualizar status da conexão
- 📊 Ver estatísticas de contatos
- 💬 Monitorar mensagens recentes
- 📤 Enviar mensagens personalizadas
- 🔄 Gerenciar conexões

### Funcionamento Automático

O bot responde automaticamente a **TODAS** as mensagens recebidas com:

```
🎉 Seja muito bem-vindo ao atendimento da Blue Gestão!

Olá! Fico feliz em ter você aqui! 😊

Sou o assistente virtual da Blue Gestão e estou aqui para ajudá-lo da melhor forma possível.

📋 Como posso ajudá-lo hoje?
Digite uma das opções:

1️⃣ Informações sobre nossos serviços
2️⃣ Solicitar orçamento
3️⃣ Suporte técnico
4️⃣ Falar com atendente humano
5️⃣ Horários de funcionamento

💙 Blue Gestão - Sua parceira em soluções empresariais!
⏰ Horário de atendimento: Segunda a Sexta, 8h às 18h
```

## 🔧 Configuração no VS Code

### Estrutura de Arquivos

```
whatsapp-bot/
├── src/main/java/com/bluegestao/whatsappbot/
│   ├── WhatsappBotApplication.java
│   ├── controller/
│   ├── service/
│   ├── model/
│   └── repository/
├── src/main/resources/
│   ├── templates/
│   └── application.properties
├── whatsapp-api-server.js
├── package.json
├── pom.xml
└── README.md
```

### Configurações Recomendadas

1. **Extensões VS Code:**
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - JavaScript (ES6) code snippets

2. **Configurações de Launch:**
   Criar `.vscode/launch.json`:
   ```json
   {
     "version": "0.2.0",
     "configurations": [
       {
         "type": "java",
         "name": "WhatsApp Bot",
         "request": "launch",
         "mainClass": "com.bluegestao.whatsappbot.WhatsappBotApplication",
         "projectName": "whatsapp-bot"
       }
     ]
   }
   ```

## ⚡ Execução em Produção

### Para Manter 24/7 Funcionando

1. **Servidor WhatsApp (Terminal 1):**
   ```bash
   # Manter sempre rodando
   npm start
   ```

2. **Aplicação Spring Boot (Terminal 2):**
   ```bash
   # Manter sempre rodando
   mvn spring-boot:run
   ```

### Dicas para Estabilidade

- 🔄 **Reiniciar automaticamente:** Use `nodemon` para Node.js
- 📊 **Monitorar logs:** Acompanhe os logs no console
- 🔍 **Verificar conexão:** Use o dashboard para monitorar status
- 💾 **Backup:** Os dados ficam salvos no H2 Database

## 🛠️ Personalização

### Modificar Mensagem de Boas-vindas

Edite o arquivo:
`src/main/java/com/bluegestao/whatsappbot/service/WhatsAppBotService.java`

Método: `generateWelcomeResponse()`

### Adicionar Novas Funcionalidades

1. **Novos endpoints:** `AdminController.java`
2. **Lógica de negócio:** `WhatsAppBotService.java`
3. **Interface web:** `templates/dashboard.html`

## 📊 Monitoramento

- **Dashboard:** http://localhost:8080
- **API Status:** http://localhost:3001
- **H2 Database:** http://localhost:8080/h2-console
- **Logs:** Console do VS Code

## 🚨 Solução de Problemas

### WhatsApp não conecta
- Verificar se Node.js está rodando na porta 3001
- Gerar novo QR Code
- Verificar internet e firewall

### Spring Boot não inicia
- Verificar Java 17+
- Executar `mvn clean install`
- Verificar porta 8080 disponível

### Mensagens não chegam
- Verificar conexão WhatsApp
- Verificar logs do console
- Testar envio manual pelo dashboard

## 📞 Suporte

Sistema desenvolvido para **Blue Gestão** 
- Dashboard: http://localhost:8080
- API WhatsApp: http://localhost:3001

---

**✅ Sistema 100% Funcional | 🤖 Automático | 🔄 24/7 | 📱 WhatsApp Oficial**