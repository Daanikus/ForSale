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

        if (p.getCash() == 0) {
            System.out.println(p.getName() + " is out of money!");
        }

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
        else if (best.getQuality() > 25) bid += 2;
        else bid = 0;
        //return bid < p.getCash() ? bid : p.getCash() - 3;
        return bid;
    }

    @Override
    public Card chooseCard(PlayerRecord p, SaleState s) {
        Integer high = 0, low = 99;
        List<Integer> cheques = s.getChequesAvailable();
        for (Integer i : cheques) {
            if (i > high) high = i;
            if (i < low) low = i;
        }
        
        if (high == 15 
            && haveValue(p.getCards(), 30)
            && !s.getChequesRemaining().contains(15)) 
            return getCard(p, 30);
        if (high == 14 
            && haveValue(p.getCards(), 29)
            && !s.getChequesRemaining().contains(14)) 
            return getCard(p, 29);
        if (high == 14 
            && haveValue(p.getCards(), 28)
            && s.getChequesRemaining().contains(14)) 
            return getCard(p, 28);
        if (!s.getChequesRemaining().contains(0) 
            && high < 10) return getWorst(p);
        if (high - low < 5 && low > 9) return getBest(p);
        if (high - low < 5 && low < 7) return getWorst(p);

        return getMedian(p);
    }

    public Card getCard(PlayerRecord p, int value) {
        for (Card c : p.getCards()) {
            if (c.getQuality() == value) return c;
        }
        // Shouldn't happen. Always call have card first.
        return null;
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
        System.out.println();
        for (Card c : bottom) {
            if (c.getQuality() > 8) {
                return false;
            }
        }
        for (Card c : top) {
            if (c.getQuality() < 10) {
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

    public Boolean haveValue(List<Card> myCards, int value) {
        for (Card c : myCards) {
            if (c.getQuality() == value) return true;
        }
        return false;
    }

}