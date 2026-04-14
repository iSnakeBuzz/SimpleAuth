package rip.snake.simpleauth.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import rip.snake.simpleauth.player.AuthPlayer;

public class AuthPlayerCodec implements Codec<AuthPlayer> {

    @Override
    public AuthPlayer decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String uniqueId = null;
        String username = null;
        String last_server = null;
        String last_ip = null;
        long last_login = 0;
        String hashed_password = null;
        boolean isPremium = false;
        boolean isBedrock = false;

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            switch (name) {
                case "_id":          reader.readObjectId(); break;
                case "uniqueId":     uniqueId = reader.readString(); break;
                case "username":     username = reader.readString(); break;
                case "last_server":  last_server = reader.readString(); break;
                case "last_ip":      last_ip = reader.readString(); break;
                case "last_login":   last_login = reader.readInt64(); break;
                case "hashed_password": hashed_password = reader.readString(); break;
                case "isPremium":    isPremium = reader.readBoolean(); break;
                case "isBedrock":    isBedrock = reader.readBoolean(); break;
                default:             reader.skipValue(); break;
            }
        }
        reader.readEndDocument();

        return new AuthPlayer(uniqueId, username, last_server, last_ip, last_login, hashed_password, isPremium, isBedrock);
    }

    @Override
    public void encode(BsonWriter writer, AuthPlayer value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("uniqueId", value.getUniqueId().toString());
        writer.writeString("username", value.getUsername().toLowerCase());
        writer.writeString("last_server", value.getLastServer());
        writer.writeString("last_ip", value.getLastIP());
        writer.writeInt64("last_login", value.getLastLogin());
        writer.writeString("hashed_password", value.getHashedPassword());
        writer.writeBoolean("isPremium", value.isPremium());
        writer.writeBoolean("isBedrock", value.isBedrock());
        writer.writeEndDocument();
    }


    @Override
    public Class<AuthPlayer> getEncoderClass() {
        return AuthPlayer.class;
    }
}
