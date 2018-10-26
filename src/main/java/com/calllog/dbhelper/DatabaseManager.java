package com.calllog.dbhelper;


import com.calllog.model.CallDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DatabaseManager {

    MongoCollection<Document> collection,uniquecollection;
    MongoDatabase callrecordDb;
    MongoClient client;

    public DatabaseManager()
    {
        Document indexOptions = new Document();
        indexOptions.put("date_month", 1);
        Document indexOptions2 = new Document();
        indexOptions2.put("calltime", 1);
//        client = new MongoClient("172.31.28.162", 27017);
        client = new MongoClient("abhidev.tk", 27017);
        callrecordDb = client.getDatabase("calldb");
        collection = callrecordDb.getCollection("calldetails");
        uniquecollection = callrecordDb.getCollection("uniquecall");
        collection.createIndex(indexOptions2,new IndexOptions().unique(true));
        uniquecollection.createIndex(indexOptions,new IndexOptions().unique(true));
    }


    public String getStatus()
    {

        List<String> dbs = client.getDatabaseNames();

        return dbs.toString();
    }

    public boolean createData(String callDetails)
    {

        try {

            Document document = Document.parse(callDetails);
            collection.insertOne(document);
            uniquecollection.insertOne(document);
        }

        catch (MongoWriteException e)
        {
            System.out.print("unique key exception::"+e.getMessage());
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.print("Exception in Databse::"+e.getMessage());
            return false;
        }

        return true;
    }


    public String getUniqueDataByMonth(String month)
    {
        ArrayList<String> list = new ArrayList<>();
        FindIterable<Document> iterDoc = uniquecollection.find(eq("month", month));

//        FindIterable<Document> iterDoc =  uniquecollection.find(
//                new Document("date_month", new Document("$gte", 100+month).append("$lte", 3100+month)));

        MongoCursor<Document> cursor = iterDoc.iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            doc.remove("_id");
            String serialized = com.mongodb.util.JSON.serialize(doc);
            try {

                list.add(serialized);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return list.toString();
    }

    public String dataByUser(String userName) {

        ArrayList<String> byUsersList = new ArrayList<>();

        FindIterable<Document> iterDoc = collection.find(eq("callBy", userName));

        MongoCursor<Document> cursor = iterDoc.iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            doc.remove("_id");
            String serialized = com.mongodb.util.JSON.serialize(doc);
            try {

                byUsersList.add(serialized);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return byUsersList.toString();
    }

    public String dataByMothDate(int month, int date) {

        ArrayList<String> list = new ArrayList<>();

        FindIterable<Document> iterDoc = collection.find(and(eq("month", month),eq("date", date)));

        MongoCursor<Document> cursor = iterDoc.iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            doc.remove("_id");
            String serialized = com.mongodb.util.JSON.serialize(doc);
            try {

                list.add(serialized);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return list.toString();
    }

    public String dataByDateAndUser(String datemonth, String userName) {

        ArrayList<String> list = new ArrayList<>();

        FindIterable<Document> iterDoc = collection.find(and(eq("date_month", datemonth),eq("callBy", userName)));

        MongoCursor<Document> cursor = iterDoc.iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            doc.remove("_id");
            String serialized = com.mongodb.util.JSON.serialize(doc);
            try {

                list.add(serialized);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return list.toString();
    }

    public String dataByMonthAndUser(String datemonth, String userName) {

        ArrayList<String> list = new ArrayList<>();

        FindIterable<Document> iterDoc = collection.find(and(eq("month", datemonth),eq("callBy", userName)));

        MongoCursor<Document> cursor = iterDoc.iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            doc.remove("_id");
            String serialized = com.mongodb.util.JSON.serialize(doc);
            try {

                list.add(serialized);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return list.toString();
    }

    public boolean createData(List<Document> list) {

        try {

//            collection.insertMany(list);
            for(Document d:list)
            {
                try {
                    uniquecollection.insertOne(d);
                }
                catch (MongoWriteException e)
                {
                    System.out.println("unique key exception:: At uniqueCollection");

                }
                try{
                    collection.insertOne(d);
                }
                catch (MongoWriteException e)
                {
                    System.out.println("unique key exception:: At normal collection");
                }

            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
            System.out.print("Exception in Databse::"+e.getMessage());
            return false;
        }

        return true;

    }
}
