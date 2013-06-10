package org.herring.core.protocol.samples;

import org.herring.core.protocol.ClientComponent;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;
import org.herring.core.protocol.handler.MessageHandler;
import org.herring.core.protocol.handler.SyncMessageHandler;

/**
 * << Description >>
 * User: hyunje
 * Date: 13. 6. 10.
 * Time: 오후 1:01
 */
public class NetworkFileSystemClientSample {
    private static final String host = "127.0.0.1";
    private static final int port = 9300;

    private ClientComponent clientComponent;
    public static void main(String[] args) throws InterruptedException {
        NetworkFileSystemClientSample nfsConnector = new NetworkFileSystemClientSample();
        HerringCodec codec = new SerializableCodec();

        MessageHandler messageHandler = new SyncMessageHandler();

        System.out.println("Network File System에 연결합니다.");
        nfsConnector.clientComponent = new ClientComponent(host,port,codec, messageHandler);

        System.out.println("NFS에 메시지를 전송합니다.");
        nfsConnector.clientComponent.getNetworkContext().sendObject("Hello NFS?!");
        System.out.println("NFS의 응답을 기다립니다.");
        nfsConnector.clientComponent.getNetworkContext().waitUntil("Recieved Message");

        System.out.println("NFS에서 전송된 메시지");
        System.out.println(nfsConnector.clientComponent.getNetworkContext().getMessageFromQueue());

        nfsConnector.clientComponent.stop();
    }
}
