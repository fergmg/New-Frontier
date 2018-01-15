package com.versionone.apiclient.filters;

import java.util.ArrayList;
import java.util.List;

import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.services.TextBuilder;

/**
 * Base class for grouping filter terms (i.e and, or)
 *
 */
public abstract class GroupFilterTerm implements IFilterTerm {
    List<IFilterTerm> terms = new ArrayList<IFilterTerm>();

    /**
     * @return Has this grouping terms or not
     */
    public boolean hasTerm() {
        return terms.size() > 0;
    }

    public String getToken() throws APIException {
        return makeToken(true);
    }

    public String getShortToken() throws APIException {
        return makeToken(false);
    }

    private String makeToken(Boolean full) throws APIException {
        ArrayList<String> tokens = new ArrayList<String>();

        for (IFilterTerm term : terms) {
            String token = full ? term.getToken() : term.getShortToken();

            if (token != null) {
                tokens.add(token);
            }
        }
        
        if (tokens.isEmpty()) {
            return null;
        }
        
        if (tokens.size() == 1) {
            return tokens.get(0);
        }

        return "(" + TextBuilder.join(tokens.toArray(), getTokenSeperator()) + ")";
    }

    abstract String getTokenSeperator();

    protected GroupFilterTerm(IFilterTerm[] terms) {
        for (IFilterTerm term : terms) {
            if (term != null) {
                this.terms.add(term);
            }
        }
    }

    /**
     * Create an AndFilterTerm from the array of terms
     *
     * @param terms - IFilterTerm data
     * @return GroupFilterTerm
     */
    public GroupFilterTerm and(IFilterTerm... terms) {
        AndFilterTerm term = new AndFilterTerm(terms);
        this.terms.add(term);
        return term;
    }

    /**
     * Create an OrFilterTerm from the array of terms
     *
     * @param terms - IFilterTerm data
     * @return GroupFilterTerm
     */
    public GroupFilterTerm or(IFilterTerm... terms) {
        OrFilterTerm term = new OrFilterTerm(terms);
        this.terms.add(term);
        return term;
    }

    /**
     * Create a filter term from the attribute definition
     *
     * @param def - IAttributeDefinition data
     * @return FilterTerm
     */
    public FilterTerm Term(IAttributeDefinition def) {
        FilterTerm term = new FilterTerm(def);
        terms.add(term);
        return term;
    }
}
