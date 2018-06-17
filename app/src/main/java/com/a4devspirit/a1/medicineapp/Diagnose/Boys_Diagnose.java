package com.a4devspirit.a1.medicineapp.Diagnose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.a4devspirit.a1.medicineapp.Diagnostics;
import com.a4devspirit.a1.medicineapp.R;

public class Boys_Diagnose extends AppCompatActivity {
    Button head, res1, res2, forearm1, forearm2, wrist1, wrist2, palm1, palm2, fingers1, fingers2, hip1,
            hip2, shin1, shin2, foot1, foot2, gastritis, appendicitis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boys__diagnose);
        this.setTitle("Diagnostics");
        initialization();
        getData(head, "head");
        getData(res1, "respiratorysystem");
        getData(res2, "respiratorysystem");
        getData(forearm1, "forearm");
        getData(forearm2, "forearm");
        getData(fingers1, "fingers");
        getData(fingers2, "fingers");
        getData(foot1, "foot");
        getData(foot2, "foot");
        getData(hip1, "hip");
        getData(hip2, "hip");
        getData(shin1, "shin");
        getData(shin2, "shin");
        getData(wrist1, "wrist");
        getData(wrist2, "wrist");
        getData(palm1, "brush");
        getData(palm2, "brush");
        getData(gastritis, "gastritis");
        getData(appendicitis, "appendicitis");
    }
    private void initialization() {
        head = (Button) findViewById(R.id.headb);
        res1 = (Button) findViewById(R.id.respsys1b);
        res2 = (Button) findViewById(R.id.respsys2b);
        forearm1 = (Button) findViewById(R.id.forearm1b);
        forearm2 = (Button) findViewById(R.id.forearm2b);
        wrist1 = (Button) findViewById(R.id.wrist1b);
        wrist2 = (Button) findViewById(R.id.wrist2b);
        palm1 = (Button) findViewById(R.id.palm1b);
        palm2 = (Button) findViewById(R.id.palm2b);
        fingers1 = (Button) findViewById(R.id.fingers1b);
        fingers2 = (Button) findViewById(R.id.fingers2b);
        appendicitis = (Button) findViewById(R.id.appendicitisb);
        gastritis = (Button) findViewById(R.id.gastritisb);
        hip1 = (Button) findViewById(R.id.hip1b);
        hip2 = (Button) findViewById(R.id.hip2b);
        shin1 = (Button) findViewById(R.id.shin1b);
        shin2 = (Button) findViewById(R.id.shin2b);
        foot1 = (Button) findViewById(R.id.foot1b);
        foot2 = (Button) findViewById(R.id.foot2b);
    }
    private void getData(Button button, final String string) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Boys_Diagnose.this, Diagnostics.class);
                intent.putExtra("data", string);
                intent.putExtra("age", "b");
                startActivity(intent);
            }
        });
    }
}
