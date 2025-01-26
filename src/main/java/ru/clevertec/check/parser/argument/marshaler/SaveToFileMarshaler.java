package main.java.ru.clevertec.check.parser.argument.marshaler;

import java.nio.file.Path;

public class SaveToFileMarshaler implements ArgumentMarshaler {

    private Path saveFilePath;

    @Override
    public void addValue(Object... values) {
        saveFilePath = Path.of((String) values[0]);
    }

    @Override
    public Object getArgumentValue() {
        return saveFilePath;
    }
}
