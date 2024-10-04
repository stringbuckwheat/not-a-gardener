package xyz.notagardener.gardener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.notagardener.gardener.dto.UserFeedResponse;
import xyz.notagardener.gardener.repository.FeedRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;

    public UserFeedResponse getFeed(String username) {
        return feedRepository.getFeed(username);
    }
}
