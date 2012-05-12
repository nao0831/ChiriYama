
package com.wadako.savemoney;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
        updateListView();
    }

    private void initButtons() {
        View calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Save.getSumOfPrice(StartActivity.this);
                if (price == 0) {
                    Toast.makeText(StartActivity.this, "0円では貯金できません!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(StartActivity.this, ResultActivity.class);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
            }
        });
        
        View endSaveButton = findViewById(R.id.endSaveButton);
        endSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int price = Save.getSumOfPrice(StartActivity.this);
                if (price == 0) {
                    Toast.makeText(StartActivity.this, "節約がなくては貯金できません!", Toast.LENGTH_SHORT).show();
                    return;
                }
                AnimationSet set = new AnimationSet(true);
                Animation anim = new TranslateAnimation(0, 0, 0, -250);
                anim.setDuration(1000);
                anim.setFillAfter(true);
                
                Animation anim2 = new TranslateAnimation(0, -500, 0, 280);
                anim2.setDuration(1000);
                anim2.setFillAfter(true);
                anim2.setStartOffset(1300);
                
                Animation anim3 = new ScaleAnimation(1, 0.5f, 1, 0.5f);
                anim3.setDuration(600);
                anim3.setFillAfter(true);
                anim3.setStartOffset(1300);
                
                set.addAnimation(anim);
                set.addAnimation(anim2);
                set.addAnimation(anim3);
                set.setFillAfter(true);
                
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent();
                        intent.setClass(StartActivity.this, ResultActivity.class);
                        intent.putExtra("price", price);
                        startActivity(intent);
                    }
                });
                findViewById(R.id.coinImage).startAnimation(set);
                
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
                final int position2 = position;
                View deleteButton = convertView.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(StartActivity.this).setTitle("確認")
                            .setMessage("削除します。よろしいですか？")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Save save = saves.get(position2);
                                    save.delete(StartActivity.this);
                                    updateListView();
                                }
                            })
                            .setNegativeButton("CANCEL", null)
                            .create()
                            .show();
                    }
                });
            }
            Save save = saves.get(position);
            TextView priceText = (TextView)convertView.findViewById(R.id.priceText);
            priceText.setText(save.getPrice() + " yen");
            ImageView objectImage = (ImageView)convertView.findViewById(R.id.objectImage);
            objectImage.setImageResource(save.getPictureResId());
            return convertView;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            updateListView();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        menu.add(0 , Menu.FIRST , Menu.NONE , "全て削除する");
        return ret;
     }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new AlertDialog.Builder(StartActivity.this).setTitle("節約記録を全て削除しますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Save.deleteAll(StartActivity.this);
                        updateListView();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .create().show();
        
        return true;
     }
    
    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.coinImage).clearAnimation();
    }
    
    private void updateListView() {
        ListView list = (ListView)findViewById(R.id.saveList);
        list.setAdapter(new MyAdapter());
        list.invalidateViews();
        TextView text = (TextView)findViewById(R.id.sumPriceText);
        text.setText("¥" + Save.getSumOfPrice(StartActivity.this));
    }
}
