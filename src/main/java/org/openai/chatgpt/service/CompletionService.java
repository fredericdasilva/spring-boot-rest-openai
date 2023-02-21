package org.openai.chatgpt.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CompletionService {

    final Map<String,String> answersPredefined = Stream.of(new String[][] {
            {"[nom]", "Grumpy Cat, un chat reposant sur les api OpenAI (ChatGPT)"},
            {"[votre nom]", "Grumpy Cat, un chat reposant sur les api OpenAI (ChatGPT)"},
            {"[name]", "Grumpy Cat, an interactive cat using Open AI api (Chat GPT)"},
            {"[your name]", "Grumpy Cat, an interactive cat using Open AI api (Chat GPT)"},
            {"[date]", new SimpleDateFormat("dd/MM/yyyy").toString()},
            {"{date}", new SimpleDateFormat("dd/MM/yyyy").toString()},
            {"[date du jour]", new SimpleDateFormat("dd/MM/yyyy").toString()},
            {"${new Date().toLocaleDateString()}", new SimpleDateFormat("dd/MM/yyyy").toString()},

    }).collect(Collectors.collectingAndThen(
            Collectors.toMap(data -> data[0], data -> data[1]),
            Collections::<String, String> unmodifiableMap));

    public String replaceSpecificAnswer(String responsesOpenAI) {

        for (String key: answersPredefined.keySet()){
            if (responsesOpenAI.toLowerCase().contains(key.toLowerCase()))
                responsesOpenAI = responsesOpenAI.replace(key,answersPredefined.get(key));
        }

        return responsesOpenAI;
    }
}
