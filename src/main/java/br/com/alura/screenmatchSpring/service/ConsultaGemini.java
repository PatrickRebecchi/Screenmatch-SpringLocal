package br.com.alura.screenmatchSpring.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

public class ConsultaGemini {

    public static String obterTraducao(String texto) {

        ChatLanguageModel gemini = GoogleAiGeminiChatModel.builder()
                //.apiKey(System.getenv("API_KEY_GEMINI"))
                .apiKey(System.getenv("API_GEMINI"))
                .modelName("gemini-2.5-flash")

                .build();

        // Adicionamos uma instrução direta para evitar conversas
        String response = gemini.generate(
                "Traduza o seguinte texto para o português. " +
                        "Retorne apenas a tradução direta, sem comentários, sem explicações e sem variações: " + texto
        );

        return response;
    }
}