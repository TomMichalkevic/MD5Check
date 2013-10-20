package com.tomasmichalkevic.md5check;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.view.View;
import android.widget.Button;
import android.content.*;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {

    Button chooseFile, compareButton, clipboardButton, exitButton;
    TextView text, matchStatus;
    String path, notifyMessage;
    EditText input;
    Context context;
    int durationOfNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseFile = (Button) findViewById(R.id.fileChoose);
        compareButton = (Button) findViewById(R.id.compareButton);
        clipboardButton = (Button) findViewById(R.id.clipboardButton);
        exitButton = (Button) findViewById(R.id.exitButton);
        text = (TextView) findViewById(R.id.hash);
        input = (EditText) findViewById(R.id.inputField);
        matchStatus = (TextView) findViewById(R.id.matchStatus);
        context = getApplicationContext();
        notifyMessage = "Copied to Clipboard";
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, 0);


            }
        });
        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.getText().toString().toUpperCase().equals(input.getText().toString()) ||
                        text.getText().toString().equals(input.getText().toString()) ||
                        text.getText().toString().equals(input.getText().toString().toUpperCase()) ||
                        text.getText().toString().toUpperCase().equals(input.getText().toString().toUpperCase())
                        ){
                    matchStatus.setText("Matches!");
                }else{
                    matchStatus.setText("No match");
                }
            }
        });
        clipboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String extractedHash = text.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hash", extractedHash);
                clipboard.setPrimaryClip(clip);
                durationOfNotify = Toast.LENGTH_SHORT;
                //Toast popup 14/10/2013
                Toast.makeText(context, notifyMessage, durationOfNotify).show();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        /**    case R.id.hash_change_item:

                return true;*/
            case R.id.about:
                Intent about = new Intent("com.tomasmichalkevic.md5check.ABOUTME");
                startActivity(about);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            path = data.getData().getPath();
            FileInputStream fis = null;
            try {
                // Generate md5
                fis = new FileInputStream(path);
                String md5String = new String(Hex.encodeHex(DigestUtils.md5(fis)));
                text.setText(md5String);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException io){
                io.printStackTrace();
            }
        }

    }


}
