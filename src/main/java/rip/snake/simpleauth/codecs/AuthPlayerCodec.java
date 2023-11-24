package rip.snake.simpleauth.codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import rip.snake.simpleauth.player.AuthPlayer;

public class AuthPlayerCodec implements Codec<AuthPlayer> {

    @Override
    public AuthPlayer decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readObjectId("_id");

        String uniqueId = reader.readString("uniqueId");
        String username = reader.readString("username");
        String last_server = reader.readString("last_server");
        String last_ip = reader.readString("last_ip");
        long last_login = reader.readInt64("last_login");
        String hashed_password = reader.readString("hashed_password");
        boolean isPremium = reader.readBoolean("isPremium");
        reader.readEndDocument();

        return new AuthPlayer(uniqueId, username, last_server, last_ip, last_login, hashed_password, isPremium);
    }

    @Override
    public void encode(BsonWriter writer, AuthPlayer value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("uniqueId", value.getUniqueId().toString());
        writer.writeString("username", value.getUsername().toLowerCase());
        writer.writeString("last_server", value.getLast_server());
        writer.writeString("last_ip", value.getLast_ip());
        writer.writeInt64("last_login", value.getLast_login());
        writer.writeString("hashed_password", value.getHashedPassword());
        writer.writeBoolean("isPremium", value.isPremium());
        writer.writeEndDocument();
    }


    @Override
    public Class<AuthPlayer> getEncoderClass() {
        return AuthPlayer.class;
    }
}
