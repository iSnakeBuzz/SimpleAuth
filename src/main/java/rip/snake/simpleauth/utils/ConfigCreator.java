package rip.snake.simpleauth.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Cleanup;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

@Getter
public class ConfigCreator {

    private final Path pluginFolder;

    private YamlDocument config;
    private YamlDocument messages;

    public ConfigCreator(Path pluginFolder) {
        this.pluginFolder = pluginFolder;
    }

    public void createConfig() {
        try {
            @Cleanup InputStream resourceAsStream = getClass().getResourceAsStream("/config.yml");
            config = YamlDocument.create(
                    pluginFolder.resolve("config.yml").toFile(),
                    Objects.requireNonNull(resourceAsStream, "Could not find config.yml in the jar!"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()
            );

            messages = YamlDocument.create(
                    pluginFolder.resolve("messages.yml").toFile(),
                    Objects.requireNonNull(getClass().getResourceAsStream("/messages.yml"), "Could not find messages.yml in the jar!"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()
            );

            config.update();
            config.save();

            messages.update();
            messages.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
