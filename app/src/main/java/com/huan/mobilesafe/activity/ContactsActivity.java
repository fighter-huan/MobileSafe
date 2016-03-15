package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.huan.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = "ContactsActivityInfo";

    private ListView lvContacts;

    //存放联系人
    private ArrayList<HashMap<String, String>> contacts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        lvContacts = (ListView) findViewById(R.id.lv_contacts);

        contacts = readContacts();

        lvContacts.setAdapter(new SimpleAdapter(this, contacts, R.layout.contact_list_item,
                new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));

        //给listView设置监听
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前item的电话号码
                String phone = contacts.get(position).get("phone");
                Intent intent = new Intent();
                //将数据放在intent中返回给上一个页面
                intent.putExtra("phone", phone);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private ArrayList<HashMap<String, String>> readContacts() {

        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        //从raw_contacts中读取联系人的id
        Cursor rawContactsCursor = getContentResolver().
                query(rawContactsUri, new String[]{"contact_id"}, null, null, null);

        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(0);

                //根据contacts_id从data表中查询相应的电话号码和联系人的名称，实际上查询的是视图view_data
                Cursor dataCursor = getContentResolver().query(dataUri,
                        new String[]{"data1", "mimetype"}, "contact_id = ?", new String[]{contactId}, null);
                if (dataCursor != null) {
                    HashMap<String, String> map = new HashMap<>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            map.put("phone", data1);
                        }
                        if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            map.put("name", data1);
                        }
                    }

                    list.add(map);
                    dataCursor.close();
                }

            }
            rawContactsCursor.close();
        }

        return list;
    }
}
