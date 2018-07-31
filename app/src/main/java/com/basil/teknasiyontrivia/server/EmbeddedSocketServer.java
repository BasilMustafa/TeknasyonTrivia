package com.basil.teknasiyontrivia.server;

import com.basil.teknasiyontrivia.network.GameService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.iki.elonen.NanoWSD;


/**
 * @author Paul S. Hawke (paul.hawke@gmail.com) On: 4/23/14 at 10:31 PM
 */
public class EmbeddedSocketServer extends NanoWSD {

    /**
     * logger to log to.
     */
    private static final Logger LOG = Logger.getLogger(EmbeddedSocketServer.class.getName());

    private final boolean debug;

    static HashMap<Integer, Integer> answers = new HashMap();

    public EmbeddedSocketServer(int port, boolean debug) {
        super(port);
        this.debug = debug;
        answers.put(1, 1);
        answers.put(2, 2);
        answers.put(3, 2);
    }

    @Override
    protected WebSocket openWebSocket(IHTTPSession handshake) {
        return new DebugWebSocket(this, handshake);
    }

    private static class DebugWebSocket extends WebSocket {

        private final EmbeddedSocketServer server;

        public DebugWebSocket(EmbeddedSocketServer server, IHTTPSession handshakeRequest) {
            super(handshakeRequest);
            this.server = server;
        }

        @Override
        protected void onOpen() {
            try {
                currentQuestionIndex = 1;
                send(DummySocketResponses.questionResponses.get(currentQuestionIndex));
                currentQuestionIndex++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onClose(WebSocketFrame.CloseCode code, String reason, boolean initiatedByRemote) {
            if (server.debug) {
                System.out.println("C [" + (initiatedByRemote ? "Remote" : "Self") + "] " + (code != null ? code : "UnknownCloseCode[" + code + "]")
                        + (reason != null && !reason.isEmpty() ? ": " + reason : ""));
            }
        }

        @Override
        protected void onMessage(final WebSocketFrame message) {
            try {
                String content = message.getTextPayload();
                final JSONObject jsonObject = new JSONObject(content);
                if (jsonObject.getString("message").equals("ANSWER")) {
                    JSONObject answerObjet = new JSONObject(jsonObject.getString("data"));
                    if (answerObjet.getInt("answer_id") == answers.get(answerObjet.getInt("question_id"))) {
                        JSONObject resultObject = new JSONObject();
                        resultObject.put("message", GameService.RIGHT_ANSWER);
                        //end(resultObject.toString());
                        send(resultObject.toString());
                        //to emulate other players answering the question
                        Thread.sleep(2000);
                        try {
                            if (currentQuestionIndex > answers.size()) {
                                send((new JSONObject(DummySocketResponses.RESULT_MESSAGE).toString()));
                            } else {

                                JSONObject jsonObject1 = new JSONObject(DummySocketResponses.questionResponses.get(currentQuestionIndex));
                                currentQuestionIndex++;
                                //send(jsonObject.toString());
                                send(jsonObject1.toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (CharacterCodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JSONObject resultObject = new JSONObject();
                        resultObject.put("message", GameService.WRONG_ANSWER);
                        JSONObject dataObject = new JSONObject();
                        dataObject.put("wrong_answer", answerObjet.getInt("answer_id"));
                        dataObject.put("right_answer", answers.get(answerObjet.getInt("question_id")));
                        resultObject.put("data", dataObject);
                        //WebSocketFrame  response = new WebSocketFrame(message.getOpCode(),false,resultObject.toString());
                        send(resultObject.toString());
                    }
                } else if (jsonObject.getString("message").equals(GameService.WILD_CARD)) {
                    if (currentQuestionIndex > answers.size()) {
                        send((new JSONObject(DummySocketResponses.RESULT_MESSAGE).toString()));
                    } else {
                        JSONObject jsonObject1 = new JSONObject(DummySocketResponses.questionResponses.get(currentQuestionIndex));
                        currentQuestionIndex++;
                        //send(jsonObject.toString());
                        send(jsonObject1.toString());
                    }
                } else if (jsonObject.getString("message").equals(GameService.REJECT_WILD_CARD)) {
                    //only viewer, send question after one another
                    streamCompetetion();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (CharacterCodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPong(WebSocketFrame pong) {
            if (server.debug) {
                System.out.println("P " + pong);
            }
        }

        @Override
        protected void onException(IOException exception) {
            EmbeddedSocketServer.LOG.log(Level.SEVERE, "exception occured", exception);
        }

        @Override
        protected void debugFrameReceived(WebSocketFrame frame) {
            if (server.debug) {
                System.out.println("R " + frame);
            }
        }

        @Override
        protected void debugFrameSent(WebSocketFrame frame) {
            if (server.debug) {
                System.out.println("S " + frame);
            }
        }

        private void streamCompetetion() {
            try {
                while (currentQuestionIndex <= answers.size()) {
                    JSONObject question_object = new JSONObject(DummySocketResponses.questionResponses.get(currentQuestionIndex));
                    currentQuestionIndex++;
                    send(question_object.toString());
                    Thread.sleep(2000);
                    JSONObject streamedAnswerObject = new JSONObject();
                    streamedAnswerObject.put("message", GameService.ANSWER_STREAMED);
                    streamedAnswerObject.put("answer_id", answers.get(question_object.getJSONObject("data").getInt("id")));
                    send(streamedAnswerObject.toString());
                    Thread.sleep(2000);
                }
                send((new JSONObject(DummySocketResponses.RESULT_MESSAGE).toString()));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    static int currentQuestionIndex = 1;
}