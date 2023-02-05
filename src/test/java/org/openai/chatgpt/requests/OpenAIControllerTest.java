package org.openai.chatgpt.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hackerrank.test.utility.Order;
import com.hackerrank.test.utility.OrderedTestRunner;
import com.hackerrank.test.utility.TestWatchman;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.image.ImageResult;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openai.chatgpt.config.WebConfig;
import org.openai.chatgpt.controllers.OpenAIController;
import org.openai.chatgpt.service.CompletionService;
import org.openai.chatgpt.service.OpenAiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@RunWith(OrderedTestRunner.class)
@ContextConfiguration(classes = { WebConfig.class, OpenAIController.class })
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class OpenAIControllerTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public TestWatcher watchman = TestWatchman.watchman;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CompletionService completionService;

    @MockBean
    OpenAiService openAiService;

    @MockBean
    OpenAiFactory openAiFactory;

    @BeforeClass
    public static void setUpClass() {
        TestWatchman.watchman.registerClass(OpenAIControllerTest.class);
    }

    @AfterClass
    public static void tearDownClass() {
        TestWatchman.watchman.createReport(OpenAIControllerTest.class);
    }

    /**
     *
     * @throws Exception
     *
     * It tests response to be Completion api
     */
    @Test
    @Order(1)
    public void completion() throws Exception {

        String urlCompletionTest = "This is a test from OpenAI Mock";
        CompletionResult completionResult = new CompletionResult();
        CompletionChoice choice = new CompletionChoice();
        choice.setText(urlCompletionTest);

        completionResult.setChoices(Arrays.asList(choice));

        Mockito.when(openAiService.createCompletion(Mockito.any())).thenReturn(completionResult);
        Mockito.when(openAiFactory.getOpenAiService()).thenReturn(openAiService);


        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString("Hello");
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/openai/completion").content(requestJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Assert.assertEquals(response, urlCompletionTest);
    }


    /**
     *
     * @throws Exception
     *
     * It tests response to be Image api
     */
    @Test
    @Order(1)
    public void image() throws Exception {

        String urlImageTest = "https://urlTotestImage.jpg";
        ImageResult imageResult = new ImageResult();
        Image image = new Image();
        image.setUrl(urlImageTest);
        imageResult.setData(Arrays.asList(image));

        Mockito.when(openAiService.createImage(Mockito.any())).thenReturn(imageResult);
        Mockito.when(openAiFactory.getOpenAiService()).thenReturn(openAiService);


        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString("Hello");
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/openai/image").content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals(response, urlImageTest);
    }


}
