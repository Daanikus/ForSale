/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forsale;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

/**
 *
 * @author MichaelAlbert
 */
public class DanStrategy implements Strategy {

    private int cardsAuctioned = 0;

    public DanStrategy() {
        super();
    }


    @Override
    public int bid(PlayerRecord p, AuctionState a) {
        Boolean firstToBid = a.getCurrentBid() == 0;
        Boolean highLow;
        int numPlayers = a.getPlayers().size();
        int round = a.getCardsInDeck().size() / numPlayers;
        int bid = a.getCurrentBid();
        int numLowCardsToGet = 6 % numPlayers;
        List<Card> myCards = p.getCards();
        int lowCardsGot = lowCardCount(myCards);
        int remainingCards = a.getCardsInAuction().size();
        List<Card> sortedCards = sortCards(a.getCardsInAuction());  

        for (Card c : myCards) if (c.getQuality() <= 8) lowCardsGot++; 

        if (lowCardsGot < numLowCardsToGet || highLowSplit(sortedCards)) {
            return bid + 1;
        }
        // We won last round, so bid first
        if (firstToBid) {
            return bid + 1;
        }
        Card best = a.getCardsInAuction().get(0);
        Card worst = a.getCardsInAuction().get(1);
        for (Card c : a.getCardsInAuction()) {
            if (c.getQuality() > best.getQuality()) best = c;
            if (c.getQuality() < worst.getQuality()) worst = c;
            
        }

        if (best.getQuality() < 10) {
            bid = bid < 2 ? bid++ : 2;
        }



        if (worst.getQuality() > 5 && cardsAuctioned < 3) bid += 0;
        else if (best.getQuality() > 15 && best.getQuality() < 20 && cardsAuctioned > 3) bid += 1;
        else if (best.getQuality() > 20 && cardsAuctioned > 3) bid += 2;
        else if (best.getQuality() > 20) bid += 3;
        else bid = 0;
        return bid;
    }

    @Override
    public Card chooseCard(PlayerRecord p, SaleState s) {
        Integer best = 0, worst = 99;
        int average = 0;
        int averageRemaining = 0;
        Card c;
        int cardCount = s.getChequesAvailable().size();
        for (Integer i : s.getChequesRemaining()) {
            averageRemaining += i;
        }
        averageRemaining = averageRemaining / cardCount;
        for (Integer i : s.getChequesAvailable()) {
            if (i > best) best = i;
            if (i < worst) worst = i;
            average += i;
        }
        average = average / cardCount;
        if (worst > 5) c = getWorst(p);
        else if (average > averageRemaining) c = getBest(p);
        else c = getMedian(p);
        return c;
    }

    public Card getWorst(PlayerRecord p) {
        int value = 99;
        Card worst = null;
        for (Card c : p.getCards()) {
            if (c.getQuality() < value) {
                value = c.getQuality();
                worst = c;
            }
        }
        return worst;
    }

    public Card getBest(PlayerRecord p) {
        int value = 0;
        Card best = null;
        for (Card c : p.getCards()) {
            if (c.getQuality() > value) {
                value = c.getQuality();
                best = c;
            }
        }
        return best;
    }

    public Card getMedian(PlayerRecord p) {
        List<Card> cards = p.getCards();
        cards = sortCards(cards);
        return cards.get(cards.size()/2);
    }

    public List<Card> sortCards(List<Card> cards) {
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o2.getQuality() - o1.getQuality();
            }
        });
        return cards;
    }

    public Boolean highLowSplit(List<Card> cards) {
        List<Card> top = cards.subList(cards.size() / 2, cards.size());
        List<Card> bottom = cards.subList(0, cards.size() / 2);
        System.out.println("High Low split:");
        for (Card c : cards) {
            System.out.print(c.getQuality() + ", ");
        }
        System.out.println();
        for (Card c : bottom) {
            if (c.getQuality() > 8) {
                return false;
            }
        }
        for (Card c : top) {
            if (c.getQuality() < 12) {
                return false;
            }
        }
        System.out.println("Split true");
        return true;
    }

    public int lowCardCount(List<Card> myCards) {
        int result = 0;
        for (Card c : myCards) {
            if (c.getQuality() <= 8) {
                result++;
            }
        }
       
        return result;
    }

}