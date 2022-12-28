package org.example.controllers;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.example.controllers.OpenAIController.REQUEST_MAPPING_URL;

/**
 *
 * A controller used for OpenAI
 */
@RestController
@RequestMapping(REQUEST_MAPPING_URL)
public class OpenAIController {


    public static final String REQUEST_MAPPING_URL = "/openai";

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.model}")
    private String openaiModel;

    /***
     *
     * @param text
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String chat(@RequestParam String text) {

        List<String> responses = new ArrayList<>();

        OpenAiService service = new OpenAiService(openaiApiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(text)
                .model(openaiModel)
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(c -> responses.add(c.getText()));

        return responses.stream().reduce("", String::concat);
    }
}
