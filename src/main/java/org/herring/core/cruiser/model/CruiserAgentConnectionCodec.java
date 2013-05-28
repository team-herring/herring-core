package org.herring.core.cruiser.model;

import org.herring.protocol.codec.HerringCodec;

import java.io.*;

/**
 * Cruiser와 Client가 통신을 수행하기 이전에, Client 는 Cruiser에 연결을 요청해야 한다.
 * 그 연결 요쳉에 사용 될 Codec이다.
 * 가장 앞 부분에 Cruiser와 Client가 미리 정의한 Command 번호가 있고,
 * 그 뒤에 Client가 해당 연결에서 사용 할 Agent의 UUID와, Delimiter에 대한 정보가 담긴 객체가 온다.
 *
 * <<Command>>
 *     1. 연결 요청
 *     2. 연결 승인
 *     3. 데이터 전송
 *
 * CruiserAgentConnectionCodec은 Agent가 Cruiser에 연결 요청을 하는 것이기 때문에 Command는 1번이다.
 * Command는 CruiserAgentConnectionObject에 포함되어있다.
 * User: hyunje
 * Date: 13. 5. 28.
 * Time: 오전 8:53
 */
public class CruiserAgentConnectionCodec implements HerringCodec, Serializable{
    private static final long serialVersionUID = -8683692019372094L;

    /**
     * Agent에서 Cruiser로 연결을 요청할 때 사용하는 데이터를 Encoding 하는 함수
     * Object o 는 @link{CruiserAgentConnectionObject}이어야 한다.
     * @param o @link{CruiserAgentConnectionObject}
     * @return encoding 된 byteArray
     * @throws Exception
     */
    @Override
    public byte[] encode(Object o) throws Exception {
        if(o == null) throw new Exception();

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
    }

    /**
     * Encoding 되어서 전송된 @link{CruiserAgentConnectionObject}를 Decoding
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
