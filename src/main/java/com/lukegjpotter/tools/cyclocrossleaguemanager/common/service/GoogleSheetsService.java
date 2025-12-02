package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.component.AlphabetComponent;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsService {

    private final Sheets googleSheets;
    private final AlphabetComponent alphabetComponent;

    public GoogleSheetsService(AlphabetComponent alphabetComponent) throws GeneralSecurityException, IOException {
        this.alphabetComponent = alphabetComponent;
        // Build a new authorized API client service.
        final String applicationName = "CycloCross League Manager";
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        // ToDo: May need to be on Project level, for Docker deployment.
        final String tokensDirectoryPath = "./src/main/resources/tokens";
        final List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);
        final String credentialsFilePath = "/credentials.json";

        // Load client secrets.
        InputStream in = GoogleSheetsService.class.getResourceAsStream(credentialsFilePath);
        if (in == null) throw new FileNotFoundException("Resource not found: " + credentialsFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File(tokensDirectoryPath)))
                .setAccessType("offline") // ToDo: May need to update this to "online" and create new Credientials for WebApplication on GCP.
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        this.googleSheets = new Sheets.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    public ValueRange readSpreadsheetValuesInRange(final String googleSpreadSheetId, final String range) throws IOException {
        return googleSheets.spreadsheets().values().get(googleSpreadSheetId, range).execute();
    }

    public String writeValuesToSpreadsheetFromCell(final String googleSpreadSheetId, final String startCell, final ValueRange body) throws IOException {
        return googleSheets.spreadsheets().values().update(googleSpreadSheetId, startCell, body)
                .setValueInputOption("RAW").execute().getUpdatedRange();
    }

    public List<String> getSpreadsheetHeaders(final String googleSpreadSheetId, final String sheetName) throws IOException {
        List<String> headers = new ArrayList<>();
        ValueRange valueRange = readSpreadsheetValuesInRange(googleSpreadSheetId, sheetName + "!A1:1");
        List<List<Object>> spreadsheetHeaders = valueRange.getValues();
        spreadsheetHeaders.forEach(headerRows -> headerRows.forEach(headerRow -> headers.add(String.valueOf(headerRow))));

        return headers;
    }

    /**
     *
     * @param googleSpreadSheetId String e.g "1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw"
     * @param columnsToSort       List e.g List.of("E", "F", "G", "H", "I", "J", "K").
     * @return
     * @throws IOException
     */
    public BatchUpdateSpreadsheetResponse sortSpreadsheetOnColumns(final String googleSpreadSheetId, final List<String> columnsToSort) throws IOException {
        List<String> columnsToSortDescending = new ArrayList<>(columnsToSort);

        Collections.reverse(columnsToSortDescending);

        // Get all the Sheet IDs.
        List<Integer> sheetIds = new ArrayList<>();
        googleSheets.spreadsheets().get(googleSpreadSheetId).execute().getSheets().forEach(sheet -> {
            sheetIds.add(sheet.getProperties().getSheetId());
        });

        List<Request> requests = new ArrayList<>();

        sheetIds.forEach(sheetId -> {

            List<SortSpec> sortSpecs = new ArrayList<>();
            columnsToSortDescending.forEach(columnLetter -> {
                sortSpecs.add(new SortSpec()
                        .setDimensionIndex(alphabetComponent.positionInAlphabet(columnLetter))
                        .setSortOrder("DESCENDING"));
            });

            SortRangeRequest srr = new SortRangeRequest().setRange(new GridRange()
                            .setSheetId(sheetId)
                            .setStartRowIndex(1)
                            .setStartColumnIndex(1))
                    .setSortSpecs(sortSpecs);

            requests.add(new Request().setSortRange(srr));
        });

        return googleSheets.spreadsheets().batchUpdate(
                        googleSpreadSheetId,
                        new BatchUpdateSpreadsheetRequest().setRequests(requests))
                .execute();
    }
}
