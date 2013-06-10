package org.herring.core.nfs;

import org.herring.core.protocol.ClientComponent;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;
import org.herring.core.protocol.handler.MessageHandler;

/**
 * << Description >>
 * User: hyunje
 * Date: 13. 6. 9.
 * Time: 오전 11:21
 */
public class NetworkFileSystemClient {
    private String host;
    private int port;

    private ClientComponent clientComponent;
    private HerringCodec codec;
    private MessageHandler messageHandler;

    public NetworkFileSystemClient(String host, int port){
        this.host = host;
        this.port = port;

        System.out.println("ClientComponent Initialize");
        codec = new SerializableCodec();
        messageHandler = new NetworkFileSystemMessageHandler();
        clientComponent = new ClientComponent(this.host,this.port, codec,messageHandler);
    }

    public void openConnection() throws Exception {
        clientComponent.start();
    }

    public void closeConnection() throws Exception{
        clientComponent.stop();
    }

    public void putData(String locate, String data){

    }

}
