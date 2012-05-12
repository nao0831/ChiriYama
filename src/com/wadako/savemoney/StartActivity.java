
package com.wadako.savemoney;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initButtons();
        initListView();
    }

    private void initButtons() {
        View calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View addSaveButton = findViewById(R.id.addSaveButton);
        addSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, AddSaveActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void initListView() {
        ListView listView = (ListView)findViewById(R.id.saveList);
        listView.setScrollingCacheEnabled(false);
        listView.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {

        private List<Save> saves;

        public MyAdapter() {
            this.saves = Save.getSaves(StartActivity.this);
        }

        @Override
        public int getCount() {
            return saves.size();
        }

        @Override
        public Object getItem(int position) {
            return saves.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.save_row, null);
                View deleteButton = convertView.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(StartActivity.this, "削除ボタンだよ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Save save = saves.get(position);
            TextView priceText = (TextView)convertView.findViewById(R.id.priceText);
            priceText.setText(save.getPrice() + "yen");
            ImageView objectImage = (ImageView)convertView.findViewById(R.id.objectImage);
            objectImage.setImageResource(save.getPictureResId());
            return convertView;
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            ListView list = (ListView)findViewById(R.id.saveList);
            list.setAdapter(new MyAdapter());
            list.invalidateViews();
        }
    }
}
