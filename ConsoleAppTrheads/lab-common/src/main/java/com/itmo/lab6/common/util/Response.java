package com.itmo.lab6.common.util;

import java.io.Serializable;
import java.util.TreeMap;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.interfaces.Builder;

public class Response implements Serializable {
    private String responseMessage;
    private TreeMap<Long, Person> collection;
    private Person person; 
    private ExecutionCode executionCode;

    public Response(ResponseBuilder builder) {
        this.responseMessage = builder.responseMessage;
        this.collection = builder.collection;
        this.person = builder.person;
        this.executionCode = builder.executionCode;
    }


    public String getResponseMessage() {
        return responseMessage;
    }
    public TreeMap<Long, Person> getCollection() {
        return collection;
    }
    public Person getPerson() {
        return person;
    }
    public ExecutionCode getExecutionCode() {
        return executionCode;
    }
    public String getData() {
        return (responseMessage == null ? "" : responseMessage + "\n") + (person == null ? "" : person.toString() + "\n") + (collection == null ? "" : collection.toString());
    }

    @Override
    public String toString() {
        return getData().trim();
    }


    public static class ResponseBuilder implements Builder<Response>, Serializable {
        private String responseMessage;
        private TreeMap<Long, Person> collection;
        private Person person; 
        private ExecutionCode executionCode;

        public ResponseBuilder setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
            return this;
        } 
        public ResponseBuilder setCollection(TreeMap<Long, Person> collection) {
            this.collection = collection;
            return this;
        }
        public ResponseBuilder setPerson(Person person) {
            this.person = person;
            return this;
        }
        public ResponseBuilder setExecutionCode(ExecutionCode executionCode) {
            this.executionCode = executionCode;
            return this;
        }
        @Override
        public Response build() {
            return new Response(this);
        }

    }
}
