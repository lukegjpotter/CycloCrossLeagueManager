package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.exception;

public class GriddingException extends RuntimeException {

    public GriddingException(String message) {
        super(message);
    }

    public GriddingException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
