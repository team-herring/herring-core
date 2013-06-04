package org.herring.core.protocol.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serializable이 구현된 객체들을 기본 Seriailzation Method로 직렬화하는 Codec 클래스.
 * 많은 경우에 기본으로 사용할 수 있다.
 *
 * @author Chiwan Park
 * @since 0.1
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
