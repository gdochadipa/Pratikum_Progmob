package co.ocha.pratikum_progmob.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import butterknife.OnClick;
import co.ocha.pratikum_progmob.BaseActivity;
import co.ocha.pratikum_progmob.LoginActivity;
import co.ocha.pratikum_progmob.MainActivity;
import co.ocha.pratikum_progmob.R;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    SharedPrefed sharedPrefManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @OnClick(R.id.btnLogout)void onLogout(){
        sharedPrefManager.saveSPBoolean(SharedPrefed.SP_SUDAH_LOGIN,false);
        startActivity(new Intent(getActivity(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();
    }
}