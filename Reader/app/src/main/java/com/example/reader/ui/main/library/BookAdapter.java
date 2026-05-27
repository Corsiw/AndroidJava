package com.example.reader.ui.main.library;

import android.net.Uri;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reader.R;
import com.example.reader.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books = new ArrayList<>();
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);

        void onFavoriteClick(Book book);

        void onDeleteClick(Book book);
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    public void setBooks(List<Book> newBooks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BookDiffCallback(this.books, newBooks));
        this.books = new ArrayList<>(newBooks);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.pbProgress.setProgress(book.getProgressPercentage());

        // TODO: Здесь можно добавить загрузку обложки через Glide или из filePath
        if (book.getCoverPath() != null) {
            holder.ivCover.setImageURI(Uri.parse(book.getCoverPath()));
        }

        // Read + Delete
        GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent e) {
                // ОБЯЗАТЕЛЬНО возвращаем true, иначе система не передаст нам DoubleTap
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                if (listener != null) listener.onBookClick(book);
                return true;
            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {
                if (listener != null) listener.onDeleteClick(book);
                return true;
            }
        });

        holder.itemView.setOnTouchListener((v, event) -> {
            boolean handled = gestureDetector.onTouchEvent(event);
            return handled;
        });

        // Favorite
        if (book.isFavorite()) {
            holder.ivFavorite.setImageResource(R.drawable.ic_star);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_star_outline);
        }

        if (holder.ivFavorite.getScaleX() != 1.0f) {
            holder.ivFavorite.animate().cancel();
            holder.ivFavorite.setScaleX(1.0f);
            holder.ivFavorite.setScaleY(1.0f);
        }

        holder.ivFavorite.setOnClickListener(v -> {
            boolean newFavoriteStatus = !book.isFavorite();
            book.setFavorite(newFavoriteStatus);

            updateFavoriteIcon(holder.ivFavorite, newFavoriteStatus);

            if (listener != null) {
                listener.onFavoriteClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        ImageView ivFavorite;
        TextView tvTitle, tvAuthor;
        ProgressBar pbProgress;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            pbProgress = itemView.findViewById(R.id.pbProgress);
        }
    }

    private void updateFavoriteIcon(ImageView imageView, boolean isFavorite) {
        if (isFavorite) {
            imageView.setImageResource(R.drawable.ic_star);

            imageView.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150).withEndAction(() -> {
                imageView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start();
            }).start();

        } else {
            imageView.setImageResource(R.drawable.ic_star_outline);
            imageView.setScaleX(1.0f);
            imageView.setScaleY(1.0f);
        }
    }

    private static class BookDiffCallback extends DiffUtil.Callback {
        private final List<Book> oldList;
        private final List<Book> newList;

        public BookDiffCallback(List<Book> oldList, List<Book> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldPos, int newPos) {
            return oldList.get(oldPos).getId() == newList.get(newPos).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldPos, int newPos) {
            Book oldBook = oldList.get(oldPos);
            Book newBook = newList.get(newPos);
            return oldBook.isFavorite() == newBook.isFavorite() && oldBook.getProgressPercentage() == newBook.getProgressPercentage() && oldBook.getTitle().equals(newBook.getTitle());
        }
    }
}