package group89.photos.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ConfirmationFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String msg = bundle.getString("message");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(msg);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Bundle resBundle = new Bundle();
            resBundle.putString("result", "OK");

            getParentFragmentManager().setFragmentResult("OK", resBundle);
        });

        builder.setNegativeButton("No", (dialog, which) -> {

        });

        return builder.create();
    }
}
