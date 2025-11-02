package com.example.aahar100;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteManager {
    private List<Quote> quotes;
    private Random random;

    public QuoteManager() {
        this.quotes = new ArrayList<>();
        this.random = new Random();
        initializeQuotes();
    }

    private void initializeQuotes() {
        quotes.add(new Quote("Technology is best when it brings people together.", "Matt Mullenweg"));
        quotes.add(new Quote("The art of being wise is the art of knowing what to overlook.", "William James"));
        quotes.add(new Quote("Almost everything will work again if you unplug it for a few minutes, including you.", "Anne Lamott"));
        quotes.add(new Quote("Balance is not something you find, it's something you create.", "Jana Kingsford"));
        quotes.add(new Quote("The key is not to prioritize what's on your schedule, but to schedule your priorities.", "Stephen Covey"));
        quotes.add(new Quote("Disconnect to reconnect with what truly matters.", "Unknown"));
        quotes.add(new Quote("Life is what happens when you're busy looking at your phone.", "Unknown"));
        quotes.add(new Quote("The greatest wealth is to live content with little.", "Plato"));
        quotes.add(new Quote("Simplicity is the ultimate sophistication.", "Leonardo da Vinci"));
        quotes.add(new Quote("Your time is limited, don't waste it living someone else's life.", "Steve Jobs"));
        quotes.add(new Quote("The present moment is filled with joy and happiness. If you are attentive, you will see it.", "Thich Nhat Hanh"));
        quotes.add(new Quote("Be present in all things and thankful for all things.", "Maya Angelou"));
        quotes.add(new Quote("The ability to simplify means to eliminate the unnecessary so that the necessary may speak.", "Hans Hofmann"));
        quotes.add(new Quote("Don't let technology control you. Take control of your technology.", "Unknown"));
        quotes.add(new Quote("The more you know yourself, the more clarity there is.", "Jiddu Krishnamurti"));
        quotes.add(new Quote("Mindfulness isn't difficult, we just need to remember to do it.", "Sharon Salzberg"));
        quotes.add(new Quote("In a world of constant connection, true luxury is disconnection.", "Unknown"));
        quotes.add(new Quote("The best time to plant a tree was 20 years ago. The second best time is now.", "Chinese Proverb"));
        quotes.add(new Quote("You can't stop the waves, but you can learn to surf.", "Jon Kabat-Zinn"));
        quotes.add(new Quote("The secret of change is to focus all of your energy not on fighting the old, but on building the new.", "Socrates"));
        quotes.add(new Quote("Digital detox: Because sometimes you need to disconnect to reconnect.", "Unknown"));
        quotes.add(new Quote("The quieter you become, the more you can hear.", "Ram Dass"));
        quotes.add(new Quote("Life is really simple, but we insist on making it complicated.", "Confucius"));
        quotes.add(new Quote("Happiness is not a matter of intensity but of balance and order.", "Thomas Merton"));
        quotes.add(new Quote("The greatest step towards a life of simplicity is to learn to let go.", "Steve Maraboli"));
        quotes.add(new Quote("Your phone is a tool, not a lifestyle.", "Unknown"));
        quotes.add(new Quote("Time you enjoy wasting is not wasted time.", "Marthe Troly-Curtin"));
        quotes.add(new Quote("The only way to do great work is to love what you do.", "Steve Jobs"));
        quotes.add(new Quote("Be where you are, not where you think you should be.", "Unknown"));
        quotes.add(new Quote("Digital wellness is about creating healthy habits with technology.", "Unknown"));
    }

    public Quote getRandomQuote() {
        if (quotes.isEmpty()) {
            return new Quote("Stay balanced, stay mindful.", "DigitalWell");
        }
        int index = random.nextInt(quotes.size());
        return quotes.get(index);
    }

    public Quote getDailyQuote() {
        long daysSinceEpoch = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
        int index = (int) (daysSinceEpoch % quotes.size());
        return quotes.get(index);
    }

    public List<Quote> getAllQuotes() {
        return new ArrayList<>(quotes);
    }
}
