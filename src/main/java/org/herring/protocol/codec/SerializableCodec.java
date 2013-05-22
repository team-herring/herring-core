package org.herring.protocol.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Chiwan Park
 * @since 0.2
 */
public class SerializableCodec implements HerringCodec {
    @Override
    public byte[] encode(Object data) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(data);

        return outputStream.toByteArray();
    }

    @Override
    public Object decode(byte[] packet) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return objectInputStream.readObject();
    }
}
