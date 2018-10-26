package com.calllog;

import com.calllog.dbhelper.DatabaseManager;
import com.calllog.model.CallDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Path("/call")
@Singleton
public class ResourceApi {

    DatabaseManager databaseManager;
    JSONObject respose ;

    public ResourceApi(String s)
    {

    }

    public ResourceApi()
    {
        System.out.println("::RestServie Constructor is called::");
        databaseManager = new DatabaseManager();

    }


    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addCallDetaisToDb(String data)
    {
        respose = new JSONObject();
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.put("uuid",UUID.randomUUID().toString());
        jsonObject.put("createdAt",System.currentTimeMillis()+"");

        System.out.println(jsonObject.toString());

        try {

        if (databaseManager.createData(jsonObject.toString()))
        {
            Logger.getGlobal().info("Document inserted successfully");
            respose.put("status",200);
            respose.put("error","");
            return respose.toString();
        }

        } catch (Exception e) {
            e.printStackTrace();
            respose.put("status",400);
            respose.put("error","exception oocurs"+e.getMessage());

        }

        respose.put("status",400);
        respose.put("error","Document not inserted");
        return respose.toString();

    }


    @Path("/sync")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String syncBulkData(String data)
    {
        System.out.println(new Date());
        System.out.println("data from app::"+data);
        respose = new JSONObject();
        List<Document> arrayList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray =jsonObject.getJSONArray("data");

        for (int i=0;i<jsonArray.length();i++) {
            jsonArray.getJSONObject(i).put("uuid",UUID.randomUUID().toString());
            jsonArray.getJSONObject(i).put("createdAt",System.currentTimeMillis()+"");
            Document document = Document.parse(jsonArray.getJSONObject(i).toString());
            arrayList.add(document);

        }

        try {

            if (databaseManager.createData(arrayList))
            {
                Logger.getGlobal().info("Document inserted successfully");
                respose.put("status",200);
                respose.put("error","");
                return respose.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            respose.put("status",400);
            respose.put("error","exception oocurs"+e.getMessage());

        }

        respose.put("status",400);
        respose.put("error","Document not inserted");
        return respose.toString();

    }


    @Path("/current")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public String getUniqueCallLogsByMonth(@QueryParam("month") String month)
    {
        respose = new JSONObject();
        Logger.getGlobal().info("month::"+month);
        String data = databaseManager.getUniqueDataByMonth(month);

        if (data !=null)
        {
            respose.put("status",200);
            respose.put("data",new JSONArray(data));
            Logger.getGlobal().info("data send by server::"+respose.toString());
            return respose.toString();
        }
        respose.put("status",400);
        respose.put("error","data is null");
        return respose.toString();
    }

    @Path("/check/request")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String getResponseStatus(String data)
    {
            System.out.println(data);
            return data;
    }

    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String groupByUser(@QueryParam("name") String userName)
    {

        Logger.getGlobal().info("user name::"+userName);
        respose = new JSONObject();
        String data = databaseManager.dataByUser(userName);

        if (data !=null)
        {
            respose.put("status",200);
            respose.put("data",new JSONArray(data));
            return respose.toString();
        }

        respose.put("status",400);
        respose.put("error","data is null");
        return respose.toString();
    }

    @Path("/monthdate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String groupByMonthDate(@QueryParam("month") int month,@QueryParam("date") int date)
    {
        respose = new JSONObject();
        String data = databaseManager.dataByMothDate(month,date);

        if (data !=null)
        {
            respose.put("status",200);
            respose.put("data",new JSONArray(data));
            return respose.toString();
        }

        return null;
    }

    @Path("/user/datemonth")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public String getlistByUserAndDate(@QueryParam("datemonth") String datemonth,@QueryParam("user") String userName)
    {
        respose = new JSONObject();
        String data = databaseManager.dataByDateAndUser(datemonth,userName);

        if (data !=null)
        {
            respose.put("status",200);
            respose.put("data",new JSONArray(data));
            return respose.toString();
        }

        return null;
    }

    @Path("/user/month")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public String getlistByUserAndMonth(@QueryParam("month") String month,@QueryParam("user") String userName)
    {
        respose = new JSONObject();
        Logger.getGlobal().info("month::"+month+" user name ::"+userName);
        String data = databaseManager.dataByMonthAndUser(month,userName);

        if (data !=null)
        {
            respose.put("status",200);
            respose.put("data",new JSONArray(data));
            return respose.toString();
        }

        return null;
    }


    @Path("/status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public String serviceStatus()
    {
        return null;
    }


    @Path("/upload")
    @POST
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public String testForData(@FormDataParam("file") InputStream uploadstream, @FormDataParam("file")FormDataContentDisposition fileDetails)  {

        try {
//for upload files to server
            java.nio.file.Files.copy(uploadstream,new File("/home/abhishek/Desktop/fileUpload/").toPath(), StandardCopyOption.REPLACE_EXISTING);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "ok";
    }

}
//015975334723.dkr.ecr.ap-south-1.amazonaws.com/calllogs
