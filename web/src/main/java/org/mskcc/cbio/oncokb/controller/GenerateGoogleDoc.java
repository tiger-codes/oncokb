/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mskcc.cbio.oncokb.controller;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.codehaus.jackson.map.ObjectMapper;
import org.mskcc.cbio.oncokb.config.GoogleAuth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jgao
 */
@Controller
public class GenerateGoogleDoc {
    /**
        * A basic struct to store cell row/column information and the associated RnCn
        * identifier.
        */
    private static class CellAddress {
        public final int row;
        public final int col;
        public final String attr;
        public final String idString;

        /**
         * Constructs a CellAddress representing the specified {@code row} and
         * {@code col}.  The idString will be set in 'RnCn' notation.
         */
        public CellAddress(int row, int col, String attr) {
          this.row = row;
          this.col = col;
          this.attr = attr;
          this.idString = String.format("R%sC%s", row, col);
        }
    }
    
    @RequestMapping(value="/generateGoogleDoc")
    public @ResponseBody String generateGoogleDoc(
            @RequestParam(value="reportContent", required=false) String reportContent) throws MalformedURLException, ServiceException{
        try {
            HashMap<String,Object> result =
            new ObjectMapper().readValue(reportContent, HashMap.class);
            
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM-dd-yyyy z");
            Date date = new Date();
            String dateString = dateFormat.format(date);
            Drive driveService = GoogleAuth.getDriveService();
            File file = new File();
            file.setTitle("New File - " + dateString);
            file.setParents(Arrays.asList(new ParentReference().setId("0BzBfo69g8fP6eXoydVRrdHJBbE0")));
            file.setDescription("New File created from server");
            file = driveService.files().copy("1fCv8J8fZ2ZziZFJMqRRpNexxqGT5tewDEdbVT64wjjM", file).execute();
            changeFileContent(file.getId(), file.getTitle(), result);
            addNewRecord(file.getId(), file.getTitle(), "zhx", dateString);
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();

            System.err.println("Error code: " + error.getCode());
            System.err.println("Error message: " + error.getMessage());
            // More error information can be retrieved with error.getErrors().
        } catch (HttpResponseException e) {
            // No Json body was returned by the API.
            System.err.println("HTTP Status code: " + e.getStatusCode());
            System.err.println("HTTP Reason:" + e.getMessage());
        } catch (IOException e) {
            // Other errors (e.g connection timeout, etc.).
            System.out.println("An error occurred: " + e);
        } catch (GeneralSecurityException e) {
            System.out.println("An GeneralSecurityException occurred: " + e);
        } catch (URISyntaxException e) {
            System.out.println("An URISyntaxException occurred: " + e);
        }
        return "Success";
    }
    
    public static void addNewRecord(String reportDataFileId, String reportName, String user, String date) throws MalformedURLException, GeneralSecurityException, IOException, ServiceException {
        String REPORTS_INFO_SHEET_ID = "1fsixOxg-o-_UwZvInU99791ITyYhs4nz8s35_qJmw8o";
        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full/" + REPORTS_INFO_SHEET_ID);
        
        SpreadsheetService service = GoogleAuth.createSpreedSheetService();
        SpreadsheetEntry spreadSheetEntry = service.getEntry(SPREADSHEET_FEED_URL, SpreadsheetEntry.class);
        
        WorksheetFeed worksheetFeed = service.getFeed(
        spreadSheetEntry.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        WorksheetEntry worksheet = worksheets.get(0);
        
        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listEntry = service.getFeed(listFeedUrl, ListFeed.class);
        // Create a local representation of the new row.
        ListEntry row = new ListEntry();
        row.getCustomElements().setValueLocal("reportdatafileid", reportDataFileId);
        row.getCustomElements().setValueLocal("reportname", reportName);
        row.getCustomElements().setValueLocal("user", user);
        row.getCustomElements().setValueLocal("requestdate", date);

        // Send the new row to the API for insertion.
        service.insert(listFeedUrl, row);
    }
    
    public static void changeFileContent(String fileId, String fileName, HashMap<String,Object> content) throws MalformedURLException, GeneralSecurityException, IOException, ServiceException {
        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full/" + fileId);
        
        SpreadsheetService service = GoogleAuth.createSpreedSheetService();
        SpreadsheetEntry spreadSheetEntry = service.getEntry(SPREADSHEET_FEED_URL, SpreadsheetEntry.class);

        WorksheetFeed worksheetFeed = service.getFeed(
        spreadSheetEntry.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        WorksheetEntry worksheet = worksheets.get(0);

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        ListEntry listEntry  = listFeed.getEntries().get(0);
        Iterator it = content.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            String value = pairs.getValue().toString();
            
            if(!value.equals("")) {
                String key = pairs.getKey().toString().toLowerCase();
                System.out.println(key);
                listEntry.getCustomElements().setValueLocal(key, value);
                System.out.println(key + " = " + value);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        listEntry.update();
    }
}