package org.herring.core.nfs;


import org.junit.Assert;

import java.util.List;

/**
 * NetworkFileSystemClient에서 사용한 API 종류와, 사용된 Parameter를 Packing 하는 클래스
 * 객체를 생성한 후 makeCommand_ 로 시작하는 method 를 call 함으로써 사용된 api와 파라메터를 정의한다.
 * <p/>
 * User: hyunje
 * Date: 13. 6. 10.
 * Time: 오후 1:35
 */
public class NetworkFileSystemAPIHandler {
    private APIs command;
    private String locate;
    private String data;
    private List<String> dataList;
    private int offset;
    private int size;
    private int linecount;

    public NetworkFileSystemAPIHandler() {
        this.command = null;
        this.locate = null;
        this.data = null;
        this.dataList = null;
        this.offset = -1;
        this.size = -1;
        this.linecount = -1;
    }

    public void makeCommand_putData_locate_data(String locate, String data) {
        Assert.assertNotNull(locate);
        Assert.assertNotNull(data);
        this.command = APIs.putData_locate_data;
        this.locate = locate;
        this.data = data;
    }

    public void makeCommand_putData_locate_dataList(String locate, List<String> dataList) {
        Assert.assertNotNull(locate);
        Assert.assertNotNull(dataList);
        this.command = APIs.putData_locate_datalist;
        this.locate = locate;
        this.dataList = dataList;
    }

    public void makeCommand_getData_locate(String locate) {
        Assert.assertNotNull(locate);
        this.command = APIs.getData_locate;
        this.locate = locate;
    }

    public void makeCommand_getData_locate_offset_size(String locate, int offset, int size) {
        Assert.assertNotNull(locate);
        Assert.assertNotNull(offset);
        Assert.assertNotNull(size);
        this.command = APIs.getData_locate_offset_size;
        this.locate = locate;
        this.offset = offset;
        this.size = size;
    }

    public void makeCommand_getLine_locate_linecount(String locate, int linecount) {
        Assert.assertNotNull(locate);
        Assert.assertNotNull(linecount);
        this.command = APIs.getLine_locate_linecount;
        this.locate = locate;
        this.linecount = linecount;
    }

    public APIs getCommand() {
        return command;
    }

    public String getLocate() {
        return locate;
    }

    public String getData() {
        return data;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }

    public int getLinecount() {
        return linecount;
    }

    public static enum APIs {
        putData_locate_data,
        putData_locate_datalist,
        getData_locate,
        getData_locate_offset_size,
        getLine_locate_linecount
    }
}