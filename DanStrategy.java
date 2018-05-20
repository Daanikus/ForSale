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
        int bid = a.getCurrentBid();
        Card best = a.getCardsInAuction().get(0);
        Card worst =a.getCardsInAuction().get(1);
        for (Card c : a.getCardsInAuction()) {
            if (c.getQuality() > best.getQuality()) best = c;
            if (c.getQuality() < worst.getQuality()) worst = c;
            
        }
        if (worst.getQuality() > 5 && cardsAuctioned < 3) bid += 0;
        else if (best.getQuality() > 15 && cardsAuctioned > 3) bid += 1;
        else if (best.getQuality() > 20 && cardsAuctioned > 3) bid += 2;
        else bid = 0;
        return bid > 6 ? 6 : bid;
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
        else c = getAvg(p);
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

    public Card getAvg(PlayerRecord p) {
        List<Card> cards = p.getCards();
        Collections.sort(cards, new Comparator<Card>() {
                @Override
                public int compare(Card o1, Card o2) {
                    return o2.getQuality() - o1.getQuality();
                }
            });
        return cards.get(cards.size()/2);
    }

}