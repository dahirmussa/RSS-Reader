package com.example.customsrss;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomActivity extends AppCompatActivity {

    ListView list;
    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<String> description;
    ArrayList<String> pubDate;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    BottomNavigationView bottomNavigationView;

    ArrayList<String> myArraylist = new ArrayList<>();

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        list = (ListView) findViewById(R.id.list);

        titles = new ArrayList<String>();
        links = new ArrayList<String>();
        description = new ArrayList<String>();
        pubDate = new ArrayList<String>();

        Uri sound = Uri.parse ("android.resource://" + getPackageName() + "/" + R.raw.tmnt_theme) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CustomActivity.this,
                default_notification_channel_id )
                .setSmallIcon(R.drawable. ic_launcher_foreground )
                .setContentTitle( "new feeds " + titles + ""+ pubDate)
                .setContentText( "summary" + links);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM )
                    .build();
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            notificationChannel.setSound(sound , audioAttributes) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis (), mBuilder.build()) ;

        bottomNavigationView = bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.home:
                        startActivity(new Intent(getApplicationContext(),CustomActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.info:
                        startActivity(new Intent(getApplicationContext(),info.class));
                        overridePendingTransition(0,0);
                        return true;

                    case  R.id.logout:
                        startActivity(new Intent(getApplicationContext(),Logout.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //String[] values = {"hello", "there"};
        //   ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myArraylist);
        // list.setAdapter(adapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(links.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);



            }
        });
        new ProcessInBackground().execute();

    }

    // in oder to make connect use inputstram which is method , call the method called getinputstream
    public InputStream getInputStream(URL url) // for the argument input stream will  be url java.net url object
    {
        // catch the input and output exception
        try {
            // call a connection on that url object, on that open connect we call get input stream that will get the input stream
            return url.openConnection().getInputStream();    // the input stream return stream that reads from the open connect
        } catch (IOException e) {
            return null; // if there is exception. return null
        }
        // using async task to start reading from the document
    }
    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> // first integer is the first passed in and will come to doing background method
    {
        ProgressDialog progressDialog = new ProgressDialog(CustomActivity.this);
        Exception exception = null;  // exception object
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Busy loading rss feed");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try {
                URL url = new URL("https://www.independent.ie/breaking-news/rss/");  // url object, new url with rss feed
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); //  create new instance of the ParserFactory that can be used to create the xml
                factory.setNamespaceAware(false); // specific whether the parser producer by this factory that support for xml namespace but in this case its not used which is set to false ,  // factory is instance that can be used to create parser xml
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url),"utf-8"); // the inputstream which makes a new connects

                boolean insideItem = false; // boolean value to tell when I'm inside the items tag.
                int evenType  = xpp.getEventType(); // when get the eventype this return the occur event

                while(evenType != XmlPullParser.END_DOCUMENT){  // keep on loop while the eventype is not end of document
                    if(evenType == XmlPullParser.START_TAG) // if its start tag and check the tag name
                    {
                        // if its start tag and if its item tag and set inside item to true and go to the next on (evenType = xpp.next();)

                        // item tag
                        if(xpp.getName().equalsIgnoreCase("item"))
                        {
                            insideItem = true; // when inside the open item , set this true
                        }

                        // if its start tag and if its not item but its title and its inside the item extract the title tag
                        else if(xpp.getName().equalsIgnoreCase("title")){
                            // sure if we are inside the item tag
                            if(insideItem)
                            {
                                titles.add(xpp.nextText()); // when inside the item, return the title tag
                            }
                            // extract the link tag
                        }else if(xpp.getName().equalsIgnoreCase("link"))
                        {
                            if(insideItem)
                            {
                                links.add(xpp.nextText());
                            }
                        }else if(xpp.getName().equalsIgnoreCase("description"))
                        {
                            if(insideItem)
                            {
                                description.add(xpp.nextText());
                            }
                        }
                        else if(xpp.getName().equalsIgnoreCase("pubDate"))
                        {
                            if(insideItem)
                            {
                                pubDate.add(xpp.nextText());
                            }
                        }
                    }
                    // if its end tag and the same xpp getname is item.
                    else if(evenType == XmlPullParser.END_DOCUMENT && xpp.getName().equalsIgnoreCase("item"))
                    {
                        insideItem = false;
                    }
                    evenType = xpp.next(); // this will increase and go to the next tag.
                }
            }
            catch (MalformedURLException e) // if there is something wrong with the url
            {
                exception = e; // when exception has occurred
            }catch (XmlPullParserException e){ // extract data
                exception = e;

            }catch (IOException e)
            {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            // make the connect and show it on the list view
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomActivity.this, android.R.layout.simple_list_item_1,titles);
            list.setAdapter(adapter);
            progressDialog.dismiss();

        }
    }
}// end of main activity class

