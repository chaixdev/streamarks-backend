package be.chaixdev.streamarksbackend.utils;


import org.bitcoinj.core.Base58;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtil {

    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        return shortUUID(uuid);
    }

    protected static String shortUUID(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        // Compared with Base64, the following similar-looking letters are omitted: 0 (zero), O (capital o),
        // I (capital i) and l (lower case L) as well as the non-alphanumeric characters + (plus) and / (slash).
        return Base58.encode(byteBuffer.array());
    }
}