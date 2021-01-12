package hr.fer.littlegreen.parkirajme.webservice.restapi;

import org.springframework.lang.Nullable;

public class Response {

    @Nullable
    protected String error;

    public Response(@Nullable String error) {
        this.error = error;
    }

    @Nullable
    public String getError() {return error;}

}
