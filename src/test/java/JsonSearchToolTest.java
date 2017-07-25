import Constants.ApplicationConstants;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.*;

public class JsonSearchToolTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    public static Properties prop = new Properties();
    public static DB db;
    public static Gson gson;
    public static ApplicationConstants applicationConstants = new ApplicationConstants();

    @Before
    public void setUpStreams() throws IOException {

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        JsonSearchToolTest jsonSearchToolTest = new JsonSearchToolTest();
        InputStream input = null;
        String mongodbHost;
        int mongodbPort;
        String mongodbDbName;
        try {
            input = jsonSearchToolTest.getClass().getResourceAsStream(applicationConstants.CONFIG_PROPERTIES);
            prop.load(input);
            mongodbPort = Integer.valueOf(prop.getProperty(applicationConstants.MONGODB_PORT));
            mongodbHost = prop.getProperty(applicationConstants.MONGODB_HOST);
            mongodbDbName = prop.getProperty(applicationConstants.MONGODB_DB_NAME);
        } catch (IOException ex) {
            throw new IOException("Mongodb properties not set in properties file");
        }
        MongoClient mongo = new MongoClient(mongodbHost, mongodbPort);
        db = mongo.getDB(mongodbDbName);
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    //Test case when field and value are provided, one result
    public void testSearchUsersCollection() {
        DBCollection col = db.getCollection(applicationConstants.USERS);
        JsonSearchTool jsonSearchTool = new JsonSearchTool();
        try {
            jsonSearchTool.searchCollection(col, "name", "Faulkner Holcomb");
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
        DBObject dbObject = (DBObject) JSON
                .parse(outContent.toString());
        assertEquals(dbObject.get("_id"), 49.0);
    }

    @Test
    //Test case when field and value are provided, multiple results
    public void testSearchTicketsCollection() {
        DBCollection col = db.getCollection(applicationConstants.TICKETS);
        JsonSearchTool jsonSearchTool = new JsonSearchTool();
        try {
            jsonSearchTool.searchCollection(col, "tags", "Idaho");
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
        int resSize = 14;
        assertEquals(resSize, StringUtils.countMatches(outContent.toString(), "\"_id\""));
    }

    @Test
    //Test case when field is provided and value is not provided
    public void testSearchOrganizationsCollection() {
        DBCollection col = db.getCollection(applicationConstants.ORGANIZATIONS);
        JsonSearchTool jsonSearchTool = new JsonSearchTool();
        try {
            jsonSearchTool.searchCollection(col, "_id", null);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
        int resSize = 26;
        assertEquals(resSize, StringUtils.countMatches(outContent.toString(), "\"_id\""));
    }

    @Test
    //Test case when field and value are not provided
    public void testSearchOrganizationsNullFieldCollection() {
        DBCollection col = db.getCollection(applicationConstants.ORGANIZATIONS);
        JsonSearchTool jsonSearchTool = new JsonSearchTool();
        try {
            jsonSearchTool.searchCollection(col, null, null);
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
}