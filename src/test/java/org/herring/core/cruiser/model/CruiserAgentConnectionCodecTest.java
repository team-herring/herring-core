package org.herring.core.cruiser.model;

import java.util.Arrays;

/**
 * << Description >>
 * User: hyunje
 * Date: 13. 6. 1.
 * Time: 오전 4:12
 */
public class CruiserAgentConnectionCodecTest {
    public static void main(String[] args) throws Exception {
        CruiserAgentConnectionCodecTest test = new CruiserAgentConnectionCodecTest();
        test.testEncode();
    }
    public void testEncode() throws Exception {
        String str = "StringTest";
        CruiserAgentConnectionCodec codec = new CruiserAgentConnectionCodec();
        System.out.println(Arrays.toString(codec.encode(str)));
    }
}
