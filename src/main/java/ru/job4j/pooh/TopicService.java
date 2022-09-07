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

    @Override
    public Resp process(Req req) {
        String text = "";
        String status = null;
        if (POST.equals(req.httpRequestType())) {
            if (topic.get(req.getSourceName()) == null) {
                status = EMPTY_STATUS;
            } else {
                for (ConcurrentLinkedQueue<String> value : topic.get(req.getSourceName()).values()) {
                    value.add(req.getParam());
                }
                status = OK_STATUS;
            }
        } else if (GET.equals(req.httpRequestType())) {
            if (topic.get(req.getSourceName()) == null
                    || (topic.get(req.getSourceName()).get(req.getParam()) == null)) {
                topic.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
                topic.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
                status = EMPTY_STATUS;

            } else {
                text = topic.get(req.getSourceName()).get(req.getParam()).poll();
                status = OK_STATUS;
            }
        }
        return new Resp(text, status);
    }
}
