package com.example.ashugupta.socketapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    Socket socket;
    private static final String TAG = "MainActivity";
    TextView text;
    EditText message;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        try {
            socket = IO.socket("http://192.168.1.7:3000");
            socket.connect();
            Log.i(TAG, "onCreate: Connection Established");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket.connected())
                {
                    String mes  = message.getText().toString();
                    socket.emit("chat message", mes);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error in connection! Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        socket.on("Hello", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(args[0].toString());
                    }
                });

            }
        });
    }
}

