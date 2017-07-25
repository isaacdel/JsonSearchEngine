import Constants.ApplicationConstants;
import Validators.FieldValidator;
import Validators.FileValidator;
import Validators.SetupValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.mongodb.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

class Main {
    public static ApplicationConstants applicationConstants = new ApplicationConstants();
    public static Properties prop = new Properties();
    @Parameter(names = {"--field", "-f"}, validateWith = FieldValidator.class)
    String field;
    @Parameter(names = {"--value", "-v"})
    String value;
    @Parameter(names = {"--file", "-j"}, validateWith = FileValidator.class)
    String file;
    @Parameter(names = {"--setup", "-s"}, validateWith = SetupValidator.class)
    String setup;


    public static void main(String... argv) throws Exception {
        Main main = new Main();
        InputStream input;
        String mongodbHost;
        int mongodbPort;
        String mongodbDbName;
        try {
            input = main.getClass().getResourceAsStream(applicationConstants.CONFIG_PROPERTIES);
            prop.load(input);
            mongodbPort = Integer.valueOf(prop.getProperty(applicationConstants.MONGODB_PORT));
            mongodbHost = prop.getProperty(applicationConstants.MONGODB_HOST);
            mongodbDbName = prop.getProperty(applicationConstants.MONGODB_DB_NAME);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("Mongodb properties not set in properties file");
        }
        MongoClient mongo = new MongoClient(mongodbHost, mongodbPort);
        DB db = mongo.getDB(mongodbDbName);

        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run(mongo, db);
    }

    /**
     * Handle user input. save the data once by "setup" and them query.
     *
     * @param mongo mongodb instance
     * @param db    db instance
     * @throws Exception
     */
    private void run(MongoClient mongo, DB db) {
        if (setup != null) {
            if (setup.equals(applicationConstants.TRUE)) {
                Main main = new Main();
                main.insertRecords(db);
            }
        } else if (file != null) {
            if (file.equals(applicationConstants.USERS)) {
                runSearch(db, applicationConstants.USERS);
            } else if (file.equals(applicationConstants.TICKETS)) {
                runSearch(db, applicationConstants.TICKETS);
            } else if (file.equals(applicationConstants.ORGANIZATIONS)) {
                runSearch(db, applicationConstants.ORGANIZATIONS);
            }
        }
        mongo.close();
    }

    private void runSearch(DB db, String model) {
        DBCollection col = db.getCollection(model);
        JsonSearchTool.searchCollection(col, field, value);
    }

    public void insertRecords(DB db) {
        insertAllRecords(db, applicationConstants.USERS);
        insertAllRecords(db, applicationConstants.TICKETS);
        insertAllRecords(db, applicationConstants.ORGANIZATIONS);
    }

    private void insertAllRecords(DB db, String model) {
        DBCollection col = db.getCollection(model);
        BasicDBObject document = new BasicDBObject();
        try {
            List<BasicDBObject> objects = JsonSearchTool.readStream(findResource(model));
            col.remove(document);
            col.insert(objects);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String findResource(String model) {
        return prop.getProperty(model);
    }


}