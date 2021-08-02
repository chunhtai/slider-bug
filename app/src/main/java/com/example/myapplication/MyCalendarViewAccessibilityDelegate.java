package com.example.myapplication;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


final class MyCalendarViewAccessibilityDelegate extends View.AccessibilityDelegate {
  // The View instance on which this class performs accessibility functions.
  private final View view;
  private int accessibilityFocusedSemanticsNode = -1;
  MyCalendarViewAccessibilityDelegate(View view) {
    this.view = view;
  }

  @Override
  public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
    return new AccessibilityNodeProvider() {
      @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
      @Override
      @Nullable
      public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
        Log.e("a11y", "got virtualViewId " + virtualViewId);
        if (virtualViewId == View.NO_ID) {
          AccessibilityNodeInfo result = AccessibilityNodeInfo.obtain(view);
          view.onInitializeAccessibilityNodeInfo(result);
          result.setBoundsInScreen(new Rect(0, 0, 200, 200));
          result.addChild(view, 1);
          result.addChild(view, 2);
          return result;
        }
        if (virtualViewId == 1) {
          AccessibilityNodeInfo result = AccessibilityNodeInfo.obtain(view, virtualViewId);
          result.setViewIdResourceName("");
          result.setPackageName(view.getContext().getPackageName());
          result.setClassName("android.view.View");
          result.setSource(view, virtualViewId);
          result.setFocusable(true);
          result.setEnabled(true);
          result.setContentDescription("child1");
          result.setBoundsInScreen(new Rect(100, 100, 200, 200));
          result.setParent(view);
          result.setVisibleToUser(true);
          if (accessibilityFocusedSemanticsNode != -1
              && accessibilityFocusedSemanticsNode == virtualViewId) {
            result.addAction(AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
          } else {
            result.addAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
          }
          result.setAccessibilityFocused(accessibilityFocusedSemanticsNode == virtualViewId);
          return result;
        }

        if (virtualViewId == 2) {
          AccessibilityNodeInfo result = AccessibilityNodeInfo.obtain(view, virtualViewId);
          result.setViewIdResourceName("");
          result.setPackageName(view.getContext().getPackageName());
          // AbsSeekBar works, but it will pronounce "one percent, progress bar", which is not what I want.
//          result.setClassName("android.widget.AbsSeekBar");
          // This stop working after android 11. It used to be able to pronounce "one percent, slider"
          result.setClassName("android.widget.SeekBar");
          result.setSource(view, virtualViewId);
          result.setFocusable(true);
          result.setEnabled(true);
          result.setContentDescription("1%");
          result.setBoundsInScreen(new Rect(200, 100, 300, 200));
          result.setParent(view);
          result.setVisibleToUser(true);
          if (accessibilityFocusedSemanticsNode != -1
              && accessibilityFocusedSemanticsNode == virtualViewId) {
            result.addAction(AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
          } else {
            result.addAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
          }
          result.setAccessibilityFocused(accessibilityFocusedSemanticsNode == virtualViewId);
          return result;
        } else {
          return null;
        }
      }

      @Override
      public boolean performAction(int virtualViewId, int action, Bundle arguments) {
        Log.e("A11y", "performed action " + action + " on virtualViewId "+ virtualViewId);
        switch (action) {
          case AccessibilityNodeInfo.ACTION_CLICK:
            return true;
          case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
            view.getParent().requestSendAccessibilityEvent(view, obtainAccessibilityEvent(virtualViewId, AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED));
            if (accessibilityFocusedSemanticsNode == -1) {
              view.invalidate();
            }
            accessibilityFocusedSemanticsNode = virtualViewId;
            return true;
          case AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS:
          {
            view.getParent().requestSendAccessibilityEvent(view, obtainAccessibilityEvent(virtualViewId, AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED));
            accessibilityFocusedSemanticsNode = -1;
            return true;
          }

        }
        return false;
      }

      private AccessibilityEvent obtainAccessibilityEvent(int virtualViewId, int eventType) {
        AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
        event.setPackageName(view.getContext().getPackageName());
        event.setSource(view, virtualViewId);
        return event;
      }
    };
  }
}
