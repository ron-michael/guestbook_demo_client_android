/*
 * Copyright (C) 2014
 *
 */

/* REVISION HISTORY
 *
 * DATE         NAME                      REMARKS
 * 2014/11/27   Ron Michael Khu           Created
 */

package net.ronmichael.trial.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Intended to be the data class representation of a "message item". Currently only supports populating contents from a
 * JSONObject.
 * </p>
 * Note: the JSON format suppported by this class conforms to the REST API specified for the Guestbook project. Should
 * the format change in the API, this class should also be updated.
 */
public class Message implements Serializable {

    /**
     * ID field
     */
    public String id = "";

    /**
     * Name
     */
    public String name = "";

    /**
     * Photo
     */
    public String photo = "";

    /**
     * Message
     */
    public String message = "";

    /**
     * Creates a Message item instance.
     */
    public Message() {
    }

    /**
     * Creates a Message item instance.
     */
    public Message(String id, String name, String message, String photo) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.photo = photo;
    }

    /**
     * Gets the value of the specified key from the JSON object.
     *
     * @param json JSON object to fetch the value
     * @param key key or field to fetch the value
     *
     * @return field value
     */
    public String getValue(JSONObject json, String key) {
        String value = "";
        try {
            Object obj = (Object)json.get(key);
            value = (String) obj;
        } catch (JSONException e) {
            value = "";
        } catch (ClassCastException eClass) {
            try {
                value = "" +  json.getInt(key);
            } catch (JSONException e) {
                value = "";
            }
        }

        return value;
    }

    /**
     * Sets the value of a specific key or field in a JSON object
     *
     * @param json JSON object to make the field assignment
     * @param key key or field to be assigned with a new value
     * @param value value to assign
     */
    public void setValue(JSONObject json, String key, String value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
        }
    }


    /**
     * Assign values to the message item by parsing values from the specified JSON object.
     *
     * @param json JSON object that serves as the data source
     */
    public void assign(JSONObject json) {
        name = getValue(json, "name");
        message = getValue(json, "message");
        photo = getValue(json, "photo");
        id = getValue(json, "id");

    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        setValue(json, "name", name);
        setValue(json, "message", message);
        setValue(json, "photo", photo);
        setValue(json, "id", id);

        return json;
    }


}
