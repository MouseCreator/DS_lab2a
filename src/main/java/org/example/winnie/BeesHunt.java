package org.example.winnie;

public interface BeesHunt {
    /**
     *
     * @param forest - forest to search for the bear
     * @return id of an area, where the bear was found or -1 if there is no bear in the forest
     */
    int find(Forest forest);
}
