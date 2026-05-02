package tn.esprit.social_interaction_service.Communications.Services;

import tn.esprit.social_interaction_service.Communications.Entities.Topic;

import java.util.List;

public interface ITopicService {
    Topic addTopic(Topic topic);
    Topic updateTopic(Topic topic);
    void deleteTopic(long id);
    Topic getTopic(long id);
    List<Topic> getAllTopics();
}
