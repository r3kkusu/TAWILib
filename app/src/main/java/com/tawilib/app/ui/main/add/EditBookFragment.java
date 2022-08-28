package com.tawilib.app.ui.main.add;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.RequestManager;
import com.soundcloud.android.crop.Crop;
import com.tawilib.app.R;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.common.FragmentNavListener;
import com.tawilib.app.ui.main.list.ListFragment;
import com.tawilib.app.util.AppUtils;
import com.tawilib.app.util.DateUtils;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;

public class EditBookFragment extends BaseFragment implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @BindView(R.id.btn_back)
    TextView txtBack;

    @BindView(R.id.txt_date)
    TextView txtDate;

    @BindView(R.id.txt_date_formatted)
    TextView txtDateFormatted;

    @BindView(R.id.img_gallery)
    ImageView btnGallery;

    @BindView(R.id.btn_save)
    Button btnSave;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private LocalDateTime localDateTime;
    private Uri croppedImageUri;

    private final ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
    private final ActivityResultLauncher<String[]> permissionsLauncher = registerForActivityResult(multiplePermissionsContract, result -> {
        Log.d(TAG, "permissions = " + result.toString());
    });

    private final ActivityResultContracts.StartActivityForResult intentContract = new ActivityResultContracts.StartActivityForResult();
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(intentContract, result -> {
        Intent intent = result.getData();
        if (intent != null && intent.getData() != null) {
            String path = intent.getData().toString();
            launchImageCropper(Uri.parse(path));
        }

    });

    private final ActivityResultContracts.StartActivityForResult cropContract = new ActivityResultContracts.StartActivityForResult();
    private final ActivityResultLauncher<Intent> cropLauncher = registerForActivityResult(cropContract, result -> {
        if (croppedImageUri != null) {
            requestManager.load(croppedImageUri).into(btnGallery);
        }
    });

    public EditBookFragment() {
        // Required empty public constructor
    }

    public EditBookFragment(FragmentNavListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtBack.setOnClickListener(v -> navigate(new ListFragment(listener)));

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        timePickerDialog = TimePickerDialog.newInstance(this, true);

        txtDate.setOnClickListener(v -> datePickerDialog.show(getParentFragmentManager(), "Date Picker Dialog"));

        btnGallery.setOnClickListener(v -> lunchGallery());
        btnSave.setOnClickListener(v -> {
            submitBook();
        });

        checkPermission();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDateTime(year, monthOfYear, dayOfMonth, 0, 0, 0);
        timePickerDialog.show(getParentFragmentManager(), "Time Picker Dialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        if (localDateTime != null) {
            setDateTime(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), hourOfDay, minute, second);
        }
    }


    private void checkPermission() {

        String[] PERMISSIONS = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        for (String permission: PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsLauncher.launch(PERMISSIONS);
            }
        }
    }

    private void setDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, int second) {
        localDateTime = DateUtils.getLocalDateTimeObject(year, monthOfYear, dayOfMonth, hourOfDay, minute, second);

        long epochDateTime = DateUtils.getUnixTimeStamp(localDateTime);
        txtDate.setText(String.valueOf(epochDateTime));

        String formattedDateTime = DateUtils.format(localDateTime, DateUtils.LOCAL_DATE_TIME_FORMAT_1);
        txtDateFormatted.setText(formattedDateTime);
    }

    private void lunchGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        galleryLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void launchImageCropper(Uri uri) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Intent cropIntent = Crop.of(uri, destination).withAspect(10, 13).getIntent(getActivity());

        croppedImageUri = destination;

        cropLauncher.launch(cropIntent);
    }

    private void submitBook() {
//        AppUtils.isValidString(getContext(), txtN)
    }
}