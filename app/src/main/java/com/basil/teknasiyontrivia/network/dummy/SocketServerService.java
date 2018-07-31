package com.basil.teknasiyontrivia.network.dummy;

import com.basil.teknasiyontrivia.server.EmbeddedSocketServer;

import java.io.IOException;

/**
 * Created by Basil on 7/30/2018.
 */

public class SocketServerService {
    EmbeddedSocketServer mServer;
    int mPort;

    public SocketServerService(int port){
        this.mPort = port;
    }

    void start(){
        mServer = new EmbeddedSocketServer(mPort,false);
        try {
            mServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
