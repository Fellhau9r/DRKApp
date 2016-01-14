package com.example.d062434.drkapp.helper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.d062434.drkapp.Fragment03;
import com.example.d062434.drkapp.R;

import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by D062434 on 26.10.2015.
 */
public class CustomNFHDialog extends DialogFragment{
    private String mglId;
    private String mglName;
    private String date;
    private String dateFormat;
    private String action;
    private Fragment03 fragment03;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] values;
        if(action.equals("add")){
            values = new String[] {"Bereitschaft", "Nicht Verfügbar"};
        }
        else{
            values = new String[] {"Eintrag löschen"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mglName + " für diesen Tag eintragen (" + dateFormat + ")")
                .setItems(values, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Set Request Data for Backend Connection
                        String[][] requestData = new String[2][2];
                        //Check if user want to add or to delete an entry
                        if(action.equals("add")){
                            String status = "B";
                            if(which == 1){
                                status = "N";
                            }

                            requestData[0][0] = "add_not_status";
                            requestData[0][1] = status;
                            requestData[1][0] = "add_bereitschaft";
                            requestData[1][1] = "speichern";
                        }
                        else{
                            requestData[0][0] = "action";
                            requestData[0][1] = "del";
                            requestData[1][0] = "notid";
                            requestData[1][1] = "a";
                        }


                        //Get Data from Backend
                        String response = "";
                        try{
                        HTTPRequester myRequester = new HTTPRequester(requestData,
                                "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_notfallplaner.php?action=" + action + "&notdat=" +
                                        URLEncoder.encode(date, "UTF-8") + "&perid=" +
                                        URLEncoder.encode(mglId, "UTF-8") + "&vor=&zurueck=");

                            response = myRequester.execute().get();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(response.equals("")){
                            Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
                            tNoConnection.show();
                        }
                        else{
                            fragment03.updateFragment();
                        }
                    }
                });
        return builder.create();
    }

    public void setMglId(String mglId){
        this.mglId = mglId;
    }

    public void setDate(String date){
        this.date = date;
        this.dateFormat = date.substring(6);
        this.dateFormat += "." + date.substring(4,6);
        this.dateFormat += "." + date.substring(0,4);
    }

    public void setMglName(String mglName){
        this.mglName = mglName;
    }

    public void setAction(int currentStatus){
        if(currentStatus == 0){
            this.action = "add";
        }
        else{
            this.action = "del";
        }
    }

    public void setParentFragment(Fragment03 fragment03){
        this.fragment03 = fragment03;
    }

}
