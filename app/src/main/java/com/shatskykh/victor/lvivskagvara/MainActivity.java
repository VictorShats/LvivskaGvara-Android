package com.shatskykh.victor.lvivskagvara;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements NavigationDrawerCallbacks,
        View.OnClickListener, TextView.OnEditorActionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    ViewGroup m_my_list;
    LocalDatabase m_local_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        m_my_list = (ViewGroup) findViewById(R.id.list);
        m_local_db = new LocalDatabase(getApplicationContext());

        LayoutInflater l = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Vector<String> strings = m_local_db.getItems();

        if (strings.isEmpty()) {
            strings.add("аґрафка");
            strings.add("айнбрух");
            strings.add("аліво");
            strings.add("аліяс");
            strings.add("алярм");
            strings.add("академус, академік");
            strings.add("акомодуватися");
            strings.add("амант");
            strings.add("андрути");
            strings.add("анцуґ");
            strings.add("аус");
            strings.add("бадиль");
            strings.add("баєр");
            strings.add("байґель");
            strings.add("байриш");
            strings.add("байтлювати");
            strings.add("бакфіш");
            strings.add("балабух");
            strings.add("балаґула");
            strings.add("балак");
            strings.add("баламкатися");
            strings.add("балватунцьо");
            strings.add("балікати");
            strings.add("бальон");
            strings.add("банда");
            strings.add("бандзьох");
            strings.add("банти");
            strings.add("банувати");
            strings.add("баняк");
            strings.add("банька");
            strings.add("бараболя");
            strings.add("батяр");
            m_local_db.fillDb(strings);
        }

        for (String s : strings) {
            addListItem(s);
        }

        findViewById(R.id.detail).setOnClickListener(this);
        EditText editText = ((EditText) findViewById(R.id.edit_text));
        editText.setOnEditorActionListener(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        // Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_to_favorite) {
            return true;
        }

        if (id == R.id.action_share) {
            return true;
        }

        if (id == R.id.action_copy_explanation) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void addListItem(String item_name) {
        LayoutInflater l = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = l.inflate(R.layout.list_item, null);

        ((TextView) item.findViewById(R.id.itemtext)).setText(item_name);
        item.findViewById(R.id.morebutton).setOnClickListener(this);

        m_my_list.addView(item);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.morebutton) {
            View detailview = findViewById(R.id.detail);
            float width = findViewById(R.id.main_layout).getWidth();
            TranslateAnimation anim = new TranslateAnimation(width, 0.0f, 0.0f, 0.0f);
            anim.setDuration(300);
            anim.setFillAfter(true);
            detailview.bringToFront();
            detailview.startAnimation(anim);
            detailview.setVisibility(View.VISIBLE);
            detailview.setEnabled(true);
        } else if (v.getId() == R.id.detail) {
            View detailview = v;
            TranslateAnimation anim = new TranslateAnimation(0.0f, detailview.getWidth(), 0.0f, 0.0f);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.listview).bringToFront();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            anim.setDuration(300);
            anim.setFillAfter(true);
            detailview.startAnimation(anim);
            detailview.setEnabled(false);
        }
    }

    public void onDeleteButtonClick(View button) {
        Vector<View> views_to_delete = new Vector<View>();
        Vector<String> items_to_delete = new Vector<String>();

        for (int i = 0; i < m_my_list.getChildCount(); ++i) {
            View item = m_my_list.getChildAt(i);
            CheckBox cb = (CheckBox) item.findViewById(R.id.itemcheck);
            TextView tv = (TextView) item.findViewById(R.id.itemtext);
            if (cb != null && tv != null) {
                if (cb.isChecked()) {
                    views_to_delete.add(item);
                    items_to_delete.add(tv.getText().toString());
                }
            }
        }

        for (String item_name : items_to_delete) {
            m_local_db.deleteItem(item_name);
        }

        for (View v : views_to_delete) {
            ViewGroup g = (ViewGroup) v.getParent();
            if (g != null) g.removeView(v);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String item = v.getText().toString();
            if (item != null && item.length() > 0) {
                m_local_db.addItem(item, null);
                addListItem(item);
                ((ScrollView) findViewById(R.id.scroll_view)).requestLayout();
                ((ScrollView) findViewById(R.id.scroll_view)).fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
        return false;
    }
}