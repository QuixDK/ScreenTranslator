package logger;

import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    Date date = new Date();
    FileHandler fh;
    String directoryName = "/logs/";
    Path path = Paths.get(System.getProperty("user.dir") + directoryName);
    Logger logger;
    public Logger GetLogger() {
        if (logger == null) {
            makeLogger();
            return logger;
        }
        else return logger;
    }
    private void makeLogger() {
        try {
            // This block configure the logger with handler and formatter
            File f = new File(String.valueOf(path));
            if(f.exists() && !f.isDirectory()) {
                Files.createDirectory(path);
            }

            File file = new File(System.getProperty("user.dir") + directoryName + date.getTime());
            this.logger = Logger.getLogger(file.getName());
            fh = new FileHandler(file.getAbsolutePath());

            this.logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);


        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


}
