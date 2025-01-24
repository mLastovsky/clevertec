package main.java.ru.clevertec.check.parser.argument.marshaler;

import java.nio.file.Path;

public class PathToFileMarshaler implements ArgumentMarshaler{

    private Path pathToFile;

    @Override
    public void addValue(Object... values) {
        pathToFile = (Path) values[0];
    }

    @Override
    public Object getArgumentValue() {
        return pathToFile;
    }
}
