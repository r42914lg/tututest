package com.r42914lg.tutu.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.r42914lg.tutu.R;
import com.r42914lg.tutu.databinding.ActivityMainBinding;
import com.r42914lg.tutu.model.TerminateDialogText;
import com.r42914lg.tutu.model.TuTuViewModel;

public class MainActivity extends AppCompatActivity implements ICoreView {
    public static final String TAG = "LG> MainActivity";

    AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private View progressOverlay;
    private TextView networkStatus;
    private TuTuViewModel tuTuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get reference to ViewModel, instantiate presenter
        tuTuViewModel = new ViewModelProvider(this).get(TuTuViewModel.class);
        new CorePresenter(tuTuViewModel, this).initCoreView(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        progressOverlay = findViewById(R.id.progress_overlay);
        networkStatus = findViewById(R.id.status);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tuTuViewModel.requestFeedUpdate(true);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void showNetworkStatus(String text) {
        networkStatus.setText(text);
    }

    @Override
    public void showFabIcon(boolean flag) {
        if (binding == null) {
            return;
        }
        binding.fab.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startProgressOverlay() {
        animateView(progressOverlay, View.VISIBLE, 0.6f);
    }

    @Override
    public void stopProgressOverlay() {
        animateView(progressOverlay, View.GONE, 0);
    }

    @Override
    public void showTerminateDialog(TerminateDialogText terminateDialogText) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(terminateDialogText.getTitle());
        dialog.setMessage(terminateDialogText.getText());
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                dialog.cancel();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void animateView(final View view, final int toVisibility, float toAlpha) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(0);
        }

        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(200)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
    }
}