package ru.job4j.pooh;

import java.sql.SQLOutput;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic =
            new ConcurrentHashMap<>();
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String EMPTY_STATUS = "204";
    private static final String OK_STATUS = "200";
    private static final String NOT_IMPLEMENTED = "501";

    @Override
    public Resp process(Req req) {
        String text = "";
        String status = NOT_IMPLEMENTED;
        var topicVal = topic.get(req.getSourceName());
        if (POST.equals(req.httpRequestType())) {
            if (topicVal == null) {
                status = EMPTY_STATUS;
            } else {
                for (ConcurrentLinkedQueue<String> value : topicVal.values()) {
                    value.add(req.getParam());
                }
                status = OK_STATUS;
            }
        } else if (GET.equals(req.httpRequestType())) {
            topic.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            topic.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            text = topic.get(req.getSourceName()).get(req.getParam()).poll();
            if (text == null) {
                text = "";
                status = EMPTY_STATUS;
            } else {
                status = OK_STATUS;
            }
        }
        return new Resp(text, status);
    }
}
