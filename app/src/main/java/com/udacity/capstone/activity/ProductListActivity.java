package com.udacity.capstone.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.capstone.R;
import com.udacity.capstone.fragment.AddProductFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListActivity extends AppCompatActivity implements AddProductFragment.OnFragmentInteractionListener {

    @BindView(R.id.toolbar_main1)
    Toolbar mtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Products");
        mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_button :
                AddProductFragment addProductFragment=AddProductFragment.newInstance("1","2");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AddProductFragment())
                        .commit();
                return true;

            case R.id.edit_button :
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
    }
}
