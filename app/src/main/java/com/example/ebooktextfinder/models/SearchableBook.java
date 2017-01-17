package com.example.ebooktextfinder.models;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;

/**
 * Created by laurent on 1/16/17.
 */

public class SearchableBook {
    private static String LOG_TAG = SearchableBook.class.getSimpleName();
    private Book mBook;

    public SearchableBook(Book book) {
        this.mBook = book;
    }

    // Find a sentence containing the words in parameters
    public Sentence findSentence(String[] wordsToFind) {
        InputStream inputStream = null;
        Sentence foundSentence = null;

        Spine spine = new Spine(mBook.getTableOfContents());

        // Get book content
        StringBuilder sb = new StringBuilder();
        for (SpineReference bookSection : spine.getSpineReferences()) {
            Resource res = bookSection.getResource();

            try {
                inputStream = res.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }

                Section section = new Section(sb.toString());
                int currentOccurences;
                int lastOccurences = 0;

                for(Sentence s:section.getSentences()) {
                    ArrayList<Word> sentenceWords = s.getWords();
                    currentOccurences = 0;
                    for(Word w:sentenceWords) {
                        currentOccurences += w.founds(wordsToFind).size();
                    }
                    if(currentOccurences > lastOccurences) {
                        lastOccurences = currentOccurences;
                        foundSentence = s;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return foundSentence;
    }

}
