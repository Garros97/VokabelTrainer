// Generated by view binder compiler. Do not edit!
package com.voctrainer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.voctrainer.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityGui3ViewStepsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView checkin;

  @NonNull
  public final ImageView imageschritt;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView7;

  private ActivityGui3ViewStepsBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView checkin, @NonNull ImageView imageschritt, @NonNull TextView textView,
      @NonNull TextView textView7) {
    this.rootView = rootView;
    this.checkin = checkin;
    this.imageschritt = imageschritt;
    this.textView = textView;
    this.textView7 = textView7;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityGui3ViewStepsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityGui3ViewStepsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_gui3_view_steps, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityGui3ViewStepsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.checkin;
      ImageView checkin = ViewBindings.findChildViewById(rootView, id);
      if (checkin == null) {
        break missingId;
      }

      id = R.id.imageschritt;
      ImageView imageschritt = ViewBindings.findChildViewById(rootView, id);
      if (imageschritt == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.textView7;
      TextView textView7 = ViewBindings.findChildViewById(rootView, id);
      if (textView7 == null) {
        break missingId;
      }

      return new ActivityGui3ViewStepsBinding((ConstraintLayout) rootView, checkin, imageschritt,
          textView, textView7);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
