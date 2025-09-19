/**
 * Servidor Node.js para integração com WhatsApp Web
 * Execute: node whatsapp-api-server.js
 */

const express = require('express');
const { Client, LocalAuth, MessageMedia } = require('whatsapp-web.js');
const qrcode = require('qrcode');
const cors = require('cors');
const path = require('path');

const app = express();
const PORT = 3001;

// Middlewares
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Cliente WhatsApp
const client = new Client({
    authStrategy: new LocalAuth({
        clientId: "blue-gestao-bot"
    }),
    puppeteer: {
        headless: true,
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-dev-shm-usage',
            '--disable-accelerated-2d-canvas',
            '--no-first-run',
            '--no-zygote',
            '--single-process',
            '--disable-gpu'
        ]
    }
});

let qrCodeData = null;
let isReady = false;

// Eventos do cliente WhatsApp
client.on('qr', async (qr) => {
    console.log('🔍 QR Code gerado!');
    qrCodeData = await qrcode.toDataURL(qr);
    console.log('📱 Escaneie o QR Code com o WhatsApp');
});

client.on('ready', () => {
    console.log('✅ Cliente WhatsApp conectado e pronto!');
    isReady = true;
    qrCodeData = null;
});

client.on('authenticated', () => {
    console.log('🔐 Cliente autenticado com sucesso!');
});

client.on('auth_failure', () => {
    console.log('❌ Falha na autenticação');
    isReady = false;
});

client.on('disconnected', (reason) => {
    console.log('🔌 Cliente desconectado:', reason);
    isReady = false;
});

// Receber mensagens e encaminhar para o Spring Boot
client.on('message', async (message) => {
    console.log('📨 Nova mensagem recebida:');
    console.log(`De: ${message.from}`);
    console.log(`Autor: ${message.author || message.from}`);
    console.log(`Conteúdo: ${message.body}`);
    
    try {
        // Enviar para o webhook do Spring Boot
        const webhookData = {
            messages: {
                from: message.from,
                notifyName: message._data.notifyName || 'Usuário',
                body: message.body,
                timestamp: message.timestamp,
                type: message.type
            }
        };
        
        // Aqui você pode fazer uma requisição para o Spring Boot
        // Por exemplo, usando fetch ou axios
        console.log('📤 Dados para webhook:', JSON.stringify(webhookData, null, 2));
        
        // Simular envio para o Spring Boot (descomente e configure se necessário)
        /*
        const fetch = require('node-fetch');
        await fetch('http://localhost:8080/webhook/whatsapp', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(webhookData)
        });
        */
        
    } catch (error) {
        console.error('❌ Erro ao processar mensagem:', error);
    }
});

// Rotas da API
app.get('/status', (req, res) => {
    res.json({ 
        connected: isReady,
        status: isReady ? 'ready' : 'disconnected'
    });
});

app.get('/qr', (req, res) => {
    if (qrCodeData) {
        // Remover prefixo data:image/png;base64,
        const base64Data = qrCodeData.replace('data:image/png;base64,', '');
        res.send(base64Data);
    } else {
        res.status(404).send('QR Code não disponível');
    }
});

app.post('/send-message', async (req, res) => {
    try {
        const { chatId, message } = req.body;
        
        if (!isReady) {
            return res.status(400).json({ 
                error: 'Cliente WhatsApp não está pronto' 
            });
        }
        
        if (!chatId || !message) {
            return res.status(400).json({ 
                error: 'chatId e message são obrigatórios' 
            });
        }
        
        await client.sendMessage(chatId, message);
        
        console.log(`📤 Mensagem enviada para ${chatId}: ${message.substring(0, 50)}...`);
        
        res.json({ 
            success: true, 
            message: 'Mensagem enviada com sucesso' 
        });
        
    } catch (error) {
        console.error('❌ Erro ao enviar mensagem:', error);
        res.status(500).json({ 
            error: 'Erro ao enviar mensagem: ' + error.message 
        });
    }
});

app.get('/', (req, res) => {
    res.send(`
        <h1>🤖 WhatsApp API Server - Blue Gestão</h1>
        <p><strong>Status:</strong> ${isReady ? '✅ Conectado' : '❌ Desconectado'}</p>
        <p><strong>Porta:</strong> ${PORT}</p>
        <h2>Endpoints disponíveis:</h2>
        <ul>
            <li><code>GET /status</code> - Status da conexão</li>
            <li><code>GET /qr</code> - QR Code para autenticação</li>
            <li><code>POST /send-message</code> - Enviar mensagem</li>
        </ul>
        <hr>
        <p><em>Blue Gestão WhatsApp Bot © 2025</em></p>
    `);
});

// Iniciar servidor
app.listen(PORT, () => {
    console.log('🚀 WhatsApp API Server iniciado!');
    console.log(`🌐 Servidor rodando em: http://localhost:${PORT}`);
    console.log('📱 Iniciando cliente WhatsApp...');
    
    // Inicializar cliente WhatsApp
    client.initialize();
});

// Tratamento de erros
process.on('unhandledRejection', (reason, promise) => {
    console.error('❌ Unhandled Rejection at:', promise, 'reason:', reason);
});

process.on('uncaughtException', (error) => {
    console.error('❌ Uncaught Exception:', error);
});