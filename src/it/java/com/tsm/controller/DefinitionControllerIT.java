package com.tsm.controller;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tsm.sendemail.SendEmailApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SendEmailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class DefinitionControllerIT {

//    private static final String EMPTY_WORDSm_ERROR_MESSAGE = "The words must not be empty.";
//
//    private static final String NONE_VALID_WORDS_GIVEN = "None valid words was given: %s";
//
//    private static final String OXFORD_SERVICE_SAMPLE_FILE_NAME = "oxford.json";
//
//    public String[] validWords = new String[] { "at", "your", "all", "home", "word", "house", "car" };
//
//    public String[] validWordsToSort = new String[] { "at", "your", "all", "word", "house" };
//
//    private List<KnownWord> knownWords;
//
//    private boolean cachedLoad = false;
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private KnownWordRepository knownWordRepository;
//
//    @Autowired
//    private DefinitionRepository definitionRepository;
//
//    @Before
//    public void setUp() {
//        // load mongo known words
//        if (knownWords == null) {
//            knownWords = loadWords();
//            knownWords.forEach(k -> {
//                knownWordRepository.save(k);
//            });
//        }
//        if (!cachedLoad) {
//            List<Definition> cachedDefinition = buidDefinitionFromFile();
//            cachedDefinition.forEach(d -> definitionRepository.save(d));
//            cachedLoad = true;
//        }
//        RestAssured.port = port;
//    }
//
//    @Test
//    public void getDefinitions_NullWordsGiven_ShouldReturnError() {
//        // Set Up
//        Set<String> words = new HashSet<>();
//        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();
//
//        // Do Test
//        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
//            .statusCode(HttpStatus.BAD_REQUEST.value()).body("message", is(EMPTY_WORDS_ERROR_MESSAGE));
//    }
//
//    @Test
//    public void getDefinitions_InvalidWordsGiven_ShouldReturnError() throws URISyntaxException {
//        // Set Up
//        String word = "ikkf";
//
//        Set<String> words = new HashSet<>();
//        words.add(word);
//        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();
//
//        // Do Test
//        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("message", is(String.format(NONE_VALID_WORDS_GIVEN, words)));
//    }
//
//    @Test
//    public void getDefinitions_CachedWordAndInvalidWordsGiven_ShouldReturnDefinition() throws URISyntaxException {
//        // Set Up
//        String word = "home";
//        String car = "Car";
//        String invalidWord = "klkkl";
//        Set<String> words = new HashSet<>();
//        words.add(word);
//        words.add(car);
//        words.add(invalidWord);
//        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();
//
//        // Do Test
//        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
//            .statusCode(HttpStatus.CREATED.value()).body("definitions.size()", is(2))
//            .body("definitions[0].word", is(car.toLowerCase()))
//            .body("definitions[0].definitions.size()", is(greaterThan(0)))
//            .body("definitions[1].word", is(word))
//            .body("definitions[1].definitions.size()", is(greaterThan(0)))
//            .body("invalidWords.size()", is(greaterThan(0)));
//    }
//
//    @Test
//    public void getDefinitions_NotCachedWordGiven_ShouldReturnDefinition() throws URISyntaxException {
//        // Set Up
//        String word = validWordsToSort[RandomUtils.nextInt(0, validWordsToSort.length - 1)];
//        Set<String> words = new HashSet<>();
//        words.add(word);
//        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();
//
//        // Do Test
//        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
//            .statusCode(HttpStatus.CREATED.value()).body("definitions.size()", is(1))
//            .body("definitions[0].word", is(word.toLowerCase()))
//            .body("definitions[0].definitions.size()", is(4));
//    }
//
//    @Test
//    public void getDefinitions_NotCachedAndCachedWordsGiven_ShouldReturnDefinition() throws URISyntaxException {
//        // Set Up
//        String homeWord = "home";
//        String invalidWord = "klkkl";
//        String word = validWordsToSort[RandomUtils.nextInt(0, validWordsToSort.length - 1)];
//        Set<String> words = new HashSet<>();
//        words.add(word);
//        words.add(homeWord);
//        words.add(invalidWord);
//
//        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();
//
//        // Do Test
//        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
//            .statusCode(HttpStatus.CREATED.value()).body("definitions.size()", is(2));
//    }
//
//    private List<KnownWord> loadWords() {
//        List<String> words = Arrays.asList(validWords);
//        return new KnownWordsBuilder().words(words).build();
//    }
//
//    private List<Definition> buidDefinitionFromFile() {
//        Gson gson = new GsonBuilder().create();
//
//        Type listType = new TypeToken<ArrayList<Definition>>() {
//        }.getType();
//
//        List<Definition> definitions = gson.fromJson(readingTemplateContent(OXFORD_SERVICE_SAMPLE_FILE_NAME), listType);
//        return definitions;
//    }
//
//    private String readingTemplateContent(String fileName) {
//        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//        File file = new File(classLoader.getResource(fileName).getFile());
//        try {
//            return new String(Files.readAllBytes(file.toPath()));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
