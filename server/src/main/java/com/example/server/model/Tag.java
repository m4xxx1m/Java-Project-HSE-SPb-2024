package com.example.server.model;

import java.util.*;

public class Tag {
    private static final List<String> tagNames = new ArrayList<>(Arrays.asList("C++", "Python", "Java",
            "JavaScript", "Go", "Yandex",
            "Tinkoff", "SberSeasons"));

    private static final Map<String, Integer> ids = new HashMap<>();

    static {
        for (int i = 0; i < tagNames.size(); i++) {
            ids.put(tagNames.get(i), i);
        }
    }

    static public String getTagName(int index) {
        return tagNames.get(index);
    }

    static public int getTagId(String tagName) {
        return ids.get(tagName);
    }

    static public List<String> getAllTags() {
        return tagNames;
    }

    static public List<Integer> tagsToTagIds(String tags) {
        List<Integer> tagIds = new ArrayList<>();
        for (int i = 0; i < tags.length(); i++) {
            if (tags.charAt(i) != '0') {
                tagIds.add(i);
            }
        }
        return tagIds;
    }

    static public String tagNamesToTags(List<String> tagNames) {
        Set<Integer> tagIds = new HashSet<>();
        for (String tagName : tagNames) {
            tagIds.add(getTagId(tagName));
        }
        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < tagNames.size(); i++) {
            if (tagIds.contains(i)) {
                tags.append('1');
            } else {
                tags.append('0');
            }
        }
        return tags.toString();
    }

}
