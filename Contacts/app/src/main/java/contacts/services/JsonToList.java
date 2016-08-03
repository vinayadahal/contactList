package contacts.services;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonToList {

    List names = new ArrayList<>();
    List phone = new ArrayList<>();

    public void setList(StringBuilder text) {
        try {
            JSONArray jArray = new JSONArray(text.toString());
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject objJson = jArray.getJSONObject(i);
                names.add(objJson.getString("name"));
                phone.add(objJson.getString("phone"));
            }
        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

    public void setList(StringBuilder text, String searchKeyword) {
        try {
            JSONArray jArray = new JSONArray(text.toString());
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject objJson = jArray.getJSONObject(i);
                if ((objJson.getString("name").contains(searchKeyword))) {
                    names.add(objJson.getString("name"));
                    phone.add(objJson.getString("phone"));
                }
            }
        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

    public List getListNames() {
        return this.names;
    }

    public List getListPhone() {
        return this.phone;
    }
}
