package com.a4devspirit.a1.medicineapp.Diagnose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.a4devspirit.a1.medicineapp.Diagnostics;
import com.a4devspirit.a1.medicineapp.R;

public class Man_Diagnose extends AppCompatActivity {
    Button head, res1, res2, forearm1, forearm2, wrist1, wrist2, palm1, palm2, fingers1, fingers2, hip1,
    hip2, shin1, gastritis,  shin2, foot1, foot2, appendicitis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man__diagnose);
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
        head = (Button) findViewById(R.id.head);
        appendicitis = (Button) findViewById(R.id.appendicitis);
        gastritis = (Button) findViewById(R.id.gastritis);
        res1 = (Button) findViewById(R.id.respsys1);
        res2 = (Button) findViewById(R.id.respsys2);
        forearm1 = (Button) findViewById(R.id.forearm1);
        forearm2 = (Button) findViewById(R.id.forearm2);
        wrist1 = (Button) findViewById(R.id.wrist1);
        wrist2 = (Button) findViewById(R.id.wrist2);
        palm1 = (Button) findViewById(R.id.palm1);
        palm2 = (Button) findViewById(R.id.palm2);
        fingers1 = (Button) findViewById(R.id.fingers1);
        fingers2 = (Button) findViewById(R.id.fingers2);
        hip1 = (Button) findViewById(R.id.hip1);
        hip2 = (Button) findViewById(R.id.hip2);
        shin1 = (Button) findViewById(R.id.shin1);
        shin2 = (Button) findViewById(R.id.shin2);
        foot1 = (Button) findViewById(R.id.foot1);
        foot2 = (Button) findViewById(R.id.foot2);
    }
    private void getData(Button button, final String string) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Man_Diagnose.this, Diagnostics.class);
                intent.putExtra("data", string);
                intent.putExtra("age", "a");
                startActivity(intent);
            }
        });
    }
}
