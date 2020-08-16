package com.example.helperbuddy.HelperClasses;

//Adapter class

/*imported fragment
fragment manager and
fragment pager adapter
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;  //imported collection of ArrayList
import java.util.List;       //imported collection of list

               //This class used for Shift of tab view
public class SectionsPagerAdapter extends FragmentPagerAdapter {  //class

    //for movement of tab view

    private final List<Fragment> mFragmentList = new ArrayList<>();  //object


    public SectionsPagerAdapter(FragmentManager fm) {  //constructor
        super(fm);
    }

    //get method

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    //get method

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }


}  //class