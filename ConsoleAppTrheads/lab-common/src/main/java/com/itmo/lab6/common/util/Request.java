package com.itmo.lab6.common.util;

import java.io.Serializable;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.interfaces.Builder;


public class Request implements Serializable {
    private String command;
    private Person person;
    private String password; 
    private String login;
    private RequestCode requestCode;

    public Request(RequestBuilder builder) {
        this.command = builder.command; 
        this.person = builder.person;
        this.login = builder.login;
        this.password = builder.password;
        this.requestCode = builder.requestCode;
    }
    public String getCommand() {
        return command;
    }
    public Person getPerson() {
        return person;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public RequestCode getRequestCode() {
        return requestCode;
    }


    public String getData() {
        return ((command == null ? "" : command + "\n") + (person == null ? "" : person.toString()));
    }
    @Override
    public String toString() {
        return "client request:\n" + getData();
    }

    public static class RequestBuilder implements Builder<Request>, Serializable {
        private String command; 
        private Person person;
        private String login;
        private String password;
        private RequestCode requestCode;

        public RequestBuilder setCommand(String command) {
            this.command = command;
            return this;
        }
        public RequestBuilder setPerson(Person person) {
            this.person = person;
            return this;
        }
        public RequestBuilder setLogin(String login) {
            this.login = login;
            return this;
        }
        public RequestBuilder setPassword(String password) {
            this.password = password;
            return this;
        }
        public RequestBuilder setRequestCode(RequestCode requestCode) {
            this.requestCode = requestCode;
            return this;
        }
        public Request build() {
            return new Request(this);
        }
    }
}
