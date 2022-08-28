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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.RequestManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.StorageReference;
import com.soundcloud.android.crop.Crop;
import com.tawilib.app.R;
import com.tawilib.app.data.model.Book;
import com.tawilib.app.ui.BaseActivity;
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
import java.util.UUID;

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

    @BindView(R.id.btn_delete)
    TextView txtDelete;

    @BindView(R.id.txt_title)
    TextView txtTitle;

    @BindView(R.id.txt_author)
    TextView txtAuthor;

    @BindView(R.id.txt_about)
    TextView txtAbout;

    @BindView(R.id.txt_date)
    TextView txtDate;

    @BindView(R.id.txt_date_formatted)
    TextView txtDateFormatted;

    @BindView(R.id.img_gallery)
    ImageView btnGallery;

    @BindView(R.id.btn_save)
    Button btnSave;

    @BindView(R.id.layout_root)
    FrameLayout layoutRoot;

    @BindView(R.id.layout_load)
    FrameLayout layoutLoad;

    private EditBookViewModel viewModel;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private LocalDateTime localDateTime;
    private String croppedImageUri;

    private boolean isUploadedSuccessful = false;
    private boolean isInUpdateMode = false;
//    private boolean isOnFirstLoad = true;

    private Book defaultBook;

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
            Uri uriSource = Uri.fromFile(new File(getActivity().getCacheDir(), croppedImageUri));
            requestManager.load(uriSource).into(btnGallery);
        }
    });

    public EditBookFragment() {
        // Required empty public constructor
    }

    public EditBookFragment(FragmentNavListener listener) {
        this.listener = listener;
    }

    public EditBookFragment(FragmentNavListener listener, Book defaultBook) {
        this.listener = listener;
        this.defaultBook = defaultBook;
        this.isInUpdateMode = true;
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

        viewModel = new ViewModelProvider(this, providerFactory).get(EditBookViewModel.class);
        viewModel.getLiveBookData().observe(getActivity(), resource -> {
            layoutLoad.setVisibility(View.GONE);
            switch (resource.status) {
                case SUCCESS: {
                    isUploadedSuccessful = true;
                    int stringResource = R.string.book_successfully_added;
                    if (resource.data == null) {
                        stringResource = R.string.book_successfully_deleted;
                    } else if (isInUpdateMode) {
                        stringResource = R.string.book_successfully_updated;
                    }
                    AppUtils.toastMessage((BaseActivity) getActivity(),
                            getString(stringResource),
                            Toast.LENGTH_LONG);
                    navigate(new ListFragment(listener));
                    break;
                }

                case ERROR: {
                    isUploadedSuccessful = true;
                    AppUtils.SnackbarMessage(layoutRoot,
                            resource.message,
                            Snackbar.LENGTH_LONG,
                            getString(R.string.retry), v -> submitBook());
                    break;
                }

                case LOADING: {
                    layoutLoad.setVisibility(View.VISIBLE);
                    break;
                }
            }
        });

        if (defaultBook != null) {
            prepFieldsDefaultValues();
        }

        if(isInUpdateMode) {
            txtDelete.setVisibility(View.VISIBLE);
            txtDelete.setOnClickListener(v -> {
                viewModel.delete(defaultBook);
            });
        }

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

    @Override
    protected void navigate(BaseFragment fragment) {

        if (isUploadedSuccessful) {
            super.navigate(fragment);
            return;
        }

        boolean isNotEmpty = AppUtils.isNotEmpty(
                txtTitle,
                txtAuthor,
                txtAbout,
                txtDate
        );

        if (isNotEmpty || croppedImageUri != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string._continue))
                    .setMessage(getString(R.string.discard_all_changes))
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        super.navigate(fragment);
                    })
                    .setNegativeButton(R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            super.navigate(fragment);
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

        croppedImageUri = UUID.randomUUID().toString();
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), croppedImageUri));
        Intent cropIntent = Crop.of(uri, destination).withAspect(10, 13).getIntent(getActivity());

        cropLauncher.launch(cropIntent);
    }

    private boolean areAllFieldsFilled() {
        boolean isValid = AppUtils.isValidString(getContext(), txtTitle)
                && AppUtils.isValidString(getContext(), txtAuthor)
                && AppUtils.isValidString(getContext(), txtAbout)
                && AppUtils.isValidString(getContext(), txtDate);

        if (!isValid) {
            AppUtils.SnackbarMessage(
                    layoutRoot,
                    getString(R.string.please_fill_up_missing_fields),
                    Snackbar.LENGTH_LONG,
                    getString(R.string.retry), v -> submitBook());
        } else if (croppedImageUri == null) {
            isValid = false;
            AppUtils.SnackbarMessage(
                    layoutRoot,
                    getString(R.string.please_add_book_cover),
                    Snackbar.LENGTH_LONG,
                    getString(R.string.retry), v -> submitBook());
        }

        return isValid;
    }

    private void prepFieldsDefaultValues() {

        if (defaultBook != null) {

            txtTitle.setText(defaultBook.getTitle());
            txtAuthor.setText(defaultBook.getAuthor());
            txtAbout.setText(defaultBook.getAbout());

            long epochDateTime = Long.parseLong(defaultBook.getDate());
            txtDate.setText(String.valueOf(epochDateTime));

            String formattedDateTime = DateUtils.format(epochDateTime, DateUtils.LOCAL_DATE_TIME_FORMAT_1);
            txtDateFormatted.setText(formattedDateTime);

            croppedImageUri = defaultBook.getCover();
            StorageReference storageReference = viewModel.getStorageReference();
            storageReference.child(defaultBook.getId()).getDownloadUrl().addOnSuccessListener(uri -> {
                requestManager.load(uri).into(btnGallery);
            });
        }

    }

    private void submitBook() {
        if (areAllFieldsFilled()) {

            Book book = new Book(
                txtTitle.getText().toString(),
                txtAuthor.getText().toString(),
                txtAbout.getText().toString(),
                txtDate.getText().toString(),
                croppedImageUri
            );

            if (defaultBook != null)
                book.setId(defaultBook.getId());

            File file = new File(getActivity().getCacheDir(), croppedImageUri);
            Uri uriSource = null;
            if (file.exists())
                uriSource = Uri.fromFile(file);

            if (isInUpdateMode) {
                viewModel.update(book, uriSource);
            } else {
                viewModel.addBook(book, uriSource);
            }
        }
    }
}