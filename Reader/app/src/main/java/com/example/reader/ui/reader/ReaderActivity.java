package com.example.reader.ui.reader;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reader.R;
import com.example.reader.databinding.ActivityReaderBinding;
import com.example.reader.infrastructure.managers.SettingsManager;
import com.example.reader.ui.main.library.BookViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hamurcuabi.pdfviewer.PdfViewListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

public class ReaderActivity extends AppCompatActivity {

    private BookViewModel bookViewModel;
    private ActivityReaderBinding binding;

    private int bookId;
    private String filePath;
    private String format;
    private int savedPage;
    boolean isFullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Получаем данные из интента
        bookId = getIntent().getIntExtra("BOOK_ID", -1);
        filePath = getIntent().getStringExtra("FILE_PATH");
        format = getIntent().getStringExtra("FORMAT");
        savedPage = getIntent().getIntExtra("PAGE", 0);
        String title = getIntent().getStringExtra("TITLE");
        String author = getIntent().getStringExtra("AUTHOR");

        binding.readerToolbar.setTitle(title);
        binding.readerToolbar.setSubtitle(author);

        binding.readerToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        binding.readerToolbar.inflateMenu(R.menu.reader_menu);
        binding.readerToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_settings) {
                showSettingsDialog();
                return true;
            }
            return false;
        });

        // TODO: For EPUB only currently
        setSettingsFromPrefs();

        setupReader();
    }

    private void setupReader() {
        if (format == null) return;

        switch (format.toLowerCase()) {
            case "pdf":
                showPdfReader();
                break;
            case "epub":
                showEpubReader();
                break;
            case "txt":
                showTxtReader();
                break;
        }
    }

    private void showPdfReader() {
        File file = new File(filePath);

        binding.pdfView.setVisibility(View.VISIBLE);
        binding.pdfView.setPdfViewListener(new PdfViewListener() {
            private Function0<Unit> onLoad;
            private Function1<? super Throwable, Unit> onError;
            private Function2<? super Integer, ? super Integer, Unit> onPageChange;

            @Override
            public void setOnLoad(@Nullable Function0<Unit> function0) {
                this.onLoad = function0;
            }

            @Override
            public void setOnError(@Nullable Function1<? super Throwable, Unit> function1) {
                this.onError = function1;
            }

            @Override
            public void setOnPageChange(@Nullable Function2<? super Integer, ? super Integer, Unit> function2) {
                this.onPageChange = function2;
            }

            @Override
            public Function0<Unit> getOnLoad() {
                return () -> {
                    if (onLoad != null) onLoad.invoke();

                    if (savedPage > 0) {
                        binding.pdfView.post(() -> {
                            for (int i = 0; i < binding.pdfView.getChildCount(); i++) {
                                View child = binding.pdfView.getChildAt(i);
                                if (child instanceof RecyclerView) {
                                    ((RecyclerView) child).scrollToPosition(savedPage - 1);
                                    int pages = ((RecyclerView) child).getAdapter() != null ? ((RecyclerView) child).getAdapter().getItemCount() : 0;
                                    bookViewModel.updateTotalPages(bookId, pages);
                                    break;
                                }
                            }
                        });
                    }


                    return Unit.INSTANCE;
                };
            }

            @Override
            public Function2<Integer, Integer, Unit> getOnPageChange() {
                return (currentPage, totalPage) -> {
                    if (onPageChange != null) onPageChange.invoke(currentPage, totalPage);

                    // Нумерация с единицы currentPage
                    saveProgress(currentPage);
                    return Unit.INSTANCE;
                };
            }

            @Override
            public Function1<Throwable, Unit> getOnError() {
                return (error) -> {
                    if (onError != null) onError.invoke(error);
                    Log.e("PDF", "Error loading PDF", error);
                    return Unit.INSTANCE;
                };
            }
        });

        binding.pdfView.loadPdfWithFile(file);

        android.view.GestureDetector gestureDetector = new android.view.GestureDetector(this,
                new android.view.GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(android.view.MotionEvent e) {
                        toggleFullscreen(); // Переключаем полноэкранный режим
                        return true;
                    }
                });

        binding.pdfView.post(() -> {
            for (int i = 0; i < binding.pdfView.getChildCount(); i++) {
                View child = binding.pdfView.getChildAt(i);
                if (child instanceof RecyclerView) {
                    RecyclerView rv = (RecyclerView) child;
                    rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                        @Override
                        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, android.view.MotionEvent e) {
                            // Передаем событие в детектор жестов
                            gestureDetector.onTouchEvent(e);
                            return false; // Не прерываем событие, чтобы скролл продолжал работать
                        }

                        @Override
                        public void onTouchEvent(@NonNull RecyclerView rv, android.view.MotionEvent e) {
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                        }
                    });
                    break;
                }
            }
        });
    }

    private void showEpubReader() {
        if (filePath == null) return;
        
        binding.epubWebView.setVisibility(View.VISIBLE);

        android.view.GestureDetector epubGestureDetector = new android.view.GestureDetector(this,
                new android.view.GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        toggleFullscreen();
                        return true;
                    }
                });

        binding.epubWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Передаем событие детектору
                epubGestureDetector.onTouchEvent(event);

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }

                // Возвращаем false, чтобы WebView мог обрабатывать скролл и ссылки
                return false;
            }
        });

        try {
            File file = new File(filePath);
            InputStream epubInputStream = new FileInputStream(file);
            nl.siegmann.epublib.domain.Book book = new EpubReader().readEpub(epubInputStream);
            String baseUrl = "file:///" + file.getParent() + "/";

            WebSettings settings = binding.epubWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);

            StringBuilder htmlContent = new StringBuilder();

            // Перебираем все ресурсы из Spine (порядок глав)
            for (SpineReference spineReference : book.getSpine().getSpineReferences()) {
                Resource resource = spineReference.getResource();
                byte[] data = resource.getData();
                htmlContent.append(new String(data, StandardCharsets.UTF_8));
            }
            
            String initialStyle = "<style id='dynamic-style'>" +
                    "body, p, div, span, section { " +
                    "font-size: " + epubTextSize + "px !important; " +
                    "font-family: " + epubFontFamily + " !important; " +
                    "color: " + epubTextColor + " !important; " +
                    "}" +
                    "body { background-color: " + epubBgColor + " !important; " +
                    "}" +
                    "</style>";

            htmlContent.insert(0, initialStyle);

            Resource coverResource = book.getCoverImage();
            if (coverResource != null) {
                byte[] coverData = coverResource.getData();    String base64Cover = android.util.Base64.encodeToString(coverData, android.util.Base64.DEFAULT);

                Bitmap coverBitmap = BitmapFactory.decodeByteArray(coverData, 0, coverData.length);
                if (coverBitmap != null) {
                    String savedPath = saveCoverToFile(coverBitmap, bookId);
                    if (savedPath != null) {
                        bookViewModel.updateCoverPath(bookId, savedPath);
                    }
                }
            }
            
            int color = android.graphics.Color.parseColor(epubBgColor);
            binding.epubWebView.setBackgroundColor(color);
            binding.readerContainer.setBackgroundColor(color);
            
            // Важно: baseUrl нужен для того, чтобы WebView понимал, где искать картинки (если они есть)
            binding.epubWebView.loadDataWithBaseURL(
                    baseUrl,
                    htmlContent.toString(),
                    "text/html",
                    "utf-8",
                    null
            );

            binding.epubWebView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                saveEpubProgress();
            });

            binding.epubWebView.setWebViewClient(new android.webkit.WebViewClient() {
                @Override
                public void onPageFinished(android.webkit.WebView view, String url) {
                    super.onPageFinished(view, url);
                    restoreEpubProgress();
                }
            });

        } catch (
                IOException e) {
            Log.e("EpubReader", "Ошибка при чтении EPUB", e);
        }
    }

    private void showTxtReader() {

    }

    private void saveProgress(int page) {
        bookViewModel.updateReadingProgress(bookId, page);
    }

    private void saveEpubProgress() {
        int scrollY = binding.epubWebView.getScrollY();

        float scale = binding.epubWebView.getScale();

        int contentHeight = (int) (binding.epubWebView.getContentHeight() * scale);

        int totalScrollable = contentHeight - binding.epubWebView.getHeight();

        if (totalScrollable > 0) {
            int clampedScrollY = Math.min(scrollY, totalScrollable);

            float progress = (float) clampedScrollY / totalScrollable;

            int progressPercent = Math.round(progress * 100);

            bookViewModel.updateReadingProgress(bookId, progressPercent);

            binding.readerToolbar.setSubtitle(progressPercent + "% прочитано");
        } else if (contentHeight > 0) {
            bookViewModel.updateReadingProgress(bookId, 100);
        }
    }
    
    private void restoreEpubProgress() {
        if (savedPage > 0) {

            binding.epubWebView.postDelayed(() -> {
                float scale = binding.epubWebView.getScale();

                int contentHeight = (int) (binding.epubWebView.getContentHeight() * scale);

                int totalScrollable = contentHeight - binding.epubWebView.getHeight();

                if (totalScrollable > 0) {
                    int scrollTo = (int) (totalScrollable * (savedPage / 100f));

                    binding.epubWebView.scrollTo(0, scrollTo);

                    binding.readerToolbar.setSubtitle(savedPage + "% прочитано");
                }
            }, 600);
        }
    }

    public Bitmap getEpubCover(nl.siegmann.epublib.domain.Book book) {
        try {
            Resource coverResource = book.getCoverImage();
            if (coverResource != null) {
                byte[] data = coverResource.getData();
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        } catch (IOException e) {
            Log.e("EPUB", "Ошибка при чтении обложки", e);
        }
        return null;
    }

    private String saveCoverToFile(Bitmap bitmap, int bookId) {
        File directory = new File(getFilesDir(), "covers");
        if (!directory.exists()) directory.mkdirs();

        File coverFile = new File(directory, "cover_" + bookId + ".jpg");
        try (FileOutputStream out = new FileOutputStream(coverFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            return coverFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e("ReaderActivity", "Error saving cover", e);
            return null;
        }
    }
    
    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;

        if (isFullscreen) {
            binding.readerToolbar.animate()
                    .translationY(-binding.readerToolbar.getHeight())
                    .setDuration(200)
                    .withEndAction(() -> {
                        binding.readerToolbar.setVisibility(View.GONE);
                        hideSystemUI();
                    })
                    .start();

        } else {
            showSystemUI();
            binding.readerToolbar.setVisibility(View.VISIBLE);

            binding.readerToolbar.animate()
                    .translationY(0)
                    .setDuration(200)
                    .start();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        // Возвращаем только флаги макета, чтобы контент не прыгал
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private int epubTextSize = 18;
    private String epubFontFamily = "serif";
    private String epubTheme = "white";
    private String epubBgColor = "#FFFFFF";
    private String epubTextColor = "#000000";

    private void showSettingsDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_reader_settings, null);

        SeekBar sbSize = view.findViewById(R.id.sbTextSize);
        RadioGroup rgFonts = view.findViewById(R.id.rgFonts);
        RadioGroup rgThemes = view.findViewById(R.id.rgThemes);

        // Установка текущих состояний
        sbSize.setProgress(epubTextSize);
        if (epubFontFamily.equals("serif")) rgFonts.check(R.id.rbSerif);
        else rgFonts.check(R.id.rbSans);

        switch (epubTheme) {
            case "white":
                rgThemes.check(R.id.rbWhite);
                break;
            case "sepia":
                rgThemes.check(R.id.rbSepia);
                break;
            case "night":
                rgThemes.check(R.id.rbNight);
                break;
        }

        // Обработка размера
        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                epubTextSize = Math.max(12, progress);
                applyEpubSettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Обработка шрифта
        rgFonts.setOnCheckedChangeListener((group, checkedId) -> {
            epubFontFamily = (checkedId == R.id.rbSerif) ? "serif" : "sans-serif";
            applyEpubSettings();
        });

        // Обработка тем
        rgThemes.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbWhite) {
                epubTheme = "white";
            } else if (checkedId == R.id.rbSepia) {
                epubTheme = "sepia";
            } else if (checkedId == R.id.rbNight) {
                epubTheme = "night";
            }
            setColorSchemeFromTheme(epubTheme);
            applyEpubSettings();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void setSettingsFromPrefs() {
        SettingsManager sm = new SettingsManager(this);

        if (sm.isGlobalOverride()) {
            epubTextSize = sm.getGlobalTextSize();
            epubFontFamily = sm.getGlobalFont();
            epubTheme = sm.getGlobalTheme();
        } else {
            loadReaderSettings();
        }
        
        setColorSchemeFromTheme(epubTheme);
    }
    
    private void setColorSchemeFromTheme(String theme) {
        if (Objects.equals(theme, "white")) {
            epubBgColor = "#FFFFFF";
            epubTextColor = "#000000";
        } else if (Objects.equals(theme, "sepia")) {
            epubBgColor = "#F4ECD8";
            epubTextColor = "#5B4636";
        } else if (Objects.equals(theme, "night")) {
            epubBgColor = "#1A1A1A";
            epubTextColor = "#CCCCCC";
        }
    }

    private void applyEpubSettings() {
        if ("epub".equalsIgnoreCase(format)) {
            // Обновляем тег <style>, воздействуя на все текстовые элементы и фон body
            String js = "var styleTag = document.getElementById('dynamic-style');" +
                    "if (styleTag) {" +
                    "  styleTag.innerHTML = 'body, p, div, span, section { " +
                    "    font-size: " + epubTextSize + "px !important; " +
                    "    font-family: " + epubFontFamily + " !important; " +
                    "    color: " + epubTextColor + " !important; " +
                    "  } " +
                    "  body { background-color: " + epubBgColor + " !important; " +
                    "}';" +
                    "}";
            binding.epubWebView.evaluateJavascript(js, null);
            int color = android.graphics.Color.parseColor(epubBgColor);
            binding.epubWebView.setBackgroundColor(color);
            binding.readerContainer.setBackgroundColor(color);

            saveReaderSettings();
        }
    }

    private static final String PREFS_NAME = "reader_settings";
    private static final String KEY_TEXT_SIZE = "text_size";
    private static final String KEY_FONT_FAMILY = "font_family";
    private static final String KEY_THEME = "theme";

    private void saveReaderSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME + "_" +  bookId, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_TEXT_SIZE, epubTextSize);
        editor.putString(KEY_FONT_FAMILY, epubFontFamily);
        editor.putString(KEY_THEME, epubTheme);
        editor.apply();
    }

    private void loadReaderSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME + "_" +  bookId, MODE_PRIVATE);
        epubTextSize = prefs.getInt(KEY_TEXT_SIZE, 18);
        epubFontFamily = prefs.getString(KEY_FONT_FAMILY, "serif");
        epubTheme = prefs.getString(KEY_THEME, "white");
    }
}