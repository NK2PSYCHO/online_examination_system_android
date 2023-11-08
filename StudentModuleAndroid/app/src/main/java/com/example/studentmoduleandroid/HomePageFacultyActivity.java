package com.example.studentmoduleandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageFacultyActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_faculty);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (item -> {
                    Fragment selectedFragment;
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = DescriptiveAssessmentFragment.newInstance();
                            break;
                        case R.id.navigation_result:
                            selectedFragment = AllMCQFragment.newInstance();
                            break;
                        case R.id.navigation_detail:
                            selectedFragment = AllDescriptiveFragment.newInstance();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " +
                                    item.getItemId());
                    }
                    FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment).addToBackStack("TAG");
                    transaction.commit();
                    return true;
                });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, DescriptiveAssessmentFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()==0) {
            moveTaskToBack(true);
        }
        else
            super.onBackPressed();
    }
}