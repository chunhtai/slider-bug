package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import static android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES;

public class FooFragment extends Fragment {
  public FooFragment() {
    super();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    // Defines the xml file for the fragment
    return inflater.inflate(R.layout.fragment_foo, parent, false);
  }

  // This event is triggered soon after onCreateView().
  // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    // Setup any handles to view objects here
    // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    view.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
    view.setAccessibilityDelegate(new MyCalendarViewAccessibilityDelegate(view));
  }
}
