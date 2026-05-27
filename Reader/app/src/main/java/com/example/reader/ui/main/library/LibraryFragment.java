package com.example.reader.ui.main.library;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reader.R;
import com.example.reader.model.Book;
import com.example.reader.ui.reader.ReaderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.epub.EpubReader;

public class LibraryFragment extends Fragment {

    private BookViewModel bookViewModel;
    private BookAdapter adapter;

    private FloatingActionButton button;

    private final ActivityResultLauncher<String[]> filePickerLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
        if (uri != null) {
            processSelectedFile(uri);
        }
    });

    public LibraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnSort).setOnClickListener(this::showSortMenu);
        view.findViewById(R.id.btnFilter).setOnClickListener(this::showFilterMenu);
        
        RecyclerView recyclerView = view.findViewById(R.id.rvLibrary);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Сетка в 2 колонки

        adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        bookViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            adapter.setBooks(books);
        });

        adapter.setOnBookClickListener(new BookAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                openBook(book);
            }

            @Override
            public void onFavoriteClick(Book book) {
                bookViewModel.update(book);
            }

            @Override
            public void onDeleteClick(Book book) {
                new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Удаление книги")
                        .setMessage("Вы уверены, что хотите удалить книгу \"" + book.getTitle() + "\"?")
                        .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Удалить", (dialog, which) -> {
                            bookViewModel.delete(book);

                            java.io.File file = new java.io.File(book.getFilePath());
                            if (file.exists()) {
                                file.delete();
                            }
                        })
                        .show();
            }
        });

        button = view.findViewById(R.id.fabAddBook);
        button.setOnClickListener(v -> {
            // Указываем разрешенные MIME-типы
            String[] mimeTypes = {"application/pdf", "application/epub+zip"};
            filePickerLauncher.launch(mimeTypes);
        });
    }

    private void openBook(Book book) {
        Intent intent = new Intent(requireContext(), ReaderActivity.class);
        intent.putExtra("BOOK_ID", book.getId());
        intent.putExtra("FILE_PATH", book.getFilePath());
        intent.putExtra("FORMAT", book.getFormat());
        intent.putExtra("PAGE", book.getCurrentPage());
        intent.putExtra("TITLE", book.getTitle());
        intent.putExtra("AUTHOR", book.getAuthor());
        startActivity(intent);
    }

    private void processSelectedFile(Uri uri) {
        String fileName = getFileName(uri);
        String title = fileName;
        String author = "Неизвестен";
        String extension = "";
        int totalPages = 0;

        String internalPath = saveFileToInternalStorage(uri, fileName);

        if (internalPath == null) {
            Log.e("LibraryFragment", "Failed to copy file to internal storage");
            return; // Если не удалось скопировать, не добавляем в базу
        }

        String mimeType = requireContext().getContentResolver().getType(uri);

        if (mimeType != null) {
            if (mimeType.equals("application/pdf")) {
                extension = "pdf";
            } else if (mimeType.equals("application/epub+zip") || fileName.endsWith(".epub")) {
                extension = "epub";

                // Читаем метаданные уже из скопированного локального файла
                try (InputStream is = new java.io.FileInputStream(internalPath)) {
                    nl.siegmann.epublib.domain.Book epubBook = new EpubReader().readEpub(is);

                    if (epubBook.getTitle() != null && !epubBook.getTitle().isEmpty()) {
                        title = epubBook.getTitle();
                    }

                    if (!epubBook.getMetadata().getAuthors().isEmpty()) {
                        Author epubAuthor = epubBook.getMetadata().getAuthors().get(0);
                        author = epubAuthor.getFirstname() + " " + epubAuthor.getLastname();
                    }

                    // WebView 0-100% scroll
                    totalPages = 100;
                } catch (Exception e) {
                    Log.e("EpubReader", "Error reading Epub metadata", e);
                }
            }
        }
        
        Book newBook = new Book(title, author, internalPath, extension, totalPages);
        bookViewModel.insert(newBook);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) result = cursor.getString(index);
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) result = result.substring(cut + 1);
        }
        return result;
    }

    private String saveFileToInternalStorage(Uri uri, String fileName) {
        // Создаем папку "books" внутри внутреннего хранилища приложения
        java.io.File folder = new java.io.File(requireContext().getFilesDir(), "books");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Чтобы избежать конфликтов имен, добавляем префикс времени
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        java.io.File destFile = new java.io.File(folder, uniqueFileName);

        try (InputStream is = requireContext().getContentResolver().openInputStream(uri); java.io.OutputStream os = new java.io.FileOutputStream(destFile)) {

            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            return destFile.getAbsolutePath(); // Возвращаем путь к новому файлу

        } catch (java.io.IOException e) {
            Log.e("SaveFile", "Error copying file: " + e.getMessage());
            return null;
        }
    }
    private void showSortMenu(View v) {
        PopupMenu popup = new PopupMenu(requireContext(), v);
        popup.getMenu().add("По названию (А-Я)");
        popup.getMenu().add("По дате добавления");
        popup.getMenu().add("По умолчанию");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            if (title.equals("По названию (А-Я)")) {
                bookViewModel.setSort(BookViewModel.SortType.TITLE);
            } else if (title.equals("По дате добавления")) {
                bookViewModel.setSort(BookViewModel.SortType.DATE);
            } else {
                bookViewModel.setSort(BookViewModel.SortType.DEFAULT);
            }
            return true;
        });
        popup.show();
    }

    private void showFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(requireContext(), v);
        popup.getMenu().add("Все книги");
        popup.getMenu().add("Только Избранное");
        popup.getMenu().add("Недавние (Прогресс > 0)");
        popup.getMenu().add("Формат: PDF");
        popup.getMenu().add("Формат: EPUB");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            switch (title) {
                case "Только Избранное":
                    bookViewModel.setFilter(BookViewModel.FilterType.FAVORITE);
                    break;
                case "Недавние (Прогресс > 0)":
                    bookViewModel.setFilter(BookViewModel.FilterType.RECENT);
                    break;
                case "Формат: PDF":
                    bookViewModel.setFilter(BookViewModel.FilterType.PDF);
                    break;
                case "Формат: EPUB":
                    bookViewModel.setFilter(BookViewModel.FilterType.EPUB);
                    break;
                default:
                    bookViewModel.setFilter(BookViewModel.FilterType.ALL);
                    break;
            }
            return true;
        });
        popup.show();
    }
}