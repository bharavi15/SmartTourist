package com.example.shubham.smarttourist;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Display extends AppCompatActivity {
    String Lang = "ENGLISH";
    String code = "en";
    String  ConvertedText;
    String PlaceName="";

    TextView  textView;
    String TextToConvert="";
    String translatedText = "";
    // todo API_KEY should not be stored in plain sight
    private static final String API_KEY = "AIzaSyDjyKVZe3TKNGKokvo5uUmHoja_gSWprec";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        PlaceName = getIntent().getStringExtra("result");
        ImageView ig=findViewById(R.id.IMGdemo);
     textView=(TextView) findViewById(R.id.Lang);
        Bundle bundle=getIntent().getExtras();
        String data=bundle.get("data").toString();
        if(data.equals("हिंदी")){ code="hi";}
        if(data.equals("मराठी")){ code="mr";}
        if(data.equals("தமிழ்")){ code="ta";}
        if(data.equals("ગુજરાતી")){ code="gu";}
        if(data.equals("ਪੰਜਾਬੀ")){ code="pa";}
        if(data.equals("తెలుగు")){ code="te";}
        if(data.equals("മല്യാലം")){ code="ml";}
        if(data.equals("سنڌي")){ code="sd";}

        if(PlaceName.equals("TajMahal"))
        {
            ig.setImageResource(R.drawable.tm);
            TextToConvert="\nTaj Mahal\n    The Taj Mahal is an ivory-white marble mausoleum on the south bank of the Yamuna river in the Indian city of Agra. It was commissioned in 1632 by the Mughal emperor, Shah Jahan (reigned from 1628 to 1658), to house the tomb of his favourite wife, Mumtaz Mahal. The tomb is the centrepiece of a 17-hectare (42-acre) complex, which includes a mosque and a guest house, and is set in formal gardens bounded on three sides by a crenellated wall. Construction of the mausoleum was essentially completed in 1643 but work continued on other phases of the project for another 10 years. The Taj Mahal complex is believed to have been completed in its entirety in 1653 at a cost estimated at the time to be around 32 million rupees, which in 2015 would be approximately 52.8 billion rupees (U.S. $827 million). The construction project employed some 20,000 artisans under the guidance of a board of architects led by the court architect to the emperor, Ustad Ahmad Lahauri. The Taj Mahal was designated as a UNESCO World Heritage Site in 1983 for being \"the jewel of Muslim art in India and one of the universally admired masterpieces of the world's heritage\". It is regarded by many as the best example of Mughal architecture and a symbol of India's rich history. The Taj Mahal attracts 7–8 million visitors a year. In 2007, it was declared a winner of the New7Wonders of the World (2000–2007) initiative.\n" +
                    "\n";
        }
        if(PlaceName.equals("HawaMahal"))
        {
            ig.setImageResource(R.drawable.hm);
            TextToConvert="\nHawa Mahal\n    Hawa Mahal(\"Palace of Winds\" or \"Palace of the Breeze\") is a palace in Jaipur, India. It is constructed of red and pink sandstone. The palace sits on the edge of the City Palace, Jaipur, and extends to the zenana, or women's chambers. The structure was built in 1799 by Maharaja Sawai Pratap Singh. He was so inspired by the unique structure of Khetri Mahal that he built this grand and historical palace. It was designed by Lal Chand Ustad. Its unique five-storey exterior is akin to the honeycomb of a beehive with its 953 small windows called jharokhas decorated with intricate latticework. The original intent of the lattice design was to allow royal ladies to observe everyday life and festivals celebrated in the street below without being seen, since they had to obey the strict rules of \"purdah\", which forbade them from appearing in public without face coverings. This architectural feature also allowed cool air from the Venturi effect (doctor breeze) to pass through, thus making the whole area more pleasant during the high temperatures in summer. Many people see the Hawa Mahal from the street view and think it is the front of the palace, but in reality it is the back of that structure. In 2006, renovation works on the Mahal were undertaken, after a gap of 50 years, to give a face lift to the monument at an estimated cost of Rs 4568 million. The corporate sector lent a hand to preserve the historical monuments of Jaipur and the Unit Trust of India has adopted Hawa Mahal to maintain it. The palace is an extended part of a huge complex. The stone-carved screens, small casements and arched roofs are some of the features of this popular tourist spot. The monument also has delicately modelled hanging cornices.\n";
        }
        if(PlaceName.equals("IndiaGate"))
        {
            ig.setImageResource(R.drawable.ig);
            TextToConvert="\nIndia Gate\n    The India Gate situated in Delhi,was part of the work of the Imperial War Graves Commission (I.W.G.C), which came into existence in December 1917 for building war graves and memorials to soldiers killed in the First World War. The foundation stone of the All-India War Memorial was laid on 10 February 1921, at 4:30 PM, by the visiting Duke of Connaught in a solemn soldierly ceremony attended by Officers and Men of the British Indian Army, Imperial Service Troops, the Commander in Chief, and Chelmsford, the viceroy. On the occasion, the viceroy said, \"The stirring tales of individual heroism, will live for ever in the annals of this country\", and that the memorial which was a tribute to the memory of heroes, \"known and unknown\" would inspire, future generations to endure hardships with similar fortitude and \"no less valour\". The King, in his message, read out by the Duke said \"On this spot, in the central vista of the Capital of India, there will stand a Memorial Archway, designed to keep\" in the thoughts of future generations \"the glorious sacrifice of the officers and men of the British Indian Army who fought and fell\". During the ceremony, the Deccan Horse, 3rd Sappers and Miners, 6th Jat Light Infantry, 34th Sikh Pioneers, 39th Garhwal Rifles, 59th Scinde Rifles (Frontier Force), 117th Mahrattas, and 5th Gurkha Rifles (Frontier Force), were honoured with title of \"Royal\" in recognition of the distinguished services and gallantry of the British Indian Army during the Great War\". Ten years after the foundation stone laying ceremony, on February 12, 1931, the All India War Memorial was inaugurated by Viceroy Lord Irwin, who on the occasion said \"those who after us shall look upon this monument may learn in pondering its purpose something of that sacrifice and service which the names upon its walls record.\" In the decade between the laying of foundation stone of the War memorial and its inauguration, the rail-line was shifted to run along the Yamuna river, and the New Delhi Railway Station was opened in 1926. The India gate, which is illuminated every evening, from 19:00 to 21:30, is a major tourist attraction. Cars, traveled through India Gate until it was closed to traffic. The Republic Day Parade starts from Rashtrapati Bhavan and passes around the India Gate.\n";
        }

        if(PlaceName.equals("LotusTemple"))
        {
            ig.setImageResource(R.drawable.lt);
            TextToConvert="\nLotus Temple\n   The Lotus Temple, located in Delhi, India, is a Bahá'í House of Worship that was dedicated in December 1986, costing $10 million. Notable for its flowerlike shape, it has become a prominent attraction in the city. Like all Bahá'í Houses of Worship, the Lotus Temple is open to all, regardless of religion or any other qualification. The building is composed of 27 free-standing marble-clad \"petals\" arranged in clusters of three to form nine sides, with nine doors opening onto a central hall with a height of slightly over 40 metres and a capacity of 2,500 people. The Lotus Temple has won numerous architectural awards and has been featured in many newspaper and magazine articles. A 2001 CNN report referred to it as the most visited building in the world.";
        }



        //translation
        EnglishToTagalog eng=new EnglishToTagalog();
        eng.execute();



    }
    private class EnglishToTagalog extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        protected void onError(Exception ex) {
            ex.printStackTrace();
        }
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Log.e("AsyncTask","doInBAckground");
                translatedText = translate(TextToConvert, "en", code, API_KEY);

                Thread.sleep(1000);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            //start the progress dialog
            progress = ProgressDialog.show(Display.this, null, "Translating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

            super.onPostExecute(result);
            translated();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public void translated(){
        textView.setText(translatedText);
        Log.e("output-----", translatedText);

    }

    public String translate(String text, String from, String to, String key) {
        StringBuilder result = new StringBuilder();
        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            String urlStr = "https://www.googleapis.com/language/translate/v2?key=" + key + "&q=" + encodedText + "&target=" + to + "&source=" + from;

            URL url = new URL(urlStr);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();
            } else
                stream = conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.e("result--", result.toString());
            JsonParser parser = new JsonParser();

            JsonElement element = parser.parse(result.toString());

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.get("error") == null) {
                    String translatedText = obj.get("data").getAsJsonObject().
                            get("translations").getAsJsonArray().
                            get(0).getAsJsonObject().
                            get("translatedText").getAsString();
                    return translatedText;

                }
            }

            if (conn.getResponseCode() != 200) {
                System.err.println(result);
            }

        } catch (IOException | JsonSyntaxException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

}
