package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.exception;

public class UpdateStandingsException extends RuntimeException {

    public UpdateStandingsException(String message) {
        super(message);
    }

    public UpdateStandingsException(String message, Throwable cause) {
        super(message, cause);
    }
}
