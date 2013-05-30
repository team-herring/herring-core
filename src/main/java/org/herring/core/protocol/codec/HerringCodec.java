package org.herring.core.protocol.codec;

/**
 * @author Chiwan Park
 * @since 0.2
 */
public interface HerringCodec {

    /**
     * 객체를 byte 데이터로 변환하는 메서드다.
     *
     * @param data 변환할 객체
     * @return 인코딩 된 byte 데이터
     */
    public byte[] encode(Object data) throws Exception;

    /**
     * byte 데이터를 프로토콜 정의에 따라 필요한 객체로 변환하는 메서드다.
     *
     * @param packet 전송된 byte 데이터
     * @return 프로토콜 정의에 따라 변환된 객체. 이 객체가 이벤트 리스너 메서드의 데이터 매개변수로 전달된다.
     */
    public Object decode(byte[] packet) throws Exception;
}
