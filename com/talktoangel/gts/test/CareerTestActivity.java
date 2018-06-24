package com.talktoangel.gts.test;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.listener.YourFragmentInterface;

public class CareerTestActivity extends AppCompatActivity {

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Class6Fragment();
                case 1:
                    return new Class10Fragment();
                case 2:
                    return new CollegeFragment();
                default:
                    return null;
            }
        }

        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Class 6th-9th";
                case 1:
                    return "Class 10th-12th";
                case 2:
                    return "College Students";
                default:
                    return null;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_career_test);
        setSupportActionBar((Toolbar) findViewById(C0585R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager mViewPager = (ViewPager) findViewById(C0585R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        ((TabLayout) findViewById(C0585R.id.tabs)).setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int i, float v, int i2) {
            }

            public void onPageSelected(int i) {
                YourFragmentInterface fragment = (YourFragmentInterface) mSectionsPagerAdapter.instantiateItem(mViewPager, i);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
