package contacts.services;


import android.content.Context;

import java.io.FileOutputStream;

public class FileHandleService {
    public void WriteToFile(String data, Context ctx, String filename) {
        System.out.println("Write file from file handler");
//        String filename = "Contacts";
        FileOutputStream outputStream;
        try {
            outputStream = ctx.openFileOutput(filename, ctx.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            System.out.println("Contact list downloaded successfully");
        } catch (Exception e) {
            System.out.println("WriteToFile>>>>>" + e);
//            createToast("Unable to save data on device");
        }
    }
}
