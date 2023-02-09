package org.openai.chatgpt.controllers;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;
import lombok.extern.slf4j.Slf4j;
import org.openai.chatgpt.service.CompletionService;
import org.openai.chatgpt.service.OpenAiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.openai.chatgpt.controllers.OpenAIController.REQUEST_MAPPING_URL;

/**
 *
 * A controller used for OpenAI
 */
@RestController
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3000", "http://34.160.152.235"})
@RequestMapping(REQUEST_MAPPING_URL)
public class OpenAIController {


    public static final String REQUEST_MAPPING_URL = "/openai";

    @Autowired
    CompletionService completionService;

    @Autowired
    OpenAiFactory openAiFactory;

    @Value("${openai.api.model}")
    private String openaiModel;

    @Value("${openai.api.temperature}")
    private Double temperature;

    @Value("${openai.api.maxTokens}")
    private Integer maxTokens;


    /***
     *
     * @param text
     * @return
     */
    @PostMapping("/completion")
    @ResponseStatus(HttpStatus.OK)
    public String chat(@RequestBody String text) throws IOException {

        log.info("/completion => " + text);
        List<String> responsesOpenAI = new ArrayList<>();
        List<String> responsesGrumpyCat;

        //Manage specific completion
        responsesGrumpyCat = completionService.manageSpecificText(text);
        if (!responsesGrumpyCat.isEmpty())
            return responsesGrumpyCat.stream().reduce("", String::concat);

        //OpenAiService service = new OpenAiService(openaiApiKey);
        OpenAiService service = openAiFactory.getOpenAiService();

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt(text)
                    .model(openaiModel)
                    .temperature(temperature)
                    .echo(false)
                    .maxTokens(maxTokens)
                    .build();
            service.createCompletion(completionRequest).getChoices().forEach(c -> responsesOpenAI.add(c.getText()));

           // responsesOpenAI = completionService.replaceSpecificAnswer(responsesOpenAI);


        return responsesOpenAI.stream().reduce("", String::concat);
    }

    @PostMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    public String image(@RequestBody String text) {

        log.info("/image => " + text);
        List<String> responses = new ArrayList<>();

        //OpenAiService service = new OpenAiService(openaiApiKey);
        OpenAiService service = openAiFactory.getOpenAiService();

        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt(text)
                .size("256x256")
                .build();
        service.createImage(createImageRequest).getData().forEach(c -> responses.add(c.getUrl()));

        return responses.stream().reduce("", String::concat);
    }
}
