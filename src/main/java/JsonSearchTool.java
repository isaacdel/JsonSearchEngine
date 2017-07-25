import java.io.*;
import java.util.LinkedList;
import java.util.List;

import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mongodb.*;

public class JsonSearchTool {

    public static final String UTF_8 = "UTF-8";

    /**
     * Search a collection/Json for the given params
     *
     * @param col   the collection to search
     * @param field the json field
     * @param value the json field's value (not required)
     * @throws IllegalArgumentException
     */
    public static void searchCollection(DBCollection col, String field, String value) throws IllegalArgumentException {
        if (field == null)
            throw new IllegalArgumentException("Field cannot be null!");
        DBCursor cursor;
        DBObject cursorOne = col.findOne(new BasicDBObject(field, new BasicDBObject("$exists", true)));
        if (cursorOne != null) {
            Object fieldValueObject = cursorOne.get(field);
            cursor = findInDB(col, field, value, fieldValueObject);
            filterAndPrintResults(field, value, cursor, fieldValueObject);
        }
    }

    /**
     * Filter and print the results for search by type conversion or explicitly
     *
     * @param field            to be searched
     * @param value            to be searched
     * @param cursor           results of search holder
     * @param fieldValueObject to be compared
     */
    private static void filterAndPrintResults(String field, String value, DBCursor cursor, Object fieldValueObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            while (cursor.hasNext()) {
                DBObject dbobj = cursor.next();
                if (isStringOrList(fieldValueObject) || value == null) {
                    System.out.println(gson.toJson(dbobj));
                } else {
                    if (String.valueOf(dbobj.get(field)).equals(value)) {
                        System.out.println(gson.toJson(dbobj));
                    }
                }
            }
        } finally {
            cursor.close();
        }
    }

    private static DBCursor findInDB(DBCollection col, String field, String value, Object fieldValueObject) {
        DBCursor cursor;
        if (value == null) {
            //if value is null than return all jsons that have that field
            cursor = col.find(new BasicDBObject(field, new BasicDBObject("$exists", true)));
        } else {
            //if the value of the searched field is a string or a string list do a simple search
            if (isStringOrList(fieldValueObject)) {
                cursor = col.find(new BasicDBObject(field, value));
            } else {
                //if the value of the searched field is not a string or a string list compare by matching
                cursor = col.find(new BasicDBObject("$where", "\'" + value + "\'.match(this." + field + ")"));
            }
        }
        return cursor;
    }

    private static boolean isStringOrList(Object o) {
        return o instanceof List || o instanceof String;
    }

    /**
     * Reads the json file to be saved to db in chunks (assuming it can be a large file)
     *
     * @param filename path to json
     * @return a list of the json objects in the stored json array
     */
    public static List<BasicDBObject> readStream(String filename) throws IOException {
        try {
            Main main = new Main();
            InputStream inputStream = main.getClass().getResourceAsStream(filename);
            if (inputStream == null) {
                throw new FileNotFoundException("Json file: " + filename + " to be persisted could not be found!");
            }
            Reader inputStreamReader = new InputStreamReader(inputStream, UTF_8);
            JsonReader reader = new JsonReader(inputStreamReader);
            Gson gson = new GsonBuilder().create();
            List<BasicDBObject> objects = new LinkedList<BasicDBObject>();
            // Read file in stream mode
            reader.beginArray();
            while (reader.hasNext()) {
                // Read data into object model
                User user = gson.fromJson(reader, User.class);
                objects.add(user);
            }
            reader.close();
            return objects;
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedEncodingException();
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }


}