package com.basil.teknasiyontrivia.server;

import com.basil.teknasiyontrivia.server.DummySocketResponses;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Basil on 7/30/2018.
 */

public class DummyRestServer extends NanoHTTPD {
    public DummyRestServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(DummySocketResponses.WILD_CARD_RESPONSE);
    }
}
