package org.herring.core.cruiser.model;

import java.io.Serializable;

/**
 * Cruiser 와 Client 가 통신 할 때 사용하는 객체.
 * CruiserClientConnection 객체는 Client 가 Cruiser 로 연결을 요청할 때 사용한다.
 * User: hyunje
 * Date: 13. 5. 26.
 * Time: 오전 12:12
 */
public class CruiserClientConnection implements Serializable {
    private static final long serialVersionUID = -847248108392015L;
    boolean isParsed;
    String rowDelimiter;
    String columnDelimiter;
    String contentDelimiter;
    private String clientUUID;

    public CruiserClientConnection(String clientUUID, boolean isParsed, String rowDelimiter, String columnDelimiter, String contentDelimiter) {
        this.clientUUID = clientUUID;
        this.isParsed = isParsed;
        this.rowDelimiter = rowDelimiter;
        this.columnDelimiter = columnDelimiter;
        this.contentDelimiter = contentDelimiter;

        validate();
    }

    /*
     * 제대로 생성 하였나 체크
     * 1. uuid가 정상적이지 않으면 false
     * 2. parsing 된 데이터가 전송 될 것인데 delimiter 가 정의되지 않은 경우
     * 3. parsing 되지 않은 데이터가 전송 될 것인데, 필요없는 delimiter 가 정의되어 있는 경우
     *
     * @return true : 정상적인 입력값, false : 비정상적인 입력 값
     */
    private boolean validate() {
        if (clientUUID == null) {
            System.out.println("ClientUUID가 제대로 정의되지 않았습니다.");
            return false;
        }

        if (isParsed) {
            if (rowDelimiter == null || columnDelimiter == null || contentDelimiter == null) {
                System.out.println("Delimiter가 부족합니다.");
                return false;
            }
        } else {
            if (rowDelimiter == null || columnDelimiter != null || contentDelimiter != null) {
                System.out.println("필요 없는Delimiter가 정의되었습니다.");
                return false;
            }
        }
        return true;
    }
}
