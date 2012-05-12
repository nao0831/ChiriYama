package com.wadako.savemoney;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AddSaveActivity extends Activity {
    
    private OnClickListener objectListener = new ObjectOnClickListener();
    private OnClickListener priceListener = new PriceOnClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_save_activity);
     
        initButtons();
        showObjectTable();
        initTableLayouts();
        
        EditText inputPriceText = (EditText)findViewById(R.id.priceEditText);
        inputPriceText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    EditText vv = (EditText)v;
                    InputMethodManager imm
                    = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow
                       (v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                   String text = vv.getText().toString();
                   if (StringUtils.isNotBlank(text)) {
                       TextView priceText = (TextView)findViewById(R.id.selectPriceButton);
                       priceText.setText(text);
                   }
                }
                return false;
            }
        });
    }
    
    private void initTableLayouts() {
        TableLayout objectTable = (TableLayout)findViewById(R.id.objectTable);
        for (int i = 0; i < objectTable.getChildCount(); i++) {
            ViewGroup row = (ViewGroup)objectTable.getChildAt(i);
            if (row instanceof FrameLayout) {
                continue;
            }
            for (int j = 0; j < row.getChildCount(); j++) {
                View layout = row.getChildAt(j);
                View button = layout.findViewById(R.id.imageButton1);
                button.setOnClickListener(objectListener);
            }
        }
        TableLayout priceTable = (TableLayout)findViewById(R.id.priceTable);
        for (int i = 0; i < priceTable.getChildCount(); i++) {
            TableRow row = (TableRow)priceTable.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                View button = row.getChildAt(j);
                button.setOnClickListener(priceListener);
            }
        }
    }
    
    private void initButtons() {
        View selectPrice = findViewById(R.id.selectPriceButton);
        selectPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriceTable();
            }
        });
        
        View selectObject = findViewById(R.id.selectObjectButton);
        selectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showObjectTable();
            }
        });
        View saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = (ImageButton)findViewById(R.id.selectedObject);
                String tag = (String)button.getTag();
                int resourceId = ResourcesUtils.getResourceIdByResourceName(AddSaveActivity.this, tag);
                if (resourceId == 0) {
                    Toast.makeText(AddSaveActivity.this, "モノを選んで下さい", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                TextView priceView = (TextView)findViewById(R.id.selectPriceButton);
                String priceStr = priceView.getText().toString();
                int price;
                try {
                    price = Integer.valueOf(priceStr);
                    if (price == 0) throw new Exception();
                } catch (Exception e) {
                    Toast.makeText(AddSaveActivity.this, "値段を選んで下さい", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Log.d(AppUtil.APP_NAME, "ID:" + resourceId + " price:" + price + "で保存するよ");
                Save save = new Save();
                save.setPictureResId(resourceId);
                save.setPrice(price);
                if (save.save(AddSaveActivity.this)) {
                    setResult(1);
                    finish();
                } else {
                    Toast.makeText(AddSaveActivity.this, "不正があります。選択しなおして下さい", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        View endInputButton = findViewById(R.id.endInputButton);
        endInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.priceEditText);
                InputMethodManager imm
                = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow
                   (editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
               String text = editText.getText().toString();
               if (StringUtils.isNotBlank(text)) {
                   TextView priceText = (TextView)findViewById(R.id.selectPriceButton);
                   priceText.setText(text);
               }
               
            }
        });
    }
    
    private void showObjectTable() {
        findViewById(R.id.objectTable).setVisibility(View.VISIBLE);
        findViewById(R.id.priceTable).setVisibility(View.GONE);
        findViewById(R.id.inputPriceView).setVisibility(View.GONE);
    }
    private void showPriceTable() {
        findViewById(R.id.objectTable).setVisibility(View.GONE);
        findViewById(R.id.priceTable).setVisibility(View.VISIBLE);
        findViewById(R.id.inputPriceView).setVisibility(View.VISIBLE);
    }
    
    private class ObjectOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ImageButton button = (ImageButton)v;
            Drawable d = button.getDrawable();
            findViewById(R.id.selectObjectButton).setVisibility(View.GONE);
            showPriceTable();
            ImageButton selectedObjectButton = (ImageButton)findViewById(R.id.selectedObject);
            selectedObjectButton.setImageDrawable(d);
            selectedObjectButton.setVisibility(View.VISIBLE);
            selectedObjectButton.setTag(button.getTag());
            selectedObjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showObjectTable();
                }
            });
        }
    }
    
    private class PriceOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView view = (TextView)v;
            String yen = view.getText().toString();
            TextView selectPriceButton = (TextView)findViewById(R.id.selectPriceButton);
            selectPriceButton.setText(yen.substring(0, yen.length() - 1));
        }
        
    }
}
