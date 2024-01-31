package com.devsling.fr.adapters.backend;

import com.devsling.fr.domain.port.out.CandidateApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class CandidateApiClientImpl implements CandidateApiClient {

    private final WebClient candidateWebClient ;

    //implementation of call to candidate web client
}
