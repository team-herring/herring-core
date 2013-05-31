package org.herring.core.cruiser.model;

import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Cruiser와 Client가 통신을 수행하기 이전에, Client 는 Cruiser에 연결을 요청해야 한다.
 * 그 연결 요쳉에 사용 될 Codec이다.
 * 가장 앞 부분에 Cruiser와 Client가 미리 정의한 Command 번호가 있고,
 * 그 뒤에 Client가 해당 연결에서 사용 할 Agent의 UUID와, Delimiter에 대한 정보가 담긴 객체가 온다.
 * <p/>
 * <<Command>>
 * 1. 연결 요청
 * 2. 연결 승인
 * 3. 데이터 전송
 * <p/>
 * CruiserAgentConnectionCodec은 Agent가 Cruiser에 연결 요청을 하는 것이기 때문에 Command는 1번이다.
 * Command는 CruiserAgentConnectionObject에 포함되어있다.
 * <p/>
 * <p/>
 * ----------->> 수정
 * <p/>
 * <p/>
 * Cruiser와 Agent가 통신을 하는 도중에 사용하는 Codec 객체.
 * Command 1은 int Byte[] 이후에 오는 데이터가 Object임을 가리키며,
 * Command 2는 int Byte[] 이후에 오는 데이터가 String을 Byte[] 로 변형시킨 스트림임을 가리킨다.
 * User: hyunje
 * Date: 13. 5. 28.
 * Time: 오전 8:53
 */
public class CruiserAgentConnectionCodec implements HerringCodec {
    /**
     * Agent에서 Cruiser로 연결을 요청할 때 사용하는 데이터를 Encoding 하는 함수
     * Object o 는
     * Command가 1인 경우에는 @link{CruiserAgentConnectionObject}
     * Command가 2인 경우에는 String 형태의 객체이다.
     *
     * @param o Object
     * @return encoding 된 byteArray
     * @throws Exception
     */
    @Override
    public byte[] encode(Object o) throws Exception {
        if (o == null) throw new Exception();

        ByteBuffer buffer;

        //CruiserAgentConnectionObject 인 경우
        if (!(o instanceof String)) {
            byte[] objectByteArr = new SerializableCodec().encode(o);
            buffer = ByteBuffer.allocate(objectByteArr.length + 4);
            buffer.putInt(0, 1); //Command = 1
            buffer.position(4);
            buffer.put(objectByteArr);
        } else {
            byte[] stringByteArr = ((String) o).getBytes(Charset.forName("UTF-8"));
            buffer = ByteBuffer.allocate(stringByteArr.length + 4);
            buffer.putInt(0, 2); //Command = 1
            buffer.position(4);
            buffer.put(stringByteArr);

        }

        return buffer.array();

        /*
        CruiserAgentConnectionObject connectionObject;
        try{
            connectionObject = (CruiserAgentConnectionObject)o;
        } catch (ClassCastException e){
            System.out.println("Object o cannot be casted to CruiserAgentConnectionObject");
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(connectionObject);

        return outputStream.toByteArray();
        */
    }

    /**
     * Encoding 되어서 전송된 @link{CruiserAgentConnectionObject}를 Decoding
     * <p/>
     * 전송된 Byte[] 를 decoding
     * 앞 4바이트는 Command.
     * 그 뒤는 데이터.
     *
     * @param bytes 전송된 Bytes
     * @return Object
     * @throws Exception
     */
    @Override
    public Object decode(byte[] bytes) throws Exception {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return objectInputStream.readObject();
    }
}
