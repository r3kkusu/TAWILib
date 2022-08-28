package com.tawilib.app.ui.common.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tawilib.app.R;
import com.tawilib.app.data.model.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookHolder> {

    private StorageReference storageReference;
    private RequestManager requestManager;

    private Context context;
    private List<Book> books;

    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(Book book);
    }

    class BookHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_book_cover)
        ImageView imgBookCover;

        @BindView(R.id.txt_book_title)
        TextView txtTitle;

        @BindView(R.id.txt_book_author)
        TextView txtAuthor;

        @BindView(R.id.txt_book_description)
        TextView txtDescription;

        @BindView(R.id.txt_book_date)
        TextView txtDate;

        @BindView(R.id.layout_root)
        ConstraintLayout layoutRoot;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public BooksAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = books.get(position);
        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());
        holder.txtDescription.setText(book.getAbout());
        holder.txtDate.setText(book.getDate());
        holder.layoutRoot.setOnClickListener(v -> editBookItem(book));

        storageReference.child(book.getId()).getDownloadUrl().addOnSuccessListener(uri -> {
            requestManager.load(uri).into(holder.imgBookCover);
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setFirebaseStorage(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    private void editBookItem(Book book) {
        if (listener != null) {
            listener.onClick(book);
        }
    }

}
