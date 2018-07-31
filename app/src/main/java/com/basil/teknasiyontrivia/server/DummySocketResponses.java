package com.basil.teknasiyontrivia.server;

import java.util.HashMap;

/**
 * Created by Basil on 7/30/2018.
 */

public class DummySocketResponses {
    public static final String WILD_CARD_RESPONSE = "{ \"wildcard_num\": 1, \"total_questions\": 3 }";
    public static final String FIRST_MESSAGE = "{ \"id\": 1, \"question_text\": \"when was the first phone call conducted\", \"options\": [ { \"id\": 1, \"option_text\": \"1876 ;\" }, { \"id\": 2, \"option_text\": \"1890;\" }, { \"id\": 3, \"option_text\": \"1850;\" }, { \"id\": 4, \"option_text\": \"1900;\" } ] }";
    public static final String FIRST_QUESTION = "{ \"message\": \"QUESTION\", \"data\": { \"id\": 1, \"questionText\": \"when was the first phone call conducted\", \"Answers\": [ { \"id\": 1, \"answerText\": \"1876\" }, { \"id\": 2, \"answerText\": \"1890\" }, { \"id\": 3, \"answerText\": \"1850\" }, { \"id\": 4, \"answerText\": \"1900\" } ] } }";
    public static final String SECOND_QUESTION = "{ \"message\": \"QUESTION\", \"data\": { \"id\": 2, \"questionText\": \"When was Turkey founded\", \"Answers\": [ { \"id\": 1, \"answerText\": \"1915\" }, { \"id\": 2, \"answerText\": \"1923\" }, { \"id\": 3, \"answerText\": \"1930\" }, { \"id\": 4, \"answerText\": \"1925\" } ] } }";
    public static final String THIRD_QUESTION = "{ \"message\": \"QUESTION\", \"data\": { \"id\": 2, \"questionText\": \"Who Created Linux\", \"Answers\": [ { \"id\": 1, \"answerText\": \"Bill Gates\" }, { \"id\": 2, \"answerText\": \"Linus Torvalds\" }, { \"id\": 3, \"answerText\": \"Richard Stallman\" }, { \"id\": 4, \"answerText\": \"Dennis Ritchie\" } ] } }";
    public static final String RESULT_MESSAGE = "{ \"message\": \"RESULT\", \"data\": { \"standings\": [ { \"standing\": 1, \"name\": \"Burak\" }, { \"standing\": 2, \"name\": \"YOU\" }, { \"standing\": 3, \"name\": \"Ercan\" }, { \"standing\": 4, \"name\": \"Ahmet\" } ] } }";
    public static HashMap<Integer,String>  questionResponses = new HashMap<>();
    static {
        questionResponses.put(1,FIRST_QUESTION);
        questionResponses.put(2,SECOND_QUESTION);
        questionResponses.put(3,THIRD_QUESTION);
    }
    }
