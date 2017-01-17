package com.example.ebooktextfinder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ebooktextfinder.R;
import com.example.ebooktextfinder.models.SearchableBook;
import com.example.ebooktextfinder.models.Sentence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {
    private static final String EBOOK_FILE_PATH = "/sdcard/Download/ebook_test.epub";
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private SearchableBook book;
    private static final String WORDS_TO_FIND = "I could tools";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        book = openBook(EBOOK_FILE_PATH);
        String[] wordsToFind = WORDS_TO_FIND.split(" ");
        Sentence foundSentence = book.findSentence(wordsToFind);

        if(foundSentence != null) {
            Log.i(LOG_TAG, "FOUND sentence : " + foundSentence.toString());
            Log.i(LOG_TAG, "Start index : " + foundSentence.getStartIndex());
            Log.i(LOG_TAG, "Length : " + foundSentence.toString().length());
        }
    }

    private SearchableBook openBook(String ebookFilePath) {
        EpubReader epubReader = new EpubReader();
        FileInputStream fileInputStream = null;
        SearchableBook book = null;

        try {
            fileInputStream = new FileInputStream(ebookFilePath);
            book = new SearchableBook(epubReader.readEpub(fileInputStream));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return book;
    }
}
