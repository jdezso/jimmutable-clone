package org.kane.base.examples.card;

import java.util.Collection;
import java.util.Collections;

import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.immutability.decks.StandardImmutableListDeck;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("card-deck")
final public class Deck extends StandardImmutableListDeck<Deck, Card>
{
    private FieldList<Card> cards = new FieldArrayList<>();
    
    public Deck(Builder builder)
    {
    }
    
    public Deck()
    {
        this(Collections.emptyList());
    }
    
    public Deck(Collection<Card> cards)
    {
        super();
        
        if (null != cards)
        {
            this.cards.addAll(cards); 
        }
        
        complete();
    }
    
    @Override
    public FieldList<Card> getSimpleContents()
    {
        return cards;
    }
    
    @Override
    public void normalize()
    {
    }

    @Override
    public void validate() 
    {
        Validator.containsNoNulls(cards);
        Validator.containsOnlyInstancesOf(Card.class, cards);
    }
    
    @Override
    public Builder getBuilder()
    {
        return new Builder(this);
    }
    
    
    final static public class Builder extends StandardImmutableListDeck.Builder<Deck, Card>
    {
        public Builder()
        {
            super();
            under_construction = new Deck(this);
        }
        
        public Builder(Deck starting_point)
        {
            super(starting_point);
        }
    }
    
    
    static public Deck createDefaultDeck(boolean include_jokers, boolean shuffle)
    {
        Builder deck_builder = new Builder();
        
        // 1) Add all the cards
        for (Suit suit : Suit.values())
        {
            if (Suit.UNKNOWN == suit) continue;
            
            if (Suit.JOKER == suit)
            {
                if (! include_jokers) continue;
                
                deck_builder.getSimpleContents().add(new Card(suit, Value.JOKER));
                deck_builder.getSimpleContents().add(new Card(suit, Value.JOKER));
            }
            
            for (Value value : Value.values())
            {
                if (Value.UNKNOWN == value) continue;
                if (Value.JOKER == value) continue;
                
                deck_builder.getSimpleContents().add(new Card(suit, value));
            }
        }
        
        // 2) Shuffle
        if (shuffle)
        {
            Collections.shuffle(deck_builder.getSimpleContents());
        }
        
        // 3) Serve
        return deck_builder.create();
    }
}
