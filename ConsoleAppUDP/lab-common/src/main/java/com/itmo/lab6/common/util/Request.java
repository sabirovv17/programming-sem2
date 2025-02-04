package com.itmo.lab6.common.util;

import java.io.Serializable;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.interfaces.Builder;


public class Request implements Serializable {
    private String command;
    private Person person;

    public Request(RequestBuilder builder) {
        this.command = builder.command; 
        this.person = builder.person;
    }
    public String getCommand() {
        return command;
    }
    public Person getPerson() {
        return person;
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

        public RequestBuilder setCommand(String command) {
            this.command = command;
            return this;
        }
        public RequestBuilder setPerson(Person person) {
            this.person = person;
            return this;
        }
        public Request build() {
            return new Request(this);
        }
    }
}
