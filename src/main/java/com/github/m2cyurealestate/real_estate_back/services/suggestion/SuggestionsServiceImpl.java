package com.github.m2cyurealestate.real_estate_back.services.suggestion;

import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class SuggestionsServiceImpl implements SuggestionsService {

    @Override
    public List<Estate> getSuggestions() {
        throw new NotImplementedException();
    }
}
