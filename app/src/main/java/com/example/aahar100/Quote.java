package com.example.aahar100;

public class Quote {
    private String text;
    private String author;

    public Quote() {
    }

    public Quote(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFullQuote() {
        if (author != null && !author.isEmpty()) {
            return "\"" + text + "\"\n- " + author;
        }
        return "\"" + text + "\"";
    }
}
