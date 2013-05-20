package org.herring.protocol.tests;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedByteChannel;
import org.herring.protocol.DataHandler;
import org.herring.protocol.NetworkContext;
import org.herring.protocol.RawPacketHandler;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

/**
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkHandlerTest {
    @Test
    public void testNetworkHandler() throws Exception {
        final byte[] sendedData = new byte[] {4, 64, 126, 15};

        EmbeddedByteChannel channel = new EmbeddedByteChannel(new RawPacketHandler(new DataHandler<byte[]>() {
            @Override
            public void dataReceived(NetworkContext ctx, byte[] data) {
                Assert.assertEquals(sendedData.length, data.length);

                System.out.println("received data length: " + data.length);
                for (int i = 0, length = data.length; i < length; ++i) {
                    Assert.assertEquals(sendedData[i], data[i]);
                    System.out.println("data " + i + " : " + sendedData[i] + ", " + data[i]);
                }
            }

            @Override
            public void connectionReady(NetworkContext ctx) {
                System.out.println("Connection Ready");
            }

            @Override
            public void sendComplete(NetworkContext ctx) {
                System.out.println("Send Complete");
            }

            @Override
            public byte[] encode(byte[] data) throws IOException {
                return data;
            }

            @Override
            public byte[] decode(byte[] packet) throws IOException, ClassNotFoundException {
                return packet;
            }
        }));

        channel.writeInbound(Unpooled.copyInt(4));
        channel.writeInbound(Unpooled.copiedBuffer(sendedData));
    }
}
