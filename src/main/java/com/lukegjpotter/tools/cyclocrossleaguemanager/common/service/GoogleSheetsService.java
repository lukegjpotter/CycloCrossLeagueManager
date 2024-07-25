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
import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.component.SheetsQuickstart;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsService {

    private final Sheets googleSheets;

    public GoogleSheetsService() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final String applicationName = "CycloCross League Manager";
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        final String tokensDirectoryPath = "./src/main/resources/";
        final List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
        final String credentialsFilePath = "/credentials.json";

        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(credentialsFilePath);
        if (in == null) throw new FileNotFoundException("Resource not found: " + credentialsFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File(tokensDirectoryPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        this.googleSheets = new Sheets.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    public ValueRange spreadsheetValuesInRange(final String googleSpreadSheetId, final String range) throws IOException {
        return googleSheets.spreadsheets().values().get(googleSpreadSheetId, range).execute();
    }
}
