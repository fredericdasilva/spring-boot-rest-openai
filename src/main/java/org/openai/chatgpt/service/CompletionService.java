package org.openai.chatgpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openai.chatgpt.dto.CompletionInputDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CompletionService {

    final Map<String, String> answersPredefined = Stream.of(new String[][] {
            { "[your name]", "Grumpy Cat" },
            { "[votre nom]", "Grumpy Cat" },
    }).collect(Collectors.collectingAndThen(
            Collectors.toMap(data -> data[0], data -> data[1]),
            Collections::<String, String> unmodifiableMap));

    public List<String> manageSpecificText(String text) throws IOException {

        List<String> responses = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        CompletionInputDTO dto = mapper.readValue(text, CompletionInputDTO.class);

        String trimedText = dto.getPrompt().toLowerCase().trim().replace("\n", "").replace(" ","");
        List<String> who = Arrays.asList("quiest-tu?", "quiest-tu", "quies-tu?", "commenttut'appelles", "commenttut'appelle","commenttutappelle", "commenttutappelle");

        if (who.contains(trimedText))
            responses.add("Mon nom est Grumpy Cat, un chat reposant sur les api OpenAI (chatGPT si tu préfères).\n Et toi quel est ton nom ?");

        return responses;
    }

    public List<String> replaceSpecificAnswer(List<String> responsesOpenAI) {


        for (String s: responsesOpenAI) {

            Matcher m = Pattern.compile("\\([.*?]\\)").matcher(s);
            while (m.find()) {
                System.out.println(m.group(1));
                answersPredefined.entrySet().contains(m.group(1));
            }

        }

        return Collections.emptyList();
    }
}
