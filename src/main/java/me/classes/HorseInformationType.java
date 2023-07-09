package me.classes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class HorseInformationType implements PersistentDataType<byte[], HorseInformation> {

    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    public Class<HorseInformation> getComplexType() {
        return HorseInformation.class;
    }

    public byte[] toPrimitive(HorseInformation information, PersistentDataAdapterContext persistentDataAdapterContext) {
        return SerializationUtils.serialize(information);
    }

    public HorseInformation fromPrimitive(byte[] bytes, PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            InputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(is);
            return (HorseInformation) o.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
