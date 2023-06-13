package com.github.m2cyurealestate.real_estate_back.services.suggestion;

import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
public interface SuggestionsService {

    List<Estate> getSuggestions();
}
