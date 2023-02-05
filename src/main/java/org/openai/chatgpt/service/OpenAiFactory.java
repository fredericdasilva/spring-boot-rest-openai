package org.openai.chatgpt.service;

import com.theokanning.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class OpenAiFactory {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public OpenAiService getOpenAiService(){
        return new OpenAiService(openaiApiKey);
    }
}
