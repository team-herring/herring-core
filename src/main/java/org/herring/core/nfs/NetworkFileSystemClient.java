package org.herring.core.nfs;

import org.herring.core.protocol.ClientComponent;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;
import org.herring.core.protocol.handler.MessageHandler;
import org.herring.core.protocol.handler.SyncMessageHandler;

import java.util.List;

/**
 * << Description >>
 * User: hyunje
 * Date: 13. 6. 9.
 * Time: 오전 11:21
 */
public class NetworkFileSystemClient {
    private ClientComponent clientComponent;

    public NetworkFileSystemClient(String host, int port) {
        System.out.println("ClientComponent Initialize");
        HerringCodec codec = new SerializableCodec();
        MessageHandler messageHandler = new SyncMessageHandler();
        clientComponent = new ClientComponent(host, port, codec, messageHandler);
    }

    public void openConnection() throws Exception {
        clientComponent.start();
    }

    public void closeConnection() throws Exception {
        clientComponent.stop();
    }

    public void putData(String locate, String data) throws InterruptedException {
        //Command에 맞게 NetworkFileSystemAPIHandler 작성
        NetworkFileSystemAPIHandler apiHandler = new NetworkFileSystemAPIHandler();
        apiHandler.makeCommand_putData_locate_data(locate, data);

        //APIHandler를 Herring Codec으로 Encoding 하여 ClientComponent를 통해 Server로 전송해야 한다.
        clientComponent.getNetworkContext().sendObject(apiHandler);
        //전송한 객체에 대한 서버에서의 처리 결과를 기다려야 한다.
        clientComponent.getNetworkContext().waitUntil("received");
        //서버에서 처리된 결과를 Queue에서 꺼내온다.
        System.out.println("Processed on NFS : "+clientComponent.getNetworkContext().getMessageFromQueue());
    }

    public void putData(String locate, List<String> dataList) throws InterruptedException {
        NetworkFileSystemAPIHandler apiHandler = new NetworkFileSystemAPIHandler();
        apiHandler.makeCommand_putData_locate_dataList(locate, dataList);

        clientComponent.getNetworkContext().sendObject(apiHandler);
        clientComponent.getNetworkContext().waitUntil("received");
        System.out.println("Processed on NFS : "+clientComponent.getNetworkContext().getMessageFromQueue());
    }

    public String getData(String locate) throws InterruptedException {
        NetworkFileSystemAPIHandler apiHandler = new NetworkFileSystemAPIHandler();
        apiHandler.makeCommand_getData_locate(locate);

        clientComponent.getNetworkContext().sendObject(apiHandler);
        clientComponent.getNetworkContext().waitUntil("received");

        String received = new String((byte[])clientComponent.getNetworkContext().getMessageFromQueue());
        System.out.println("Processed result on NFS : "+received);
        return received;
    }

    public String getData(String locate, int offset, int size) throws InterruptedException {
        NetworkFileSystemAPIHandler apiHandler = new NetworkFileSystemAPIHandler();
        apiHandler.makeCommand_getData_locate_offset_size(locate,offset,size);

        clientComponent.getNetworkContext().sendObject(apiHandler);
        clientComponent.getNetworkContext().waitUntil("received");

        String received = new String((byte[])clientComponent.getNetworkContext().getMessageFromQueue());
        System.out.println("Processed result on NFS : "+received);
        return received;
    }

    public String getLine(String locate, int linecount) throws InterruptedException {
        NetworkFileSystemAPIHandler apiHandler = new NetworkFileSystemAPIHandler();
        apiHandler.makeCommand_getLine_locate_linecount(locate,linecount);

        clientComponent.getNetworkContext().sendObject(apiHandler);
        clientComponent.getNetworkContext().waitUntil("received");

        String received = new String((byte[])clientComponent.getNetworkContext().getMessageFromQueue());
        System.out.println("Processed result on NFS : "+received);
        return received;
    }
}
