package com.kuro.kuroline.fragments.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import com.kuro.kuroline.R;

public class FragmentManagerUtils {

    public static void addFragment(Fragment fragment, FragmentManager fragmentManager) {
        // Create a FragmentManager and a FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction
                .setReorderingAllowed(true)
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).add(R.id.fragment_container_view, fragment)
                .commit();
    }

    public static void replaceFragment(Fragment fragment, FragmentManager fragmentManager, boolean addToBackStack) {
        // Create a FragmentManager and a FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction
                .setReorderingAllowed(true)
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
//        if (addToBackStack){
//                transaction
//                .addToBackStack(null);
//        }
        transaction.replace(R.id.fragment_container_view, fragment)
                .commit();
    }

    public static void detachFragment(Fragment fragment, FragmentManager fragmentManager) {
        // Create a FragmentManager and a FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction
                .setReorderingAllowed(true)
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .detach(fragment)
                .commit();
    }


}
