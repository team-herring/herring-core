package org.herring.protocol;

import java.io.*;

/**
 * Java Serialization 기능을 이용해 일반 Object의 전송, 수신을 처리하는 Handler 클래스. 일반적인 Object Message를 전송할 때 사용한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public abstract class SerializableDataHandler implements DataHandler<Object> {

    @Override
    public byte[] encode(Object data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(data);

        return outputStream.toByteArray();
    }

    @Override
    public Object decode(byte[] packet) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return objectInputStream.readObject();
    }
}
